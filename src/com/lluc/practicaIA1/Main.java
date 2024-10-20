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
        System.out.println();
        int npaq = scanner.nextInt();

        Random r = new Random();
        int seed = r.nextInt();

        System.out.print("Introduce ratio de espacio ");
        scanner = new Scanner(System.in);
        System.out.println();
        double ratio = scanner.nextDouble();
        Paquetes paquetes = new Paquetes(npaq, seed);
        Transporte transporte = new Transporte(paquetes, ratio, seed);

        Estado.paquetes = paquetes;
        Estado.transporte = transporte;
        Estado.semilla = seed; //me paree que esto no es necesario

        paquetes.sort(new Comparator<Paquete>() {
            @Override
            public int compare(Paquete p1, Paquete p2) {
                return p1.getPrioridad() - p2.getPrioridad();
            }
        });
        Estado inicial = new Estado();

        // Seleccionar uno
        long ini_time, end_time;
        ini_time = System.nanoTime();
        azamonHillClimbingSearch(inicial);
        end_time = System.nanoTime();
        System.out.println("Durada Hill Climbing: " + (end_time-ini_time)/1000000 + "ms" );
        /*
        ini_time = System.nanoTime();
        azamonSimulatedAnnealingSearch(inicial, npaq, ratio);
        end_time = System.nanoTime();
        System.out.println("Durada Simulated Annealing: " + (end_time-ini_time)/1000000 + "ms" );
        */
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
            /*
            System.out.println("Solucion Hill Climbing");
            solucion.imprimir_tabla();
            */
            System.out.print("Coste HC: " + solucion.get_coste());
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
            System.out.print("Coste SA: " + solucion.get_coste());
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
