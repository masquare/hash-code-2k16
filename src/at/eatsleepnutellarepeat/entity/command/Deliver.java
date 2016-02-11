package at.eatsleepnutellarepeat.entity.command;

import at.eatsleepnutellarepeat.entity.Drone;
import at.eatsleepnutellarepeat.entity.Order;
import at.eatsleepnutellarepeat.entity.Product;
import at.eatsleepnutellarepeat.entity.Warehouse;

/**
 * Created by martinmaritsch on 11/02/16.
 */
public class Deliver implements ICommand {

  public Drone drone;
  public Order order;
  public Product product;
  public int count;

  public Deliver(Drone drone, Order order, Product product, int count) {
    this.drone = drone;
    this.order = order;
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

    order.products.put(product, order.products.get(product) - count);*/
  }

  @Override
  public String print() {
    return drone.id + " D " + order.id + " " + product.id + " " + count;
  }
}
