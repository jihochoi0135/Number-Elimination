import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class leaderboardCustomized extends JPanel {
    private List<ScoreRecord> records;

    public leaderboardCustomized() {
        this.records = readLeaderboard();
        Collections.sort(records, Comparator.comparing(ScoreRecord::getTime).thenComparing(ScoreRecord::getScore));
        setLayout(new GridLayout(records.size() + 1, 1));

        add(new JLabel("Leaderboard", SwingConstants.CENTER));
        for (int i = 0; i < records.size(); i++) {
            ScoreRecord record = records.get(i);
            JLabel label = new JLabel((i + 1) + ". Score: " + record.getScore() + ", Time: " + record.getTime() + " sec., rows: " + record.getRows() + ", cols: " + record.getCols(), SwingConstants.LEFT);
            add(label);
        }
    }

    private List<ScoreRecord> readLeaderboard() {
        List<ScoreRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("leaderboardCustomized.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    int score = Integer.parseInt(parts[0]);
                    int time = Integer.parseInt(parts[1]);
                    int rows = Integer.parseInt(parts[2]);
                    int cols = Integer.parseInt(parts[3]);
                    records.add(new ScoreRecord(score, time, rows, cols));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    private static class ScoreRecord {
        private int score;
        private int time;
        private int rows;
        private int cols;

        public ScoreRecord(int score, int time, int rows, int cols) {
            this.score = score;
            this.time = time;
            this.rows = rows;
            this.cols = cols;
        }

        public int getScore() {
            return score;
        }

        public int getTime() {
            return time;
        }
        public int getRows(){return rows;}
        public int getCols(){return cols;}
    }

    public static void showLeaderboard() {
        JFrame frame = new JFrame("Leaderboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);

        leaderboardCustomized leaderboardPanel = new leaderboardCustomized();
        frame.add(leaderboardPanel);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        showLeaderboard();
    }
}
