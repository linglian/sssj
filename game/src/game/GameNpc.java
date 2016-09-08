package game;

//********************************************
import build.Build;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.TextureIO;
import static game.GameMap.isLooking;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import md2.MD2Animate;
import md2.MD2Loader;
import md2.MD2Model;
import npc.Npc;
import npc.NpcObject;

//*类名:GameNpcObject
//*作者:凌恋      时间:2016-8-13 16:57:40
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameNpc {

    private LinkedList<NpcObject> npc;
    public static LinkedList<Npc> npcExample;
    GameManage gameManage;
    public static LinkedList<LinkedList<MD2Loader>> npcModelExample;
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public boolean isChange = false;

    public static MD2Animate[] getAnimate(int id) {
        LinkedList<MD2Loader> temp = npcModelExample.get(id);
        int size = temp.size();
        MD2Animate[] animate = new MD2Animate[size];
        for (int i = 0; i < size; i++) {
            animate[i] = new MD2Animate(temp.get(i).get3DModel());
        }
        return animate;
    }

    public GameNpc(GameManage gameManage) {
        this.gameManage = gameManage;
        npc = new LinkedList();
        npcExample = new LinkedList();
        if (npcModelExample == null) {
            npcModelExample = new LinkedList();
        }
    }

    public void saveNpc(File file) {
        try {
            OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(file));
            out.write(npc.get(0).toString2() + "\n");
            out.flush();
        } catch (FileNotFoundException ex) {
            System.out.println("保存失败");
        } catch (IOException ex) {
            System.out.println("保存失败");
        }
    }

    public void loadNpc(File file) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String temp = in.readLine();
            while (temp != null) {
                int id = Integer.parseInt(temp.substring(0, temp.indexOf(',')));
                temp = temp.substring(temp.indexOf(',') + 1);
                double maxHp = Double.parseDouble(temp.substring(0, temp.indexOf(',')));
                temp = temp.substring(temp.indexOf(',') + 1);
                double maxMp = Double.parseDouble(temp.substring(0, temp.indexOf(',')));
                temp = temp.substring(temp.indexOf(',') + 1);
                double gj = Double.parseDouble(temp.substring(0, temp.indexOf(',')));
                temp = temp.substring(temp.indexOf(',') + 1);
                double fy = Double.parseDouble(temp.substring(0, temp.indexOf(',')));
                temp = temp.substring(temp.indexOf(',') + 1);
                double addHp = Double.parseDouble(temp.substring(0, temp.indexOf(',')));
                temp = temp.substring(temp.indexOf(',') + 1);
                double addMp = Double.parseDouble(temp.substring(0, temp.indexOf(',')));
                temp = temp.substring(temp.indexOf(',') + 1);
                double pay = Double.parseDouble(temp.substring(0, temp.indexOf(',')));
                temp = temp.substring(temp.indexOf(',') + 1);
                float attackRange = Float.parseFloat(temp.substring(0, temp.indexOf(',')));
                temp = temp.substring(temp.indexOf(',') + 1);
                double speed = Double.parseDouble(temp.substring(0, temp.indexOf(',')));
                temp = temp.substring(temp.indexOf(',') + 1);
                double speedFight = Double.parseDouble(temp.substring(0, temp.indexOf(',')));
                temp = temp.substring(temp.indexOf(',') + 1);
                int view;
                if (temp.indexOf(',') == -1) {
                    view = Integer.parseInt(temp);
                } else {
                    view = Integer.parseInt(temp.substring(0, temp.indexOf(',')));
                }
                Npc tNpc = new Npc(id, maxHp, maxMp, gj, fy, addHp, addMp, pay, attackRange, speed, speedFight, view);
                npcExample.add(tNpc);
                temp = in.readLine();
            }
        } catch (FileNotFoundException ex) {
            System.out.println("读取失败");
        } catch (IOException ex) {
            Logger.getLogger(GameNpc.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void clearNpc() {
        npc.clear();
    }

    public void replay() {
        gameManage.getMainBar().setText("正在初始化人物信息");
        npc = new LinkedList();
        Random r = new Random();
        int size = npcModelExample.size();
        for (int i = 0; i < 100; i++) {
            gameManage.getMainBar().barNum += 1 / 100f;
            int x = r.nextInt(GameMap.width);
            int y = r.nextInt(GameMap.height);
            NpcObject tNpc = new NpcObject(r.nextInt(size), x, y, GameMap.getHigh(x, y), 0.02f,r.nextInt(7)+1);
            tNpc.init(npcExample.get(tNpc.getId()));
            gameManage.getNpc().addNpc(tNpc);
        }
    }

    public void init(GLAutoDrawable glad) {
        if (npcModelExample.size() == 0) {
            gameManage.getMainBar().setText("开始读取人物模型");
            LinkedList<MD2Loader> temp;
            //读取士兵
            int num = 0;
            for (int i = 0; i < 5; i++) {
                gameManage.getMainBar().setText("正在读取" + (num + 1) + "号模型");
                temp = new LinkedList();
                temp.add(new MD2Loader(glad, "model\\md2\\shibing_" + i + "_0.MD2", "model\\md2\\shibing_" + i + "_0.jpg"));
                temp.add(new MD2Loader(glad, "model\\md2\\shibing_" + i + "_1.MD2", "model\\md2\\shibing_" + i + "_1.jpg"));
                npcModelExample.add(temp);
                num++;
                gameManage.getMainBar().barNum++;
            }
            for (int j = 0; j < 11; j++) {
                gameManage.getMainBar().setText("正在读取" + (num + 1) + "号模型");
                temp = new LinkedList();
                temp.add(new MD2Loader(glad, "model\\md2\\yaoguai_" + j + "_0.MD2", "model\\md2\\yaoguai_" + j + "_0.jpg"));
                temp.add(new MD2Loader(glad, "model\\md2\\yaoguai_" + j + "_1.MD2", "model\\md2\\yaoguai_" + j + "_1.jpg"));
                npcModelExample.add(temp);
                num++;
                gameManage.getMainBar().barNum++;
            }
        }
        loadNpc(new File("npc\\npc.txt"));
        replay();
    }

    public void drawNpc(GL2 gl) {
        if (gameManage.getEvent().isReplay) {
            return;
        }
        gl.glPushMatrix();
        gl.glPushAttrib(GL2.GL_CURRENT_BIT);
        gl.glEnable(GL2.GL_BLEND);
        GameUser user = gameManage.getUser();
        int x = (int) user.getX();
        int y = (int) user.getY();
        float mx = 0;
        float my = 0;
        int range = user.range;
        mx = user.getMouseOfWorld().x;
        my = user.getMouseOfWorld().z;
        float width;
        GameMap map = gameManage.getMap();
        boolean isFirstAttackNpc = true;
        boolean isFirst = true;
        boolean isFound = false;
        if (gameManage.getUser().getChooseNpcNum() == 0) {
            isFirstAttackNpc = false;
        }
        int team = gameManage.user.team;
        for (int i = 0; i < npc.size(); i++) {
            NpcObject tNpcObject = npc.get(i);
            float tx = tNpcObject.getX();
            float ty = tNpcObject.getY();
            if (tNpcObject.isEnable()&&isLooking[team][(int)(tx+0.5f)][(int)(ty+0.5f)]-GameThread.gameTime>0) {
                if (tx >= x - range && tx < x + range && ty >= y - range && ty< y + range) {
                    gl.glPushMatrix();
                    gl.glTranslatef(tNpcObject.getX(), tNpcObject.getHigh() + 0.5f, tNpcObject.getY());
                    if (tNpcObject.getState() != NpcObject.STATE_CHOOSE) {
                        if (isChange) {
                            if (isFirst && tNpcObject.isChoseRange(mx, my)) {
                                isFirst = false;
                                tNpcObject.setState(NpcObject.STATE_CHOSE);
                            } else {
                                tNpcObject.setState(0);
                            }
                        }
                    }
                    switch (tNpcObject.getState()) {
                        case NpcObject.STATE_CHOSE:
                            gl.glColor4f(0.6f, 0.6f, 0.6f, 1f);
                            break;
                        case NpcObject.STATE_CHOOSE:
                            gl.glColor4f(1f, 1f, 1f, 1f);
                            break;
                        default:
                            gl.glColor4f(0.3f, 0.3f, 0.3f, 1f);
                            break;
                    }
                    if (isFirstAttackNpc && tNpcObject.isChoseRange(mx, my) && !tNpcObject.isChoosed()) {
                        user.setChoseNpc(tNpcObject);
                        gl.glColor4f(1f, 0, 0, 1f);
                        isFirstAttackNpc = false;
                        isFound = true;
                    }
                    gl.glRotatef(tNpcObject.getFace(), 0f, 1f, 0f);
                    width = tNpcObject.getWidth();
                    gl.glScaled(width, width, width);
                    tNpcObject.draw(gl, true);
                    gl.glPopMatrix();
                }
            }
        }
        if (!isFound) {
            user.setChoseNpc(null);
        }
        isChange = false;
        gl.glDisable(GL2.GL_BLEND);
        gl.glPopAttrib();
        gl.glPopMatrix();
    }

    public void addNpc(NpcObject b) {
        try {
            lock.writeLock().lock();
            npc.add(b);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public NpcObject getNpc(int n) {
        try {
            lock.readLock().lock();
            return npc.get(n);
        } finally {
            lock.readLock().unlock();
        }
    }

    public void removeNpc(int n) {
        try {
            lock.writeLock().lock();
            npc.remove(n);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int getSize() {
        try {
            lock.readLock().lock();
            return npc.size();
        } finally {
            lock.readLock().unlock();
        }
    }
}
