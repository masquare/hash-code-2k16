package at.eatsleepnutellarepeat.entity.command;

import at.eatsleepnutellarepeat.entity.Drone;
import at.eatsleepnutellarepeat.entity.Product;
import at.eatsleepnutellarepeat.entity.Warehouse;

/**
 * Created by martinmaritsch on 11/02/16.
 */
public class Unload implements ICommand {

  public Drone drone;
  public Warehouse warehouse;
  public Product product;
  public int count;

  public Unload(Drone drone, Warehouse warehouse, Product product, int count) {
    this.drone = drone;
    this.warehouse = warehouse;
    this.product = product;
    this.count = count;
  }

  @Override
  public void execute() {
    /*if(drone.products.containsKey(product)) {
      drone.products.put(product, drone.products.get(product) - count);
    } else {
      drone.products.put(product, count);
    }

    warehouse.products.put(product, warehouse.products.get(product) + count);*/
  }

  @Override
  public String print() {
    return drone.id + " U " + warehouse.id + " " + product.id + " " + count;
  }
}
