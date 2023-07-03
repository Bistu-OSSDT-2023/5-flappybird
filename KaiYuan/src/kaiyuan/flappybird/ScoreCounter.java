package kaiyuan.flappybird;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import kaiyuan.flappybird.Constant;
import kaiyuan.flappybird.MusicUtil;

//游戏计时器
public class ScoreCounter {

    private static class ScoreCounterHolder {
        private static final ScoreCounter scoreCounter = new ScoreCounter();
    }

    public static ScoreCounter getInstance() {
        return ScoreCounterHolder.scoreCounter;
    }

    public long score = 0; // 分数
    private long bestScore; // 最高分数

    private ScoreCounter() {
        bestScore = -1;
        try {
            loadBestScore();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 装载最高纪录
    private void loadBestScore() throws Exception {
        File file = new File(Constant.SCORE_FILE_PATH);
        if (file.exists()) {
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            bestScore = dis.readLong();
            dis.close();
        }
    }

    public void saveScore() {
        bestScore = Math.max(bestScore, getCurrentScore());
        try {
            File file = new File(Constant.SCORE_FILE_PATH);
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
            dos.writeLong(bestScore);
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void score(Bird bird) {
        if (!bird.isDead()) {
            MusicUtil.playScore();
            score += 1;
        }

    }

    public void score1(){

            MusicUtil.playScore();
            score += 1;

    }

    public void score2(){

        if(score > 0){
            score -= 1;
        }
        return;

    }

    public long getBestScore() {
        return bestScore;
    }

    public long getCurrentScore() {
        return score;
    }

    public void reset() {
        score = 0;
    }

}
