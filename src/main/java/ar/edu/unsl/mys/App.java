package ar.edu.unsl.mys;

import ar.edu.unsl.mys.engine.AirportSimulation;
import ar.edu.unsl.mys.engine.Engine;
import ar.edu.unsl.mys.policies.*;

public class App 
{
    private static final double MIN_PER_DAY = 1440;
    private static final double NUMBER_OF_DAYS = 28;
    private static final double EXECUTION_TIME = MIN_PER_DAY*NUMBER_OF_DAYS;

    public static void main( String[] args )
    {
        Engine engine = new AirportSimulation(1,2,1,1,EXECUTION_TIME, MultiServerModelPolicy.getInstance());
        engine.execute();
        engine.generateReport(false,null);
    }
}