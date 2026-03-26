package org.example;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Exam {
    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<Question> questions;
    private Map<String, Double> studentScores;

    public Exam(String name, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.questions = new ArrayList<>();
        this.studentScores = new TreeMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public void listQuestionsHistory(String testFolder, LocalDateTime timestamp) throws IOException {
        DateTimeFormatter form = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
        String filename = testFolder + "/questions_history_" + name + "_" + timestamp.format(form) + ".out";
        List<Question> sortedQuestions = new ArrayList<>(questions);

        sortedQuestions.sort((q1, q2) -> {
            int comp = q1.getDate().compareTo(q2.getDate());

            if (comp == 0) {
                return q1.getAuthor().compareTo(q2.getAuthor());
            }

            return comp;
        });

        FileWriter writer = new FileWriter(filename);
        String correctAnswer;

        for (Question q : sortedQuestions) {
            if (q instanceof OpenEndedQuestion) {
                correctAnswer = ((OpenEndedQuestion) q).getCorrectAnswer();
            } else if (q instanceof MultipleChoiceQuestion) {
                correctAnswer = ((MultipleChoiceQuestion) q).getCorrectAnswer().toString();
            } else {
                correctAnswer = ((FillInTheBlankQuestion) q).getCorrectAnswer();
            }

            writer.write(q.getDate().format(form) + " | " + q.getText() + " | " + correctAnswer +
                    " | " + q.getDifficulty() + " | " + q.getMaxScore() + " | " + q.getAuthor() + "\n");
        }

        writer.close();
    }

    public List<Question> getSortedQuestionsForPrint() {
        List<Question> sorted = new ArrayList<>(questions);
        sorted.sort((q1, q2) -> {
            int comp = Integer.compare(q1.getDifficulty(), q2.getDifficulty());

            if (comp == 0) {
                return q1.getText().compareTo(q2.getText());
            }

            return comp;
        });

        return sorted;
    }

    public void printExam(String testFolder, LocalDateTime timestamp) throws IOException {
        DateTimeFormatter form = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
        String filename = testFolder + "/print_exam_" + name + "_" + timestamp.format(form) + ".out";
        List<Question> sorted = getSortedQuestionsForPrint();

        FileWriter writer = new FileWriter(filename);
        String correctAnswer;

        for (Question q : sorted) {
            if (q instanceof OpenEndedQuestion) {
                correctAnswer = ((OpenEndedQuestion) q).getCorrectAnswer();
            } else if (q instanceof MultipleChoiceQuestion) {
                correctAnswer = ((MultipleChoiceQuestion) q).getCorrectAnswer().toString();
            } else {
                correctAnswer = ((FillInTheBlankQuestion) q).getCorrectAnswer();
            }

            writer.write(q.getMaxScore() + " | " + q.getText() + " | " + q.getDifficulty() + " | " +
                    correctAnswer + "\n");
        }

        writer.close();
    }

    public void submitExam(String studentName, List<String> answers, LocalDateTime timestamp)
            throws SubmissionOutsideTimeIntervalException {
        List<Question> sortedQuestions = getSortedQuestionsForPrint();
        double totalScore = 0.0;
        Correctness correctness;

        if (timestamp.isBefore(startDate) || timestamp.isAfter(endDate)) {
            throw new SubmissionOutsideTimeIntervalException(timestamp, studentName);
        }

        for (int i = 0; i < answers.size() && i < sortedQuestions.size(); i++) {
            Question q = sortedQuestions.get(i);
            String answer = answers.get(i);

            if (q instanceof MultipleChoiceQuestion) {
                correctness = q.checkAnswer(ResponseOption.valueOf(answer));
            } else {
                correctness = q.checkAnswer(answer);
            }

            totalScore += q.getMaxScore() * q.getPercentage(correctness);
        }

        studentScores.put(studentName, totalScore);
    }

    public double getStudentScore(String studentName) {
        return studentScores.getOrDefault(studentName, 0.0);
    }

    public void generateReport(String testFolder, LocalDateTime timestamp) throws IOException {
        DateTimeFormatter form = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
        String filename = testFolder + "/exam_report_" + name + "_" + timestamp.format(form) + ".out";

        List<Map.Entry<String, Double>> sortedScores = new ArrayList<>(studentScores.entrySet());
        sortedScores.sort(Map.Entry.comparingByKey());

        FileWriter writer = new FileWriter(filename);

        for (Map.Entry<String, Double> r : sortedScores) {
            writer.write(r.getKey() + " | " + String.format("%.2f", r.getValue()) + "\n");
        }

        writer.close();
    }
}
