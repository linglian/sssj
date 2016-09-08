package gui;

//********************************************

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

//*类名:ActionListener
//*作者:凌恋      时间:2016-8-16 20:16:50
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public interface ActionListener {
    public void keyClicked(KeyEvent e);//键盘点击-1,4
    public void keyTyped(KeyEvent e);//键盘键入-2
    public void keyReleased(KeyEvent e);//键盘放出-3
    public void mouseClicked(MouseEvent e);//鼠标点击
    public void mouseDragged(MouseEvent e);//鼠标拖拽
    public void mouseEntered(MouseEvent e);//鼠标进入
    public void mouseExited(MouseEvent e);//鼠标离开
    public void mouseMoved(MouseEvent e);//鼠标移动
    public void mouseWheelMoved(MouseWheelEvent e);//滚轮转动
}
