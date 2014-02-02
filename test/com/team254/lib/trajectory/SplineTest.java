/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team254.lib.trajectory;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author jarussell
 */
public class SplineTest {
  
  public boolean almostEqual(double x, double y) {
    return Math.abs(x - y) < 1E-6;
  }
  
  public void test(double x0, double y0, double theta0, double x1, double y1,
          double theta1, boolean is_straight) {
    Spline s = new Spline();
    Assert.assertTrue(Spline.reticulateSplines(x0, y0, theta0, x1, y1, theta1,
            s));
    System.out.println(s.toString());
    
    for (double t = 0; t <= 1.0; t += .05 ) {
      System.out.println("" + t + ", " + s.valueAt(t));
    }
    
    Assert.assertTrue(almostEqual(s.valueAt(0), y0));
    Assert.assertTrue(almostEqual(s.valueAt(1), y1));
    
    Assert.assertTrue(almostEqual(s.angleAt(0), theta0));
    Assert.assertTrue(almostEqual(s.angleAt(1), theta1));
    
    if (is_straight) {
      double expected = Math.sqrt((x1-x0)*(x1-x0) + (y1-y0)*(y1-y0));
      System.out.println("Expected length=" + expected + "; actual=" +
              s.calculateLength());
      Assert.assertTrue(almostEqual(s.calculateLength(), expected));
    }
  }
  
  public void expectFailure(double x0, double y0, double theta0, double x1,
          double y1, double theta1) {
    Spline s = new Spline();
    Assert.assertFalse(Spline.reticulateSplines(x0, y0, theta0, x1, y1, theta1,
            s));
  }
  
  public SplineTest() {
  }
  
  @BeforeClass
  public static void setUpClass() {
  }
  
  @AfterClass
  public static void tearDownClass() {
  }
  
  @Before
  public void setUp() {
  }
  
  @After
  public void tearDown() {
  }
  
  @Test
  public void testUnitLines() {
    test(0, 0, 0, 1, 0, 0, true);
    test(0, 0, Math.PI / 4, Math.sqrt(2)/2, Math.sqrt(2)/2, Math.PI / 4, true);
  }
  
  @Test
  public void testNonUnitLines() {
    test(0, 0, 0, 5, 0, 0, true);
    test(0, 0, Math.PI / 4, Math.sqrt(2), Math.sqrt(2), Math.PI / 4, true);
  }
  
  @Test
  public void testTranslatedUnitLines() {
    test(1, 1, 0, 2, 1, 0, true);
    test(1, 1, Math.PI / 4, Math.sqrt(2)/2+1, Math.sqrt(2)/2+1, Math.PI / 4,
            true);
  }
  
  @Test
  public void testTranslatedNonUnitLines() {
    test(1, 1, 0, 6, 1, 0, true);
    test(1, 1, Math.PI / 4, Math.sqrt(2) + 1, Math.sqrt(2) + 1, Math.PI / 4,
            true);
  }
  
  @Test
  public void testUnitStep() {
    test(0, 0, 0, Math.sqrt(2)/2, Math.sqrt(2)/2, 0, false);
  }
  
  @Test
  public void testNonUnitStep() {
    test(0, 0, 0, 10, 20, 0, false);
  }
  
  @Test
  public void testTranslatedStep() {
    test(0, 5, 0, 10, 20, 0, false);
  }
  
  @Test
  public void testInvalidInput() {
    expectFailure(0, 0, 0, 0, 0, 0);
    expectFailure(0, 0, -Math.PI / 2, 1, 0, 0);
  }
}
