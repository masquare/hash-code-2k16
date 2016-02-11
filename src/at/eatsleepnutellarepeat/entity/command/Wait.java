package at.eatsleepnutellarepeat.entity.command;

import at.eatsleepnutellarepeat.entity.Drone;
import at.eatsleepnutellarepeat.entity.Product;
import at.eatsleepnutellarepeat.entity.Warehouse;

/**
 * Created by martinmaritsch on 11/02/16.
 */
public class Wait implements ICommand {

  public Drone drone;
  public int count;

  public Wait(Drone drone) {
    this.drone = drone;
  }

  @Override
  public void execute() {
  }

  @Override
  public String print() {
    return drone.id + " W 1";
  }
}
