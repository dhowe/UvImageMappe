package uvm;

import processing.core.*;

public class Quad implements Comparable<Quad> {

	private static int ID_GEN = 0;

	public float[] points;
	public UvImage image;
	public int id;
	private float minX, minY, maxX, maxY;

	private PImage warped;

	public Quad(float... pts) {

		this.points = pts;
		this.id = ID_GEN++;
		this.minX = 10000;//depends on the UV data
		this.minY = 10000;
		this.maxX = 0;
		this.maxY = 0;

	}

	public Quad image(UvImage image) {

		this.image = image;
		return this;
	}

	public String toConvertCommand() {
		
		calculatQuadBorder();
		
		float[] npts = normalizeQuadPosition();
		int[] newImageSize = getNewImageSize();
		
		image.width = newImageSize[0];
		image.height = newImageSize[1];
	
		float[] srcDst = new float[16];
		for (int i = 0, j = 0; i < srcDst.length; i++) {
			if (i % 4 > 1) srcDst[i] = npts[j++];
		}
		
		srcDst[4] = srcDst[8] = image.width;
		srcDst[9] = srcDst[13] = image.height;
    
		//resize the image before transform [Width and height emphatically given, original aspect ratio ignored]
		String s = UvMapper.CONVERT_CMD + image.width + "x" + image.height + "! " + image.imageIn + UvMapper.CONVERT_ARGS;

		for (int i = 0; i < srcDst.length; i++) {
			s += UvMapper.ROUNT_DATA_TO_INTS ? "" + Math.round(srcDst[i]) : srcDst[i];
			if (i < srcDst.length - 1) s += ",";
		}

		return s.trim() + ' ' + image.imageOut;
	}

	private float[] normalizeQuadPosition() {

		float[] n = new float[points.length];
		float referenceX;
		float referenceY;
		for (int i = 0; i < n.length; i++)
			n[i] = points[i] - (i % 2 == 0 ? minX : minY);
		return n;
	}

	private int[] getNewImageSize() {
		System.out.println(minX + " " + minY + " " +maxX +  " " + maxY);
		int[] size = {Math.round(maxX-minX), Math.round(maxY-minY)};
		return size;
	}
	
	private void calculatQuadBorder() {
		
		for (int i = 0; i < points.length; i += 2) {
			
			if (points[i] > maxX) maxX = points[i];
			if (points[i] < minX) minX = points[i];
			if (points[i + 1] > maxY) maxY = points[i+1];
			if (points[i + 1] < minY) minY = points[i+1];
		}
		
//		System.out.println(minX + " " + minY + " " +maxX +  " " + maxY);
		
	}
	
	public float area() {

	  // Initialze area
	  float area = 0;
	  int n = 4;
	  float[] x = { points[0], points[2], points[4], points[6] };
	  float[] y = { points[1], points[3], points[5], points[7] };

	  int j = n - 1;
	  for (int i = 0; i < n; i++) {
	   area += (x[j] + x[i]) * (y[j] - y[i]);
	   j = i;
	  }
	  return Math.abs(area / 2f);
	 }

	public float aspectRatio() {

		return image.aspectRation();
	}

	public int compareTo(Quad o) {

		return aspectRatio() > o.aspectRatio() ? 1 : -1;
	}

	public Quad draw(PApplet p) {

		p.noFill();
		p.stroke(0);
//		p.imageMode(PApplet.CORNERS);
		
		p.image(this.warped, minX, minY, image.width, image.height);
		p.quad(points[0], points[1], points[2], points[3], points[4], points[5], points[6], points[7]);
	
		return this;
	}

	public Quad loadWarp(PApplet p) {

		warped = p.loadImage(this.image.imageOut);
		return this;
	}
	
}
