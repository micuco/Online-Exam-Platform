package org.example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubmissionOutsideTimeIntervalException extends Exception {
    private LocalDateTime timestamp;
    private String studentName;

    public SubmissionOutsideTimeIntervalException(LocalDateTime timestamp, String studentName) {
        this.timestamp = timestamp;
        this.studentName = studentName;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy-HH-mm");
        return timestamp.format(formatter) + " | Submission outside of time interval for student " + studentName;
    }
}
