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
    private int countdown = 30; // 30秒倒计时
    private long lastSecond = 0; // 上一秒的时间戳

    private final Random random = new Random();

    private  java.util.List<Target> targetList;

    public GamePanel() {
        setPreferredSize(new Dimension(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT));
        setBackground(new Color(249, 239, 245));
        setFocusable(true);
        requestFocusInWindow();


        // 不再初始化靶子和鼠标监听器，因为游戏还未开始
        targetList = new java.util.ArrayList<>();

        // 添加鼠标监听器，但根据游戏状态处理不同逻辑
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!isRunning) {
                    // 如果游戏未开始，点击任意位置开始游戏
                    startGame();
                } else {
                    // 如果游戏已开始，处理正常的打靶逻辑
                    checkMouseHit(e.getX(), e.getY());
                }
            }
        });
    }

    public void startGame() {
        if (!isRunning) {  // 防止重复启动
            isRunning = true;
            initializeTargets();
            requestFocusInWindow();

            countdown = 30;
            lastSecond = System.currentTimeMillis();
            // 启动游戏线程
            startGameLoop();  // 添加这一行来启动游戏循环

            repaint();
        }
    }

    public void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
        isRunning = true;
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
            updateCountdown();
            repaint();
            try {
                Thread.sleep(GameConfig.FRAME_DELAY);
            }catch (InterruptedException e){
                Thread.currentThread().interrupt();
                JOptionPane.showMessageDialog(this,"线程中断:" + e.getMessage());
            }
        }
    }

    /**
     * 更新游戏状态
     */
    private void updateGameState() {
        //判断targetList是否为空
        if (targetList.isEmpty()) {
            if(GameConfig.DEBUG_MODE){
                System.out.println("TargetList is empty");
            }
            initFixedTargets(5);
        }
    }

    // 添加倒计时更新方法
    private void updateCountdown() {
        if (isRunning && countdown > 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastSecond >= 1000) { // 每秒更新一次
                countdown--;
                lastSecond = currentTime;

                if (countdown <= 0) {
                    // 倒计时结束，显示选择对话框
                    SwingUtilities.invokeLater(this::showGameOverDialog);
                }
            }
        }
    }

    private void initializeTargets() {
        targetList = new java.util.ArrayList<>();
        initFixedTargets(5);
    }

    private void initFixedTargets(int spawnNum) {
        for(int i = 0;i < spawnNum;i++){
            int x = random.nextInt(GameConfig.WINDOW_WIDTH - GameConfig.TARGET_SIZE - 50 + 1) + 50; // 50 是边距，可调整
            int y = random.nextInt(GameConfig.WINDOW_HEIGHT - GameConfig.TARGET_SIZE - 50 + 1) + 50;
            int size = random.nextInt(GameConfig.TARGET_SIZE - 40 + 1) + 40;
            if(GameConfig.DEBUG_MODE){
                System.out.println(x + " " + y);
            }
            targetList.add(new Target(x,y,size));
        }
    }

    private void checkMouseHit(int mouseX, int mouseY) {
        //迭代器遍历Target表
        Iterator<Target> iterator = targetList.iterator();
        while (iterator.hasNext()) {
            Target target = iterator.next();
            if (target.isHit(mouseX, mouseY, target.getSize())) {
                target.setAlive(false); // 靶子死亡

                score += GameConfig.SCORE_PER_TARGET + (GameConfig.TARGET_SIZE - target.getSize()); // 根据靶子大小计算分数，靶子越小分数越高
                iterator.remove();
                break; // 一次点击只击中一个
            }
        }
    }

    // 添加游戏结束对话框方法
    private void showGameOverDialog() {
        isRunning = false; // 暂停游戏循环

        JOptionPane.showMessageDialog(
                this,
                "时间到！您的得分是: " + score,
                "游戏结束",
                JOptionPane.INFORMATION_MESSAGE
        );

        // 重置游戏状态
        score = 0;
        targetList.clear();
        repaint();
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (!isRunning) {
            // 绘制主页界面
            drawHomePage(g2d);
        } else {
            // 绘制游戏界面
            drawGameUI(g2d);
            drawTargets(g2d);
        }
    }

    private void drawHomePage(Graphics2D g2d) {
        // 绘制网格背景
        g2d.setColor(new Color(249, 239, 245));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // 绘制网格线
        g2d.setColor(new Color(230, 220, 235)); // 浅灰色网格线
        g2d.setStroke(new BasicStroke(1));

        // 绘制垂直线
        for (int x = 0; x < getWidth(); x += 20) {
            g2d.drawLine(x, 0, x, getHeight());
        }

        // 绘制水平线
        for (int y = 0; y < getHeight(); y += 20) {
            g2d.drawLine(0, y, getWidth(), y);
        }

        // 绘制标题
        g2d.setColor(new Color(0, 0, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 36));
        String title = "VF";
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, (getWidth() - titleWidth) / 2, getHeight() / 2 - 50);

        // 绘制副标题
        g2d.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        String subtitle = "在30s内点击屏幕出现的小球获取分数";
        fm = g2d.getFontMetrics();
        int subtitleWidth = fm.stringWidth(subtitle);
        g2d.drawString(subtitle, (getWidth() - subtitleWidth) / 2, getHeight() / 2);

        // 绘制提示文字
        String prompt = "点击任意位置开始游戏";
        fm = g2d.getFontMetrics();
        int promptWidth = fm.stringWidth(prompt);
        g2d.drawString(prompt, (getWidth() - promptWidth) / 2, getHeight() / 2 + 100);
    }


    // 修改 drawGameUI() 方法，添加倒计时显示
// 修改 drawGameUI() 方法，添加网格背景
    private void drawGameUI(Graphics2D g2d) {
        // 绘制网格背景（游戏界面）
        g2d.setColor(new Color(249, 239, 245));
        g2d.fillRect(0, 0, getWidth(), getHeight());

        // 绘制网格线
        g2d.setColor(new Color(230, 220, 235)); // 浅灰色网格线
        g2d.setStroke(new BasicStroke(1));

        // 绘制垂直线
        for (int x = 0; x < getWidth(); x += 20) {
            g2d.drawLine(x, 0, x, getHeight());
        }

        // 绘制水平线
        for (int y = 0; y < getHeight(); y += 20) {
            g2d.drawLine(0, y, getWidth(), y);
        }

        // 绘制分数和倒计时
        g2d.setColor(new Color(0, 0, 0));
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("score:" + score, 40, 40);

        // 绘制倒计时（右上角）
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        FontMetrics fm = g2d.getFontMetrics();
        String timeText = "Time: " + countdown + "s";
        int timeWidth = fm.stringWidth(timeText);
        g2d.drawString(timeText, getWidth() - timeWidth - 40, 40); // 右边距40像素
    }


    private void drawTargets(Graphics2D g2d) {
        for(Target target : targetList){
            if(target.isAlive()){
                // 绘制主圆形
                g2d.setColor(new Color(105, 240, 174));
                g2d.fillOval(target.getX(), target.getY(), target.getSize(), target.getSize());

                // 白色边框
                g2d.setColor(Color.WHITE);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawOval(target.getX(), target.getY(), target.getSize(), target.getSize());

                //test
                g2d.setFont(new Font("Arial",Font.BOLD,10));
                if(GameConfig.DEBUG_MODE){
                    g2d.drawString(target.getX() + "<-x" + target.getSize() +"y->" + target.getY(), target.getX(), target.getY());
                }
            }
        }
    }

}
