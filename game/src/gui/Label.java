package gui;

//********************************************
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

//*类名:Label
//*作者:凌恋      时间:2016-8-16 21:22:26
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Label extends GameGUI {

    boolean isDragged = true;

    @Override
    public void draw(Graphics g) {
        if (this.isViewable == false) {
            return;
        }
        super.draw(g);
    }

    public Label() {
        super();
        this.setText(null);
        this.normalColor = Color.gray;
        this.activeColor = Color.white;
        this.textColor = Color.black;
    }

    public Label(int x, int y, int width, int height) {
        this();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isViewable = true;
        this.addActionListener(new LabelActionAdapter());
    }

    public Label(int x, int y, int width, int height, String text) {
        this();
        this.setText(text);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isViewable = true;
        this.addActionListener(new LabelActionAdapter());
    }

    class LabelActionAdapter extends ActionAdapter {

        @Override
        public void mouseDragged(MouseEvent e) {
            if (isDragged) {
                x = e.getX() - width / 2;
                y = e.getY() - 20;
            }
        }

    }
}
