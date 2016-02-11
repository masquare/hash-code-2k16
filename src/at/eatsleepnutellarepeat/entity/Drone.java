package at.eatsleepnutellarepeat.entity;

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
}
