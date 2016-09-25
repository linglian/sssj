package game;

//********************************************
import npc.Build;
import npc.Flame;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import static game.GameMap.map;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import npc.NpcObject;
import sd3.Game3DS;

//*类名:GameBuild
//*作者:凌恋      时间:2016-8-13 16:57:18
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameBuild {

    LinkedList<Build> build;
    public static LinkedList<Build> buildExample;
    GameManage gameManage;
    public static LinkedList<Game3DS> buildModel;
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public GameBuild(GameManage gameManage) {
        this.gameManage = gameManage;
        build = new LinkedList();
        buildExample = new LinkedList();
    }

    public void drawBuild(GL2 gl) {
        if (gameManage.getEvent().isReplay) {
            return;
        }
        gl.glPushMatrix();
        gl.glPushAttrib(GL2.GL_CURRENT_BIT);
        gl.glEnable(GL2.GL_BLEND);
        GameUser user = gameManage.getUser();
        int x = (int) user.getX();
        int y = (int) user.getY();
        float mx = user.getMouseOfWorld().x;
        float my = user.getMouseOfWorld().z;
        int range = user.range;
        float width;
        boolean isFirstAttackBuild = true;
        boolean isFirst = true;
        boolean isFound = false;
        GameMap map = gameManage.getMap();
        int team = gameManage.user.team;
        if (gameManage.getUser().getChooseNpcNum() == 0) {
            isFirstAttackBuild = false;
        }
        for (int i = 0; i < build.size(); i++) {
            Build tBuild = build.get(i);
            if (tBuild.isEnable()) {
                if (GameMap.isLooked[team][(int) tBuild.x][(int) tBuild.y]) {
                    if (tBuild.getX() >= x - range && tBuild.getX() < x + range && tBuild.getY() >= y - range && tBuild.getY() < y + range) {
                        gl.glPushMatrix();
                        gl.glTranslatef(tBuild.getX(), tBuild.getHigh(), tBuild.getY());
                        gl.glRotatef(tBuild.getFace(), 0f, 1f, 0f);
                        width = tBuild.getWidth();
                        gl.glColor4f(1f, 1f, 1f, 1f);
                        if (tBuild.getState() != NpcObject.STATE_CHOOSE) {
                            if (isFirst && tBuild.isChoseRange(mx, my)) {
                                isFirst = false;
                                tBuild.setState(NpcObject.STATE_CHOSE);
                            } else {
                                tBuild.setState(NpcObject.STATE_NOTHING);
                            }
                        }
                        switch (tBuild.getState()) {
                            case Build.STATE_CHOSE:
                                gl.glColor4f(0f, 0.6f, 0f, 1f);
                                break;
                            case Build.STATE_CHOOSE:
                                gl.glColor4f(0f, 1f, 0f, 1f);
                                break;
                        }
                        //System.out.println(tBuild.isChoseRange(mx, my)+","+tBuild.isChoosed()+","+isFirstAttackBuild);
                        if (isFirstAttackBuild && tBuild.isChoseRange(mx, my) && !tBuild.isChoosed()) {
                            user.setChoseBuild(tBuild);
                            gl.glColor4f(1f, 0, 0, 1f);
                            //System.out.println("Red");
                            isFirstAttackBuild = false;
                            isFound = true;
                        }
                        gl.glScaled(width, width, width);
                        buildModel.get(tBuild.getId()).draw(gl);
                        gl.glPopMatrix();
                    }
                }
            }
        }
        if (!isFound) {
            user.setChoseBuild(null);
        }
        gl.glDisable(GL2.GL_BLEND);
        gl.glPopAttrib();
        gl.glPopMatrix();
    }

    public void loadBuild() {
        for (int i = 0; i < 20; i++) {
            buildExample.add(new Build(i, 100, 300, 1, 1, 1, 0, 5, 2, 3, 4, 5, 100, 0, 0));
        }
    }

    public Build getBuildExample(int id) {
        return buildExample.get(id);
    }

    public int getBuildModelSize() {
        return buildModel.size();
    }

    public void init(GLAutoDrawable glad) {
        gameManage.getMainBar().setText("正在读取建筑模型");
        buildModel = new LinkedList();
        Game3DS temp = new Game3DS(glad);
        temp.init("model\\3ds\\huodui.3DS", "model\\3ds");
        buildModel.add(temp);
        gameManage.getMainBar().barNum++;
        temp = new Game3DS(glad);
        temp.init("model\\3ds\\zhangpeng.3DS", "model\\3ds");
        buildModel.add(temp);
        gameManage.getMainBar().barNum++;
        temp = new Game3DS(glad);
        temp.init("model\\3ds\\fangzi.3DS", "model\\3ds");
        buildModel.add(temp);
        gameManage.getMainBar().barNum++;
        temp = new Game3DS(glad);
        temp.init("model\\3ds\\jianta.3DS", "model\\3ds");
        buildModel.add(temp);
        gameManage.getMainBar().barNum++;
        loadBuild();
        replay();
    }

    public void replay() {
        gameManage.getMainBar().setText("正在初始化建筑信息");
        build = new LinkedList();
        Random r = new Random();
        int size = buildModel.size();
        for (int i = 0; i < 100; i++) {
            gameManage.getMainBar().barNum += 1 / 100f;
            int x = r.nextInt(GameMap.width);
            int y = r.nextInt(GameMap.height);
            int id = r.nextInt(size);

            Build build;
            switch (id) {
                case 0:
                    build = new Flame(id, 0, x, y, GameMap.getHigh(x, y), 1f, 0f, r.nextInt(7) + 1);
                    break;
                default:
                    build = new Build(id, 0, 0, 0, 0, 0, 0, 0);
                    break;
            }
            this.build.add(build);
        }
    }

    public void addBuild(Build b) {
        try {
            lock.writeLock().lock();
            build.add(b);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Build getBuild(int n) {
        try {
            lock.writeLock().lock();
            return build.get(n);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void removeBuild(int n) {
        try {
            lock.writeLock().lock();
            build.remove(n);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int getSize() {
        try {
            lock.writeLock().lock();
            return build.size();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
