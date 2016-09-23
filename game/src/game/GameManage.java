package game;

//********************************************
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.awt.GLJPanel;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import granule.SkillAnimation;
import gui.ChoiceBox;
import gui.GameBar;
import gui.GameGUI;
import gui.MainLoadBar;
import gui.MainMenu;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import map.Map;
import npc.Npc;
import npc.NpcObject;
import skill.skill;

//*类名:GameManage
//*作者:凌恋      时间:2016-8-13 16:38:12
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameManage {

    GLU glu;
    GL2 gl;
    GLUT glut;
    GameJFrame frame;
    GameManage gameManage;
    GameEvent event;
    GameMap map;
    GameNpc npc;
    GameGLEventListener gameGLEventListener;
    GameBuild build;
    GameGLJPanel panel;
    GLJPanel npcPanel;
    GameUser user;
    FPSAnimator animator;
    GameThread thread;
    GameGUI focusGameGui;
    MainMenu mainMenu;
    MainLoadBar mainBar;
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public static LinkedList<GameGUI> gameGUI;
    public static LinkedList<GameGUI> chooseGUI;
    public static LinkedList<SkillAnimation> skill;

    public GameManage() {
        this.gameManage = this;
        gameGUI = new LinkedList();
        chooseGUI = new LinkedList();
        skill = new LinkedList();
        Toolkit kit = Toolkit.getDefaultToolkit();
        mainBar = new MainLoadBar(0, 0, kit.getScreenSize().width, kit.getScreenSize().height, 27f, null);
        mainBar.init();
    }

    public GameGLEventListener getGameGLEventListener() {
        return gameGLEventListener;
    }

    public void setGameGLEventListener(GameGLEventListener gameGLEventListener) {
        this.gameGLEventListener = gameGLEventListener;
    }

    public MainLoadBar getMainBar() {
        return mainBar;
    }

    public void setMainBar(MainLoadBar mainBar) {
        this.mainBar = mainBar;
    }

    public void replay() {
        event.isReplay = true;
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        map.replay(50);
        npc.replay();
        Random r = new Random();
        user.replay(r.nextInt(map.getWidth()), r.nextInt(map.getHeight()));
        event.isReplay = false;
    }

    private void saveFile(OutputStream tOut) {
        OutputStreamWriter out = new OutputStreamWriter(tOut);
        float w = frame.getWidth();
        float h = frame.getHeight();
        GameBar g = new GameBar((int) (w / 2 - w / 10), (int) (h / 2 - h / 10), (int) (w / 5), (int) (h / 5), map.getWidth() * map.getHeight() + npc.getSize(), "正在保存中$(注意，存档文件较大，请不要随便存档)");
        g.init();
        gameManage.addGameGUI(g);
        try {
            out.write("Map:\n");
            for (int k = 0; k < 10; k++) {
                for (int i = 0; i < map.getWidth(); i++) {
                    for (int j = 0; j < map.getHeight(); j++) {
                        out.write(map.getMap(i, j, k).toString() + "\n");
                        g.barNum++;
                    }
                }
            }
            out.write("EndMap\n");
            out.write("Npc:\n");
            int size = npc.getSize();
            for (int i = 0; i < size; i++) {
                if (npc.getNpc(i).isEnable()) {
                    out.write(npc.getNpc(i).toString() + "\n");
                }
                g.barNum++;
            }
            out.write("EndNpc\n");
            g.setViewable(false);
            gameManage.removeGameGUI(g);
            out.flush();
        } catch (IOException ex) {
            System.out.println("地图存取失败");
        }
    }

    private void loadFile(InputStream tIn) {
        gameManage.getMainBar().reset(0, 0, gameManage.getFrame().getWidth(), gameManage.getFrame().getHeight(), 10f + 100f, "正在读取存档ing");
        gameManage.addGameGUI(gameManage.getMainBar());
        BufferedReader in = new BufferedReader(new InputStreamReader(tIn));
        float w = frame.getWidth();
        float h = frame.getHeight();
        String temp;
        try {
            temp = in.readLine();
            gameManage.getMainBar().setText("正在读取地图信息");
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {

            }
            if (temp.equals("Map:")) {
                for (int k = 0; k < 10; k++) {
                    for (int i = 0; i < map.getWidth(); i++) {
                        for (int j = 0; j < map.getHeight(); j++) {
                            temp = in.readLine();
                            int id = Integer.parseInt(temp.substring(0, temp.indexOf(',')));
                            temp = temp.substring(temp.indexOf(',') + 1);
                            int num = Integer.parseInt(temp.substring(0, temp.indexOf(',')));
                            temp = temp.substring(temp.indexOf(',') + 1);
                            float high = Float.parseFloat(temp.substring(0, temp.indexOf(',')));
                            temp = temp.substring(temp.indexOf(',') + 1);
                            boolean isHasBuild;
                            if (temp.indexOf(',') == -1) {
                                isHasBuild = Boolean.getBoolean(temp);
                            } else {
                                isHasBuild = Boolean.getBoolean(temp.substring(0, temp.indexOf(',')));
                            }
                            Map tMap = new Map(i, j, k, id, num, high);
                            tMap.setIsHasBuild(isHasBuild);
                            map.setMap(i, j, k, tMap);
                        }
                    }
                    gameManage.getMainBar().barNum++;
                }
                map.setIsLooked(new boolean[8][map.getWidth()][map.getHeight()]);
                map.setIsLooking(new int[8][map.getWidth()][map.getHeight()]);
                temp = in.readLine();
                if (temp.equals("EndMap")) {
                    gameManage.getMainBar().setText("地图读取完毕");
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException ex) {

                    }
                }
            }
            temp = in.readLine();
            if (temp.equals("Npc:")) {
                gameManage.getMainBar().setText("开始读取人物信息");
                try {
                    Thread.sleep(250);
                } catch (InterruptedException ex) {

                }
                npc.clearNpc();
                while (true) {
                    temp = in.readLine();
                    if (temp.equals("EndNpc")) {
                        gameManage.getMainBar().setText("人物读取完毕");
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException ex) {

                        }
                        break;
                    }
                    int id = Integer.parseInt(temp.substring(0, temp.indexOf(',')));
                    temp = temp.substring(temp.indexOf(',') + 1);
                    float x = Float.parseFloat(temp.substring(0, temp.indexOf(',')));
                    temp = temp.substring(temp.indexOf(',') + 1);
                    float y = Float.parseFloat(temp.substring(0, temp.indexOf(',')));
                    temp = temp.substring(temp.indexOf(',') + 1);
                    float high = Float.parseFloat(temp.substring(0, temp.indexOf(',')));
                    temp = temp.substring(temp.indexOf(',') + 1);
                    float width = Float.parseFloat(temp.substring(0, temp.indexOf(',')));
                    temp = temp.substring(temp.indexOf(',') + 1);
                    float maxHp = Float.parseFloat(temp.substring(0, temp.indexOf(',')));
                    temp = temp.substring(temp.indexOf(',') + 1);
                    float maxMp = Float.parseFloat(temp.substring(0, temp.indexOf(',')));
                    temp = temp.substring(temp.indexOf(',') + 1);
                    float gj = Float.parseFloat(temp.substring(0, temp.indexOf(',')));
                    temp = temp.substring(temp.indexOf(',') + 1);
                    float fy = Float.parseFloat(temp.substring(0, temp.indexOf(',')));
                    temp = temp.substring(temp.indexOf(',') + 1);
                    int team = Integer.parseInt(temp.substring(0, temp.indexOf(',')));
                    temp = temp.substring(temp.indexOf(',') + 1);
                    float addHp;
                    if (temp.indexOf(',') == -1) {
                        addHp = Float.parseFloat(temp);
                    } else {
                        addHp = Float.parseFloat(temp.substring(0, temp.indexOf(',')));
                    }
                    NpcObject tNpc = new NpcObject(id, x, y, high, width,team);
                    tNpc.setMaxHp(maxHp);
                    tNpc.setHp(maxHp);
                    tNpc.setMaxMp(maxMp);
                    tNpc.setMp(maxMp);
                    tNpc.setAddHp(addHp);
                    tNpc.setGj(gj);
                    tNpc.setFy(fy);
                    npc.addNpc(tNpc);
                    gameManage.getMainBar().barNum++;
                }
            }
            gameManage.getMainBar().setText("存档读取完成");
            try {
                Thread.sleep(250);
            } catch (InterruptedException ex) {

            }
            gameManage.getMainBar().setViewable(false);
            gameManage.removeGameGUI(gameManage.getMainBar());
        } catch (IOException ex) {
            System.out.println("地图读取失败");
        }
    }

    public void load(String fileName) {
        File file = new File("saves\\" + fileName + ".sssj");
        try {
            if (file.createNewFile()) {
                panel.setNotice("文件读取失败!请检查文件名是否正确---" + fileName);
            } else {
                Thread t = new Thread() {
                    public void run() {
                        event.isLoad = true;
                        float w = frame.getWidth();
                        float h = frame.getHeight();
                        ChoiceBox box = new ChoiceBox((int) (w / 2 - w / 10), (int) (h / 2 - h / 10), (int) (w / 5), (int) (h / 5), "已经找到存档，是否读取", gameManage);
                        box.init();
                        box.setViewable(false);
                        addGameGUI(box);
                        box.setViewable(true);
                        while (!box.choiced) {
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                System.out.println("休眠失败");
                            }
                        }
                        if (box.choice) {
                            try {
                                loadFile(new FileInputStream(file));
                                panel.setNotice("文件读取成功!");
                            } catch (FileNotFoundException ex) {
                                panel.setNotice("文件读取失败!");
                            }
                        }
                        removeGameGUI(box);
                        event.isLoad = false;
                    }
                };
                t.start();
            }
        } catch (IOException ex) {
            panel.setNotice("文件无法访问!请审核是否具有存取权限");
        }
    }

    public LinkedList<SkillAnimation> getSkill() {
        return skill;
    }

    public void setSkill(LinkedList<SkillAnimation> skill) {
        this.skill = skill;
    }

    public void save(String fileName) {
        File file = new File("saves\\" + fileName + ".sssj");
        try {
            if (file.createNewFile()) {
                saveFile(new FileOutputStream(file));
            } else {
                Thread t = new Thread() {
                    public void run() {
                        event.isSave = true;
                        float w = frame.getWidth();
                        float h = frame.getHeight();
                        ChoiceBox box = new ChoiceBox((int) (w / 2 - w / 10), (int) (h / 2 - h / 10), (int) (w / 5), (int) (h / 5), "已存在同名存档，是否覆盖", gameManage);
                        box.init();
                        box.setViewable(false);
                        addGameGUI(box);
                        box.setViewable(true);
                        while (!box.choiced) {
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                System.out.println("休眠失败");
                            }
                        }
                        if (box.choice) {
                            try {
                                saveFile(new FileOutputStream(file));
                                panel.setNotice("文件保存成功!");
                            } catch (FileNotFoundException ex) {
                                panel.setNotice("文件保存失败!");
                            }
                        }
                        removeGameGUI(box);
                        event.isSave = false;
                    }
                };
                t.start();
            }
        } catch (IOException ex) {
            panel.setNotice("文件无法访问!请审核是否具有存取权限");
        }
    }

    public void addChooseGUI(GameGUI gui) {
        if (chooseGUI.indexOf(gui) == -1) {
            chooseGUI.add(gui);
        }
    }

    public void clearGameGUI() {
        chooseGUI.clear();
    }

    public LinkedList<GameGUI> getChooseGUI() {
        return chooseGUI;
    }

    public void addGameGUI(GameGUI gui) {
        if (gameGUI.indexOf(gui) == -1) {
            gameGUI.add(gui);
        }
    }

    public void removeGameGUI(GameGUI gui) {
        gameGUI.remove(gui);
        if (this.focusGameGui == gui) {
            this.focusGameGui = null;
        }
    }

    public LinkedList<GameGUI> getGameGUI() {
        return gameGUI;
    }

    public GameGUI getFocusGameGui() {
        return focusGameGui;
    }

    public void setFocusGameGui(GameGUI focusGameGui) {
        this.focusGameGui = focusGameGui;
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public void setMainMenu(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
    }

    public GLU getGlu() {
        return glu;
    }

    public void setGlu(GLU glu) {
        this.glu = glu;
    }

    public GL2 getGl() {
        return gl;
    }

    public void setGl(GL2 gl) {
        this.gl = gl;
    }

    public GLUT getGlut() {
        return glut;
    }

    public void setGlut(GLUT glut) {
        this.glut = glut;
    }

    public void setGL(GLU glu, GL2 gl, GLUT glut) {
        this.glu = glu;
        this.gl = gl;
        this.glut = glut;
    }

    public GameThread getThread() {
        try {
            lock.readLock().lock();
            return thread;
        } finally {
            lock.readLock().unlock();
        }
    }

    public GLJPanel getNpcPanel() {
        return npcPanel;
    }

    public void setNpcPanel(GLJPanel npcPanel) {
        this.npcPanel = npcPanel;
    }

    public void setThread(GameThread thread) {
        try {
            lock.writeLock().lock();
            this.thread = thread;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public GameUser getUser() {
        try {
            lock.readLock().lock();
            return user;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setUser(GameUser user) {
        try {
            lock.writeLock().lock();
            this.user = user;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public GameJFrame getFrame() {
        try {
            lock.readLock().lock();
            return frame;
        } finally {
            lock.readLock().unlock();
        }
    }

    public FPSAnimator getAnimator() {
        try {
            lock.readLock().lock();
            return animator;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setAnimator(FPSAnimator animator) {
        try {
            lock.writeLock().lock();
            this.animator = animator;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void setFrame(GameJFrame frame) {
        try {
            lock.writeLock().lock();
            this.frame = frame;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public GameEvent getEvent() {
        try {
            lock.readLock().lock();
            return event;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setEvent(GameEvent event) {
        try {
            lock.writeLock().lock();
            this.event = event;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public GameMap getMap() {
        try {
            lock.readLock().lock();
            return map;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setMap(GameMap map) {
        try {
            lock.writeLock().lock();

            this.map = map;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public GameNpc getNpc() {
        try {
            lock.readLock().lock();
            return npc;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setNpc(GameNpc npc) {
        try {
            lock.writeLock().lock();
            this.npc = npc;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public GameBuild getBuild() {
        try {
            lock.readLock().lock();
            return build;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setBuild(GameBuild build) {
        try {
            lock.writeLock().lock();
            this.build = build;
        } finally {
            lock.writeLock().unlock();
        }
    }

    public GameGLJPanel getPanel() {
        try {
            lock.readLock().lock();
            return panel;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setPanel(GameGLJPanel panel) {
        try {
            lock.writeLock().lock();
            this.panel = panel;
        } finally {
            lock.writeLock().unlock();
        }
    }

}
