package test;

import processing.core.PApplet;
import uvm.Quad;

public class OneQuad extends PApplet {

	public void settings() {

		size(400, 400);
	}

	public void setup() {
		
		float[] f = { 0.8424786f,0.10419211f,0.7860224f,0.14596535f,0.7459136f,0.078942426f,0.7828462f,0.0014371289f }, g = new float[f.length/2];
		
		for (int i = 0; i < f.length; i+=2) {
			g[i/2] = dist(f[i], f[i+1], f[(i+2)%f.length], f[(i+3)%f.length]); 
		}
		
		Quad q = new Quad(this, f, g);
		q.scale(width,height);
		q.draw();
	}
	
	public static void main(String[] args) {

		PApplet.main(OneQuad.class.getName());
	}
}
