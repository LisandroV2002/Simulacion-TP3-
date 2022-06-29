package ar.edu.unsl.mys.entities;

public class Mantenimiento extends Entity
{
    public Mantenimiento()
    {
        super();
    }
    // Recupera:
    // Liviana: +150hp
    // Mediana: +450hp
    // Pesada: +750hp

    @Override
    public String toString()
    {
        return "id = "+this.getId()+" >> Mantenimiento type";
    }

    @Override
    public void aplicaEfecto(){
        int maxHp = this.getAttendingServer().getMaxHp();
        int actualHp = this.getAttendingServer().getHp();
        int restoreHp = (int)(maxHp * 0.15);
        if(actualHp + restoreHp < maxHp)
            this.getAttendingServer().setHp(actualHp + restoreHp);
        else
            this.getAttendingServer().setHp(maxHp);
    }
}