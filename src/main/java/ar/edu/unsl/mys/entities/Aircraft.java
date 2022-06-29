package ar.edu.unsl.mys.entities;

public class Aircraft extends Entity
{ // 3 clases de aviones: livianas, medianas y pesadas

    public Aircraft()
    {
        super();
    }
    
    @Override
    public String toString()
    {
        return "id = "+this.getId()+" >> default aircraft type";
    }

    @Override
    public void aplicaEfecto(){;}
}