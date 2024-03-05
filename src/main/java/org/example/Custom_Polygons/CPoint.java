package org.example.Custom_Polygons;

import java.awt.*;
import java.io.Serializable;

public class CPoint implements Serializable {
    public double cordX, cordY;
    public double scale = 0.5;
    public CPoint(double cordX, double cordY){
        this.cordX = cordX;
        this.cordY = cordY;
    }
    public Point ConvertPoint(){
        double tempNewX = cordX * scale;
        double tempNewY = cordY * scale;
        double[] newValues = Scale(tempNewX,tempNewY);
        int tempOutputX = (int) (50 + newValues[0]);
        int tempOutputY = (int) (50 + newValues[1]);
        return new Point(tempOutputX,tempOutputY);
    }
    public static double[] Scale(double tempX, double tempY) {
        double dist = Math.sqrt(Math.pow(tempX,2) + Math.pow(tempY,2));
        double theta = Math.atan2(tempY,tempX);
        double[] outValues = new double[2];
        outValues[0] = dist * Math.cos(theta);
        outValues[1] = dist * Math.sin(theta);
        return outValues;
    }
    public void Rotate(boolean CW, double degree){
        double radius = Math.sqrt(Math.pow(cordX,2) + Math.pow(cordY,2));
        double theta = Math.atan2(cordY, cordX);
        if (CW) { theta += 2 * Math.PI / 360 * degree * -1; }
        else { theta += 2 * Math.PI / 360 * degree; }
        cordX = radius * Math.cos(theta);
        cordY = radius * Math.sin(theta);
    }

}
