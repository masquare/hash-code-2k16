package at.eatsleepnutellarepeat.entity;

import at.eatsleepnutellarepeat.entity.command.ICommand;
import at.eatsleepnutellarepeat.entity.command.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by martinmaritsch on 11/02/16.
 */
public class Drone implements Comparable<Drone> {

  public int id;
  public int maxWeight;
  public Coordinates position;
  public TreeMap<Product, Integer> products;

  public List<ICommand> processedCommands = new ArrayList<>();
  public List<ICommand> commands = new ArrayList<>();

  public Drone(int id, int maxWeight, Coordinates position, TreeMap<Product, Integer> products) {
    this.id = id;
    this.maxWeight = maxWeight;
    this.position = position;
    this.products = products;
  }

  public Drone(int id, int maxWeight, Coordinates position) {
    this.id = id;
    this.maxWeight = maxWeight;
    this.position = position;
    this.products = new TreeMap<>();
  }

  @Override
  public int compareTo(Drone o) {
    return this.id - o.id;
  }

  public int getCurrentWeight() {
    int weight = 0;
    for(Map.Entry<Product, Integer> e : products.entrySet()) {
      weight += e.getKey().weight * e.getValue();
    }
    return weight;
  }

  public boolean tick() {
    if(!commands.isEmpty()) {
      ICommand c = commands.remove(0);
      if(!(c instanceof Move)) {
        processedCommands.add(c);
      }
      return commands.isEmpty();
    } else {
      return false;
    }
  }

  public boolean isBusy() {
    return !commands.isEmpty();
  }

  public void addCommand(ICommand command) {
    commands.add(command);
  }

  public void loadProduct(Product product) {
    if(products.containsKey(product)) {
      products.put(product, products.get(product) + 1);
    } else {
      products.put(product, 1);
    }
  }

  public void unloadProduct(Product product) {
    if(products.containsKey(product)) {
      products.put(product, products.get(product) - 1);
      if(products.get(product) <= 0) {
        products.remove(product);
      }
    }
  }
}
