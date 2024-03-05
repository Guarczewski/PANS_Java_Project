package org.example.Custom_Polygons;
import org.example.Custom_Enums.PreMadeType;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class CPolygon implements Serializable {
    public List<CPoint> Points;
    public double offsetX;
    public double offsetY;
    public CPolygon(){
        this.offsetX = -100;
        this.offsetY = -100;
        this.Points = new ArrayList<>();
    }

    public void Copy(CPolygon source) {
        this.offsetX = source.offsetX;
        this.offsetY = source.offsetY;

        this.Points.clear();
        for (CPoint temp : source.Points)
            this.Points.add(new CPoint(temp.cordX, temp.cordY));
    }

    public Polygon GetPoly(){
        Polygon tempPolygon = new Polygon();

        for (CPoint tempCPoint : Points) {
            Point tempPoint = tempCPoint.ConvertPoint();
            tempPolygon.addPoint((int) (tempPoint.x + offsetX), (int) (tempPoint.y + offsetY));
        }
        return tempPolygon;
    }

    public void Render(Graphics2D graphics2D, Color color) {
        Polygon tempPolygon = new Polygon();

        for (CPoint tempCPoint : Points) {
            Point tempPoint = tempCPoint.ConvertPoint();
            tempPolygon.addPoint((int) (tempPoint.x + offsetX), (int) (tempPoint.y + offsetY));
        }

        graphics2D.setColor(color);
        graphics2D.fillPolygon(tempPolygon);
    }
    public void UpdateOffsets(double newOffsetX, double newOffsetY) {
        this.offsetX = newOffsetX;
        this.offsetY = newOffsetY;
    }
    public void Rotate(boolean CW, double _VectorX) {
        for (CPoint temp : Points)
            temp.Rotate(CW,_VectorX);
    }
    public void InitPreMade(PreMadeType preMadeType, double size) {

        Points.clear();

        switch (preMadeType) {
            case SQUARE -> {
                Points.add(new CPoint(-5 * size, -5 * size));
                Points.add(new CPoint(5 * size, -5 * size));
                Points.add(new CPoint(5 * size, 5 * size));
                Points.add(new CPoint(-5 * size, 5 * size));
            }
            case TRIANGLE -> {
                Points.add(new CPoint(-5 * size, -5 * (size / 2)));
                Points.add(new CPoint(5 * size, -5 * (size / 2)));
                Points.add(new CPoint(0, 5 * size));
            }
            case PENTAGON -> {
                Points.add(new CPoint(0 * size, 8 * size));
                Points.add(new CPoint(8 * size, 2 * size));
                Points.add(new CPoint(4 * size, -7 * size));
                Points.add(new CPoint(-4 * size, -7 * size));
                Points.add(new CPoint(-8 * size, 2 * size));
            }
        }

    }

}
