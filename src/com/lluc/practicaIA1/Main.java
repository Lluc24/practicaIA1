package com.lluc.practicaIA1;
import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class Main {
    public static void main(String[] args) {

        int npaq = 5;
        int seed = 1;
        double ratio = 2;
        Paquetes paquetes = new Paquetes(npaq, seed);
        Transporte transporte = new Transporte(paquetes, ratio, seed);

        Collections.sort(paquetes, new Comparator<Paquete>() {
            @Override
            public int compare(Paquete p1, Paquete p2) {
                return p1.getPrioridad() - p2.getPrioridad();
            }
        });
        Estado inicial = new Estado(paquetes, transporte);
    }
}
