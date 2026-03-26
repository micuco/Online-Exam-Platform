package org.example;

import java.util.*;

public class Student {
    private String name;
    private String group;
    private Map<String, Double> examScores;

    public Student(String name, String group) {
        this.name = name;
        this.group = group;
        this.examScores = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Map<String, Double> getExamScores() {
        return examScores;
    }

    public void addExamScore(String examName, double score) {
        examScores.put(examName, score);
    }

    public double getExamScore(String examName) {
        return examScores.getOrDefault(examName, 0.0);
    }
}
