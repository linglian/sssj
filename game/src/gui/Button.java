package gui;

//********************************************
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

//*类名:Button
//*作者:凌恋      时间:2016-8-16 20:14:39
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Button extends GameGUI {

    public Button() {
        super();
        this.normalColor = Color.gray;
        this.activeColor = Color.white;
        this.textColor = Color.black;
        this.textStyle = TEXT_CENTER;
        this.setText(null);
        this.addActionListener(new ButtonActionAdapter());
    }


    
    public Button(int x, int y, int width, int height) {
        this();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    public void draw(Graphics g) {
        if (this.isViewable == false) {
            return;
        }
        super.draw(g);
    }

    class ButtonActionAdapter extends ActionAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            isActive = true;
        }

        @Override
        public void mouseExited(MouseEvent e) {
            isActive = false;
        }
    }

}
