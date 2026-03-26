package org.example;

import java.io.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Main {
    public static void main(String[] args) {
        String testFolder = args[0];
        String inputFile = "src/main/resources/" + testFolder + "/input.in";

        try {
            Platform platform = new Platform(testFolder);
            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    String[] parts = line.split(" \\| ");
                    String timestamp = parts[0];
                    String command = parts[1];

                    DateTimeFormatter form = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
                    LocalDateTime dateTime = LocalDateTime.parse(timestamp, form);

                    if (command.equals("create_exam")) {

                        String exam = parts[2];
                        LocalDateTime startDate = LocalDateTime.parse(parts[3], form);
                        LocalDateTime endDate = LocalDateTime.parse(parts[4], form);
                        platform.createExam(exam, startDate, endDate);

                    } else if (command.equals("add_question")) {

                        String questionType = parts[2];
                        String examName = parts[3];
                        String author = parts[4];
                        int difficulty = Integer.parseInt(parts[5]);
                        double maxScore = Double.parseDouble(parts[6]);
                        String text = parts[7];
                        String correctAnswer = parts[8];

                        platform.addQuestion(dateTime, questionType, examName, author, difficulty,
                                maxScore, text, correctAnswer);

                    } else if (command.equals("list_questions_history")) {

                        String examName = parts[2];
                        platform.listQuestionsHistory(examName, dateTime);

                    } else if (command.equals("print_exam")) {

                        String examName = parts[2];
                        platform.printExam(examName, dateTime);

                    } else if (command.equals("register_student")) {

                        String studentName = parts[2];
                        String group = parts[3];
                        platform.registerStudent(studentName, group);

                    } else if (command.equals("submit_exam")) {

                        String examName = parts[2];
                        String studentName = parts[3];
                        List<String> answers = new ArrayList<>();
                        for (int i = 4; i < parts.length; i++) {
                            answers.add(parts[i]);
                        }
                        platform.submitExam(dateTime, examName, studentName, answers);

                    } else if (command.equals("show_student_score")) {

                        String studentName = parts[2];
                        String examName = parts[3];
                        platform.showStudentScore(dateTime, studentName, examName);

                    } else if (command.equals("generate_report")) {

                        String examName = parts[2];
                        platform.generateReport(examName, dateTime);

                    } else if (command.equals("exit")) {

                        reader.close();
                        platform.close();
                        return;
                    }
                }
            }

            reader.close();
            platform.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}