package com.lluc.practicaIA1;
import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;
import IA.probTSP.ProbTSPBoard;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.*;

public class AzamonSuccessorFunction implements SuccessorFunction {

    public List<Successor> getSuccessors(Object a) {
        ArrayList<Successor> retVal = new ArrayList();
        Estado estadoActual = (Estado) a;
        Paquetes paquetes = Estado.paquetes;
        Transporte transporte = Estado.transporte;

        for (int i = 0; i < paquetes.size(); i++) {
            for (int j = i + 1; j < paquetes.size(); j++) {
                Estado newState = new Estado(estadoActual);

                if(newState.swap(i, j)){
                    String S = ("INTERCAMBIO " + " " + i + " " + j + " " + newState.toString() + "\n");
                    retVal.add(new Successor(S, newState));
                }
            }
        }
        for (int i = 0; i < paquetes.size(); ++i) {
            for (int j = 0; j < transporte.size(); ++j) {
                Estado newState = new Estado(estadoActual);
                if(newState.moure_paquete(i, j)) {
                    String S = "MOVIDO paquete: " + i + " a la oferta: " + j + " " +newState.toString() + "\n";
                    retVal.add(new Successor(S, newState));
                }
            }
        }

        return retVal;
    }


}
