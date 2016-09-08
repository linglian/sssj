package main;

//********************************************

import game.GameJFrame;
import game.GameManage;
import game.GameObject;
import java.util.LinkedList;
import tool.Point;

//*类名:Main
//*作者:凌恋       时间:2016-8-13 16:24:22
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Main {
    public static void main(String[] args) {
       GameManage gameManage = new GameManage();
      GameJFrame frame = new GameJFrame(gameManage);
       frame.init();
    }

}
