package com.team254.lib.trajectory;

import com.team254.lib.trajectory.io.JavaSerializer;
import com.team254.path.Path;

/**
 *
 * @author Jared341
 */
public class Main {
  public static void main(String[] args) {
    TrajectoryGenerator.Config config = new TrajectoryGenerator.Config();
    config.dt = .01;
    config.max_acc = 12.0;
    config.max_jerk = 75.0;
    config.max_vel = 15.0;
    
    WaypointSequence p = new WaypointSequence(10);
    p.addWaypoint(new WaypointSequence.Waypoint(0, 0, 0));
    p.addWaypoint(new WaypointSequence.Waypoint(6.5, 0, 0));
    p.addWaypoint(new WaypointSequence.Waypoint(15, 5, Math.PI / 6.0));
    
    Path path = PathGenerator.makePath(p, config,
        25.5/12, "AutoMode1");

    JavaSerializer js = new JavaSerializer();
    String serialized = js.serialize(path);
    System.out.print(serialized);
  }
}
