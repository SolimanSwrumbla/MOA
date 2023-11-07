package com.soliman;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class Costs {
    private final double[] costs;

    // Costruttore che accetta una sequenza di costi
    public Costs(double... costs) {
        this.costs = costs.clone();
    }

    // Verifica se i costi sono maggiori o uguali rispetto a un altro oggetto Costs
    public boolean isGreaterOrEqualThan(Costs other) {
        if (costs.length != other.costs.length)
            return false;
        for (int i = 0; i < costs.length; i++) {
            if (costs[i] < other.costs[i]) {
                return false;
            }
        }
        return true;
    }

    // Verifica se i costi sono strettamente maggiori rispetto a un altro oggetto Costs
    public boolean isGreaterThan(Costs other) {
        return isGreaterOrEqualThan(other) && !equals(other);
    }

    // Verifica se i costi sono minori o uguali rispetto a un altro oggetto Costs
    public boolean isLessOrEqualThan(Costs other) {
        if (costs.length != other.costs.length)
            return false;
        for (int i = 0; i < costs.length; i++) {
            if (costs[i] > other.costs[i]) {
                return false;
            }
        }
        return true;
    }

    // Verifica se i costi sono strettamente minori rispetto a un altro oggetto Costs
    public boolean isLessThan(Costs other) {
        return isLessOrEqualThan(other) && !equals(other);
    }

    // Somma i costi con un altro oggetto Costs
    public Costs add(Costs other) {
        if (costs.length != other.costs.length) {
            throw new IllegalArgumentException(
                    "Impossibile sommare costi di lunghezza differente" + " " + this + " " + other);
        }
        double[] newCosts = new double[costs.length];
        for (int i = 0; i < costs.length; i++) {
            newCosts[i] = costs[i] + other.costs[i];
        }
        return new Costs(newCosts);
    }

    // Calcola la somma totale dei costi
    public double sum() {
        double total = 0.0;
        for (double cost : costs) {
            total += cost;
        }
        return total;
    }

    @Override
    // Verifica l'uguaglianza con un altro oggetto
    public boolean equals(Object obj) {
        if (!(obj instanceof Costs other))
            return false;
        return Arrays.equals(costs, other.costs);
    }

    @Override
    // Calcola l'hash code basato sugli array di costi
    public int hashCode() {
        return Objects.hash(costs);
    }

    @Override
    // Converte l'oggetto in una rappresentazione testuale
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for (int i = 0; i < costs.length; i++) {
            sb.append(String.format(Locale.US, "%.2f", costs[i]));
            if (i < costs.length - 1) {
                sb.append(" ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}