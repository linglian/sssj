package skill;

//********************************************
import com.jogamp.opengl.GL2;
import granule.Granule;
import granule.SkillAnimation;
import java.util.logging.Level;
import java.util.logging.Logger;
import npc.NpcObject;

//*类名:skill
//*作者:凌恋      时间:2016-8-20 11:29:41
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class skill extends SkillAnimation implements Runnable {

    NpcObject targetNpc;
    NpcObject npc;
    float mainSpeed;
    double hurt;

    public skill(float x, float y, float high, NpcObject npc, NpcObject tNpc, float speed,double hurt) {
        this.x = x;
        this.mainSpeed = speed;
        this.y = y;
        this.high = high;
        this.npc = npc;
        targetNpc = tNpc;
        this.hurt = hurt;
        granule = new Granule[1];
        number = 1;
        aliveNumber = 1;
        granule[0] = new Granule(x, y, high + 0.5f, 1f, 0f, 0f, 1f, 0.02f, 0f, 0f, 0f, 0f, 100f,false);
    }

    @Override
    public void start() {
        mainThread = new Thread(this);
        mainThread.start();
    }

    @Override
    public void run() {
        while (aliveNumber > 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {

            }
            float targetX = targetNpc.getX();
            float targetY = targetNpc.getY();
            float targetH = targetNpc.getHigh() + 0.5f;
            for (int i = 0; i < number; i++) {
                granule[i].run();
                if (granule[i].isAlive) {
                    if (targetNpc.isRange(granule[i].x - granule[i].width / 2, granule[i].y - granule[i].width / 2, granule[i].x + granule[i].width / 2, granule[i].y + granule[i].width / 2)) {
                        granule[i].isAlive = false;
                        aliveNumber--;
                        targetNpc.setHp(targetNpc.getHp() - hurt);
                        if (targetNpc.getHp() <= 0) {
                            targetNpc.setIsAlive(false);
                        }
                    } else {
                        float xDis = Math.abs(targetX - granule[i].x);
                        float yDis = Math.abs(targetY - granule[i].y);
                        float hDis = Math.abs(targetH - granule[i].high);
                        float xTime = xDis / mainSpeed;
                        float yTime = yDis / mainSpeed;
                        float hTime = yDis /mainSpeed;
                        float xy = xTime / yTime;
                        float xh = xTime / hTime;
                        float yS, xS, hS;
                        if (xTime < 1 && yTime < 1 && hTime < 1) {
                            yS = mainSpeed * yTime;
                            xS =mainSpeed * xTime;
                            hS =mainSpeed * hTime;
                        } else if (xy >= 1 && xh >= 1) {
                            xS = mainSpeed;
                            yS =mainSpeed / xy;
                            hS = mainSpeed / xh;
                        } else if (xy < 1 && xh >= 1) {
                            xS = mainSpeed* xy;
                            hS = mainSpeed/ xh;
                            yS =mainSpeed;
                        } else if (xy >= 1 && xh < 1) {
                            xS = mainSpeed;
                            yS = mainSpeed / xy;
                            hS = mainSpeed * xh;
                        } else {
                            xS = mainSpeed* xy;
                            yS = mainSpeed;
                            hS = mainSpeed;
                        }
                        if (granule[i].x < targetX) {
                            granule[i].x += xS;
                        } else if (granule[i].x > targetX) {
                            granule[i].x -= xS;
                        }
                        if (granule[i].y < targetY) {
                            granule[i].y += yS;
                        } else if (granule[i].y > targetY) {
                            granule[i].y -= yS;
                        }
                        if (granule[i].high < targetH) {
                            granule[i].high += hS;
                        } else if (granule[i].y > targetH) {
                            granule[i].high -= hS;
                        }

                    }
                }
            }
        }
    }

}
