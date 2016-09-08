package game;

//********************************************
import build.Build;
import build.Flame;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
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
    GameManage gameManage;
    public static LinkedList<Game3DS> buildModel;
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public GameBuild(GameManage gameManage) {
        this.gameManage = gameManage;
        build = new LinkedList();
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
        int range = user.range;
        float width;
        GameMap map = gameManage.getMap();
        int team = gameManage.user.team;
        for (int i = 0; i < build.size(); i++) {
            Build tBuild = build.get(i);
            if (tBuild.isEnable()) {
                if (GameMap.isLooked[team][tBuild.x][tBuild.y]) {
                    if (tBuild.getX() >= x - range && tBuild.getX() < x + range && tBuild.getY() >= y - range && tBuild.getY() < y + range) {
                        gl.glPushMatrix();
                        gl.glTranslatef(tBuild.getX(), tBuild.getHigh(), tBuild.getY());
                        gl.glRotatef(tBuild.getFace(), 0f, 1f, 0f);
                        width = tBuild.getWidth();
                        gl.glScaled(width, width, width);
                        buildModel.get(tBuild.getId()).draw(gl);
                        gl.glPopMatrix();
                    }
                }
            }
        }
        gl.glDisable(GL2.GL_BLEND);
        gl.glPopAttrib();
        gl.glPopMatrix();
    }

    public void init(GLAutoDrawable glad) {
        gameManage.getMainBar().setText("正在读取建筑模型");
        buildModel = new LinkedList();
        Game3DS temp = new Game3DS(glad);
        temp.init("model\\3ds\\huodui.3DS", "model\\3ds");
        buildModel.add(temp);
        gameManage.getMainBar().barNum++;
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
            switch(id){
                case 0:build= new Flame(id, 0, x, y, GameMap.getHigh(x, y), 1f, 0f,r.nextInt(7)+1);break;
                default:build = new Build(id,0,0,0,0,0,0,0);break;
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
