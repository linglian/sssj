package gui;

//********************************************

import java.awt.Image;

//*类名:GuiImage
//*作者:凌恋      时间:2016-8-16 21:04:07
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GuiImage {
    public int x;
    public int y;
    public int width;
    public int height;
    public Image image;

    public GuiImage(int x, int y, int width, int height, Image image) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.image = image;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
    
    
}
