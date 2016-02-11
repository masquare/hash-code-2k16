package at.eatsleepnutellarepeat.entity;

/**
 * Created by martinmaritsch on 11/02/16.
 */
public class Product implements Comparable<Product> {

  public int id;
  public int weight;

  public Product(int id, int weight) {
    this.id = id;
    this.weight = weight;
  }

  @Override
  public int compareTo(Product o) {
    return this.id - o.id;
  }
}
