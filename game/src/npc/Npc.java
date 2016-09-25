package npc;

//********************************************
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import game.GameMap;
import game.GameNpc;
import game.GameObject;
import main.Main;
import md2.MD2Animate;
import md2.MD2Model;
import tool.Vector3;

//*类名:Npc
//*作者:凌恋      时间:2016-8-13 16:58:12
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Npc extends GameObject {

    int lvl;
    int id;
    public float x;
    public float y;
    public float high;
    public float lastX;
    public float lastY;
    public float lastHigh;
    public int view;
    public int team;
    public Vector3 v;
    public double hp;
    public double maxHp;
    public double mp;
    public double maxMp;
    public double fy;
    public double gj;
    public float width;
    public double pay;
    public double lastAddHp;
    public double addHp;
    public double lastAddMp;
    public double addMp;
    public float attackRange;
    public float face;
    public double speed;
    public double speedFight;
    public double fightSpeedTime;
    public boolean isAlive = true;
    public boolean isEnable = true;
    public static final double MAX_SPEED_FIGHT = 2f;

    public Npc(){

    }
    public Npc(int id, double maxHp, double maxMp, double gj, double fy, double addHp, double addMp, double pay, float attackRange, double speed, double speedFight, int view) {
        super(1f, 1f, 1f, 0, 0, 0);
        this.id = id;
        this.maxHp = maxHp;
        this.addHp = addHp;
        this.addMp = addMp;
        this.maxMp = maxMp;
        this.hp = maxHp;
        this.mp = maxMp;
        this.fy = fy;
        this.gj = gj;
        this.lvl = 1;
        this.pay = pay;
        this.attackRange = attackRange;
        fightSpeedTime = 0;
        this.view = view;
        this.speed = speed;
        this.speedFight = speedFight;
        v = new Vector3(0, 0, 0);
    }
    public void initNpc(int id, double maxHp, double maxMp, double gj, double fy, double addHp, double addMp, double pay, float attackRange, double speed, double speedFight, int view) {
        this.id = id;
        this.maxHp = maxHp;
        this.addHp = addHp;
        this.addMp = addMp;
        this.maxMp = maxMp;
        this.hp = maxHp;
        this.mp = maxMp;
        this.fy = fy;
        this.gj = gj;
        this.lvl = 1;
        this.pay = pay;
        this.attackRange = attackRange;
        fightSpeedTime = 0;
        this.view = view;
        this.speed = speed;
        this.speedFight = speedFight;
        v = new Vector3(0, 0, 0);
    }

    public Npc(int id, float x, float y, float high, float width,int team) {
        super(1f, 1f, 1f, x, 0, y);
        this.high = high;
        this.id = id;
        this.x = x;
        this.team = team;
        this.y = y;
        this.width = width;
        this.speed = 0.1f;
        this.hp = 1;
        this.mp = 100;
        this.maxHp = 100;
        this.maxMp = 100;
        this.gj = 10;
        this.fy = 1;
        this.lvl = 1;
        attackRange = 5f;
        speedFight = 0.565f;
        addHp = 0.1;
        addMp = 0.1;
        fightSpeedTime = 0;
        view = 1;
        v = new Vector3(0, 0, 0);
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

    public double getFightSpeedTime() {
        return fightSpeedTime;
    }

    public void setFightSpeedTime(double fightSpeedTime) {
        this.fightSpeedTime = fightSpeedTime;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setIsAlive(boolean isAlive) {
        this.isAlive = isAlive;
    }

    public double getSpeedFight() {
        return speedFight;
    }

    public void setSpeedFight(double speedFight) {
        this.speedFight = speedFight;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public double getLastAddMp() {
        return lastAddMp;
    }

    public double getAddMp() {
        return addMp;
    }

    public void setAddMp(double addMp) {
        lastAddHp = addMp;
        this.addHp = addMp;
    }

    public double getLastAddHp() {
        return lastAddHp;
    }

    public double getAddHp() {
        return addHp;
    }

    public void setAddHp(float addHp) {
        lastAddHp = addHp;
        this.addHp = addHp;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public float getLastX() {
        return lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public float getLastHigh() {
        return lastHigh;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        if (high < 0) {
            this.lastHigh = this.high;
            this.high = 0;
        } else if (high >= 10) {
            this.lastHigh = this.high;
            this.high = 9;
        } else {
            this.lastHigh = this.high;
            this.high = high;
        }
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        if (x < 0) {
            this.lastX = this.x;
            this.x = 0;
        } else if (x >= GameMap.width) {
            this.lastX = this.x;
            this.x = GameMap.width - 1;
        } else {
            this.lastX = this.x;
            this.x = x;
        }
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        if (y < 0) {
            this.lastY = this.y;
            this.y = 0;
        } else if (y >= GameMap.width) {
            this.lastY = this.y;
            this.y = GameMap.width - 1;
        } else {
            this.lastY = this.y;
            this.y = y;
        }
    }

    public double getHp() {
        return hp;
    }

    public void setHp(double hp) {
        if (hp > maxHp) {
            this.hp = maxHp;
        } else {
            this.hp = hp;
        }
    }

    public double getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(double maxHp) {
        this.maxHp = maxHp;
    }

    public double getMp() {
        return mp;
    }

    public void setMp(double mp) {
        if (mp > maxMp) {
            this.mp = maxMp;
        } else {
            this.mp = mp;
        }
    }

    public double getMaxMp() {
        return maxMp;
    }

    public void setMaxMp(double mapMp) {
        this.maxMp = mapMp;
    }

    public double getFy() {
        return fy;
    }

    public void setFy(double fy) {
        this.fy = fy;
    }

    public double getGj() {
        return gj;
    }

    public void setGj(double gj) {
        this.gj = gj;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public double getPay() {
        return pay;
    }

    public void setPay(double pay) {
        this.pay = pay;
    }

    public float getFace() {
        return face;
    }

    public void setFace(float face) {
        this.face = face;
    }

    public String toString2() {
        return id + "," + maxHp + "," + maxMp + "," + gj + "," + fy + "," + addHp + "," + addMp + "," + pay + "," + this.attackRange + "," + this.speed + "," + this.speedFight + "," + this.view;
    }
}
