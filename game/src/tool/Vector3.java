package tool;

//********************************************
//*类名:Vector3
//*作者:凌恋      时间:2016-8-4 10:52:26
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Vector3 {

    public float x, y, z;

    public Vector3() {
        x = 0.0f;
        y = 0.0f;
        z = 0.0f;
    }

    public Vector3(float xx, float yy, float zz) {
        x = xx;
        y = yy;
        z = zz;
    }

    Vector3(Vector3 vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
    }

    //计算向量的长度
    public float length() {
        float temp = x * x + y * y + z * z;
        return temp;
    }

    //单位化一向量
    public Vector3 normalize() {
        float len = length();
        if (len == 0) {
            len = 1;
        }
        x = x / len;
        y = y / len;
        z = z / len;

        return this;
    }

    // 点积 
    public float dotProduct(Vector3 v) {
        return (x * v.x + y * v.y + z * v.z);
    }

    //叉积
    public Vector3 crossProduct(Vector3 v) {

        Vector3 vec = new Vector3();

        vec.x = y * v.z - z * v.y;
        vec.y = z * v.x - x * v.z;
        vec.z = x * v.y - y * v.x;

        return vec;
    }

    public Vector3 add(Vector3 v) {
        Vector3 vec = new Vector3();

        vec.x = x + v.x;
        vec.y = y + v.y;
        vec.z = z + v.z;

        return vec;
    }

    public Vector3 cut(Vector3 v) {

        Vector3 vec = new Vector3();

        vec.x = x - v.x;
        vec.y = y - v.y;
        vec.z = z - v.z;

        return vec;
    }

    public Vector3 ride(float scale) {

        x = x * scale;
        y = y * scale;
        z = z * scale;

        return this;
    }

    public Vector3 divide(float scale) {

        if (scale != 0.0) {
            x = x / scale;
            y = y / scale;
            z = z / scale;
        }
        return this;
    }

    public Vector3 cut() {
        Vector3 vec = new Vector3(-x, -y, -z);
        return vec;
    }

    @Override
    public String toString(){
        return "("+x+","+y+","+z+")";
    }
}
