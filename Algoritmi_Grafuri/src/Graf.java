//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import javax.swing.*;
import java.awt.*;


public class Graf {
    public Graf() {
    }

    private static void initUI() {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        JFrame f = new JFrame("Algoritmica Grafurilor");
        f.setLayout(new BorderLayout());
        f.setDefaultCloseOperation(3);
        f.add(new MyPanel(), BorderLayout.CENTER);
        f.setSize(1100, 700);
        int x = ((screenSize.width - f.getWidth())/2);
        int y = ((screenSize.height - f.getWidth())/2+100);
        f.setVisible(true);
        f.setLocation(x,y);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Graf.initUI();
            }
        });
    }
}
