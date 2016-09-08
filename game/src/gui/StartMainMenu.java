package gui;

//********************************************
import game.GameGLEventListener;
import game.GameManage;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

//*类名:StartMainMenu
//*作者:凌恋      时间:2016-8-20 23:02:21
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class StartMainMenu extends Dialog {

    public static Image[] uiImage;
    public static Image[][] menuImage;
    public static boolean isFirst = true;
    Button[] button;
    GuiImage[][] guiImage2;
    GameManage gameManage;
    int number = 6;

    public StartMainMenu(int x, int y, int width, int height, GameManage gameManage) {
        super(x, y, width, height);
        this.gameManage = gameManage;
        guiImage2 = new GuiImage[2][];
        guiImage2[0] = new GuiImage[number];
        guiImage2[1] = new GuiImage[number];
        button = new Button[6];
        this.isDragged = false;
    }

    public void init() {
        if (isFirst) {
            uiImage = new Image[5];
            menuImage = new Image[2][];
            menuImage[0] = new Image[number];
            menuImage[1] = new Image[number];
            isFirst = false;
            try {
                uiImage[0] = ImageIO.read(new File("image\\UI\\Main.png"));
                menuImage[0][0] = ImageIO.read(new File("image\\UI\\ksyx_0.png"));//开始游戏
                menuImage[0][1] = ImageIO.read(new File("image\\UI\\dqyx_0.png"));//读取游戏
                menuImage[0][2] = ImageIO.read(new File("image\\UI\\tcyx_0.png"));//退出游戏
                menuImage[0][3] = ImageIO.read(new File("image\\UI\\gyyx_0.png"));//关于游戏
                menuImage[0][4] = ImageIO.read(new File("image\\UI\\ckbz_0.png"));//查看帮助
                menuImage[0][5] = ImageIO.read(new File("image\\UI\\lxzz_0.png"));//联系作者
                menuImage[1][0] = ImageIO.read(new File("image\\UI\\ksyx_1.png"));//开始游戏
                menuImage[1][1] = ImageIO.read(new File("image\\UI\\dqyx_1.png"));//读取游戏
                menuImage[1][2] = ImageIO.read(new File("image\\UI\\tcyx_1.png"));//退出游戏
                menuImage[1][3] = ImageIO.read(new File("image\\UI\\gyyx_1.png"));//关于游戏
                menuImage[1][4] = ImageIO.read(new File("image\\UI\\ckbz_1.png"));//查看帮助
                menuImage[1][5] = ImageIO.read(new File("image\\UI\\lxzz_1.png"));//联系作者
            } catch (IOException ex) {
                isFirst = true;
                System.out.println(ex.getMessage() + "图片读取失败");
            }
        }
        reset(x, y, width, height);
    }

    public void reset(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.guiImage.clear();
        this.gameGUI.clear();
        button[0] = new Button((int) (this.width * 0.68f), (int) (this.height * 0.04f), (int) (this.width * 0.26f), (int) (this.height * 0.16f));//开始游戏
        button[1] = new Button((int) (this.width * 0.68f), (int) (this.height * 0.24f), (int) (this.width * 0.26f), (int) (this.height * 0.16f));//读取游戏
        button[2] = new Button((int) (this.width * 0.68f), (int) (this.height * 0.44f), (int) (this.width * 0.26f), (int) (this.height * 0.16f));//推出游戏
        button[3] = new Button((int) (this.width * 0.76f), (int) (this.height * 0.73f), (int) (this.width * 0.22f), (int) (this.height * 0.15f));//关于游戏
        button[4] = new Button((int) (this.width * 0.76f), (int) (this.height * 0.91f), (int) (this.width * 0.10f), (int) (this.height * 0.075f));
        button[5] = new Button((int) (this.width * 0.88f), (int) (this.height * 0.91f), (int) (this.width * 0.10f), (int) (this.height * 0.075f));
        guiImage2[0][0] = new GuiImage(0, 0, (int) (this.width * 0.26f), (int) (this.height * 0.16f), menuImage[0][0]);
        guiImage2[0][1] = new GuiImage(0, 0, (int) (this.width * 0.26f), (int) (this.height * 0.16f), menuImage[0][1]);
        guiImage2[0][2] = new GuiImage(0, 0, (int) (this.width * 0.26f), (int) (this.height * 0.16f), menuImage[0][2]);
        guiImage2[0][3] = new GuiImage(0, 0, (int) (this.width * 0.22f), (int) (this.height * 0.15f), menuImage[0][3]);
        guiImage2[0][4] = new GuiImage(0, 0, (int) (this.width * 0.10f), (int) (this.height * 0.075f), menuImage[0][4]);
        guiImage2[0][5] = new GuiImage(0, 0, (int) (this.width * 0.10f), (int) (this.height * 0.075f), menuImage[0][5]);
        guiImage2[1][0] = new GuiImage(0, 0, (int) (this.width * 0.26f), (int) (this.height * 0.16f), menuImage[1][0]);
        guiImage2[1][1] = new GuiImage(0, 0, (int) (this.width * 0.26f), (int) (this.height * 0.16f), menuImage[1][1]);
        guiImage2[1][2] = new GuiImage(0, 0, (int) (this.width * 0.26f), (int) (this.height * 0.16f), menuImage[1][2]);
        guiImage2[1][3] = new GuiImage(0, 0, (int) (this.width * 0.22f), (int) (this.height * 0.15f), menuImage[1][3]);
        guiImage2[1][4] = new GuiImage(0, 0, (int) (this.width * 0.10f), (int) (this.height * 0.075f), menuImage[1][4]);
        guiImage2[1][5] = new GuiImage(0, 0, (int) (this.width * 0.10f), (int) (this.height * 0.075f), menuImage[1][5]);
        for (int i = 0; i < number; i++) {
            button[i].addImage(guiImage2[0][i]);
            button[i].setId(i);
            this.addGUI(button[i]);
        }
        this.addImage(new GuiImage(0, 0, width, height, uiImage[0]));
        this.setText(null);

    }

    public void click(MouseEvent e) {
        int size = gameGUI.size();
        int id = -1;
        for (int i = 0; i < size; i++) {
            Button temp = (Button) gameGUI.get(i);
            if (temp.isRange(e.getX() - x, e.getY() - y)) {
                id = temp.getId();
                break;
            }
        }
        Thread t;
        switch (id) {
            case 0://开始游戏
                System.out.println(Thread.currentThread().getName());
                this.isViewable = false;
                gameManage.setFocusGameGui(null);
                GameGLEventListener.isBegin = true;
                break;
            case 1://读取游戏
                t = new Thread() {
                    public void run() {
                        float w = gameManage.getFrame().getWidth();
                        float h = gameManage.getFrame().getHeight();
                        InputBox t = new InputBox((int) (w / 2 - w / 10), (int) (h / 2 - h / 10), (int) (w / 5), (int) (h / 5), 5, "请输入读取文件名$不能超过5个汉字");
                        t.init();
                        t.getTextField().setIsActive(true);
                        gameManage.addGameGUI(t);
                        gameManage.setFocusGameGui(t);
                        while (!t.isInput()) {
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                System.out.println("休眠失败");
                            }
                        }
                        String str = t.getStr();
                        if (str != null) {
                            GameGLEventListener.isBegin = true;
                            while (GameGLEventListener.isFirst) {
                                try {
                                    Thread.sleep(20);
                                } catch (InterruptedException ex) {
                                    System.out.println("休眠失败");
                                }
                            }
                            gameManage.load(str);
                        }
                        isViewable = false;
                        gameManage.setFocusGameGui(null);
                    }
                };
                t.start();
                break;
            case 2://退出游戏
                System.exit(0);
                break;
            case 3://关于游戏
                break;
            case 4://查看帮助
                break;
            case 5://联系作者
                break;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (this.isViewable == false) {
            return;
        }
        int size = gameGUI.size();
        for (int i = 0; i < size; i++) {
            Button temp = (Button) gameGUI.get(i);
            int id = temp.getId();
            if (temp.isActive) {
                temp.clearGuiImage();
                temp.addImage(guiImage2[1][id]);
            } else {
                temp.clearGuiImage();
                temp.addImage(guiImage2[0][id]);
            }
        }
        super.draw(g);
    }
}
