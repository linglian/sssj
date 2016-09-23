package listener;

import build.Build;
import build.Flame;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import game.GameGLEventListener;
import game.GameManage;
import game.GameMap;
import game.GameUser;
import gui.GameGUI;
import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.*;
import java.awt.event.MouseListener;
import java.nio.Buffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.Random;
import map.Map;
import npc.Npc;
import npc.NpcObject;
import tool.Point;
import tool.Vector3;

//********************************************
//*类名:GameMouseListener
//*作者:李俊萍       时间:2016-7-30 6:55:19
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameMouseListener implements MouseListener {

    GLU glu;
    GL2 gl;
    GameManage gameManage;
    Thread thread;

    public GameMouseListener(GameManage gameManage) {
        this.gameManage = gameManage;
    }

    public boolean mouseChose(MouseEvent e) {
        if (gameManage.getUser().getGameFunction() == null) {
            return false;
        }
        int x = e.getX();
        int y = e.getY();
        int width = gameManage.getFrame().getWidth();
        int height = gameManage.getFrame().getHeight();
        float w = (float) x / (float) width;
        float h = (float) y / (float) height;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                if (w >= 0.7f + j * 0.04f && w <= 0.74f + j * 0.04f && h >= 0.73f + 0.05f * i && h <= 0.78f + 0.05f * i) {
                    gameManage.getUser().getGameFunction()[i * 5 + j].function();
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        GameUser user = gameManage.getUser();
        if (user.isCtrl()) {
            System.out.println("x=" + (float) ((float) e.getX() / (float) gameManage.getFrame().getWidth()) + "," + "y=" + (float) ((float) e.getY() / (float) gameManage.getFrame().getHeight()));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!GameGLEventListener.isFirst) {
            if ((gameManage.getUser().getChooseNpcNum() >= 1 || gameManage.getUser().getChooseBuildNum() >= 1) && e.getX() > (int) (gameManage.getFrame().getWidth() * 0.345f) && e.getY() > (int) (gameManage.getFrame().getHeight() * 0.77f) && e.getX() < (int) (gameManage.getFrame().getWidth() * 0.495f) && e.getY() < (int) (gameManage.getFrame().getHeight() * 0.96f)) {
                gameManage.getUser().setFlow(true);
            }
        }
    }

    public void tMouseReleaed(MouseEvent e) {
        gameManage.getEvent().isOtherGUI = false;
        gameManage.clearGameGUI();
        if (!GameGLEventListener.isFirst) {
            if (gameManage.getUser().isFlow()) {
                gameManage.getUser().setFlow(false);
                return;
            }
            if (gameManage.getUser().isDragged()) {
                gameManage.getUser().setReleasedMouseX(e.getX());
                gameManage.getUser().setReleasedMouseY(e.getY());
                gameManage.getUser().setIsChooseDragged(true);
                return;
            }
        }
        if (gameManage.getUser().isCantHandle()) {
            return;
        }
        GameUser user = gameManage.getUser();
        //检测是否为gameGui
        int size = gameManage.getGameGUI().size();
        int mouseX = e.getX();
        int mouseY = e.getY();
        boolean isFound = false;
        for (int i = size - 1; i >= 0; i--) {
            GameGUI temp = gameManage.getGameGUI().get(i);
            if (temp.enable && temp.isViewable && temp.isRange(mouseX, mouseY)) {
                temp.click(e);
                isFound = true;
                gameManage.setFocusGameGui(temp);
                break;
            } else {
                temp.exit(e);
            }
        }
        if (isFound || GameGLEventListener.isFirst) {
            return;
        }
        gameManage.setFocusGameGui(null);//失去焦点
        if (e.getY() <= (int) (gameManage.getFrame().getHeight() * 0.675f)) {//游戏画面
            if (e.getButton() == BUTTON1) {//左键
                if (user.isBuild()) {
                    Random r = new Random();
                    int x = (int) (user.getMouseOfWorld().x + 0.5f);
                    int y = (int) (user.getMouseOfWorld().z + 0.5f);
                    int z = gameManage.getMap().getHigher(x, y);
                    //   System.out.println("放置" + x + "," + y + "," + z);
                    if (GameMap.isCanMoveHigh(x, y)) {
                        Build b = gameManage.getBuild().getBuildExample(gameManage.getUser().getBuildBuildId());
                        int gold = user.getGold();
                        int water = user.getWater();
                        int rock = user.getRock();
                        int wood = user.getWood();
                        if (gold >= b.getGold() && water >= b.getWater() && rock >= b.getRock() && wood >= b.getWood()) {
                            user.setGold(gold - b.getGold());
                            user.setWater(water - b.getWater());
                            user.setRock(rock - b.getRock());
                            user.setWood(wood - b.getWood());
                            gameManage.getBuild().addBuild(new Flame(gameManage.getUser().getBuildBuildId(), 1, x, y, z, 1f, 0, gameManage.getUser().getTeam()));
                        }
                    }
                }
                user.setIsClicked(true);//点击生效，交给其他类进一步处理
            } else {//右键攻击或者移动
                thread = new Thread() {
                    public void run() {
                        gameManage.getUser().setIsBuild(false, -1);
                        int num = user.getChooseNpcNum();
                        NpcObject[] tempNpc = user.getChooseNpc();
                        NpcObject attackNpc = null;
                        float mx = user.getMouseOfWorld().x;
                        float my = user.getMouseOfWorld().z;
                        if (user.getChoseNpc() != null) {
                            attackNpc = user.getChoseNpc();
                        }
                        //移动
                        for (int i = 0; i < num; i++) {
                            if (tempNpc[i].isEnable()) {
                                if (attackNpc == null) {
                                    if (thread != this) {
                                        return;
                                    }
                                    Point ps = new Point(tempNpc[i].getX(), tempNpc[i].getY());
                                    Point pd = new Point(mx, my);
                                    LinkedList temp = Point.printPath(ps, pd, tempNpc[i].getHigh(), 50);
                                    if (thread != this) {
                                        return;
                                    }
                                    if (temp == null) {
                                        tempNpc[i].setMove(mx, my);
                                        tempNpc[i].setPath(null);
                                    } else {
                                        //Point.sPath(temp);
                                        tempNpc[i].setPath(temp);
                                    }
                                    tempNpc[i].setAttack(null);
                                } else if (!attackNpc.isChoosed()) {
                                    tempNpc[i].setAttack(attackNpc);
                                }
                            }
                        }
                    }
                };
                thread.start();
                //System.out.println("移动" + gameManage.getUser().getMouseOfWorld().x + "," + gameManage.getUser().getMouseOfWorld().z);
            }
        } else if (e.getY() > (int) (gameManage.getFrame().getHeight() * 0.675f) && e.getX() < (int) (gameManage.getFrame().getWidth() * 0.335f)) {//小地图
            float mapX = e.getX() - gameManage.getFrame().getWidth() * 0.046f;
            float mapY = e.getY() - gameManage.getFrame().getHeight() * 0.719f;
            float width = gameManage.getFrame().getWidth() * 0.23f;
            float height = gameManage.getFrame().getHeight() * 0.238f;
            float xUnit = width / GameMap.width;
            float yUnit = height / GameMap.height;
            if (e.getButton() == BUTTON1) {//移动视野
                user.setX(mapX / xUnit);
                user.setY(mapY / yUnit);
            } else {//在小地图上移动小人
                int num = gameManage.getUser().getChooseNpcNum();
                NpcObject[] tempNpc = gameManage.getUser().getChooseNpc();
                for (int i = 0; i < num; i++) {
                    if (tempNpc[i].isEnable()) {
                        Point ps = new Point(tempNpc[i].getX(), tempNpc[i].getY());
                        Point pd = new Point(mapX / xUnit, mapY / yUnit);
                        LinkedList temp = Point.printPath(ps, pd, tempNpc[i].getHigh(), 200);
                        if (temp == null) {
                            tempNpc[i].setMove(mapX / xUnit, mapY / yUnit);
                            tempNpc[i].setPath(null);
                        } else {
                            //Point.sPath(temp);
                            tempNpc[i].setPath(temp);
                        }
                    }
                }
            }
        } else if (gameManage.getUser().getChooseBuildNum() >= 1 || gameManage.getUser().getChooseNpcNum() >= 1) {
            mouseChose(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        tMouseReleaed(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
