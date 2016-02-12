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
    for(int round = 0; round < turnsCount; round++) {
      //System.out.println("Round: " + round);
      for(Drone d : drones) {
        if(d.isAvailable()) {
          if(isDroneAtWarehouse(d)) {
            Warehouse warehouse = getDroneWarehouse(d);
            Order bestOrder = getBestOrder(d, warehouse);
            if(bestOrder == null) {
              // move to next Warehouse
              Warehouse bestWarehouse = getBestWarehouse(d);
              if(bestWarehouse != null) {
                d.moveToCoordinates(bestWarehouse.coordinates);
              } else {
                d.addCommand(new Wait(d));
              }
            } else {
              // execute bestOrder
              d.storage = warehouse.storage.getBestSubStorage(bestOrder.storage, maxWeight);

              // build commands for loading
              Iterator<Map.Entry<Product, Integer>> it = d.storage.getProductsIterator();
              while(it.hasNext()) {
                Map.Entry<Product, Integer> entry = it.next();
                d.addCommand(new Load(d, warehouse, entry.getKey(), entry.getValue()));
              }

              // remove stuff from warehouse where we took it from
              warehouse.storage.removeAllFromStorage(d.storage);

              // move to order
              d.moveToCoordinates(bestOrder.coordinates);

              // build commands for unloading
              it = d.storage.getProductsIterator();
              while(it.hasNext()) {
                Map.Entry<Product, Integer> entry = it.next();
                d.addCommand(new Deliver(d, bestOrder, entry.getKey(), entry.getValue()));
              }
              bestOrder.storage.removeAllFromStorage(d.storage);

              d.storage.removeAllFromStorage(new Storage(d.storage));

              if(d.storage.getWeight() != 0) {
                throw new RuntimeException("Drone should be empty...");
              }
            }
          } else if (isDroneAtOrder(d)){
            Order order = getDroneOrder(d);
            if(order.isFinished()) {
              orders.remove(order);

              // fly to next best warehouse
              Warehouse warehouse = getBestWarehouse(d);
              d.moveToCoordinates(warehouse.coordinates);
            } else {
              // fly to best warehouse for order
              Warehouse warehouse = getBestWarehouseForOrder(d, order);
              d.moveToCoordinates(warehouse.coordinates);
            }

          } else {
            // fly to next best warehouse
            Warehouse warehouse = getBestWarehouse(d);
            d.moveToCoordinates(warehouse.coordinates);
            //throw new IllegalStateException("Must never land here");
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

      Storage storage = new Storage();
      for(int j = 0; j < productCount.length; j++) {
        int count = Integer.parseInt(productCount[j]);
        if(count > 0) {
          storage.insertProduct(scenario.products.get(j), count);
        }
      }
      scenario.warehouses.add(new Warehouse(i, c, storage));

      //scenario.warehouses.get(i).storage.getProductsIterator().forEachRemaining(e -> System.out.println(e.getKey().weight + " -> " + e.getValue()));

      // at the beginning all drones are at the first warehouse
      if(i == 0) {
        for(Drone d : scenario.drones) {
          d.coordinates = new Coordinates(c);
        }
      }
    }

    // ORDERS
    scenario.orderCount = Integer.parseInt(linesIt.next());

    for(int i = 0; i < scenario.orderCount; i++) {
      String[] coordElements = linesIt.next().split(" ");
      Coordinates c = new Coordinates(Integer.parseInt(coordElements[0]), Integer.parseInt(coordElements[1]));
      int productCount = Integer.parseInt(linesIt.next());

      String[] productIds = linesIt.next().split(" ");

      Storage storage = new Storage();
      for (int j = 0; j < productIds.length; j++) {
        storage.insertProduct(scenario.products.get(Integer.parseInt(productIds[j])));
      }

      scenario.orders.add(new Order(i, c, storage));
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
    return warehouses.stream().anyMatch(warehouse -> warehouse.coordinates.equals(d.coordinates));
  }

  public Warehouse getDroneWarehouse(Drone d) {
    return warehouses.stream().filter(warehouse -> warehouse.coordinates.equals(d.coordinates)).findFirst().orElse(null);
  }

  public boolean isDroneAtOrder(Drone d) {
    return orders.stream().anyMatch(order -> order.coordinates.equals(d.coordinates));
  }

  public Order getDroneOrder(Drone d) {
    return orders.stream().filter(order -> order.coordinates.equals(d.coordinates)).findFirst().orElse(null);
  }

  public Warehouse getBestWarehouse(Drone d) {
    int distance = -1;
    Warehouse bestWarehouse = null;
    if(warehouses.size() == 1) {
      return warehouses.get(0);
    }
    for(Warehouse w : warehouses) {
      if(!w.coordinates.equals(d.coordinates)) {
        int tempDistance = d.coordinates.distanceTo(w.coordinates);
        if(tempDistance < distance || distance == -1) {
          distance = tempDistance;
          bestWarehouse = w;
        }
      }
    }
    return bestWarehouse;
  }

  public Order getBestOrder(Drone drone, Warehouse warehouse) {
    double bestValue = Double.POSITIVE_INFINITY;
    Order bestOrder = null;

    if(orders.size() == 1) {
      return orders.get(0);
    }
//    for(Order order : orders) {
//        if(!order.coordinates.equals(drone.coordinates)) {
//          //int newDistance = Coordinates.distance(order.coordinates, drone.coordinates);
//
//          double newDistance = 1000 / (order.storage.getWeight() + 0.001) + //prioritize close to finish orders
//                               30 * warehouse.storage.containsWeightOfOtherStorage(order.storage) / (order.storage.getWeight() + 0.001) +
//                               1000 * Coordinates.distance(order.coordinates, drone.coordinates) / Math.sqrt(rowsCount*rowsCount + columnsCount*columnsCount);
//          if(warehouse.storage.containsWeightOfOtherStorage(order.storage) != 0 && newDistance < distance) {
//            distance = newDistance;
//            bestOrder = order;
//          }
//        }
//      }
    
    for(Order order : orders) {
      if(!order.coordinates.equals(drone.coordinates)) {
        double lambda = 0.1; //Experimental Constant
    	//busy day: ca 0	MoaW: ca 0.1	Red.: ca 0.1		
        
        double distance = 1.0 * Coordinates.distance(order.coordinates, drone.coordinates);
    	
    	double duration = 1.0 * distance * 
    		Math.max(1, order.storage.getWeight() / this.maxWeight);
        
    	double value = lambda * distance + (1-lambda) * duration;
    	
    	if(warehouse.storage.containsWeightOfOtherStorage(order.storage) != 0 && value < bestValue) {
          bestValue = value;
          bestOrder = order;
        }
      }
    }
    return bestOrder;
  }

  private Warehouse getBestWarehouseForOrder(Drone drone, Order order) {
    double bestValue = Double.POSITIVE_INFINITY;
    Warehouse bestWarehouse = null;
    if(warehouses.size() == 1) {
      return warehouses.get(0);
    }
    for(Warehouse warehouse : warehouses) {
      //int tempWeight = warehouse.storage.getBestSubStorage(order.storage, maxWeight).getWeight();
    	
      double Value =  1.0 * Coordinates.distance(drone.coordinates, warehouse.coordinates) /
    		  Math.min(warehouse.storage.containsWeightOfOtherStorage(order.storage),
    				  this.maxWeight);            

      if(warehouse.storage.containsWeightOfOtherStorage(order.storage) != 0 && Value < bestValue) {
        bestValue = Value;
        bestWarehouse = warehouse;
      }
    }
    return bestWarehouse;
  }
}
