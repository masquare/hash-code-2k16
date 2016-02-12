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
    return o.weight - this.weight != 0 ? o.weight - this.weight : o.id - this.id;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Product product = (Product) o;

    return id == product.id;

  }

  @Override
  public int hashCode() {
    return id;
  }
}
