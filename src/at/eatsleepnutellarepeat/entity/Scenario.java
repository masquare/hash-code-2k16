package at.eatsleepnutellarepeat.entity;

import at.eatsleepnutellarepeat.entity.command.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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

    for(int round = 0; round < turnsCount; round++) {
      System.out.println("Round: " + round);
      for(Drone d : drones) {
        if(!d.isBusy()) {
          if(isDroneAtWarehouse(d)) {
            Warehouse warehouse = getDroneWarehouse(d);
            int bestOrderWeight = -1;
            Order bestOrder = null;
            for(Order o : orders) {
              int orderWeight = calculateMaxDeliverableWeight(warehouse, o);
              if(orderWeight > bestOrderWeight) {
                bestOrderWeight = orderWeight;
                bestOrder = o;
              }
            }
            if(bestOrderWeight == -1) {
              //TODO refactor
              //warehouses.remove(warehouse);
              Warehouse best = getBestWarehouse(d);
              if(best != null) {
                int distance = Coordinates.distance(best.coordinates, d.position);
                for(int i = 0; i < distance; i++) {
                  d.addCommand(new Move());
                }
                d.position = new Coordinates(best.coordinates);
              } else {
                d.addCommand(new Wait(d));
              }
            } else {
              for(Map.Entry<Product, Integer> e : warehouse.products.entrySet()) {
                if (bestOrder.products.containsKey(e.getKey())) {
                  int available = bestOrder.products.get(e.getKey());
                  for (int j = 0; j < available; j++) {
                    if (warehouse.products.containsKey(e.getKey()) && warehouse.products.get(e.getKey()) > 0 && d.getCurrentWeight() + e.getKey().weight <= maxWeight) {
                      d.loadProduct(e.getKey());
                      d.addCommand(new Load(d, warehouse, e.getKey(), 1));
                      bestOrder.removeProduct(e.getKey());
                      warehouse.takeProduct(e.getKey());
                    }
                  }
                }
              }
              int distance = Coordinates.distance(bestOrder.coordinates, d.position);
              for (int j = 0; j < distance; j++) {
                d.addCommand(new Move());
              }
              for(Map.Entry<Product, Integer> e : d.products.entrySet()) {
                d.addCommand(new Deliver(d, bestOrder, e.getKey(), e.getValue()));
              }
              d.position = new Coordinates(bestOrder.coordinates);
            }
          } else if (isDroneAtOrder(d)){
            Order order = getDroneOrder(d);
            if(order.isFinished()) {
              orders.remove(order);
            } else {
              // fly to warehouse
              Warehouse warehouse = getBestWarehouseForOrder(d, order);
              int distance = Coordinates.distance(warehouse.coordinates, d.position);
              for(int i = 0; i < distance; i++) {
                d.addCommand(new Move());
              }
              d.position = new Coordinates(warehouse.coordinates);
            }

          } else {
            // drone is at 0|0
            Warehouse warehouse = getBestWarehouse(d);
            int distance = Coordinates.distance(warehouse.coordinates, d.position);
            for(int i = 0; i < distance; i++) {
              d.addCommand(new Move());
            }
            d.position = new Coordinates(warehouse.coordinates);
          }
        }
      }

      for(Drone d : drones) {
        d.tick();
      }

      if(orders.isEmpty()) {
        break;
      }
    }
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

      scenario.orders.add(new Order(i, c, products));
    }

    return scenario;
  }

  public void writeToFile(String fileName) throws IOException {
    PrintStream ps = new PrintStream(new FileOutputStream(fileName));

    // GET COMMAND COUNT
    int count = 0;
    for(Drone d : drones) {
      count += d.processedCommands.size();
    }

    ps.println(count);

    for(Drone d : drones) {
      for(ICommand c : d.processedCommands) {
        ps.println(c.print());
      }
    }
    ps.flush();
    ps.close();
  }

  public boolean isDroneAtWarehouse(Drone d) {
    for(Warehouse w : warehouses) {
      if (w.coordinates.equals(d.position)) {
        return true;
      }
    }
    return false;
  }

  public Warehouse getDroneWarehouse(Drone d) {
    for(Warehouse w : warehouses) {
      if (w.coordinates.equals(d.position)) {
        return w;
      }
    }
    return null;
  }

  public boolean isDroneAtOrder(Drone d) {
    for(Order o : orders) {
      if (o.coordinates.equals(d.position)) {
        return true;
      }
    }
    return false;
  }

  public Order getDroneOrder(Drone d) {
    for(Order o : orders) {
      if (o.coordinates.equals(d.position)) {
        return o;
      }
    }
    return null;
  }

  public int calculateMaxDeliverableWeight(Warehouse w, Order o) {
    int weight = -1;
    for(Map.Entry<Product, Integer> e : w.products.entrySet()) {
      int availableCount = e.getValue();
      if(availableCount > 0 && o.products.containsKey(e.getKey())) {
        int neededCount = o.products.get(e.getKey());
        weight += Math.min(availableCount, neededCount);
      }
    }
    return weight;
  }

  public Warehouse getBestWarehouse(Drone d) {
    int distance = -1;
    Warehouse bestWarehouse = null;
    for(Warehouse w : warehouses) {
      if(!w.coordinates.equals(d.position)) {
        int newDistance = Coordinates.distance(w.coordinates, d.position);
        if(newDistance < distance || distance == -1) {
          distance = newDistance;
          bestWarehouse = w;
        }
      }
    }
    if(bestWarehouse == null) {
      System.out.println("NULL");
    }
    return bestWarehouse; // TODO what if only one warehouse available?
  }

  public Order getBestOrder(Drone d) {
    int distance = -1;
    Order bestOrder = null;
    for(Order o : orders) {
      if(!o.coordinates.equals(d.position)) {
        int newDistance = Coordinates.distance(o.coordinates, d.position);
        if(newDistance < distance) {
          distance = newDistance;
          bestOrder = o;
        }
      }
    }
    return bestOrder;
  }

  private Warehouse getBestWarehouseForOrder(Drone d, Order order) {
    int bestWeight = -1;
    Warehouse bestWarehouse = null;
    for(Warehouse w : warehouses) {
      int weight = 0;
      for(Map.Entry<Product, Integer> e : order.products.entrySet()) {
        if(w.products.containsKey(e.getKey())) {
          int available = w.products.get(e.getKey());
          if(available > 0) {
            weight += Math.min(available * e.getKey().weight, e.getValue() * e.getKey().weight);
          }
        }
      }
      if(weight < bestWeight || bestWeight == -1) {
        bestWeight = weight;
        bestWarehouse = w;
      }
    }
    return bestWarehouse;
  }
}
