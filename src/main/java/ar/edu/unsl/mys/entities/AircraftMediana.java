package ar.edu.unsl.mys.entities;
import java.util.Random;

public class AircraftMediana extends Entity
{ 
    private static int idMedianos = 0;
    public AircraftMediana()
    {
        super();
        idMedianos++;
    }

    public static int getIdMedianos(){
        return idMedianos;
    }

    public static void resetEntityMediano(){
        idMedianos=0;
    }
    
    @Override
    public String toString()
    {
        return "id = "+this.getId()+" >> aircraftMediana type";
    }

    @Override
    public void aplicaEfecto(){
        Random r = new Random(System.currentTimeMillis());
        int z = (int)(1+(4-1) * (r.nextDouble()));
        this.getAttendingServer().setHp(this.getAttendingServer().getHp() - z);
    }
}