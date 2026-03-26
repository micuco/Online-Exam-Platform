package org.example;

import java.time.LocalDateTime;

public class MultipleChoiceQuestion extends Question {
    private ResponseOption correctAnswer;

    public MultipleChoiceQuestion(String text, String author, LocalDateTime date, int difficulty,
                                  double maxScore, ResponseOption correctAnswer) {
        super(text, author, date, difficulty, maxScore);
        this.correctAnswer = correctAnswer;
    }

    public ResponseOption getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(ResponseOption correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    @Override
    public <T> Correctness checkAnswer(T answer) {
        ResponseOption studentAnswer = (ResponseOption) answer;

        if (studentAnswer == correctAnswer) {
            return Correctness.CORRECT;
        }

        return Correctness.INCORRECT;
    }

    @Override
    public double getPercentage(Correctness correctness) {
        if (correctness == Correctness.CORRECT) {
            return 1.0;
        }

        return 0.0;
    }
}
