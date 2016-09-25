package game;

//********************************************
//*类名:GameObject
//*作者:凌恋      时间:2016-8-16 11:30:56
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameObject {

    public float objectWidth;
    public float objectHeight;
    public float objectHigh;
    public float cx, cy, cz;

    public GameObject(){

    }
    public GameObject(float width, float height, float high, float cx, float cy, float cz) {
        this.objectWidth = width;
        this.objectHeight = height;
        this.objectHigh = high;
        this.cx = cx;
        this.cy = cy;
        this.cz = cz;
    }

    public boolean crash(GameObject o) {
        float x = o.cx;
        float y = o.cy;
        float z = o.cz;
        if (2f * Math.abs(cx - x) >= (objectWidth + o.objectWidth)) {
            return true;
        } else if (2f * Math.abs(cy - y) >= (objectHigh + o.objectHigh)) {
            return true;
        } else if (2f * Math.abs(cz - z) >= (objectHeight + o.objectHeight)) {
            return true;
        }
        return false;
    }
    @Override
    public String toString(){
        return "x="+cx+"\ty="+cy+"z=\t"+cz+"width=\t"+objectWidth+"high=\t"+objectHigh+"height=\t"+objectHeight;
    }
}
