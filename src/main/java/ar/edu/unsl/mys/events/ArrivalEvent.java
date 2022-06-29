package ar.edu.unsl.mys.events;

import java.util.List;
import ar.edu.unsl.mys.resources.Server;
import ar.edu.unsl.mys.entities.*;
import ar.edu.unsl.mys.engine.FutureEventList;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;
import ar.edu.unsl.mys.behaviors.*;

public class ArrivalEvent extends Event
{
    private ServerSelectionPolicy selectionPolicy;
    private EndOfServiceEventBehavior endOfServiceEventBehavior;


    public ArrivalEvent(double clock, Entity entity, ServerSelectionPolicy policy)
    {
        super(clock, entity, ArrivalEventBehavior.getInstance());
        this.selectionPolicy=policy;
        this.endOfServiceEventBehavior = EndOfServiceEventBehavior.getInstance();
        this.getEntity().setArrivalEvent(this);
    }

    public ServerSelectionPolicy getSelectionPolicy()
    {
        return this.selectionPolicy;
    }

    public EndOfServiceEventBehavior getEndOfServiceEventBehavior()
    {
        return this.endOfServiceEventBehavior;
    }

     @Override
    public void planificate(List<Server> servers, FutureEventList fel)
    {
        Server server= this.getSelectionPolicy().selectServer(servers, this.getEntity());
        this.getEntity().setAttendingServer(server);
        
        //Si la entidad que llegó es tipo Mantenimiento 
        if(this.getEntity() instanceof Mantenimiento) 
            server.setEstadoMantenimiento(true);

        if(server.isBusy())
        {
            server.getQueue().enqueue(this.getEntity());
        }
        else
        {
            server.setBusy(true);
            this.getEntity().aplicaEfecto();
            server.setIdleTimeFinishMark(this.getClock());
            server.setServedEntity(this.getEntity());
            fel.insert(this.endOfServiceEventBehavior.nextEvent(this, this.getEntity()));
        }
        fel.insert(this.getEventBehavior().nextEvent(this, null));
        
        // if(server == servers.get(2)){
        //     System.out.println("entro server 2\nclock: "+this.getClock());
        //     System.out.println("\nInicio: "+server.getIdleTimeStartMark());
        //     System.out.println("\nFin: "+server.getIdleTimeFinishMark());
        // }
        // if(server == servers.get(8)){
        // System.out.println("entro server 8\nclock: "+this.getClock());
        // System.out.println("\nInicio: "+server.getIdleTimeStartMark());
        // System.out.println("\nFin: "+server.getIdleTimeFinishMark());
        // }
    }

    @Override
    public String toString()
    {
        return String.format("Type: Arrival        -- Clock: %"+Event.END_TIME_DIGITS+"s -- entity: %s", this.getClock(), this.getEntity().toString());
    }
}