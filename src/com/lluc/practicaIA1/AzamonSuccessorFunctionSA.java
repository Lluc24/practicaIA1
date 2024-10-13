package com.lluc.practicaIA1;

import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;
import IA.probTSP.ProbTSPBoard;
import IA.probTSP.ProbTSPHeuristicFunction;
import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AzamonSuccessorFunctionSA /*implements SuccessorFunction*/  {
    public List getSuccessors(int npaq, Paquetes paquetes, Transporte transporte) {
        ArrayList retVal = new ArrayList();
        //ProbTSPHeuristicFunction TSPHF  = new ProbTSPHeuristicFunction();
        Random myRandom=new Random();
        int i,j;

        // Nos ahorramos generar todos los sucesores escogiendo un par de paquetes al azar

        i=myRandom.nextInt(npaq);

        do{
            j=myRandom.nextInt(npaq);
        } while (i==j);


        //ProbTSPBoard newBoard = new ProbTSPBoard(board.getNCities(), board.getPath(), board.getDists());
        Estado newState = new Estado(paquetes, transporte);

        if(newState.swap(i, j, paquetes, transporte)) {

            //double   v = TSPHF.getHeuristicValue(newBoard);
            String S = ("INTERCAMBIO " + " " + i + " " + j + " " + newState.toString());

            retVal.add(new Successor(S, newState));
        }

        return retVal;
    }
}
