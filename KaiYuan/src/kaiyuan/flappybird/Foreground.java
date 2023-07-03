package kaiyuan.flappybird;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import kaiyuan.flappybird.Constant;
import kaiyuan.flappybird.GameUtil;

import static kaiyuan.flappybird.ElementLayer.HORIZONTAL_INTERVAL;

//游戏前景，负责云朵生成的逻辑并绘制云朵，金币的生成与绘制
public class Foreground {

    private final List<Cloud> clouds; // 云朵的容器
    private final BufferedImage[] cloudImages; // 图片资源
    private long time; // 控制云的逻辑运算周期
    public static final int CLOUD_INTERVAL = 100; //云朵刷新的逻辑运算的周期

    //构造器
    public Foreground() {
        clouds = new ArrayList<>(); //云朵的容器
        jinbis = new ArrayList<>(); //金币的容器
        daojvs = new ArrayList<>(); //道具的容器
        // 读入图片资源
        cloudImages = new BufferedImage[Constant.CLOUD_IMAGE_COUNT];
        jinbiImgs = new BufferedImage[Constant.JINBI_IMAGE_COUNT];
        daojvImgs = new BufferedImage[Constant.DAOJV_IMAGE_COUNT];
        for (int i = 0; i < Constant.CLOUD_IMAGE_COUNT; i++) {
            cloudImages[i] = GameUtil.loadBufferedImage(Constant.CLOUDS_IMG_PATH[i]);
        }
        for (int i = 0; i < Constant.JINBI_IMAGE_COUNT; i++) {
            jinbiImgs[i] = GameUtil.loadBufferedImage(Constant.JINBI_IMG_PATH[i]);
        }
        for (int i = 0; i < Constant.DAOJV_IMAGE_COUNT; i++) {
            daojvImgs[i] = GameUtil.loadBufferedImage(Constant.DAOJU_IMG_PATH[i]);
        }
        time = System.currentTimeMillis();// 获取当前时间，用于控制云的逻辑运算周期
        timecoin = System.currentTimeMillis(); //同理
    }

    //绘制方法
    public void draw(Graphics g, Bird bird) {
        cloudBornLogic();
        for (Cloud cloud : clouds) {
            cloud.draw(g, bird);
        }
    }

    //云朵的控制
    private void cloudBornLogic() {
        // 100ms运算一次
        if (System.currentTimeMillis() - time > CLOUD_INTERVAL) {
            time = System.currentTimeMillis(); // 重置time
            // 如果屏幕的云朵的数量小于允许的最大数量，根据给定的概率随机添加云朵
            if (clouds.size() < Constant.MAX_CLOUD_COUNT) {
                try {
                    if (GameUtil.isInProbability(Constant.CLOUD_BORN_PERCENT, 100)) { // 根据给定的概率添加云朵
                        int index = GameUtil.getRandomNumber(0, Constant.CLOUD_IMAGE_COUNT); // 随机选取云朵图片

                        // 云朵刷新的坐标
                        int x = Constant.FRAME_WIDTH; // 从屏幕左侧开始刷新
                        // y坐标随机在上1/3屏选取
                        int y = GameUtil.getRandomNumber(Constant.TOP_BAR_HEIGHT, Constant.FRAME_HEIGHT / 3);

                        //向容器中添加云朵
                        Cloud cloud = new Cloud(cloudImages[index], x, y);
                        clouds.add(cloud);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // 若云朵飞出屏幕则从容器中移除
            for (int i = 0; i < clouds.size(); i++) {
                // 遍历容器中的云朵
                Cloud tempCloud = clouds.get(i);
                if (tempCloud.isOutFrame()) {
                    clouds.remove(i);
                    i--;
                }
            }
        }

    }

    public static final int zhouqi = 100; //金币刷新的逻辑运算的周期

    //金币资源数目
    public static final int jinbi_shu = 3;

    //屏幕中最多的金币数
    public static final int max_jinbi_shu = 2;

    //用来管理前景中的金币容器
    private  List<Coin> jinbis;

    private BufferedImage[] jinbiImgs; //金币图片

    private long timecoin; //控制金币的逻辑运算周期

    //绘制方法
    public void drawCoin(Graphics g,Bird bird){

        for (int i = 0; i < jinbis.size(); i++) {

            jinbis.get(i).draw(g, bird);

        }

        pengZhuang(bird);

        coinControl(bird);

        //ScoreCounter.getInstance().score1(bird,this);

    }

    //金币碰撞
    public boolean pengZhuang(Bird bird){

        if (bird.isDead()){
            return false;
        }
        for (int i = 0; i < jinbis.size(); i++) {

            Coin jinbi = jinbis.get(i);

            if(jinbi.getCoinCollisionRect().intersects(bird.getBirdCollisionRect())) {

                jinbis.remove(i);
                ScoreCounter.getInstance().score1();

                return true;

            }

        }

        return false;

    }

    //金币的控制
    private void coinControl(Bird bird)  {
        if(System.currentTimeMillis() - timecoin > zhouqi){
            timecoin = System.currentTimeMillis();//重置timecoin

            if(bird.isDead()){

                return ;
            }
            if(jinbis.size() < max_jinbi_shu){

                try {
                    if(GameUtil.isInProbability(Constant.JINBI_BORN_PERCENT,100)){

                        int index = GameUtil.getRandomNumber(0,jinbi_shu);

                        int x = Constant.FRAME_WIDTH;

                        int y = GameUtil.getRandomNumber(320,280);

                        Coin c1 = new Coin(jinbiImgs[index],x,y);

                        jinbis.add(c1);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            for (int i = 0; i < jinbis.size(); i++) {
                Coin c = jinbis.get(i);
                if(c.isOutFrame()){
                    jinbis.remove(i);
                    i --;
                }
            }
        }
    }

    public static final int zhouqi_daojv = 150; //道具刷新的逻辑运算周期

    //道具资源数目
    public static final int daojv_shu = 3;

    //最多道具数
    public static final int max_daojv_shu = 1;

    //道具容器
    private List<Daojv> daojvs;

    private BufferedImage[] daojvImgs;//图片

    private long timedaojv;

    //绘制方法
    public void drawDaojv(Graphics g,Bird bird){

        for (int i = 0; i < daojvs.size(); i++) {

            daojvs.get(i).draw(g,bird);

        }

        peng(bird);

        daojvControl(bird);

    }

    //道具碰撞
    public boolean peng(Bird bird) {

        if (bird.isDead()){
            return false;
        }
        for (int i = 0; i < daojvs.size(); i++) {

            Daojv daojv = daojvs.get(i);

            if(daojv.getDaojvCollisionRect().intersects(bird.getBirdCollisionRect())) {


                daojvs.remove(i);
                ScoreCounter.getInstance().score2();


                return true;

            }

        }

        return false;
    }

    //道具的控制
    private void daojvControl(Bird bird)  {
        if(System.currentTimeMillis() - timedaojv > zhouqi_daojv){
            timedaojv = System.currentTimeMillis();//重置daojvcoin

            if(bird.isDead()){

                return ;
            }
            if(daojvs.size() < max_daojv_shu){

                try {
                    if(GameUtil.isInProbability(Constant.DAOJV_BORN_PERCENT,100)){

                        int index = GameUtil.getRandomNumber(0,daojv_shu);

                        int x = Constant.FRAME_WIDTH;

                        int y = GameUtil.getRandomNumber(360,330);

                        Daojv d1 = new Daojv(daojvImgs[index],x,y);

                        daojvs.add(d1);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            for (int i = 0; i < daojvs.size(); i++) {
                Daojv d = daojvs.get(i);
                if(d.isOutFrame()){
                    daojvs.remove(i);
                    i --;
                }
            }
        }
    }

}