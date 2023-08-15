package com.soliman;

import java.util.Map;
import java.util.Set;

public interface HeuristicFunction<Node, Cost extends PartialOrder<Cost>> {
    Node apply(Set<Node> ND, Map<Node, Set<Path<Node, Cost>>> label);
}
