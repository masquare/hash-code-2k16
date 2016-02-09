package at.eatsleepnutellarepeat.entity;

import javafx.util.Pair;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by martinmaritsch on 06/02/16. nice
 */
public class Scenario {

  private Pair<Integer, Integer> size;
  private Map<Coordinates, Boolean> painting = new TreeMap<Coordinates, Boolean>();
  private Map<Coordinates, Integer> solution = new TreeMap<Coordinates, Integer>();

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
      if(i == 0) {
        // do something
        String[] elements = line.split(" ");
        scenario.size = new Pair<Integer, Integer>(Integer.parseInt(elements[0]), Integer.parseInt(elements[1]));
        continue;
      }
      for(int j = 0; j < line.length(); j++) {
        scenario.painting.put(new Coordinates(i-1, j), line.charAt(j) == '#');
      }
    }

    return scenario;
  }
  
  public static boolean NeighborsToColor(boolean[] array)
  {
      for(boolean b : array) if(!b) return false;
      return true;
  }	    
  
  public void writeToFile(String fileName) throws IOException {
    PrintStream ps = new PrintStream(new FileOutputStream(fileName));

    long count = painting.values().stream().filter((v) -> v).count();
    ps.println(count);
    painting.forEach((k, v) -> {
      if(v) {
    	  int neighbors = 0;
    	  painting.forEach((k1,v1) -> {
    		  if( ( (k.distanceSqrtTo(k1)==1) || (k.distanceSqrtTo(k1)==2) ) && (v1) ) {
    			//whuppsi\\neighbors++; 
    		  }
    	  });
    	  if(neighbors==8){
    		  solution.put(k,2);
    	  }
    	  else{
    		  solution.put(k,1);
    	  }
      
    	  
       // ps.println("PAINT_SQUARE " + k.getX() + " " + k.getY() + " 0");
       // System.out.println("PAINT_SQUARE " + k.getX() + " " + k.getY() + " 0");
      }

    });

    //delete everything unnecessary from solution
    
    solution.forEach((k, z) -> {
    	int r = (z-1)/2;
    	 ps.println("PAINT_SQUARE " + k.getX() + " " + k.getY() + " " + r);
         System.out.println("PAINT_SQUARE " + k.getX() + " " + k.getY() + " " + r);
    });
    
    
    ps.flush();
    ps.close();
  }
}
