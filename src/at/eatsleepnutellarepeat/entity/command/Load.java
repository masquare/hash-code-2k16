package at.eatsleepnutellarepeat.entity.command;

import at.eatsleepnutellarepeat.entity.Drone;
import at.eatsleepnutellarepeat.entity.Product;
import at.eatsleepnutellarepeat.entity.Warehouse;

/**
 * Created by martinmaritsch on 11/02/16.
 */
public class Load implements ICommand {

  public Drone drone;
  public Warehouse warehouse;
  public Product product;
  public int count;

  public Load(Drone drone, Warehouse warehouse, Product product, int count) {
    this.drone = drone;
    this.warehouse = warehouse;
    this.product = product;
    this.count = count;
  }

  @Override
  public void execute() {
    //drone.storage.insertProduct(product, count);
    //warehouse.storage.removeProduct(product, count);
  }

  @Override
  public String print() {
    return drone.id + " L " + warehouse.id + " " + product.id + " " + count;
  }
}
