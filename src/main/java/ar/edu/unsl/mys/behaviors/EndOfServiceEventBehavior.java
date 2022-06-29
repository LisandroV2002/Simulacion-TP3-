package ar.edu.unsl.mys.behaviors;

import ar.edu.unsl.mys.events.*;
import ar.edu.unsl.mys.entities.*;
import ar.edu.unsl.mys.utils.*;

public class EndOfServiceEventBehavior extends EventBehavior
{
    private static EndOfServiceEventBehavior endOfServiceEventBehavior;

    private EndOfServiceEventBehavior(Randomizer randomizer)
    {
        super(randomizer);
    }

    public static EndOfServiceEventBehavior getInstance()
    {
        if(EndOfServiceEventBehavior.endOfServiceEventBehavior == null)
            EndOfServiceEventBehavior.endOfServiceEventBehavior = new EndOfServiceEventBehavior(CustomRandomizer.getInstance());
        return EndOfServiceEventBehavior.endOfServiceEventBehavior;

    } 

    @Override
    public Event nextEvent(Event actualEvent, Entity entity) {
        double r = this.getRandomizer().nextRandom();
        Event e;
        e = new EndOfServiceEvent(actualEvent.getClock(),entity);

        if(actualEvent.getEntity() instanceof  AircraftLiviano){
            if(r < 0.363){
                e.setClock(e.getClock() + 5);
            }
            else if(r < 0.838){
                e.setClock(e.getClock() + 10);
            }
            else {
                e.setClock(e.getClock() + 15);
            }
        }
        else if(actualEvent.getEntity() instanceof  AircraftMediana)
        {
            double clockUni = (10 + ((20 - 10) * r));
            e.setClock(e.getClock() + clockUni);    
        }
        else if(actualEvent.getEntity() instanceof  AircraftPesada)
        { 
            if(r < 0.65){
                e.setClock(e.getClock()+40);
            }
            else {
                e.setClock(e.getClock()+50);
            }
        }
        else //Mantenimiento
        {
            double clockUni = (12 + ((24 - 12) * r))*60;//Lo pasamos a minutos 60' en 1 hs
            e.setClock(e.getClock() + clockUni); 
        }
        
        return e;
    }
}