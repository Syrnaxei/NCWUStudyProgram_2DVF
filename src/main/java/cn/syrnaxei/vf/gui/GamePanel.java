package cn.syrnaxei.vf.gui;

import cn.syrnaxei.vf.core.GameConfig;
import cn.syrnaxei.vf.core.Target;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Random;

/**
 * 游戏面板类（核心）
 * 负责：绘制界面、处理游戏循环、接收用户输入
 */

public class GamePanel extends JPanel implements Runnable{
    private Thread gameThread; //引用线程对象
    private boolean isRunning;
    private int score;
    private Random random = new Random();

    private  java.util.List<Target> targetList;

    public GamePanel() {
        setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH,GameConfig.WINDOW_HEIGHT));
        setBackground(new Color(0, 0, 0));
        setFocusable(true);
        requestFocusInWindow();

        targetList = new java.util.ArrayList<>();
        initFixedTargets(5);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 鼠标点击时，检测是否击中靶子
                checkMouseHit(e.getX(), e.getY());
            }
        });
    }

    private void checkMouseHit(int mouseX, int mouseY) {
        //迭代器遍历Target表
        Iterator<Target> iterator = targetList.iterator();
        while (iterator.hasNext()) {
            Target target = iterator.next();
            if (target.isHit(mouseX, mouseY)) {
                target.setAlive(false); // 靶子死亡
                score += GameConfig.SCORE_PER_TARGET; // 加分
                break; // 一次点击只击中一个
            }
        }
    }

    public void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
        isRunning = true;
    }

    private void initFixedTargets(int spawnNum) {
        for(int i = 0;i < spawnNum;i++){
            int x = random.nextInt(GameConfig.WINDOW_WIDTH - GameConfig.TARGET_SIZE - 50 + 1) + 50; // 50 是边距，可调整
            int y = random.nextInt(GameConfig.WINDOW_HEIGHT - GameConfig.TARGET_SIZE - 50 + 1) + 50;
            System.out.println(x + " " + y);
            targetList.add(new Target(x,y,false));
        }
    }

    /**
     * 游戏主循环（核心）
     * 运行在独立线程中，负责：更新游戏状态 -> 重绘界面 -> 控制帧率
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

    protected void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(new Color(255, 255, 255));
        g2d.setFont(new Font("Arial",Font.BOLD,24));
        g2d.drawString("score:" + score,400,50);

        drawTargets(g2d);
    }

    private void drawTargets(Graphics2D g2d) {
        for(Target target : targetList){
            if(target.isAlive()){
                g2d.setColor(new Color(5, 158, 243));
                g2d.fillOval(target.getX(), target.getY(), GameConfig.TARGET_SIZE, GameConfig.TARGET_SIZE);
                //test
                g2d.setFont(new Font("Arial",Font.BOLD,10));
                g2d.drawString(target.getX() + "<-x y->" + target.getY(),target.getX(),target.getY());
            }
        }
    }
    /**
     * 更新游戏状态
     * 待实现
     */
    private void updateGameState() {

    }

}
