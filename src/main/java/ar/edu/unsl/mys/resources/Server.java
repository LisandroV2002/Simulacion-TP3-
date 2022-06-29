package ar.edu.unsl.mys.resources;

import ar.edu.unsl.mys.entities.Entity;

public abstract class Server
{
    private static int idCount = 0;

    private int hp;
    private int maxHp;
    private boolean estadoMantenimiento = false; //asdasdasd
    //attributes
    private int id;
    private boolean busy;
    /**
     * init with 0 to avoid desynchronized in the firt arrival event.
     */
    private double idleTimeStartMark = 0;
    /**
     * init with 0 to avoid desynchronized in the firt arrival event.
     */
    private double idleTotal=1440*28;
    private double idleTimeFinishMark = 0;
    private int idleTime;
    private double maxIdleTime = 0;

    //associations
    private Entity servedEntity;
    private Queue queue;

    public Server(Queue queue)
    {
        this.id = idCount + 1; 
        idCount++;
        this.queue = queue;
        this.busy = false;
        this.idleTime = 0;
    }

    public boolean getEstadoMantenimiento(){
        return this.estadoMantenimiento;
    }

    public void setEstadoMantenimiento(boolean estado){
        this.estadoMantenimiento = estado;
    }

    public int getHp() {
        return this.hp;
    }

    public void setHp(int hp){
        this.hp = hp;
    }

    public int getMaxHp(){
        return this.maxHp;
    }

    public void setMaxHp(int maxHp){
        this.maxHp = maxHp;
    }

    public float getPorcentajeHp(){
        return (this.getHp()*100)/this.getMaxHp();
    }

    public int getId()
    {
        return this.id;
    }

    public boolean isBusy()
    {
        return this.busy;
    }

    public void setBusy(boolean busy)
    {
        this.busy = busy;
    }

    public void setIdleTimeStartMark(double idleTimeStartMark)
    {
        this.idleTimeStartMark = idleTimeStartMark;
    }

    public void setIdleTimeFinishMark(double idleTimeFinishMark)
    {
        this.idleTimeFinishMark = idleTimeFinishMark;

        try
        {
            if(this.idleTimeStartMark != -1 && this.idleTimeFinishMark != -1 && this.idleTimeStartMark <= this.idleTimeFinishMark)
            {
                // something to do here? 
                //this.setIdleTotal();

                if(this.idleTimeFinishMark-this.idleTimeStartMark > this.maxIdleTime){
                    this.maxIdleTime = this.idleTimeFinishMark-this.idleTimeStartMark;
                }       

                this.idleTimeStartMark = -1;
                this.idleTimeFinishMark = -1;
            }
            else
            {
                throw new Exception("desynchronized idle time marks");
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }   
    }

    public int getIdleTime()
    {
        return this.idleTime;
    }

    public Entity getServedEntity()
    {
        return this.servedEntity;
    }

    public void setServedEntity(Entity servedEntity)
    {
        this.servedEntity = servedEntity;
    }

    public Queue getQueue()
    {
        return this.queue;
    }

    public double getMaxIdleTime()
    {
        return this.maxIdleTime;
    }

    public void  setIdleTotal(double transicion){  //Ocio total
        //this.idleTotal += this.idleTimeFinishMark - this.idleTimeStartMark;
        this.idleTotal = this.idleTotal - transicion;
        /*if(this.idleTimeFinishMark-this.idleTimeStartMark > this.maxIdleTime){
            this.maxIdleTime = this.idleTimeFinishMark-this.idleTimeStartMark;
        }*/
    } 

    public double getIdleTimeFinishMark() {
        return this.idleTimeFinishMark;
    }
    public double getIdleTimeStartMark() {
        return this.idleTimeStartMark;
    }

    public double getIdleTotal(){
        return this.idleTotal;
    }
}