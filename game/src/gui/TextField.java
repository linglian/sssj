package gui;

//********************************************
import static gui.GameGUI.font;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

//*类名:TextField
//*作者:凌恋      时间:2016-8-18 0:27:09
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class TextField extends GameGUI {

    public static Image[] uiImage;
    public static boolean isFirst = true;
    public String str;
    public boolean isPut = false;
    float maxSize;
    float size;
    Button button;
    int time;

    public TextField() {
        super();
    }

    public TextField(int x, int y, int width, int height, int size) {
        this();
        this.setText(text);
        this.maxSize = size;
        this.x = x;
        this.y = y;
        this.width = width;
        this.textStyle = GameGUI.TEXT_LEFT;
        this.height = height;
        this.isViewable = true;
        this.addActionListener(new TextFieldActionAdapter());
        this.textColor = Color.GRAY;
    }

    public void init() {
        if (isFirst) {
            isFirst = false;
            uiImage = new Image[1];
            try {
                uiImage[0] = ImageIO.read(new File("image\\UI\\shuru.png"));
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
        this.textStyle = GameGUI.TEXT_LEFT;
        this.text = "";
        size = 0;
    }

    @Override
    public void click(MouseEvent e) {
        int x = e.getX();
        if (x > this.x + this.width * 0.9f) {
            str = text;
            this.text = new String("");
            isPut = true;
            size = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        Font lastFont = g.getFont();
        if (font != null) {
            g.setFont(font);
        }
        Color lastColor = g.getColor();
        FontMetrics t = g.getFontMetrics(g.getFont());
        int w = t.charWidth('啊');
        g.drawImage(uiImage[0], x, y, width, height, null);
        g.setColor(textColor);
        g.drawString(text, x + (int) (width * 0.02f), y + height / 2 - w / 2 + (int) (height * 0.3f));
        if (isActive) {
            time++;
            g.setColor(Color.white);
            if (time < 5) {
                g.drawLine(x + (int) (size * w) + (int) (width * 0.02f), y + (int) (height * 0.15f), x + (int) (size * w) + (int) (width * 0.02f), y + (int) (height * 0.85f));
            } else {

            }
            if (time >= 10) {
                time = 0;
            }
        }
        g.setFont(lastFont);
        g.setColor(lastColor);
    }

    class TextFieldActionAdapter extends ActionAdapter {

        @Override
        public void keyTyped(KeyEvent e) {
            String temp = e.toString();
            temp = temp.substring(temp.indexOf("keyChar="));
            temp = temp.substring(temp.indexOf('=') + 1, temp.indexOf(','));
            if (temp.equals("Delete") || temp.equals("Backspace")) {
                if (text.length() <= 1) {
                    text = new String("");
                    size = 0;
                } else {
                    char t = text.substring(text.length() - 1).toCharArray()[0];
                    if (t >= -125 && t <= 124) {
                        size -= 0.525f;
                    } else {
                        size -= 1f;
                    }
                    text = text.substring(0, text.length() - 1);
                }
            } else if (temp.equals("Enter")) {
                str = text;
                text = new String("");
                size = 0;
                isPut = true;
            } else if (temp.indexOf('\'') != -1) {
                if (maxSize <= size) {

                } else {
                    temp = temp.substring(1, temp.length() - 1);
                    char[] c = temp.toCharArray();
                    char t = c[0];
                    if (t >= -125 && t <= 124) {
                        size += 0.525f;
                    } else {
                        size += 1f;
                    }
                    text = text.concat(temp);
                }
            }
        }

    }
}
