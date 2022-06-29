package ar.edu.unsl.mys.behaviors;

import ar.edu.unsl.mys.entities.*;
import ar.edu.unsl.mys.events.*;
import ar.edu.unsl.mys.utils.*;
import ar.edu.unsl.mys.policies.*;

public class ArrivalEventBehavior extends EventBehavior
{
    private static ArrivalEventBehavior arrivalEventBehavior;

    private ArrivalEventBehavior(Randomizer randomizer)
    {
        super(randomizer);
    }

    public static ArrivalEventBehavior getInstance()
    {
        if(ArrivalEventBehavior.arrivalEventBehavior == null)
            ArrivalEventBehavior.arrivalEventBehavior = new ArrivalEventBehavior(CustomRandomizer.getInstance()); 
        return ArrivalEventBehavior.arrivalEventBehavior;
    }

    @Override
    public Event nextEvent(Event actualEvent, Entity entity) {
        double r = this.getRandomizer().nextRandom();
        double glockNuevo;
        Event e=null;
        //Si la entidad del evento actual es mantenimiento genera otro mantenimiento.
        if(actualEvent.getEntity() instanceof Mantenimiento){
            Double z;
            for (int i = 0; i < 11; i++){
                r = r + this.getRandomizer().nextRandom();
            }
            z = (r - 6) / 1;
            glockNuevo = (z * 0.5 + 5) * 1440 * 5;
            e = new ArrivalEvent(actualEvent.getClock()+glockNuevo,
                                 new Mantenimiento(),
                                 MultiServerModelPolicy.getInstance());
            return e;
        }

        //Si esta en hora pico o no
        if(  (actualEvent.getClock() % 12)<= 10 && (actualEvent.getClock()%12) >=7)
        {
            if(actualEvent.getEntity() instanceof  AircraftLiviano){

                glockNuevo = (-20)* ((Math.log(1-r)));

                e = new ArrivalEvent(actualEvent.getClock()+glockNuevo,
                                    new AircraftLiviano(),
                                    MultiServerModelPolicy.getInstance());
                                           
            }
            if(actualEvent.getEntity() instanceof  AircraftMediana){
                
                glockNuevo = (-15)*((Math.log(1-r)));

                e = new ArrivalEvent(actualEvent.getClock()+glockNuevo,
                                    new AircraftMediana(),
                                    MultiServerModelPolicy.getInstance());
            }

            if(actualEvent.getEntity() instanceof  AircraftPesada){
                Double z;
                for (int i = 0; i < 11; i++){
                    r = r + this.getRandomizer().nextRandom();
                }
                z = (r - 6) / 1; 
                glockNuevo = (z * 2 + 30);
                e = new ArrivalEvent(actualEvent.getClock()+glockNuevo,
                                     new AircraftPesada(),
                                     MultiServerModelPolicy.getInstance());
            }
                
        }
        else
        {
            if(actualEvent.getEntity() instanceof  AircraftLiviano){
                glockNuevo = (-40)*((Math.log(1-r)));
                e = new ArrivalEvent(actualEvent.getClock()+glockNuevo,
                                     new AircraftLiviano(),
                                     MultiServerModelPolicy.getInstance());
            }
            if(actualEvent.getEntity() instanceof  AircraftMediana){
                glockNuevo = (-30)*((int)(Math.log(1-r)));
                e = new ArrivalEvent(actualEvent.getClock()+glockNuevo,
                                     new AircraftMediana(),
                                     MultiServerModelPolicy.getInstance());
                
            }
            if(actualEvent.getEntity() instanceof  AircraftPesada){
                Double z;
                for (int i = 0; i < 11; i++){
                    r = r + this.getRandomizer().nextRandom();
                }
                z = (r - 6) / 1;
                glockNuevo = (z * 2 + 60);
                e = new ArrivalEvent(actualEvent.getClock()+glockNuevo,
                                     new AircraftPesada(),
                                     MultiServerModelPolicy.getInstance());
            } 
        }
        
        return e;
    }
}