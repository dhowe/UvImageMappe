package uvm;

import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

public class BrightnessCalculator extends PApplet{
	public static String DATA_FILE = "data/BerthaData20170205.txt";
	public static String BRIGHTNESS_FILE = "data/texture.jpeg";
	public static String UV_NAME = "BarthaTest.png";
	public static String OUTPUT_DIR = "data/";
	
	List<Quad> quads;
	PImage img;
	
	public void settings() {

		size(1000, 1000);
		img = loadImage(BRIGHTNESS_FILE);
		img.loadPixels();
	}

	public void setup() {

		quads = Quad.fromData(this, DATA_FILE);
		
		//Brightness Image
		image(img, 0, 0);
		img.resize(this.width, this.height);
		image(img, 0, 0);
		img.loadPixels();
		
		Quad.drawAll(quads);
		
		float max = 0, min = 255;
	  for (int x = 0; x < img.width; x++) {
	    for (int y = 0; y < img.height; y++ ) {
	      // Calculate the 1D location from a 2D grid
	      int loc = x + y*img.width;
	      float b = brightness(img.pixels[loc]);
	      
	      if(b > max) max = b;
	      if(b < min) min = b;
	    }
	  }
	  
	  System.out.println("Brightness range of the image: " + min + "-" +  max);
		calculateAllBrightness();
		//save to data
   
	}
	
	public void draw() {
		
//		Quad.mouseOver(quads);
	}
	
	public void calculateAllBrightness() {
		for (int i = 0; i < quads.size(); i++) {
			Quad quad = quads.get(i);	
			quad.getBrightness(img, this);
		}
		
	}
	
	public static void main(String[] args) {

		PApplet.main(BrightnessCalculator.class.getName());
	}
	
	

}
