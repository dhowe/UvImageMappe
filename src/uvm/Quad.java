package uvm;

import java.util.*;

import processing.core.*;

public class Quad implements Comparable<Quad> {

	private static int ID_GEN = 0;

	public int id;
	public UvImage image;
	public float[] points;
	
	protected PImage warped;
	protected float[] bounds;

	public Quad(float... points) {

		this.id = ID_GEN++;
		this.points = points;
		this.bounds = bounds();
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

		srcDst[4] = srcDst[8] = bounds[2];
		srcDst[9] = srcDst[13] = bounds[3];

		// resize the image before transform: width/ height specifically given, original aspect ratio ignored
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
		
		return new float[] { minX, minY, maxX-minX, maxY-minY }; // x,y,w,h
	}

	public float aspectRatio() {

		return image.aspectRation();
	}

	public Quad draw(PApplet p) {

		p.image(this.warped, bounds[0], bounds[1], bounds[2], bounds[3]);

		p.noFill();
		p.stroke(0);
		p.quad(points[0], points[1], points[2], points[3], points[4], points[5], points[6], points[7]);

		return this;
	}

	public Quad loadWarp(PApplet p) {

		warped = p.loadImage(this.image.imageOut);
		return this;
	}

	// ////////////// not used at moment ///////////////////////

	public float area() {

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

	public int compareTo(Quad o) {

		return aspectRatio() > o.aspectRatio() ? 1 : -1;
	}

	public static void sort(ArrayList<Quad> quads) { // not used at moment

		quads.sort(new Comparator<Quad>() {

			public int compare(Quad q1, Quad q2) {

				return q1.compareTo(q2);
			};
		});
	}

}
