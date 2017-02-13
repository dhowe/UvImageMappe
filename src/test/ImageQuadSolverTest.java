package test;

import java.util.Arrays;

import org.junit.*;

import uvm.*;

public class ImageQuadSolverTest {

  @Test
  public void testComputeCost() {
  	
  	// TODO: add simple test data and expected result below (2)

		UvImage[] workers = null;
		Quad[] jobs = null;
		
		// Test 1 -----------------------------
		double expectedCost1 = .56788;
  	int imageIndex1 = 0, quadIndex1 = 0;
  	
  	double result1 = new ImageQuadSolver(workers, jobs).cost(imageIndex1, quadIndex1);
    Assert.assertTrue(expectedCost1 == result1);
    
    // Test 2 -----------------------------
		double expectedCost2 = .56788;
  	int imageIndex2 = 0, quadIndex2 = 0;
  	
		double result2 = new ImageQuadSolver(workers, jobs).cost(imageIndex2, quadIndex2);
    Assert.assertTrue(expectedCost2 == result2);
    
    // Test 3 -----------------------------
		double expectedCost3 = .56788;
  	int imageIndex3 = 0, quadIndex3 = 0;
  	
		double result3 = new ImageQuadSolver(workers, jobs).cost(imageIndex3, quadIndex3);
    Assert.assertTrue(expectedCost3 == result3);
  }
  
  @Test
  public void testExecute1() {
  	
  	// TODO: add simple test data and expected result below (3)
  		
		UvImage[] workers = null;
		Quad[] jobs = null;
		
		// the min cost matching of workers to jobs, 
		// with -1 indicating an unassigned worker/image
		int[] result = new ImageQuadSolver(workers, jobs).execute();
		
		for (int i = 0; i < result.length; i++) {
			System.out.println(i+") Quad@"+jobs[i].id+" :: Image#"+workers[i].imageName);
		}
		
    Assert.assertTrue(Arrays.equals(new int[] { /* Expected Result Here */ }, result));
  }
  
}