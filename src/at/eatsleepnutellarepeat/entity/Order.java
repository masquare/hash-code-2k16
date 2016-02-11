package at.eatsleepnutellarepeat.entity;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by martinmaritsch on 11/02/16.
 */
public class Order {

  public Coordinates coordinates;
  public TreeMap<Product, Integer> products;

  public Order(Coordinates coordinates, TreeMap<Product, Integer> products) {
    this.coordinates = coordinates;
    this.products = products;
  }

  public Order(Coordinates coordinates) {
    this.coordinates = coordinates;
    this.products = new TreeMap<>();
  }
}
