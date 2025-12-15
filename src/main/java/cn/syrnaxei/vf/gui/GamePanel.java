package cn.syrnaxei.vf.gui;

import cn.syrnaxei.vf.core.GameConfig;

import javax.swing.*;
import java.awt.*;

/**
 * 游戏面板类（核心）
 * 负责：绘制界面、处理游戏循环、接收用户输入
 */

public class GamePanel extends JPanel implements Runnable{
    private Thread gameThread; //引用线程对象
    private boolean isRunning;

    public GamePanel() {
        setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH,GameConfig.WINDOW_HEIGHT));
        setBackground(new Color(0, 0, 0));
        setFocusable(true);
        requestFocusInWindow();
    }

    public void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
        isRunning = true;
    }

    /**
     * 更新游戏状态
     * 待实现
     */
    private void updateGameState() {

    }

    /**
     * 游戏主循环（核心）
     * 运行在独立线程中，负责：更新游戏状态 → 重绘界面 → 控制帧率
     * gameThread.start调用
     */
    @Override
    public void run() {
        while(isRunning){
            updateGameState();
            repaint();
            try {
                Thread.sleep(GameConfig.FRAME_DELAY);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                JOptionPane.showMessageDialog(this,"线程中断:" + e.getMessage());
            }
        }
    }
}
