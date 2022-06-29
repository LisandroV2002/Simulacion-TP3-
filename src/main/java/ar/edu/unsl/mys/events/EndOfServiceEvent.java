package ar.edu.unsl.mys.events;

import java.util.List;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.entities.Mantenimiento;
// import ar.edu.unsl.mys.resources.Queue;
import ar.edu.unsl.mys.resources.Server;
import ar.edu.unsl.mys.engine.FutureEventList;
import ar.edu.unsl.mys.behaviors.EndOfServiceEventBehavior;

public class EndOfServiceEvent extends Event
{
    public EndOfServiceEvent(double clock, Entity entity)
    {
        super(clock,entity,EndOfServiceEventBehavior.getInstance());
        this.getEntity().setEndOfServiceEvent(this);
    }

    @Override
    public String toString()
    {
        return String.format("Type: End of Service -- Clock: %"+Event.END_TIME_DIGITS+"s -- entity: %s", this.getClock(), this.getEntity().toString());
    }

    @Override
    public void planificate(List<Server> servers, FutureEventList fel) 
    {
        Server server= this.getEntity().getAttendingServer();

        
        server.setIdleTotal(this.getClock() - this.getEntity().getArrivalEvent().getClock() - this.getEntity().getWaitingTime());
        if(this.getEntity() instanceof Mantenimiento) server.setEstadoMantenimiento(false);
        
        if(server.getQueue().isEmpty())
        {
            server.setBusy(false);
            server.setIdleTimeStartMark(this.getClock());
        }
        else
        {
            Entity entity = server.getQueue().next();
            entity.setWaitingTime(this.getClock()-entity.getArrivalEvent().getClock());
            
            fel.insert(this.getEventBehavior().nextEvent(this, entity));
        }
        //Transit Time
        this.getEntity().setTransitTime(this.getClock() - this.getEntity().getArrivalEvent().getClock());

        
    }
}