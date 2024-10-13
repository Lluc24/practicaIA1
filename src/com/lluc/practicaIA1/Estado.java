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
        Paquete paquete = paquetes.get(iPaquete);
        while (iPaquete < paquetes.size()) {
            if (vecOfertas[iOferta] - paquete.getPeso() > 0) {
                vecPaquetes[iPaquete] = iOferta;
                vecOfertas[iOferta] -= paquete.getPeso();
                ++iPaquete;
                paquete = paquetes.get(iPaquete);

                coste += oferta.getPrecio()*paquete.getPeso();
                if (oferta.getDias() >= 3 && oferta.getDias() < 5) {
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
            }
            else {
                ++iOferta;
                oferta = transporte.get(iOferta);
            }
        }
    }

    public double heuristicoCoste() {
        return coste;
    }

    public double heuristicoCoseFelicidad() {
        return felicidad + coste;
    }

    public boolean moure_paquete(int ip, int oferta_desti, Paquetes paquetes, Transporte ofertas) {
    	Paquete p = paquetes.get(ip);
    	double peso_paquete = p.getPeso();
    	
    	vecOfertas[vecPaquetes[ip]] += peso_paquete;
    	vecOfertas[oferta_desti] -= peso_paquete;
    	
    	Oferta oferta_des = ofertas.get(oferta_desti);
    	Oferta oferta_og = ofertas.get(vecPaquetes[ip]);
    	
    	if (oferta_des.getPesomax() < vecOfertas[oferta_desti]) return false;
    
    	coste += oferta_des.getPrecio()*peso_paquete;
    	coste -= oferta_og.getPrecio()*peso_paquete;
    	
    	int ofertadesti_tempal = 0;
    	if(oferta_des.getDias() == 3 || oferta_des.getDias() == 4) ofertadesti_tempal = 1;
    	else if (oferta_des.getDias() == 5) ofertadesti_tempal = 2;
    	
    	int ofertaog_tempal = 0;
    	if(oferta_og.getDias() == 3 || oferta_og.getDias() == 4) ofertaog_tempal = 1;
    	else if (oferta_og.getDias() == 5) ofertaog_tempal = 2;
    	
    	//precio guardar en almacen
    	coste -= ofertaog_tempal* 0.25 * peso_paquete;
    	coste += ofertadesti_tempal* 0.25 * peso_paquete;
    	
    	if(p.getPrioridad() == Paquete.PR1) {
    		if(oferta_des.getDias() > 1) return false;
    	}
    	else if(p.getPrioridad() == Paquete.PR2) {
    		if(oferta_des.getDias() > 3) return false;
    	}
    	
    	int antelacion_original = 0;
    	if(p.getPrioridad() == Paquete.PR2) {
    		if(oferta_og.getDias() == 1) antelacion_original = 1;
    	}
    	else if(p.getPrioridad() == Paquete.PR3) {
    		if(oferta_og.getDias() == 1) antelacion_original = 2;
    		if(oferta_og.getDias() == 2 || oferta_og.getDias() == 3) antelacion_original = 1;
    	}
    	
    	felicidad -= antelacion_original;
    	
    	int nueva_antelacion = 0;
    	
    	if(p.getPrioridad() == Paquete.PR2) {
    		if(oferta_des.getDias() == 1) nueva_antelacion = 1;
    	}
    	else if(p.getPrioridad() == Paquete.PR3) {
    		if(oferta_des.getDias() == 1) nueva_antelacion = 2;
    		if(oferta_des.getDias() == 2 || oferta_des.getDias() == 3) nueva_antelacion = 1;
    	}
    	
    	felicidad += nueva_antelacion;
    	
    	vecPaquetes[ip] = oferta_desti;
    	
    	return true;
    	
    }
    
    public boolean swap(int paquete_1, int paquete_2, Paquetes paquetes, Transporte ofertas) {
    	int ofertap1 = vecPaquetes[paquete_1];
    	int ofertap2 = vecPaquetes[paquete_2];
    	
    	double peso_paquete1 = paquetes.get(paquete_1).getPeso();
    	double peso_paquete2 = paquetes.get(paquete_2).getPeso();
    	
    	vecOfertas[ofertap1] += peso_paquete1 - peso_paquete2;
    	vecOfertas[ofertap2] -= peso_paquete1 - peso_paquete2;
    	
    	Oferta oferta1 = ofertas.get(ofertap1);
    	Oferta oferta2 = ofertas.get(ofertap2);
    	
    	if (oferta1.getPesomax() < vecOfertas[ofertap1]) return false;
    	if (oferta2.getPesomax() < vecOfertas[ofertap2]) return false;
    	
    	//Calculamos los costes segun el precio de la oferta
    	coste -= oferta1.getPrecio()*peso_paquete1 + oferta2.getPrecio()*peso_paquete2;
    	coste += oferta1.getPrecio()*peso_paquete2 + oferta2.getPrecio()*peso_paquete1;
    	
    	int dias2 = oferta2.getDias();
    	int dias1 = oferta1.getDias();
    	
    	Paquete paquete1 = paquetes.get(paquete_1);
    	Paquete paquete2 = paquetes.get(paquete_2);
    	
    	
    	//comprobamos is la nueva asignacion cumple la demanda de dias
    	if(paquete1.getPrioridad() == Paquete.PR1) {
    		if(oferta2.getDias() != 1) return false;
    	} 
    	else if(paquete1.getPrioridad() == Paquete.PR2) {
    		if(oferta2.getDias() > 3) return false;
    	}
    	
    	if(paquete2.getPrioridad() == Paquete.PR1) {
    		if(oferta1.getDias() != 1) return false;
    	} 
    	else if(paquete2.getPrioridad() == Paquete.PR2) {
    		if(oferta1.getDias() > 3) return false;
    	}
    	
    	//calcular con que antelacion iban a llegar los paquetes en la distribucion original
    	
    	int antelacion_original1 = 0;
    	int antelacion_original2 = 0;
    	
    	if(paquete1.getPrioridad() == Paquete.PR2) {
    		if(oferta1.getDias() == 1) antelacion_original1 = 1;
    	}
    	else if(paquete1.getPrioridad() == Paquete.PR3) {
    		if(oferta1.getDias() == 1) antelacion_original1 = 2;
    		if(oferta1.getDias() == 2 || oferta1.getDias() == 3) antelacion_original1 = 1;
    	}
    	
    	if(paquete2.getPrioridad() == Paquete.PR2) {
    		if(oferta2.getDias() == 1) antelacion_original2 = 1;
    	}
    	else if(paquete2.getPrioridad() == Paquete.PR3) {
    		if(oferta2.getDias() == 1) antelacion_original2 = 2;
    		if(oferta2.getDias() == 2 || oferta2.getDias() == 3) antelacion_original2 = 1;
    	}
    	
    	felicidad -= antelacion_original2 + antelacion_original1;
    	
    	//calculamos la nueva antelacion con la que llegaran los paquetes segun las asignaciones hechas
    	
    	int antelacion_postcambio1 = 0; 
    	int antelacion_postcambio2 = 0;
    	
    	if(paquete1.getPrioridad() == Paquete.PR2) {
    		if(oferta2.getDias() == 1) antelacion_postcambio1 = 1;
    	}
    	else if(paquete1.getPrioridad() == Paquete.PR3) {
    		if(oferta2.getDias() == 1) antelacion_postcambio1 = 2;
    		if(oferta2.getDias() == 2 || oferta2.getDias() == 3) antelacion_postcambio1 = 1;
    	}
    	
    	
    	if(paquete2.getPrioridad() == Paquete.PR2) {
    		if(oferta1.getDias() == 1) antelacion_postcambio2 = 1;
    	}
    	else if(paquete2.getPrioridad() == Paquete.PR3) {
    		if(oferta1.getDias() == 1) antelacion_postcambio2 = 2;
    		if(oferta1.getDias() == 2 || oferta2.getDias() == 3) antelacion_postcambio2 = 1;
    	}
    	
    	felicidad += antelacion_postcambio1 + antelacion_postcambio2;
    	
    	//Calculamos los costes segun el cambio de tiempo que están en almacenaje
    	int oferta1_tempal = 0;
    	if(oferta1.getDias() == 3 || oferta1.getDias() == 4) oferta1_tempal = 1;
    	else if (oferta1.getDias() == 5) oferta1_tempal = 2;
    	
    	int oferta2_tempal = 0;
    	if(oferta2.getDias() == 3 || oferta2.getDias() == 4) oferta2_tempal = 1;
    	else if (oferta2.getDias() == 5) oferta2_tempal = 2;
    	
    	/*Aquests valors assignats a oferta1_tempal i oferta2_tempal es basen en el contingut del pdf
    	El pdf dice que si una oferta es de 3 a 4 dias, se llevan el paquete en un dia
    	si es de 5 dias se lleva el paquete en dos dias
    	si es de 1 a 2 dias, se llevan el paquete ese mismo dia, por lo tanto no lo incluyo en el coste?
    	*/
    	
    	coste -= oferta1_tempal*0.25 * peso_paquete1 + oferta2_tempal*0.25 * peso_paquete2;
    	coste += oferta1_tempal*0.25 * peso_paquete2 + oferta2_tempal*0.25 * peso_paquete1;
    	
    	vecPaquetes[paquete_1] = ofertap2;
    	vecPaquetes[paquete_2] = ofertap1;
    	return true;
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
