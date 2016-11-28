package uvm;

import java.io.*;
import java.util.*;

import org.apache.commons.exec.*;

import processing.core.PApplet;

public class UvMapper extends PApplet {

	public static String IMAGE_DIR = "data/";
	public static String OUTPUT_DIR = "warp/";

	public static boolean ROUNT_DATA_TO_INTS = true;
	public static String CONVERT_CMD = "/usr/local/bin/convert ";
	public static String CONVERT_ARGS = " -matte -virtual-pixel transparent -interpolate Spline -distort BilinearForward ";

	ArrayList<Quad> quads = new ArrayList<Quad>();
	ArrayList<UvImage> ads = new ArrayList<UvImage>();

	float[][] test = { { 10, 10, 326, 10, 316, 260, 0, 280 } }; // TODO: tmp-replace

	public void settings() {

		size(400, 400);
	}

	public void setup() {

		// Step #1: load images into UvImage objects
		String[] files = new File(IMAGE_DIR).list();
		for (int i = 0; i < files.length; i++) {
			if (files[i].matches(".*\\.(png|gif|jpg|jpeg)"))
				ads.add(new UvImage(this, files[i]));
		}

		// Step #2: import data and create the Quads 
		// (TODO: replace 'test' with .txt file in data folder)
		for (int i = 0; i < test.length; i++) {
			
			quads.add(new Quad(test[i]));
		}

		// Step #5: sort the quads
		quads.sort(new Comparator<Quad>() {
			public int compare(Quad q1, Quad q2) {
				return q1.compareTo(q2);
			};
		});

		// Step #6: loop over quads, assigning best fitting ad-image (TODO)
		for (Iterator it = quads.iterator(); it.hasNext();) {
			
			Quad q = (Quad) it.next();
			for (Iterator it2 = ads.iterator(); it2.hasNext();) {
				UvImage img = (UvImage) it2.next();
				q.image(img);
			}
		}

		// Step #7: Create/exec the commands for each warp op
		for (Iterator it = quads.iterator(); it.hasNext();) {
			
			Quad q = (Quad) it.next();
			String cmd = q.toConvertCommand();			
			System.out.println(cmd + " -> " + exec(cmd));
			q.loadWarp(this);
		}
	}

	public void draw() {

		// Step #8: Draw warped images with quads on top
		for (Iterator it = quads.iterator(); it.hasNext();) {
			Quad q = (Quad) it.next();
			q.draw(this);
		}
	}

	public static int exec(String line) {

		CommandLine cmdLine = CommandLine.parse(line);
		DefaultExecutor executor = new DefaultExecutor();
		ExecuteWatchdog watchdog = new ExecuteWatchdog(10000);
		executor.setWatchdog(watchdog);
		try {
			return executor.execute(cmdLine);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return -1;
	}

	public static void main(String[] args) {

		PApplet.main(UvMapper.class.getName());
	}
}
