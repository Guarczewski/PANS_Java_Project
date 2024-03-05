package org.example.Custom_Objects;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QuizQuestion {
    public String Question, Answer_0, Answer_1, Answer_2, CorrectAnswer;
    public QuizQuestion(String question, String answer_0, String answer_1, String answer_2, String correctAnswer) {
        this.Question = question;
        this.Answer_0 = answer_0;
        this.Answer_1 = answer_1;
        this.Answer_2 = answer_2;
        this.CorrectAnswer = correctAnswer;
    }
    public static List<QuizQuestion> ReadQuizQuestions() {
        List<QuizQuestion> list = new ArrayList<>();
        String[] temp;
        try {
            File file = new File("Questions.txt");
            Scanner scanner = new Scanner(new FileInputStream(file.getAbsolutePath()), StandardCharsets.UTF_8);

            while(scanner.hasNextLine()){
                temp = scanner.nextLine().split("-");
                list.add(new QuizQuestion(temp[0], temp[1], temp[2],temp[3], temp[4]));
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return list;
    }
}
