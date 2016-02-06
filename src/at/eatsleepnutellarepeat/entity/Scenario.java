package at.eatsleepnutellarepeat.entity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by martinmaritsch on 06/02/16.
 */
public class Scenario {

  private Scenario() {
  }

  public void calculate() {
    // do proper calculations
  }

  public static Scenario parseFromFile(String filename) throws IOException {
    Scenario scenario = new Scenario();

    List<String> lines = Files.readAllLines(Paths.get(filename));

    for(int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      if(i == 1) {
        // do something
        String[] elements = line.split(" ");
        break;
      }
      // do something more
      break;
    }

    return scenario;
  }

  public void writeToFile(String fileName) throws IOException {
    PrintStream ps = new PrintStream(new FileOutputStream(fileName));

    //write output ...

    ps.flush();
    ps.close();
  }
}
