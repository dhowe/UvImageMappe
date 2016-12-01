package test;

import java.awt.geom.Line2D;
import java.util.*;

import processing.core.PApplet;
import uvm.Quad;

public class PolySort extends PApplet {

	public void settings() {

		size(1200, 1000);
	}

	void dline(Line2D.Float l) {
		line(l.x1,l.y1,l.x2,l.y2);
	}
	
	public void setup() {

		int i = 0;
		List<Quad> quads = Quad.fromData(this, "polys.txt");
		for (Quad quad : quads) {
			quad.offset(100+50*i,50+50*i++);
			quad.draw();
		}
	}

	public static void main(String[] args) {

		PApplet.main(PolySort.class.getName());
	}
}
