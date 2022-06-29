package ar.edu.unsl.mys.resources;

import java.util.Iterator;
import java.util.LinkedList;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.entities.Mantenimiento;

public class CustomQueue extends Queue
{
    public CustomQueue()
    {
        super(new LinkedList<Entity>());
    }

    @Override
    public String toString()
    {
        String ret = "server queue "+this.getAssignedServer().getId()+":\n\t";

        Iterator<Entity> it = this.getQueue().iterator();

        while(it.hasNext())
        {
            ret += it.next().toString();
        }

        return ret;
    }

    @Override
    public void enqueue(Entity entity){
        this.getQueue().add(entity);
        this.setSize(this.getSize()+1);

    }

    @Override
    public Entity next() {
        this.setSize(this.getSize()-1);
	    return this.getQueue().poll();
    }

    @Override
    public Entity checkNext() {
    	return this.getQueue().element();
    }

    @Override
    public boolean checkMantenimiento() {
        boolean ret = false;
        // for(int i = 0; i < this.getQueue().size(); i++){
        //     if(this.getQueue()..element() instanceof Mantenimiento)
        //         ret = true;
        // }
        // https://suarezdeveloper.wordpress.com/2012/05/18/estructuras-de-datos-linkedlist/
        Iterator<Entity> iter = this.getQueue().iterator();
        if(iter instanceof Mantenimiento)
            ret = true; // para cuando la cola solo tiene un avion
        while(iter.hasNext()){
            if(iter instanceof Mantenimiento)
                ret = true;
            iter.remove();
            
        }
        

        return ret;
    }
}