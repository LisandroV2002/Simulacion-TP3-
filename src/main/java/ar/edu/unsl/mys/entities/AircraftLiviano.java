package ar.edu.unsl.mys.entities;

import java.util.Random;

public class AircraftLiviano extends Entity
{
    private static int idLivianos=0;


    public AircraftLiviano()
    {
        super();
        idLivianos++;
    }   
    
    public static int getIdLiviano(){
        return idLivianos;
    }

    public static void resetEntityLivianos(){
        idLivianos = 0;
    }

    @Override
    public String toString()
    {
        return "id = "+this.getId()+" >> aircraftLiviano type";
    }

     @Override
    public void aplicaEfecto(){
        Random r = new Random(System.currentTimeMillis());
        int z = (int)(0+(1-0) * (r.nextDouble()));
        this.getAttendingServer().setHp(this.getAttendingServer().getHp() - z);
    }
}