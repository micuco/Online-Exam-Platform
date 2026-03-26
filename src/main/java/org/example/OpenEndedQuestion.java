package org.example;

import java.time.LocalDateTime;

public class OpenEndedQuestion extends Question {
    private String correctAnswer;

    public OpenEndedQuestion(String text, String author, LocalDateTime date, int difficulty,
                             double maxScore, String correctAnswer) {
        super(text, author, date, difficulty, maxScore);
        this.correctAnswer = correctAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public <T> Correctness checkAnswer(T answer) {
        String studentAnswer = (String) answer;

        int studentLength = studentAnswer.length();
        int correctLength = correctAnswer.length();

        int upperBound = (int) (correctLength + correctLength * 0.3);
        int lowerBound = (int) (correctLength - correctLength * 0.3);

        if (studentAnswer.equals(correctAnswer)) {
            return Correctness.CORRECT;
        }

        if (studentLength <= upperBound && studentLength >= lowerBound) {
            if (studentAnswer.contains(correctAnswer) || correctAnswer.contains(studentAnswer)) {
                return Correctness.PARTIALLY_CORRECT;
            }
        }

        return Correctness.INCORRECT;
    }

    @Override
    public double getPercentage(Correctness correctness) {
        if (correctness == Correctness.CORRECT) {
            return 1.0;
        }

        if (correctness == Correctness.PARTIALLY_CORRECT) {
            return 0.7;
        }

        return 0.0;
    }
}
