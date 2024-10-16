package com.lluc.practicaIA1;
import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;
import IA.probTSP.ProbTSPBoard;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.*;

public class AzamonSuccessorFunction implements SuccessorFunction {
    public List getSuccessors(Object a) {
        ArrayList                retVal = new ArrayList();
        Estado estado_actual = (Estado) a;
        Paquetes paquetes = estado_actual.get_paquetes();
        Transporte transporte = estado_actual.get_transporte();
        for (int i = 0; i < paquetes.size(); i++) {
            for (int j = i + 1; j < paquetes.size(); j++) {
                Estado newState = new Estado(estado_actual);

                if(newState.swap(i, j)){
                    String S = ("INTERCAMBIO " + " " + i + " " + j + " " + newState.toString());
                    retVal.add(new Successor(S, newState));
                }
            }
        }
        for (int i = 0; i < paquetes.size(); ++i) {
            for (int j = 0; j < transporte.size(); ++j) {
                Estado newState = new Estado(estado_actual);
                if(newState.moure_paquete(i, j)) {
                    String S = "MOVIDO paquete: " + i + " a la oferta " + j + newState.toString();
                    retVal.add(new Successor(S, newState));
                }
            }
        }

        return retVal;
    }
}
