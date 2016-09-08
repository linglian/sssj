package gui;

//********************************************
import game.GameManage;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

//*类名:MainMenu
//*作者:凌恋      时间:2016-8-17 14:27:24
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class MainMenu extends Dialog {

    public static Image[] uiImage;
    public static Image[][] menuImage;
    public static boolean isFirst = true;
    Button[] button;
    GuiImage[][] guiImage2;
    GameManage gameManage;

    public MainMenu(int x, int y, int width, int height, GameManage gameManage) {
        super(x, y, width, height);
        this.gameManage = gameManage;
        guiImage2 = new GuiImage[2][];
        guiImage2[0] = new GuiImage[8];
        guiImage2[1] = new GuiImage[8];
        button = new Button[8];
    }

    public void init() {
        if (isFirst) {
            uiImage = new Image[5];
            menuImage = new Image[2][];
            menuImage[0] = new Image[8];
            menuImage[1] = new Image[8];
            isFirst = false;
            try {
                uiImage[0] = ImageIO.read(new File("image\\UI\\menu.png"));
                menuImage[0][0] = ImageIO.read(new File("image\\UI\\jixuyouxi_0.png"));//继续游戏
                menuImage[0][1] = ImageIO.read(new File("image\\UI\\chongxinkaishi_0.png"));//重新开始
                menuImage[0][2] = ImageIO.read(new File("image\\UI\\baocunyouxi_0.png"));//保存游戏
                menuImage[0][3] = ImageIO.read(new File("image\\UI\\duquyouxi_0.png"));//读取游戏
                menuImage[0][4] = ImageIO.read(new File("image\\UI\\chakanbangzhu_0.png"));//查看帮助
                menuImage[0][5] = ImageIO.read(new File("image\\UI\\guanyuyouxi_0.png"));//关于游戏
                menuImage[0][6] = ImageIO.read(new File("image\\UI\\tuichuyouxi_0.png"));//退出游戏
                menuImage[0][7] = ImageIO.read(new File("image\\UI\\kaishiyouxi_0.png"));//开始游戏
                menuImage[1][0] = ImageIO.read(new File("image\\UI\\jixuyouxi_1.png"));//继续游戏
                menuImage[1][1] = ImageIO.read(new File("image\\UI\\chongxinkaishi_1.png"));//重新开始
                menuImage[1][2] = ImageIO.read(new File("image\\UI\\baocunyouxi_1.png"));//保存游戏
                menuImage[1][3] = ImageIO.read(new File("image\\UI\\duquyouxi_1.png"));//读取游戏
                menuImage[1][4] = ImageIO.read(new File("image\\UI\\chakanbangzhu_1.png"));//查看帮助
                menuImage[1][5] = ImageIO.read(new File("image\\UI\\guanyuyouxi_1.png"));//关于游戏
                menuImage[1][6] = ImageIO.read(new File("image\\UI\\tuichuyouxi_1.png"));//退出游戏
                menuImage[1][7] = ImageIO.read(new File("image\\UI\\kaishiyouxi_1.png"));//开始游戏
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
        for (int i = 0; i < 7; i++) {
            button[i] = new Button(this.width / 2 - width / 4, height / 9 + this.height / 9 * i, this.width / 2, this.height / 9);
        }
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 7; i++) {
                guiImage2[j][i] = new GuiImage(0, 0, this.width / 2, this.height / 9, menuImage[j][i]);
            }
        }
        for (int i = 0; i < 7; i++) {
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
         Thread t;
        for (int i = 0; i < size; i++) {
            Button temp = (Button) gameGUI.get(i);
            if (temp.isRange(e.getX() - x, e.getY() - y)) {
                id = temp.getId();
                break;
            }
        }
        switch (id) {
            case 0://继续游戏
                this.isViewable = false;
                gameManage.setFocusGameGui(null);
                break;
            case 1://重新开始
                t = new Thread() {
                    public void run() {
                        float w = gameManage.getFrame().getWidth();
                        float h = gameManage.getFrame().getHeight();
                        ChoiceBox box = new ChoiceBox((int) (w / 2 - w / 10), (int) (h / 2 - h / 10), (int) (w / 5), (int) (h / 5), "注意$此操作不可撤销$是否确认重新开始", gameManage);
                        box.init();
                        box.setViewable(false);
                        gameManage.addGameGUI(box);
                        box.setViewable(true);
                        setViewable(false);
                        while (!box.choiced) {
                            try {
                                Thread.sleep(20);
                            } catch (InterruptedException ex) {
                                System.out.println("休眠失败");
                            }
                        }
                        if (box.choice) {
                            if (!gameManage.getEvent().isReplay) {
                                gameManage.replay();
                            }
                        }
                        gameManage.removeGameGUI(box);
                    }
                };
                t.start();
                break;
            case 2://保存游戏
                this.isViewable = false;
                t = new Thread() {
                    public void run() {
                        float w = gameManage.getFrame().getWidth();
                        float h = gameManage.getFrame().getHeight();
                        InputBox t = new InputBox((int) (w / 2 - w / 10), (int) (h / 2 - h / 10), (int) (w / 5), (int) (h / 5), 5, "请输入保存文件名$不能超过5个汉字");
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
                            gameManage.save(str);
                        }
                        gameManage.setFocusGameGui(null);
                    }
                };
                t.start();
                break;
            case 3://读取游戏
                this.isViewable = false;
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
                            gameManage.load(str);
                        }
                        gameManage.setFocusGameGui(null);
                    }
                };
                t.start();
                break;
            case 4://查看帮助
                break;
            case 5://关于游戏
                gameManage.getNpc().saveNpc(new File("npc.txt"));
                break;
            case 6://退出游戏
                System.exit(0);
                break;
            case 7://开始游戏
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
