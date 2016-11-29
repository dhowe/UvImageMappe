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

		float[] srcDst = new float[16];
		for (int i = 0, j = 0; i < srcDst.length; i++) {
			if (i % 4 > 1) srcDst[i] = npts[j++];
		}
		srcDst[4] = srcDst[8] = image.width;
		srcDst[9] = srcDst[13] = image.height;

		String s = UvMapper.CONVERT_CMD + image.imageIn + UvMapper.CONVERT_ARGS;

		for (int i = 0; i < srcDst.length; i++) {
			s += UvMapper.ROUNT_DATA_TO_INTS ? "" + Math.round(srcDst[i]) : srcDst[i];
			if (i < srcDst.length - 1) s += ",";
		}
		
		return s.trim() + ' ' + image.imageOut;
	}

	private float[] normalizeQuadPosition() {
		
		float[] n  = new float[points.length];
		for (int i = 0; i < n.length; i++)
			n[i] = points[i] - (i % 2 == 0 ? points[0] : points[1]);
		return n;
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
		p.imageMode(PApplet.CORNERS);
		p.quad(points[0], points[1], points[2], points[3], points[4], points[5], points[6], points[7]);
		p.stroke(200,0,0);
		
		// TODO: adjust the warped image pos to fit the quad correctly
		p.image(this.warped, points[0], points[1], points[4], points[5]);
		
		return this;
	}

	public Quad loadWarp(PApplet p) {

		warped = p.loadImage(this.image.imageOut);
		return this;
	}

}
