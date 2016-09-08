package gui;

//********************************************
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

//*类名:GameGUI
//*作者:凌恋      时间:2016-8-16 20:26:05
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameGUI {

    int id = -1;
    public static Font font;
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean isActive = false;
    public boolean enable = true;
    public boolean isViewable = true;
    public boolean hasImage = false;
    public boolean hasText = false;
    public String text;//按钮上的字
    public int textStyle;

    public Color normalColor;//平常颜色
    public Color activeColor;//激活颜色
    public Color textColor;//字体颜色

    public static final int TEXT_LEFT = 0;
    public static final int TEXT_CENTER = 1;
    public static final int TEXT_RIGHT = 2;
    LinkedList< GuiImage> guiImage;
    LinkedList<ActionListener> actionListener;

    public GameGUI() {
        guiImage = new LinkedList();
        actionListener = new LinkedList();
        this.normalColor = Color.white;
        this.activeColor = Color.white;
        this.textColor = Color.black;
        this.textStyle = TEXT_CENTER;
        this.hasText = true;
        this.text = "未定义";
        isActive = false;
        hasImage = false;
        isViewable = true;
        this.enable = true;
    }

    public GameGUI(int x, int y, int width, int height) {
        this();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public static void setFont(Font f) {
        font = f;
    }

    public void clearGuiImage() {
        guiImage.clear();
    }

    public GuiImage getGuiImage(int i) {
        return guiImage.get(i);
    }

    public LinkedList<GuiImage> getGuiImage() {
        return guiImage;
    }

    public void clearActionListener() {
        actionListener.clear();
    }

    public ActionListener getActionListener(int i) {
        return actionListener.get(i);
    }

    public LinkedList<ActionListener> getActionListener() {
        return actionListener;
    }

    public void draw(Graphics g) {
        if (this.isViewable == false) {
            return;
        }
        Font lastFont = g.getFont();
        if (font != null) {
            g.setFont(font);
        }
        Color lastColor = g.getColor();
        if (enable) {
            if (isActive) {
                g.setColor(activeColor);
            } else {
                g.setColor(normalColor);
            }
        } else {
            g.setColor(Color.gray);
        }
        if (this.hasImage) {
            int size = this.guiImage.size();
            for (int i = 0; i < size; i++) {
                GuiImage temp = guiImage.get(i);
                g.drawImage(temp.image, x + temp.x, y + temp.y, temp.width, temp.height, null);
            }
        } else {
            g.fillRect(x, y, width, height);
        }
        if (hasText) {
            g.setColor(textColor);
            FontMetrics tfm = g.getFontMetrics(g.getFont());
            int w = tfm.charWidth('啊');
            if (text.indexOf('$') != -1) {
                int maxRow = 0;
                LinkedList<String> temp = new LinkedList();
                String t = text;
                int index = -1;
                while ((index = t.indexOf('$')) != -1) {
                    temp.add(t.substring(0, index));
                    t = t.substring(index + 1);
                }
                temp.add(t);
                maxRow = temp.size();
                for (int i = 0; i < maxRow; i++) {
                    int lenth = temp.get(i).length();
                    if (this.textStyle == GameGUI.TEXT_CENTER) {
                        g.drawString(temp.get(i), x + width / 2 - lenth * w / 2, y + height / 2 + i * w - (int) ((float) maxRow / 2f) * w);
                    } else if (this.textStyle == GameGUI.TEXT_LEFT) {
                        g.drawString(temp.get(i), x, y + height / 2 + i * w - (int) ((float) maxRow / 2f) * w);
                    } else {
                        g.drawString(temp.get(i), x + width - lenth * w, y + height / 2 + i * w - (int) ((float) maxRow / 2f) * w);
                    }
                }
            } else {
                int lenth = text.length();
                int wd = (int) ((float) width / (float) w);
                int row = 0;
                int maxRow = lenth / wd + 1;
                String temp = text;
                while (lenth > wd) {
                    String t = temp.substring(0, wd);
                    if (this.textStyle == GameGUI.TEXT_CENTER) {
                        g.drawString(t, x + width / 2 - wd * w / 2, y + height / 2 + row * w - maxRow / 2 * w);
                    } else if (this.textStyle == GameGUI.TEXT_LEFT) {
                        g.drawString(t, x, y + height / 2 + row * w - maxRow / 2 * w);
                    } else {
                        g.drawString(t, x + width - wd * w, y + height / 2 + row * w - maxRow / 2 * w);
                    }
                    lenth -= wd;
                    row++;
                    temp = temp.substring(wd);
                }
                if (this.textStyle == GameGUI.TEXT_CENTER) {
                    g.drawString(temp, x + width / 2 - lenth * w / 2, y + height / 2 + row * w - maxRow / 2 * w);
                } else if (this.textStyle == GameGUI.TEXT_LEFT) {
                    g.drawString(temp, x, y + height / 2 + row * w - maxRow / 2 * w);
                } else {
                    g.drawString(temp, x + width - lenth * w, y + height / 2 + row * w - maxRow / 2 * w);
                }
            }
        }
        g.setFont(lastFont);
        g.setColor(lastColor);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (text == null) {
            this.text = null;
            this.hasText = false;
        } else {
            this.text = text;
            this.hasText = true;
        }
    }

    public boolean isRange(int x, int y) {
        if (x > this.x && y > this.y && x < this.x + this.width && y < this.y + this.height) {
            return true;
        } else {
            return false;
        }
    }

    public void click(MouseEvent e) {
        int size = actionListener.size();
        for (int i = 0; i < size; i++) {
            actionListener.get(i).mouseClicked(e);
        }
    }

    public void move(MouseEvent e) {
        int size = actionListener.size();
        for (int i = 0; i < size; i++) {
            actionListener.get(i).mouseMoved(e);
        }
    }

    public void exit(MouseEvent e) {
        int size = actionListener.size();
        for (int i = 0; i < size; i++) {
            this.isActive = false;
            actionListener.get(i).mouseExited(e);
        }
    }

    public void click(KeyEvent e) {
        int size = actionListener.size();
        for (int i = 0; i < size; i++) {
            actionListener.get(i).keyClicked(e);
        }
    }

    public void typed(KeyEvent e) {
        int size = actionListener.size();
        for (int i = 0; i < size; i++) {
            actionListener.get(i).keyTyped(e);
        }
    }

    public void dragged(MouseEvent e) {
        int size = actionListener.size();
        for (int i = 0; i < size; i++) {
            actionListener.get(i).mouseDragged(e);
        }
    }

    public void addImage(GuiImage image) {
        guiImage.add(image);
        this.hasImage = true;
    }

    public void removeImage(GuiImage image) {
        guiImage.remove(image);
        if (guiImage.size() == 0) {
            this.hasImage = false;
        }
    }

    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.enable = true;
        this.isViewable = true;
    }

    public void addActionListener(ActionListener actionListener) {
        this.actionListener.add(actionListener);
    }

    public void removeActionListener(ActionListener actionListener) {
        this.actionListener.remove(actionListener);
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public boolean isHasText() {
        return hasText;
    }

    public void setHasText(boolean hasText) {
        this.hasText = hasText;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getTextStyle() {
        return textStyle;
    }

    public void setTextStyle(int style) {
        this.textStyle = style;
    }

    public Color getNormalColor() {
        return normalColor;
    }

    public void setNormalColor(Color normalColor) {
        this.normalColor = normalColor;
    }

    public Color getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(Color activeColor) {
        this.activeColor = activeColor;
    }

    public Color getTextColor() {
        return textColor;
    }

    public void setTextColor(Color textColor) {
        this.textColor = textColor;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public boolean isViewable() {
        return isViewable;
    }

    public void setViewable(boolean isViewable) {
        this.isViewable = isViewable;
    }

}
