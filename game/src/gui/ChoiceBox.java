package gui;

//********************************************
import game.GameManage;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

//*类名:ChoiceBox
//*作者:凌恋      时间:2016-8-17 19:48:13
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class ChoiceBox extends Dialog {

    public static Image[] uiImage;
    public static Image[][] menuImage;
    public static boolean isFirst = true;
    public boolean choice = false;
    public boolean choiced = false;
    Button[] button;
    GuiImage[][] guiImage2;
    GameManage gameManage;

    public ChoiceBox(int x, int y, int width, int height, String text, GameManage gameManage) {
        super(x, y, width, height);
        this.text = text;
        this.gameManage = gameManage;
        guiImage2 = new GuiImage[2][];
        guiImage2[0] = new GuiImage[3];
        guiImage2[1] = new GuiImage[3];
        button = new Button[3];
    }

    public void init() {
        if (isFirst) {
            uiImage = new Image[1];
            menuImage = new Image[2][];
            menuImage[0] = new Image[3];
            menuImage[1] = new Image[3];
            isFirst = false;
            try {
                uiImage[0] = ImageIO.read(new File("image\\UI\\menu.png"));
                menuImage[0][0] = ImageIO.read(new File("image\\UI\\yes_0.png"));//YES
                menuImage[0][1] = ImageIO.read(new File("image\\UI\\no_0.png"));//NP
                menuImage[0][2] = ImageIO.read(new File("image\\UI\\guanbi_0.png"));//关闭
                menuImage[1][0] = ImageIO.read(new File("image\\UI\\yes_1.png"));//YES
                menuImage[1][1] = ImageIO.read(new File("image\\UI\\no_1.png"));//NP
                menuImage[1][2] = ImageIO.read(new File("image\\UI\\guanbi_1.png"));//关闭
                uiImage[0] = ImageIO.read(new File("image\\UI\\ChoiceBox.png"));//框
            } catch (IOException ex) {
                isFirst = true;
                System.out.println(ex.getMessage() + "图片读取失败");
            }
        }
        reset(x, y, width, height);
    }

    public void reset(int x, int y, int width, int height) {
        this.choice = false;
        this.choiced = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.guiImage.clear();
        this.gameGUI.clear();
        int h = (int) (this.height * 0.25f);
        int w = (int) (this.width * 0.44f);
        int m = 10;
        button[0] = new Button((int) (this.width * 0.04f), (int) (this.height * 0.7f), w, h);
        guiImage2[0][0] = new GuiImage(0, 0, w, h, menuImage[0][0]);
        guiImage2[1][0] = new GuiImage(0, 0, w, h, menuImage[1][0]);
        button[1] = new Button((int) (this.width * 0.52f), (int) (this.height * 0.7f), w, h);
        guiImage2[0][1] = new GuiImage(0, 0, w, h, menuImage[0][1]);
        guiImage2[1][1] = new GuiImage(0, 0, w, h, menuImage[1][1]);
        button[2] = new Button(this.width - this.width / m, 0, this.width / m, this.width / m);
        guiImage2[0][2] = new GuiImage(0, 0, this.width / m, this.height / m, menuImage[0][2]);
        guiImage2[1][2] = new GuiImage(0, 0, this.width / m, this.height / m, menuImage[1][2]);
        for (int i = 0; i < 3; i++) {
            button[i].addImage(guiImage2[0][i]);
            button[i].setId(i);
            this.addGUI(button[i]);
        }
        this.addImage(new GuiImage(0, 0, width, height, uiImage[0]));
        this.hasText = true;
        this.textColor = new Color(0.5f, 0f, 0f);
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
        switch (id) {
            case 0://yes
                this.isViewable = false;
                this.choice = true;
                this.choiced = true;
                break;
            case 1://no
                this.isViewable = false;
                this.choice = false;
                this.choiced = true;
                break;
            case 2://关闭
                this.isViewable = false;
                this.choice = false;
                this.choiced = true;
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
