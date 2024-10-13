package com.lluc.practicaIA1;
import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;
import IA.probTSP.ProbTSPBoard;
import aima.search.framework.Successor;

import java.util.*;

public class AzamonSuccessorFunction /*implements SuccessorFunction*/ {
    public List getSuccessors(Paquetes paquetes, Transporte transporte) {
        ArrayList                retVal = new ArrayList();

        for (int i = 0; i < paquetes.size(); i++) {
            for (int j = i + 1; j < paquetes.size(); j++) {
                Estado newState = new Estado(paquetes, transporte);

                if(newState.swap(i, j, paquetes, transporte)){
                    String S = ("INTERCAMBIO " + " " + i + " " + j + " " + newState.toString());
                    retVal.add(new Successor(S, newState));
                }
            }
        }

        return retVal;
    }
}
