package com.soliman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomNode implements Node<String> {
    private String value;
    private String filename;
    private int costLength;
    private boolean directional;

    public static CustomNode detectCostLength(String value, String filename, boolean directional) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine().split("\\s+\\|\\s+")[1];
            String[] costs = line.trim().replace(",", ".").replace("(", "").replace(")", "").split("\\s+");
            return new CustomNode(value, filename, costs.length, directional);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public CustomNode(String value, String filename, int costLength, boolean directional) {
        this.value = value;
        this.filename = filename;
        this.costLength = costLength;
        this.directional = directional;
    }

    @Override
    public Iterable<Child<String>> successors() {
        List<Child<String>> children = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    if (parts[0].trim().equalsIgnoreCase(value)) {
                        String[] costs = parts[1].trim().replace(",", ".").replace("(", "")
                                .replace(")", "")
                                .split("\\s+");
                        try {
                            double[] convertedCosts = Arrays.stream(costs).mapToDouble(Double::parseDouble).toArray();
                            String target = parts[2].toUpperCase().trim();
                            if (target.matches(".*[,].*")) {
                                System.err.println("\nErrore: Formato nodo invalido : " + line);
                            }
                            children.add(new Child<>(new CustomNode(target, filename, costLength, directional),
                                    new Costs(convertedCosts)));
                        } catch (NumberFormatException e) {
                            System.err.println("\nErrore: Formato del costo invalido : " + line);
                        }
                    }
                    if (directional&&parts[1].trim().equalsIgnoreCase(value)) {
                        String[] costs = parts[1].trim().replace(",", ".").replace("(", "")
                                .replace(")", "")
                                .split("\\s+");
                        try {
                            double[] convertedCosts = Arrays.stream(costs).mapToDouble(Double::parseDouble).toArray();
                            String target = parts[0].toUpperCase().trim();
                            if (target.matches(".*[,].*")) {
                                System.err.println("\nErrore: Formato nodo invalido : " + line);
                            }
                            children.add(new Child<>(new CustomNode(target, filename, costLength, directional),
                                    new Costs(convertedCosts)));
                        } catch (NumberFormatException e) {
                            System.err.println("\nErrore: Formato del costo invalido : " + line);
                        }
                    }
                }
            }
            return children;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int costLength() {
        return costLength;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof CustomNode node))
            return false;
        return value.equals(node.value);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}