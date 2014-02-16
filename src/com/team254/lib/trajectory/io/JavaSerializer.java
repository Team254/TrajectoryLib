package com.team254.lib.trajectory.io;

import com.team254.lib.trajectory.Trajectory;
import com.team254.path.Path;

/**
 * Serialize a path to a Java file that can be compiled into a J2ME project.
 *
 * @author Jared341
 */
public class JavaSerializer implements IPathSerializer {

  /**
   * Generate a Java source code file from a Path
   * 
   * For example output, see the unit test.
   * 
   * @param path The path to serialize.
   * @return A complete Java file as a string.
   */
  public String serialize(Path path) {
    String contents = "package com.team254.frc2014.paths;\n\n";
    contents += "import com.team254.lib.trajectory.Trajectory;\n";
    contents += "import com.team254.path.Path;\n\n";
    contents += "public class " + path.getName() + " extends Path {\n";
    path.goLeft();
    contents += serializeTrajectory("kGoLeftLeftWheel", 
            path.getLeftWheelTrajectory());
    contents += serializeTrajectory("kGoLeftRightWheel", 
            path.getRightWheelTrajectory());
    path.goRight();
    contents += serializeTrajectory("kGoRightLeftWheel", 
            path.getLeftWheelTrajectory());
    contents += serializeTrajectory("kGoRightRightWheel", 
            path.getRightWheelTrajectory());
    
    contents += "  public " + path.getName() + "() {\n";
    contents += "    super(\"" + path.getName() + "\",\n";
    contents += "      new Trajectory.Pair(kGoLeftLeftWheel, kGoLeftRightWheel),\n";
    contents += "      new Trajectory.Pair(kGoRightLeftWheel, kGoRightRightWheel));\n";
    contents += "  }\n\n";

    contents += "}\n";
    return contents;
  }
  
  private String serializeTrajectory(String name, Trajectory traj) {
    String contents = 
            "  private static final Trajectory " + name + " = new Trajectory( new Trajectory.Segment[] {\n";
    for (int i = 0; i < traj.getNumSegments(); ++i) {
      Trajectory.Segment seg = traj.getSegment(i);
      contents += "    new Trajectory.Segment("
              + seg.pos + ", " + seg.vel + ", " + seg.acc + ", "
              + seg.jerk + ", " + seg.heading + ", " + seg.dt + ", "
              + seg.x + ", " + seg.y + "),\n";
    }
    contents += "  });\n\n";
    return contents;
  }

}
