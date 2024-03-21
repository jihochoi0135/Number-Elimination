import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * cardPanel; main panel
 * cl; main layout
 * newRows, newCols, newTime, targetSum; are for customized game input
 */
public class GameStartScreen extends JFrame {
    private JPanel cardPanel;
    private CardLayout cl;
    private int newRows;
    private int newCols;
    private int newTime;
    private int targetSum;

    // game start screen running function
    public GameStartScreen() {
        createStartScreen();
    }

    // creating start screen
    private void createStartScreen() {
        // basic set up
        cl = new CardLayout();
        cardPanel = new JPanel(cl);

        // set up for start screen and customized game screen (for input)
        JPanel startScreen = createStartPanel();
        JPanel customizedGame = createCustomizedGamePanel();
        cardPanel.add(startScreen, "START");
        cardPanel.add(customizedGame, "CUSTOMIZED_GAME");
        add(cardPanel); // display

        // window set up
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(900, 600);
        setVisible(true);
    }

    // start screen
    private JPanel createStartPanel() {
        // basic panel set up
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // Title
        JLabel title = new JLabel("Number Elimination", SwingConstants.CENTER);
        title.setFont(new Font("Serif", Font.BOLD, 24));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 3;
        constraints.insets = new Insets(10,10,10,10);
        panel.add(title, constraints);

        // Classic Game Button
        JButton button1 = new JButton("CLASSIC_GAME");
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(cardPanel, "CLASSIC_GAME");
                Rectangle bounds = getBounds(); // save current window setups (location and window size
                dispose(); // close current window
                Classic_Game newGame = new Classic_Game(); // run Classic_Game
                newGame.setBounds(bounds); // Pop-up with the same setting as previous screen
                newGame.setVisible(true); // display
            }
        });
        // button location and display
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        panel.add(button1, constraints);

        // Customized button
        JButton button2 = new JButton("CUSTOMIZED_GAME");
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(cardPanel, "CUSTOMIZED_GAME"); // show input screen
            }
        });
        // button location and display
        constraints.gridx = 1;
        constraints.gridy = 1;
        panel.add(button2, constraints);

        // PVP
        JButton button3 = new JButton("PVP");
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl.show(cardPanel, "PVP"); // display PVP screen
                createPvpPanel();
            }
        });
        // button location
        constraints.gridx = 2;
        constraints.gridy = 1;
        panel.add(button3, constraints);


        // button of leaderboard for classic games
        JButton button4 = new JButton("LEADERBOARD - CLASSIC");
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the bounds of the current window
                Rectangle bounds = getBounds();

                // Hide the current window
                setVisible(false);

                // Create and show the leaderboard window with the same size
                JFrame leaderboardFrame = new JFrame("Leaderboard");
                leaderboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                leaderboardFrame.setSize(bounds.width, bounds.height);
                leaderboardFrame.setLocation(bounds.x, bounds.y); // Set the location based on the previous window
                leaderboardFrame.add(new leaderboardClassic());
                leaderboardFrame.setVisible(true);
            }
        });
        // button location and display
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        panel.add(button4, constraints);

        // button of leaderboard for customized games
        JButton button5 = new JButton("LEADERBOARD - CUSTOMIZED");
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the bounds of the current window
                Rectangle bounds = getBounds();

                // Hide the current window
                setVisible(false);

                // Create and show the leaderboard window with the same size
                JFrame leaderboardFrame = new JFrame("Leaderboard");
                leaderboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                leaderboardFrame.setSize(bounds.width, bounds.height);
                leaderboardFrame.setLocation(bounds.x, bounds.y); // Set the location based on the previous window
                leaderboardFrame.add(new leaderboardCustomized());
                leaderboardFrame.setVisible(true);
            }
        });
        // button location and display
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        panel.add(button5, constraints);

        return panel;
    }


    // customized game panel
    private JPanel createCustomizedGamePanel() {
        // basic panel settings
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Customized Game", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 24));
        panel.add(label, BorderLayout.NORTH);

        // input settings
        JPanel inputPanel = new JPanel();
        JTextField rowField = new JTextField(2); // 2-digit row field
        JTextField colField = new JTextField(2); // 2-digit column field
        JTextField timeField = new JTextField(3); // 3-digits columns
        JTextField sumField = new JTextField(2); // 2-digits columns

        // Restrict input to numbers only for all textfields
        rowField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });

        colField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });

        timeField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });

        sumField.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char c = e.getKeyChar();
                if (!(Character.isDigit(c) || c == KeyEvent.VK_BACK_SPACE || c == KeyEvent.VK_DELETE)) {
                    e.consume();
                }
            }
        });


        // add all input panels
        inputPanel.add(new JLabel("Rows:"));
        inputPanel.add(rowField);
        inputPanel.add(new JLabel("Columns:"));
        inputPanel.add(colField);
        inputPanel.add(new JLabel("Time Limit:"));
        inputPanel.add(timeField);
        inputPanel.add(new JLabel(" seconds."));
        inputPanel.add(new JLabel("Total Sum to Eliminate: "));
        inputPanel.add(sumField);
        panel.add(inputPanel, BorderLayout.CENTER); // place them on the center

        JButton enterButton = new JButton("Enter"); // enter button

        // enter by pressing enter key on the keyboard;
        rowField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterButton.doClick();
                }
            }
        });

        colField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterButton.doClick();
                }
            }
        });

        timeField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterButton.doClick();
                }
            }
        });

        sumField.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterButton.doClick();
                }
            }
        });

        // if enter pressed/clicked;
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    // get texts and convert into integer
                    int sum = Integer.parseInt(sumField.getText());
                    int rows = Integer.parseInt(rowField.getText());
                    int cols = Integer.parseInt(colField.getText());
                    int time = Integer.parseInt(timeField.getText());

                    // if the condition are satisfies; please refer to the Customized_rule.png
                    if(sum >= 5 && sum <= 15){
                        if (rows >= 5 && rows <= 12 && cols >= 15 && cols <= 25) {
                            if (rows * cols <= 100) {
                                System.out.println(sum);
                                if (time >= 60 && time <= 90) {
                                    // Assign the values to new variables or use them as needed
                                    newRows = rows;
                                    newCols = cols;
                                    newTime = time;
                                    targetSum = sum;
                                    int result = JOptionPane.showConfirmDialog(panel, "You have typed " + "Rows: " + newRows + ", Columns: " + newCols + ", Time Limit: " + newTime + " seconds, Total Sum:" + targetSum, "Confirmation", JOptionPane.YES_NO_OPTION);
                                    if (result == JOptionPane.YES_OPTION) {
                                        System.out.println("Rows: " + newRows + ", Columns: " + newCols + ", Time Limit: " + newTime + " seconds, sum: " + targetSum);
                                        new Customized_Game(newRows, newCols, newTime, targetSum);
                                    } else {
                                        rowField.setText("");
                                        colField.setText("");
                                        timeField.setText("");
                                        sumField.setText("");
                                        rowField.requestFocusInWindow();
                                    }
                                } else {
                                    throw new IllegalArgumentException("Time must be between 60-90 seconds for under 100 numbers on the board.\n"
                                            + "Current total numbers: " + rows * cols + " and time entered: " + time);
                                }
                            } else if (rows * cols > 100 && rows * cols <= 200) {
                                if (time >= 90 && time <= 180) {
                                    // Assign the values to new variables or use them as needed
                                    newRows = rows;
                                    newCols = cols;
                                    newTime = time;
                                    targetSum = sum;
                                    System.out.println("Rows: " + newRows + ", Columns: " + newCols + ", Time Limit: " + newTime + " seconds, sum: " + targetSum);
                                    int result = JOptionPane.showConfirmDialog(panel, "You have typed " + "Rows: " + newRows + ", Columns: " + newCols + ", Time Limit: " + newTime + " seconds, total sum:" + targetSum, "Confirmation", JOptionPane.YES_NO_OPTION);
                                    if (result == JOptionPane.YES_OPTION) {
                                        System.out.println("Rows: " + newRows + ", Columns: " + newCols + ", Time Limit: " + newTime + " seconds, sum: " + targetSum);
                                        new Customized_Game(newRows, newCols, newTime, targetSum);
                                    } else {
                                        rowField.setText("");
                                        colField.setText("");
                                        timeField.setText("");
                                        sumField.setText("");
                                        rowField.requestFocusInWindow();
                                    }
                                } else {
                                    throw new IllegalArgumentException("Time must be between 90-180 seconds for between 100-200 numbers on the board.\n"
                                            + "Current total numbers: " + rows * cols + " and time entered: " + time);
                                }
                            } else {
                                if (time >= 120 && time <= 240) {
                                    newRows = rows;
                                    newCols = cols;
                                    newTime = time;
                                    targetSum = sum;
                                    System.out.println("Rows: " + newRows + ", Columns: " + newCols + ", Time Limit: " + newTime + " seconds, sum: " + targetSum);
                                    int result = JOptionPane.showConfirmDialog(panel, "You have typed " + "Rows: " + newRows + ", Columns: " + newCols + ", Time Limit: " + newTime + " seconds, total sum:" + targetSum, "Confirmation", JOptionPane.YES_NO_OPTION);
                                    if (result == JOptionPane.YES_OPTION) {
                                        System.out.println("Rows: " + newRows + ", Columns: " + newCols + ", Time Limit: " + newTime + " seconds, sum: " + targetSum);
                                        new Customized_Game(newRows, newCols, newTime, targetSum);
                                    } else {
                                        rowField.setText("");
                                        colField.setText("");
                                        timeField.setText("");
                                        sumField.setText("");
                                        rowField.requestFocusInWindow();
                                    }
                                } else {
                                    throw new IllegalArgumentException("Time must be between 120-240 seconds for over 200 numbers on the board.\n"
                                            + "Current total numbers: " + rows * cols + " and time entered: " + time);
                                }
                            }

                        } else {
                            throw new IllegalArgumentException("Rows must be between 5 and 12, and Columns must be between 15 and 25");
                        }
                    }else{
                        throw new IllegalArgumentException("Total sum to eliminate must be between 5 and 15.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Please enter valid numbers for Rows, Columns, Time Limit and Total Sum To Eliminate. ", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(panel, ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        inputPanel.add(enterButton);

        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new GameStartScreen();
            }
        });
        panel.add(backButton, BorderLayout.SOUTH);

        return panel;
    }



    private JPanel createPvpPanel() {
        JPanel panel = new JPanel();
        JLabel label = new JLabel("PVP Game", SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 24));
        panel.add(label);

        // 서버에 연결 중임을 알리는 팝업 메시지 띄우기
        JOptionPane.showMessageDialog(panel, "Connecting to server...");

        // 서버에 연결하는 로직 추가
        // 예시: connectToServer();

        return panel;
    }



    public static void main(String[] args) {
        new GameStartScreen();
    }
}

