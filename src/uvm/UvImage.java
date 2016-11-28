package uvm;

import processing.core.*;

public class UvImage {

	public PImage image;
	public int quadId = -1;
	public int width, height;
	public String imageIn, imageOut;

	public UvImage(PApplet p, String imageName) {
		
		this.image = p.loadImage(imageName);
		this.width = this.image.width;
		this.height = this.image.height;
		this.imageIn = UvMapper.IMAGE_DIR + imageName;
		this.imageOut = changeExt(UvMapper.OUTPUT_DIR + imageName, ".png");
		//this.imageOut = rename(imageName, "-warp");
	}

	public String changeExt(String fileName, String ext) {
		
		String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
		return tokens[0] + ext;
	}
	
	public String rename(String fileName, String postfix) {
		
		String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
		return tokens[0] + postfix + '.' + tokens[1]; 
	}

	public float aspectRation() {

		return image.width / (float) image.height;
	}

	public String toString() {

		return "{ name: " + imageIn + ", width: " + width + ", height: " 
				+ height + (quadId > -1 ? (", quad: " + quadId) : " ") + "}";
	}
}
