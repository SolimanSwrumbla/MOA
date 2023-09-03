package com.soliman;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;

public class ExplainationLogger implements Logger<String> {

    @Override
    public void log(Set<String> open, Set<String> ND, Set<String> closed, Map<String, Set<Path<String>>> solutionCosts,
            Map<String, Set<Path<String>>> label, DefaultDirectedWeightedGraph<String, LabeledEdge<Costs>> graph,
            int k, String prescelto, Set<String> endNodes, Map<String, Set<Path<String>>> paths) {

        int i = 0;
        
        for (String node : open) {
            if (i == open.size() / 2)
                System.out.format("%3d", k);
            else
                System.out.format("   ");
            var NewG = String.join("", label.get(node).stream().map(p -> p.cost().toString()).toList());
            var NewF = String.join("", Moa.selectionFunction(graph, node, paths, endNodes).stream().map(p -> p.cost().toString()).collect(Collectors.toSet()));
            if(node.equals(prescelto))
                System.out.format(" | %4s** %40s %40s", node, NewG, NewF);
            else if (ND.contains(node))
                System.out.format(" | %4s*  %40s %40s", node, NewG, NewF);
            else
                System.out.format(" | %4s   %40s %40s", node, NewG, NewF);
            if (i == open.size() / 2)
                System.out.format("%40s %60s", closed, solutionCosts);
            System.out.println();
            i++;
        }
        System.out.println();
    }
}
