/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.team254.lib.trajectory;

import com.team254.lib.trajectory.Trajectory.Segment;
import static com.team254.lib.trajectory.TrajectoryGenerator.SCurvesStrategy;
import static com.team254.lib.trajectory.TrajectoryGenerator.generate;
import com.team254.lib.util.ChezyMath;

/**
 * Generate a smooth Trajectory from a Path.
 * 
 * @author Art Kalb
 * @author Stephen Pinkerton
 * @author Jared341
 */
public class PathGenerator {
  public static Trajectory generateFromPath(Path path,
          TrajectoryGenerator.Config config) {
    if (path.getNumWaypoints() < 2) {
      return null;
    }
    
    // Compute the total length of the path by creating splines for each pair
    // of waypoints.
    Spline[] splines = new Spline[path.getNumWaypoints() - 1];
    double[] spline_lengths = new double[splines.length];
    double total_distance = 0;
    for (int i = 0; i < splines.length; ++i) {
      splines[i] = new Spline();
      if (!Spline.reticulateSplines(path.getWaypoint(i), 
              path.getWaypoint(i+1), splines[i])) {
        return null;
      }
      spline_lengths[i] = splines[i].calculateLength();
      total_distance += spline_lengths[i];
    }
    
    // Generate a smooth trajectory over the total distance.
    Trajectory traj = generate(config, SCurvesStrategy, 0.0,
            path.getWaypoint(0).theta, total_distance, 0.0,
            path.getWaypoint(0).theta);
    
    // Assign headings based on the splines.
    int cur_spline = 0;
    double cur_spline_start_pos = 0;
    for (int i = 0; i < traj.getNumSegments(); ++i) {
      double cur_pos = traj.getSegment(i).pos;
      
      boolean found_spline = false;
      while (!found_spline) {
        double cur_pos_relative = cur_pos - cur_spline_start_pos;
        if (cur_pos_relative <= spline_lengths[cur_spline]) {
          double percentage = cur_pos_relative / spline_lengths[cur_spline];
          traj.getSegment(i).heading = splines[cur_spline].angleAt(percentage);
          traj.getSegment(i).delta_heading =
                  splines[cur_spline].angleChangeAt(percentage);
          found_spline = true;
        } else if (cur_spline < splines.length) {
          cur_spline_start_pos = cur_pos;
          ++cur_spline;
        } else {
          traj.getSegment(i).heading = splines[splines.length].angleAt(1.0);
          traj.getSegment(i).delta_heading = 
                  splines[splines.length].angleChangeAt(1.0);
        }
      }
    }
    
    return traj;
  }
  
  public static Trajectory[] makeLeftAndRightTrajectories(Trajectory input,
          double wheelbase_width) {
    Trajectory[] output = new Trajectory[2];
    output[0] = input.copy();
    output[1] = input.copy();
    Trajectory left = output[0];
    Trajectory right = output[1];
    
    double left_total = 0;
    double right_total = 0;
    for (int i = 0; i < input.getNumSegments(); ++i) {      
      Segment current = input.getSegment(i);
      Segment next = current;
      if (i < input.getNumSegments() - 1) {
        next = input.getSegment(i+1);
      }
      double delta_heading = ChezyMath.getDifferenceInAngleRadians(next.heading,
              current.heading);
      Segment inner, outer;
      double inner_last_pos, outer_last_pos;
      if (delta_heading > 0) {
        outer = right.getSegment(i);
        inner = left.getSegment(i);
        outer_last_pos = right_total;
        inner_last_pos = left_total;
      } else {
        outer = left.getSegment(i);
        inner = right.getSegment(i);
        outer_last_pos = left_total;
        inner_last_pos = right_total;
      }

      double radius, scaling_inner, scaling_outer;
      if (Math.abs(delta_heading) > 1E-6) {
        radius = current.vel * current.dt / Math.abs(delta_heading);
        scaling_inner = (radius - wheelbase_width/2) / radius;
        scaling_outer = (radius + wheelbase_width/2) / radius;
      } else {
        scaling_inner = 1;
        scaling_outer = 1;
      }
      inner.vel = scaling_inner * current.vel;
      double inner_delta = scaling_inner * inner.vel * inner.dt;
      inner.pos = inner_last_pos + inner_delta;
      inner.acc = scaling_inner * current.acc;
      inner.jerk = scaling_inner * current.jerk;

      outer.vel = scaling_outer * current.vel;
      double outer_delta = scaling_outer * outer.vel * outer.dt;
      outer.pos = outer_last_pos + outer_delta;
      outer.acc = scaling_outer * current.acc;
      outer.jerk = scaling_outer * current.jerk;
      
      if (delta_heading > 0) {
        left_total += inner_delta;
        right_total += outer_delta;
      } else {
        left_total += outer_delta;
        right_total += inner_delta;
      }
    }
    
    return output;
  }
}
