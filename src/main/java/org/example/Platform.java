package org.example;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Platform {
    private Map<String, Exam> exams;
    private Map<String, Student> students;
    private String testFolder;
    private FileWriter writer;

    public Platform(String testFolder) throws IOException {
        this.exams = new HashMap<>();
        this.students = new HashMap<>();
        this.testFolder = "src/main/resources/" + testFolder;
        this.writer = new FileWriter(this.testFolder + "/console.out");
    }

    public void createExam(String examName, LocalDateTime startDate, LocalDateTime endDate) {
        Exam exam = new Exam(examName, startDate, endDate);
        exams.put(examName, exam);
    }

    public void addQuestion(LocalDateTime timestamp, String questionType, String examName, String author,
                            int difficulty, double maxScore, String text, String correctAnswer) {
        Exam exam = exams.get(examName);

        Question question;
        if ("open_ended".equals(questionType)) {
            question = new OpenEndedQuestion(text, author, timestamp, difficulty, maxScore, correctAnswer);
        } else if ("multiple_choice".equals(questionType)) {
            ResponseOption option = ResponseOption.valueOf(correctAnswer);
            question = new MultipleChoiceQuestion(text, author, timestamp, difficulty, maxScore, option);
        } else if ("fill_in_the_blank".equals(questionType)) {
            question = new FillInTheBlankQuestion(text, author, timestamp, difficulty, maxScore, correctAnswer);
        } else {
            return;
        }

        exam.addQuestion(question);
    }

    public void listQuestionsHistory(String examName, LocalDateTime timestamp) throws IOException {
        Exam exam = exams.get(examName);

        if (exam != null) {
            exam.listQuestionsHistory(testFolder, timestamp);
        }
    }

    public void printExam(String examName, LocalDateTime timestamp) throws IOException {
        Exam exam = exams.get(examName);

        if (exam != null) {
            exam.printExam(testFolder, timestamp);
        }
    }

    public void registerStudent(String studentName, String group) {
        Student student = new Student(studentName, group);
        students.put(studentName, student);
    }

    public void submitExam(LocalDateTime timestamp, String examName, String studentName, List<String> answers)
            throws IOException {
        Exam exam = exams.get(examName);
        Student student = students.get(studentName);

        try {
            exam.submitExam(studentName, answers, timestamp);
            student.addExamScore(examName, exam.getStudentScore(studentName));
        } catch (SubmissionOutsideTimeIntervalException e) {
            writer.write(e.toString() + "\n");
            writer.flush();
            student.addExamScore(examName, 0.0);
        }
    }

    public void showStudentScore(LocalDateTime timestamp, String studentName, String examName) throws IOException {
        Student student = students.get(studentName);
        if (student != null) {
            double score = student.getExamScore(examName);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
            writer.write(timestamp.format(formatter) + " | The score of " + studentName + " for exam " +
                    examName + " is " + String.format("%.2f", score) + "\n");
            writer.flush();
        }
    }

    public void generateReport(String examName, LocalDateTime timestamp) throws IOException {
        Exam exam = exams.get(examName);
        if (exam != null) {
            exam.generateReport(testFolder, timestamp);
        }
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }
}
