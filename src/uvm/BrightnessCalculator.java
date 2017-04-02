package uvm;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;

public class BrightnessCalculator extends PApplet {

	public static String DATA_FILE = "data/berthaFinal.txt";
	public static String BRIGHTNESS_FILE = "data/textureNew.jpg";

	public static String IMAGE_DIR = "BerthaDouble/";
	public static int MAX_NUM_IMGS_TO_LOAD = 2600;

	List<Quad> quads;
	List<UvImage> ads;
	PImage img;
	ArrayList<Float> brightnessData = new ArrayList<Float>();
	int[] count = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	ArrayList<Float> imageBrightnessData = new ArrayList<Float>();
	int[] imageCount = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	public void settings() {

		size(2000, 2000);
		img = loadImage(BRIGHTNESS_FILE);
		img.loadPixels();
	}

	public void setup() {

		calculateForQuads();
//		calculateForImages();

	}

	public void calculateForImages() {

		ads = UvImage.fromFolder(this, IMAGE_DIR, 5000);
		calculateImagesBrightness();

		System.out.println("Brightness range of Images: ");

		for (int i = 0; i < imageCount.length; i++) {
			System.out.println("0." + i + "- 0." + (i + 1) + ":" + imageCount[i]);
		}

		// imageBrightnessData -> imageBrightness.txt
		writeToFile(imageBrightnessData, "imageBrightness");

	}

	public void calculateForQuads() {

		quads = Quad.fromData(this, DATA_FILE);

		// Brightness Image
		image(img, 0, 0);
		img.resize(this.width, this.height);
		image(img, 0, 0);
		img.loadPixels();

		Quad.drawAll(quads);

		float max = 0, min = 255;
		for (int x = 0; x < img.width; x++) {
			for (int y = 0; y < img.height; y++) {
				// Calculate the 1D location from a 2D grid
				int loc = x + y * img.width;
				float b = brightness(img.pixels[loc]);

				if (b > max) max = b;
				if (b < min) min = b;
			}
		}

		System.out.println("Brightness range of the image: " + min + "-" + max);

		calculateAllBrightness();

		System.out.println("Brightness range of Quad: ");

		for (int i = 0; i < count.length; i++) {
			System.out.println("0." + i + "- 0." + (i + 1) + ":" + count[i]);
		}

		for (int i = 0; i < quads.size(); i++) {
			System.out.print("Quad[" + i + "]");
			System.out.println(quads.get(i).brightness);
		}

		writeToFile(brightnessData, "quadBrightness");
	}

	public void writeToFile(ArrayList<Float> arr, String name) {

		FileWriter writer;
		try {
			writer = new FileWriter("data/" + name + ".txt");
			for (float str : arr) {
				String line = Float.toString(str) + "\n";
				writer.write(line);
			}
			writer.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void calculateImagesBrightness() {

		for (int i = 0; i < ads.size(); i++) {
			UvImage img = ads.get(i);
			float b = img.brightness;

			imageBrightnessData.add(b);
			System.out.println(img.imageName + ":" + b);
			int range = (int) Math.floor(b * 10);
			if (range >= 0 && range < 10) imageCount[range]++;

		}

	}

	public void draw() {

		// Quad.mouseOver(quads);
	}

	public void calculateAllBrightness() {

		for (int i = 0; i < quads.size(); i++) {
			Quad quad = quads.get(i);
			float b = quad.computeBrightness(img, this);

			brightnessData.add(b);
			// System.out.println(b);
			int range = (int) Math.floor(b * 10);
			count[range]++;

		}
	}

	public static void main(String[] args) {

		PApplet.main(BrightnessCalculator.class.getName());
	}

}
