package cn.syrnaxei.vf.gui;

import javax.swing.*;

public class GameGUI {
    private JFrame gameFrame;
    private GamePanel gamePanel;



    public void initGame() {
        gameFrame = new JFrame("vf");
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        gamePanel = new GamePanel();
        gameFrame.add(gamePanel);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }

    public void startGame() {
        gamePanel.startGameLoop();
    }

}
