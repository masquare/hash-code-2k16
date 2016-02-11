package at.eatsleepnutellarepeat.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by martinmaritsch on 11/02/16.
 */
public class Warehouse implements Comparable<Warehouse> {

  public int id;
  public Coordinates coordinates;
  public TreeMap<Product, Integer> products;

  public Warehouse(int id, Coordinates coordinates, TreeMap<Product, Integer> products) {
    this.id = id;
    this.coordinates = coordinates;
    this.products = products;
  }

  public Warehouse(int id, Coordinates coordinates) {
    this.id = id;
    this.coordinates = coordinates;
    this.products = new TreeMap<>();
  }

  @Override
  public int compareTo(Warehouse o) {
    return this.id - o.id;
  }

  public void putProduct(Product product) {
    if(products.containsKey(product)) {
      products.put(product, products.get(product) + 1);
    } else {
      products.put(product, 1);
    }
  }

  public void takeProduct(Product product) {
    if(products.containsKey(product)) {
      products.put(product, products.get(product) - 1);
      /*if(products.get(product) <= 0) {
        products.remove(product);
      }*/
    }
  }


}