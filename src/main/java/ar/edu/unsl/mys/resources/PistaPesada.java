package ar.edu.unsl.mys.resources;

public class PistaPesada extends Airstrip
{// mantenimiento: cada vez recupera 750 de hp
    public PistaPesada(Queue queue){
        super(queue);
        this.setHp(5000);
        this.setMaxHp(5000);
        this.setEstadoMantenimiento(false);
        this.setIdleTimeStartMark(0);
    }
}
