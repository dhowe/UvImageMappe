package uvm;

import java.util.ArrayList;

import org.apache.commons.exec.*;

import processing.core.*;

public class Quad {

	public UvImage image;
	public float[] points, bounds;

	protected PImage warped;
	protected PApplet parent;

	public Quad(PApplet p, float... points) {

		this.parent = p;
		this.points = points;
		for(int i=0;i<8;i++)
			this.points[i] = this.points[i];//*p.displayHeight;
		this.bounds = bounds();
	}

	public Quad image(UvImage image) {

		this.image = image;
		if (exec(toConvertCommand()) != 0) 
			throw new RuntimeException("Warp failed on: " + this);
		warped = parent.loadImage(this.image.imageOut);
		return this;
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
		String s = UvMapper.CONVERT_CMD + bounds[2] + "x" + bounds[3] + "! " + image.imageIn + UvMapper.CONVERT_ARGS;

		for (int i = 0; i < srcDst.length; i++) {
			s += UvMapper.ROUNT_DATA_TO_INTS ? "" + Math.round(srcDst[i]) : srcDst[i];
			if (i < srcDst.length - 1) s += ",";
		}

		return s.trim() + ' ' + image.imageOut;
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

	public Quad draw() {

		if (this.warped != null)
			parent.image(this.warped, bounds[0], bounds[1], bounds[2], bounds[3]);

		parent.noFill();
		parent.stroke(0);
		parent.quad(points[0], points[1], points[2], points[3], points[4], points[5], points[6], points[7]);

		return this;
	}

	public static int exec(String line) {

		DefaultExecutor executor = new DefaultExecutor();
		executor.setWatchdog(new ExecuteWatchdog(10000));
		try {
			return executor.execute(CommandLine.parse(line));
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static ArrayList<Quad> fromData(PApplet p, String dataFilePath) {

		ArrayList<Quad> quads = new ArrayList<Quad>();
		for (String line : p.loadStrings(dataFilePath)) {

			String[] spts = line.split(",");
			float[] fpts = new float[spts.length];
			for (int i = 0; i < spts.length; i++) {
				fpts[i] = Float.parseFloat(spts[i]);
			}
			quads.add(new Quad(p, fpts));
		}

		return quads;
	}

	// ////////////// not used at moment ///////////////////////

	public float area() {

		float area = 0;
		float[] x = { points[0], points[2], points[4], points[6] },
				y = { points[1], points[3], points[5], points[7] };

		int j = 3;
		for (int i = 0; i < 4; i++) {
			area += (x[j] + x[i]) * (y[j] - y[i]);
			j = i;
		}
		return Math.abs(area / 2f);
	}
}
