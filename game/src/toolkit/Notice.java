package toolkit;

//********************************************
//*类名:Notice
//*作者:凌恋      时间:2016-8-13 16:55:49
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Notice {

    String str;
    int x;
    int y;
    int lifeTime;

    public Notice(String str, int x, int y, int lifeTime) {
        this.str = str;
        this.x = x;
        this.y = y;
        this.lifeTime = lifeTime;
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

    public String getStr() {
        return str;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
