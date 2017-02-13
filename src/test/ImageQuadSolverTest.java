package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import uvm.ImageQuadSolver;
import uvm.Quad;
import uvm.UvImage;

public class ImageQuadSolverTest {
	
	String IMAGE_DIR = "allImages/images/";
	List<UvImage> workers = new ArrayList<UvImage>();
	List<Quad> jobs;
	
	
  String TEST_QUAD_DATA = "0,0,50,10,60,20,10,15;50,50,100,50,100,100,50,100;0,30,10,30,20,70,0,70;";
	
  public void assignTestData() {
  	jobs = Quad.fromString(TEST_QUAD_DATA);
//  	System.out.println(jobs);
  	
  	UvImage a = new UvImage("a",728,90);
  	UvImage b = new UvImage("b",320,400);
  	UvImage c = new UvImage("c",300,600);
  	
  	workers.add(a);
  	workers.add(b);
  	workers.add(c);
  	
//  	System.out.println(workers);
  }
	
  @Test
  public void testComputeCost() {
  	
 // TODO: add simple test data and expected result below (2)
  	assignTestData();

  	 

		// Test 1 -----------------------------
		double expectedCost1 = 0.6545696160205333;
  	int imageIndex1 = 0, quadIndex1 = 0;
  	
  	double result1 = new ImageQuadSolver(workers, jobs).cost(imageIndex1, quadIndex1);
  	System.out.println("Test1:" + result1 + "\n");
    Assert.assertTrue(expectedCost1 == result1);
  	
    
    // Test 2 -----------------------------
		double expectedCost2 = 1.1630900469120355;
  	int imageIndex2 = 1, quadIndex2 = 1;
  	
		double result2 = new ImageQuadSolver(workers, jobs).cost(imageIndex2, quadIndex2);
		System.out.println("Test2:" + result2 + "\n");
		Assert.assertTrue(expectedCost2 == result2);
 
    // Test 3 -----------------------------
		double expectedCost3 = 0.5790309945650017;
  	int imageIndex3 = 2, quadIndex3 = 2;
  	
  	
		double result3 = new ImageQuadSolver(workers, jobs).cost(imageIndex3, quadIndex3);
		System.out.println("Test3:" + result3 + "\n");
    Assert.assertTrue(expectedCost3 == result3);
  	
  }
  
  @Test
  public void testExecute1() {
  	
  	// TODO: add simple test data and expected result below (3)
  		
  	assignTestData();
//  	
		// the min cost matching of workers to jobs, 
		// with -1 indicating an unassigned worker/image
		int[] result = new ImageQuadSolver(workers, jobs).execute();

		for (int i = 0; i < result.length; i++) {
			System.out.println(i+") Quad@"+jobs.get(i).id+" :: Image#"+workers.get(i).imageName);
		}
		System.out.println("\n");
    Assert.assertTrue(Arrays.equals(new int[] {0,1,2}, result));
  }
  
  
}