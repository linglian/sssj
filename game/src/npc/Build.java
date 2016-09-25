package npc;

//********************************************
import game.GameMap;
import npc.NpcObject;

//*类名:Build
//*作者:凌恋      时间:2016-8-13 16:57:09
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Build extends NpcObject{
    
    int maxLvl;
    int gold;
    int wood;
    int water;
    int rock;
    int repairPay;
    int buildTime;

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


    public void setSpeed(float speed) {
        this.speed = speed;
    }

    
    public static void putMapBuild(int x, int y, boolean b) {
        for (int i = 0; i < 10; i++) {
            GameMap.map[x][y][i].isHasBuild = b;
        }
    }


    public int getMaxLvl() {
        return maxLvl;
    }

    public void setMaxLvl(int maxLvl) {
        this.maxLvl = maxLvl;
    }


    @Override
    public int getLvl() {
        if (lvl > maxLvl) {
            return maxLvl;
        }
        return lvl;
    }

    @Override
    public void setLvl(int lvl) {
        if (lvl > maxLvl) {
            this.lvl = maxLvl;
        }
        this.lvl = lvl;
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
            Build.putMapBuild((int)x, (int)y, false);
        }
    }
}
