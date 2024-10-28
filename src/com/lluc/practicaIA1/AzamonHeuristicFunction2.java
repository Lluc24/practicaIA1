package com.lluc.practicaIA1;

import aima.search.framework.HeuristicFunction;

public class AzamonHeuristicFunction2 implements HeuristicFunction{
    double a, b;

    public AzamonHeuristicFunction2(double a, double b) {
        this.a = a;
        this.b = b;
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA " + a);
        System.out.println("BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB " + b);
    }

    public double getHeuristicValue(Object o) {
        Estado estado = (Estado) o;
        System.out.println("a " + a + " b " + b + " coste " + estado.getCosteEU() + " feliz " + estado.getFelicidad() + " AC " + a*estado.getCoste() + " BF " + b* estado.getFelicidad());
        return a*estado.getCoste() + b*estado.getFelicidad();
    }
}