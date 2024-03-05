package org.example.Custom_Frames;

import org.example.Config.CONFIG;
import org.example.Custom_Objects.QuizQuestion;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.List;
import java.util.Random;

public class CQuizFrame extends JFrame {
    public boolean active = false;
    static List<QuizQuestion> quizQuestionList;
    public static QuizQuestion selectedQuestion;
    public JRadioButton[] jRadioButtons;
    public JButton submitButton;
    JLabel questionLabel;
    ImageIcon backgroundImage = null;
    public CQuizFrame(){
        super("Quiz Ostatniej Szansy!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(500,500, CONFIG.QUIZ_WINDOW_WIDTH,CONFIG.QUIZ_WINDOW_HEIGHT);

        try {
            backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Package_Images/Logo-PANS.png")));
            Image tempImage = backgroundImage.getImage().getScaledInstance(420, 80,  Image.SCALE_SMOOTH);
            backgroundImage = new ImageIcon(tempImage);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }

        JPanel cPanel = new JPanel();
        cPanel.setLayout(new BorderLayout());

        JPanel jPanel = new JPanel(new GridLayout(7,0));
        questionLabel = new JLabel();

        jPanel.add(new JLabel("QUIZ OSTATNIEJ SZANSY!"));
        jPanel.add(new JLabel("Jeśli prawidłowo odpowiesz na wylosowane pytanie Twoja postać wróci do gry!"));
        jPanel.add(new JLabel("Pytanie: "));
        jPanel.add(questionLabel);

        jRadioButtons = new JRadioButton[3];
        ButtonGroup buttonGroup = new ButtonGroup();
        for (int i = 0; i < jRadioButtons.length; i++) {
            jRadioButtons[i] = new JRadioButton();
            buttonGroup.add(jRadioButtons[i]);
            jPanel.add(jRadioButtons[i]);
        }

        submitButton = new JButton("Wyślij");

        cPanel.add(new JLabel(backgroundImage),BorderLayout.NORTH);
        cPanel.add(jPanel,BorderLayout.CENTER);
        cPanel.add(submitButton,BorderLayout.SOUTH);

        GetQuestions();
        SelectQuestion();
        UpdateQuiz();

        setContentPane(cPanel);
        setVisible(false);
    }
    public static void GetQuestions(){
        quizQuestionList = QuizQuestion.ReadQuizQuestions();
        Random random = new Random();
        selectedQuestion = quizQuestionList.get(random.nextInt(quizQuestionList.size()));
    }
    public void SelectQuestion(){
        Random random = new Random();
        do {
            selectedQuestion = quizQuestionList.get(random.nextInt(quizQuestionList.size()));
        }
        while (selectedQuestion == quizQuestionList.get(random.nextInt(quizQuestionList.size())));
        UpdateQuiz();
    }
    public void UpdateQuiz(){
        questionLabel.setText(selectedQuestion.Question);
        jRadioButtons[0].setText(selectedQuestion.Answer_0);
        jRadioButtons[1].setText(selectedQuestion.Answer_1);
        jRadioButtons[2].setText(selectedQuestion.Answer_2);
    }
}
