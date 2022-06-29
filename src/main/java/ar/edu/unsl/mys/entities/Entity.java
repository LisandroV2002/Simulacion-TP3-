package ar.edu.unsl.mys.entities;

// import java.util.List;
// import java.util.ArrayList;
// import java.util.Comparator;
// import ar.edu.unsl.mys.events.Event;
import ar.edu.unsl.mys.resources.Server;
import ar.edu.unsl.mys.events.ArrivalEvent;
import ar.edu.unsl.mys.events.EndOfServiceEvent;

public abstract class Entity
{
    private static int idCount=0;
    //Tiempo espera
    private static double totalWaitingTime=0;
    private static double maxWaitingTime=0;
    private static double totalWaitingTimeLiviano=0;
    private static double maxWaitingTimeLiviano=0;
    private static double totalWaitingTimeMediano=0;
    private static double maxWaitingTimeMediano=0;
    private static double totalWaitingTimePesado=0;
    private static double maxWaitingTimePesado=0;

    //Tiempo de transito
    private static double totalTransitTime=0;
    private static double maxTransitTime=0;
    private static double totalTransitTimeLiviano;
    private static double maxTransitTimeLiviano=0;
    private static double totalTransitTimeMediano=0;
    private static double maxTransitTimeMediano=0;
    private static double totalTransitTimePesado=0;
    private static double maxTransitTimePesado=0;

    //attributes
    private int id;
    private double waitingTime;
    private double transitTime;

    //associations
    private Server attendingServer;
    //private List<Event> events;
    private ArrivalEvent arrivalEvent;
    private EndOfServiceEvent endOfServiceEvent;

    // others
    /**
     * Used if it is necessary to order chronologically the events of this entity.
     */
    // private Comparator<Event> comparator;

    public Entity()
    {
        this.id = idCount + 1;
        idCount++;
        this.waitingTime=0;
        this.transitTime=0;
    }

    public static void setIdCount(int idCount1){
        idCount = idCount1;
    }

    public static void resetEntity()
    {
        idCount=0;

        totalWaitingTime=0;
        maxWaitingTime=0;
        totalTransitTime=0;
        maxTransitTime=0;

        totalWaitingTimeLiviano=0;
        maxWaitingTimeLiviano=0;
        totalTransitTimeLiviano=0;
        maxTransitTimeLiviano=0;
    }

    public static double getTotalWaitingTime()
    {
        return totalWaitingTime;
    }

    public static double getTotalWaitingTimeLiviano()
    {
        return totalWaitingTimeLiviano;
    }

    public static double getTotalWaitingTimeMediano()
    {
        return totalWaitingTimeMediano;
    }

    public static double getTotalWaitingTimePesado()
    {
        return totalWaitingTimePesado;
    }

    public static int getIdCount()
    {
        return idCount;
    }

    public static double getMaxWaitingTime()
    {
        return maxWaitingTime;
    }

    public static double getMaxWaitingTimeLiviano()
    {
        return maxWaitingTimeLiviano;
    }

    public static double getMaxWaitingTimeMediano()
    {
        return maxWaitingTimeMediano;
    }

    public static double getMaxWaitingTimePesado()
    {
        return maxWaitingTimePesado;
    }

    public static double getAccumulatedTransitTime()
    {
        return totalTransitTime;
    }

    public static double getAccumulatedTransitTimeLiviano()
    {
        return totalTransitTimeLiviano;
    }

    public static double getAccumulatedTransitTimeMediano()
    {
        return totalTransitTimeMediano;
    }

    public static double getAccumulatedTransitTimePesado()
    {
        return totalTransitTimePesado;
    }

    public static double getMaxTransitTime()
    {
        return maxTransitTime;
    }

    public static double getMaxTransitTimeLiviano()
    {
        return maxTransitTimeLiviano;
    }

    public static double getMaxTransitTimeMediano()
    {
        return maxTransitTimeMediano;
    }

    public static double getMaxTransitTimePesado()
    {
        return maxTransitTimePesado;
    }

    public double getId()
    {
        return this.id;
    }

    public void setId (int id)
    {
        this.id = id;
    }

    public double getWaitingTime()
    {
        return this.waitingTime;
    }

    public void setWaitingTime(double waitingTime)
    {
        this.waitingTime = waitingTime;
        totalWaitingTime += waitingTime;
        if(waitingTime > maxWaitingTime){
            maxWaitingTime = waitingTime;
        }

        if(this instanceof AircraftLiviano)
        {
            totalWaitingTimeLiviano += waitingTime;
            if(waitingTime > maxWaitingTimeLiviano){
                maxWaitingTimeLiviano = waitingTime;
            }
        }
        else if(this instanceof AircraftMediana)
        {
            totalWaitingTimeMediano += waitingTime;
            if(waitingTime > maxWaitingTimeMediano){
                maxWaitingTimeMediano = waitingTime;
            }
        }
        else
        {
            totalWaitingTimePesado += waitingTime;
            if(waitingTime > maxWaitingTimePesado){
                maxWaitingTimePesado = waitingTime;
            }
        }
    }

    public double getTransitTime()
    {
        return this.transitTime;
    }

    public void setTransitTime(double transitTime)
    {
        this.transitTime = transitTime;
        totalTransitTime += transitTime;
        if(transitTime > maxTransitTime){
            maxTransitTime = transitTime;
        }
        
        if(this instanceof AircraftLiviano)
        {
            totalTransitTimeLiviano += transitTime;
            if(transitTime > maxTransitTimeLiviano){
                maxTransitTimeLiviano = transitTime;
            }
        }
        else if(this instanceof AircraftMediana)
        {
            totalTransitTimeMediano += transitTime;
            if(transitTime > maxTransitTimeMediano){
                maxTransitTimeMediano = transitTime;
            }
        }
        else
        {
            totalTransitTimePesado += transitTime;
            if(transitTime > maxTransitTimePesado){
                maxTransitTimePesado = transitTime;
            }
        }
    }

    
    public ArrivalEvent getArrivalEvent()
    {
        return this.arrivalEvent;
    }

    public void setArrivalEvent(ArrivalEvent e){
        this.arrivalEvent = e;
    }

    public EndOfServiceEvent getEndOfServiceEvent()
    {
        return this.endOfServiceEvent;
    }
    
    public void setEndOfServiceEvent(EndOfServiceEvent e){
        this.endOfServiceEvent = e;
    }
    
    public Server getAttendingServer()
    {
        return attendingServer;
    }

    public void setAttendingServer(Server attendingServer)
    {
        this.attendingServer = attendingServer;
    }
    
    public abstract  void aplicaEfecto();
    
    /*
    public List<Event> getEvents()
    {
        return this.events;
    }

    public void setEvent(Event event)
    {
        this.events.add(event);
    }
    */
}