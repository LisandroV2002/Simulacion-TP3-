package ar.edu.unsl.mys.engine;

import java.util.List;
// import java.util.Scanner;
// import java.util.Iterator;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.io.BufferedWriter;

import ar.edu.unsl.mys.entities.*;
import ar.edu.unsl.mys.resources.*;
import ar.edu.unsl.mys.events.*;
import ar.edu.unsl.mys.policies.ServerSelectionPolicy;

/**
 * Event oriented simulation of an airport
 */
public class AirportSimulation implements Engine {
    private String report = "==============================================================================================\n" +
        "                                        R E P O R T                                           \n" +
        "==============================================================================================\n" +
        "\n";

    private double endTime;
    private boolean stopSimulation;
    private FutureEventList fel;
    private List < Server > servers;
    private int cantLivianas;
    private int cantMedianas;
    private int cantPesadas;
    private ServerSelectionPolicy policy;


    public AirportSimulation(int airstripCantLiviana, int airstripCantMediana, int airstripCantPesada, int airstripCantAux,
        double endTime, ServerSelectionPolicy policy) 
    {
        this.stopSimulation = false;
        this.cantLivianas = airstripCantLiviana;
        this.cantMedianas = airstripCantMediana;
        this.cantPesadas = airstripCantPesada;
        this.policy = policy;
        this.endTime = endTime;

        this.servers = new ArrayList < > ();
        agregarPistas();

        this.fel = new FutureEventList();
        agregarEvent();

        tiempoMedioTotalOcioLiviana = new double[EJECUCIONES+2][cantLivianas];
        tiempoMedioTotalOcioMediana = new double[EJECUCIONES+2][cantMedianas];
        tiempoMedioTotalOcioPesada = new double[EJECUCIONES+2][cantPesadas];
        tiempoMedioMaxOcioLiviana = new double[EJECUCIONES+2][cantLivianas];
        tiempoMedioMaxOcioMediana = new double[EJECUCIONES+2][cantMedianas];
        tiempoMedioMaxOcioPesada = new double[EJECUCIONES+2][cantPesadas];
        tamMedioMaxColaLiviana = new double[EJECUCIONES+2][cantLivianas];
        tamMedioMaxColaMediana = new double[EJECUCIONES+2][cantMedianas];
        tamMedioMaxColaPesada = new double[EJECUCIONES+2][cantPesadas];
        medioDurabilidaLiviana = new double[EJECUCIONES+2][cantLivianas];
        medioDurabilidaMediana = new double[EJECUCIONES+2][cantMedianas];
        medioDurabilidaPesada = new double[EJECUCIONES+2][cantPesadas];
    }

    public void agregarPistas()
    {
        for (int i = 0; i < cantLivianas; i++) {
            this.servers.add(new PistaLiviana(new CustomQueue()));
        }

        for (int i = 0; i < cantMedianas; i++) {
            this.servers.add(new PistaMediana(new CustomQueue()));
        }

        for (int i = 0; i < cantPesadas; i++) {
            this.servers.add(new PistaPesada(new CustomQueue()));
        }

        for (int i = 0; i < 1; i++) {
            this.servers.add(new PistaAux(new CustomQueue()));
        }
    }

    public void agregarEvent()
    {
        this.fel.insert(new StopExecutionEvent(endTime, this));
        this.fel.insert(new ArrivalEvent(0, new AircraftLiviano(), policy));
        this.fel.insert(new ArrivalEvent(0, new AircraftMediana(), policy));
        this.fel.insert(new ArrivalEvent(0, new AircraftPesada(), policy));

        Entity mantenimiento = new Mantenimiento();
        this.fel.insert(new ArrivalEvent(0, mantenimiento, policy));
        this.fel.insert(new EndOfServiceEvent(0, mantenimiento));
    }

    public double getEndTime() {
        return this.endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void setStopSimulation(boolean stopSimulation) {
        this.stopSimulation = stopSimulation;
    }

    private static final int EJECUCIONES = 50;

    private static final int []cantMediaAvionesAterrizados = new int[EJECUCIONES+2];
    private static final double []tiempoMedioEsperaEnCola = new double[EJECUCIONES+2];
    private static final double []tiempoMedioMaximoEspera = new double[EJECUCIONES+2]; 
    private static final double []tiempoMedioTransito = new double[EJECUCIONES+2];
    private static final double []tiempoMedioMaximoTransito = new double[EJECUCIONES+2];
    //Atributos de aviones livianos:
    private static final int []cantMediaAvionesAterrizadosLivianos = new int[EJECUCIONES+2];
    private static final double []tiempoMedioEsperaEnColaLivianos = new double[EJECUCIONES+2];
    private static final double []tiempoMedioMaximoEsperaLivianos = new double[EJECUCIONES+2]; 
    private static final double []tiempoMedioTransitoLivianos = new double[EJECUCIONES+2];
    private static final double []tiempoMedioMaximoTransitoLivianos = new double[EJECUCIONES+2];
    //Atributos de aviones medianos:
    private static final int []cantMediaAvionesAterrizadosMedianos= new int[EJECUCIONES+2];
    private static final double []tiempoMedioEsperaEnColaMedianos = new double[EJECUCIONES+2];
    private static final double []tiempoMedioMaximoEsperaMedianos = new double[EJECUCIONES+2]; 
    private static final double []tiempoMedioTransitoMedianos = new double[EJECUCIONES+2];
    private static final double []tiempoMedioMaximoTransitoMedianos = new double[EJECUCIONES+2];
    //Atributos de aviones pesados:
    private static final int []cantMediaAvionesAterrizadosPesados = new int[EJECUCIONES+2];
    private static final double []tiempoMedioEsperaEnColaPesados = new double[EJECUCIONES+2];
    private static final double []tiempoMedioMaximoEsperaPesados = new double[EJECUCIONES+2]; 
    private static final double []tiempoMedioTransitoPesados = new double[EJECUCIONES+2];
    private static final double []tiempoMedioMaximoTransitoPesados = new double[EJECUCIONES+2];

    private double [][]tiempoMedioTotalOcioLiviana;
    private double [][]tiempoMedioMaxOcioLiviana;
    private double [][]tamMedioMaxColaLiviana;
    private double [][]medioDurabilidaLiviana;

    private double [][]tiempoMedioTotalOcioMediana;
    private double [][]tiempoMedioMaxOcioMediana;
    private double [][]tamMedioMaxColaMediana;
    private double [][]medioDurabilidaMediana;

    private double [][]tiempoMedioTotalOcioPesada;
    private double [][]tiempoMedioMaxOcioPesada;
    private double [][]tamMedioMaxColaPesada;
    private double [][]medioDurabilidaPesada;

    @Override
    public void execute() {

        for(int i=0;i<EJECUCIONES;i++)
        {
            while (!stopSimulation) {
                fel.getImminent().planificate(this.servers, this.fel);
            }

            llenarVariablesEntity(i);
            llenarVariablesServers(i);

            Entity.resetEntity();
            AircraftLiviano.resetEntityLivianos();
            AircraftMediana.resetEntityMediano();
            AircraftPesada.resetEntityPesado();


            while(!this.fel.isEmpty()){
                this.fel.getImminent();
            }
            servers.clear();

            this.stopSimulation=false;
            agregarEvent();
            agregarPistas();
        }
        calcularMediaEntity();
        calcularSEntity();
        calcularMediaServers();
        calcularSServers();

        mostrarIntervalos();
        mostrarIntervalosServers();
    }

    public void llenarVariablesEntity(int i)
    {
        cantMediaAvionesAterrizados[i] = Entity.getIdCount();
        cantMediaAvionesAterrizados[EJECUCIONES] +=cantMediaAvionesAterrizados[i];

        tiempoMedioEsperaEnCola[i] = (double)(Entity.getTotalWaitingTime() / Entity.getIdCount());
        tiempoMedioEsperaEnCola[EJECUCIONES] += tiempoMedioEsperaEnCola[i];

        tiempoMedioMaximoEspera[i] = Entity.getMaxWaitingTime();
        tiempoMedioMaximoEspera[EJECUCIONES] += tiempoMedioMaximoEspera[i];
            
        tiempoMedioTransito[i]=(double)(Entity.getAccumulatedTransitTime() / Entity.getIdCount());
        tiempoMedioTransito[EJECUCIONES] += tiempoMedioTransito[i];
            
        tiempoMedioMaximoTransito[i]= Entity.getMaxTransitTime();
        tiempoMedioMaximoTransito[EJECUCIONES] += tiempoMedioMaximoTransito[i];

        //Livianos
        cantMediaAvionesAterrizadosLivianos[i] = AircraftLiviano.getIdLiviano();
        cantMediaAvionesAterrizadosLivianos[EJECUCIONES] += cantMediaAvionesAterrizadosLivianos[i];
            
        tiempoMedioEsperaEnColaLivianos[i] = (float) Entity.getTotalWaitingTimeLiviano() / AircraftLiviano.getIdLiviano();
        tiempoMedioEsperaEnColaLivianos[EJECUCIONES] += tiempoMedioEsperaEnColaLivianos[i];
            
        tiempoMedioMaximoEsperaLivianos[i] = Entity.getMaxWaitingTimeLiviano();
        tiempoMedioMaximoEsperaLivianos[EJECUCIONES] += tiempoMedioMaximoEsperaLivianos[i];
            
        tiempoMedioTransitoLivianos[i]=(float)(Entity.getAccumulatedTransitTimeLiviano() / AircraftMediana.getIdMedianos());
        tiempoMedioTransitoLivianos[EJECUCIONES] += tiempoMedioTransitoLivianos[i];
            
        tiempoMedioMaximoTransitoLivianos[i]= Entity.getMaxTransitTimeLiviano();
        tiempoMedioMaximoTransitoLivianos[EJECUCIONES] += tiempoMedioMaximoTransitoLivianos[i];

        //Medianos
        cantMediaAvionesAterrizadosMedianos[i] = AircraftMediana.getIdMedianos();
        cantMediaAvionesAterrizadosMedianos[EJECUCIONES] += cantMediaAvionesAterrizadosMedianos[i];
            
        tiempoMedioEsperaEnColaMedianos[i] = (double) Entity.getTotalWaitingTimeMediano() / AircraftMediana.getIdMedianos();
        tiempoMedioEsperaEnColaMedianos[EJECUCIONES] += tiempoMedioEsperaEnColaMedianos[i];
            
        tiempoMedioMaximoEsperaMedianos[i] = Entity.getMaxWaitingTimeMediano();
        tiempoMedioMaximoEsperaMedianos[EJECUCIONES] += tiempoMedioMaximoEsperaMedianos[i];
            
        tiempoMedioTransitoMedianos[i]=(double)(Entity.getAccumulatedTransitTimeMediano() / AircraftMediana.getIdMedianos());
        tiempoMedioTransitoMedianos[EJECUCIONES] += tiempoMedioTransitoMedianos[i];
            
        tiempoMedioMaximoTransitoMedianos[i]= Entity.getMaxTransitTimeMediano();
        tiempoMedioMaximoTransitoMedianos[EJECUCIONES] += tiempoMedioMaximoTransitoMedianos[i];
            
        //Pesados
        cantMediaAvionesAterrizadosPesados[i] = AircraftPesada.getIdPesados();
        cantMediaAvionesAterrizadosPesados[EJECUCIONES] += cantMediaAvionesAterrizadosPesados[i];
            
        tiempoMedioEsperaEnColaPesados[i] = (double) Entity.getTotalWaitingTimePesado() / AircraftPesada.getIdPesados();
        tiempoMedioEsperaEnColaPesados[EJECUCIONES] += tiempoMedioEsperaEnColaPesados[i];
            
        tiempoMedioMaximoEsperaPesados[i] = Entity.getMaxWaitingTimePesado();
        tiempoMedioMaximoEsperaPesados[EJECUCIONES] += tiempoMedioMaximoEsperaPesados[i];
            
        tiempoMedioTransitoPesados[i]=(double)(Entity.getAccumulatedTransitTimePesado() / AircraftPesada.getIdPesados());
        tiempoMedioTransitoPesados[EJECUCIONES] += tiempoMedioTransitoPesados[i];
            
        tiempoMedioMaximoTransitoPesados[i]= Entity.getMaxTransitTimePesado();
        tiempoMedioMaximoTransitoPesados[EJECUCIONES] += tiempoMedioMaximoTransitoPesados[i];
    }

    public void calcularMediaEntity()
    {
        cantMediaAvionesAterrizados[EJECUCIONES]=cantMediaAvionesAterrizados[EJECUCIONES]/EJECUCIONES;
        tiempoMedioEsperaEnCola[EJECUCIONES] = tiempoMedioEsperaEnCola[EJECUCIONES]/EJECUCIONES;
        tiempoMedioMaximoEspera[EJECUCIONES]=tiempoMedioMaximoEspera[EJECUCIONES]/ EJECUCIONES;
        tiempoMedioTransito[EJECUCIONES]=tiempoMedioTransito[EJECUCIONES]/EJECUCIONES;
        tiempoMedioMaximoTransito[EJECUCIONES] = tiempoMedioMaximoTransito[EJECUCIONES] /EJECUCIONES;

        cantMediaAvionesAterrizadosLivianos[EJECUCIONES]=cantMediaAvionesAterrizadosLivianos[EJECUCIONES]/EJECUCIONES;
        tiempoMedioEsperaEnColaLivianos[EJECUCIONES]=tiempoMedioEsperaEnColaLivianos[EJECUCIONES]/EJECUCIONES;
        tiempoMedioMaximoEsperaLivianos[EJECUCIONES]=tiempoMedioMaximoEsperaLivianos[EJECUCIONES]/EJECUCIONES;
        tiempoMedioTransitoLivianos[EJECUCIONES]=tiempoMedioTransitoLivianos[EJECUCIONES]/EJECUCIONES;
        tiempoMedioMaximoTransitoLivianos[EJECUCIONES]=tiempoMedioMaximoTransitoLivianos[EJECUCIONES]/EJECUCIONES;
   
        //Media de media de las medias Medianas
        cantMediaAvionesAterrizadosMedianos[EJECUCIONES]=cantMediaAvionesAterrizadosMedianos[EJECUCIONES]/EJECUCIONES;
        tiempoMedioEsperaEnColaMedianos[EJECUCIONES]=tiempoMedioEsperaEnColaMedianos[EJECUCIONES]/EJECUCIONES;
        tiempoMedioMaximoEsperaMedianos[EJECUCIONES]=tiempoMedioMaximoEsperaMedianos[EJECUCIONES]/EJECUCIONES;
        tiempoMedioTransitoMedianos[EJECUCIONES]= tiempoMedioTransitoMedianos[EJECUCIONES]/EJECUCIONES;
        tiempoMedioMaximoTransitoMedianos[EJECUCIONES]=tiempoMedioMaximoTransitoMedianos[EJECUCIONES]/EJECUCIONES;
        
        //Media de media de las medias Pesadas
        cantMediaAvionesAterrizadosPesados[EJECUCIONES]=cantMediaAvionesAterrizadosPesados[EJECUCIONES]/EJECUCIONES;
        tiempoMedioEsperaEnColaPesados[EJECUCIONES]=tiempoMedioEsperaEnColaPesados[EJECUCIONES]/EJECUCIONES;
        tiempoMedioMaximoEsperaPesados[EJECUCIONES]=tiempoMedioMaximoEsperaPesados[EJECUCIONES]/EJECUCIONES;
        tiempoMedioTransitoPesados[EJECUCIONES]=tiempoMedioTransitoPesados[EJECUCIONES]/EJECUCIONES;
        tiempoMedioMaximoTransitoPesados[EJECUCIONES]=tiempoMedioMaximoTransitoPesados[EJECUCIONES]/EJECUCIONES;  
    }

    public void calcularSEntity()
    {
        for(int i=0;i<EJECUCIONES;i++)
        {
            cantMediaAvionesAterrizados[EJECUCIONES+1]+=Math.sqrt(Math.pow(cantMediaAvionesAterrizados[i]-cantMediaAvionesAterrizados[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioEsperaEnCola[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioEsperaEnCola[i]-tiempoMedioEsperaEnCola[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioMaximoEspera[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioMaximoEspera[i]-tiempoMedioMaximoEspera[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioTransito[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioTransito[i]-tiempoMedioTransito[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioMaximoTransito[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioMaximoTransito[i]-tiempoMedioMaximoTransito[EJECUCIONES],2)/(EJECUCIONES-1));
       
            //Livianas
            cantMediaAvionesAterrizadosLivianos[EJECUCIONES+1]+=Math.sqrt(Math.pow(cantMediaAvionesAterrizadosLivianos[i]-cantMediaAvionesAterrizadosLivianos[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioEsperaEnColaLivianos[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioEsperaEnColaLivianos[i]-tiempoMedioEsperaEnColaLivianos[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioMaximoEsperaLivianos[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioMaximoEsperaLivianos[i]-tiempoMedioMaximoEsperaLivianos[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioTransitoLivianos[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioTransitoLivianos [i]-tiempoMedioTransitoLivianos [EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioMaximoTransitoLivianos[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioMaximoTransitoLivianos[i]-tiempoMedioMaximoTransitoLivianos[EJECUCIONES],2)/(EJECUCIONES-1));
       
            //Medianas
            cantMediaAvionesAterrizadosMedianos[EJECUCIONES+1]+=Math.sqrt(Math.pow(cantMediaAvionesAterrizadosMedianos[i]-cantMediaAvionesAterrizadosMedianos[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioEsperaEnColaMedianos[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioEsperaEnColaMedianos[i]-tiempoMedioEsperaEnColaMedianos[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioMaximoEsperaMedianos[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioMaximoEsperaMedianos[i]-tiempoMedioMaximoEsperaMedianos[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioTransitoMedianos[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioTransitoMedianos[i]-tiempoMedioTransitoMedianos[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioMaximoTransitoMedianos[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioMaximoTransitoMedianos[i]-tiempoMedioMaximoTransitoMedianos[EJECUCIONES],2)/(EJECUCIONES-1));
            
            //Pesadas
            cantMediaAvionesAterrizadosPesados[EJECUCIONES+1]+=Math.sqrt(Math.pow(cantMediaAvionesAterrizadosPesados[i]-cantMediaAvionesAterrizadosPesados[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioEsperaEnColaPesados[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioEsperaEnColaPesados[i]-tiempoMedioEsperaEnColaPesados[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioMaximoEsperaPesados[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioMaximoEsperaPesados[i]-tiempoMedioMaximoEsperaPesados[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioTransitoPesados[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioTransitoPesados[i]-tiempoMedioTransitoPesados[EJECUCIONES],2)/(EJECUCIONES-1));
            tiempoMedioMaximoTransitoPesados[EJECUCIONES+1]+=Math.sqrt(Math.pow(tiempoMedioMaximoTransitoPesados[i]-tiempoMedioMaximoTransitoPesados[EJECUCIONES],2)/(EJECUCIONES-1));
        }
    }

    public void llenarVariablesServers(int i)
    {
        for(int j=0;j<cantLivianas;j++)
        {
            tiempoMedioTotalOcioLiviana[i][j]=this.servers.get(j).getIdleTotal();
            tiempoMedioTotalOcioLiviana[EJECUCIONES][j]+=this.servers.get(j).getIdleTotal();

            tiempoMedioMaxOcioLiviana [i][j]=this.servers.get(j).getMaxIdleTime();
            tiempoMedioMaxOcioLiviana [EJECUCIONES][j]+=this.servers.get(j).getMaxIdleTime();

            tamMedioMaxColaLiviana[i][j]= this.servers.get(j).getQueue().getMaxSize();
            tamMedioMaxColaLiviana[EJECUCIONES][j]+=this.servers.get(j).getQueue().getMaxSize();

            medioDurabilidaLiviana[i][j]=this.servers.get(j).getHp();
            medioDurabilidaLiviana[EJECUCIONES][j]+=this.servers.get(j).getHp();
        }
        for(int j=0;j<cantMedianas;j++)
        {
            tiempoMedioTotalOcioMediana[i][j]=this.servers.get((j+cantLivianas)).getIdleTotal();
            tiempoMedioTotalOcioMediana[EJECUCIONES][j]+=this.servers.get((j+cantLivianas)).getIdleTotal();

            tiempoMedioMaxOcioMediana [i][j]=this.servers.get((j+cantLivianas)).getMaxIdleTime();
            tiempoMedioMaxOcioMediana [EJECUCIONES][j]+=this.servers.get((j+cantLivianas)).getMaxIdleTime();

            tamMedioMaxColaMediana[i][j]= this.servers.get((j+cantLivianas)).getQueue().getMaxSize();
            tamMedioMaxColaMediana[EJECUCIONES][j]+=this.servers.get((j+cantLivianas)).getQueue().getMaxSize();

            medioDurabilidaMediana[i][j]=this.servers.get((j+cantLivianas)).getHp();
            medioDurabilidaMediana[EJECUCIONES][j]+=this.servers.get((j+cantLivianas)).getHp();
        }
        for(int j=0;j<cantPesadas;j++)
        {
            tiempoMedioTotalOcioPesada[i][j]=this.servers.get(j+cantLivianas+cantMedianas).getIdleTotal();
            tiempoMedioTotalOcioPesada[EJECUCIONES][j]+=this.servers.get(j+cantLivianas+cantMedianas).getIdleTotal();

            tiempoMedioMaxOcioPesada [i][j]=this.servers.get(j+cantLivianas+cantMedianas).getMaxIdleTime();
            tiempoMedioMaxOcioPesada [EJECUCIONES][j]+=this.servers.get(j+cantLivianas+cantMedianas).getMaxIdleTime();

            tamMedioMaxColaPesada[i][j]= this.servers.get(j+cantLivianas+cantMedianas).getQueue().getMaxSize();
            tamMedioMaxColaPesada[EJECUCIONES][j]+=this.servers.get(j+cantLivianas+cantMedianas).getQueue().getMaxSize();

            medioDurabilidaPesada[i][j]=this.servers.get(j+cantLivianas+cantMedianas).getHp();
            medioDurabilidaPesada[EJECUCIONES][j]+=this.servers.get(j+cantLivianas+cantMedianas).getHp();
        }
    }

    public void calcularMediaServers(){
        for(int j=0;j<cantLivianas;j++)
        {
            tiempoMedioTotalOcioLiviana[EJECUCIONES][j]=tiempoMedioTotalOcioLiviana[EJECUCIONES][j]/EJECUCIONES;
            tiempoMedioMaxOcioLiviana [EJECUCIONES][j]=tiempoMedioMaxOcioLiviana [EJECUCIONES][j]/EJECUCIONES;
                tamMedioMaxColaLiviana[EJECUCIONES][j]=tamMedioMaxColaLiviana[EJECUCIONES][j]/EJECUCIONES;
                medioDurabilidaLiviana[EJECUCIONES][j]=medioDurabilidaLiviana[EJECUCIONES][j]/EJECUCIONES;
        }
        for(int j=0;j<cantMedianas;j++)
        {
            tiempoMedioTotalOcioMediana[EJECUCIONES][j]=tiempoMedioTotalOcioMediana[EJECUCIONES][j]/EJECUCIONES;
            tiempoMedioMaxOcioMediana[EJECUCIONES][j]=tiempoMedioMaxOcioMediana[EJECUCIONES][j]/EJECUCIONES;
            tamMedioMaxColaMediana[EJECUCIONES][j]=tamMedioMaxColaMediana[EJECUCIONES][j]/EJECUCIONES;
            medioDurabilidaMediana[EJECUCIONES][j]=medioDurabilidaMediana[EJECUCIONES][j]/EJECUCIONES;
        }
        for(int j=0;j<cantPesadas;j++)
        {
            tiempoMedioTotalOcioPesada[EJECUCIONES][j]=tiempoMedioTotalOcioPesada[EJECUCIONES][j]/EJECUCIONES;
            tiempoMedioMaxOcioPesada[EJECUCIONES][j]=tiempoMedioMaxOcioPesada[EJECUCIONES][j]/EJECUCIONES;
            tamMedioMaxColaPesada[EJECUCIONES][j]=tamMedioMaxColaPesada[EJECUCIONES][j]/EJECUCIONES;
            medioDurabilidaPesada[EJECUCIONES][j]=medioDurabilidaPesada[EJECUCIONES][j]/EJECUCIONES;            
        }    
    }

    public void calcularSServers(){
        for(int i=0;i<EJECUCIONES;i++)
        {
            for(int j=0;j<cantLivianas;j++)
            {
                tiempoMedioTotalOcioLiviana[EJECUCIONES+1][j]+=Math.sqrt(Math.pow(tiempoMedioTotalOcioLiviana[i][j]-tiempoMedioTotalOcioLiviana[EJECUCIONES][j],2)/(EJECUCIONES-1));
                tiempoMedioMaxOcioLiviana[EJECUCIONES+1][j]+=Math.sqrt(Math.pow(tiempoMedioMaxOcioLiviana[i][j]-tiempoMedioMaxOcioLiviana[EJECUCIONES][j],2)/(EJECUCIONES-1));
                tamMedioMaxColaLiviana[EJECUCIONES+1][j]+=Math.sqrt(Math.pow(tamMedioMaxColaLiviana[i][j]-tamMedioMaxColaLiviana[EJECUCIONES][j],2)/(EJECUCIONES-1));
                medioDurabilidaLiviana[EJECUCIONES+1][j]+=Math.sqrt(Math.pow(medioDurabilidaLiviana[i][j]-medioDurabilidaLiviana[EJECUCIONES][j],2)/(EJECUCIONES-1));
            }
            for(int j=0;j<cantMedianas;j++)
            {
                tiempoMedioTotalOcioMediana[EJECUCIONES+1][j]+=Math.sqrt(Math.pow(tiempoMedioTotalOcioMediana[i][j]-tiempoMedioTotalOcioMediana[EJECUCIONES][j],2)/(EJECUCIONES-1));
                tiempoMedioMaxOcioMediana[EJECUCIONES+1][j]+=Math.sqrt(Math.pow(tiempoMedioMaxOcioMediana[i][j]-tiempoMedioMaxOcioMediana[EJECUCIONES][j],2)/(EJECUCIONES-1));
                tamMedioMaxColaMediana[EJECUCIONES+1][j]+=Math.sqrt(Math.pow(tamMedioMaxColaMediana[i][j]-tamMedioMaxColaMediana[EJECUCIONES][j],2)/(EJECUCIONES-1));
                medioDurabilidaMediana[EJECUCIONES+1][j]+=Math.sqrt(Math.pow(medioDurabilidaMediana[i][j]-medioDurabilidaMediana[EJECUCIONES][j],2)/(EJECUCIONES-1));
            }     
            for(int j=0;j<cantPesadas;j++)
            {
                tiempoMedioTotalOcioPesada[EJECUCIONES+1][j]+=Math.sqrt(Math.pow(tiempoMedioTotalOcioPesada[i][j]-tiempoMedioTotalOcioPesada[EJECUCIONES][j],2)/(EJECUCIONES-1));
                tiempoMedioMaxOcioPesada[EJECUCIONES+1][j]+=Math.sqrt(Math.pow(tiempoMedioMaxOcioPesada[i][j]-tiempoMedioMaxOcioPesada[EJECUCIONES][j],2)/(EJECUCIONES-1));
                tamMedioMaxColaPesada[EJECUCIONES+1][j]+=Math.sqrt(Math.pow(tamMedioMaxColaPesada[i][j]-tamMedioMaxColaPesada[EJECUCIONES][j],2)/(EJECUCIONES-1));
                medioDurabilidaPesada[EJECUCIONES+1][j]+=Math.sqrt(Math.pow(medioDurabilidaPesada[i][j]-medioDurabilidaPesada[EJECUCIONES][j],2)/(EJECUCIONES-1));
            }
        }
    }

    public String mostrarIntervalosServers()
    {
        DecimalFormat formato1 = new DecimalFormat("#.##");
        String ret="\nIntervalos Servers=";

        for(int j=0;j<cantLivianas;j++)
        {
            ret+=("\n------------------------------------------------------\n"+
            "Pista Liviana n°"+(j+1)+":");
            ret+=("Tiempo de Ocio: ["+
                        formato1.format(tiempoMedioTotalOcioLiviana[EJECUCIONES][j] - (1.96 * (tiempoMedioTotalOcioLiviana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        ","+formato1.format(tiempoMedioTotalOcioLiviana[EJECUCIONES][j] + (1.96 * (tiempoMedioTotalOcioLiviana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        "]\n"+
                        "Porcentaje total de ocio: ["+
                        formato1.format(((tiempoMedioTotalOcioLiviana[EJECUCIONES][j] - (1.96 * (tiempoMedioTotalOcioLiviana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))*100)/40329)
                        +"%, "+
                        formato1.format(((tiempoMedioTotalOcioLiviana[EJECUCIONES][j] + (1.96 * (tiempoMedioTotalOcioLiviana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))*100)/40329)+
                        "%]\n"+
                        "Maximo Ocio: ["+
                        formato1.format(tiempoMedioMaxOcioLiviana[EJECUCIONES][j] - (1.96 * (tiempoMedioMaxOcioLiviana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        ","+formato1.format(tiempoMedioMaxOcioLiviana[EJECUCIONES][j] + (1.96 * (tiempoMedioMaxOcioLiviana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        "]\n"+
                        "Tamaño maximo de cola: ["+
                        formato1.format(tamMedioMaxColaLiviana[EJECUCIONES][j] - (1.96 * (tamMedioMaxColaLiviana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        ","+formato1.format(tamMedioMaxColaLiviana[EJECUCIONES][j] + (1.96 * (tamMedioMaxColaLiviana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        "]\n"+
                        "Durabilidad: ["+
                        formato1.format(medioDurabilidaLiviana[EJECUCIONES][j] - (1.96 * (medioDurabilidaLiviana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        ","+formato1.format(medioDurabilidaLiviana[EJECUCIONES][j] + (1.96 * (medioDurabilidaLiviana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        "]");
        }

        for(int j=0;j<cantMedianas;j++)
        {
            ret+=("\n------------------------------------------------------\n"+
            "Pista Mediana n°"+(j+1)+":");
            ret+=("Tiempo de Ocio: ["+
                        formato1.format(tiempoMedioTotalOcioMediana[EJECUCIONES][j] - (1.96 * (tiempoMedioTotalOcioMediana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        ","+formato1.format(tiempoMedioTotalOcioMediana[EJECUCIONES][j] + (1.96 * (tiempoMedioTotalOcioMediana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        "]\n"+
                        "Porcentaje total de ocio: ["+
                        formato1.format(((tiempoMedioTotalOcioMediana[EJECUCIONES][j] - (1.96 * (tiempoMedioTotalOcioMediana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))*100)/40329)
                        +"%, "+
                        formato1.format(((tiempoMedioTotalOcioMediana[EJECUCIONES][j] + (1.96 * (tiempoMedioTotalOcioMediana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))*100)/40329)+
                        "%]\n"+
                        "Maximo Ocio: ["+
                        formato1.format(tiempoMedioMaxOcioMediana[EJECUCIONES][j] - (1.96 * (tiempoMedioMaxOcioMediana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        ","+formato1.format(tiempoMedioMaxOcioMediana[EJECUCIONES][j] + (1.96 * (tiempoMedioMaxOcioMediana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        "]\n"+
                        "Tamaño maximo de cola: ["+
                        formato1.format(tamMedioMaxColaMediana[EJECUCIONES][j] - (1.96 * (tamMedioMaxColaMediana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        ","+formato1.format(tamMedioMaxColaMediana[EJECUCIONES][j] + (1.96 * (tamMedioMaxColaMediana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        "]\n"+
                        "Durabilidad: ["+
                        formato1.format(medioDurabilidaMediana[EJECUCIONES][j] - (1.96 * (medioDurabilidaMediana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        ","+formato1.format(medioDurabilidaMediana[EJECUCIONES][j] + (1.96 * (medioDurabilidaMediana[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        "]");
        }
        
        for(int j=0;j<cantPesadas;j++)
        {
            ret+=("\n------------------------------------------------------\n"+
            "Pista Pesada n°"+(j+1)+":"+"");
            ret+=("Tiempo de Ocio: ["+
                        formato1.format(tiempoMedioTotalOcioPesada[EJECUCIONES][j] - (1.96 * (tiempoMedioTotalOcioPesada[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        ","+(tiempoMedioTotalOcioPesada[EJECUCIONES][j] + (1.96 * (tiempoMedioTotalOcioPesada[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        "]\n"+
                        "Porcentaje total de ocio: ["+
                        formato1.format(((tiempoMedioTotalOcioPesada[EJECUCIONES][j] - (1.96 * (tiempoMedioTotalOcioPesada[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))*100)/40329)
                        +"%, "+
                        formato1.format(((tiempoMedioTotalOcioPesada[EJECUCIONES][j] + (1.96 * (tiempoMedioTotalOcioPesada[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))*100)/40329)+
                        "%]\n"+
                        "Maximo Ocio: ["+
                        formato1.format(tiempoMedioMaxOcioPesada[EJECUCIONES][j] - (1.96 * (tiempoMedioMaxOcioPesada[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        ","+formato1.format(tiempoMedioMaxOcioPesada[EJECUCIONES][j] + (1.96 * (tiempoMedioMaxOcioPesada[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        "]\n"+
                        "Tamaño maximo de cola: ["+
                        formato1.format(tamMedioMaxColaPesada[EJECUCIONES][j] - (1.96 * (tamMedioMaxColaPesada[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        ","+formato1.format(tamMedioMaxColaPesada[EJECUCIONES][j] + (1.96 * (tamMedioMaxColaPesada[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        "]\n"+
                        "Durabilidad: ["+
                        formato1.format(medioDurabilidaPesada[EJECUCIONES][j] - (1.96 * (medioDurabilidaPesada[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        ","+formato1.format(medioDurabilidaPesada[EJECUCIONES][j] + (1.96 * (medioDurabilidaPesada[EJECUCIONES+1][j]/Math.sqrt(EJECUCIONES))))+
                        "]");
        }
        ret+="";
        return ret;
    }

    public String mostrarIntervalos()
    {
        DecimalFormat formato1 = new DecimalFormat("#.##");
        String ret="\n\nIntervalos Entidades:\n"+
        "------------------------------------------------------\n";
        ret+=("Cantidad de Aviones Aterrizados: ["+
                            formato1.format(cantMediaAvionesAterrizados[EJECUCIONES] - (1.96 * (cantMediaAvionesAterrizados[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(cantMediaAvionesAterrizados[EJECUCIONES] + (1.96 * (cantMediaAvionesAterrizados[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo Medio de espera en cola: ["
                            +formato1.format(tiempoMedioEsperaEnCola[EJECUCIONES] - (1.96 * (tiempoMedioEsperaEnCola[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioEsperaEnCola[EJECUCIONES] + (1.96 * (tiempoMedioEsperaEnCola[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo maximo de espera: ["
                            +formato1.format(tiempoMedioMaximoEspera[EJECUCIONES] - (1.96 * (tiempoMedioMaximoEspera[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioMaximoEspera[EJECUCIONES] + (1.96 * (tiempoMedioMaximoEspera[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo medio de transito: ["
                            +formato1.format(tiempoMedioTransito[EJECUCIONES] - (1.96 * (tiempoMedioTransito[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioTransito[EJECUCIONES] + (1.96 * (tiempoMedioTransito[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo maximo de transito: ["
                            +formato1.format(tiempoMedioMaximoTransito[EJECUCIONES] - (1.96 * (tiempoMedioMaximoTransito[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioMaximoTransito[EJECUCIONES] + (1.96 * (tiempoMedioMaximoTransito[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "------------------------------------------------------"+
                            "\nCantidad de Aviones Livianos Aterrizados: ["+
                            formato1.format(cantMediaAvionesAterrizadosLivianos[EJECUCIONES] - (1.96 * (cantMediaAvionesAterrizadosLivianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(cantMediaAvionesAterrizadosLivianos[EJECUCIONES] + (1.96 * (cantMediaAvionesAterrizadosLivianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo medio de espera en cola: ["
                            +formato1.format(tiempoMedioEsperaEnColaLivianos[EJECUCIONES] - (1.96 * (tiempoMedioEsperaEnColaLivianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioEsperaEnColaLivianos[EJECUCIONES] + (1.96 * (tiempoMedioEsperaEnColaLivianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo maximo de espera: ["
                            +formato1.format(tiempoMedioMaximoEsperaLivianos[EJECUCIONES] - (1.96 * (tiempoMedioMaximoEsperaLivianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioMaximoEsperaLivianos[EJECUCIONES] + (1.96 * (tiempoMedioMaximoEsperaLivianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo medio de transito: ["
                            +formato1.format(tiempoMedioTransitoLivianos[EJECUCIONES] - (1.96 * (tiempoMedioTransitoLivianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioTransitoLivianos[EJECUCIONES] + (1.96 * (tiempoMedioTransitoLivianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo maximo de transito: ["
                            +formato1.format(tiempoMedioMaximoTransitoLivianos[EJECUCIONES] - (1.96 * (tiempoMedioMaximoTransitoLivianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioMaximoTransitoLivianos[EJECUCIONES] + (1.96 * (tiempoMedioMaximoTransitoLivianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "------------------------------------------------------"+
                            "\nCantidad de Aviones Medianos Aterrizados: ["+
                            formato1.format(cantMediaAvionesAterrizadosMedianos[EJECUCIONES] - (1.96 * (cantMediaAvionesAterrizadosMedianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(cantMediaAvionesAterrizadosMedianos[EJECUCIONES] + (1.96 * (cantMediaAvionesAterrizadosMedianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo Medio de espera en cola: ["
                            +formato1.format(tiempoMedioEsperaEnColaMedianos[EJECUCIONES] - (1.96 * (tiempoMedioEsperaEnColaMedianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioEsperaEnColaMedianos[EJECUCIONES] + (1.96 * (tiempoMedioEsperaEnColaMedianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo maximo de espera: ["
                            +formato1.format(tiempoMedioMaximoEsperaMedianos[EJECUCIONES] - (1.96 * (tiempoMedioMaximoEsperaMedianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioMaximoEsperaMedianos[EJECUCIONES] + (1.96 * (tiempoMedioMaximoEsperaMedianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo medio de transito: ["
                            +formato1.format(tiempoMedioTransitoMedianos[EJECUCIONES] - (1.96 * (tiempoMedioTransitoMedianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioTransitoMedianos[EJECUCIONES] + (1.96 * (tiempoMedioTransitoMedianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo maximo de transito: ["
                            +formato1.format(tiempoMedioMaximoTransitoMedianos[EJECUCIONES] - (1.96 * (tiempoMedioMaximoTransitoMedianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioMaximoTransitoMedianos[EJECUCIONES] + (1.96 * (tiempoMedioMaximoTransitoMedianos[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "------------------------------------------------------"+ 
                            "\nCantidad de Aviones Pesados Aterrizados: ["+
                            formato1.format(cantMediaAvionesAterrizadosPesados[EJECUCIONES] - (1.96 * (cantMediaAvionesAterrizados[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(cantMediaAvionesAterrizadosPesados[EJECUCIONES] + (1.96 * (cantMediaAvionesAterrizados[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo Medio de espera en cola: ["
                            +formato1.format(tiempoMedioEsperaEnColaPesados[EJECUCIONES] - (1.96 * (tiempoMedioEsperaEnColaPesados[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioEsperaEnColaPesados[EJECUCIONES] + (1.96 * (tiempoMedioEsperaEnColaPesados[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo maximo de espera: ["
                            +formato1.format(tiempoMedioMaximoEsperaPesados[EJECUCIONES] - (1.96 * (tiempoMedioMaximoEsperaPesados[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioMaximoEsperaPesados[EJECUCIONES] + (1.96 * (tiempoMedioMaximoEsperaPesados[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo medio de transito: ["
                            +formato1.format(tiempoMedioTransitoPesados[EJECUCIONES] - (1.96 * (tiempoMedioTransitoPesados[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioTransitoPesados[EJECUCIONES] + (1.96 * (tiempoMedioTransitoPesados[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]\n"+
                            "Tiempo maximo de transito: ["
                            +formato1.format(tiempoMedioMaximoTransitoPesados[EJECUCIONES] - (1.96 * (tiempoMedioMaximoTransitoPesados[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            ","+formato1.format(tiempoMedioMaximoTransitoPesados[EJECUCIONES] + (1.96 * (tiempoMedioMaximoTransitoPesados[EJECUCIONES+1]/Math.sqrt(EJECUCIONES))))+
                            "]");

        return ret;
    }

    // GENERATE REPORT INTO FILE EXAMPLE
    @Override
    public String generateReport(boolean intoFile, String fileName) 
    {
        this.report += reportEntity();
        this.report += mostrarIntervalos();
        this.report += reportServer();
        this.report += mostrarIntervalosServers();

        if (intoFile) {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
                writer.write(this.report);
                writer.close();
            } catch (Exception exception) {
                System.out.println("Error when trying to write the report into a file.");
                System.out.println("Showing on screen...");
                System.out.println(this.report);
            }
        }

        System.out.println(this.report);
        return this.report;
    }

    public String reportEntity()
    {
        DecimalFormat formato1 = new DecimalFormat("#.##");
        String ret="";
        ret += "Cantidad media de aviones aterrizado = " + (cantMediaAvionesAterrizados[EJECUCIONES] - 1) + "\n" +
            "Tiempo medio de espera = " + formato1.format((float)( tiempoMedioEsperaEnCola[EJECUCIONES])) + "\n" +
            "Tiempo maximo de espera = " + formato1.format(tiempoMedioMaximoEspera[EJECUCIONES]) + "\n" +
            "Tiempo medio de transito = " + formato1.format((float)(tiempoMedioTransito[EJECUCIONES])) + "\n" +
            "Tiempo maximo de transito = " + formato1.format(tiempoMedioMaximoTransito[EJECUCIONES]) + "\n" +
            

            "------------------------------------------------------\n" +
            "Aviones Livanos:\n" +
            "Cantidad de Aviones Livianos = " + cantMediaAvionesAterrizadosLivianos[EJECUCIONES] + "\n" +
            "Tiempo MEDIO de espera = " + formato1.format((float) tiempoMedioEsperaEnColaLivianos[EJECUCIONES]) + "\n" +
            "Tiempo MAXIMO de espera = " + formato1.format(tiempoMedioMaximoEsperaLivianos[EJECUCIONES]) + "\n" +
            "Tiempo MEDIO de transito = " + formato1.format((float)(tiempoMedioTransitoLivianos[EJECUCIONES])) + "\n" +
            "Tiempo MAXIMO de transito = " + formato1.format(tiempoMedioMaximoTransitoLivianos[EJECUCIONES]) + "\n" +
            

            "------------------------------------------------------\n" +
            "Aviones Medianos:\n" +
            "Cantidad de Aviones Medianos = " + cantMediaAvionesAterrizadosMedianos[EJECUCIONES] + "\n" +
            "Tiempo MEDIO de espera = " + formato1.format((float) tiempoMedioEsperaEnColaMedianos[EJECUCIONES]) + "\n" +
            "Tiempo MAXIMO de espera = " + formato1.format(tiempoMedioMaximoEsperaMedianos[EJECUCIONES]) + "\n" +
            "Tiempo MEDIO de transito = " + formato1.format((float)(tiempoMedioTransitoMedianos[EJECUCIONES])) + "\n" +
            "Tiempo MAXIMO de transito = " + formato1.format(tiempoMedioMaximoTransitoMedianos[EJECUCIONES]) + "\n" +
            

            "------------------------------------------------------\n" +
            "Aviones Pesados:\n" +
            "Cantidad de Aviones Pesados = " + cantMediaAvionesAterrizadosPesados[EJECUCIONES] + "\n" +
            "Tiempo MEDIO de espera = " + formato1.format((float) tiempoMedioEsperaEnColaPesados[EJECUCIONES]) + "\n" +
            "Tiempo MAXIMO de espera = " + formato1.format(tiempoMedioMaximoEsperaPesados[EJECUCIONES]) + "\n" +
            "Tiempo MEDIO de transito = " + formato1.format((float)(tiempoMedioTransitoPesados[EJECUCIONES])) + "\n"+
            "Tiempo MAXIMO de transito = " + formato1.format(tiempoMedioMaximoTransitoPesados[EJECUCIONES]) + "\n" ;
            
        return ret;
    }
    
    public String reportServer() {
        DecimalFormat formato1 = new DecimalFormat("#.##");
        String ret = "";
        
        for (int i = 0; i < cantLivianas; i++) {
            ret += "\n\n------------------------------------------------------\n" +
                "Pista " + i + "(Liviana):\n"+
                "Tiempo medio TOTAL de ocio = " + formato1.format(tiempoMedioTotalOcioLiviana[EJECUCIONES][i]) + "\n" +
                "Porcentaje medio del total de ocio = " + formato1.format((float)(tiempoMedioTotalOcioLiviana[EJECUCIONES][i] * 100) / 40320) + "%\n" +
                "Media Tiempo maximo de ocio = " + formato1.format(tiempoMedioMaxOcioLiviana[EJECUCIONES][i]) + "\n" +
                "Media Tamaño maximo de la cola de espera = " + tamMedioMaxColaPesada[EJECUCIONES][i] + "\n";
        }
        
        for (int i = 0; i < cantMedianas; i++) {
            ret += "------------------------------------------------------\n" +
                "Pista " + i + "(Mediana):\n"+
                "Tiempo medio TOTAL de ocio = " + formato1.format(tiempoMedioTotalOcioMediana[EJECUCIONES][i]) + "\n" +
                "Porcentaje medio del total de ocio = " + formato1.format((float)(tiempoMedioTotalOcioMediana[EJECUCIONES][i] * 100) / 40320) + "%\n" +
                "Media Tiempo maximo de ocio = " + formato1.format(tiempoMedioMaxOcioMediana[EJECUCIONES][i]) + "\n" +
                "Media Tamaño maximo de la cola de espera = " + tamMedioMaxColaMediana[EJECUCIONES][i] + "\n";
        }
        
        for (int i = 0; i < cantPesadas; i++) {
            ret += "------------------------------------------------------\n" +
                "Pista " + i + "(Pesada):\n"+
                "Tiempo medio TOTAL de ocio = " + formato1.format(tiempoMedioTotalOcioPesada[EJECUCIONES][i]) + "\n" +
                "Porcentaje medio del total de ocio = " + formato1.format((float)(tiempoMedioTotalOcioPesada[EJECUCIONES][i] * 100) / 40320) + "%\n" +
                "Media Tiempo maximo de ocio = " + formato1.format(tiempoMedioMaxOcioPesada[EJECUCIONES][i]) + "\n" +
                "Media Tamaño maximo de la cola de espera = " + tamMedioMaxColaPesada[EJECUCIONES][i] + "\n";
        }
        /* 
        ret += "------------------------------------------------------\n" +
            "Pista "+(cantLivianas + cantMedianas + cantPesadas)+ "(Aux):\nTiempo TOTAL de ocio = " + formato1.format(this.servers.get(cantLivianas + cantMedianas + cantPesadas).getIdleTotal()) + "\n" +
            "Porcentaje del tiempo total de ocio = " + formato1.format((float)(this.servers.get(cantLivianas + cantMedianas + cantPesadas).getIdleTotal() * 100) / 40320) + "%\n" +
            "Tiempo maximo de ocio = " + formato1.format(this.servers.get(cantLivianas + cantMedianas + cantPesadas).getMaxIdleTime()) + "\n" +
            "Tamaño maximo de la cola de espera para este servidor = " + this.servers.get(cantLivianas + cantMedianas + cantPesadas).getQueue().getMaxSize() + "\n";
        */    
        return ret;
    }
}