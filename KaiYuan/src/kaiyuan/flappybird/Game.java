package kaiyuan.flappybird;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import kaiyuan.flappybird.ElementLayer;
import kaiyuan.flappybird.Bird;
import kaiyuan.flappybird.Background;
import kaiyuan.flappybird.Foreground;
import kaiyuan.flappybird.WelcomeAnimation;

import static kaiyuan.flappybird.Constant.FRAME_HEIGHT;
import static kaiyuan.flappybird.Constant.FRAME_WIDTH;
import static kaiyuan.flappybird.Constant.FRAME_X;
import static kaiyuan.flappybird.Constant.FRAME_Y;
import static kaiyuan.flappybird.Constant.FPS;
import static kaiyuan.flappybird.Constant.GAME_TITLE;

//游戏的主体
public class Game extends Frame{

    private static final long serialVersionUID = 1L; // 保持版本的兼容性

    private static int gameState; // 游戏状态
    public static final int GAME_READY = 0; // 游戏未开始
    public static final int GAME_START = 1; // 游戏开始
    public static final int STATE_OVER = 2; // 游戏结束

    private Background background; // 游戏背景对象
    private Foreground foreground; // 游戏前景对象
    private Bird bird; // 小鸟对象
    private ElementLayer gameElement; // 游戏元素对象
    private WelcomeAnimation welcomeAnimation; // 游戏未开始时对象

    // 在构造器中初始化
    public Game() {
        initFrame(); // 初始化游戏窗口
        setVisible(true); // 窗口默认为不可见，设置为可见
        initGame(); // 初始化游戏对象
    }

    // 初始化游戏窗口
    private void initFrame() {
        setSize(FRAME_WIDTH, FRAME_HEIGHT); // 设置窗口大小
        setTitle(GAME_TITLE); // 设置窗口标题
        setLocation(FRAME_X, FRAME_Y); // 窗口初始位置
        setResizable(false); // 设置窗口大小不可变
        // 添加关闭窗口事件（监听窗口发生的事件，派发给参数对象，参数对象调用对应的方法）
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0); // 结束程序
            }
        });
        addKeyListener(new BirdKeyListener()); // 添加按键监听
    }

    // 用于接收按键事件的对象的内部类
    class BirdKeyListener implements KeyListener {
        // 按键按下，根据游戏当前的状态调用不同的方法
        public void keyPressed(KeyEvent e) {
            int keycode = e.getKeyCode();
            switch (gameState) {
                case GAME_READY:
                    if (keycode == KeyEvent.VK_SPACE) {
                        // 游戏启动界面时按下空格，小鸟振翅一次并开始受重力影响
                        bird.birdFlap();
                        bird.birdFall();
                        setGameState(GAME_START); // 游戏状态改变
                    }
                    break;
                case GAME_START:
                    if (keycode == KeyEvent.VK_SPACE) {
                        //游戏过程中按下空格则振翅一次，并持续受重力影响
                        bird.birdFlap();
                        bird.birdFall();
                    }
                    break;
                case STATE_OVER:
                    if (keycode == KeyEvent.VK_SPACE) {
                        //游戏结束时按下空格，重新开始游戏
                        resetGame();
                    }
                    break;
            }
        }

        // 重新开始游戏
        private void resetGame() {
            setGameState(GAME_READY);
            gameElement.reset();
            bird.reset();
        }

        // 按键松开，更改按键状态标志
        public void keyReleased(KeyEvent e) {
            int keycode = e.getKeyChar();
            if (keycode == KeyEvent.VK_SPACE) {
                bird.keyReleased();
            }
        }

        public void keyTyped(KeyEvent e) {
        }
    }

    // 初始化游戏中的各个对象
    private void initGame() {
        background = new Background();
        gameElement = new ElementLayer();
        foreground = new Foreground();
        welcomeAnimation = new WelcomeAnimation();
        bird = new Bird();
        setGameState(GAME_READY);

        // 启动用于刷新窗口的线程
        new Thread(() ->{
            while (true) {
                repaint(); // 通过调用repaint(),让JVM调用update()
                try {
                    Thread.sleep(FPS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private final BufferedImage bufImg = new BufferedImage(FRAME_WIDTH, FRAME_HEIGHT, BufferedImage.TYPE_4BYTE_ABGR);

    public void update(Graphics g) {
        Graphics bufG = bufImg.getGraphics(); // 获得图片画笔
        // 使用图片画笔将需要绘制的内容绘制到图片
        background.draw(bufG, bird); // 背景层
        foreground.draw(bufG, bird);// 前景层

        if (gameState == GAME_READY) { // 游戏未开始
            welcomeAnimation.draw(bufG);
        } else { // 游戏结束
            gameElement.draw(bufG, bird);// 游戏元素层
            foreground.drawCoin(bufG,bird);
            foreground.drawDaojv(bufG,bird);
        }
        bird.draw(bufG);
        g.drawImage(bufImg, 0, 0, null); // 一次性将图片绘制到屏幕上
    }

    public static void setGameState(int gameState) {
        Game.gameState = gameState;
    }

}
