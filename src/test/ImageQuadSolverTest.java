package test;

import java.util.*;
import org.junit.*;
import uvm.*;

public class ImageQuadSolverTest {

	List<UvImage> workers;
	List<Quad> jobs;

	@Before
	public void setUp() {

		String TEST_QUAD_DATA = "50,50,100,50,100,100,50,100;0,30,10,30,20,70,0,70;0,0,50,10,50,20,10,15;";
		
		jobs = Quad.fromString(TEST_QUAD_DATA);
		
		workers = new ArrayList<UvImage>();
		workers.add(new UvImage("a", 600, 120));
		workers.add(new UvImage("b", 300, 300));
		workers.add(new UvImage("c", 300, 600));
	}

	@After
	public void tearDown() {
		jobs = null;
		workers = null;
	}

	@Test
	public void testComputeCost() {

		// TODO: add simple test data and expected result below (2)

		// Test 1 -----------------------------
		// Square quad match with square image

		// Quad[0]:Bound width 50.0, Bound height 50.0
		// Quad points:0.0, 0.0; 1.0, 0.0; 1.0, 1.0; 0.0, 1.0;
		// IMAGE[b]:width 300, height 300
		// Image Points:0.0, 0.0; 0.5, 0.0; 0.5, 0.5; 0.0, 0.5;
		// Quad Bound width:1.0, height1.0
		// Align Image with Height
		// Image Points after allignment:0.0, 0.0; 1.0, 0.0; 1.0, 1.0; 0.0, 1.0;
		// Fit:0.0

		double expectedCost1 = 0;
		int imageIndex1 = 1, quadIndex1 = 0;

		double result1 = new ImageQuadSolver(workers, jobs).cost(imageIndex1, quadIndex1);
		System.out.println("Test1:" + result1 + "\n");
		Assert.assertTrue(expectedCost1 == result1);

		// Test 2 -----------------------------

		// Quad[0]:Bound width 50.0, Bound height 50.0
		// Quad points:0.0, 0.0; 1.0, 0.0; 1.0, 1.0; 0.0, 1.0;
		// IMAGE[c]:width 300, height 600
		// Image Points:0.0, 0.0; 0.5, 0.0; 0.5, 1.0; 0.0, 1.0;
		// Quad Bound width:1.0, height1.0
		// Align Image with Height
		// Image Points after allignment:0.0, 0.0; 0.5, 0.0; 0.5, 1.0; 0.0, 1.0;
		// Fit:1.0

		double expectedCost2 = 1.0;
		int imageIndex2 = 2, quadIndex2 = 0;

		double result2 = new ImageQuadSolver(workers, jobs).cost(imageIndex2, quadIndex2);
		System.out.println("Test2:" + result2 + "\n");
		Assert.assertTrue(expectedCost2 == result2);

		// Test 3 -----------------------------

		// Quad[0]:Bound width 50.0, Bound height 50.0
		// Quad points:0.0, 0.0; 1.0, 0.0; 1.0, 1.0; 0.0, 1.0;
		// IMAGE[a]:width 600, height 120
		// Image Points:0.0, 0.0; 1.0, 0.0; 1.0, 0.2; 0.0, 0.2;
		// Quad Bound width:1.0, height1.0
		// Align Image with Width
		// Image Points after allignment:0.0, 0.0; 1.0, 0.0; 1.0, 0.2; 0.0, 0.2;
		// Fit:1.6

		double expectedCost3 = 1.6;
		int imageIndex3 = 0, quadIndex3 = 0;

		double result3 = new ImageQuadSolver(workers, jobs).cost(imageIndex3, quadIndex3);
		System.out.println("Test3:" + result3 + "\n");
		Assert.assertTrue(expectedCost3 == result3);

	}

	@Test
	public void testExecute1() {

		// the min cost matching of workers to jobs,
		// with -1 indicating an unassigned worker/image
		int[] result = new ImageQuadSolver(workers, jobs).execute();
//		System.out.println(result[0] + " " + result[1] + " " + result[2]);
//		for (int i = 0; i < result.length; i++) {
//			System.out.println(i + ") Quad@" + jobs.get(result[i]).id + " :: Image#" + workers.get(i).imageName);
//		}
//		System.out.println("\n");
		// a - quad[2] - horizontal
		// b - quad[0] - square
		// c - quad[1] - vertical
		Assert.assertTrue(Arrays.equals(new int[] { 2, 0, 1 }, result));
	}

	@Test
	public void testExecute2() {

		// when the number of jobs is less than the number of workers

		jobs.remove(2);

		// the min cost matching of workers to jobs,
		// with -1 indicating an unassigned worker/image
		int[] result = new ImageQuadSolver(workers, jobs).execute();
//		System.out.println(result[0] + " " + result[1] + " " + result[2]);
//		for (int i = 0; i < result.length; i++) {
//			if (result[i] != -1) System.out.println(i + ") Quad@" + jobs.get(result[i]).id + " :: Image#" + workers.get(i).imageName);
//		}
//
//		System.out.println("\n");
		// b - quad[0] - square
		// c - quad[1] - vertical
		Assert.assertTrue(Arrays.equals(new int[] { -1, 0, 1 }, result));
	}

	@Test
	public void testExecute3() {

		// when the number of workers is less than the number of jobs

		workers.remove(2);

		// the min cost matching of workers to jobs,
		// with -1 indicating an unassigned worker/image
		int[] result = new ImageQuadSolver(workers, jobs).execute();
//		System.out.println(result[0] + " " + result[1]);
//
//		for (int i = 0; i < result.length; i++) {
//			if (result[i] != -1) System.out.println(i + ") Quad@" + jobs.get(result[i]).id + " :: Image#" + workers.get(i).imageName);
//		}

		//System.out.println("\n");
		// a - quad[2] - horizontal
		// b - quad[0] - square
		Assert.assertTrue(Arrays.equals(new int[] { 2, 0 }, result));
	}

}