package com.team254.lib.trajectory.io;

import com.team254.lib.trajectory.Trajectory;

/**
 * Serialize a path to a Java file that can be compiled into a J2ME project.
 *
 * @author Jared341
 */
public class JavaSerializer implements IPathSerializer {

  /**
   * Generate a Java source code file from a set of Trajectories.
   * 
   * For example output, see the unit test.
   * 
   * @param filename The name of the Java file *including package!*
   * @param names The filenames to associate with the trajectories.
   * @param trajectories The trajectories.  Must be the same length as names!
   * @return A complete Java file as a string.
   */
  public String serialize(String filename, String[] names,
          Trajectory[] trajectories) {
    // Split filename
    String[] split_filename = filename.split("\\.");
    String package_name = "";
    String class_name = "ERROR";
    boolean has_suffix = false;

    if (split_filename.length > 1
            && split_filename[split_filename.length - 1].equalsIgnoreCase("java")) {
      has_suffix = true;
    }

    for (int i = 0; i < split_filename.length; ++i) {
      if (!has_suffix && (i == split_filename.length - 1)) {
        class_name = split_filename[i];
        break;
      } else if (has_suffix && (i == split_filename.length - 2)) {
        class_name = split_filename[i];
        break;
      } else {
        if (i != 0) {
          package_name += ".";
        }
        package_name += split_filename[i];
      }
    }

    String contents = "package " + package_name + ";\n\n";
    contents += "import com.team254.lib.trajectory.Trajectory;\n\n";
    contents += "public class " + class_name + " {\n";
    for (int i = 0; i < names.length; ++i) {
      contents += "  public Trajectory.Segment[] " + names[i] + " = {\n";
      for (int s = 0; s < trajectories[i].getNumSegments(); ++s) {
        Trajectory.Segment seg = trajectories[i].getSegment(s);
        contents += "    new Trajectory.Segment("
                + seg.pos + ", " + seg.vel + ", " + seg.acc + ", "
                + seg.jerk + ", " + seg.heading + ", " + seg.dt + ", "
                + seg.x + ", " + seg.y + "),\n";
      }
      contents += "  };\n\n";
    }
    contents += "}\n";
    return contents;
  }

}
