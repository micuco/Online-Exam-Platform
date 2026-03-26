package org.example;

import java.time.LocalDateTime;

public abstract class Question implements Gradable {
    private String text;
    private String author;
    private LocalDateTime date;
    private int difficulty;
    private double maxScore;

    public Question(String text, String author, LocalDateTime date, int difficulty, double maxScore) {
        this.text = text;
        this.author = author;
        this.date = date;
        this.difficulty = difficulty;
        this.maxScore = maxScore;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(double maxScore) {
        this.maxScore = maxScore;
    }

    public abstract <T> Correctness checkAnswer(T answer);
}
