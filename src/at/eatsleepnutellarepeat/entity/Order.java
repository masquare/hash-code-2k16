package at.eatsleepnutellarepeat.entity;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by martinmaritsch on 11/02/16.
 */
public class Order {

  public int id;
  public Coordinates coordinates;
  public TreeMap<Product, Integer> products;

  public Order(int id, Coordinates coordinates, TreeMap<Product, Integer> products) {
    this.id = id;
    this.coordinates = coordinates;
    this.products = products;
  }

  public Order(int id, Coordinates coordinates) {
    this.id = id;
    this.coordinates = coordinates;
    this.products = new TreeMap<>();
  }

  public boolean isFinished() {
    for(Integer i : products.values()) {
      if (i != 0) {
        return false;
      }
    }
    return true;
  }

  public void removeProduct(Product product) {
    if(products.containsKey(product)) {
      products.put(product, products.get(product) - 1);
      if(products.get(product) <= 0) {
        products.remove(product);
      }
    }
  }
}
