package build;

//********************************************
import game.GameMap;

//*类名:Build
//*作者:凌恋      时间:2016-8-13 16:57:09
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Build {

    int team;
    int id;
    int lvl;
    int maxLvl;
    int hp;
    int maxHp;
    int fy;
    int gj;
    int pay;
    int repairPay;
    int buildTime;
    public int x;
    public int y;
    boolean isEnable;
    float width;
    float face;
    float high;

    public Build(int id, int lvl, int x, int y, float high, float width, float face,int team) {
        this.id = id;
        this.team = team;
        this.lvl = lvl;
        this.high = high;
        this.face = face;
        this.width = width;
        this.x = x;
        this.y = y;
        this.isEnable = true;
        putMapBuild(x,y,true);
    }

    public static void putMapBuild(int x,int y,boolean b){
        for (int i = 0; i < 10; i++) {
            GameMap.map[x][y][i].isHasBuild =b;
        }
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }
    
    public boolean isEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getFace() {
        return face;
    }

    public void setFace(float face) {
        this.face = face;
    }

    public int getMaxLvl() {
        return maxLvl;
    }

    public void setMaxLvl(int maxLvl) {
        this.maxLvl = maxLvl;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLvl() {
        if (lvl > maxLvl) {
            return maxLvl;
        }
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getFy() {
        return fy;
    }

    public void setFy(int fy) {
        this.fy = fy;
    }

    public int getGj() {
        return gj;
    }

    public void setGj(int gj) {
        this.gj = gj;
    }

    public int getPay() {
        return pay;
    }

    public void setPay(int pay) {
        this.pay = pay;
    }

    public int getRepairPay() {
        return repairPay;
    }

    public void setRepairPay(int repairPay) {
        this.repairPay = repairPay;
    }

    public int getBuildTime() {
        return buildTime;
    }

    public void setBuildTime(int buildTime) {
        this.buildTime = buildTime;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void event(Object o) {
    }

    public void event() {
    }

    public void run() {
        if(this.hp<=0){
            this.isEnable = false;
            this.putMapBuild(x, y,false);
        }
    }
}
