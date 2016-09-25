package npc;

//********************************************
import com.jogamp.opengl.GL2;
import game.GameManage;
import game.GameMap;
import game.GameNpc;
import game.GameThread;
import java.util.LinkedList;
import md2.MD2Animate;
import skill.skill;
import tool.Point;
import tool.Vector2;

//*类名:GameNpc
//*作者:凌恋      时间:2016-8-14 15:24:54
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class NpcObject extends Npc {

    MD2Animate[] animate;
    String stateName;
    LinkedList<Point> path;
    int pathFoot;
    int state = 0;
    Vector2 scrnPoint;
    Vector2 lastScrnPoint;
    NpcObject attackNpc;
    float wide;
    float targetX;
    float targetY;
    float runTime;
    float maxRun;
    float addHigh;
    boolean isChoosed = false;
    boolean isGuard = true;
    public int fid;
    public int frame;
    public int startFrame;
    public int endFrame;
    public final static int STATE_NOTHING = 0;
    public final static int STATE_CHOSE = 1;
    public final static int STATE_CHOOSE = 2;

    public NpcObject() {

    }

    public NpcObject(int id, float x, float y, float high, float width, int team) {
        super(id, x, y, high, width, team);
        maxRun = 0.3f;
        this.animate = game.GameNpc.getAnimate(id);
        for (int i = 0; i < animate.length; i++) {
            animate[i].ChangeId("stand");
        }
        stateName = "stand";
        this.targetX = x;
        this.targetY = y;
        scrnPoint = new Vector2(x, y);
    }

    public void init(Npc npc) {
        this.initNpc(npc.id, npc.maxHp, npc.maxMp, npc.gj, npc.fy, npc.addHp, npc.addMp, npc.pay, npc.attackRange, npc.speed, npc.speedFight, npc.view);
    }

    public void changeStateId(String str) {
        stateName = str;
        boolean isFirst = true;
        for (int i = 0; i < animate.length; i++) {
            if (isFirst) {
                int tid;
                tid = animate[i].ChangeId(str);
                if (fid != tid) {
                    this.fid = tid;
                    this.startFrame = GameNpc.npcModelExample.get(id).get(0).get3DModel().pAnimations.get(fid).startFrame;
                    this.endFrame = GameNpc.npcModelExample.get(id).get(0).get3DModel().pAnimations.get(fid).endFrame;
                    this.frame = startFrame;
                }
                isFirst = false;
            } else {
                animate[i].ChangeId(str);
            }
        }
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

    public boolean isGuard() {
        return isGuard;
    }

    public void setIsGuard(boolean isGuard) {
        this.isGuard = isGuard;
    }

    public NpcObject getAttackNpc() {
        return attackNpc;
    }

    public void look() {
        int x = (int) (this.x + 0.5f);
        int y = (int) (this.y + 0.5f);
        for (int i = x - view; i <= x + view; i++) {
            for (int j = y - view; j <= y + view; j++) {
                if (i < 0 || j < 0 || i >= GameMap.width || j >= GameMap.height) {
                    continue;
                }
                GameMap.isLooked[team][i][j] = true;
                GameMap.isLooking[team][i][j] = GameThread.gameTime + 10;
            }
        }
    }

    public void run() {
        if (!isEnable) {
            return;
        }
        runTime += speed;
        if (!this.isAlive) {
            this.changeStateId("death");
            this.maxRun = 0.5f;
            if (frame >= endFrame) {
                this.isEnable = false;
            }
            if (runTime >= maxRun) {
                addFrame();
                runTime = 0;
                frame++;
            }
            return;
        }
        if (hp < maxHp) {
            this.setHp(hp + addHp);
        }
        if (runTime >= maxRun) {
            addFrame();
            runTime = 0;
        }
        this.fightSpeedTime += this.speedFight;
        if (this.fightSpeedTime >= Npc.MAX_SPEED_FIGHT) {
            this.fightSpeedTime = Npc.MAX_SPEED_FIGHT;
        }
        float h = GameMap.getHigh((int) (x + 0.5f), (int) (y + 0.5f));
        if (h > high + 1.3f) {

        } else {
            //动力
            if (addHigh != 0) {
                if (addHigh > 0) {
                    high += 0.4f;
                    addHigh -= 0.4f;
                    if (addHigh < 0) {
                        addHigh = 0;
                    }
                    if (h + high > 5f) {
                        high = 5f;
                    }
                } else if (addHigh < 0) {
                    high -= 0.2f;
                    addHigh += 0.7f;
                    if (addHigh > 0) {
                        addHigh = 0;
                    }
                }
            }
            //重力
            if (high > h) {
                high -= 0.2f;
            }
            //地面
            if (high < h) {
                high = h;
                addHigh = 0;
            }
        }
        if (!attack()) {
            move();
        }
        look();
    }

    public boolean attack() {
        if (this.attackNpc != null) {
            NpcObject npc = this.attackNpc;
            if (npc.isAlive == false) {
                this.attackNpc = null;
                this.path = null;
                return true;
            }
            float rx = this.x - npc.x;
            float ry = this.y - npc.y;
            float r = rx * rx + ry * ry;
            if (r <= this.attackRange * this.attackRange) {
                this.changeStateId("attack");
                if (this.fightSpeedTime >= Npc.MAX_SPEED_FIGHT) {
                    this.fightSpeedTime = 0;
                    skill temp = new skill(this.x, this.y, this.high, this, attackNpc, (float) this.speedFight, this.gj * (1f - (npc.fy / (npc.fy + 100))));
                    temp.start();
                    GameManage.skill.add(temp);
                }
            } else {
                if (this.path == null) {
                    path = Point.printPath(new Point(this.x, this.y), new Point(npc.x, npc.y), high, 50);
                    if (path != null) {
                        pathFoot = 0;
                    }
                }
                move();
            }
            return true;
        }
        return false;
    }

    public void setAttack(NpcObject npc) {
        this.attackNpc = npc;
        if (this.attackNpc == null) {
            return;
        }
        if (this.attackNpc == this) {
            this.attackNpc = null;
            return;
        }
        if (npc.isAlive == false) {
            this.attackNpc = null;
            this.path = null;
            return;
        }
        float rx = this.x - npc.x;
        float ry = this.y - npc.y;
        float r = rx * rx + ry * ry;
        if (r <= this.attackRange) {
            if (this.fightSpeedTime >= Npc.MAX_SPEED_FIGHT) {
                this.fightSpeedTime = 0;
                skill temp = new skill(this.x, this.y, this.high, this, attackNpc, (float) this.speedFight, this.gj * (1f - (npc.fy / (npc.fy + 100))));
                temp.start();
                GameManage.skill.add(temp);
            }
        } else {
            path = Point.printPath(new Point(this.x, this.y), new Point(npc.x, npc.y), high, 50);
            if (path != null) {
                pathFoot = 0;
            }
            move();
        }
    }

    public void setMove(float x, float y) {
        this.targetX = x;
        this.targetY = y;
    }

    private void move1() {
        if (this.x != this.targetX || this.y != this.targetY) {
            float lx = x;
            float ly = y;
            if (this.x < this.targetX) {
                this.x += this.speed;
            } else if (this.x > this.targetX) {
                this.x -= this.speed;
            }
            if (this.y < this.targetY) {
                this.y += this.speed;
            } else if (this.y > this.targetY) {
                this.y -= this.speed;
            }
            this.cy = this.high;
            this.cx = this.x;
            this.cz = this.y;
            //检测通过
            if (!GameMap.isCanMoved((int) (this.x + 0.5f), (int) (this.y + 0.5f), this.high)) {
                this.x = lx;
                this.y = ly;
                this.targetX = this.x;
                this.targetY = this.y;
                this.cy = this.high;
                this.cx = this.x;
                this.cz = this.y;
                maxRun = 0.2f;
                this.changeStateId("stand");
            } else {
                //可以通过
                if (high < GameMap.getHigh((int) (x + 0.5f), (int) (y + 0.5f))) {
                    this.high = GameMap.getHigh((int) (x + 0.5f), (int) (y + 0.5f));
                }
                float xl = this.targetX - this.x;
                if (Math.abs(this.x - this.targetX) <= speed * 1.5f) {
                    if (GameMap.getHigh((int) (targetX + 0.5f), (int) (y + 0.5f)) < high) {
                        this.x = this.targetX;
                    } else {
                        this.targetX = this.x;
                    }
                }
                float yl = this.targetY - this.y;
                if (Math.abs(this.y - this.targetY) <= speed * 1.5f) {
                    if (GameMap.getHigh((int) (x + 0.5f), (int) (targetY + 0.5f)) < high) {
                        this.y = this.targetY;
                    } else {
                        this.targetY = this.y;
                    }
                }
                if (yl == 0) {
                    if (xl > 0) {
                        this.setFace(0f);
                    } else {
                        this.setFace(180f);
                    }
                } else if (xl == 0) {
                    if (yl > 0) {
                        this.setFace(270f);
                    } else {
                        this.setFace(90f);
                    }
                } else if (yl > 0) {
                    this.setFace((float) Math.toDegrees((float) Math.atan(yl / xl)) - 90f);
                } else {
                    this.setFace((float) Math.toDegrees((float) Math.atan(yl / xl)) + 90f);
                }
                maxRun = 0.3f;
                this.changeStateId("run");
            }
        } else {
            maxRun = 0.2f;
            this.changeStateId("stand");
        }
    }

    private void move2() {
        if (this.x != this.targetX || this.y != this.targetY) {
            float lx = this.x;
            float ly = this.y;
            if (this.x < this.targetX) {
                this.x += this.speed;
            } else if (this.x > this.targetX) {
                this.x -= this.speed;
            }
            if (this.y < this.targetY) {
                this.y += this.speed;
            } else if (this.y > this.targetY) {
                this.y -= this.speed;
            }
            this.cy = this.high;
            this.cx = this.x;
            this.cz = this.y;
            //可以通过
            // System.out.println("正常：" + x + "," + y);
            // System.out.println("简化后:" + (int) (x + 0.5f) + "," + (int) (y + 0.5f));
            float th = GameMap.getHigh((int) (x + 0.5f), (int) (y + 0.5f));
            if (!GameMap.isCanMoved((int) (this.x + 0.5f), (int) (this.y + 0.5f), this.high)) {

            } else if (high < th) {
                this.high = th;
            }
            float xl = this.targetX - this.x;
            if (Math.abs(this.x - this.targetX) <= speed * 1.5f) {
                if (GameMap.getHigh((int) (targetX + 0.5f), (int) (y + 0.5f)) <= high) {
                    this.x = this.targetX;
                } else {
                    this.path.get(this.pathFoot).setX(this.x);
                    this.targetX = this.x;
                }
            }
            float yl = this.targetY - this.y;
            if (Math.abs(this.y - this.targetY) <= speed * 1.5f) {
                if (GameMap.getHigh((int) (x + 0.5f), (int) (targetY + 0.5f)) <= high) {
                    this.y = this.targetY;
                } else {
                    this.path.get(this.pathFoot).setY(this.y);
                    this.targetY = this.y;
                }
            }
            if (yl == 0) {
                if (xl > 0) {
                    this.setFace(0f);
                } else {
                    this.setFace(180f);
                }
            } else if (xl == 0) {
                if (yl > 0) {
                    this.setFace(270f);
                } else {
                    this.setFace(90f);
                }
            } else if (yl > 0) {
                this.setFace((float) Math.toDegrees((float) Math.atan(yl / xl)) - 90f);
            } else {
                this.setFace((float) Math.toDegrees((float) Math.atan(yl / xl)) + 90f);
            }
            maxRun = 0.3f;
            this.changeStateId("run");
        } else {
            pathFoot++;
        }
    }

    public void move() {
        if (path == null) {
            move1();
        } else {
            this.targetX = path.get(pathFoot).getX();
            this.targetY = path.get(pathFoot).getY();
            move2();
            if (pathFoot == path.size()) {
                maxRun = 0.1f;
                this.changeStateId("stand");
                path = null;
                pathFoot = 0;
            }
        }
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void addFrame() {
        for (int i = 0; i < animate.length; i++) {
            animate[i].currentFrame++;
        }
    }

    public void draw(GL2 gl, boolean isTexture) {
        for (int i = 0; i < animate.length; i++) {
            animate[i].AnimateMD2Model(gl, isTexture);
        }
    }

    public boolean isChoosed() {
        return isChoosed;
    }

    public void setIsChoosed(boolean isChoosed) {
        this.isChoosed = isChoosed;
    }

    public float getWide() {
        return wide;
    }

    public void setWide(float wide) {
        this.wide = wide;
    }

    public Vector2 getLastScrnPoint() {
        return lastScrnPoint;
    }

    public void setLastScrnPoint(Vector2 lastScrnPoint) {
        this.lastScrnPoint = lastScrnPoint;
    }

    public Vector2 getScrnPoint() {
        return scrnPoint;
    }

    public void setScrnPoint(Vector2 scrnPoint) {
        this.lastScrnPoint = scrnPoint;
        this.scrnPoint = scrnPoint;
    }

    public LinkedList<Point> getPath() {
        return path;
    }

    public void setPath(LinkedList<Point> path) {
        this.pathFoot = 0;
        this.path = path;
    }

    public float getAddHigh() {
        return addHigh;
    }

    public void setAddHigh(float addHigh) {
        this.addHigh = addHigh;
        if (this.addHigh + high > 5f) {
            this.addHigh = 5f - high;
        }
    }

    public String getBase() {
        String leixing;
        switch (id) {
            case 0:
                leixing = "士兵1号";
                break;
            case 1:
                leixing = "士兵2号";
                break;
            case 2:
                leixing = "士兵3号";
                break;
            case 3:
                leixing = "士兵4号";
                break;
            case 4:
                leixing = "怪兽1号";
                break;
            case 5:
                leixing = "怪兽2号";
                break;
            case 6:
                leixing = "怪兽3号";
                break;
            case 7:
                leixing = "怪兽4号";
                break;
            case 8:
                leixing = "怪兽5号";
                break;
            case 9:
                leixing = "怪兽6号";
                break;
            case 10:
                leixing = "怪兽7号";
                break;
            case 11:
                leixing = "怪兽8号";
                break;
            case 12:
                leixing = "怪兽9号";
                break;
            case 13:
                leixing = "怪兽10号";
                break;
            case 14:
                leixing = "怪兽11号";
                break;
            case 15:
                leixing = "怪兽12号";
                break;
            default:
                leixing = "神秘生物";
                break;
        }
        return "人物类型:" + leixing + "，最大生命:" + maxHp + "，最大能量:" + maxMp + "，攻击力:" + gj + "，防御力:" + fy + "，回复生命率:" + addHp + "，回复能量率" + addMp;
    }

    @Override
    public String toString() {
        return id + "," + x + "," + y + "," + high + "," + width + "," + maxHp + "," + maxMp + "," + gj + "," + fy + "," + team + "," + addHp;
    }

    public float getRunTime() {
        return runTime;
    }

    public void setRunTime(float runTime) {
        this.runTime = runTime;
    }

    public float getMaxRun() {
        return maxRun;
    }

    public void setMaxRun(float maxRun) {
        this.maxRun = maxRun;
    }
}
