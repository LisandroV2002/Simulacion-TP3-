package ar.edu.unsl.mys.resources;

public class PistaMediana extends Airstrip
{ // mantenimiento: cada vez recupera 450 de hp
    public PistaMediana(Queue queue){
        super(queue);
        this.setHp(3000);
        this.setMaxHp(3000);
        this.setEstadoMantenimiento(false);
        this.setIdleTimeStartMark(0);
    }
}
