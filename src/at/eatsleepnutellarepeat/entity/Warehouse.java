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
  public Storage storage;

  public Warehouse(int id, Coordinates coordinates, Storage storage) {
    this.id = id;
    this.coordinates = coordinates;
    this.storage = new Storage(storage);
  }

  @Override
  public int compareTo(Warehouse o) {
    return this.id - o.id;
  }
}
