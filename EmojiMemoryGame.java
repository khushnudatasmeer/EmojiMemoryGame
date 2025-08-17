import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.border.Border;


public class MiniEmojiMemoryGame extends JFrame {

    private JLayeredPane layeredPane;
    private JLabel timerLabel, scoreLabel;
    private int countdown, score = 0;
    private java.util.List<String> toRemember = new ArrayList<>();
    private java.util.List<JButton> selectedButtons = new ArrayList<>();
    private final String[] emojis = {"🐶", "🍕", "🌟", "🎈", "🎮", "🎯"};

    public MiniEmojiMemoryGame() {
        setTitle("Emoji Memory Game");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        getContentPane().setLayout(new BorderLayout());
        layeredPane = new JLayeredPane();
        layeredPane.setBounds(0, 0, getWidth(), getHeight());
        getContentPane().add(layeredPane, BorderLayout.CENTER);

        // Update everything on window resize
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                layeredPane.setBounds(0, 0, getWidth(), getHeight());
                for (Component comp : layeredPane.getComponents()) {
                    if (comp instanceof JLabel lbl && lbl.getIcon() instanceof ImageIcon) {
                        ImageIcon icon = (ImageIcon) lbl.getIcon();
                        Image img = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
                        lbl.setIcon(new ImageIcon(img));
                        lbl.setBounds(0, 0, getWidth(), getHeight());
                    } else if (comp instanceof JPanel panel) {
                        panel.setBounds(0, 0, getWidth(), getHeight());
                    }
                }
                layeredPane.revalidate();
                layeredPane.repaint();
            }
        });

        showStartScreen();
    }

    // ---------------- Start Screen ----------------
   private void showStartScreen() {
    layeredPane.removeAll();

    // Background image
    JLabel bgLabel = createBackgroundLabel("C:/Users/Ayesha/Desktop/khushi project/levelimage.jpg");
    layeredPane.add(bgLabel, Integer.valueOf(0));

    // Panel to hold title and button
    JPanel panel = new JPanel();
    panel.setOpaque(false);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBounds(0, 0, getWidth(), getHeight());
    layeredPane.add(panel, Integer.valueOf(1));

    panel.add(Box.createVerticalGlue()); // Push content to vertical center

    // Title label as heading (no box, just text)
    JLabel title = new JLabel("Emoji Memory Game", SwingConstants.CENTER);
    title.setFont(new Font("Segoe UI Emoji", Font.BOLD, 70));
    title.setForeground(Color.WHITE); // text color
    title.setAlignmentX(Component.CENTER_ALIGNMENT);
    panel.add(title);

    panel.add(Box.createRigidArea(new Dimension(0, 100))); // space between title and button

    // Oval Start Button (transparent background, no blue fill)
    JButton startButton = new JButton("Start Game") {
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Draw only the button shape (blue)
           g2.setColor(new Color(139, 69, 19));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        public void setBorder(javax.swing.border.Border border) {
            // Remove default border
        }
    };
    startButton.setContentAreaFilled(false); // remove default fill
    startButton.setFocusPainted(false);
    startButton.setForeground(Color.WHITE); // text color
    startButton.setFont(new Font("Arial", Font.BOLD, 36));
    startButton.setPreferredSize(new Dimension(400, 150));
    startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
    startButton.addActionListener(e -> showLevelSelection());

    panel.add(startButton);

    panel.add(Box.createVerticalGlue()); // Push content to vertical center

    layeredPane.revalidate();
    layeredPane.repaint();
}

    // ---------------- Level Selection ----------------
    private void showLevelSelection() {
        layeredPane.removeAll();

        JLabel bgLabel = createBackgroundLabel("C:/Users/Ayesha/Desktop/khushi project/levelimage.jpg");
        layeredPane.add(bgLabel, Integer.valueOf(0));

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(panel, Integer.valueOf(1));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.insets = new Insets(20, 0, 20, 0);

        JLabel label = new JLabel("Select Level", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI Emoji", Font.BOLD, 60));
        label.setForeground(Color.WHITE);
        gbc.gridy = 0;
        panel.add(label, gbc);

        String[] levels = {"Easy", "Medium", "Hard"};
        int y = 1;
        for (String level : levels) {
            JButton btn = new JButton(level);
            btn.setFont(new Font("Arial", Font.BOLD, 36));
            btn.setPreferredSize(new Dimension(250, 100));
            btn.setBackground(new Color(139, 69, 19));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.addActionListener(e -> startMemoryChallenge(level));
            gbc.gridy = y++;
            panel.add(btn, gbc);
        }

        layeredPane.revalidate();
        layeredPane.repaint();
    }

    // ---------------- Memory Challenge ----------------
    private void startMemoryChallenge(String level) {
    layeredPane.removeAll();
    selectedButtons.clear();

    int emojiCount;
    switch (level) {
        case "Easy" -> { countdown = 6; emojiCount = 3; }
        case "Medium" -> { countdown = 4; emojiCount = 4; }
        default -> { countdown = 3; emojiCount = 5; }
    }

    java.util.List<String> list = new ArrayList<>(Arrays.asList(emojis));
    Collections.shuffle(list);
    toRemember = list.subList(0, emojiCount);

    JLabel bgLabel = createBackgroundLabel("C:/Users/Ayesha/Desktop/khushi project/countdown.jpeg");
    layeredPane.add(bgLabel, Integer.valueOf(0));

    JPanel panel = new JPanel();
    panel.setOpaque(false);
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // vertical layout
    panel.setBounds(0, 0, getWidth(), getHeight());
    layeredPane.add(panel, Integer.valueOf(1));

    panel.add(Box.createVerticalGlue()); // pushes content to vertical center

    // Emoji panel
    JPanel emojiPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 50, 50));
    emojiPanel.setOpaque(false);
    for (String emoji : toRemember) {
        JLabel lbl = new JLabel(emoji, SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 80));
        lbl.setOpaque(true);
        lbl.setBackground(new Color(255, 255, 255, 200));
        lbl.setBorder(BorderFactory.createLineBorder(Color.BLACK, 3));
        lbl.setPreferredSize(new Dimension(120, 120));
        emojiPanel.add(lbl);
    }
    panel.add(emojiPanel);

    panel.add(Box.createRigidArea(new Dimension(0, 50))); // space between emojis and timer

    // Timer label
    timerLabel = new JLabel("Memorize the emojis! Time left: " + countdown, SwingConstants.CENTER);
    timerLabel.setFont(new Font("Arial", Font.BOLD, 36));
    timerLabel.setForeground(Color.YELLOW);
    timerLabel.setOpaque(true);
    timerLabel.setBackground(new Color(0, 0, 0, 150));
    timerLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // center horizontally
    panel.add(timerLabel);

    panel.add(Box.createVerticalGlue()); // pushes content to vertical center

    new javax.swing.Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            countdown--;
            timerLabel.setText("Memorize the emojis! Time left: " + countdown);
            if (countdown <= 0) {
                ((javax.swing.Timer) e.getSource()).stop();
                showAnswerOptions(emojiCount);
            }
        }
    }).start();

    layeredPane.revalidate();
    layeredPane.repaint();
}

    // ---------------- Answer Selection ----------------
    private void showAnswerOptions(int emojiCount) {
        layeredPane.removeAll();

        JLabel bgLabel = createBackgroundLabel("C:/Users/Ayesha/Desktop/khushi project/emojisselection.jpeg");
        layeredPane.add(bgLabel, Integer.valueOf(0));

        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setOpaque(false);
        panel.setBounds(0, 0, getWidth(), getHeight());
        layeredPane.add(panel, Integer.valueOf(1));

        JLabel question = new JLabel("Select the " + emojiCount + " emojis you saw:", SwingConstants.CENTER);
        question.setFont(new Font("Arial", Font.BOLD, 36));
        question.setForeground(Color.WHITE);
        question.setOpaque(true);
        question.setBackground(new Color(0, 0, 0, 150));
        question.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(question, BorderLayout.NORTH);

        JPanel optionsPanel = new JPanel(new GridLayout(2, 6, 20, 20));
        optionsPanel.setOpaque(false);
        java.util.List<String> options = new ArrayList<>(Arrays.asList(emojis));
        Collections.shuffle(options);

        for (String opt : options) {
            JButton btn = new JButton(opt);
            btn.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 50));
            btn.setFocusPainted(false);
            btn.addActionListener(e -> handleSelection(btn, emojiCount));
            optionsPanel.add(btn);
        }
        panel.add(optionsPanel, BorderLayout.CENTER);

       
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    // ---------------- Button Selection ----------------
    private void handleSelection(JButton btn, int emojiCount) {
        if (selectedButtons.contains(btn)) {
            btn.setOpaque(false);
            btn.setBackground(null);
            selectedButtons.remove(btn);
        } else if (selectedButtons.size() < emojiCount) {
            btn.setOpaque(true);
            btn.setBackground(Color.BLUE);
            selectedButtons.add(btn);
        }

        if (selectedButtons.size() == emojiCount) {
            checkSelections();
        }
    }

    // ---------------- Check Answers ----------------
    private void checkSelections() {
        int correctCount = 0;
        for (JButton btn : selectedButtons) {
            if (toRemember.contains(btn.getText())) {
                btn.setBackground(Color.GREEN);
                correctCount++;
            } else {
                btn.setBackground(Color.RED);
            }
        }

        if (correctCount == toRemember.size()) {
            score += 10;
            JOptionPane.showMessageDialog(this, "Excellent! You remembered all! 🎉");
            openScoreCard();
        } else {
            JOptionPane.showMessageDialog(this, "Oops! Some selections were wrong. Try again.");
        }
    }

    private void openScoreCard() {
        ScoreCardFrame scoreFrame = new ScoreCardFrame(score);
        scoreFrame.setVisible(true);
        this.dispose();
    }

    // ---------------- Utility to create scalable background ----------------
    private JLabel createBackgroundLabel(String path) {
        ImageIcon icon = new ImageIcon(path);
        Image img = icon.getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(img));
        label.setBounds(0, 0, getWidth(), getHeight());
        return label;
    }

    // ---------------- Score Card Frame ----------------
    private static class ScoreCardFrame extends JFrame {
        public ScoreCardFrame(int currentScore) {
            setTitle("Score Card");
            setSize(800, 600);
            setDefaultCloseOperation(EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            JLayeredPane layeredPane = new JLayeredPane();
            layeredPane.setBounds(0,0,getWidth(),getHeight());
            add(layeredPane);

            JLabel bgLabel = new JLabel(new ImageIcon(new ImageIcon(
                    "C:/Users/Ayesha/Desktop/khushi project/scorecard.jpeg")
                    .getImage().getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH)));
            bgLabel.setBounds(0,0,getWidth(),getHeight());
            layeredPane.add(bgLabel, Integer.valueOf(0));

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false);
            panel.setBounds(0,0,getWidth(),getHeight());
            layeredPane.add(panel, Integer.valueOf(1));

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.insets = new Insets(20,0,20,0);

            JLabel scoreLabel = new JLabel("Your Score: " + currentScore, SwingConstants.CENTER);
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 60));
            scoreLabel.setForeground(Color.GRAY);
            gbc.gridy = 0;
            panel.add(scoreLabel, gbc);

            JButton nextLevelBtn = new JButton("Next Level");
            nextLevelBtn.setFont(new Font("Arial", Font.BOLD, 48));
            nextLevelBtn.setBackground(new Color(50,150,250));
            nextLevelBtn.setForeground(Color.WHITE);
            nextLevelBtn.setFocusPainted(false);
            nextLevelBtn.addActionListener(e -> {
                Level2Game level2 = new Level2Game(currentScore);
                level2.setVisible(true);
                ScoreCardFrame.this.dispose();
            });
            gbc.gridy = 1;
            panel.add(nextLevelBtn, gbc);
        }
    }

    // ---------------- Level 2 Frame ----------------
    private static class Level2Game extends JFrame {
    public Level2Game(int prevScore) {
        setTitle("Level 2");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Background label with image
        ImageIcon bgIcon = new ImageIcon("C:/Users/Ayesha/Desktop/khushi project/emojisselection.jpeg");
        Image img = bgIcon.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        JLabel bgLabel = new JLabel(new ImageIcon(img));
        bgLabel.setLayout(new GridBagLayout()); // allows adding components on top

        // Text label
        JLabel label = new JLabel("Welcome to Level 2!", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 60));
        label.setForeground(Color.BLACK);

        // Add the text label to the background label
        bgLabel.add(label, new GridBagConstraints());

        // Add background label to frame
        add(bgLabel, BorderLayout.CENTER);

        setVisible(true);
    }
}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MiniEmojiMemoryGame().setVisible(true));
    }
}
