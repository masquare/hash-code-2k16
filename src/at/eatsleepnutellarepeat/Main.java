
package at.eatsleepnutellarepeat;

import at.eatsleepnutellarepeat.entity.Scenario;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length != 2) {
            System.out.println("Usage: <executable.jar> <inputfile> <outputfile>");
        }
        Scenario scenario = Scenario.parseFromFile(args[0]);
        scenario.calculate();
        scenario.writeToFile(args[1]);
    }
}
