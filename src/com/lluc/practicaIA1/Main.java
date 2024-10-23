package com.lluc.practicaIA1;
import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;

import java.util.*;

import aima.search.framework.*;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class Main {
    public static void main(String[] args) {
        System.out.print("Introduce numero de paquetes: ");
        Scanner scanner = new Scanner(System.in);
        int npaq = scanner.nextInt();
        System.out.println();

        int seed;
        System.out.print("Quieres semilla random? [1 para si / 0 para no]: ");
        scanner = new Scanner(System.in);
        int random = scanner.nextInt();
        System.out.println();
        if (random == 1) {
            Random r = new Random();
            seed = r.nextInt();
            System.out.println("La semilla es: " + seed);
        }
        else {
            System.out.print("Introduce la semilla: ");
            scanner = new Scanner(System.in);
            seed = scanner.nextInt();
            System.out.println();
        }

        boolean greedy = true;

        System.out.print("Introduce ratio de espacio: ");
        Scanner scanner2 = new Scanner(System.in);
        double ratio = scanner2.nextDouble();
        System.out.println();

        Paquetes paquetes = new Paquetes(npaq, seed);
        Transporte transporte = new Transporte(paquetes, ratio, seed);

        Estado.paquetes = paquetes;
        Estado.transporte = transporte;

        if (greedy) {
            paquetes.sort(new Comparator<Paquete>() {
                @Override
                public int compare(Paquete p1, Paquete p2) {
                    // TODO return 1 if p2 should be before p1
                    //      return -1 if p1 should be before p2
                    //      return 0 otherwise (meaning the order stays the same)
                    if (p1.getPrioridad() < p2.getPrioridad()) return -1;
                    else if (p1.getPrioridad() > p2.getPrioridad()) return 1;
                    else {
                        if (p1.getPeso() > p2.getPeso()) return -1;
                        else if (p1.getPeso() < p2.getPeso()) return 1;
                        else return 0;
                    }
                }
            });

            transporte.sort(new Comparator<Oferta>() {
                @Override
                public int compare(Oferta o1, Oferta o2) {
                    // TODO return 1 if o2 should be before o1
                    //      return -1 if o1 should be before o2
                    //      return 0 otherwise (meaning the order stays the same)
                    if (o1.getDias() < o2.getDias()) return -1;
                    else if (o1.getDias() > o2.getDias()) return 1;
                    else {
                        if (o1.getPrecio() > o2.getPrecio()) return 1;
                        else if (o1.getPrecio() < o2.getPrecio()) return -1;
                        else return 0;
                    }
                }
            });
        } else {
            paquetes.sort(new Comparator<Paquete>() {
                @Override
                public int compare(Paquete p1, Paquete p2) {
                    return p1.getPrioridad() - p2.getPrioridad();
                }
            });
        }

        long ini_time, end_time;
        ini_time = System.nanoTime();

        Estado inicial = new Estado(greedy);

        azamonHillClimbingSearch(inicial);
        end_time = System.nanoTime();
        System.out.println("Durada Hill Climbing: " + (end_time-ini_time)/1000000 + "ms" );
    }


    private static void azamonHillClimbingSearch(Estado estado) {
        System.out.println("\nAzamon HillClimbing  -->");
        try {
            AzamonSuccessorFunction successorFunction = new AzamonSuccessorFunction();
            AzamonGoalTest goalTest = new AzamonGoalTest();
            AzamonHeuristicFunction1 heuristicFunction1 = new AzamonHeuristicFunction1();
            Problem problem =  new Problem(estado, successorFunction, goalTest, heuristicFunction1);
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());

            Estado solucion = (Estado) search.getGoalState();

            System.out.println();
            System.out.print("Coste HC: " + solucion.getCosteEU());
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void azamonSimulatedAnnealingSearch(Estado estado, int n_paquetes, double ratio) {
        System.out.println("\nAzamon Simulated Annealing  -->");
        try {
            AzamonSuccessorFunctionSA successorFunction = new AzamonSuccessorFunctionSA();
            AzamonGoalTest goalTest = new AzamonGoalTest();
            AzamonHeuristicFunction1 heuristicFunction1 = new AzamonHeuristicFunction1();
            Problem problem =  new Problem(estado, successorFunction, goalTest, heuristicFunction1);
            Search search = new SimulatedAnnealingSearch((n_paquetes*n_paquetes)/*(int)((n_paquetes * 1000)*(1/ratio))*/, 200, 20, 0.001);
            //Steps: pasos maximos de la solución (mi idea es que ha de ser dependiente del numero de paquetes)
            //Stiter: ni idea
            //k = numero de nodos succesores desde nodo actual
            //lambda = temperatura inicial
            SearchAgent agent = new SearchAgent(problem, search);

            System.out.println();
            //printActions(agent.getActions());
            //printInstrumentation(agent.getInstrumentation());

            Estado solucion = (Estado) search.getGoalState();
            /*
            System.out.println();
            System.out.println("Solucion Simulated Annealing");
            solucion.imprimir_tabla();

            System.out.println();
            */
            System.out.print("Coste SA: " + solucion.getCosteEU());
            System.out.println();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " : " + property);
        }

    }

    private static void printActions(List actions) {
        /*for (int i = 0; i < actions.size(); i++) {
            String action = actions.toString();
            System.out.println(action);
        }*/
       System.out.println(actions.toString());
    }
}
