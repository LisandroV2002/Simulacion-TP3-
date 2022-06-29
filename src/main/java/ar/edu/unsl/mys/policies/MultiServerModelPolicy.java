package ar.edu.unsl.mys.policies;

import java.util.List;

import ar.edu.unsl.mys.entities.*;
import ar.edu.unsl.mys.entities.Entity;
import ar.edu.unsl.mys.resources.PistaLiviana;
import ar.edu.unsl.mys.resources.PistaMediana;
import ar.edu.unsl.mys.resources.PistaPesada;
import ar.edu.unsl.mys.resources.Server;

public class MultiServerModelPolicy implements ServerSelectionPolicy {

    private static MultiServerModelPolicy policy;

    public static MultiServerModelPolicy getInstance() {
        if (MultiServerModelPolicy.policy == null)
            MultiServerModelPolicy.policy = new MultiServerModelPolicy();

        return MultiServerModelPolicy.policy;
    }

    public Server selectServer(List < Server > servers, Entity entity) {
        int tipo, sizeMenor, indexRetorno = -1, i, cantLivianas = 0, cantMediana = 0, cantPesada = 0;
        
        for (Server elemento: servers){
            if(elemento instanceof PistaLiviana)
                cantLivianas++;
            else if(elemento instanceof PistaMediana)
                cantMediana++;
            else if(elemento instanceof PistaPesada)
                cantPesada++;
        }

        if (entity instanceof AircraftLiviano)
            tipo = 1;
        else if (entity instanceof AircraftMediana)
            tipo = 2;
        else if (entity instanceof AircraftPesada)
            tipo = 3;
        else
            tipo = 4; // instanceof Mantenimiento

        switch (tipo) {
            case 1: // retornar pista liviana con cola mas corta
                sizeMenor = servers.get(cantLivianas - 1).getQueue().getSize();
                for (i = cantLivianas - 1; i >= 0; i--) {
                    if (servers.get(i).getQueue().getSize() <= sizeMenor && !servers.get(i).getEstadoMantenimiento()) { // getEstadMante: si no esta en mantenimiento devuelve verdadero
                        sizeMenor = servers.get(i).getQueue().getSize();
                        indexRetorno = i;
                    }
                }
                break;

            case 2: // retornar pista mediana
                sizeMenor = servers.get(cantMediana+cantLivianas - 1).getQueue().getSize();
                for (i = cantMediana + cantLivianas - 1; i >= cantLivianas; i--) {
                    if (servers.get(i).getQueue().getSize() <= sizeMenor && !servers.get(i).getEstadoMantenimiento()) {
                        sizeMenor = servers.get(i).getQueue().getSize();
                        indexRetorno = i;
                    }
                }
                break;

            case 3: // retornar pista pesada
                sizeMenor = servers.get(cantLivianas + cantMediana + cantPesada - 1).getQueue().getSize();
                for (i = cantLivianas + cantMediana + cantPesada - 1; i >= cantLivianas + cantMediana; i--) {
                    if (servers.get(i).getQueue().getSize() <= sizeMenor && !servers.get(i).getEstadoMantenimiento()) {
                        sizeMenor = servers.get(i).getQueue().getSize();
                        indexRetorno = i;
                    }
                }
                break;

            case 4:
                Server pistaReparable = servers.get(0);
                for (i = 0; i < cantLivianas + cantMediana + cantPesada; i++) {
                    if (servers.get(i).getPorcentajeHp() <= pistaReparable.getPorcentajeHp()) {
                        pistaReparable = servers.get(i);
                        indexRetorno = i;
                        
                    }
                }
                break;
        }
        if (indexRetorno != -1)
            return servers.get(indexRetorno);
        else
            return servers.get(cantLivianas + cantMediana + cantPesada); // retorna pista auxiliar
    }
}