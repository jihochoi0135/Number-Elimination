import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;


public class Classic_Game extends JFrame {

    /**
     * timeLimit = 120 seconds
     * labels: total numbers on the board
     * startPoint and endPoint is used to get a location of the cursor
     * labelsPanel: panel for grid and the number board.
     * layeredPane: is used for layering the pane and the panel to see all activity happenings on the game.
     * scoreLabel: label to display score.
     * score;
     * timer: using java swing timer to work on all types related to the time.
     * timerLabel: label to display the time remaining.
     * timerBar: the progress bar of the remaining time.
     * timeRecord: the record of time once the game is finished. (is used when the board is all clear).
     *
     */
    private int timeLimit = 120;
    private List<JLabel> labels = new ArrayList<>();
    private Point startPoint;
    private Point endPoint;
    private JPanel labelsPanel;
    private JLayeredPane layeredPane;
    private JLabel scoreLabel;
    private int score = 0;
    private Timer timer;
    private JLabel timerLabel;
    private JProgressBar timerBar;
    private int timeRecord = 0;
    private int targetSum = 10;
    private int rows = 10;
    private int cols = 20;

    /**
     * boolean that checks whether the board is clear or not.
     * @return true or false.
     */
    private boolean isBoardClear() {
        for (JLabel label : labels) {
            if (!label.getText().isEmpty()) {
                return false; // the situation that at least one label is not empty: return false represents board is not clear
            }
        }
        return true; // if all label is empty, board is clear; return true;
    }

    /**
     * Classic_Game function
     */
    public Classic_Game() {
        // basic setup
        super("Classic Game"); // Title
        setLayout(new BorderLayout());
        layeredPane = new JLayeredPane(); // initialize new layeredPane

        // scorePanel setup
        JPanel scorePanel = new JPanel(new BorderLayout());
        scoreLabel = new JLabel("Score: ");
        scorePanel.add(scoreLabel, BorderLayout.WEST); // display score top left corner
        JButton pauseButton = new JButton("Pause"); // pause button is on the scorePanel which will be on the top of the window
        scorePanel.add(pauseButton, BorderLayout.EAST); // place the pause button top right corner
        add(scorePanel, BorderLayout.NORTH); // place the panel on the top of the window

        // timer label setup; initialization, resets, etc.
        timerLabel = new JLabel("Time: " + timeLimit);
        add(timerLabel, BorderLayout.SOUTH); // place the timerLabel at the bottom of the window
        timerBar = new JProgressBar(); // timerBar
        timerBar.setMaximum(timeLimit); // maximum is timeLimit
        timerBar.setValue(timeLimit); // initial time starts from timeLimit for countdown
        timerBar.setStringPainted(true);
        add(timerBar, BorderLayout.SOUTH); // place the timerBar at the bottom of the window

        // labelsPanel setup; grid and random numbers on each label
        labelsPanel = new JPanel(new GridLayout(rows, cols));
        for (int i = 0; i < rows*cols; i++) { // check all labels and fill with the random numbers
            JLabel label = new JLabel(Integer.toString(new Random().nextInt((targetSum - 1) - 1 + 1) + 1), SwingConstants.CENTER); // random number range is 1 to targetSum - 1
            label.setOpaque(true);
            label.setBackground(Color.WHITE);
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // grid
            label.setFont(new Font("Arial", Font.BOLD, 16)); // font on the grid setup
            labelsPanel.add(label);
            labels.add(label);
        }
        layeredPane.add(labelsPanel, JLayeredPane.DEFAULT_LAYER);

        // drawPanel which shows the live action of the drag and select
        JPanel drawPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (startPoint != null && endPoint != null) {
                    g.setColor(Color.BLUE); // set color as blue
                    Rectangle rectangle = createRectangle(startPoint, endPoint); // form rectangle
                    g.drawRect(rectangle.x, rectangle.y, rectangle.width, rectangle.height); // draw rectangle on the pane
                }
            }
        };
        drawPanel.setOpaque(false);
        layeredPane.add(drawPanel, JLayeredPane.PALETTE_LAYER);
        add(layeredPane, BorderLayout.CENTER); // place grid in the center of the window

        // pause button action
        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timer.stop(); // stop timer

                String[] options = {"Continue", "Restart", "Quit", "Go Home"}; // possible options
                int choice = JOptionPane.showOptionDialog(Classic_Game.this,
                        "Game Paused", "Pause Menu", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                // switch with continuing, restarting, quiting, and going back to home screen.
                switch (choice) {
                    case 0: // continue
                        continueGame();
                        break;
                    case 1: // restart
                        restartGame();
                        break;
                    case 2: // Quit
                        System.exit(0);
                        break;
                    case 3: // Go Home
                        dispose();
                        new GameStartScreen();
                        break;
                }

                timer.start(); // timer resume
            }
        });

        // mouse control
        layeredPane.addMouseListener(new MouseAdapter() {
            int count = 0; // count score

            // if pressed, shows the action of all mouse controlling; get starting point
            @Override
            public void mousePressed(MouseEvent e) {
                startPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), labelsPanel);
                endPoint = startPoint;
                repaint();
                scorePanel.remove(scoreLabel);
            }

            // if released, select the numbers, and if satisfies, eliminate.
            @Override
            public void mouseReleased(MouseEvent e) {
                // get endpoint and form rectangle
                endPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), labelsPanel);
                Rectangle rectangle = createRectangle(startPoint, endPoint);

                // initialize sum of selected numbers, arraylist of selected and deleted values.
                int sum = 0;
                List<String> deletedValues = new ArrayList<>();
                List<String> selectedValues = new ArrayList<>();

                // select and get the number to calculate sum. If the user select empty labels only, throw exception
                for (JLabel label : labels) {
                    if (rectangle.intersects(label.getBounds())) {
                        selectedValues.add(label.getText());
                        try {
                            int value = Integer.parseInt(label.getText());
                            sum += value;
                        } catch (NumberFormatException exception) {
                            System.out.println("---Invalid number format: Only blank is selected.---");
                        }

                    }
                }
                if (sum == targetSum) { // eliminate if and only if is equal to the targetSum
                    for (JLabel label : labels) {
                        if (rectangle.intersects(label.getBounds())) {
                            deletedValues.add(label.getText());
                            deletedValues.remove(""); // removing empty string from the arraylist
                            label.setText(""); // eliminate
                        }
                    }
                    count += deletedValues.size(); // count scores by getting the size of arraylist of deletedValues
                }

                // print the actions to the terminal
                System.out.println("-------");
                System.out.println("Selected values: " + String.join(", ", selectedValues));
                System.out.println("Eliminated values: " + String.join(", ", deletedValues));
                System.out.println("Sum: " + sum);
                System.out.println("Score:" + count);

                // update score
                scoreLabel = new JLabel("Score: " + count);
                score = count;
                scorePanel.add(scoreLabel, BorderLayout.WEST);

                // reset for the next mouse action
                startPoint = null;
                endPoint = null;
                repaint();
            }
        });

        // Timer
        timer = new Timer(1000, new ActionListener() {
            int timeLeft = timeLimit; // initialize time left

            public void actionPerformed(ActionEvent e) {
                timeLeft--; // countdown by 1 seconds
                timerBar.setValue(timeLeft); // update timerBar
                timerBar.setString(timeLeft + " sec"); // update timeLeft on the timerBar
                timerLabel.setText("Time: " + timeLeft + " sec."); // refresh the timerLabel

                // if time is over,
                if (timeLeft <= 0) {
                    ((Timer) e.getSource()).stop(); // stop the timer
                    saveScoreAndTime(score,timeLimit); // save the record

                    // give options to the user and show the result
                    String[] GameOverOption = {"Restart", "Quit", "Go Home"};
                    int ending = JOptionPane.showOptionDialog(Classic_Game.this,
                            "Time is Over! Your score is " + score + ".", "Game Over",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, GameOverOption, GameOverOption[0]);
                    switch (ending) {
                        case 0: // restart
                            restartGame();
                            break;
                        case 1: // Quit
                            System.exit(0);
                            break;
                        case 2: // Go Home
                            dispose();
                            new GameStartScreen();
                            break;
                    }
                }
                // if the board is clear
                else if (isBoardClear()) {
                    ((Timer) e.getSource()).stop(); // stop the timer
                    timeRecord = timeLimit - timeLeft; // gets the timeRecord
                    saveScoreAndTime(score,timeRecord); // save

                    // give options to the user and show the result
                    String[] GameOverOption = {"Restart", "Quit", "Go Home"};
                    int ending2 = JOptionPane.showOptionDialog(Classic_Game.this,
                            "Board is cleared! Your final score is " + score + ".\n"
                                    + "Your time record is: " + timeRecord + " sec.", "Game Over",
                            JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, GameOverOption, GameOverOption[0]);
                    switch (ending2) {
                        case 0: // restart
                            restartGame();
                            break;
                        case 1: // Quit
                            System.exit(0);
                            break;
                        case 2: // Go Home
                            dispose();
                            new GameStartScreen();
                            break;
                    }
                }
            }
        });
        timer.start(); // start the timer

        // dragging action
        layeredPane.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                endPoint = SwingUtilities.convertPoint(e.getComponent(), e.getPoint(), labelsPanel);
                repaint();
            }
        });

        // window setup
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }


    // run the program if and only if it is validating with satisfying all conditions.
    @Override
    public void validate() {
        super.validate();
        if (layeredPane != null) {
            labelsPanel.setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
            layeredPane.getComponentsInLayer(JLayeredPane.PALETTE_LAYER)[0].setBounds(0, 0, layeredPane.getWidth(), layeredPane.getHeight());
        }
    }

    // saving records on the leaderboard
    private void saveScoreAndTime(int score, int timeRecord) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("leaderboardClassic.txt", true))) {
            writer.write(score + "," + timeRecord);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // creating rectangle function
    private Rectangle createRectangle(Point startPoint, Point endPoint) {
        int x = Math.min(startPoint.x, endPoint.x);
        int y = Math.min(startPoint.y, endPoint.y);
        int width = Math.abs(startPoint.x - endPoint.x);
        int height = Math.abs(startPoint.y - endPoint.y);

        return new Rectangle(x, y, width, height);
    }

    // continue game option
    private void continueGame() {
        scoreLabel.setText("Score: " + score);
        timer.restart();
    }


    // restarting game option
    private void restartGame() {
        Rectangle bounds = getBounds(); // save the current window size and the location
        dispose(); // close current window
        Classic_Game newGame = new Classic_Game(); // new classic_game initialize
        newGame.setBounds(bounds); // set bounds as the previous window
        newGame.setVisible(true);
    }
}
