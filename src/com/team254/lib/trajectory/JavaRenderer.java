package com.team254.lib.trajectory;

import java.util.Date;

/**
 *
 */
public class JavaRenderer {
  public JavaRenderer() {
    
  }
  
  public String render(Path path, Trajectory left, Trajectory right,
          String package_path, String name) {
    String result = "";
    
    // Boilerplate
    result += "package " + package_path + ";\n\n";
    result += "// File Generated: " + new Date() + "\n";
    result += "public class " + name + " {\n";
    
    // TODO: The result.  Write all of the data to the generated java file.
    
    return result;
  }
}
