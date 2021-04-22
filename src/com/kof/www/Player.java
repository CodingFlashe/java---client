package com.kof.www;

import java.awt.*;

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

    /*
     * 蹲
     */
    void down(Graphics g);

    /*
     * 后
     */
    void after(Graphics g);

    /*
     * 前
     */
    void before(Graphics g);
    
    /*
     * 挨打
     */
    void beaten(Graphics g);

    /*
     * 拳
     */
    void j(Graphics g);
    
    /*
     * 腿
     */
    void k(Graphics g);

    /*
     * 闪避
     */
    void l(Graphics g);

    /*
     * 技能一
     */
    void u(Graphics g);

    /*
     * 技能二
     */
    void i(Graphics g);
}
