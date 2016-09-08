package game;

//********************************************
//*类名:GameEvent
//*作者:凌恋      时间:2016-8-13 16:37:31
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameEvent {

    GameManage gameManage;
    public int isFlickerTime = 0;
    public boolean isFlicker = false;
    public boolean isOtherGUI = false;
    public boolean isReplay = false;
    public boolean isLoad = false;
    public boolean isSave= false;

    public GameEvent(GameManage gameManage) {
        this.gameManage = gameManage;
    }
}
