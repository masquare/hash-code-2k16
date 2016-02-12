package at.eatsleepnutellarepeat.entity;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * Created by martinmaritsch on 11/02/16.
 */
public class Order {

  public int id;
  public Coordinates coordinates;
  public Storage storage;

  public Order(int id, Coordinates coordinates, Storage storage) {
    this.id = id;
    this.coordinates = coordinates;
    this.storage = storage;
  }

  public boolean isFinished() {
    return storage.isEmpty();
  }
}
