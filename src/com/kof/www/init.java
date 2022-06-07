package com.kof.www;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import javax.swing.JPanel;

public class init {

    private JFrame frame = new JFrame("背景图片测试");

    private JPanel imagePanel;

    private ImageIcon background;

    public static void main(String[] args) {

        new init();

    }

    public init() {

        background = new ImageIcon("D:\\JAVA\\kof\\src\\com\\kof\\www\\images\\12.jpg");// 背景图片

        JLabel label = new JLabel(background);// 把背景图片显示在一个标签里面

        label.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());

        imagePanel = (JPanel) frame.getContentPane();

        imagePanel.setOpaque(false);

        imagePanel.setLayout(new FlowLayout());
        JButton b1 = new JButton("单机游戏");
        b1.setBounds(400,100,100,50);
        b1.setContentAreaFilled(false);
        b1.setFocusPainted(false);
        b1.setForeground(Color.BLUE);
        //点击时打开单机游戏
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new KofGame().launchFrame();
                frame.dispose();
                frame.setVisible(false);
            }
        });

        JButton b2 = new JButton("联机游戏");
        b2.setBounds(400,200,100,50);
        b2.setContentAreaFilled(false);
        b2.setFocusPainted(false);
        b2.setForeground(Color.BLUE);
        b2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                frame.setVisible(false);
            }
        });
        JButton b3 = new JButton("结束游戏");
        b3.setBounds(400,300,100,50);
        b3.setContentAreaFilled(false);
        b3.setFocusPainted(false);
        b3.setForeground(Color.BLUE);
        //点击后退出游戏
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
                frame.setVisible(false);
            }
        });
        imagePanel.setLayout(null);
        imagePanel.add(b1);
        imagePanel.add(b2);
        imagePanel.add(b3);


        frame.getLayeredPane().setLayout(null);

// 把背景图片添加到分层窗格的最底层作为背景

        frame.getLayeredPane().add(label, new Integer(Integer.MIN_VALUE));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(background.getIconWidth(), background.getIconHeight());

        frame.setResizable(false);

        frame.setVisible(true);

    }

}
