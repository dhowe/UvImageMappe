package uvm;

import processing.core.*;

public class Quad implements Comparable<Quad> {

	private static int ID_GEN = 0;

	public float[] points;
	public UvImage image;
	public int id;

	private PImage warped;

	public Quad(float... pts) {

		this.points = pts;
		this.id = ID_GEN++;
	}

	public Quad image(UvImage image) {

		this.image = image;
		return this;
	}

	public String toConvertCommand() {

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
		for (int i = 0; i < n.length; i++)
			n[i] = points[i] - (i % 2 == 0 ? points[0] : points[1]);
		return n;
	}

	private int[] getNewImageSize() {

		float maxX = 0, minX = 100000, maxY = 0, minY = 100000;
		for (int i = 0; i < points.length; i += 2) {
			
			if (points[i] > maxX) maxX = points[i];
			if (points[i] < minX) minX = points[i];
			if (points[i + 1] > maxY) maxY = points[i+1];
			if (points[i + 1] < minY) minY = points[i+1];
		}

		int[] size = {Math.round(maxX-minX), Math.round(maxY-minY)};
		return size;
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
		
		p.image(this.warped, points[0], points[1], image.width, image.height);
		p.quad(points[0], points[1], points[2], points[3], points[4], points[5], points[6], points[7]);
	
		return this;
	}

	public Quad loadWarp(PApplet p) {

		warped = p.loadImage(this.image.imageOut);
		return this;
	}
	
}
