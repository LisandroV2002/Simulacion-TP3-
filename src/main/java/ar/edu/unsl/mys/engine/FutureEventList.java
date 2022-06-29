package ar.edu.unsl.mys.engine;

import ar.edu.unsl.mys.events.EndOfServiceEvent;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Comparator;
import ar.edu.unsl.mys.events.Event;

public class FutureEventList {
    private List < Event > felImpl;
    private Comparator < Event > comparator;

    public FutureEventList() {
        this.felImpl = new ArrayList < > ();
        this.comparator = new Comparator < Event > () {
            @Override
            public int compare(Event event1, Event event2) // Si el event1 es mayor que event2 devuelve un numero positvo 
            {
                int ret;
                if (event1.getClock() > event2.getClock())
                    ret = 1;
                else if (event1.getClock() < event2.getClock())
                    ret = -1;
                else if (event1 instanceof EndOfServiceEvent) //Ver cual ponemos primero el arribo o la salida
                    ret = 1;
                else
                    ret = -1;

                return ret;
            }

        };


    }

    public Event getImminent() {
        return this.felImpl.remove(0);
    }

    public void insert(Event event) {
        this.felImpl.add(event);
        this.felImpl.sort(comparator);
    }

    public boolean isEmpty(){
        return this.felImpl.isEmpty();
    }

    @Override
    public String toString() {
        String ret = "============================================================== F E L ==============================================================\n";

        Iterator < Event > it = this.felImpl.iterator();

        while (it.hasNext()) {
            ret += it.next().toString() + "\n";
        }

        ret += "***********************************************************************************************************************************\n\n";

        return ret;
    }
}