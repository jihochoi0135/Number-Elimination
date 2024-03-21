import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class leaderboardClassic extends JPanel {
    private List<ScoreRecord> records;

    public leaderboardClassic() {
        this.records = readLeaderboard();
        Collections.sort(records, Comparator.comparing(ScoreRecord::getTime).thenComparing(ScoreRecord::getScore));
        setLayout(new GridLayout(records.size() + 1, 1));

        add(new JLabel("Leaderboard", SwingConstants.CENTER));
        for (int i = 0; i < records.size(); i++) {
            ScoreRecord record = records.get(i);
            JLabel label = new JLabel((i + 1) + ". Score: " + record.getScore() + ", Time: " + record.getTime() + " sec.", SwingConstants.LEFT);
            add(label);
        }
    }

    private List<ScoreRecord> readLeaderboard() {
        List<ScoreRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("leaderboardClassic.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    int score = Integer.parseInt(parts[0]);
                    int time = Integer.parseInt(parts[1]);
                    records.add(new ScoreRecord(score, time));
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

        public ScoreRecord(int score, int time) {
            this.score = score;
            this.time = time;
        }

        public int getScore() {
            return score;
        }

        public int getTime() {
            return time;
        }
    }

    public static void showLeaderboard() {
        JFrame frame = new JFrame("Leaderboard");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setLocationRelativeTo(null);

        leaderboardClassic leaderboardPanel = new leaderboardClassic();
        frame.add(leaderboardPanel);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        showLeaderboard();
    }
}
