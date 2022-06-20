package com.kof.www;

import java.awt.*;
import java.awt.event.KeyEvent;

public class BaShenAn implements Player {
    public int x,y,width,height;
    public int tempW;
    public int speed = 10;// 移动的基础速度
    public boolean stand = true;// boolean值记录角色状态
    public int maxX = 99999;
    public long time = 0;// 角色出招时间
    public boolean before,after,down,j,k,l,u,i,peng,beaten,fly;
    public BaShenAn(){

    }
    public BaShenAn(int x, int y, int width, int height) {
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
        //t.println("八神庵：站立");
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
            //System.out.println("八神庵：组合技=下蹲+腿击");
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

        //System.out.println("八神庵：下蹲防御");
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
        //System.out.println("八神庵：后退");
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
        //System.out.println("八神庵：前进");
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
        //System.out.println("八神庵：挨揍");
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
            //System.out.println("八神庵：组合技=后退+拳击");
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
            //System.out.println("八神庵：组合技=前进+拳击");
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
        //System.out.println("八神庵：拳击");
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
            //System.out.println("八神庵：组合技=后退+腿击");
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
            //System.out.println("八神庵：组合技=前进+腿击");
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
        //System.out.println("八神庵：腿击");
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
            //System.out.println("八神庵：向后闪避");
            if (lCount1 >= 9) {
                lCount1=0;
            }
            g.drawImage(lImgs1[lCount1], x, y+height/2, width+30,height/2,null,null);
            lCount1++;
            if (x >= 3*speed){
                x -= 3*speed;
            }
        }else{
            //System.out.println("八神庵：向前闪避");
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

            }
            uImgs2[i] = GameUtil.getImage("com/kof/www/images/bashenan/u/6-" + i + ".png");
            uImgs2[i].getWidth(null);
            uImgs1[i].getWidth(null);
        }
    }
    @Override
    public void u(Graphics g) {
        time = System.currentTimeMillis();
        //System.out.println("八神庵：技能一");
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

    }

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
        //System.out.println("八神庵：技能二");
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
