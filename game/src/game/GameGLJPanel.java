package game;

//********************************************
import npc.Build;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.awt.GLJPanel;
import static game.GameMap.isLooking;
import gui.ActionAdapter;
import gui.ActionListener;
import gui.Button;
import gui.ChoiceBox;
import gui.Dialog;
import gui.GameBar;
import gui.GameGUI;
import gui.GuiImage;
import gui.InputBox;
import gui.MainLoadBar;
import gui.MainMenu;
import gui.StartMainMenu;
import gui.TextField;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import npc.Npc;
import npc.NpcObject;
import toolkit.GameString;
import toolkit.Notice;

//*类名:GameGLJPanel
//*作者:凌恋      时间:2016-8-13 16:59:57
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameGLJPanel extends GLJPanel {

    LinkedList<Notice> strList;
    LinkedList<GameString> strLinkedList;
    Image[] uiImage;
    Color[] color;
    int uiNumber = 14;
    public static Font gameFont;
    public static Image[] gnImage;
    public static int gnImageNumber = 42;
    public static Image[] jnImage;
    public static int jnImageNumber = 36;
    public static Image[] zbImage;
    public static int zbImageNumber = 18;
    public static Image[] ztImage;
    public static int ztImageNumber = 12;
    public static Image[] buildImage;
    public static Image kongImage;
    public static int buildImageNumber = 20;
    GameManage gameManage;
    int lastWidth;
    int lastHeight;
    float changValueY;
    float changValueX;
    int fontSize;

    public GameGLJPanel(GLCapabilities capabilities, GameManage gameManage) {
        super(capabilities);
        this.gameManage = gameManage;
        strList = new LinkedList();
        strLinkedList = new LinkedList();
    }

    public LinkedList<GameString> getStrLinkedList() {
        return strLinkedList;
    }

    public void setNotice(String str) {
        int x = (int) (gameManage.getFrame().getWidth() * 0.03f);
        int y = (int) (gameManage.getFrame().getHeight() * 0.67f);
        strList.add(new Notice(str, x, y, 50));
    }

    public void init() {
        try {
            if (GameGLJPanel.gameFont == null) {
                GameGLJPanel.gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("font\\MainFont.ttf"));
                System.out.println("字体加载成功");
                if (GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(GameGLJPanel.gameFont)) {
                    System.out.println("字体放入系统");
                }
            }
        } catch (FontFormatException ex) {
            System.out.println(ex.getMessage() + "——:GameGLJPanel--panel(Graphics)");
            GameGLJPanel.gameFont = null;
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + "——:GameGLJPanel--panel(Graphics)");
            GameGLJPanel.gameFont = null;
        }
        Toolkit kit = Toolkit.getDefaultToolkit();
        StartMainMenu s = new StartMainMenu(0, 0, kit.getScreenSize().width, kit.getScreenSize().height, gameManage);
        s.init();
        gameManage.addGameGUI(s);
    }

    public void init2() {
        //读取状态图片
        ztImage = new Image[ztImageNumber];
        for (int i = 0; i < ztImageNumber; i++) {
            try {
                if (i < 9) {
                    ztImage[i] = ImageIO.read(new File("image\\zhuangtai\\zhuangtai_0" + (i + 1) + ".png"));
                } else {
                    ztImage[i] = ImageIO.read(new File("image\\zhuangtai\\zhuangtai_" + (i + 1) + ".png"));
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + "状态图片文件读取失败" + i);
            }
        }
        //读取功能图片
        gnImage = new Image[gnImageNumber];
        for (int i = 0; i < gnImageNumber; i++) {
            try {
                if (i < 9) {
                    gnImage[i] = ImageIO.read(new File("image\\gongneng\\gongneng_0" + (i + 1) + ".png"));
                } else {
                    gnImage[i] = ImageIO.read(new File("image\\gongneng\\gongneng_" + (i + 1) + ".png"));
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + "功能图片文件读取失败" + i);
            }
        }
        //读取装备图片
        zbImage = new Image[zbImageNumber];
        for (int i = 0; i < zbImageNumber; i++) {
            try {
                if (i < 9) {
                    zbImage[i] = ImageIO.read(new File("image\\zhuangbei\\zhuangbei_0" + (i + 1) + ".png"));
                } else {
                    zbImage[i] = ImageIO.read(new File("image\\zhuangbei\\zhuangbei_" + (i + 1) + ".png"));
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + "装备图片文件读取失败" + i);
            }
        }
        //读取技能图片
        jnImage = new Image[jnImageNumber];
        for (int i = 0; i < jnImageNumber; i++) {
            try {
                if (i < 9) {
                    jnImage[i] = ImageIO.read(new File("image\\jineng\\jineng_0" + (i + 1) + ".png"));
                } else {
                    jnImage[i] = ImageIO.read(new File("image\\jineng\\jineng_" + (i + 1) + ".png"));
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + "技能图片文件读取失败" + i);
            }
        }
        //读取UI图片
        uiImage = new Image[uiNumber];
        for (int i = 0; i < uiNumber; i++) {
            try {
                uiImage[i] = ImageIO.read(new File("image\\UI\\MainUI_" + i + ".png"));
            } catch (IOException ex) {
                System.out.println(ex.getMessage() + "UI文件读取失败");
            }
        }

        buildImage = new Image[buildImageNumber];
        try {
            buildImage[0] = ImageIO.read(new File("image\\build\\huodui.png"));
            buildImage[1] = ImageIO.read(new File("image\\build\\zhangpeng.png"));
            buildImage[2] = ImageIO.read(new File("image\\build\\fangzi.png"));
            buildImage[3] = ImageIO.read(new File("image\\build\\jianta.png"));
            kongImage = ImageIO.read(new File("image\\build\\kong.png"));
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + "建筑文件读取失败");
        }
        color = new Color[9];
        color[0] = new Color(0f, 0f, 0f);
        color[1] = new Color(1f, 1f, 0f);
        color[2] = new Color(0.2f, 0.5f, 0.9f);
        color[3] = new Color(0f, 1f, 0f);
        color[4] = new Color(0f, 1f, 1f);
        color[5] = new Color(0f, 0f, 1f);
        color[6] = new Color(1f, 0f, 1f);
        color[7] = new Color(0f, 0.8f, 0.25f);
        color[8] = new Color(0.8f, 0.8f, 0.8f);

        float w = this.getWidth();
        float h = this.getHeight();
        MainMenu mainMenu = new MainMenu((int) (w * 0.35f), (int) (h * 0.2f), (int) (w * 0.3f), (int) (h * 0.6f), gameManage);
        mainMenu.init();
        mainMenu.setViewable(false);
        gameManage.setMainMenu(mainMenu);
        gameManage.addGameGUI(mainMenu);
    }

    public void drawUI(Graphics g) {
        //画大框
        g.drawImage(uiImage[0], 0, 0, this.getWidth(), this.getHeight(), null);
        //g.drawImage(uiImage[1],0,0,this.getWidth(),this.getHeight(),null);
        //画小地图
        g.drawImage(uiImage[2], (int) (this.getWidth() * 0.02f), (int) (this.getHeight() * 0.675f), (int) (this.getWidth() * 0.315f), (int) (this.getHeight() * 0.3025f), null);
        //画人物栏
        g.drawImage(uiImage[5], (int) (this.getWidth() * 0.525f), (int) (this.getHeight() * 0.73f), (int) (this.getWidth() * 0.43f), (int) (this.getHeight() * 0.22f), null);
        g.drawImage(uiImage[3], (int) (this.getWidth() * 0.332f), (int) (this.getHeight() * 0.675f), (int) (this.getWidth() * 0.65f), (int) (this.getHeight() * 0.3025f), null);
        //画功能框
        g.drawImage(uiImage[8], (int) (this.getWidth() * 0.7f), (int) (this.getHeight() * 0.73f), (int) (this.getWidth() * 0.255f), (int) (this.getHeight() * 0.218), null);
        if (gameManage.getUser().chooseNpcNum <= 0) {
            g.drawImage(uiImage[7], (int) (this.getWidth() * 0.345f), (int) (this.getHeight() * 0.77f), (int) (this.getWidth() * 0.15f), (int) (this.getHeight() * 0.19f), null);
        } else if (gameManage.getUser().chooseNpcNum >= 1) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 5; j++) {
                    g.setColor(Color.red);
                    if (buildImage[i*4+j] != null) {
                        g.drawImage(buildImage[i*5+j], (int) ((0.702f + j * 0.05f) * this.getWidth()), (int) ((0.73f + 0.055f * i) * this.getHeight()), (int) (0.049f * this.getWidth()), (int) (0.053f * this.getHeight()), this);
                    } else {
                        g.drawImage(kongImage, (int) ((0.702f + j * 0.05f) * this.getWidth()), (int) ((0.73f + 0.055f * i) * this.getHeight()), (int) (0.049f * this.getWidth()), (int) (0.053f * this.getHeight()), this);
                    }
                }
            }
        }
        for (int i = 0; i < 5; i++) {
            g.drawImage(uiImage[i + 9], (int) (this.getWidth() * (0.565f + 0.085f * i)), (int) (this.getHeight() * 0.03f), (int) (this.getWidth() * 0.03f), (int) (this.getHeight() * 0.03f), null);
        }

    }

    public void drawMap(Graphics g) {
        GameMap map = gameManage.getMap();
        GameNpc npc = gameManage.getNpc();
        Image temp = new BufferedImage(map.getWidth() * 5, map.getHeight() * 5, BufferedImage.TYPE_INT_RGB);
        Graphics tg = temp.getGraphics();
        GameBuild build = gameManage.getBuild();
        int size = build.getSize();
        tg.setColor(Color.blue);
        int team = gameManage.user.team;
        for (int i = 0; i < size; i++) {
            if (build.getBuild(i).getTeam() == team) {
                tg.setColor(Color.blue);
            } else {
                tg.setColor(Color.red);
            }
            if (build.getBuild(i).isEnable() && GameMap.isLooked[team][(int)build.getBuild(i).x][(int)build.getBuild(i).y]) {
                int x = (int)build.getBuild(i).getX();
                int y = (int)build.getBuild(i).getY();
                tg.fillRect(x * 5, y * 5, 5, 5);
            }
        }
        tg.setColor(Color.green);
        size = npc.getSize();
        for (int i = 0; i < size; i++) {
            if (npc.getNpc(i).getTeam() == team) {
                tg.setColor(Color.green);
            } else {
                tg.setColor(Color.red);
            }
            if (npc.getNpc(i).isEnable() && isLooking[team][(int) (npc.getNpc(i).getX() + 0.5f)][(int) (npc.getNpc(i).getY() + 0.5f)] - GameThread.gameTime > 0) {
                int x = (int) npc.getNpc(i).getX();
                int y = (int) npc.getNpc(i).getY();
                tg.fillRect(x * 5, y * 5, 5, 5);
            }
        }
        int range = gameManage.getUser().range;
        int tx = (int) gameManage.getUser().getX() - range;
        int ty = (int) gameManage.getUser().getY() - range;
        tg.setColor(Color.white);
        tg.drawRect(5 * tx - 2, 5 * ty - 2, range * 10 + 4, range * 10 + 4);
        tg.drawRect(5 * tx - 1, 5 * ty - 1, range * 10 + 2, range * 10 + 2);
        tg.drawRect(5 * tx, 5 * ty, range * 10, range * 10);
        tg.drawRect(5 * tx + 1, 5 * ty + 1, range * 10 - 2, range * 10 - 2);
        tg.drawRect(5 * tx + 2, 5 * ty + 2, range * 10 - 4, range * 10 - 4);
        g.drawImage(temp, (int) (this.getWidth() * 0.046f), (int) (this.getHeight() * 0.719f), (int) (this.getWidth() * 0.23f), (int) (this.getHeight() * 0.238f), this);
    }

    public void drawChooseRect(Graphics g) {
        GameUser user = gameManage.getUser();
        g.setColor(Color.GREEN);
        int x = user.getMouseX();
        int y = user.getMouseY();
        int rx = user.getReleasedMouseX();
        int ry = user.getReleasedMouseY();
        int dx = Math.abs(rx - x);
        int dy = Math.abs(ry - y);
        if (x < rx && y < ry) {
            g.drawRect(x, y, dx, dy);
        } else if (x > rx && y < ry) {
            g.drawRect(rx, y, dx, dy);
        } else if (x > rx && y > ry) {
            g.drawRect(rx, ry, dx, dy);
        } else if (x < rx && y > ry) {
            g.drawRect(x, ry, dx, dy);
        }
    }

    public void drawString(Graphics g) {
        Font lastFont = g.getFont();
        GameJFrame frame = gameManage.getFrame();
        if (lastWidth != gameManage.getFrame().getWidth() || lastHeight != frame.getHeight()) {
            changValueX = (float) lastWidth / (float) frame.getWidth();
            changValueY = (float) lastHeight / (float) frame.getHeight();
            lastWidth = frame.getWidth();
            lastHeight = frame.getHeight();
            if (changValueX != 0 && changValueY != 0) {
                for (int i = 0; i < strList.size(); i++) {
                    strList.get(i).setX((int) (strList.get(i).getX() / changValueX));
                    strList.get(i).setY((int) (strList.get(i).getY() / changValueY));
                }
            }
            gameFont = gameFont.deriveFont(Font.PLAIN, (int) (lastHeight * 0.02f));
        }
        int size = strList.size();
        Notice[] re = new Notice[size];
        int num = 0;
        for (int i = 0; i < size; i++) {
            Color lastColor = g.getColor();
            g.setColor(Color.red);
            gameFont = gameFont.deriveFont(Font.PLAIN, (int) (lastHeight * 0.02f));
            g.setFont(gameFont);
            Notice n = strList.getFirst();
            g.drawString(n.getStr(), n.getX(), n.getY() - gameFont.getSize() * i);
            g.setColor(lastColor);
            int time = n.getLifeTime();
            n.setLifeTime(time - 1);
            if (time - 1 <= 0) {
                re[num++] = n;
            }
        }
        for (int i = 0; i < num; i++) {
            strList.remove(re[i]);
        }
        size = strLinkedList.size();
        GameString[] re2 = new GameString[size];
        num = 0;
        for (int i = 0; i < size; i++) {
            GameString n = strLinkedList.getFirst();
            gameFont = gameFont.deriveFont(Font.PLAIN, (int) (lastHeight * n.getSize()));
            Color lastColor = g.getColor();
            g.setColor(n.getColor());
            g.setFont(gameFont);
            g.drawString(n.getStr(), n.getX(), n.getY());
            g.setColor(lastColor);
            int time = n.getLifeTime();
            n.setLifeTime(time - 1);
            if (time - 1 <= 0) {
                re2[num++] = n;
            }
        }
        for (int i = 0; i < num; i++) {
            strLinkedList.remove(re2[i]);
        }
        g.setFont(lastFont);
    }

    public void drawNpcState(Graphics g) {
        GameNpc npc = gameManage.getNpc();
        GameUser user = gameManage.getUser();
        int range = user.range;
        int x = (int) user.getX();
        int y = (int) user.getY();
        int team = gameManage.user.team;
        for (int i = 0; i < npc.getSize(); i++) {
            NpcObject tNpcObject = npc.getNpc(i);
            if (tNpcObject.isEnable() && isLooking[team][(int) (tNpcObject.getX() + 0.5f)][(int) (tNpcObject.getY() + 0.5f)] - GameThread.gameTime > 0) {
                if (tNpcObject.getX() >= x - range && tNpcObject.getX() < x + range && tNpcObject.getY() >= y - range && tNpcObject.getY() < y + range) {
                    int tx = Math.round(tNpcObject.getScrnPoint().x);
                    int ty = Math.round(tNpcObject.getScrnPoint().y);
                    int width = gameManage.getFrame().getWidth();
                    int height = gameManage.getFrame().getHeight();
                    if (ty >= (int) (this.getHeight() * 0.675f) || tx < width * 0.08f || tx >= width * 0.92f || ty < height * 0.08f || ty >= height * 0.92f) {
                        continue;
                    }
                    double hp = tNpcObject.getHp();
                    if (hp <= 0) {
                        hp = 0;
                    }
                    double maxHp = tNpcObject.getMaxHp();
                    float temp = (float) (hp / maxHp);
                    if (temp < 0) {
                        temp = 0;
                    } else if (temp > 1) {
                        temp = 1;
                    }
                    g.setColor(Color.black);
                    g.fillRect(tx, ty, (int) tNpcObject.getWide(), 10);
                    Color c = new Color(1 - temp, temp, 0);
                    g.setColor(c);
                    g.fillRect(tx + 1, ty + 1, (int) (tNpcObject.getWide() * temp) - 2, 8);
                }
            }
        }
    }

    public void drawGUI(Graphics g) {
        int size = gameManage.getGameGUI().size();
        for (int i = 0; i < size; i++) {
            GameGUI temp = gameManage.getGameGUI().get(i);
            temp.draw(g);
        }
    }

    public void drawNpcBase(Graphics g) {
        Font lastFont = g.getFont();
        int num = gameManage.getUser().getChooseNpcNum();
        NpcObject[] tNpc = gameManage.getUser().getChooseNpc();
        if (num >= 1) {
            //获得字体大小
            gameFont = gameFont.deriveFont(Font.PLAIN, (int) (lastHeight * 0.025f));
            g.setFont(gameFont);
            FontMetrics tfm = g.getFontMetrics(g.getFont());
            int fontW = tfm.charWidth('a');
            int fontH = tfm.getHeight();
            //获得npc基础属性
            NpcObject npc = tNpc[0];
            int hp = (int) npc.getHp();
            int maxHp = (int) npc.getMaxHp();
            int mp = (int) npc.getMp();
            int maxMp = (int) npc.getMaxMp();
            int gj = (int) npc.getGj();
            int fy = (int) npc.getFy();
            int w = (int) (this.getWidth() * 0.365f);
            int h = (int) (this.getHeight() * 0.975f);
            //画Hp
            float temp = (float) (npc.getHp() / npc.getMaxHp());
            if (temp < 0) {
                temp = 0;
            } else if (temp > 1) {
                temp = 1;
            }
            g.setColor(new Color(1 - temp, temp, 0));
            String str = getString(hp);
            int lenth = str.length();//计算hp的值
            g.drawString(str, w, h);
            g.setColor(Color.LIGHT_GRAY);
            str = getString(maxHp);
            g.drawString("/" + str, w + lenth * fontW, h);
            lenth += str.length() + 1;//计算最大hp
            //画Mp
            temp = (float) (npc.getMp() / npc.getMaxMp());
            if (temp < 0) {
                temp = 0;
            } else if (temp > 1) {
                temp = 1;
            }
            g.setColor(new Color(1 - temp, 0, temp));
            str = getString(mp);
            lenth += 2;
            g.drawString(str, w + lenth * fontW, h);
            lenth += str.length();//计算Mp
            g.setColor(Color.LIGHT_GRAY);
            str = getString(maxMp);
            g.drawString("/" + str, w + lenth * fontW, h);
            w = (int) (this.getWidth() * 0.535f);
            h = (int) (this.getHeight() * 0.75f);
            int rectW = (int) (this.getWidth() * 0.025f);
            int rectH = (int) (this.getHeight() * 0.025f);
            int cha = fontH / 3 + rectH;
            //画攻击力
            int i = 0;
            g.drawImage(gnImage[29], w, h + cha * i, rectW, rectH, null);
            g.drawImage(uiImage[6], w, h + cha * i, rectW, rectH, null);
            str = getString(gj);
            g.drawString(str, w + rectW + fontW / 3, h + cha * i + fontH / 2);
            i++;
            //画防御力
            g.drawImage(gnImage[4], w, h + cha * i, rectW, rectH, this);
            g.drawImage(uiImage[6], w, h + cha * i, rectW, rectH, null);
            str = getString(fy);
            g.drawString(str, w + rectW + fontW / 3, h + cha * i + fontH / 2);
            i++;
            //画回复生命值
            g.drawImage(gnImage[3], w, h + cha * i, rectW, rectH, this);
            g.drawImage(uiImage[6], w, h + cha * i, rectW, rectH, null);
            str = getString(npc.getAddHp() * gameManage.getUser().getGameSpeed());
            g.drawString(str, w + rectW + fontW / 3, h + cha * i + fontH / 2);
            i++;
            //画回复法力
            g.drawImage(gnImage[27], w, h + cha * i, rectW, rectH, this);
            g.drawImage(uiImage[6], w, h + cha * i, rectW, rectH, null);
            str = getString(npc.getAddMp() * gameManage.getUser().getGameSpeed());
            g.drawString(str, w + rectW + fontW / 3, h + cha * i + fontH / 2);
            i++;
            //画移动速度
            g.drawImage(gnImage[1], w, h + cha * i, rectW, rectH, this);
            g.drawImage(uiImage[6], w, h + cha * i, rectW, rectH, null);
            str = getString((int) (npc.getSpeed() * 100 * gameManage.getUser().getGameSpeed()));
            g.drawString(str, w + rectW + fontW / 3, h + cha * i + fontH / 2);
            i++;
            //画攻击速度
            g.drawImage(gnImage[16], w, h + cha * i, rectW, rectH, this);
            g.drawImage(uiImage[6], w, h + cha * i, rectW, rectH, null);
            double speed = npc.getSpeedFight() * gameManage.getUser().getGameSpeed();
            double maxSpeed = Npc.MAX_SPEED_FIGHT;
            double tempS = speed / maxSpeed;
            tempS = (int) (tempS * 100);
            str = String.valueOf((float) tempS / 100);
            g.drawString(str, w + rectW + fontW / 3, h + cha * i + fontH / 2);
            i++;

            //坐标
            /*
             g.drawImage(gnImage[23], w, h + cha * i, rectW, rectH, this);
             g.drawImage(uiImage[6], w, h + cha * i, rectW, rectH, null);
             str = getString((int) npc.getX());
             g.drawString(str + ',', w + rectW + fontW / 3, h + cha * i + fontH / 2);
             lenth = str.length() + 1;
             str = getString((int) npc.getY());
             g.drawString(str + ',', w + rectW + fontW / 3 + fontW * lenth, h + cha * i + fontH / 2);
             lenth += str.length() + 1;
             str = getString((int) npc.getHigh());
             g.drawString(str, w + rectW + fontW / 3 + fontW * lenth, h + cha * i + fontH / 2);
             i++;//
             */
        }
        g.setFont(lastFont);
    }

    public void drawBuildBase(Graphics g) {
        int num = gameManage.getUser().getChooseBuildNum();
        if (num != 0) {
            Build[] tBuild = gameManage.getUser().getChooseBuild();
            Font lastFont = g.getFont();
            g.setFont(lastFont);
            gameFont = gameFont.deriveFont(Font.PLAIN, (int) (lastHeight * 0.025f));
            g.setFont(gameFont);
            FontMetrics tfm = g.getFontMetrics(g.getFont());
            int fontW = tfm.charWidth('a');
            int fontH = tfm.getHeight();
            //获得npc基础属性
            Build build = tBuild[0];
            int hp = (int) build.getHp();
            int maxHp = (int) build.getMaxHp();
            int mp = (int) build.getMp();
            int maxMp = (int) build.getMaxMp();
            int gj = (int) build.getGj();
            int fy = (int) build.getFy();
            int w = (int) (this.getWidth() * 0.365f);
            int h = (int) (this.getHeight() * 0.975f);
            //画Hp
            float temp = (float) (build.getHp() / build.getMaxHp());
            if (temp < 0) {
                temp = 0;
            } else if (temp > 1) {
                temp = 1;
            }
            g.setColor(new Color(1 - temp, temp, 0));
            String str = getString(hp);
            int lenth = str.length();//计算hp的值
            g.drawString(str, w, h);
            g.setColor(Color.LIGHT_GRAY);
            str = getString(maxHp);
            g.drawString("/" + str, w + lenth * fontW, h);
            lenth += str.length() + 1;//计算最大hp
            //画Mp
            temp = (float) (build.getMp() / build.getMaxMp());
            if (temp < 0) {
                temp = 0;
            } else if (temp > 1) {
                temp = 1;
            }
            g.setColor(new Color(1 - temp, 0, temp));
            str = getString(mp);
            lenth += 2;
            g.drawString(str, w + lenth * fontW, h);
            lenth += str.length();//计算Mp
            g.setColor(Color.LIGHT_GRAY);
            str = getString(maxMp);
            g.drawString("/" + str, w + lenth * fontW, h);
            w = (int) (this.getWidth() * 0.535f);
            h = (int) (this.getHeight() * 0.75f);
            int rectW = (int) (this.getWidth() * 0.025f);
            int rectH = (int) (this.getHeight() * 0.025f);
            int cha = fontH / 3 + rectH;
            int i = 0;
            //画攻击力
            if (gj != 0) {
                g.drawImage(gnImage[29], w, h + cha * i, rectW, rectH, null);
                g.drawImage(uiImage[6], w, h + cha * i, rectW, rectH, null);
                str = getString(gj);
                g.drawString(str, w + rectW + fontW / 3, h + cha * i + fontH / 2);
            }
            i++;
            //画防御力
            if (fy != 0) {
                g.drawImage(gnImage[4], w, h + cha * i, rectW, rectH, this);
                g.drawImage(uiImage[6], w, h + cha * i, rectW, rectH, null);
                str = getString(fy);
                g.drawString(str, w + rectW + fontW / 3, h + cha * i + fontH / 2);
            }
            i++;
            //画回复生命值
            g.drawImage(gnImage[3], w, h + cha * i, rectW, rectH, this);
            g.drawImage(uiImage[6], w, h + cha * i, rectW, rectH, null);
            str = getString(build.getAddHp() * gameManage.getUser().getGameSpeed());
            g.drawString(str, w + rectW + fontW / 3, h + cha * i + fontH / 2);
            i++;
            //画回复法力
            g.drawImage(gnImage[27], w, h + cha * i, rectW, rectH, this);
            g.drawImage(uiImage[6], w, h + cha * i, rectW, rectH, null);
            str = getString(build.getAddMp() * gameManage.getUser().getGameSpeed());
            g.drawString(str, w + rectW + fontW / 3, h + cha * i + fontH / 2);
            i++;
            //画移动速度
            if (build.getSpeed() != 0) {
                g.drawImage(gnImage[1], w, h + cha * i, rectW, rectH, this);
                g.drawImage(uiImage[6], w, h + cha * i, rectW, rectH, null);
                str = getString((int) (build.getSpeed() * 100 * gameManage.getUser().getGameSpeed()));
                g.drawString(str, w + rectW + fontW / 3, h + cha * i + fontH / 2);
            }
            i++;
            //画攻击速度
            if (gj != 0) {
                g.drawImage(gnImage[16], w, h + cha * i, rectW, rectH, this);
                g.drawImage(uiImage[6], w, h + cha * i, rectW, rectH, null);
                double speed = build.getSpeedFight() * gameManage.getUser().getGameSpeed();
                double maxSpeed = Npc.MAX_SPEED_FIGHT;
                double tempS = speed / maxSpeed;
                tempS = (int) (tempS * 100);
                str = String.valueOf((float) tempS / 100);
                g.drawString(str, w + rectW + fontW / 3, h + cha * i + fontH / 2);
            }
            i++;
        }
    }

    public String getString(double temp) {
        String str;
        String t = "";
        if (temp >= 1000f) {
            temp /= 1000f;
            t = "T";
            if (temp >= 100f) {
                temp /= 100f;
                t = "M";
                if (temp >= 100f) {
                    temp /= 100f;
                    t = "B";
                    if (temp >= 100f) {
                        temp /= 100f;
                        t = "G";
                    }
                }
            }
        }
        str = String.valueOf((int) temp);
        str = str.concat(t);
        return str;
    }

    public void drawResource(Graphics g) {
        Font lastFont = g.getFont();
        gameFont = gameFont.deriveFont(Font.PLAIN, (int) (lastHeight * 0.03f));
        g.setFont(gameFont);
        g.setColor(Color.WHITE);
        //FontMetrics tfm = g.getFontMetrics(g.getFont());
        //int fontW = tfm.charWidth('a');
        //int fontH = tfm.getHeight();
        GameUser user = gameManage.getUser();
        g.drawString(getString(user.getGold()), (int) (this.getWidth() * (0.683f + 0.085f * -1)), (int) (this.getHeight() * 0.05f));
        g.drawString(getString(user.getRock()), (int) (this.getWidth() * (0.683f + 0.085f * 0)), (int) (this.getHeight() * 0.05f));
        g.drawString(getString(user.getWater()), (int) (this.getWidth() * (0.683f + 0.085f * 1)), (int) (this.getHeight() * 0.05f));
        g.drawString(getString(user.getWood()), (int) (this.getWidth() * (0.683f + 0.085f * 2)), (int) (this.getHeight() * 0.05f));
        g.drawString(getString(user.getPopulation())+"/"+getString(user.getMaxPopulation()), (int) (this.getWidth() * (0.675f + 0.085f * 3)), (int) (this.getHeight() * 0.05f));
        g.setFont(lastFont);
    }

    @Override
    public void paint(Graphics g) {
        if (gameManage.getEvent().isReplay || gameManage.getEvent().isLoad || gameManage.getEvent().isSave) {
            g.setColor(Color.black);
            g.fillRect(0, 0, this.getWidth(), this.getHeight());
            drawGUI(g);
            return;
        }
        super.paint(g);
        if (!GameGLEventListener.isFirst) {
            drawUI(g);
            drawResource(g);
            drawMap(g);
            drawString(g);
            drawNpcState(g);
            drawNpcBase(g);
            drawBuildBase(g);
        }
        drawGUI(g);
        if (gameManage.getUser().isDragged()) {
            drawChooseRect(g);
        }
    }
}
