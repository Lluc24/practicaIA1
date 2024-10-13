package com.lluc.practicaIA1;
import IA.Azamon.Paquetes;
import IA.Azamon.Paquete;
import IA.Azamon.Transporte;
import IA.Azamon.Oferta;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


public class Estado {
    int vecPaquetes[];
    double vecOfertas[];
    double coste = 0;
    double felicidad = 0;

    Estado(Paquetes paquetes, Transporte transporte) {
        vecPaquetes = new int[paquetes.size()];
        vecOfertas = new double[transporte.size()];

        for (int i = 0; i < transporte.size(); ++i) {
            vecOfertas[i] = transporte.get(i).getPesomax();
        }

        generarSolucion1(paquetes, transporte);
        imprimir(paquetes, transporte);
    }

    void generarSolucion1(Paquetes paquetes, Transporte transporte) {
        int iOferta = 0;
        int iPaquete = 0;
        Oferta oferta = transporte.get(iOferta);
        while (iPaquete < paquetes.size()) {
            Paquete paquete = paquetes.get(iPaquete);
            if (vecOfertas[iOferta] - paquete.getPeso() > 0) {
                vecPaquetes[iPaquete] = iOferta;
                vecOfertas[iOferta] -= paquete.getPeso();
                coste += oferta.getPrecio()*paquete.getPeso();
                if (oferta.getDias() == 3 || oferta.getDias() == 4) {
                    coste += 0.25*paquete.getPeso();
                }
                else if (oferta.getDias() == 5) {
                    coste += 2*0.25*paquete.getPeso();
                }
                if (paquete.getPrioridad() == Paquete.PR2 && oferta.getDias() == 1) {
                    felicidad += 1;
                }
                else if (paquete.getPrioridad() == Paquete.PR3 && oferta.getDias() < 4) {
                    felicidad += 4 - oferta.getDias();
                }
                ++iPaquete;
            }
            else {
                ++iOferta;
                oferta = transporte.get(iOferta);
            }
        }
    }

    public double heuristicoCoste() {
        return -coste;
    }

    public double heuristicoCoseFelicidad() {
        return felicidad - coste;
    }

    public void imprimir(Paquetes paquetes, Transporte transporte) {
        for (int i = 0; i < vecOfertas.length; ++i) {
            System.out.println("Oferta: " + i + " peso libre " + vecOfertas[i]);
            System.out.println(transporte.get(i).toString());
            for (int j = 0; j < vecPaquetes.length; ++j) {
                if (vecPaquetes[j] == i) {
                    System.out.println("Paquete: " + j);
                    System.out.println(paquetes.get(j).toString());
                }
            }
        }
    }
}
