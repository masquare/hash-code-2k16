package at.eatsleepnutellarepeat.entity;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by martinmaritsch on 12/02/16.
 */
public class Storage {

  private TreeMap<Product, Integer> products;
  private int maxWeight;

  public Storage(int maxWeight) {
    products = new TreeMap<>();
    this.maxWeight = maxWeight;
  }

  public Storage(Storage other) {
    products = (TreeMap<Product, Integer>) other.products.clone();
    this.maxWeight = other.maxWeight;
  }

  public Storage() {
    this(-1);
  }

  public Iterator<Map.Entry<Product, Integer>> getProductsIterator() {
    return products.entrySet().iterator();
  }

  public void insertProduct(Product product) {
    insertProduct(product, 1);
  }

  public void insertProduct(Product product, int count) {
    if(maxWeight != -1 && getWeight() + product.weight * count > maxWeight) {
      throw new IllegalArgumentException("Insertion would exceed weight limit");
    }
    products.put(product, products.getOrDefault(product, 0) + count);
  }

  public int getWeight() {
    return products.entrySet().stream().mapToInt(p -> p.getValue() * p.getKey().weight).sum();
  }

  public int getProductCount(Product product) {
    return products.getOrDefault(product, 0);
  }

  public boolean fitsIn(Product product) {
    return fitsIn(product, 1);
  }

  public boolean fitsIn(Product product, int count) {
    return maxWeight == -1 || getWeight() + product.weight * count <= maxWeight;
  }

  public int howManyFitIn(Product product) {
    int i = 0;
    int weight = getWeight();
    while(weight <= maxWeight) {
      weight += product.weight;
      i++;
    }
    return maxWeight == -1 ? Integer.MAX_VALUE : i - 1;
  }

  public void removeProduct(Product product) {
    removeProduct(product, 1);
  }

  public void removeProduct(Product product, int count) {
    if(!products.containsKey(product)) {
      throw new IllegalArgumentException("Product not in storage");
    }

    if(products.get(product) < count) {
      throw new IllegalArgumentException("Cannot remove that many products");
    } else {
      products.put(product, products.get(product) - count);
      if(products.get(product) == 0) {
        products.remove(product);
      }
    }
  }

  public Storage getBestSubStorage(Storage needed, int maxWeight) {
    Storage best = new Storage(maxWeight);

    Iterator<Map.Entry<Product, Integer>> it = products.entrySet().iterator();

    while(best.getWeight() < maxWeight && it.hasNext()) {
      Map.Entry<Product, Integer> entry = it.next();
      Product productUnderTest = entry.getKey();

      if(needed.getProductCount(productUnderTest) > 0) {
        int count = Math.min(getProductCount(productUnderTest), Math.min(needed.getProductCount(productUnderTest), best.howManyFitIn(productUnderTest)));
        if(count > 0) {
          best.insertProduct(productUnderTest, count);
        }
      }
    }
    return best;
  }

  public int containsWeightOfOtherStorage(Storage storage) {

    int weight = 0;
    Iterator<Map.Entry<Product, Integer>> it = storage.getProductsIterator();

    while(it.hasNext()) {
      Map.Entry<Product, Integer> entry = it.next();
      int availability = Math.min(this.products.getOrDefault(entry.getKey(), 0), entry.getValue());
      weight += availability * entry.getKey().weight;
    }
    return weight;
  }

  public void removeAllFromStorage(Storage toRemove) {
    for (Map.Entry<Product, Integer> entry : toRemove.products.entrySet()) {
      removeProduct(entry.getKey(), entry.getValue());
    }
  }

  public void insertAllIntoStorage(Storage toInsert) {
    for (Map.Entry<Product, Integer> entry : toInsert.products.entrySet()) {
      insertProduct(entry.getKey(), entry.getValue());
    }
  }

  public boolean isEmpty() {
    return products.isEmpty();
  }
}
