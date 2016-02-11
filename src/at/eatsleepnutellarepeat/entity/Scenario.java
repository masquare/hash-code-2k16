package at.eatsleepnutellarepeat.entity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by martinmaritsch on 06/02/16.
 */
public class Scenario {

  private int rowsCount;
  private int columnsCount;
  private int dronesCount;
  private int turnsCount;
  private int maxWeight;


  private int productCount;
  private int warehouseCount;
  private int orderCount;

  private List<Drone> drones = new ArrayList<>();
  private List<Product> products = new ArrayList<>();
  private List<Warehouse> warehouses = new ArrayList<>();
  private List<Order> orders = new ArrayList<>();

  private Scenario() {
  }

  public void calculate() {
    // do proper calculations
  }

  public static Scenario parseFromFile(String filename) throws IOException {
    Scenario scenario = new Scenario();

    List<String> lines = Files.readAllLines(Paths.get(filename));

    Iterator<String> linesIt = lines.iterator();

    // GENERAL SETUP
    String[] elements = linesIt.next().split(" ");
    scenario.rowsCount = Integer.parseInt(elements[0]);
    scenario.columnsCount = Integer.parseInt(elements[1]);
    scenario.dronesCount = Integer.parseInt(elements[2]);
    scenario.turnsCount = Integer.parseInt(elements[3]);
    scenario.maxWeight = Integer.parseInt(elements[4]);


    // GENERATE DRONES
    for(int i = 0; i < scenario.dronesCount; i++) {
      scenario.drones.add(new Drone(i, scenario.maxWeight, new Coordinates(0, 0)));
    }


    // PRODUCTS
    scenario.productCount = Integer.parseInt(linesIt.next());

    String[] productElements = linesIt.next().split(" ");
    for(int i = 0; i < productElements.length; i++) {
      scenario.products.add(new Product(i, Integer.parseInt(productElements[i])));
    }


    // WAREHOUSES
    scenario.warehouseCount = Integer.parseInt(linesIt.next());

    for(int i = 0; i < scenario.warehouseCount; i++) {
      String[] coordElements = linesIt.next().split(" ");
      Coordinates c = new Coordinates(Integer.parseInt(coordElements[0]), Integer.parseInt(coordElements[1]));
      String[] productCount = linesIt.next().split(" ");

      TreeMap<Product, Integer> products = new TreeMap<>();
      for(int j = 0; j < productCount.length; j++) {
        products.put(scenario.products.get(j), Integer.parseInt(productCount[j]));
      }

      scenario.warehouses.add(new Warehouse(i, c, products));
    }

    // ORDERS
    scenario.orderCount = Integer.parseInt(linesIt.next());

    for(int i = 0; i < scenario.orderCount; i++) {
      String[] coordElements = linesIt.next().split(" ");
      Coordinates c = new Coordinates(Integer.parseInt(coordElements[0]), Integer.parseInt(coordElements[1]));
      int productCount = Integer.parseInt(linesIt.next());

      String[] productIds = linesIt.next().split(" ");

      TreeMap<Product, Integer> products = new TreeMap<>();
      for (int j = 0; j < productIds.length; j++) {
        Product product = scenario.products.get(Integer.parseInt(productIds[j]));
        if (products.containsKey(product)) {
          products.put(product, products.get(product) + 1);
        } else {
          products.put(product, 1);
        }
      }

      scenario.orders.add(new Order(c, products));
    }

    /*for(int i = 0; i < lines.size(); i++) {
      String line = lines.get(i);
      if(i == 1) {
        // do something
        String[] elements = line.split(" ");
        break;
      }
      // do something more
      break;
    }*/

    return scenario;
  }

  public void writeToFile(String fileName) throws IOException {
    PrintStream ps = new PrintStream(new FileOutputStream(fileName));

    //write output ...

    ps.flush();
    ps.close();
  }
}
