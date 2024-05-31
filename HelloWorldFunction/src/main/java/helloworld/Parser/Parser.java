package helloworld.Parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public interface Parser {
  public ArrayList<JavaEntity> parseSourceFiles() throws IOException;
}
