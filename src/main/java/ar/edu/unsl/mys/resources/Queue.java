package ar.edu.unsl.mys.resources;

import ar.edu.unsl.mys.entities.Entity;

public abstract class Queue
{
    private Server assignedServer;
    private java.util.Queue<Entity> queue;
    private int maxSize;
    private int size;

    public Queue(java.util.Queue<Entity> queue)
    {
      this.queue = queue;
    }

    public Server getAssignedServer()
    {
        return this.assignedServer;
    }

    protected void setAssignedServer(Server assignedServer)
    {
        this.assignedServer = assignedServer;
    }

    protected java.util.Queue<Entity> getQueue()
    {
        return this.queue;
    }

    protected void setQueue(java.util.Queue<Entity> queue)
    {
        this.queue = queue;
    }

    public int getMaxSize()
    {
        return this.maxSize;
    }

    public void setMaxSize(int maxSize)
    {
        this.maxSize = maxSize;
    }

    public int getSize(){
        return this.size;
    }

    public void setSize(int size){
        this.size=size;
        if (size > maxSize){
            this.setMaxSize(size);
        }
        
    }

    public boolean isEmpty()
    {
        return this.queue.isEmpty();
    }

    /**
     * Queue an entity using the policy defined in the underlying 
     * implementation of this method.
     * @param entity The entity to be queued.
     */
    public abstract void enqueue(Entity entity);

    /**
     * Gets the next entity in the queue.
     * After calling this method, the entity returned is no longer in the queue.
     * @return The next entity in the queue.
     */
    public abstract Entity next();

    /**
     * Checks the next element in the queue without removing it.
     * The queue remains intact after calling this method.
     * @return The next entity in the queue.
     */
    public abstract Entity checkNext();

    /**
     * Checkea si en la cola hay mantenimiento.
     */
    public abstract boolean checkMantenimiento();
}