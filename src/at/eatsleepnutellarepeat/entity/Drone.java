package at.eatsleepnutellarepeat.entity;

import at.eatsleepnutellarepeat.entity.command.Deliver;
import at.eatsleepnutellarepeat.entity.command.ICommand;
import at.eatsleepnutellarepeat.entity.command.Load;
import at.eatsleepnutellarepeat.entity.command.Move;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martinmaritsch on 11/02/16.
 */
public class Drone implements Comparable<Drone> {

  public int id;
  public Coordinates coordinates;
  public Storage storage;

  public List<ICommand> processedCommands = new ArrayList<>();
  public List<ICommand> upcomingCommands = new ArrayList<>();

  public Drone(int id, int maxWeight, Coordinates coordinates) {
    this.id = id;
    this.storage = new Storage(maxWeight);
    this.coordinates = coordinates;
  }

  @Override
  public int compareTo(Drone o) {
    return this.id - o.id;
  }

  public boolean tick() {
    if(!upcomingCommands.isEmpty()) {
      ICommand c = upcomingCommands.remove(0);
      if(!(c instanceof Move)) {
        processedCommands.add(c);
      }
      return upcomingCommands.isEmpty();
    } else {
      return false;
    }
  }

  public boolean isAvailable() {
    return upcomingCommands.isEmpty();
  }

  public void addCommand(ICommand command) {
    upcomingCommands.add(command);
  }

  public void moveToCoordinates(Coordinates target) {
    int distance = coordinates.distanceTo(target);
    for(int i = 0; i < distance; i++) {
      addCommand(new Move());
    }
    coordinates = new Coordinates(target);
  }
}
