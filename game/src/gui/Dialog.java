package gui;

//********************************************
import static gui.GameGUI.TEXT_CENTER;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

//*类名:Dialog
//*作者:凌恋      时间:2016-8-16 22:16:55
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Dialog extends GameGUI {

    LinkedList<GameGUI> gameGUI;
    boolean isDragged = true;

    public Dialog() {
        super();
        gameGUI = new LinkedList();
        this.normalColor = Color.gray;
        this.activeColor = Color.white;
        this.textColor = Color.black;
        this.addActionListener(new DialogActionAdapter());
    }

    public Dialog(int x, int y, int width, int height) {
        this();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isViewable = true;
    }

    @Override
    public void click(MouseEvent e) {
        int size = gameGUI.size();
        for (int i = 0; i < size; i++) {
            GameGUI temp = gameGUI.get(i);
            if (temp.isRange(e.getX() - x, e.getY() - y)) {
                temp.click(e);
                break;
            }
        }
    }

    public void typed(KeyEvent e) {
        int size = gameGUI.size();
        for (int i = 0; i < size; i++) {
            GameGUI temp = gameGUI.get(i);
            if (temp.isActive) {
                temp.typed(e);
            }
        }
    }

    @Override
    public void exit(MouseEvent e) {
        int size = gameGUI.size();
        for (int i = 0; i < size; i++) {
            GameGUI temp = gameGUI.get(i);
            temp.exit(e);
        }
    }

    @Override
    public void move(MouseEvent e) {
        int size = gameGUI.size();
        for (int i = 0; i < size; i++) {
            GameGUI temp = gameGUI.get(i);
            if (temp.isRange(e.getX() - x, e.getY() - y)) {
                temp.isActive = true;
                temp.move(e);
            } else {
                temp.exit(e);
            }
        }
    }

    public void addGUI(GameGUI gui) {
        gameGUI.add(gui);
    }

    public void removeGUI(GameGUI gui) {
        gameGUI.add(gui);
    }

    @Override
    public void draw(Graphics g) {
        if (this.isViewable == false) {
            return;
        }
        super.draw(g);
        Image image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_ARGB);
        Graphics tg = image.getGraphics();
        int size = gameGUI.size();
        for (int i = 0; i < size; i++) {
            GameGUI temp = gameGUI.get(i);
            temp.draw(tg);
        }
        g.drawImage(image, x, y, width, height, null);
    }

    public boolean isDragged() {
        return isDragged;
    }

    public void setIsDragged(boolean isDragged) {
        this.isDragged = isDragged;
    }

    class DialogActionAdapter extends ActionAdapter {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (isDragged) {
                x = e.getX() - width / 2;
                y = e.getY() - 20;
            }
        }

    }
}
