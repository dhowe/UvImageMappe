package uvm;

import processing.core.*;


public class UvImage {
	
	public PImage image;
	public String imageName;
	public int width, height;
	public int quadId = -1;
	
	public UvImage(PApplet p, String imageName) {
		
		this.imageName = imageName;
		this.image = p.loadImage(imageName);
		this.width = this.image.width;
		this.height = this.image.height;
	}

	public float aspectRation() {

		return image.width/(float)image.height;
	}
	
	public String toString() {
		
		return "{ name: "+imageName + ", width: "+width + ", height: " 
				+ height + (quadId > -1 ? (", quad: "+quadId) : " ") + "}";
	}
}
