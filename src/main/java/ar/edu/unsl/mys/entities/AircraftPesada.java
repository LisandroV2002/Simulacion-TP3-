package ar.edu.unsl.mys.entities;

import java.util.Random;

public class AircraftPesada extends Entity
{ 
    private static int idPesados = 0;

    public AircraftPesada()
    {
        super();
        idPesados++;
    }
    
    public static int getIdPesados(){
        return idPesados;
    }

    public static void resetEntityPesado(){
        idPesados=0;
    }

    @Override
    public String toString()
    {
        return "id = "+this.getId()+" >> aircraftPesada type";
    }

    @Override
    public void aplicaEfecto(){
        Random r = new Random(System.currentTimeMillis());
        int z = (int)(3+(6-3) * (r.nextDouble()));
        this.getAttendingServer().setHp(this.getAttendingServer().getHp() - z);
    }
}