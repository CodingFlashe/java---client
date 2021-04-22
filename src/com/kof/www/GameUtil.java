package com.kof.www;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * <p>游戏工具类</p>
 * @author Bosen
 * 2020/11/14 21:33
 */
public class GameUtil {
    private CaoZhiJing caoZhiJing;
    private BaShenAn baShenAn;
    public int blood1;
    public int blood2;
    public boolean ko;

    public GameUtil(BaShenAn baShenAn,CaoZhiJing caoZhiJing) {
        this.blood1 = 100;
        this.blood2 = 100;
        this.ko = false;
        this.baShenAn = baShenAn;
        this.caoZhiJing = caoZhiJing;
    }

    /*
     * 检查优先级
     */
    public void check(boolean peng){
        long time1 = baShenAn.time;
        long time2 = caoZhiJing.time;
        if (peng) {
            // 八神庵后出手，打破草稚京攻势
            if (time1 > time2 || (time2 == 0 && time1 != 0) || caoZhiJing.stand || caoZhiJing.l) {
                // 八神庵i大于于草稚京i
                if (caoZhiJing.i && baShenAn.i){
                    caoZhiJingBeaten();return;
                }
                // 八神庵i大于草稚京u
                if (baShenAn.i && caoZhiJing.u){
                    caoZhiJingBeaten();return;
                }
                // 八神庵u小于草稚京i
                if (baShenAn.u && caoZhiJing.i){
                    baShenAnBeaten();return;
                }
                // 八神庵u小于草稚京u
                if (baShenAn.u && caoZhiJing.u){
                    baShenAnBeaten();return;
                }
                // 八神庵ui大于草稚京adjkl
                if ((baShenAn.u || baShenAn.i) && (caoZhiJing.j || caoZhiJing.k || caoZhiJing.stand
                        || caoZhiJing.after || caoZhiJing.before || caoZhiJing.l)){
                    caoZhiJingBeaten();return;
                }
                // 八神庵adjkl小于草稚京ui
                if ((caoZhiJing.u || caoZhiJing.i) && (baShenAn.j || baShenAn.k
                        || baShenAn.stand || baShenAn.after || baShenAn.before)){
                    baShenAnBeaten();return;
                }
                caoZhiJingBeaten();
            }

            // 草稚京后出手，打破八神庵攻势
            if (time1 < time2 || (time1 == 0 && time2 != 0) || baShenAn.stand || baShenAn.l) {
                // 草稚京i小于于八神庵i
                if (caoZhiJing.i && baShenAn.i){
                    caoZhiJingBeaten();return;
                }
                // 草稚京i大于八神庵u
                if (caoZhiJing.i && baShenAn.i){
                    baShenAnBeaten();return;
                }
                // 草稚京u大于于八神庵i
                if (caoZhiJing.u && baShenAn.i){
                    baShenAnBeaten();return;
                }
                // 草稚京u小于八神庵u
                if (caoZhiJing.u && baShenAn.u){
                    caoZhiJingBeaten();return;
                }
                // 草稚京ui大于八神庵adjkl
                if ((caoZhiJing.u || caoZhiJing.i) && (baShenAn.j || baShenAn.k
                        || baShenAn.stand || baShenAn.after || baShenAn.before)){
                    baShenAnBeaten();return;
                }
                // 草稚京adjkl小于八神庵ui
                if ((baShenAn.u || baShenAn.i) && (caoZhiJing.j || caoZhiJing.k || caoZhiJing.stand
                        || caoZhiJing.after || caoZhiJing.before || caoZhiJing.l)){
                    caoZhiJingBeaten();return;
                }
                baShenAnBeaten();
            }
        }
    }
    
    /*
     * 草稚京血量
     */
    public void caoZhiJingBlood(){
        if (blood1 > 0 && blood2 > 0){
            blood2 -= 1;
        }
    }

    /*
     * 八神庵血量
     */
    public void baShenAnBlood(){
        if (blood1 > 0 && blood2 > 0) {
            blood1 -= 1;
        }
    }
    
    /*
     * ko画面
     */
    static Image koImg = GameUtil.getImage("com/kof/www/images/ko.png");;
    public void ko(Graphics g){
        Font f = new Font("", Font.BOLD, 36);
        g.setColor(Color.white);
        g.setFont(f);
        ko = true;
        if (blood2 <= 0){
            System.out.println("KO！！！玩家一取得了游戏的胜利！");
            g.drawString("玩家一取得了游戏的胜利！！！",850,700);
        }else{
            System.out.println("KO！！！玩家二取得了游戏的胜利！");
            g.drawString("玩家二取得了游戏的胜利！！！",850,700);
        }
        g.drawString("按回车键继续下一场的对局",850,736);
        g.drawImage(koImg, 0, 0, Constant.GAME_WIDTH,Constant.GAME_HEIGHT,
                null,null);
    }
    
    /*
     * ko后的重置游戏键盘监听
     */
    public void addDirection(KeyEvent e){
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            blood1 = blood2 = 100;
            ko = false;
            baShenAn.x = Constant.PLAYER_X;
            caoZhiJing.x = Constant.GAME_WIDTH-Constant.PLAYER_X-Constant.PLAYER_WIDTH;
        }
    }

    /*
     * 返回指定路径文件的图片对象
     */
    public static Image getImage(String path) {
        BufferedImage bi = null;
        try {
            URL u = GameUtil.class.getClassLoader().getResource(path);
            bi = ImageIO.read(u);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi;
    }
    
    /*
     * 草稚京挨揍或被击飞
     */
    public void caoZhiJingBeaten(){
        // 草稚京挨揍
        if (baShenAn.j || baShenAn.k || baShenAn.u || baShenAn.i) {
            if (!caoZhiJing.down && !baShenAn.l && !baShenAn.down) {
                caoZhiJing.beaten = true;
                caoZhiJingBlood();
            }
        }

        // 草稚京被击飞
        if (BaShenAn.downKCount >= 2 || BaShenAn.uCount >= 8 || BaShenAn.iCount >= 8
                || BaShenAn.afterJCount >= 2) {
            if (!caoZhiJing.down || (caoZhiJing.down && caoZhiJing.k)) {
                caoZhiJing.beaten = false;
                caoZhiJing.fly = true;
                caoZhiJingBlood();
            }
        }
    }

    /*
     * 八神庵挨揍或被击飞
     */
    public void baShenAnBeaten(){
        // 八神庵挨揍
        if (caoZhiJing.j || caoZhiJing.k || caoZhiJing.u || caoZhiJing.i) {
            if (!baShenAn.down && !caoZhiJing.l && !caoZhiJing.down) {
                baShenAn.beaten = true;
                baShenAnBlood();
            }
        }

        // 八神庵被击飞
        if (CaoZhiJing.downKCount >= 5 || CaoZhiJing.uCount >= 6 || CaoZhiJing.iCount >= 8
                || CaoZhiJing.beforeJCount >= 5 || CaoZhiJing.afterKCount >= 4) {
            if (!baShenAn.down || (baShenAn.down && baShenAn.k)) {
                baShenAn.beaten = false;
                baShenAn.fly = true;
                baShenAnBlood();
            }
        }
    }
}
