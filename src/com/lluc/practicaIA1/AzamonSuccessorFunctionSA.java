package com.lluc.practicaIA1;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AzamonSuccessorFunctionSA implements SuccessorFunction  {

    public List<Successor> getSuccessors(Object aState) {
        ArrayList<Successor> retVal = new ArrayList();
        Estado hijo = (Estado) aState;
        Random myRandom = new Random();
        int npaq = Estado.paquetes.size();
        int nof = Estado.transporte.size();

        int factorRamificacionMoverPaquete = npaq*nof;      // Se puede mover a static en Estado o Main
        int factorRamificacionSwapPaquetes = npaq*npaq;     // para evitar calcularlo en cada iteracion de SA
        int factorRamificacionTotal = factorRamificacionMoverPaquete + factorRamificacionSwapPaquetes;

        int numeroRandom = myRandom.nextInt(factorRamificacionTotal);  // 0 <= numeroRandom < factorRamificacionTotal
        Estado newState = new Estado(hijo);
        if (numeroRandom < factorRamificacionMoverPaquete) { // Usamos el operador mover paquete
            int paqueteRandom = myRandom.nextInt(npaq); // 0 <= paqueteRandom < npaq
            int ofertaRandom = myRandom.nextInt(nof);
            while (!newState.moure_paquete(paqueteRandom, ofertaRandom) /*&& i < nof*/) {
                paqueteRandom = myRandom.nextInt(npaq); // 0 <= paqueteRandom < npaq
                ofertaRandom = myRandom.nextInt(nof);
                newState = new Estado(hijo);
            }
            String S;
            S = ("MOVIDO " + " paquete " + paqueteRandom + " a oferta " + ofertaRandom + " | " + newState.toString() + "\n");

            retVal.add(new Successor(S, newState));
        }
        else { // Usamos el operador swap paquete
            int paqueteRandom1 = myRandom.nextInt(npaq); // 0 <= paqueteRandom1 < npaq
            int paqueteRandom2 = myRandom.nextInt(npaq); // 0 <= paqueteRandom2 < npaq
            while (!newState.swap(paqueteRandom1, paqueteRandom2) /*&& i < npaq*/) {
                paqueteRandom1 = myRandom.nextInt(npaq);
                paqueteRandom2 = myRandom.nextInt(npaq);
                newState = new Estado(hijo);
            }
            String S;
            S = ("INTERCAMBIO " + " " + paqueteRandom1 + " " + paqueteRandom1 + " | " + newState.toString() + "\n");
            retVal.add(new Successor(S, newState));
        }
        return retVal;
    }
}
