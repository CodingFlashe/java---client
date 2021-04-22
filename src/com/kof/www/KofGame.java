package com.kof.www;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;

/**
 * <p>kof游戏启动类</p>
 * @author Bosen
 * 2020/11/14 10:09
 */
public class KofGame extends Frame {
    /*
     * 游戏启动主方法
     */
    public static void main(String[] args) {
        KofGame mgf = new KofGame();
        mgf.launchFrame();
    }

    /*
     * 角色初始化
     */
    BaShenAn baShenAn = new BaShenAn(Constant.PLAYER_X,Constant.PLAYER_Y,
            Constant.PLAYER_WIDTH,Constant.PLAYER_HEIGHT);// 八神庵角色
    CaoZhiJing caoZhiJing = new CaoZhiJing(Constant.GAME_WIDTH-Constant.PLAYER_X-Constant.PLAYER_WIDTH,
            Constant.PLAYER_Y, Constant.PLAYER_WIDTH,Constant.PLAYER_HEIGHT);;// 草稚京角色

    GameUtil gameUtil = new GameUtil(baShenAn,caoZhiJing);

    Image bg = GameUtil.getImage("com/kof/www/images/bg.jpg");// 背景图
    Image portrait1 = GameUtil.getImage("com/kof/www/images/portrait1.png");// 玩家一头像
    Image portrait2 = GameUtil.getImage("com/kof/www/images/portrait2.png");// 玩家二头像

    /*
     * 绘画
     */
    @Override
    public void paint(Graphics g) {
        Color c =  g.getColor();
        // 画背景
        g.drawImage(bg, 0, 0, Constant.GAME_WIDTH,Constant.GAME_HEIGHT,
                null,null);

        // 出招表提示
        Font f = new Font("", Font.BOLD, 24);
        g.setFont(f);
        g.setColor(Color.blue);
        g.drawString("键位提示：(当你学会了基本的操作，尝试搭配左右键位释放隐藏的组合技，技能需要蓄力，长按效果更佳)",30,90);
        g.setColor(Color.black);
        g.drawString("A或←左移、S或↓下蹲防御、D或→右移、J或1拳击、K或2腿击、L或3闪避、U或4技能一、I或5技能二",30,130);
        g.setColor(Color.red);
        g.drawString("温馨提示：（若按下按键角色无反应，请检查您的键盘是否开启了英文键盘以及数字小键盘的NUM锁定按钮）",30,170);
        g.setColor(Color.white);
        // 角色血条显示
        g.drawImage(portrait1,35, 199, 42, 42,null,null);
        g.drawImage(portrait2,1275, 199, 42, 42,null,null);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(10.0f));
        g.drawRect(769,199,502,42);
        g.drawRect(79,199,502,42);
        g.setColor(Color.magenta);
        g.drawString(gameUtil.blood1+"%",595,230);
        g.drawString(gameUtil.blood2+"%",700,230);
        g.setColor(Color.pink);
        g.fillRect(80,200,5*gameUtil.blood1,40);
        g.fillRect(770+(500-5*gameUtil.blood2),200,5*gameUtil.blood2,40);

        // 碰撞检测
        boolean peng = baShenAn.getRect().intersects(caoZhiJing.getRect());
        if (peng){
            System.out.println("***********碰撞***********");
        }
        // 优先级检查
        gameUtil.check(peng);
        // 画八神庵
        baShenAn.drawSelf(g,peng);
        // 画草稚京
        caoZhiJing.drawSelf(g,peng);
        // ko
        if (gameUtil.blood1 <= 0 || gameUtil.blood2 <= 0){
            gameUtil.ko(g);
        }

        g.setColor(c);
    }

    /*
     * 初始化窗口
     */
    public void launchFrame() {
        this.setTitle("kof拳皇");
        this.setVisible(true);
        this.setSize(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
        this.setLocation(Constant.GAME_X, Constant.GAME_Y);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });

        new PaintThread().start();	// 启动重画窗口的线程
        addKeyListener(new KeyMonitor());   // 给窗口增加键盘的监听

    }

    /*
     * 反复的重画窗口
     */
    class PaintThread extends Thread {
        public void run() {
            while (true) {
                repaint();
                try {
                    Thread.sleep(55);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*
     * 键盘监视内部类
     */
    class KeyMonitor extends KeyAdapter {

        public void keyPressed(java.awt.event.KeyEvent e) {
            if (gameUtil.ko){
                gameUtil.addDirection(e);
            }
            baShenAn.addDirection(e);
            caoZhiJing.addDirection(e);
        }

        public void keyReleased(java.awt.event.KeyEvent e) {
            baShenAn.minusDirection(e);
            caoZhiJing.minusDirection(e);
        }
    }

    /**
     * <p>双缓冲防止背景的闪烁</p>
     * @author Bosen
     * 2020/11/14 19:36
     */
    private Image offScreenImage = null;

    public void update(Graphics g) {
        if(offScreenImage == null) {
            //这是游戏窗口的宽度和高度
            offScreenImage = this.createImage(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
        }

        Graphics gOff = offScreenImage.getGraphics();
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);
    }

}
