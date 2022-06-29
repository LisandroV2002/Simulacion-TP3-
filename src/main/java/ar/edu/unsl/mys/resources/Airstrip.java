package ar.edu.unsl.mys.resources;

public class Airstrip extends Server
{ // 9 pistas: 3 livianas(vida 1000), 4 medianas(vida 3000), 2 pesadas(vida 5000).

    public Airstrip(Queue queue)
    {
        super(queue);
    }

    @Override
    public String toString()
    {
        return "Airstrip "+this.getId()+" -- busy? : "+this.isBusy()+" -- attending: "+this.getServedEntity();
    }

    
}