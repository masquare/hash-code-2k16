package at.eatsleepnutellarepeat.entity.command;

import at.eatsleepnutellarepeat.entity.Drone;
import at.eatsleepnutellarepeat.entity.Product;
import at.eatsleepnutellarepeat.entity.Warehouse;

/**
 * Created by martinmaritsch on 11/02/16.
 */
public class Move implements ICommand {

  public Drone drone;
  public Warehouse warehouse;
  public Product product;
  public int count;

  public Move() {
  }

  @Override
  public void execute() {
    return;
  }

  @Override
  public String print() {
    return null;
  }
}
