package com.kof.www;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.swing.plaf.TableHeaderUI;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;
import java.io.*;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

public class KofServer extends Frame{
    CaoZhiJing caoZhiJing = new CaoZhiJing(Constant.GAME_WIDTH-Constant.PLAYER_X-Constant.PLAYER_WIDTH,
            Constant.PLAYER_Y, Constant.PLAYER_WIDTH,Constant.PLAYER_HEIGHT); // 草稚京角色
    BaShenAn baShenAn;
    GameUtil gameUtil;
    Image bg = GameUtil.getImage("com/kof/www/images/bg.jpg");// 背景图
    Image portrait1 = GameUtil.getImage("com/kof/www/images/portrait1.png");// 玩家一头像
    Image portrait2 = GameUtil.getImage("com/kof/www/images/portrait2.png");// 玩家二头像
    public void startServer() throws Exception {
        ServerSocket s2=new ServerSocket(10000);
        Socket s=s2.accept();
        PrintWriter pw=new PrintWriter(s.getOutputStream());
        BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
        String data1= JSONObject.toJSONString(caoZhiJing);
        System.out.println("server data1"+data1);
        pw.println(data1);
        pw.flush();
        String data2=br.readLine();
        System.out.println("server data2"+data2);
        baShenAn = JSON.parseObject(data2,BaShenAn.class);  // 草稚京角色
        pw.println(data1);
        pw.flush();
        this.gameUtil = new GameUtil(baShenAn,caoZhiJing);
        this.setTitle("kof拳皇");
        this.setVisible(true);
        this.setSize(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
        this.setLocation(Constant.GAME_X, Constant.GAME_Y);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                System.exit(0);
            }
        });
        new PaintThread1().start();	// 启动重画窗口的线程
        addKeyListener(new KeyMonitor1());   // 给窗口增加键盘的监听
        new WriteThread(pw).start();
        new ReadThread(br).start();
    }
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

    public static void main(String[] args) throws Exception {
        new KofServer().startServer();
    }


    class WriteThread extends Thread {
        PrintWriter pw;
        WriteThread(PrintWriter pw){
            this.pw=pw;
        }
        public void run() {
            Mix2 mix2=new Mix2(caoZhiJing, gameUtil.blood1, gameUtil.blood2);
            while(true){

                String temp=JSONObject.toJSONString(mix2);
                pw.println(temp);
                System.out.println("发送的数据："+temp);
                pw.flush();
            }
        }
    }

    class ReadThread extends Thread{
        BufferedReader br;
        ReadThread(BufferedReader br){
            this.br=br;
        }
        public void run() {
            System.out.println("开始运行");
            String data2="";
            while(true) {
                try {
                    data2 = br.readLine();
                    System.out.println("client data2" + data2);
                    Mix1 temp1=JSON.parseObject(data2,Mix1.class);
                    baShenAn = temp1.baShenAn;
                    //gameUtil.blood1=temp1.blood1;
                    //gameUtil.blood2=temp1.blood2;
                } catch (IOException e) {

                }
            }
        }
    }
    class PaintThread1 extends Thread {
        public void run() {
            while (true) {
                repaint();  //调用update()清除当前显示并再调用paint()方法
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
    class KeyMonitor1 extends KeyAdapter {

        public void keyPressed(java.awt.event.KeyEvent e) {
            if (gameUtil.ko){
                gameUtil.addDirection(e);
            }
            caoZhiJing.addDirection(e);
        }

        public void keyReleased(java.awt.event.KeyEvent e) {
            caoZhiJing.minusDirection(e);
        }
    }

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
