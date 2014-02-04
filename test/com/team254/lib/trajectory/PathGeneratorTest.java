/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team254.lib.trajectory;

import com.team254.lib.trajectory.Trajectory.Segment;
import static com.team254.lib.trajectory.TrajectoryGeneratorTest.test;
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
  
  static double distanceToClosest(Trajectory traj, Path.Waypoint waypoint) {
    double closest = Double.MAX_VALUE;
    for (int i = 0; i < traj.getNumSegments(); ++i) {
      Segment segment = traj.getSegment(i);
      double distance = Math.sqrt(
              (segment.x-waypoint.x)*(segment.x-waypoint.x) + 
              (segment.y-waypoint.y)*(segment.y-waypoint.y));
      closest = Math.min(distance, closest);
    }
    System.out.println("Closest point distance: " + closest);
    return closest;
  }
  
  static void test(Path path) {
    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = .01;
    config.max_acc = 250.0;
    config.max_jerk = 1250.0;
    config.max_vel = 100.0;
    Trajectory traj = PathGenerator.generateFromPath(path, config);
    
    // TODO: Check some stuff
    System.out.print(traj.toStringProfile());
    System.out.print(traj.toStringEuclidean());
    System.out.println("Final distance=" +
            traj.getSegment(traj.getNumSegments()-1).pos);
    
    for (int i = 0; i < path.getNumWaypoints(); ++i) {
      Path.Waypoint waypoint = path.getWaypoint(i);
      Assert.assertTrue(4 > distanceToClosest(traj, waypoint));
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
}
