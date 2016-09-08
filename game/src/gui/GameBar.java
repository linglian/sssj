package gui;

//********************************************
import static gui.MainMenu.menuImage;
import static gui.MainMenu.uiImage;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

//*类名:GameBar
//*作者:凌恋      时间:2016-8-17 23:29:22
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameBar extends Label {

    public float barNum;
    public float maxBarNum;
    public static Image[] uiImage;
    public static Image[] barkuang;
    public static BufferedImage[] bar;
    public static boolean isFirst = true;
    public boolean isFinish;

    public GameBar() {
        super();
    }

    public GameBar(int x, int y, int width, int height, float maxBar, String text) {
        this();
        this.barNum = 0;
        this.maxBarNum = maxBar;
        isFinish = false;
        this.setText(text);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isViewable = true;
        this.addActionListener(new LabelActionAdapter());
    }

    public void init() {
        if (isFirst) {
            isFirst = false;
            uiImage = new Image[1];
            barkuang = new Image[1];
            bar = new BufferedImage[1];
            try {
                uiImage[0] = ImageIO.read(new File("image\\UI\\barImage.png"));
                barkuang[0] = ImageIO.read(new File("image\\UI\\barkuang.png"));
                bar[0] = ImageIO.read(new File("image\\UI\\bar_0.png"));
            } catch (IOException ex) {
                isFirst = true;
                Logger.getLogger(GameBar.class.getName()).log(Level.SEVERE, null, ex);
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
        this.addImage(new GuiImage(0, 0, width, height, uiImage[0]));
        this.addImage(new GuiImage((int) (width * 0.1f), (int) (height * 0.8f), (int) (width * 0.8f), (int) (height * 0.1f), barkuang[0]));
        this.textStyle = GameGUI.TEXT_CENTER;
    }

    public void draw(Graphics g) {
        super.draw(g);
        float f = barNum / maxBarNum + 0.05f;
        if (f >= 1) {
            f = 1;
        }
        Image i = bar[0].getSubimage(0, 0, (int) (bar[0].getWidth() * f), bar[0].getHeight());
        g.drawImage(i, x + (int) (width * 0.1f), y + (int) (height * 0.8f), (int) (width * 0.8f * f), (int) (height * 0.1f), null);
        if (this.barNum >= maxBarNum) {
            isFinish = true;
        }
    }
}
