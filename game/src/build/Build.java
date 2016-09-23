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
    float hp;
    float mp;
    float maxMp;
    float maxHp;
    float addHp;
    float addMp;
    int fy;
    int gj;
    int pay;
    int gold;
    int wood;
    int water;
    int rock;
    int repairPay;
    int buildTime;
    public int x;
    public int y;
    boolean isEnable;
    boolean isChoosed = false;
    float width;
    float face;
    float high;
    float speed;
    double speedFight;
    int state = 0;
    public final static int STATE_NOTHING = 0;
    public final static int STATE_CHOSE = 1;
    public final static int STATE_CHOOSE = 2;

    public Build(int id, int lvl, int x, int y, float high, float width, float face, int team) {
        this.id = id;
        this.team = team;
        this.lvl = lvl;
        this.high = high;
        this.face = face;
        this.width = width;
        this.x = x;
        this.y = y;
        this.hp = 100f;
        this.mp = 100f;
        this.maxMp = 100f;
        this.maxHp = 100f;
        this.speedFight = 0f;
        this.addHp = 0.05f;
        this.addMp = 0.05f;
        this.isEnable = true;
        putMapBuild(x, y, true);
    }

    public Build(int id, float maxMp, float maxHp, float addHp, float addMp, int fy, int gj, int pay, int gold, int wood, int water, int rock, int buildTime, float speed, double speedFight) {
        this.id = id;
        this.team = -1;
        this.lvl = 0;
        this.maxLvl = 10;
        this.maxMp = maxMp;
        this.mp = maxMp;
        this.maxHp = maxHp;
        this.hp = maxHp;
        this.addHp = addHp;
        this.addMp = addMp;
        this.fy = fy;
        this.gj = gj;
        this.pay = pay;
        this.gold = gold;
        this.wood = wood;
        this.water = water;
        this.rock = rock;
        this.buildTime = buildTime;
        this.speed = speed;
        this.speedFight = speedFight;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getRock() {
        return rock;
    }

    public void setRock(int rock) {
        this.rock = rock;
    }

    
    public double getSpeedFight() {
        return speedFight;
    }

    public void setSpeedFight(double speedFight) {
        this.speedFight = speedFight;
    }

    public float getMaxHp() {
        return maxHp;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    
    public void setMaxHp(float maxHp) {
        this.maxHp = maxHp;
    }

    public float getHp() {
        return hp;
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public float getAddHp() {
        return addHp;
    }

    public void setAddHp(float addHp) {
        this.addHp = addHp;
    }

    public float getAddMp() {
        return addMp;
    }

    public void setAddMp(float addMp) {
        this.addMp = addMp;
    }

    public float getMp() {
        return mp;
    }

    public void setMp(float mp) {
        this.mp = mp;
    }

    public float getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(float maxMp) {
        this.maxMp = maxMp;
    }

    public boolean isChoseRange(float x, float y) {
        if (x <= this.x + 0.45f && x >= this.x - 0.45f && y <= this.y + 0.45f && y >= this.y - 0.45f) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isRange(float x1, float y1, float x2, float y2) {
        if (x1 <= this.x && x2 >= this.x && y1 <= this.y && y2 >= this.y) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isChoseRange(float x1, float y1, float x2, float y2) {
        float dx = Math.abs(x2 - x1);
        float dy = Math.abs(y2 - y1);
        if (x1 < x2 && y1 < y2) {
            return isRange(x1, y1, x2, y2);
        } else if (x1 > x2 && y1 < y2) {
            return isRange(x2, y1, x1, y2);
        } else if (x1 > x2 && y1 > y2) {
            return isRange(x2, y2, x1, y1);
        } else if (x1 < x2 && y1 > y2) {
            return isRange(x1, y2, x2, y1);
        }
        return false;
    }

    public static void putMapBuild(int x, int y, boolean b) {
        for (int i = 0; i < 10; i++) {
            GameMap.map[x][y][i].isHasBuild = b;
        }
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setIsChoosed(boolean isChoosed) {
        this.isChoosed = isChoosed;
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
        this.hp += addHp;
        if (this.hp > maxHp) {
            this.hp = maxHp;
        }
        this.mp += addMp;
        if (this.mp > maxMp) {
            this.mp = maxMp;
        }
        if (this.hp <= 0) {
            this.isEnable = false;
            Build.putMapBuild(x, y, false);
        }
    }
}
