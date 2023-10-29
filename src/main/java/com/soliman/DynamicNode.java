package com.soliman;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DynamicNode implements Node<String> {
    private String filename; // Rappresentazione del grafo tramite file
    private String value;
    private int costLength;
    private boolean directional;

    private DynamicNode(String filename, String value, int costLength, boolean directional) {
        this.filename = filename;
        this.value = value;
        this.costLength = costLength;
        this.directional = directional;
    }

    // Creazione di un'istanza di DynamicNode da un file di input
    public static DynamicNode detectCostLength(String filename, String value, boolean directional) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            // Leggi la prima riga del file per determinare la lunghezza dei costi
            String line = reader.readLine().split("\\s+\\|\\s+")[1];
            String[] costs = line.replace(",", ".").replace("(", "").replace(")", "").trim().split("\\s+");
            return new DynamicNode(filename, value, costs.length, directional);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Restituisce i successori del nodo corrente
    @Override
    public Iterable<Child<String>> successors() {
        List<Child<String>> successors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");
                if (parts.length == 3) {
                    // Verifica se il nodo corrente è il nodo di partenza
                    if (parts[0].trim().equalsIgnoreCase(value)) {
                        String[] costs = parts[1].replace(",", ".").replace("(", "")
                                .replace(")", "").trim()
                                .split("\\s+");
                        try {
                            // Parsa i costi in un array di double
                            double[] convertedCosts = Arrays.stream(costs).mapToDouble(Double::parseDouble).toArray();
                            String target = parts[2].toUpperCase().trim();
                            if (target.matches(".*[,].*")) {
                                System.err.println("\nErrore: Formato nodo invalido : " + line);
                            }
                            // Aggiunge il nodo di destinazione come figlio con i costi appropriati
                            successors.add(new Child<>(new DynamicNode(filename, target, costLength, directional),
                                    new Costs(convertedCosts)));
                        } catch (NumberFormatException e) {
                            System.err.println("\nErrore: Formato del costo invalido : " + line);
                        }
                    }
                    // Se il grafo non è direzionale, verifica anche i collegamenti in direzione opposta
                    if (!directional && parts[2].trim().equalsIgnoreCase(value)) {
                        String[] costs = parts[1].replace(",", ".").replace("(", "")
                                .replace(")", "").trim()
                                .split("\\s+");
                        try {
                            double[] convertedCosts = Arrays.stream(costs).mapToDouble(Double::parseDouble).toArray();
                            String target = parts[0].toUpperCase().trim();
                            if (target.matches(".*[,].*")) {
                                System.err.println("\nErrore: Formato nodo invalido : " + line);
                            }
                            // Aggiunge il nodo di destinazione come figlio con i costi appropriati
                            successors.add(new Child<>(new DynamicNode(filename, target, costLength, directional),
                                    new Costs(convertedCosts)));
                        } catch (NumberFormatException e) {
                            System.err.println("\nErrore: Formato del costo invalido : " + line);
                        }
                    }
                }
            }
            return successors;
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
        if (!(obj instanceof DynamicNode node))
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
