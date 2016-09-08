package map;

//********************************************
import game.GameObject;

//*类名:Map
//*作者:凌恋      时间:2016-8-13 16:54:09
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Map{

    int x, y, z;
   public int id;
    public int num;
    float high;
    String name;
    public int lightRange = 1;
    public boolean isCanMove = true;
    public boolean isLight = false;
    public boolean isHasBuild = false;

    public boolean isHasBuild() {
        return isHasBuild;
    }



    public void setIsHasBuild(boolean isHasBuild) {
        this.isHasBuild = isHasBuild;
    }

    public Map(int x, int y, int z, int id, int num, float high) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.id = id;
        this.high = high;
        this.num = num;
        this.name = name;
        lightRange = 1;
    }

    public float getHigh() {
        return high;
    }

    public boolean isLight() {
        return isLight;
    }

    public void setIsLight(boolean isLight) {
        this.isLight = isLight;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCanMove() {
        return isCanMove;
    }

    public void setIsCanMove(boolean isCanMove) {
        this.isCanMove = isCanMove;
    }

    @Override
    public String toString(){
        return id+","+num+","+high+","+isHasBuild;
    }
}
