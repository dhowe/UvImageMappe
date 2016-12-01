package uvm;

import processing.core.PApplet;

public class OneQuad extends PApplet {

	public void settings() {

		size(400, 400);
	}

	public void setup() {
		float[] f = new float[]{0.8424786f,0.10419211f,0.7860224f,0.14596535f,0.7459136f,0.078942426f,0.7828462f,0.0014371289f};
		for (int i = 0; i < f.length; i++) {
		//	f[i] *= width;
		}
		Quad q = new Quad(this, f);
		q.scale(width,height);
		q.draw();
	}
	
	public static void main(String[] args) {

		PApplet.main(OneQuad.class.getName());
	}
}
