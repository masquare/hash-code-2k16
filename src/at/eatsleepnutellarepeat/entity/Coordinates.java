package at.eatsleepnutellarepeat.entity;

/**
 * Created by martinmaritsch on 06/02/16.
 */
public class Coordinates implements Comparable<Coordinates> {

  private int x;
  private int y;

  public Coordinates() {
    this.x = 0;
    this.y = 0;
  }

  public Coordinates(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Coordinates(Coordinates other) {
    this.x = other.x;
    this.y = other.y;
  }

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int distanceTo(Coordinates other) {
    return (int) Math.ceil(Math.sqrt((this.x - other.x) * (this.x - other.x) + (this.y - other.y) * (this.y - other.y)));
  }

  public static int distance(Coordinates one, Coordinates other) {
    return (int) Math.ceil(Math.sqrt((one.x - other.x) * (one.x - other.x) + (one.y - other.y) * (one.y - other.y)));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Coordinates that = (Coordinates) o;

    return x == that.x && y == that.y;
  }

  @Override
  public int hashCode() {
    int result = x;
    result = 31 * result + y;
    return result;
  }

  @Override
  public String toString() {
    return "Coordinates{" + x + "|" + y + "}";
  }

  @Override
  public int compareTo(Coordinates o) {
    if(this.x != o.x) {
      return this.x - o.x;
    }
    return this.y - o.y;
  }
}
