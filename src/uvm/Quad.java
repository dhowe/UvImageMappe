package uvm;

import java.awt.geom.Line2D;
import java.util.*;

import org.apache.commons.exec.*;

import processing.core.*;

public class Quad {

	public UvImage image;
	public float[] points, bounds;

	protected PImage warped;
	protected PApplet parent;
	protected int id;
	
	static boolean testOnlyOne = false;
	static int idx = 0;
	
	public Quad(PApplet p, float... points) {

		this.id = idx++;

		this.parent = p;
		this.points = points;
		fixOrdering();
		this.bounds = bounds();
	}
	
	public Quad image(UvImage image) {

		this.image = image;
		this.image.usedCount++;

		String cmd = toConvertCommand();
		//System.out.println(cmd);
		if (exec(cmd) != 0) 
			throw new RuntimeException("Warp failed on: " + this);
		warped = parent.loadImage(UvMapper.OUTPUT_DIR + this.image.imageOut);
		if (warped == null) 
			System.err.println("[WARN] Unable to load image: " +
					UvMapper.OUTPUT_DIR + image.imageOut + "\n  $"+cmd);
		else
		  System.out.println("Quad#"+id + ": "+image.imageOut+ "  area="+area());			
		return this;
	}
	
	public boolean isClockwise() {
		int sum = 0;
		for (int i = 0; i < points.length; i += 2) {
			sum +=  (points[(i+2)%8] - points[i]) * (points[(i+3)%8] + points[i+1]); 
		}
		return sum < 0;
	}
	
	public float[] centroid() {

		float centerX = 0, centerY = 0;
		for (int i = 0; i < points.length; i += 2) {
			centerX += points[i];
			centerY += points[i + 1];
		}
		return new float[] { centerX / 4f, centerY / 4f };
	}

	public String toConvertCommand() {

		float[] npts = normalizeQuadPosition();

		float[] srcDst = new float[16];
		for (int i = 0, j = 0; i < srcDst.length; i++) {
			if (i % 4 > 1) srcDst[i] = npts[j++];
		}

		srcDst[4] = srcDst[8] = bounds[2];
		srcDst[9] = srcDst[13] = bounds[3];

		// resize the image before transform: width/ height specifically given,
		// original aspect ratio ignored
		
		String s = UvMapper.CONVERT_CMD + bounds[2] + "x" + bounds[3] + "! " 
				+ UvMapper.IMAGE_DIR + image.imageIn + UvMapper.CONVERT_ARGS;

		for (int i = 0; i < srcDst.length; i++) {
			s += UvMapper.ROUNT_DATA_TO_INTS ? "" + Math.round(srcDst[i]) : srcDst[i];
			if (i < srcDst.length - 1) s += ",";
		}

		return s.trim() + ' ' + UvMapper.OUTPUT_DIR + image.imageOut;
	}

	private float[] normalizeQuadPosition() {

		float[] n = new float[points.length];
		for (int i = 0; i < n.length; i++)
			n[i] = points[i] - (i % 2 == 0 ? bounds[0] : bounds[1]);
		return n;
	}

	private float[] bounds() {

		float maxX = 0, maxY = 0, minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;

		for (int i = 0; i < points.length; i += 2) {

			if (points[i] > maxX) maxX = points[i];
			if (points[i] < minX) minX = points[i];
			if (points[i + 1] > maxY) maxY = points[i + 1];
			if (points[i + 1] < minY) minY = points[i + 1];
		}

		return new float[] { minX, minY, maxX - minX, maxY - minY }; // x,y,w,h
	}

	public float aspectRatio() {

		// Calculate the middle points
		float[][] mPs = new float[4][];

		for (int i = 0; i < mPs.length; i++) {
			if (i < 3)
				mPs[i] = new float[] { (points[2 + 2 * i] + points[2 * i]) / 2, (points[3 + 2 * i] + points[1 + 2 * i]) / 2 };
			else
				mPs[i] = new float[] { (points[6] + points[0]) / 2, (points[7] + points[1]) / 2 };
			// System.out.println(mPs[i][0] + " " + mPs[i][1]);
		}

		float h1 = (float) Math.sqrt((mPs[0][0] - mPs[2][0]) * (mPs[0][0] - mPs[2][0]) + (mPs[0][1] - mPs[2][1]) * (mPs[0][1] - mPs[2][1]));
		float h2 = (float) Math.sqrt((mPs[1][0] - mPs[3][0]) * (mPs[1][0] - mPs[3][0]) + (mPs[1][1] - mPs[3][1]) * (mPs[1][1] - mPs[3][1]));

		return h1 > h2 ? h1 / h2 : h2 / h1;
	}

	public void fixOrdering() {
		Line2D.Float l1 = new Line2D.Float(points[0], points[1], points[6], points[7]);
		Line2D.Float l3 = new Line2D.Float(points[0], points[1], points[2], points[3]);
		if (l1.intersectsLine(points[2], points[3], points[4], points[5])) 
			swapPoints(0, 1, 2, 3);
		if (l3.intersectsLine(points[4], points[5], points[6], points[7])) 
			swapPoints(2, 3, 4, 5);
		if (!isClockwise()) 
			swapPoints(2,3,6,7);
	}

	public static void main(String[] args) {

		float[] f = { 0, 1, 2, 3, 4, 5, 6, 7 };
		shiftArray(f, 3);
		for (int i = 0; i < f.length; i++) {
			System.out.print(f[i] + ",");
		}
		System.out.println();
	}

	public int upperLeftIndex() {

		int ulIdx = -1;
		float[] c = centroid();

		for (int i = 0; i < points.length; i+=2) {
			float cx = c[0] - points[i];
			float cy = c[1] - points[i+1];
			if (cx > 0 && cy > 0)
				ulIdx = i;
		}

		if (ulIdx < 0) throw new RuntimeException("invalid state");

		return ulIdx;
	}

	static void shiftArray(float[] f, int places) {

		for (int j = 0; j < places; j++) {

			float tmp = f[0];
			for (int i = 1; i < f.length; i++) {
				f[i - 1] = f[i];
			}
			f[f.length - 1] = tmp;
		}
	}

	void swapPoints(int x1, int y1, int x2, int y2) {

		float tmp = points[x1];
		points[x1] = points[x2];
		points[x2] = tmp;
		tmp = points[y1];
		points[y1] = points[y2];
		points[y2] = tmp;
	}

	public Quad draw() {

		
//		parent.fill(0,200,0);
//		if (!isClockwise())
//			parent.fill(200,0,0);
		
		if (this.warped != null) 
			parent.image(this.warped, bounds[0], bounds[1], bounds[2], bounds[3]);
		
		parent.stroke(0);
		parent.noFill();
		parent.quad(points[0], points[1], points[2], points[3], points[4], points[5], points[6], points[7]);		
		
		//for (int i = 0; i < points.length; i += 2)
			//parent.text(i + "," + (i + 1), points[i], points[i + 1]);

		return this;
	}

	public static int exec(String line) {

		DefaultExecutor executor = new DefaultExecutor();
		executor.setWatchdog(new ExecuteWatchdog(10000));
		try {
			return executor.execute(CommandLine.parse(line));
		}
		catch (Exception e) {
//			throw new RuntimeException(e);
			return 1;
		}
	}

	public static ArrayList<Quad> fromData(PApplet p, String dataFilePath) {
		return fromData(p, dataFilePath, Integer.MAX_VALUE);
	}
	
	public static ArrayList<Quad> fromData(PApplet p, String dataFilePath, int maxToLoad) {

		ArrayList<Quad> quads = new ArrayList<Quad>();
		for (String line : p.loadStrings(dataFilePath)) {

			String[] spts = line.split(",");
			if (spts.length != 8) {
				System.err.println("[WARN] Ignoring invalid quad("+spts.length+"pts): " + line);
				continue;
			}
			float[] fpts = new float[spts.length];
			for (int i = 0; i < spts.length; i++) {
				fpts[i] = Float.parseFloat(spts[i]);
			}
			
			quads.add(new Quad(p, fpts));
			if (testOnlyOne) break;
		}
		
		// Scale the Quads (by sketch size) and ignore tiny ones
		int removed = 0;
		for (Iterator<Quad> it = quads.iterator(); it.hasNext();) {
			Quad quad = it.next();
			quad.scale(p.width, p.height);
			if (quad.area() < UvMapper.MIN_ALLOWED_QUAD_AREA ) {
				it.remove();
				removed++;
			}
		}
		
		// Sort the Quads by area
		quads.sort(new Comparator<Quad>() {
			public int compare(Quad q1, Quad q2) {
				return q1.area() > q2.area() ? -1 : 1;
			}
		});
		
		System.out.println("\nFound " + quads.size()+" Quads (after removing " +
			removed + " too small) with max-area=" + quads.get(0).area()+"\n");
		
		return quads;
	}

	public static void drawAll(ArrayList<Quad> quads) {

		for (Quad q : quads) {
			q.draw();
		}
	}
	
	public float area() {

		float area = 0;
		float[] x = { points[0], points[2], points[4], points[6] }, y = { points[1], points[3], points[5], points[7] };

		int j = 3;
		for (int i = 0; i < 4; i++) {
			area += (x[j] + x[i]) * (y[j] - y[i]);
			j = i;
		}
		return Math.abs(area / 2f);
	}

	public void offset(float x, float y) {

		for (int i = 0; i < points.length; i++)
			points[i] += (i % 2 == 0 ? x : y);
		bounds = bounds();
	}
	
	public void scale(float x, float y) {

		for (int i = 0; i < points.length; i++)
			points[i] *= (i % 2 == 0 ? x : y);
		bounds = bounds();
	}
	
}
