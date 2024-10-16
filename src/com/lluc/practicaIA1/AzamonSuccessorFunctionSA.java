package com.lluc.practicaIA1;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AzamonSuccessorFunctionSA implements SuccessorFunction  {
    public List getSuccessors(Object aState) {
        ArrayList retVal = new ArrayList();
        Estado hijo = new Estado((Estado)aState);
        Random myRandom=new Random(Estado.semilla);

        int npaq = Estado.paquetes.size();
        int nof = Estado.transporte.size();

        int factorRamificacionMoverPaquete = npaq*nof;      // Se puede mover a static en Estado o Main
        int factorRamificacionSwapPaquetes = npaq*npaq;     // para evitar calcularlo en cada iteracion de SA
        int factorRamificacionTotal = factorRamificacionMoverPaquete + factorRamificacionSwapPaquetes;

        int numeroRandom = myRandom.nextInt(factorRamificacionTotal);  // 0 <= numeroRandom < factorRamificacionTotal

        if (numeroRandom < factorRamificacionMoverPaquete) { // Usamos el operador mover paquete
            int paqueteRandom = myRandom.nextInt(npaq); // 0 <= paqueteRandom < npaq
            int ofertaRandom = myRandom.nextInt(nof);
            while (!hijo.moure_paquete(paqueteRandom, ofertaRandom)) {
                paqueteRandom = myRandom.nextInt(npaq);
                ofertaRandom = myRandom.nextInt(nof);
                String S = ("MOVIDO " + " paquete " + paqueteRandom + " a oferta " + ofertaRandom + " | " + hijo.toString());
                retVal.add(new Successor(S, hijo));
            }
        }
        else { // Usamos el operador swap paquete
            int paqueteRandom1 = myRandom.nextInt(npaq); // 0 <= paqueteRandom1 < npaq
            int paqueteRandom2 = myRandom.nextInt(npaq); // 0 <= paqueteRandom2 < npaq
            while (!hijo.swap(paqueteRandom1, paqueteRandom2)) {
                paqueteRandom1 = myRandom.nextInt(npaq);
                paqueteRandom2 = myRandom.nextInt(npaq);
                String S = ("INTERCAMBIO " + " " + paqueteRandom1 + " " + paqueteRandom1 + " | " + hijo.toString());
                retVal.add(new Successor(S, hijo));
            }
        }
        return retVal;
    }
}
