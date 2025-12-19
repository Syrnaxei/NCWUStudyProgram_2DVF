package cn.syrnaxei.vf;

import cn.syrnaxei.vf.gui.GameGUI;

import javax.swing.*;

public class GameMain {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameGUI game = new GameGUI();
            game.initGame();
        });
    }
}

