package controller;

import annotations.Bind;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    private String modelName;
    private Map<String, Object> variables;
    private int LL;

    public Controller(String modelName) {
        this.modelName = modelName;
        this.variables = new LinkedHashMap<>();
    }

    public Controller readDataFrom(String fileName) {
        List<String> lines;
        try {
            lines = Files.readAllLines(Paths.get(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if(!lines.get(0).startsWith("LATA")){
            throw new IllegalArgumentException("Missing 'LATA' line in the data file.");
        }

        for (int i = 0 ; i < lines.size(); i++) {
            String[] parts = lines.get(i).trim().split("\\s+");
            if (parts.length <= 1) {
                throw new IllegalArgumentException("Missing values for a variable.");
            }

            String variableName = parts[0];

            int numValues = parts.length - 1;
            if(i == 0){
                LL = numValues;
                int[] values = new int [LL];
                for (int j = 1; j <= LL; j++) {
                    values[j - 1] = Integer.parseInt(parts[j]);
                }
                variables.put(variableName, values);
            }else {
                double[] values = new double[LL];
                for (int j = 1; j <= LL; j++) {
                    if (j > numValues) {
                        values[j - 1] = Double.parseDouble(parts[numValues]);
                    } else values[j - 1] = Double.parseDouble(parts[j]);
                }
                variables.put(variableName, values);
            }
        }
        return this;
    }

    public Controller runModel() {
        try {
            Class<?> modelClass = Class.forName(modelName);
            Object modelInstance = modelClass.getDeclaredConstructor().newInstance();

            for (java.lang.reflect.Field field : modelClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Bind.class)) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    if (variables.containsKey(fieldName)) {
                        field.set(modelInstance, variables.get(fieldName));
                    }
                }
            }

            java.lang.reflect.Field llField = modelClass.getDeclaredField("LL");
            llField.setAccessible(true);
            llField.set(modelInstance, LL);

            modelClass.getDeclaredMethod("run").invoke(modelInstance);

            for (java.lang.reflect.Field field : modelClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Bind.class)) {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    variables.put(fieldName, field.get(modelInstance));
                }
            }
            variables.remove("LL");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }


    public Controller runScriptFromFile(String fileName) {
        String script = null;
        try {
            script = new String(Files.readAllBytes(Paths.get(fileName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return runScript(script);
    }

    public Controller runScript(String script) {
        Binding binding = new Binding();
        binding.setVariable("LL", LL);
        variables.forEach((key, value) -> binding.setVariable((String) key, value));
        String name = script.split(" ")[0];
        double[] value = new double[LL];

        StringBuilder regex = new StringBuilder("\\b(");
        variables.keySet().forEach((key) -> regex.append(key).append("|"));
        regex.deleteCharAt(regex.length() - 1).append(")\\b(?!\\[)");

        Pattern pattern = Pattern.compile(regex.toString());
        Matcher matcher = pattern.matcher(script);

        ArrayList<String> names = new ArrayList<>();
        while (matcher.find()) {
            names.add(matcher.group(1));
        }
        for (int i = 0; i < LL; i++) {
            String modifiedScript = script;
            for (String n : names) {
                modifiedScript = modifiedScript.replaceAll("\\b" + n + "\\b(?!\\[)", n + "[" + i + "]");
            }

            GroovyShell shell = new GroovyShell(binding);
            shell.evaluate(modifiedScript);

            value[i] = (double) binding.getVariable(name);
        }
        variables.remove("LL");
        variables.put(name, value);

        return this;
    }

    public String getResultsAsTsv() {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, Object> entry : variables.entrySet()) {
            String variableName = entry.getKey();
            Object value = entry.getValue();
                result.append(variableName).append("\t");

            if (value instanceof double[]) {
                double[] values = (double[]) value;
                for (double v : values) {
                    result.append(v).append("\t");
                }
            } else if (value instanceof int[]) {
                int[] values = (int[]) value;
                for (int v : values) {
                    result.append(v).append("\t");
                }
            } else {
                result.append(value).append("\t");
            }
            result.append("\n");
        }

        return result.toString();
    }
}
