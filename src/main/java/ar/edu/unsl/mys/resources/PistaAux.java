package ar.edu.unsl.mys.resources;

public class PistaAux extends Airstrip
{
    public PistaAux(Queue queue){
        super(queue);
        this.setHp(250);
        this.setMaxHp(250);
        this.setEstadoMantenimiento(false);
    }
}
