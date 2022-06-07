package com.kof.www;

import java.awt.*;
import java.awt.event.KeyEvent;

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
            case KeyEvent.VK_COMMA:
                stand = false;
                j = true;
                break;
            case KeyEvent.VK_PERIOD:
                stand = false;
                k = true;
                break;
            case KeyEvent.VK_SLASH:
                stand = false;
                l = true;
                break;
            case KeyEvent.VK_SEMICOLON:
                u = true;
                break;
            case KeyEvent.VK_QUOTE:
                i = true;
                break;
        }
    }


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
            case KeyEvent.VK_COMMA:// 1
                j = false;
                break;
            case KeyEvent.VK_PERIOD:// 2
                k = false;
                break;
            case KeyEvent.VK_SLASH:// 3
                l = false;
                break;
            case KeyEvent.VK_SEMICOLON:// 4
                u = false;
                break;
            case KeyEvent.VK_QUOTE:// 5
                i = false;
                break;
        }
    }


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

    }

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

        tempX = x;
        flyCount++;
        try {
            Thread.sleep(55);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

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

