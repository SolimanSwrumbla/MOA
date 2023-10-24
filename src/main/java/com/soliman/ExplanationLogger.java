package com.soliman;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ExplanationLogger implements Logger<String> {
    @Override
    public void log(int k, Set<Node<String>> open, Map<Node<String>, Set<Path<String>>> paths,
            Predicate<Node<String>> endNodes, Node<String> prescelto, Set<Node<String>> ND, Set<Node<String>> closed,
            Map<Node<String>, Set<Path<String>>> solutionCosts) {

        int i = 0;

        for (var node : open) {
            if ((open.size() % 2 == 0 && i == open.size() / 2 - 1) || (open.size() % 2 != 0 && i == open.size() / 2)) {
                System.out.format("%3d", k);
            } else {
                System.out.print("   ");
            }
            
            
            var NewG = String.join("", paths.get(node).stream().map(p -> p.cost().toString()).toList());
            var NewF = String.join("", Moa.selectionFunction(node, paths, endNodes).stream()
                    .map(p -> p.cost().toString()).collect(Collectors.toSet()));

            if (node.equals(prescelto))
                System.out.format(" | %4s** %40s %40s", node, NewG, NewF);
            else if (ND.contains(node))
                System.out.format(" | %4s*  %40s %40s", node, NewG, NewF);
            else
                System.out.format(" | %4s   %40s %40s", node, NewG, NewF);
            if ((open.size() % 2 == 0 && i == open.size() / 2 - 1) || (open.size() % 2 != 0 && i == open.size() / 2))
                System.out.format("%40s %60s", closed, solutionCosts);
            System.out.println();
            i++;
        }
        System.out.println();

    }
}
