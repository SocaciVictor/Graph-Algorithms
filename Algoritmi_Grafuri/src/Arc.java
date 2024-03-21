//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

import java.awt.*;

public class Arc {
    private Point start;

    private String cost;
    private int costInteger;
    private Point end;
    public int startNode;
    public int endNode;

    private Node StartNodeNode;
    private Node EndNodeNode;

    public Arc(Point start, Point end) {
        this.start = start;
        this.end = end;
    }


    public void drawArrowEdge(Graphics g,boolean isOriented)
    {
        if (this.start !=null && isOriented==true) {
            g.setColor(Color.red);

            int arrowSize = 10;
            double angle = Math.atan2(this.end.y - this.start.y,this.end.x-this.start.x);


            int x = (int) (this.end.x - (arrowSize+5) * Math.cos(angle));
            int y = (int) (this.end.y - (arrowSize+5) * Math.sin(angle));


            int[] xPoints = {x, (int) (x - arrowSize * Math.cos(angle - Math.PI / 6)), (int) (x - arrowSize * Math.cos(angle + Math.PI / 6))};
            int[] yPoints = {y, (int) (y - arrowSize * Math.sin(angle - Math.PI / 6)), (int) (y - arrowSize * Math.sin(angle + Math.PI / 6))};
            g.fillPolygon(xPoints, yPoints, 3);
            g.drawLine(this.start.x, this.start.y, this.end.x, this.end.y);
        }
        else {
            g.setColor(Color.RED);
            g.drawLine(this.start.x, this.start.y, this.end.x, this.end.y);
        }
    }
    public void drawArrowEdgeConnected(Graphics g,boolean isOriented)
    {
        if (this.start !=null && isOriented==true) {
            g.setColor(Color.red);

            int arrowSize = 15;
            double angle = Math.atan2(this.end.y - this.start.y,this.end.x-this.start.x);


            int x = (int) (this.end.x - (arrowSize+20) * Math.cos(angle));
            int y = (int) (this.end.y - (arrowSize+20) * Math.sin(angle));


            int[] xPoints = {x, (int) (x - arrowSize * Math.cos(angle - Math.PI / 6)), (int) (x - arrowSize * Math.cos(angle + Math.PI / 6))};
            int[] yPoints = {y, (int) (y - arrowSize * Math.sin(angle - Math.PI / 6)), (int) (y - arrowSize * Math.sin(angle + Math.PI / 6))};
            g.fillPolygon(xPoints, yPoints, 3);
            g.drawLine(this.start.x, this.start.y, this.end.x, this.end.y);
        }
    }
    public void drawArc(Graphics g) {
        if (this.start != null) {
            g.setColor(Color.RED);
            g.drawLine(this.start.x, this.start.y, this.end.x, this.end.y);
            g.setFont(new Font("Arial", Font.PLAIN, 24));
            g.setColor(Color.ORANGE);
            g.drawString(cost, (this.start.x+this.end.x)/2, (this.start.y+this.end.y)/2);
        }

    }

    public Point getCoordX()
    {
        return this.start;
    }

    public Point getCoordY(){
        return this.end;
    }

    public int getStartNode() {
        return this.startNode;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public void setStartNode(Node startNode) {
        StartNodeNode = startNode;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public void setCostInteger(int costInteger) {
        this.costInteger = costInteger;
    }

    public String getCost() {
        return cost;
    }

    public int getCostInteger() {
        return costInteger;
    }

    public void setEndNode(Node endNode) {
        EndNodeNode = endNode;
    }

    public void setStartNodeNode(Node startNodeNode) {
        StartNodeNode = startNodeNode;
    }
    public Node getStartNodeNode(){
        return StartNodeNode;
    }

    public void setEndNodeNode(Node endNodeNode) {
        EndNodeNode = endNodeNode;
    }
    public Node getEndNodeNode(){
        return EndNodeNode;
    }

    public int getEndNode() {
        return endNode;
    }
}
