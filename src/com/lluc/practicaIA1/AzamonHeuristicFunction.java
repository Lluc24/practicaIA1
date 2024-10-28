package com.lluc.practicaIA1;

import aima.search.framework.HeuristicFunction;

public class AzamonHeuristicFunction implements HeuristicFunction{
    public double getHeuristicValue(Object o) {
        Estado estado = (Estado) o;
        return estado.getHeuristica();
    }
}
