//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Vector;

public class Node {
    private String label;
    private int coordX;
    private int coordY;
    private int number;

    public Vector<Node> VeciniInversi = new Vector<>();
    public Vector<Integer> Vecinii=new Vector<Integer>();

    public Vector<Node> VeciniNoduri = new Vector<>();

    public Node(int coordX, int coordY, int number) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.number = number;
    }

    public Node(int coordX, int coordY, int number,String label)
    {
        this.coordX = coordX;
        this.coordY = coordY;
        this.number = number;
        this.label = label;
    }

    public int getCoordX() {
        return this.coordX;
    }

    public void setCoordX(int coordX) {
        this.coordX = coordX;
    }

    public int getCoordY() {
        return this.coordY;
    }

    public void setCoordY(int coordY) {
        this.coordY = coordY;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void moveBy(int dx, int dy) {
        this.coordX += dx;
        this.coordY += dy;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void drawNode(Graphics g, int node_diam) {
        g.setColor(Color.RED);
        g.setFont(new Font("TimesRoman", 1, 15));
        g.fillOval(this.coordX, this.coordY, node_diam, node_diam);
        g.setColor(Color.WHITE);
        g.drawOval(this.coordX, this.coordY, node_diam, node_diam);
        if (this.number < 10) {
            g.drawString(Integer.valueOf(this.number).toString(), this.coordX + 13, this.coordY + 20);
        } else {
            g.drawString(Integer.valueOf(this.number).toString(), this.coordX + 8, this.coordY + 20);
        }

    }

    public void drawConnectedComponent(Graphics g)
    {
        g.setColor(Color.BLUE);
        g.setFont(new Font("TimesRoman", 1, 15));
        g.fillOval(this.coordX, this.coordY, 70, 70);
        g.setColor(Color.WHITE);
        g.drawOval(this.coordX, this.coordY, 70, 70);
        g.drawString(label, this.coordX + 20, this.coordY + 30);
    }
    public void drawComponents(Graphics g, Color color, int node_diam)
    {
        g.setColor(color);
        g.setFont(new Font("TimesRoman", 1, 15));
        g.fillOval(this.coordX, this.coordY, node_diam, node_diam);
        g.setColor(Color.WHITE);
        g.drawOval(this.coordX, this.coordY, node_diam, node_diam);
        if (this.number < 10) {
            g.drawString(Integer.valueOf(this.number).toString(), this.coordX + 13, this.coordY + 20);
        } else {
            g.drawString(Integer.valueOf(this.number).toString(), this.coordX + 8, this.coordY + 20);
        }
    }
}
