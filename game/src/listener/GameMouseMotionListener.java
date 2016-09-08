package listener;

import game.GameGLEventListener;
import game.GameManage;
import game.GameMap;
import game.GameUser;
import gui.GameGUI;
import java.awt.event.MouseEvent;
import static java.awt.event.MouseEvent.BUTTON1;
import java.awt.event.MouseMotionListener;

//********************************************
//*类名:GameMouseMotionListener
//*作者:李俊萍       时间:2016-7-30 7:01:22
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameMouseMotionListener implements MouseMotionListener {

    GameManage gameManage;
    int width;
    int height;

    public GameMouseMotionListener(GameManage gameManage) {
        this.gameManage = gameManage;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(gameManage.getUser().isCantHandle()){
            return;
        }
        int size = gameManage.getGameGUI().size();
        int mouseX = e.getX();
        int mouseY = e.getY();
        GameGUI tempGui;
        if (gameManage.getEvent().isOtherGUI) {
            int s = gameManage.getChooseGUI().size();
            for (int i = s-1; i >= 0; i--) {
                tempGui = gameManage.getChooseGUI().get(i);
                tempGui.dragged(e);
                break;
            }
        } else {
            for (int i = size-1; i >= 0; i--) {
                tempGui = gameManage.getGameGUI().get(i);
                if (tempGui.enable && tempGui.isViewable && tempGui.isRange(mouseX, mouseY)) {
                    tempGui.dragged(e);
                    gameManage.addChooseGUI(tempGui);
                    gameManage.getEvent().isOtherGUI = true;
                }
            }
        }
        if (gameManage.getEvent().isOtherGUI||GameGLEventListener.isFirst) {
            return;
        }
        boolean isButton1 = false;
        String temp = e.toString();
        temp = temp.substring(temp.indexOf("extModifiers="));
        temp = temp.substring(temp.indexOf('=') + 1, temp.indexOf(','));
        if (temp.equals("Button1")) {
            isButton1 = true;
        }
        //拖动，出来绿色框框
        if (isButton1 && (gameManage.getUser().isDragged() || e.getY() <= (int) (gameManage.getFrame().getHeight() * 0.675f))) {
            gameManage.getUser().setIsDragged(true);
            gameManage.getUser().setReleasedMouseX(e.getX());
            gameManage.getUser().setReleasedMouseY(e.getY());
            //在小地图区域
        } else if (isButton1 && e.getY() > (int) (gameManage.getFrame().getHeight() * 0.675f) && e.getX() < (int) (gameManage.getFrame().getWidth() * 0.335f)) {
            float mapX = e.getX() - gameManage.getFrame().getWidth() * 0.046f;
            float mapY = e.getY() - gameManage.getFrame().getHeight() * 0.719f;
            float width = gameManage.getFrame().getWidth() * 0.23f;
            float height = gameManage.getFrame().getHeight() * 0.238f;
            float xUnit = width / GameMap.width;
            float yUnit = height / GameMap.height;
            gameManage.getUser().setX(mapX / xUnit);
            gameManage.getUser().setY(mapY / yUnit);
        } else {
            gameManage.getUser().setIsDragged(false);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(gameManage.getUser().isCantHandle()){
            return;
        }
        int x = e.getX();
        int y = e.getY();
        int flag = 0;
        int size = gameManage.getGameGUI().size();
        int mouseX = e.getX();
        int mouseY = e.getY();
        boolean isFound = false;
        for (int i = size-1; i >=0; i--) {
            GameGUI temp = gameManage.getGameGUI().get(i);
            if (temp.enable && temp.isViewable && temp.isRange(mouseX, mouseY)) {
                temp.move(e);
                isFound = true;
                break;
            }
        }
        if (isFound||GameGLEventListener.isFirst) {
            return;
        }
        if (e.getY() <= (int) (gameManage.getFrame().getHeight() * 0.675f)) {
            if (!gameManage.getUser().isDragged()) {
                gameManage.getUser().setMouseX(x);
                gameManage.getUser().setMouseY(y);
            }
        }
        if (x <= 3) {//往左移
            flag = 1;
            gameManage.getUser().setMoveState(GameUser.MOVE_LEFT);
        } else if (x >= gameManage.getFrame().getWidth() - 2) {//往右移
            flag = 1;
            gameManage.getUser().setMoveState(GameUser.MOVE_RIGHT);
        }
        if (y <= 3) {//往上移
            flag = 1;
            if (gameManage.getUser().getMoveState() == GameUser.MOVE_LEFT) {
                gameManage.getUser().setMoveState(GameUser.MOVE_UP_LEFT);
            } else if (gameManage.getUser().getMoveState() == GameUser.MOVE_RIGHT) {
                gameManage.getUser().setMoveState(GameUser.MOVE_UP_RIGHT);
            } else {
                gameManage.getUser().setMoveState(GameUser.MOVE_UP);
            }
        } else if (y >= gameManage.getFrame().getHeight() - 2) {//往下移
            flag = 1;
            if (gameManage.getUser().getMoveState() == GameUser.MOVE_LEFT) {
                gameManage.getUser().setMoveState(GameUser.MOVE_DOWN_LEFT);
            } else if (gameManage.getUser().getMoveState() == GameUser.MOVE_RIGHT) {
                gameManage.getUser().setMoveState(GameUser.MOVE_DOWN_RIGHT);
            } else {
                gameManage.getUser().setMoveState(GameUser.MOVE_DOWN);
            }
        }
        if (flag == 0) {
            gameManage.getUser().setMoveState(GameUser.MOVE_STOP);
        }
    }

}
