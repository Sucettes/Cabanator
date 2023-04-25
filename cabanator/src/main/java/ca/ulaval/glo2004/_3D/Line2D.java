package ca.ulaval.glo2004._3D;

import java.awt.*;

public class Line2D {
    Point p1;
    Point p2;
    Color color;

    double epaisseur = 1;

    public Line2D(Point p1, Point p2, Color color, double epaisseur) {
        this.p1 = p1;
        this.p2 = p2;
        this.color = color;
        this.epaisseur = epaisseur;
    }

    public Line2D(Line2D line) {
        this.p1 = new Point(line.p1);
        this.p2 = new Point(line.p2);
        this.color = line.color;
        this.epaisseur = line.epaisseur;
    }

    public Line2D(Point[] points, Color color, double epaisseur) {
        if (points.length == 2) {
            this.p1 = points[0];
            this.p2 = points[1];
        }
        else {
            this.p1 = new Point(0,0);
            this.p2 = new Point(0,0);
        }
        this.color = color;
        this.epaisseur = epaisseur;
    }



}
