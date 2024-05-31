package helloworld.Parser;

import helloworld.DataFetcher.GithubDataFetcher;
import helloworld.FileSingleton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SourceCodeParser implements Parser {
  private ArrayList<JavaEntity> entities;

  public SourceCodeParser() {
    this.entities = new ArrayList<>();
  };

  @Override
  public ArrayList<JavaEntity> parseSourceFiles() throws IOException {
    JavaEntityFactory entityFactory = new JavaEntityFactory();
    ArrayList<File> sourceCodeFiles = FileSingleton.getInstance().getFiles();
    // Iterate through the provided ArrayList of Files
    for (File file : sourceCodeFiles) {
      String path =  file.getPath();
      if (!path.contains(FileSingleton.getInstance().getUuid().toString())) {
        System.out.println("File " + file.getName() + " does not belong to the current request");
        continue;
      }
      JavaEntity entity = entityFactory.createEntityFromFile(file);
      if (entity != null) {
        entities.add(entity);
      } else {
        System.out.println(file.getName() + " is not a valid java file");
//        System.out.println("File content:\n" + Files.readString(Paths.get(file.toURI())));
      }
    }
    GithubDataFetcher.clearFiles();
    return entities;
  }
}
