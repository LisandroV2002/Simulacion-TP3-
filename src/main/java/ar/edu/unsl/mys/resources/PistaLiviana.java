package ar.edu.unsl.mys.resources;

public class PistaLiviana extends Airstrip
{ // mantenimiento: cada vez recupera 150 de hp
    public PistaLiviana(Queue queue){
        super(queue);
        this.setHp(1000);
        this.setMaxHp(1000);
        this.setEstadoMantenimiento(false);
        this.setIdleTimeStartMark(0);
    }
}