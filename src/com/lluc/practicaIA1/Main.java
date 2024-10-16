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
        System.out.println("introduce el número de paquetes");
        Scanner scanner = new Scanner(System.in);
        int npaq = scanner.nextInt();
        System.out.println("introduce la semilla");
        scanner = new Scanner(System.in);
        int seed = scanner.nextInt();
        System.out.println("introduce el ratio");
        scanner = new Scanner(System.in);
        double ratio = scanner.nextDouble();
        Paquetes paquetes = new Paquetes(npaq, seed);
        Transporte transporte = new Transporte(paquetes, ratio, seed);
        Estado.paquetes = paquetes;
        Estado.transporte = transporte;
        paquetes.sort(new Comparator<Paquete>() {
            @Override
            public int compare(Paquete p1, Paquete p2) {
                return p1.getPrioridad() - p2.getPrioridad();
            }
        });
        Estado inicial = new Estado();

        // Seleccionar uno
        inicial.imprimir_tabla();
        azamonHillClimbingSearch(inicial);
        //azamonSimulatedAnnealingSearch(inicial);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /*
    private static void azamonSimulatedAnnealingSearch(Estado estado) {
        try {
            AzamonSuccessorFunctionSA successorFunction = new AzamonSuccessorFunctionSA();
            AzamonGoalTest goalTest = new AzamonGoalTest();
            AzamonHeuristicFunction1 heuristicFunction1 = new AzamonHeuristicFunction1();
            Problem problem =  new Problem(estado, successorFunction, goalTest, heuristicFunction1);
            Search search = new SimulatedAnnealingSearch(2000,100,5,0.001);
            SearchAgent agent = new SearchAgent(problem, search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
    private static void printInstrumentation(Properties properties) {
        Iterator keys = properties.keySet().iterator();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            String property = properties.getProperty(key);
            System.out.println(key + " $ " + property);
        }

    }

    private static void printActions(List actions) {
        
        for (int i = 0; i < actions.size(); i++) {
            String action = (String) actions.get(i);
            System.out.println(action);
        }

        /*
        int n = actions.size() - 1;
        String action = (String) actions.get(n);
        System.out.println(action);
        */
    }

}
