package schelling;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class frame1 extends JFrame {

    JLabel label = new JLabel("请输入矩阵大小：");
    JTextField text = new JTextField();
    JPanel panel = new JPanel();//面板1

    JButton button1 = new JButton("创建");//按钮2

    public frame1()
    {
        this.setSize(600,400);
        init();
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void init() {

        // TODO Auto-generated method stub
        this.add(panel);
        panel.setLayout(null);
        label.setBounds(10, 10, 150, 20);
        panel.add(label);
        text.setBounds(120,10,50,20);
        panel.add(text);
        button1.setBounds(50, 50, 100, 30);
        panel.add(button1);



        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String s = text.getText();
                Integer a = Integer.parseInt(s);
                System.out.println(a);
                dispose();
                new mainFrame2(a);

            }
        });

    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        new frame1();
    }

}
