package schelling;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;



class mainFrame2 extends JFrame

{   JLabel label = new JLabel();
    JLabel label1 = new JLabel();
    JPanel myPanel1 = new JPanel();//面板1
    JPanel myPanel2 =new JPanel();//面板2
    JButton button1 = new JButton("返回");//按钮2
    JButton button2 = new JButton("确定");
    JButton button3 = new JButton("单步搬家");//按钮3
    JButton button4 = new JButton("搬家");
    JButton[][] b;
    Integer a ;
    JSplitPane jSplitPane =new JSplitPane();//设定为左右拆分布局
    public mainFrame2(int a)
    { this.a =a;
        this.setSize(1000,800);
        init(a);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);




    }

    void init(int a)
    {	 createArray c = new createArray(a);
        jSplitPane.setOneTouchExpandable(true);//让分割线显示出箭头
        jSplitPane.setContinuousLayout(true);//操作箭头，重绘图形
        //jSplitPane.setPreferredSize(new Dimension (100,200));
        jSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);//设置分割线方向
        myPanel1.setSize(800, 800);
        myPanel2.setSize(200, 800);
        jSplitPane.setLeftComponent(myPanel1);//布局中添加组件 ，面板1
        jSplitPane.setRightComponent(myPanel2);//添加面板2
        jSplitPane.setDividerSize(1);//设置分割线的宽度
        //jSplitPane.setDividerLocation(100);//设置分割线位于中央
        jSplitPane.setDividerLocation(800);//设定分割线的距离左边的位置
        setContentPane(jSplitPane);
        //pack();
        myPanel2.setLayout(null);
        label.setText("单机一下蓝色");
        label.setBounds(10, 10, 150, 40);
        myPanel2.add(label);
        label1.setText("单机两下红色");
        label1.setBounds(10,30,150,40);
        myPanel2.add(label1);
        button1.setBounds(50, 80, 100, 30);
        button2.setBounds(50,130,100,30);
        button3.setBounds(50,180,100,30);
        button4.setBounds(50,230,100,30);
        myPanel2.add(button1);
        myPanel2.add(button2);
        myPanel2.add(button3);
        myPanel2.add(button4);





        JButton[][] b=new JButton[a][a];
        myPanel1.setLayout(new GridLayout(a,a,4,4));
        for(int i=0;i<a;i++){
            for( int j =0;j<a;j++) {

                b[i][j]=new JButton();
                myPanel1.add(b[i][j]);

                b[i][j].addActionListener(new ActionListener() {


                    public void actionPerformed(ActionEvent e) {


                        JButton button = (JButton) e.getSource();
                        if(button.getBackground() == Color.BLUE) {
                            button.setBackground(Color.RED);
                        }
                        else {button.setBackground(Color.BLUE);
                        }

                    }
                });
            }
        }

 				/* createArray c = new createArray(a);
 				 for(int i=0;i<a;i++){
					  for( int j =0;j<a;j++) {
						  if(b[i][j].getBackground()==Color.BLUE) {
							  c.array[i][j] =1;
						  }else   if(b[i][j].getBackground()==Color.RED) {
							  c.array[i][j] =2;
						  }else {
							  c.array[i][j] =0;
						  }
					  }
					  }

 			*/
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
                new frame1();

            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {



                for(int i=0;i<a;i++){
                    for( int j =0;j<a;j++) {
                        if(b[i][j].getBackground()==Color.BLUE) {
                            c.array[i][j] =1;
                        }else   if(b[i][j].getBackground()==Color.RED) {
                            c.array[i][j] =2;
                        }else {
                            c.array[i][j] =0;
                        }
                    }
                }

                for(int i=0;i<a;i++) {
                    for(int j =0;j<a;j++) {
                        System.out.print(c.array[i][j]);
                    }
                    System.out.println();

                }

            }
        });
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                c.checkNeighbor();
                for(int i =0;i<a;i++) {
                    for(int j = 0;j<a;j++) {
                        if(c.array[i][j]==2)
                        {
                            b[i][j].setBackground(Color.RED);
                        }
                        else if(c.array[i][j]==1) {
                            b[i][j].setBackground(Color.BLUE);
                        }else
                        {
                            b[i][j].setBackground(null);
                        }
                    }
                }

            }
        });

        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for(int i = 0;i<10000;i++) {
                    c.checkNeighbor();}
                for(int i =0;i<a;i++) {
                    for(int j = 0;j<a;j++) {
                        if(c.array[i][j]==2)
                        {
                            b[i][j].setBackground(Color.RED);
                        }
                        else if(c.array[i][j]==1) {
                            b[i][j].setBackground(Color.BLUE);
                        }else
                        {
                            b[i][j].setBackground(null);
                        }
                    }
                }

            }

        });


    }



}




