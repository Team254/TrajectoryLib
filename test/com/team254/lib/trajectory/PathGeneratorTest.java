/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team254.lib.trajectory;

import com.team254.lib.trajectory.Trajectory.Segment;
import static com.team254.lib.trajectory.TrajectoryGeneratorTest.test;
import com.team254.lib.util.ChezyMath;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Art Kalb
 * @author Stephen Pinkerton
 * @author Jared341
 */
public class PathGeneratorTest {
  
  static double distanceToClosest(Trajectory traj, Path.Waypoint waypoint,
          Trajectory.Segment closest_segment) {
    double closest = Double.MAX_VALUE;
    int closest_id = -1;
    for (int i = 0; i < traj.getNumSegments(); ++i) {
      Segment segment = traj.getSegment(i);
      double distance = Math.sqrt(
              (segment.x-waypoint.x)*(segment.x-waypoint.x) + 
              (segment.y-waypoint.y)*(segment.y-waypoint.y));
      if (distance < closest) {
        closest = distance;
        closest_segment.x = waypoint.x;
        closest_segment.y = waypoint.y;
        closest_segment.heading = segment.heading;
        closest_id = i;
      }
    }
    System.out.println("Closest point segment #: " + closest_id);
    System.out.println("Closest point distance: " + closest);
    System.out.println("Closest point heading difference: " + 
            ChezyMath.getDifferenceInAngleRadians(closest_segment.heading, 
                    waypoint.theta));
    return closest;
  }
  
  static void test(Path path) {
    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = .01;
    config.max_acc = 250.0;
    config.max_jerk = 1250.0;
    config.max_vel = 100.0;
    Trajectory traj = PathGenerator.generateFromPath(path, config);
    
    System.out.print(traj.toStringProfile());
    System.out.print(traj.toStringEuclidean());
    System.out.println("Final distance=" +
            traj.getSegment(traj.getNumSegments()-1).pos);
    
    // The trajectory should be close (allowing for loss of precision) to each
    // desired waypoint.
    for (int i = 0; i < path.getNumWaypoints(); ++i) {
      Path.Waypoint waypoint = path.getWaypoint(i);
      Segment closest = new Segment();
      Assert.assertTrue(1 > distanceToClosest(traj, waypoint, closest));
      Assert.assertTrue(ChezyMath.getDifferenceInAngleRadians(closest.heading,
              waypoint.theta) < 1E-6);
    }
    
    Trajectory[] output = PathGenerator.makeLeftAndRightTrajectories(traj,
            20.0);
    
    //System.out.println("LEFT PROFILE:");
    //System.out.println(output[0].toString());
    //System.out.println("RIGHT PROFILE:");
    //System.out.println(output[1].toString());
  }
  
  public PathGeneratorTest() {
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
  public void testSCurveLikePath() {
    Path p = new Path(10);
    p.addWaypoint(new Path.Waypoint(0, 0, 0));
    p.addWaypoint(new Path.Waypoint(10*12, 0, 0));
    test(p);
    p.addWaypoint(new Path.Waypoint(15*12, 5*12, Math.PI/4));
    test(p);
    p.addWaypoint(new Path.Waypoint(20*12, 10*12, Math.PI/4));
    test(p);
    p.addWaypoint(new Path.Waypoint(30*12, 10*12, 0));
    test(p);
  }
  
  @Test
  public void testZigZag() {
    Path p = new Path(10);
    p.addWaypoint(new Path.Waypoint(0, 0, 0));
    p.addWaypoint(new Path.Waypoint(10, 5, 0));
    p.addWaypoint(new Path.Waypoint(30, -5, 0));
    p.addWaypoint(new Path.Waypoint(40, 0, 0));
    test(p);
  }
 
  @Test
  public void testZigZagWithHeadings() {
    Path p = new Path(10);
    p.addWaypoint(new Path.Waypoint(0, 0, 0));
    p.addWaypoint(new Path.Waypoint(5, 2.5, Math.PI/5));
    p.addWaypoint(new Path.Waypoint(25, -2.5, -Math.PI/5));
    p.addWaypoint(new Path.Waypoint(40, 0, 0));
    test(p);
  }
}
