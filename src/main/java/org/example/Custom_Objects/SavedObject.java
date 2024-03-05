package org.example.Custom_Objects;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class SavedObject implements Comparable<SavedObject> {
    public String Name;
    public int Score;
    public SavedObject(String Name, int Score) {
        this.Name = Name;
        this.Score = Score;
    }
    @Override
    public int compareTo(SavedObject o) {
        return o.Score - Score;
    }

    public String toScoreboardString(int index){
        return (index + 1) + ". " + Name + " Score: " + Score;
    }

    public static void Save(String nick, int Score) {
        try {
            File file = new File("Scoreboard.txt");
            FileWriter fileWriter = new FileWriter(file,true);
            String output = nick + "-" + Score + "\n";
            fileWriter.write(output);
            fileWriter.close();
        }
        catch (Exception ignored) {

        }
    }

    public static List<SavedObject> Read() {
        List<SavedObject> list = new ArrayList<>();
        String[] temp;
        try {
            File file = new File("Scoreboard.txt");
            Scanner scanner = new Scanner(new FileInputStream(file.getAbsolutePath()), StandardCharsets.UTF_8);

            while(scanner.hasNextLine()){
                temp = scanner.nextLine().split("-");
                list.add(new SavedObject(temp[0], Integer.parseInt(temp[1])));
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        Collections.sort(list);
        return list;
    }

}
