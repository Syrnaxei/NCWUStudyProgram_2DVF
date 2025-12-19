package cn.syrnaxei.vf.gui;

import javax.swing.*;

public class GameGUI {

    public void initGame() {
        JFrame gameFrame = new JFrame("VF");
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        gameFrame.add(gamePanel);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
    }
}

