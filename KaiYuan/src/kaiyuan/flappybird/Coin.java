package kaiyuan.flappybird;

import java.awt.*;
import java.awt.image.BufferedImage;

import kaiyuan.flappybird.Constant;

public class Coin {

    //图片
    private BufferedImage img;
    //速度
    private final int speed;
    //方向
    private int dir;
    public static final int DIR_left=0;
    public static final int DIR_right=1;
    //坐标
    private int x,w,h;

    private final int y;

    private final int ImageWidth;
    private final int ImageHeight;

    private final Rectangle coinCollisionRect;

    public Coin(BufferedImage img, int x, int y){

        super();
        this.img=img;
        this.speed=Constant.GAME_SPEED;
        //this.dir=dir;
        this.x=x;
        this.y=y;

        ImageWidth = img.getWidth();
        ImageHeight = img.getHeight();

        w = ImageWidth;
        h = ImageHeight;

        coinCollisionRect = new Rectangle();

        coinCollisionRect.x = x;
        coinCollisionRect.y = y;
        coinCollisionRect.width = w;
        coinCollisionRect.height = h;
    }

    //绘制方法
    public void draw(Graphics g, Bird bird) {

        if(bird.isDead()){
            return;
        }
        int speed = this.speed;
        if (bird.isDead())
            speed = 1;
        x -= speed;
        g.drawImage(img, x, y, ImageWidth, ImageHeight, null);

        logic();
    }

    //判断金币是否出屏幕
    public boolean isOutFrame() {
        return x < -1 * ImageWidth;
    }

    public Rectangle getCoinCollisionRect() {
        return coinCollisionRect;
    }

    public void logic(){

        coinCollisionRect.x -= speed;
    }

}
