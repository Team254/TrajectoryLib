/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team254.lib.trajectory.io;

import com.team254.lib.trajectory.WaypointSequence;
import com.team254.lib.trajectory.WaypointSequence.Waypoint;
import com.team254.lib.trajectory.PathGenerator;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.TrajectoryGenerator;
import com.team254.lib.trajectory.io.JavaSerializer;
import com.team254.path.Path;
import junit.framework.Assert;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jarussell
 */
public class SerializationTest {

  public SerializationTest() {
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
  public void testJavaSerialization() {
    WaypointSequence p = new WaypointSequence(10);
    p.addWaypoint(new Waypoint(0, 0, 0));
    p.addWaypoint(new Waypoint(10, 0, 0));
    p.addWaypoint(new Waypoint(20, 20, Math.PI / 4));

    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = .01;
    config.max_acc = 1000.0;
    config.max_jerk = 5000.0;
    config.max_vel = 100.0;

    Path path = PathGenerator.makePath(p, config, 25.0, "TestPath");

    JavaSerializer js = new JavaSerializer();
    String serialized = js.serialize(path);
    System.out.print(serialized);

    //Assert.assertEquals(serialized, kGoldenOutput);
  }
  
  @Test
  public void testTextFileSerialization() {
    WaypointSequence p = new WaypointSequence(10);
    p.addWaypoint(new Waypoint(0, 0, 0));
    p.addWaypoint(new Waypoint(10, 0, 0));
    p.addWaypoint(new Waypoint(20, 20, Math.PI / 4));

    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = .01;
    config.max_acc = 1000.0;
    config.max_jerk = 5000.0;
    config.max_vel = 100.0;

    Path path = PathGenerator.makePath(p, config, 25.0, "TestPath");

    TextFileSerializer tf = new TextFileSerializer();
    String serialized = tf.serialize(path);
    System.out.print(serialized);
    
    TextFileDeserializer tfd = new TextFileDeserializer();
    Path deserialized = tfd.deserialize(serialized);
    
    Assert.assertEquals("TestPath", deserialized.getName());
    Assert.assertEquals(deserialized.getLeftWheelTrajectory().getNumSegments(), 
            path.getLeftWheelTrajectory().getNumSegments());
    Assert.assertEquals(deserialized.getRightWheelTrajectory().getNumSegments(), 
            path.getRightWheelTrajectory().getNumSegments());
  }
  
  @Test
  public void testJavaStringSerializer() {
    WaypointSequence p = new WaypointSequence(10);
    p.addWaypoint(new Waypoint(0, 0, 0));
    p.addWaypoint(new Waypoint(10, 0, 0));
    p.addWaypoint(new Waypoint(20, 20, Math.PI / 4));

    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = .01;
    config.max_acc = 1000.0;
    config.max_jerk = 5000.0;
    config.max_vel = 100.0;

    Path path = PathGenerator.makePath(p, config, 25.0, "TestPath");

    JavaStringSerializer tf = new JavaStringSerializer();
    String serialized = tf.serialize(path);
    System.out.print(serialized);
  }
}
