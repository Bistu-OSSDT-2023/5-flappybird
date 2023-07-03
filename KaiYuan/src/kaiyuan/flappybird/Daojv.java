package kaiyuan.flappybird;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Daojv {

    //图片
    private BufferedImage img;
    //速度
    private final int speed;

    //坐标
    private int x,w,h;

    private final int y;

    private final int ImageWidth;
    private final int ImageHeight;

    private final Rectangle daojvCollisionRect;

    public Daojv(BufferedImage img, int x, int y){

        super();
        this.img=img;
        this.speed=Constant.GAME_SPEED;
        this.x=x;
        this.y=y;

        ImageWidth = img.getWidth();
        ImageHeight = img.getHeight();

        w = ImageWidth;
        h = ImageHeight;

        daojvCollisionRect = new Rectangle();
        daojvCollisionRect.x = x;
        daojvCollisionRect.y = y;
        daojvCollisionRect.width = w;
        daojvCollisionRect.height = h;

    }

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

    public boolean isOutFrame() {
        return x < -1 * ImageWidth;
    }

    public Rectangle getDaojvCollisionRect() {
        return daojvCollisionRect;
    }

    public void logic(){

        daojvCollisionRect.x -= speed;
    }
}
