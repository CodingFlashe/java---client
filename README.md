# 前言
废话不多说，先让大家看看游戏的运行效果动态图：

![](https://img-blog.csdnimg.cn/img_convert/16e446095ef1a6083f221c18bbc04d12.gif)
![](https://img-blog.csdnimg.cn/img_convert/e7c88f51025501093f12f02427043efd.gif)

**游戏设计概念**
Kof拳皇是基于JDK1.8下的awt包各组件编写的双人单机格斗类游戏。参考了97年推出的经典街机游戏《拳皇97》,并对其中的八神庵与草稚京两位角色基础动作和各种技能进行了一定的还原。游戏内设置有操作角色的键位提示,对于角色的基本操作做有详细的说明,但角色多键位配合的组合技能,未做详细的说明,待玩家自己发掘,以使玩家自己发现游戏的彩蛋,增加其对此游戏的趣味度。

---
**游戏参数说明**
游戏运行环境建议在JRE 1.8.0_261或以上版本

---
**游戏玩法**

玩家一操作八神庵角色与玩家二操作的草稚京角色进行格斗，双方通过各种键位攻击对方或者防御对方，最终将对方的血条消耗完即为本局游戏的胜利。KO对方后，如果觉得不够过瘾，玩家可按下回车键再来一局！

该游戏耗时八天完成，制作过程相对简单，没有运用什么复杂的知识，所以接下来让我来给大家讲解整个游戏的制作过程：

---
# 下载角色动作素材
为了还原最真实的八神庵大战草稚京，我们需要下载《拳皇97》的角色动作素材包，大家可以自己从网站搜索下载，也可以通过关注公众号后回复“kof”获取整个游戏的源码和素材。
![](https://img-blog.csdnimg.cn/img_convert/733595b1d44462f5b1971d47d2c884d4.png)
![](https://img-blog.csdnimg.cn/img_convert/fdc589df97814125b9d1b52624fc2b4f.png)
![](https://img-blog.csdnimg.cn/img_convert/c3b3b5239c7403da5c5417872efdfc6e.png)
![](https://img-blog.csdnimg.cn/img_convert/e2183272e0f1685772b3d8fa514a0d16.png)
![](https://img-blog.csdnimg.cn/img_convert/190482bc1eb760ac09a2f8bef1072c62.png)

---
# 创建游戏启动类
**游戏启动类的作用：**

- 角色和背景的初始化

- 键盘监听

- 角色的绘画

- 角色的碰撞检测

**游戏启动类的具体代码如下：**
```java
package com.kof.www;
​
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.WindowAdapter;
​
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
​
    /*
     * 角色初始化
     */
    BaShenAn baShenAn = new BaShenAn(Constant.PLAYER_X,Constant.PLAYER_Y,
            Constant.PLAYER_WIDTH,Constant.PLAYER_HEIGHT);// 八神庵角色
    CaoZhiJing caoZhiJing = new CaoZhiJing(Constant.GAME_WIDTH-Constant.PLAYER_X-Constant.PLAYER_WIDTH,
            Constant.PLAYER_Y, Constant.PLAYER_WIDTH,Constant.PLAYER_HEIGHT);;// 草稚京角色
​
    GameUtil gameUtil = new GameUtil(baShenAn,caoZhiJing);
​
    Image bg = GameUtil.getImage("com/kof/www/images/bg.jpg");// 背景图
    Image portrait1 = GameUtil.getImage("com/kof/www/images/portrait1.png");// 玩家一头像
    Image portrait2 = GameUtil.getImage("com/kof/www/images/portrait2.png");// 玩家二头像
​
    /*
     * 绘画
     */
    @Override
    public void paint(Graphics g) {
        Color c =  g.getColor();
        // 画背景
        g.drawImage(bg, 0, 0, Constant.GAME_WIDTH,Constant.GAME_HEIGHT,
                null,null);
​
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
​
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
​
        g.setColor(c);
    }
​
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
​
        new PaintThread().start();  // 启动重画窗口的线程
        addKeyListener(new KeyMonitor());   // 给窗口增加键盘的监听
​
    }
​
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
​
    /*
     * 键盘监视内部类
     */
    class KeyMonitor extends KeyAdapter {
​
        public void keyPressed(java.awt.event.KeyEvent e) {
            if (gameUtil.ko){
                gameUtil.addDirection(e);
            }
            baShenAn.addDirection(e);
            caoZhiJing.addDirection(e);
        }
​
        public void keyReleased(java.awt.event.KeyEvent e) {
            baShenAn.minusDirection(e);
            caoZhiJing.minusDirection(e);
        }
    }
​
    /**
     * <p>双缓冲防止背景的闪烁</p>
     * @author Bosen
     * 2020/11/14 19:36
     */
    private Image offScreenImage = null;
​
    public void update(Graphics g) {
        if(offScreenImage == null) {
            //这是游戏窗口的宽度和高度
            offScreenImage = this.createImage(Constant.GAME_WIDTH, Constant.GAME_HEIGHT);
        }
​
        Graphics gOff = offScreenImage.getGraphics();
        paint(gOff);
        g.drawImage(offScreenImage, 0, 0, null);
    }
​
}
```

# 创建游戏工具类
游戏工具类GameUtil的主要作用：

- 角色动作图片素材的加载

- 角色血条的减少判断

- 游戏结束的判断

- 角色技能优先级的判断

- 角色对于技能的强度判断

**下面是游戏工具类GameUtil的具体代码：**
```java
package com.kof.www;
​
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
​
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
​
    public GameUtil(BaShenAn baShenAn,CaoZhiJing caoZhiJing) {
        this.blood1 = 100;
        this.blood2 = 100;
        this.ko = false;
        this.baShenAn = baShenAn;
        this.caoZhiJing = caoZhiJing;
    }
​
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
​
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
​
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
​
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
​
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
​
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
​
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
```

# 创建常量类
常量类主要规定角色的初始坐标位置和窗口的大小（常量类具体代码如下）
```java
package com.kof.www;
​
/**
 * <p>常量类</p>
 * @author Bosen
 * 2020/11/14 10:15
 */
public class Constant {
    /*
     * 窗口的宽高
     */
    public static final int GAME_WIDTH = 1350;
    public static final int GAME_HEIGHT = 750;
    /*
     * 窗口坐标
     */
    public static final int GAME_X = 280;
    public static final int GAME_Y = 180;
    /*
     * 角色宽高
     */
    public static final int PLAYER_WIDTH = 150;
    public static final int PLAYER_HEIGHT = 350;
    /*
     * 角色坐标
     */
    public static final int PLAYER_X = 300;
    public static final int PLAYER_Y = 370;
}

创建定义角色的接口
创建一个角色接口Player定义角色的基本动作（具体代码如下）

package com.kof.www;
​
import java.awt.*;
​
/**
 * <p>玩家接口类</p>
 * @author Bosen
 * 2020/11/14 10:26
 */
public interface Player {
    /*
     * 站立
     */
    void stand(Graphics g);
​
    /*
     * 蹲
     */
    void down(Graphics g);
​
    /*
     * 后
     */
    void after(Graphics g);
​
    /*
     * 前
     */
    void before(Graphics g);
    
    /*
     * 挨打
     */
    void beaten(Graphics g);
​
    /*
     * 拳
     */
    void j(Graphics g);
    
    /*
     * 腿
     */
    void k(Graphics g);
​
    /*
     * 闪避
     */
    void l(Graphics g);
​
    /*
     * 技能一
     */
    void u(Graphics g);
​
    /*
     * 技能二
     */
    void i(Graphics g);
}
```
​

# 创建八神庵角色实体类
现在我们正式开始编写具体的角色对象——八神庵，创建类BaShenAn并继承接口Player（具体代码如下）
```java
package com.kof.www;
​
import java.awt.*;
import java.awt.event.KeyEvent;
​
/**
 * <p>角色八神庵：实体类</p>
 * @author Bosen
 * 2020/11/14 10:28
 */
public class BaShenAn implements Player {
    int x,y,width,height;
    int tempW;
    int speed = 10;// 移动的基础速度
    boolean stand = true;// boolean值记录角色状态
    int maxX = 99999;
    long time = 0;// 角色出招时间
    boolean before,after,down,j,k,l,u,i,peng,beaten,fly;
    public BaShenAn(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
​
    /************************************************************************
     * 按下某个键，增加相应的方向
     ************************************************************************/
    public void addDirection(KeyEvent e){
        switch (e.getKeyCode()) {
            case KeyEvent.VK_S:
                stand = false;
                down = true;
                break;
            case KeyEvent.VK_A:
                stand = false;
                after = true;
                break;
            case KeyEvent.VK_D:
                stand = false;
                before = true;
                break;
            case KeyEvent.VK_J:
                stand = false;
                j = true;
                break;
            case KeyEvent.VK_K:
                stand = false;
                k = true;
                break;
            case KeyEvent.VK_L:
                stand = false;
                l = true;
                break;
            case KeyEvent.VK_U:
                u = true;
                break;
            case KeyEvent.VK_I:
                i = true;
                break;
        }
    }
​
​
    /************************************************************************
     * 按下某个键，取消相应的方向
     ************************************************************************/
    public void minusDirection(KeyEvent e){
        clean();
        switch (e.getKeyCode()) {
            case KeyEvent.VK_S:
                down = false;
                break;
            case KeyEvent.VK_A:
                after = false;
                break;
            case KeyEvent.VK_D:
                before = false;
                break;
            case KeyEvent.VK_J:
                j = false;
                break;
            case KeyEvent.VK_K:
                k = false;
                break;
            case KeyEvent.VK_L:
                l = false;
                break;
            case KeyEvent.VK_U:
                u = false;
                break;
            case KeyEvent.VK_I:
                i = false;
                break;
        }
    }
​
​
    /************************************************************************
     * 站立
     ************************************************************************/
    static Image[] standImgs = new Image[18];
    static int standCount;
    static {
        for (int i = 0; i < 18; i++) {
            if (i>8){
                standImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/stand/0-" + (17-i) + ".png");
            }else{
                standImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/stand/0-" + i + ".png");
            }
            standImgs[i].getWidth(null);
        }
    }
    @Override
    public void stand(Graphics g) {
        System.out.println("八神庵：站立");
        if (standCount >= 18) {
            standCount = 0;
        }
        g.drawImage(standImgs[standCount], x, y, width+30,height,null,null);
        tempW = width + 30;
        standCount++;
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
    /************************************************************************
     * 下蹲
     ************************************************************************/
    static Image[] downImgs = new Image[6];
    static int downCount;
    static Image[] downKImgs = new Image[11];
    static int downKCount;
    static {
        for (int i = 0; i < 6; i++) {
            downImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/down/11-" + (i+1) + ".png");
            downImgs[i].getWidth(null);
        }
        for (int i = 0; i < 11; i++) {
            downKImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/downk/0-" + i + ".png");
            downKImgs[i].getWidth(null);
        }
    }
    @Override
    public void down(Graphics g) {
        if (k){
            System.out.println("八神庵：组合技=下蹲+腿击");
            if (downKCount >= 11) {
                downKCount=0;
            }
            if (downKCount >=2 && downKCount <= 5){
                g.drawImage(downKImgs[downKCount], x, y+height/3,
                        width*5/2,height*2/3,null,null);
                tempW = width*5/2;
            }else{
                g.drawImage(downKImgs[downKCount], x, y+height*2/5,
                        width*3/2,height*3/5,null,null);
                tempW = width+50;
            }
            downKCount++;
            try {
                Thread.sleep(55);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
​
        System.out.println("八神庵：下蹲防御");
        if (downCount >= 6) {
            downCount=0;
        }
        g.drawImage(downImgs[downCount], x, Constant.PLAYER_Y+Constant.PLAYER_HEIGHT/2-50,
                width+50,Constant.PLAYER_HEIGHT/2+50,null,null);
        tempW = width + 50;
        downCount++;
        try {
            Thread.sleep(55);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
​
    /************************************************************************
     * 后退
     ************************************************************************/
    static Image[] afterImgs = new Image[9];
    static int afterCount;
    static {
        for (int i = 0; i < 9; i++) {
            afterImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/after/21-" + i + ".png");
            afterImgs[i].getWidth(null);
        }
    }
    @Override
    public void after(Graphics g) {
        System.out.println("八神庵：后退");
        if (afterCount >= 9) {
            afterCount=0;
        }
        g.drawImage(afterImgs[afterCount], x, y, width,height,null,null);
        tempW = width;
        afterCount++;
        if (x>=speed){
            x -= speed;
        }
    }
​
​
    /************************************************************************
     * 前进
     ************************************************************************/
    static Image[] beforeImgs = new Image[9];
    static int beforeCount;
    static {
        for (int i = 0; i < 9; i++) {
            beforeImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/before/21-" + i + ".png");
            beforeImgs[i].getWidth(null);
        }
    }
    @Override
    public void before(Graphics g) {
        System.out.println("八神庵：前进");
        if (!l){
            if (beforeCount >= 9) {
                beforeCount=0;
            }
            g.drawImage(beforeImgs[beforeCount], x, y, width,
                    height,null,null);
            tempW = width;
            beforeCount++;
            if (x<(Constant.GAME_WIDTH-Constant.PLAYER_WIDTH) && x<maxX && !peng){
                x += speed;
            }
        }else{
            l(g);l(g);
            if (x<(Constant.GAME_WIDTH-Constant.PLAYER_WIDTH) && x<maxX && !peng){
                x += 4*speed;
            }
        }
    }
​
​
    /************************************************************************
     * 挨揍
     ************************************************************************/
    static Image[] beatenImgs = new Image[4];
    static int beatenCount;
    static {
        for (int i = 0; i < 4; i++) {
            beatenImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/beaten/0-" + i + ".png");
            beatenImgs[i].getWidth(null);
        }
    }
    @Override
    public void beaten(Graphics g) {
        if (down){
            down(g);return;
        }
        if (beatenCount >= 4) {
            beatenCount=0;
            beaten = false;
        }
        System.out.println("八神庵：挨揍");
        g.drawImage(beatenImgs[beatenCount], x, y, width+80,
                height,null,null);
        tempW = width;
        beatenCount++;
        if (x>=speed){
            x -= speed/3;
        }
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
    /************************************************************************
     * 拳击
     ************************************************************************/
    static Image[] jImgs = new Image[4];
    static int jCount;
    static Image[] beforeJImgs = new Image[8];
    static int beforeJCount;
    static Image[] afterJImgs = new Image[8];
    static int afterJCount;
    static Image[] beforeJImgs2 = new Image[8];
​
    static {
        for (int i = 0; i < 4; i++) {
            jImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/j/1-" + i + ".png");
            jImgs[i].getWidth(null);
        }
        for (int i = 0; i < 8; i++){
            afterJImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/afterj/0-" + i + ".png");
            afterJImgs[i].getWidth(null);
        }
        for (int i = 0; i < 8; i++){
            beforeJImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/beforej/0-" + i + ".png");
            beforeJImgs[i].getWidth(null);
        }
        for (int i = 0; i < 8; i++){
            beforeJImgs2[i] = GameUtil.getImage("com/kof/www/images/bashenan/beforej/1-" + i + ".png");
            beforeJImgs2[i].getWidth(null);
        }
    }
    @Override
    public void j(Graphics g) {
        time = System.currentTimeMillis();
        if (after){
            System.out.println("八神庵：组合技=后退+拳击");
            if (afterJCount >= 8) {
                afterJCount=0;
            }
            g.drawImage(afterJImgs[afterJCount], x+30, y, width*3/2,height,null,null);
            tempW = width*3/2;
            afterJCount++;
            try {
                Thread.sleep(55);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        if (before){
            System.out.println("八神庵：组合技=前进+拳击");
            if (beforeJCount >= 8) {
                beforeJCount=0;
            }
            g.drawImage(beforeJImgs[beforeJCount], x, y, width+50,height,null,null);
            g.drawImage(beforeJImgs2[beforeJCount], x+120+60*beforeJCount,
                    y+3*height/4-25, width+50,height/4+35,null,null);
            tempW = 60*beforeJCount+290;
            beforeJCount++;
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        System.out.println("八神庵：拳击");
        if (jCount >= 4) {
            jCount=0;
        }
        if (jCount==2 || jCount==3){
            g.drawImage(jImgs[jCount], x, y, width+150,height,null,null);
            tempW = width + 150;
        }else{
            g.drawImage(jImgs[jCount], x, y, width+80,height,null,null);
            tempW = width + 80;
        }
        jCount++;
        try {
            Thread.sleep(55);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
​
    /************************************************************************
     * 腿击
     ************************************************************************/
    static Image[] kImgs = new Image[4];
    static int kCount;
    static Image[] afterKImgs = new Image[10];
    static int afterKCount;
    static Image[] beforeKImgs = new Image[9];
    static int beforeKCount;
    static {
        for (int i = 0; i < 4; i++) {
            kImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/k/0-" + i + ".png");
            kImgs[i].getWidth(null);
        }
        for (int i = 0; i < 10; i++) {
            afterKImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/afterk/0-" + i + ".png");
            afterKImgs[i].getWidth(null);
        }
        for (int i = 0; i < 9; i++) {
            beforeKImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/beforek/0-" + i + ".png");
            beforeKImgs[i].getWidth(null);
        }
    }
    @Override
    public void k(Graphics g) {
        time = System.currentTimeMillis();
        if (after){
            System.out.println("八神庵：组合技=后退+腿击");
            if (afterKCount >= 10) {
                afterKCount=0;
            }
            if (afterKCount >= 0 && afterKCount <= 2){
                g.drawImage(afterKImgs[afterKCount], x+35, y-50, width+30,height+60,null,null);
                tempW = 0;
            }
            if (afterKCount >= 3 && afterKCount <= 7){
                g.drawImage(afterKImgs[afterKCount], x-10, y-10, width+120,height+10,null,null);
                tempW = width*3/2;
            }
            if (afterKCount >7){
                g.drawImage(afterKImgs[afterKCount], x+35, y-40, width,height+40,null,null);
                tempW = 0;
            }
            afterKCount++;
            try {
                Thread.sleep(55);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        if (before){
            System.out.println("八神庵：组合技=前进+腿击");
            if (beforeKCount >= 9) {
                beforeKCount=0;
            }
            if (beforeKCount == 0 || beforeKCount == 7 || beforeKCount == 8){
                g.drawImage(beforeKImgs[beforeKCount], x, y, width+50,height,null,null);
                tempW = 0;
            }else{
                g.drawImage(beforeKImgs[beforeKCount], x, y, width*3/2+30,height,null,null);
                tempW = width*3/2;
            }
            if (x<=Constant.GAME_WIDTH-2*Constant.PLAYER_WIDTH-speed && !peng){
                x += speed;
            }
            beforeKCount++;
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        System.out.println("八神庵：腿击");
        if (kCount >= 4) {
            kCount=0;
        }
        if (kCount==0 || kCount==1){
            g.drawImage(kImgs[kCount], x, y, width+150,height,null,null);
            tempW = width + 135;
        }else{
            g.drawImage(kImgs[kCount], x, y, width+75,height,null,null);
            tempW = width + 75;
        }
        kCount++;
        try {
            Thread.sleep(55);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
​
    /************************************************************************
     * 闪避
     ************************************************************************/
    static Image[] lImgs1 = new Image[9];
    static Image[] lImgs2 = new Image[1];
    static int lCount1;
    static int lCount2;
    static {
        for (int i = 0; i < 9; i++) {
            lImgs1[i] = GameUtil.getImage("com/kof/www/images/bashenan/l/1-" + i + ".png");
            lImgs1[i].getWidth(null);
        }
        for (int i = 0; i < 1; i++) {
            lImgs2[i] = GameUtil.getImage("com/kof/www/images/bashenan/l/9-" + i + ".png");
            lImgs2[i].getWidth(null);
        }
    }
    @Override
    public void l(Graphics g) {
        stand = false;
        if (after){
            System.out.println("八神庵：向后闪避");
            if (lCount1 >= 9) {
                lCount1=0;
            }
            g.drawImage(lImgs1[lCount1], x, y+height/2, width+30,height/2,null,null);
            lCount1++;
            if (x >= 3*speed){
                x -= 3*speed;
            }
        }else{
            System.out.println("八神庵：向前闪避");
            if (lCount2 >= 1) {
                lCount2=0;
            }
            g.drawImage(lImgs2[lCount2], x, y+height/2, 2*width,height/2,null,null);
            tempW = width*2-100;
            lCount2++;
            if (x<(Constant.GAME_WIDTH-Constant.PLAYER_WIDTH-20) && !peng){
                x += 4*speed;
            }
        }
    }
​
    /************************************************************************
     * 技能一
     ************************************************************************/
    static Image[] uImgs1 = new Image[18];
    static int uCount;
    static Image[] uImgs2 = new Image[18];
    static {
        for (int i = 0; i < 18; i++) {
            if (i>=13){
                uImgs1[i] = GameUtil.getImage("com/kof/www/images/bashenan/u/5-" + 13 + ".png");
            }else{
                uImgs1[i] = GameUtil.getImage("com/kof/www/images/bashenan/u/5-" + (i+1) + ".png");
​
            }
            uImgs2[i] = GameUtil.getImage("com/kof/www/images/bashenan/u/6-" + i + ".png");
            uImgs2[i].getWidth(null);
            uImgs1[i].getWidth(null);
        }
    }
    @Override
    public void u(Graphics g) {
        time = System.currentTimeMillis();
        System.out.println("八神庵：技能一");
        if (uCount < 18) {
            g.drawImage(uImgs1[uCount], x, y, width*3/2,height,null,null);
            g.drawImage(uImgs2[uCount], x+2*width, y-(3*height/2-height), 2*width,3*height/2,null,null);
            tempW = width*3;
            uCount++;
        }else{
            uCount=0;
        }
        try {
            Thread.sleep(65);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
​
    }
​
    /************************************************************************
     * 技能二
     ************************************************************************/
    static Image[] iImgs = new Image[18];
    static int iCount;
    static {
        for (int i = 0; i < 18; i++) {
            iImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/i/4-" + (i+1) + ".png");
            iImgs[i].getWidth(null);
        }
    }
    @Override
    public void i(Graphics g) {
        System.out.println("八神庵：技能二");
        if (iCount < 18) {
            g.drawImage(iImgs[iCount],x+width*3/2, y-height+10, width*3/2,
                    2*height,null,null);
            tempW = 3*width-30;
            iCount++;
        }else{
            iCount=0;
        }
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
    /************************************************************************
     * 被击飞
     ************************************************************************/
    static Image[] flyImgs = new Image[9];
    static int flyCount;
    static {
        for (int i = 0; i < 9; i++) {
            flyImgs[i] = GameUtil.getImage("com/kof/www/images/bashenan/fly/0-" + i + ".png");
            flyImgs[i].getWidth(null);
        }
    }
    public void fly(Graphics g) {
        time = System.currentTimeMillis();
        if (flyCount >= 9) {
            flyCount=0;fly = false;stand(g);return;
        }
        if (flyCount <= 1){
            g.drawImage(flyImgs[flyCount],x-width, y+height/3-50, 2*width+80,
                    height*2/3+50,null,null);
            if (x>speed*8){
                x -= speed*8;
            }
        }
        if (flyCount >= 2 && flyCount <= 6){
            g.drawImage(flyImgs[flyCount],x-width+20, y+height/3+20, 2*width-20,
                    height*2/3-20,null,null);
        }
        if (flyCount >= 7 && flyCount <= 8){
            g.drawImage(flyImgs[flyCount],x-width+100, y+80, 2*width-80,
                    height-80,null,null);
        }
        tempW = width;
        flyCount++;
        try {
            Thread.sleep(55);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
    /**
     * <p>出招控制器</p>
     * @author Bosen
     * 2020/11/14 21:10
     */
    public void drawSelf(Graphics g,boolean peng) {
        this.peng = peng;
        if (beaten || fly){
            if (fly){
                fly(g);
            }else{
                beaten(g);
            }
        }else{
            if (stand){
                if (i && after && !down){
                    stand(g);i(g);
                    return;
                }
                if (i && !down){
                    stand(g);i(g);
                    return;
                }
                if(u && !down){
                    u(g);
                    return;
                }
                stand(g);
            }else{
                if (before && !down && !k){
                    after = false;
                    if (l){
                        l(g);l(g);return;
                    }
                    if (j){
                        j(g);return;
                    }
                    if (u){
                        u(g);return;
                    }
                    if (i){
                        stand(g);
                        i(g);return;
                    }
                    before(g);
                    return;
                }
                if (after && !down && !j && !k){
                    before = false;
                    if (l){
                        l(g);l(g);return;
                    }
                    if (u){
                        u(g);return;
                    }
                    if (i){
                        stand(g);
                        i(g);return;
                    }
                    after(g);
                    return;
                }
                if (down){
                    before = false;after = false;
                    down(g);
                    return;
                }
                if (j && !down){
                    j(g);
                    return;
                }
                if (k && !down){
                    k(g);
                    return;
                }
                if (l && !down){
                    l(g);l(g);
                    return;
                }
                stand(g);
                stand = true;
            }
        }
    }
​
    /**
     * <p>返回物体所在的矩形。便于后续的碰撞检测</p>
     * @author Bosen
     * 2020/11/15 21:17
     *
     * @return Rectangle
     */
    public Rectangle  getRect(){
        return  new Rectangle(x, y, tempW, height);
    }
​
    /**
     * <p>技能计数器归零</p>
     * @author Bosen
     * 2020/11/17 21:06
     */
    public void clean(){
        // 基础技能归零
        lCount2=lCount1=uCount=jCount=kCount=iCount=0;
        // 后退组合技归零
        afterCount=afterJCount=afterKCount=0;
        // 出招时间归零
        time = 0;
        // 下蹲组合技归零
        downCount=downKCount=0;
        // 前进组合技归零
        beforeCount=beforeKCount=beforeJCount=0;
    }
}
```

# 创建草稚京角色实体类
草稚京角色实体类代码与八神庵的实体类类似（具体代码如下）
```java
package com.kof.www;
​
import java.awt.*;
import java.awt.event.KeyEvent;
​
/**
 * <p>角色草稚京实体类</p>
 * @author Bosen
 * 2020/11/14 10:30
 */
public class CaoZhiJing implements Player{
    int x,y,width,height;
    int tempX;
    long time = 0;// 角色出招时间
    int speed = 10;// 移动的基础速度
    boolean stand = true;// boolean值记录角色状态
    boolean before,after,down,j,k,l,u,i,peng,beaten,fly;
    public CaoZhiJing(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
​
    /************************************************************************
     * 按下某个键，增加相应的方向
     ************************************************************************/
    public void addDirection(KeyEvent e){
        switch (e.getKeyCode()) {
            case 40:
                stand = false;
                down = true;
                break;
            case 37:
                stand = false;
                after = true;
                break;
            case 39:
                stand = false;
                before = true;
                break;
            case KeyEvent.VK_NUMPAD1:
                stand = false;
                j = true;
                break;
            case KeyEvent.VK_NUMPAD2:
                stand = false;
                k = true;
                break;
            case KeyEvent.VK_NUMPAD3:
                stand = false;
                l = true;
                break;
            case KeyEvent.VK_NUMPAD4:
                u = true;
                break;
            case KeyEvent.VK_NUMPAD5:
                i = true;
                break;
        }
    }
​
​
    /************************************************************************
     * 按下某个键，取消相应的方向
     ************************************************************************/
    public void minusDirection(KeyEvent  e){
        clean();
        switch (e.getKeyCode()) {
            case 40:// 下
                down = false;
                break;
            case 39:// 右
                after = false;
                before = false;
                break;
            case 37:// 左
                after = false;
                before = false;
                break;
            case KeyEvent.VK_NUMPAD1:// 1
                j = false;
                break;
            case KeyEvent.VK_NUMPAD2:// 2
                k = false;
                break;
            case KeyEvent.VK_NUMPAD3:// 3
                l = false;
                break;
            case KeyEvent.VK_NUMPAD4:// 4
                u = false;
                break;
            case KeyEvent.VK_NUMPAD5:// 5
                i = false;
                break;
        }
    }
​
​
    /************************************************************************
     * 站立
     ************************************************************************/
    static Image[] standImgs = new Image[10];
    static int standCount;
    static {
        for (int i = 0; i < 10; i++) {
            standImgs[i] = GameUtil.getImage("com/kof/www/images/caozhijing/stand/0-" + i + ".png");
            standImgs[i].getWidth(null);
        }
    }
    @Override
    public void stand(Graphics g) {
        System.out.println("草稚京：站立");
        if (standCount >= 10) {
            standCount = 0;
        }
        g.drawImage(standImgs[standCount], x, y, width+20,height,null,null);
        tempX = x;
        standCount++;
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
    /************************************************************************
     * 下蹲
     ************************************************************************/
    static Image downImgs;
    static int downCount;
    static Image[] downKImgs = new Image[9];
    static int downKCount;
    static {
        downImgs = GameUtil.getImage("com/kof/www/images/caozhijing/down/6-2.png");
        downImgs.getWidth(null);
        for (int i = 0; i < 9; i++) {
            downKImgs[i] = GameUtil.getImage("com/kof/www/images/caozhijing/downk/0-" + i + ".png");
            downKImgs[i].getWidth(null);
        }
    }
    @Override
    public void down(Graphics g) {
        if (k){
            System.out.println("草稚京：组合技=下蹲+腿击");
            if (downKCount >= 9) {
                downKCount=0;
            }
            if (downKCount <= 3){
                g.drawImage(downKImgs[downKCount], x, Constant.PLAYER_Y+Constant.PLAYER_HEIGHT/2-80,
                        width+80,Constant.PLAYER_HEIGHT/2+80,null,null);
                tempX = x;
            }
            if (downKCount == 4){
                g.drawImage(downKImgs[downKCount], x-150, y+height*2/3-150,
                        width*2+100,height/3+150,null,null);
                tempX = x-120;
            }
            if (downKCount > 4 && downKCount <=8){
                g.drawImage(downKImgs[downKCount], x, Constant.PLAYER_Y+Constant.PLAYER_HEIGHT/2-130,
                        width+130,Constant.PLAYER_HEIGHT/2+130,null,null);
                tempX = x;
            }
            downKCount++;
            try {
                Thread.sleep(55);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        System.out.println("草稚京：下蹲防御");
        g.drawImage(downImgs, x, Constant.PLAYER_Y+Constant.PLAYER_HEIGHT/2-40,
                width+20,Constant.PLAYER_HEIGHT/2+50,null,null);
        tempX = x;
        downCount++;
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
​
    /************************************************************************
     * 后退
     ************************************************************************/
    static Image[] afterImgs = new Image[11];
    static int afterCount;
    static {
        for (int i = 0; i < 11; i++) {
            afterImgs[i] = GameUtil.getImage("com/kof/www/images/caozhijing/after/0-" + i + ".png");
            afterImgs[i].getWidth(null);
        }
    }
    @Override
    public void after(Graphics g) {
        System.out.println("草稚京：前进");
        if (afterCount >= 11) {
            afterCount=0;
        }
        g.drawImage(afterImgs[afterCount], x, y, width,height,null,null);
        tempX = x;
        afterCount++;
        if (x>=speed && !peng){
            x -= speed;
        }
    }
​
​
    /************************************************************************
     * 前进
     ************************************************************************/
    static Image[] beforeImgs = new Image[11];
    static int beforeCount;
    static {
        for (int i = 0; i < 11; i++) {
            beforeImgs[i] = GameUtil.getImage("com/kof/www/images/caozhijing/before/0-" + i + ".png");
            beforeImgs[i].getWidth(null);
        }
    }
    @Override
    public void before(Graphics g) {
        System.out.println("草稚京：后退");
        if (!l){
            if (beforeCount >= 11) {
                beforeCount=0;
            }
            g.drawImage(beforeImgs[beforeCount], x, y, width,
                    height,null,null);
            tempX = x;
            beforeCount++;
            if (x<(Constant.GAME_WIDTH-Constant.PLAYER_WIDTH)){
                x += speed;
            }
        }else{
            l(g);l(g);
            if (x<(Constant.GAME_WIDTH-Constant.PLAYER_WIDTH)){
                x += 4*speed;
            }
        }
    }
​
​
    /************************************************************************
     * 挨揍
     ************************************************************************/
    static Image[] beatenImgs = new Image[5];
    static int beatenCount;
    static {
        for (int i = 0; i < 5; i++) {
            beatenImgs[i] = GameUtil.getImage("com/kof/www/images/caozhijing/beaten/0-" + i + ".png");
            beatenImgs[i].getWidth(null);
        }
    }
    @Override
    public void beaten(Graphics g) {
        if (down){
            down(g);return;
        }
        if (beatenCount >= 5) {
            beatenCount=0;
            beaten = false;
        }
        System.out.println("草稚京：挨揍");
        g.drawImage(beatenImgs[beatenCount], x, y, width+80,
                height,null,null);
        tempX = x;
        beatenCount++;
        if (x<(Constant.GAME_WIDTH-Constant.PLAYER_WIDTH)){
            x += speed/3;
        }
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
    /************************************************************************
     * 拳击
     ************************************************************************/
    static Image[] jImgs = new Image[2];
    static int jCount;
    static Image[] beforeJImgs = new Image[6];
    static int beforeJCount;
    static Image[] afterJImgs = new Image[7];
    static int afterJCount;
    static Image afterJImg = GameUtil.getImage("com/kof/www/images/caozhijing/afterj/1-1.png");
​
    static {
        for (int i = 0; i < 2; i++) {
            jImgs[i] = GameUtil.getImage("com/kof/www/images/caozhijing/j/0-" + i + ".png");
            jImgs[i].getWidth(null);
        }
        for (int i = 0; i < 6; i++){
            beforeJImgs[i] = GameUtil.getImage("com/kof/www/images/caozhijing/beforej/0-" + i + ".png");
            beforeJImgs[i].getWidth(null);
        }
        for (int i = 0; i < 7; i++){
            afterJImgs[i] = GameUtil.getImage("com/kof/www/images/caozhijing/afterj/0-" + i + ".png");
            afterJImgs[i].getWidth(null);
        }
    }
    @Override
    public void j(Graphics g) {
        time = System.currentTimeMillis();
        if (before){
            System.out.println("草稚京：组合技=前进+拳击");
            if (beforeJCount >= 6) {
                beforeJCount=0;
            }
            g.drawImage(beforeJImgs[beforeJCount], x-80, y, width+80,height,null,null);
            tempX = x-80;
            beforeJCount++;
            try {
                Thread.sleep(55);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        if(after){
            System.out.println("草稚京：组合技=后退+拳击");
            if (afterJCount >= 7) {
                afterJCount=0;
            }
            g.drawImage(afterJImgs[afterJCount], x-80, y+30, width*2-30,height-30,null,null);
            if (afterJCount>=3){
                g.drawImage(afterJImg, x-180, y-30, width,height-30,null,null);
            }
            tempX = x-180;
            afterJCount++;
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        if (jCount >= 2) {
            jCount=0;
        }
        System.out.println("草稚京：拳击");
        g.drawImage(jImgs[jCount], x-80, y, width+100,height,null,null);
        tempX = x-100;
        jCount++;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
​
    /************************************************************************
     * 腿击
     ************************************************************************/
    static Image[] kImgs = new Image[4];
    static int kCount;
    static Image[] afterKImgs = new Image[5];
    static int afterKCount;
    static Image[] beforeKImgs = new Image[10];
    static int beforeKCount;
    static {
        for (int i = 0; i < 4; i++) {
            kImgs[i] = GameUtil.getImage("com/kof/www/images/caozhijing/k/0-" + i + ".png");
            kImgs[i].getWidth(null);
        }
        for (int i = 0; i < 5; i++) {
            afterKImgs[i] = GameUtil.getImage("com/kof/www/images/caozhijing/afterk/0-" + i + ".png");
            afterKImgs[i].getWidth(null);
        }
        for (int i = 0; i < 10; i++) {
            beforeKImgs[i] = GameUtil.getImage("com/kof/www/images/caozhijing/beforek/2-" + i + ".png");
            beforeKImgs[i].getWidth(null);
        }
    }
    @Override
    public void k(Graphics g) {
        time = System.currentTimeMillis();
        if (after){
            System.out.println("草稚京：组合技=前进+腿击");
            if (afterKCount >= 5) {
                afterKCount=0;
            }
            if (afterKCount>=0 && afterKCount<=2){
                g.drawImage(afterKImgs[afterKCount], x-50, y, width+50,height,null,null);
                tempX = x;
            }
            if (afterKCount==3){
                g.drawImage(afterKImgs[afterKCount], x-width, y-30, 2*width,height-30,null,null);
                tempX = x-width;
            }
            if (afterKCount==4){
                g.drawImage(afterKImgs[afterKCount], x-width-50, y-30, 2*width-45,height-20,null,null);
                tempX = x-width-30;
            }
            afterKCount++;
            if (x>speed+2*Constant.PLAYER_WIDTH-50 && !peng){
                x -= speed;
            }
            try {
                Thread.sleep(65);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        if (before){
            System.out.println("草稚京：组合技=后退+腿击");
            if (beforeKCount >= 10) {
                beforeKCount=0;
            }
            if(beforeKCount <= 3){
                g.drawImage(beforeKImgs[beforeKCount], x-30, y, width+10,height,null,null);
                tempX = x;
            }
            if (beforeKCount == 4 || beforeKCount == 5){
                g.drawImage(beforeKImgs[beforeKCount], x-30, y-80, width-10,height+80,null,null);
                tempX = x;
            }
            if (beforeKCount > 5){
                g.drawImage(beforeKImgs[beforeKCount], x-width+50, y-10, width*3/2-30,height+10,null,null);
                tempX = x-width+50;
            }
            beforeKCount++;
            try {
                Thread.sleep(40);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return;
        }
        System.out.println("草稚京：腿击");
        if (kCount >= 4) {
            kCount=0;
        }
        if (kCount==0 || kCount==1){
            g.drawImage(kImgs[kCount], x-50, y, width+50,height,null,null);
            tempX = x;
        }
        if (kCount==2 || kCount==3){
            g.drawImage(kImgs[kCount], x-150, y, width+150,height,null,null);
            tempX = x-100;
        }
        kCount++;
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
​
    /************************************************************************
     * 闪避
     ************************************************************************/
    static Image[] lImgs1 = new Image[6];
    static Image[] lImgs2 = new Image[6];
    static int lCount1;
    static int lCount2;
    static {
        for (int i = 0; i < 6; i++) {
            lImgs1[i] = GameUtil.getImage("com/kof/www/images/caozhijing/l/0-" + i + ".png");
            lImgs1[i].getWidth(null);
        }
        for (int i = 0; i < 6; i++) {
            lImgs2[i] = GameUtil.getImage("com/kof/www/images/caozhijing/l/1-" + i + ".png");
            lImgs2[i].getWidth(null);
        }
    }
    @Override
    public void l(Graphics g) {
        stand = false;
        if (before){
            System.out.println("草稚京：向后闪避");
            if (lCount1 >= 6) {
                lCount1=0;
            }
            g.drawImage(lImgs1[lCount1], x, y+height/2, width+30,height/2,null,null);
            tempX = x-30;
            lCount1++;
            if (x<(Constant.GAME_WIDTH-Constant.PLAYER_WIDTH)){
                x += 3*speed;
            }
        }else{
            System.out.println("草稚京：向前闪避");
            if (lCount2 >= 6) {
                lCount2=0;
            }
            g.drawImage(lImgs2[lCount2], x, y+height/2, width+30,height/2,null,null);
            tempX = x-30;
            lCount2++;
            if (x >= 3*speed && !peng){
                x -= 4*speed;
            }
        }
    }
​
    /************************************************************************
     * 技能一
     ************************************************************************/
    static Image[] uImgs1 = new Image[10];
    static int uCount;
    static Image[] uImgs2 = new Image[10];
    static {
        for (int i = 0; i < 10; i++) {
            uImgs1[i] = GameUtil.getImage("com/kof/www/images/caozhijing/u/0-" + i + ".png");
            uImgs1[i].getWidth(null);
        }
        for (int i = 0; i < 10; i++) {
            uImgs2[i] = GameUtil.getImage("com/kof/www/images/caozhijing/u/1-" + i + ".png");
            uImgs2[i].getWidth(null);
        }
    }
    @Override
    public void u(Graphics g) {
        time = System.currentTimeMillis();
        System.out.println("草稚京：技能一");
        if (uCount < 10) {
            g.drawImage(uImgs2[uCount], x, y, width*3/2,height,null,null);
            g.drawImage(uImgs1[uCount], x-2*width+30, y+height-height/2-180,
                    2*width,height-30,null,null);
            tempX = x-2*width+50;
            uCount++;
        }else{
            uCount=0;
        }
        try {
            Thread.sleep(65);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
​
    }
​
    /************************************************************************
     * 技能二
     ************************************************************************/
    static Image[] iImgs = new Image[17];
    static int iCount;
    static {
        for (int i = 0; i < 17; i++) {
            iImgs[i] = GameUtil.getImage("com/kof/www/images/caozhijing/i/0-" + i + ".png");
            iImgs[i].getWidth(null);
        }
    }
    @Override
    public void i(Graphics g) {
        time = System.currentTimeMillis();
        System.out.println("草稚京：技能二");
        if (iCount < 17) {
            if (iCount<=4){
                g.drawImage(iImgs[iCount],x-width*2, y-height+75, width*3/2,
                        2*height,null,null);
            }else{
                g.drawImage(iImgs[iCount],x-width*2, y-height+10, width*3/2,
                        2*height,null,null);
            }
            tempX = x-width-width*3/2+80;
            iCount++;
        }else{
            iCount=0;
        }
        try {
            Thread.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
    /************************************************************************
     * 被击飞
     ************************************************************************/
    static Image[] flyImgs = new Image[9];
    static int flyCount;
    static {
        for (int i = 0; i < 9; i++) {
            flyImgs[i] = GameUtil.getImage("com/kof/www/images/caozhijing/fly/0-" + i + ".png");
            flyImgs[i].getWidth(null);
        }
    }
    public void fly(Graphics g) {
​
        if (flyCount >= 9) {
            flyCount=0;fly = false;stand(g);return;
        }
        if (flyCount == 0 || flyCount == 8 || flyCount == 5){
            g.drawImage(flyImgs[flyCount],x, y+height/4, width+100,
                    height*3/4,null,null);
        }
        if (flyCount >= 1 && flyCount <= 3){
            g.drawImage(flyImgs[flyCount],x, y+height/2+50, 2*width+50,
                    height/2-50,null,null);
        }
        if (flyCount == 4){
            g.drawImage(flyImgs[flyCount],x, y+height/3, height*2/3+50,
                    height*2/3,null,null);
        }
        if (flyCount == 6){
            g.drawImage(flyImgs[flyCount],x, y+height/3, width+50+50,
                    height*2/3,null,null);
        }
        if (flyCount == 7){
            g.drawImage(flyImgs[flyCount],x, y-50, width,
                    height+50,null,null);
        }
        if(flyCount >= 0 && flyCount <= 3 && x<Constant.GAME_WIDTH-width-8*speed){
            x += 8*speed;
        }
​
        tempX = x;
        flyCount++;
        try {
            Thread.sleep(55);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
​
    /**
     * <p>出招控制器</p>
     * @author Bosen
     * 2020/11/14 21:10
     */
    public void drawSelf(Graphics g,boolean peng) {
        this.peng = peng;
        if (beaten || fly){
            if (fly){
                fly(g);
            }else{
                beaten(g);
            }
        }else{
            if (stand) {
                if (i && !down) {
                    stand(g);i(g);
                    return;
                }
                if (u && !down) {
                    u(g);
                    return;
                }
                stand(g);
            } else {
                if (before && !down && !j && !k) {
                    after = false;
                    if (l) {
                        l(g);l(g);return;
                    }
                    if (i){
                        stand(g);
                        i(g);return;
                    }
                    if (u){
                        u(g);return;
                    }
                    before(g);
                    return;
                }
                if (after && !down && !j && !k) {
                    before = false;
                    if (l) {
                        l(g);l(g);return;
                    }
                    if (i){
                        stand(g);
                        i(g);return;
                    }
                    if (u){
                        u(g);return;
                    }
                    after(g);
                    return;
                }
                if (down) {
                    before = false;after = false;
                    down(g);
                    return;
                }
                if (j && !down) {
                    j(g);
                    return;
                }
                if (k && !down) {
                    k(g);
                    return;
                }
                if (l && !down) {
                    l(g);l(g);
                    return;
                }
                stand(g);
                stand = true;
            }
        }
    }
​
    /**
     * <p>返回物体所在的矩形。便于后续的碰撞检测</p>
     * @author Bosen
     * 2020/11/15 21:17
     *
     * @return Rectangle
     */
    public Rectangle  getRect(){
        return  new Rectangle(tempX, y, width, height);
    }
​
    /**
     * <p>技能计数器归零</p>
     * @author Bosen
     * 2020/11/17 21:06
     */
    public void clean(){
        // 基础技能归零
        lCount2=lCount1=uCount=jCount=kCount=iCount=0;
        // 后退组合技归零
        afterCount=afterJCount=afterKCount=0;
        // 出招时间归零
        time = 0;
        // 下蹲组合技归零
        downCount=downKCount=0;
        // 前进组合技归零
        beforeCount=beforeKCount=beforeJCount=0;
    }
}
```

# 总结
该游戏代码已经托管在gitee中，关注公众号回复“kof”即可获取对应的gitee仓库地址下载游戏源码和发行版。发行版打包为jar类型，运行需要jre环境，推荐8或以上版本。（具体操作如下）

![](https://img-blog.csdnimg.cn/img_convert/77f05109cbd54897cfc460881b9df3b3.png)
