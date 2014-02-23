package com.team254.lib.trajectory.io;

import com.team254.lib.trajectory.Path;
import java.util.StringTokenizer;

/**
 * Save to a Java class with a static string, because J2ME has problems with
 * large arrays.
 * 
 * @author Jared341
 */
public class JavaStringSerializer implements IPathSerializer {

  public String serialize(Path path) {
    String contents = "package com.team254.frc2014.paths;\n\n";
    contents += "import com.team254.lib.trajectory.Trajectory;\n";
    contents += "import com.team254.lib.trajectory.io.TextFileDeserializer;\n";
    contents += "import com.team254.path.Path;\n\n";
    contents += "public class " + path.getName() + " extends Path {\n";
  
    TextFileSerializer serializer = new TextFileSerializer();
    String serialized = serializer.serialize(path);
    
    // J2ME can't parse multi line string literals.
    StringTokenizer tokenizer = new StringTokenizer(serialized, "\n");
    contents += "  private static final String kSerialized = \"" + 
            tokenizer.nextToken() + "\\n\"\n";
    while (tokenizer.hasMoreTokens()) {
      contents += "    + \"" + tokenizer.nextToken() + "\\n\"";
      if (tokenizer.hasMoreTokens()) {
        contents += "\n";
      } else {
        contents += ";\n\n";
      }
    }
    
    contents += "  public " + path.getName() + "() {\n";
    contents += "     TextFileDeserializer d = new TextFileDeserializer();\n";
    contents += "     Path p = d.deserialize(kSerialized);\n";
    contents += "     this.name_ = p.getName();\n";
    contents += "     this.go_left_pair_ = p.getPair();\n";
    contents += "  }\n\n";

    contents += "}\n";
    return contents;
  }
  
}
