package test;

import java.awt.geom.Line2D;
import java.util.*;

import processing.core.PApplet;
import uvm.*;

public class PolySort extends PApplet {

	public void settings() {

		size(1000,1000);
	}

	void dline(Line2D.Float l) {

		line(l.x1, l.y1, l.x2, l.y2);
	}

	public void setup() {

		int i = 0;
		List<Quad> quads = Quad.fromData(this, "bug3.txt");
		for (Quad quad : quads) {
			int idx = 0;
			quad.draw();
			stroke(200,0,0);
			//ellipse(quad.points[idx],quad.points[idx+1],10,10);
			ellipse(quad.points[idx],quad.points[idx+1],10,10);
		}
	}

	public static void main(String[] args) {

		UvMapper.SCALE_QUADS_TO_DISPLAY = false;
		PApplet.main(PolySort.class.getName());
	}
}
