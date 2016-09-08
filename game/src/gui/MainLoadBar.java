package gui;

//********************************************
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

//*类名:MainLoadBar
//*作者:凌恋      时间:2016-8-20 23:52:57
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class MainLoadBar extends Label {

    public float barNum;
    public float maxBarNum;
    public static Image[] image;
    public static BufferedImage[] bar;
    //public static Image barkuang;
    public static Image uiImage;
    public static boolean isFirst = true;
    public boolean isFinish;
    float gf = 0;
    float addGf = 1;
    float xf = 0;
    float addXf = 0.5f;

    public MainLoadBar() {
        super();
        this.isDragged = false;
        this.textColor = Color.red;
    }

    public MainLoadBar(int x, int y, int width, int height, float maxBar, String text) {
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
            image = new Image[6];
            bar = new BufferedImage[8];
            try {
                //barkuang = ImageIO.read(new File("image\\loading\\barkuang.png"));
                uiImage = ImageIO.read(new File("image\\loading\\background.png"));
                for (int i = 0; i < 8; i++) {
                    bar[i] = ImageIO.read(new File("image\\loading\\g_" + i + ".png"));
                }
                for (int i = 0; i < 6; i++) {
                    image[i] = ImageIO.read(new File("image\\loading\\x_" + i + ".png"));
                }
            } catch (IOException ex) {
                isFirst = true;
                Logger.getLogger(GameBar.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        reset(x, y, width, height, maxBarNum, text);
    }

    public void reset(int x, int y, int width, int height, float maxBar, String str) {
        gf = 0;
        addGf = 1;
        xf = 0;
        addXf = 0.5f;
        this.x = x;
        this.y = y;
        this.setViewable(true);
        this.maxBarNum = maxBar;
        barNum = 0;
        this.setText(text);
        this.width = width;
        this.height = height;
        this.guiImage.clear();
        this.addImage(new GuiImage(0, 0, width, height, uiImage));
        //this.addImage(new GuiImage((int) (width * 0.1f), (int) (height * 0.8f), (int) (width * 0.8f), (int) (height * 0.05f), barkuang));
        this.textStyle = GameGUI.TEXT_CENTER;
    }

    public void draw(Graphics g) {
        super.draw(g);
        float f = barNum / maxBarNum + 0.05f;
        if (f >= 1) {
            f = 1;
        }
        gf += addGf;
        if (gf > 7 || gf < 1) {
            addGf = -addGf;
            gf += addGf;
        }
        xf += addXf;
        if (xf > 5 || xf < 0) {
            addXf = -addXf;
            xf += addXf;
        }
        g.drawImage(image[(int) xf], x + (int) (width * 0.1f) + (int) (width * 0.8f * f), y + (int) (height * 0.765f), (int) (width * 0.05f), (int) (height * 0.05f), null);
        g.drawImage(bar[(int) gf], x + (int) (width * 0.1f) + (int) (width * 0.8f * f), y + (int) (height * 0.8f), (int) (width * 0.05f), (int) (height * 0.025f), null);
        if (this.barNum >= maxBarNum) {
            isFinish = true;
        }
        g.setColor(Color.white);
        g.drawString(String.valueOf((int)(f*100f))+'%', x+(int) (width * 0.488f),  y + (int) (height * 0.84f));

    }
}
