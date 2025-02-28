package com.lluc.practicaIA1;
import IA.Azamon.Paquetes;
import IA.Azamon.Paquete;
import IA.Azamon.Transporte;
import IA.Azamon.Oferta;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;


public class Estado {
    public static Paquetes paquetes;
    public static Transporte transporte;
    public static double a, b;

    private int vecPaquetes[];
    private double vecOfertas[];
    private double coste = 0;
    private double felicidad = 0;
    
    public double getCoste() {
        return coste;
    }
    public double getFelicidad() {
        return felicidad;
    }

    public double getHeuristica() {
        return a*coste - b*felicidad;
    }

    Estado(boolean greedy) {
        vecPaquetes = new int[paquetes.size()];
        vecOfertas = new double[transporte.size()];

        for (int i = 0; i < paquetes.size(); ++i) vecPaquetes[i] = -1;

        for (int i = 0; i < transporte.size(); ++i) vecOfertas[i] = transporte.get(i).getPesomax();

        if (greedy) generarSolucionGreedy();
        else generarSolucionIngenua();

        if (!solucionInicialValida()) {
            System.out.println("Solucion invalida!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            System.exit(0);
        }
    }

    Estado(Estado a) {
        vecPaquetes = new int[paquetes.size()];
        System.arraycopy(a.vecPaquetes, 0, this.vecPaquetes, 0, paquetes.size());
        this.vecOfertas = new double[transporte.size()];
        System.arraycopy(a.vecOfertas, 0, this.vecOfertas, 0, transporte.size());
        this.coste = a.coste;
        this.felicidad = a.felicidad;
    }

    void generarSolucionIngenua() {
        int iOferta = 0;
        int iPaquete = 0;
        Oferta oferta = transporte.get(iOferta);
        while (iPaquete < paquetes.size()) {
            Paquete paquete = paquetes.get(iPaquete);
            if (vecOfertas[iOferta] - paquete.getPeso() >= 0) {
                vecPaquetes[iPaquete] = iOferta;
                vecOfertas[iOferta] -= paquete.getPeso();
                coste += oferta.getPrecio() * paquete.getPeso();
                if (oferta.getDias() == 3 || oferta.getDias() == 4) {
                    coste += 0.25 * paquete.getPeso();
                } else if (oferta.getDias() == 5) {
                    coste += 2 * 0.25 * paquete.getPeso();
                }
                if (paquete.getPrioridad() == Paquete.PR2 && oferta.getDias() == 1) {
                    felicidad += 1;
                } else if (paquete.getPrioridad() == Paquete.PR3 && oferta.getDias() < 4) {
                    felicidad += 4 - oferta.getDias();
                }
                ++iPaquete;
            } else {
                ++iOferta;
                oferta = transporte.get(iOferta);
            }
        }
    }

    void generarSolucionGreedy() {
        int iPaquete = 0;
        while (iPaquete < vecPaquetes.length) {
            Paquete paquete = paquetes.get(iPaquete);
            boolean asignado = false;
            int iOferta = 0;
            Oferta oferta = transporte.get(iOferta);
            while (!asignado) {
                if (vecOfertas[iOferta] - paquete.getPeso() >= 0) {
                    asignado = true;
                    vecPaquetes[iPaquete] = iOferta;
                    vecOfertas[iOferta] -= paquete.getPeso();
                    coste += oferta.getPrecio() * paquete.getPeso();
                    if (oferta.getDias() == 3 || oferta.getDias() == 4) {
                        coste += 0.25 * paquete.getPeso();
                    } else if (oferta.getDias() == 5) {
                        coste += 2 * 0.25 * paquete.getPeso();
                    }
                    if (paquete.getPrioridad() == Paquete.PR2 && oferta.getDias() == 1) {
                        felicidad += 1;
                    } else if (paquete.getPrioridad() == Paquete.PR3 && oferta.getDias() < 4) {
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
    }

    public boolean moure_paquete(int ip, int oferta_desti) {
        Paquete p = paquetes.get(ip);
        double peso_paquete = p.getPeso();

        //si intenta moure a la mateixa oferta retorna error
        if (vecPaquetes[ip] == oferta_desti) return false;

        vecOfertas[vecPaquetes[ip]] += peso_paquete;
        vecOfertas[oferta_desti] -= peso_paquete;

        Oferta oferta_des = transporte.get(oferta_desti);
        Oferta oferta_og = transporte.get(vecPaquetes[ip]);

        if (0 > vecOfertas[oferta_desti]) return false;

        coste += oferta_des.getPrecio() * peso_paquete;
        coste -= oferta_og.getPrecio() * peso_paquete;

        int ofertadesti_tempal = 0;
        if (oferta_des.getDias() == 3 || oferta_des.getDias() == 4) ofertadesti_tempal = 1;
        else if (oferta_des.getDias() == 5) ofertadesti_tempal = 2;

        int ofertaog_tempal = 0;
        if (oferta_og.getDias() == 3 || oferta_og.getDias() == 4) ofertaog_tempal = 1;
        else if (oferta_og.getDias() == 5) ofertaog_tempal = 2;

        //precio guardar en almacen
        coste -= ofertaog_tempal * 0.25 * peso_paquete;
        coste += ofertadesti_tempal * 0.25 * peso_paquete;

        if (p.getPrioridad() == Paquete.PR1) {
            if (oferta_des.getDias() > 1) return false;
        } else if (p.getPrioridad() == Paquete.PR2) {
            if (oferta_des.getDias() > 3) return false;
        }
        int antelacion_original = 0;
        if (p.getPrioridad() == Paquete.PR2) {
            if (oferta_og.getDias() == 1) antelacion_original = 1;
        } else if (p.getPrioridad() == Paquete.PR3 && oferta_og.getDias() < 4) {
            antelacion_original += 4 - oferta_og.getDias();
        }

        felicidad -= antelacion_original;

        int nueva_antelacion = 0;

        if (p.getPrioridad() == Paquete.PR2) {
            if (oferta_des.getDias() == 1) nueva_antelacion = 1;
        } else if (p.getPrioridad() == Paquete.PR3 && oferta_des.getDias() < 4) {
            nueva_antelacion += 4 - oferta_des.getDias();
        }

        felicidad += nueva_antelacion;

        vecPaquetes[ip] = oferta_desti;

        return true;

    }

    public boolean swap(int paquete_1, int paquete_2) {
        int ofertap1 = vecPaquetes[paquete_1];
        int ofertap2 = vecPaquetes[paquete_2];

        //si la asignació d'oferta es la mateixa per els dos paquets no la contemplem
        if (ofertap1 == ofertap2) return false;

        double peso_paquete1 = paquetes.get(paquete_1).getPeso();
        double peso_paquete2 = paquetes.get(paquete_2).getPeso();

        vecOfertas[ofertap1] += peso_paquete1 - peso_paquete2;
        vecOfertas[ofertap2] -= peso_paquete1 - peso_paquete2;

        Oferta oferta1 = transporte.get(ofertap1);
        Oferta oferta2 = transporte.get(ofertap2);

        if (0 > vecOfertas[ofertap1]) return false;
        if (0 > vecOfertas[ofertap2]) return false;

        //Calculamos los costes segun el precio de la oferta
        coste -= oferta1.getPrecio() * peso_paquete1 + oferta2.getPrecio() * peso_paquete2;
        coste += oferta1.getPrecio() * peso_paquete2 + oferta2.getPrecio() * peso_paquete1;

        int dias2 = oferta2.getDias();
        int dias1 = oferta1.getDias();

        Paquete paquete1 = paquetes.get(paquete_1);
        Paquete paquete2 = paquetes.get(paquete_2);


        //comprobamos is la nueva asignacion cumple la demanda de dias
        if (paquete1.getPrioridad() == Paquete.PR1) {
            if (oferta2.getDias() != 1) return false;
        } else if (paquete1.getPrioridad() == Paquete.PR2) {
            if (oferta2.getDias() > 3) return false;
        }

        if (paquete2.getPrioridad() == Paquete.PR1) {
            if (oferta1.getDias() != 1) return false;
        } else if (paquete2.getPrioridad() == Paquete.PR2) {
            if (oferta1.getDias() > 3) return false;
        }

        //calcular con que antelacion iban a llegar los paquetes en la distribucion original

        int antelacion_original1 = 0;
        int antelacion_original2 = 0;

        if (paquete1.getPrioridad() == Paquete.PR2) {
            if (oferta1.getDias() == 1) antelacion_original1 = 1;
        } else if (paquete1.getPrioridad() == Paquete.PR3 && oferta1.getDias() < 4) {
            antelacion_original1 += 4 - oferta1.getDias();
        }

        if (paquete2.getPrioridad() == Paquete.PR2) {
            if (oferta2.getDias() == 1) antelacion_original2 = 1;
        } else if (paquete2.getPrioridad() == Paquete.PR3 && oferta2.getDias() < 4) {
            antelacion_original2 += 4 - oferta2.getDias();
        }

        felicidad -= antelacion_original2 + antelacion_original1;

        //calculamos la nueva antelacion con la que llegaran los paquetes segun las asignaciones hechas

        int antelacion_postcambio1 = 0;
        int antelacion_postcambio2 = 0;

        if (paquete1.getPrioridad() == Paquete.PR2) {
            if (oferta2.getDias() == 1) antelacion_postcambio1 = 1;
        } else if (paquete1.getPrioridad() == Paquete.PR3 && oferta2.getDias() < 4) {
            antelacion_postcambio1 += 4 - oferta2.getDias();
        }


        if (paquete2.getPrioridad() == Paquete.PR2) {
            if (oferta1.getDias() == 1) antelacion_postcambio2 = 1;
        } else if (paquete2.getPrioridad() == Paquete.PR3 && oferta1.getDias() < 4) {
            antelacion_postcambio2 += 4 - oferta1.getDias();
        }

        felicidad += antelacion_postcambio1 + antelacion_postcambio2;

        //Calculamos los costes segun el cambio de tiempo que están en almacenaje
        int oferta1_tempal = 0;
        if (oferta1.getDias() == 3 || oferta1.getDias() == 4) oferta1_tempal = 1;
        else if (oferta1.getDias() == 5) oferta1_tempal = 2;

        int oferta2_tempal = 0;
        if (oferta2.getDias() == 3 || oferta2.getDias() == 4) oferta2_tempal = 1;
        else if (oferta2.getDias() == 5) oferta2_tempal = 2;

    	/*Aquests valors assignats a oferta1_tempal i oferta2_tempal es basen en el contingut del pdf
    	El pdf dice que si una oferta es de 3 a 4 dias, se llevan el paquete en un dia
    	si es de 5 dias se lleva el paquete en dos dias
    	si es de 1 a 2 dias, se llevan el paquete ese mismo dia, por lo tanto no lo incluyo en el coste?
    	*/

        coste -= oferta1_tempal * 0.25 * peso_paquete1 + oferta2_tempal * 0.25 * peso_paquete2;
        coste += oferta1_tempal * 0.25 * peso_paquete2 + oferta2_tempal * 0.25 * peso_paquete1;

        vecPaquetes[paquete_1] = ofertap2;
        vecPaquetes[paquete_2] = ofertap1;
        return true;
    }

    public void imprimir_tabla_2() {
        for (int i = 0; i < vecOfertas.length; ++i) {
            double peso_acumulado = 0;
            System.out.println("---------------------------------------------------------------------------------------------------------");
            String oferta = "Oferta " + i + transporte.get(i).toString().substring(8);
            System.out.print("| " + oferta + " |");
            for (int j = 0; j < vecPaquetes.length; ++j) {
                if (vecPaquetes[j] == i) {
                    peso_acumulado += paquetes.get(j).getPeso();
                    String paquete = paquetes.get(j).toString().substring(9);
                    System.out.print(" Paquete " + j + paquete + " |");
                }
            }
            System.out.print(" Peso libre por vector->" + vecOfertas[i] + "kg ");
            System.out.print("contador->" + (transporte.get(i).getPesomax() - peso_acumulado) + "kg |");
        }
    }

    private boolean solucionInicialValida() {
        boolean[] paqueteAsignado = new boolean[vecPaquetes.length];
        for (int i = 0; i < vecPaquetes.length; ++i) paqueteAsignado[i] = false;

        for (int i = 0; i < vecOfertas.length; ++i) {
            double peso_acumulado = 0;
            for (int j = 0; j < vecPaquetes.length; ++j) {
                if (vecPaquetes[j] == i) {
                    peso_acumulado += paquetes.get(j).getPeso();
                    if (paquetes.get(j).getPrioridad() == Paquete.PR1) {
                        if (transporte.get(i).getDias() > 1) {
                            System.out.println("1 P" + j + "O" + i);
                            return false;
                        }
                    } else if (paquetes.get(j).getPrioridad() == Paquete.PR2) {
                        if (transporte.get(i).getDias() > 3) {
                            System.out.println("2 P" + j + "O" + i);
                            return false;
                        }
                    }
                    paqueteAsignado[j] = true;
                }
            }
            if (transporte.get(i).getPesomax() < peso_acumulado) {
                System.out.println("3 O" + i);
                return false;
            }
            if (transporte.get(i).getPesomax() - peso_acumulado != vecOfertas[i]) {
                System.out.println("4 O" + i);
                return false;
            }
        }
        for (int i = 0; i < vecPaquetes.length; ++i) {
            if (!paqueteAsignado[i]) {
                System.out.println("5 P" + i);
                return false;
            }
        }
        return true;
    }

    public String toString() {
        String s = "Valores (" + getCosteEU() + " €, " + felicidad + " feliz)";
        return s;
    }

    public String getCosteEU() {
        String costeEU = "" + coste;
        int comma = -1;
        for (int i = 0; i < costeEU.length(); ++i) {
            if (costeEU.charAt(i) == '.') comma = i;
        }
        if (comma == -1) return costeEU;
        if (comma == costeEU.length() - 1) return costeEU.substring(0, comma);
        if (comma == costeEU.length() - 2) return costeEU.substring(0, comma) + "," + costeEU.charAt(comma+1);
        return costeEU.substring(0, comma) + "," + costeEU.substring(comma+1, comma+3);
    }
}