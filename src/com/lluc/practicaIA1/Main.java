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

        System.out.print("Introduce ratio de espacio: ");
        scanner = new Scanner(System.in);
        double ratio = scanner.nextDouble();

        System.out.print("¿Que algoritmo quieres usar? [1 para Hill Climbing / 0 para Simulated Annealing]: ");
        scanner = new Scanner(System.in);
        boolean hillClimb = (scanner.nextInt() == 1);

        int steps = 10000, stiter = 1000, k = 25;
        double lambda = 0.01;
        if (!hillClimb) {
            System.out.println("A continuacion introduce los parametros del Simulated annealing");
            System.out.print("Parametro steps [10000 por defecto]: ");
            scanner = new Scanner(System.in);
            steps = scanner.nextInt();
            System.out.print("Parametro stiter [1000 por defecto]: ");
            scanner = new Scanner(System.in);
            stiter = scanner.nextInt();
            System.out.print("Parametro K [25 por defecto]: ");
            scanner = new Scanner(System.in);
            k = scanner.nextInt();
            System.out.println("Parametro lambda [0.01 por defecto]: ");
            scanner = new Scanner(System.in);
            lambda = scanner.nextDouble();
        }

        int seed;
        System.out.print("¿Quieres semilla random? [1 para si / 0 para no]: ");
        scanner = new Scanner(System.in);
        if (scanner.nextInt() == 1) {
            Random r = new Random();
            seed = r.nextInt();
            System.out.println("La semilla random es: " + seed);
        }
        else {
            System.out.print("Introduce la semilla: ");
            scanner = new Scanner(System.in);
            seed = scanner.nextInt();
        }

        boolean greedy;
        System.out.print("¿Que estrategia para generar la solucion inicial quieres usar? [1 para avariciosa / 0 para ingenua]: ");
        scanner = new Scanner(System.in);
        greedy = (scanner.nextInt() == 1);

        boolean heurisitcaCoste;
        System.out.print("¿Que funcion heuristica quieres usar? [1 para solo coste / 0 para coste + felicidad]: ");
        scanner = new Scanner(System.in);
        heurisitcaCoste = (scanner.nextInt() == 1);

        double a = 0.1, b = 0.2;
        if (!heurisitcaCoste) {
            System.out.println("A continuacion introduce los parametros A (pondera el coste) y B (pondera la felicidad) de la heuristica");
            System.out.print("Ponderacion A [0.1 por defecto]: ");
            scanner = new Scanner(System.in);
            a = scanner.nextDouble();
            System.out.print("Ponderacion B [0.2 por defecto]: ");
            scanner = new Scanner(System.in);
            b = scanner.nextDouble();
        }

        long ini_time, end_time;
        ini_time = System.nanoTime();

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
        }
        else {
            paquetes.sort(new Comparator<Paquete>() {
                @Override
                public int compare(Paquete p1, Paquete p2) {
                    return p1.getPrioridad() - p2.getPrioridad();
                }
            });
        }

        Estado inicial = new Estado(greedy);
        inicial.imprimir_tabla_2();

        if (hillClimb) {
            if (heurisitcaCoste) azamonHillClimbingSearch(inicial);
            else azamonHillClimbingSearch(inicial, a, b);
        }
        else {
            if (heurisitcaCoste) azamonSimulatedAnnealingSearch(inicial, steps, stiter, k, lambda);
            else azamonSimulatedAnnealingSearch(inicial, steps, stiter, k, lambda, a, b);
        }


        end_time = System.nanoTime();
        int duracion = (int)(end_time-ini_time)/1000000;
        System.out.println("Duracion del algoritmo: " + duracion + " ms ");
    }


    private static void azamonHillClimbingSearch(Estado estado) {
        System.out.println("\nAzamon Hill Climbing  -->");
        try {
            AzamonSuccessorFunction successorFunction = new AzamonSuccessorFunction();
            AzamonGoalTest goalTest = new AzamonGoalTest();
            AzamonHeuristicFunction1 heuristicFunction = new AzamonHeuristicFunction1();
            Problem problem =  new Problem(estado, successorFunction, goalTest, heuristicFunction);
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());

            Estado solucion = (Estado) search.getGoalState();

            System.out.println("Valores de la solucion final: (" + solucion.getCosteEU() + " €, " + solucion.getFelicidad() + " feliz)");
            solucion.imprimir_tabla_2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void azamonHillClimbingSearch(Estado estado, double a, double b) {
        System.out.println("\nAzamon Hill Climbing  -->");
        try {
            AzamonSuccessorFunction successorFunction = new AzamonSuccessorFunction();
            AzamonGoalTest goalTest = new AzamonGoalTest();
            AzamonHeuristicFunction2 heuristicFunction = new AzamonHeuristicFunction2(a, b);
            Problem problem =  new Problem(estado, successorFunction, goalTest, heuristicFunction);
            Search search =  new HillClimbingSearch();
            SearchAgent agent = new SearchAgent(problem,search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());

            Estado solucion = (Estado) search.getGoalState();

            System.out.println("Valores de la solucion final: (" + solucion.getCosteEU() + " €, " + solucion.getFelicidad() + " feliz)");
            solucion.imprimir_tabla_2();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void azamonSimulatedAnnealingSearch(Estado estado, int steps, int stiter, int k, double lambda) {
        System.out.println("\nAzamon Simulated Annealing  -->");
        try {
            AzamonSuccessorFunctionSA successorFunction = new AzamonSuccessorFunctionSA();
            AzamonGoalTest goalTest = new AzamonGoalTest();
            AzamonHeuristicFunction1 heuristicFunction = new AzamonHeuristicFunction1();
            Problem problem =  new Problem(estado, successorFunction, goalTest, heuristicFunction);
            Search search = new SimulatedAnnealingSearch(steps, stiter, k, lambda);
            SearchAgent agent = new SearchAgent(problem, search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());

            Estado solucion = (Estado) search.getGoalState();

            System.out.println("Valores de la solucion final: (" + solucion.getCosteEU() + " €, " + solucion.getFelicidad() + " feliz)");
            solucion.imprimir_tabla_2();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void azamonSimulatedAnnealingSearch(Estado estado, int steps, int stiter, int k, double lambda, double a, double b) {
        System.out.println("\nAzamon Simulated Annealing  -->");
        try {
            AzamonSuccessorFunctionSA successorFunction = new AzamonSuccessorFunctionSA();
            AzamonGoalTest goalTest = new AzamonGoalTest();
            AzamonHeuristicFunction2 heuristicFunction = new AzamonHeuristicFunction2(a, b);
            Problem problem =  new Problem(estado, successorFunction, goalTest, heuristicFunction);
            Search search = new SimulatedAnnealingSearch(steps, stiter, k, lambda);
            SearchAgent agent = new SearchAgent(problem, search);

            System.out.println();
            printActions(agent.getActions());
            printInstrumentation(agent.getInstrumentation());

            Estado solucion = (Estado) search.getGoalState();

            System.out.println("Valores de la solucion final: (" + solucion.getCosteEU() + " €, " + solucion.getFelicidad() + " feliz)");
            solucion.imprimir_tabla_2();

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
        System.out.println(actions.toString());
    }
}
