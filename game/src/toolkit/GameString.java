package toolkit;

//********************************************
import java.awt.Color;

//*类名:GameString
//*作者:凌恋      时间:2016-8-16 18:45:14
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameString {

    String str;
    int x;
    int y;
    int wdInEhRow;
    int lifeTime;
    float size;
    Color color;

    public GameString(String str, int x, int y, float size, int wdInEhRow, int lifeTime, Color color) {
        this.str = str;
        this.x = x;
        this.color = color;
        this.y = y;
        this.size = size;
        this.wdInEhRow = wdInEhRow;
        this.lifeTime = lifeTime;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setWdInEhRow(int wdInEhRow) {
        this.wdInEhRow = wdInEhRow;
    }

    public String getStr() {
        return str;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWdInEhRow() {
        return wdInEhRow;
    }

}
