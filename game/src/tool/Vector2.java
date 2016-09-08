package tool;

//********************************************
//*类名:Vector2
//*作者:凌恋      时间:2016-8-4 10:56:26
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Vector2 {

   public  float x, y;

    public Vector2(float xx, float yy) {
        x = xx;
        y = yy;
    }

    public Vector2(){
        x = 0f;
        y = 0f;
    }
    Vector2(Vector2 v) {
        x = v.x;
        y = v.y;
    }

}
