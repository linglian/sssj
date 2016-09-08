package listener;

//********************************************
import game.GameGLEventListener;
import game.GameManage;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

//*类名:GameMouseWheelListener
//*作者:凌恋      时间:2016-8-15 16:52:53
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameMouseWheelListener implements MouseWheelListener {

    GameManage gameManage;

    public GameMouseWheelListener(GameManage gameManage) {
        this.gameManage = gameManage;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(GameGLEventListener.isFirst){
            return;
        }
        int dir = e.getWheelRotation();
        // down
        if (dir == 1) {
            float high = gameManage.getUser().getHigh();
            float wide = gameManage.getUser().getWide();
            wide += 1f;
            gameManage.getUser().range++;
            if (wide > 25f) {
                gameManage.getUser().range--;
                wide = 25f;
            }
            gameManage.getUser().setHigh(wide * 1.7f);
            gameManage.getUser().setWide(wide);
        }

        // up
        if (dir == -1) {
            float high = gameManage.getUser().getHigh();
            float wide = gameManage.getUser().getWide();
            wide -= 1f;
            gameManage.getUser().range--;
            if (wide < 2.5f) {
            gameManage.getUser().range++;
                wide = 2.5f;
            }
            gameManage.getUser().setHigh(wide * 1.7f);
            gameManage.getUser().setWide(wide);
        }
    }

}
