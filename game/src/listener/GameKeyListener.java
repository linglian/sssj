package listener;

import npc.Flame;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.glu.GLU;
import game.GameGLEventListener;
import game.GameJFrame;
import game.GameManage;
import game.GameThread;
import gui.GameGUI;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import npc.Npc;
import npc.NpcObject;
import toolkit.GameString;

public class GameKeyListener implements KeyListener {

    GameManage gameManage;

    public GameKeyListener(GameManage gameManage) {
        this.gameManage = gameManage;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (gameManage.getFocusGameGui() != null) {
            GameGUI temp = gameManage.getFocusGameGui();
            temp.typed(e);
        } else {
            String temp = e.toString();
            temp = temp.substring(temp.indexOf("keyChar="));
            temp = temp.substring(temp.indexOf('=') + 1, temp.indexOf(','));
            if (temp.equals("Delete")) {
                gameManage.getUser().setIsTurn(true);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameManage.getFocusGameGui() != null || GameGLEventListener.isFirst) {
            return;
        }
        int num;
        NpcObject[] tempNpc;
        float w, h;
        GameJFrame frame = gameManage.getFrame();
        double s = Math.sin(Math.toRadians(gameManage.getUser().getRotate()));
        double c = Math.cos(Math.toRadians(gameManage.getUser().getRotate()));
        if (c < -1.5 || c > 1.5) {
            c = 0;
        }
        if (s < -1.5 || s > 1.5) {
            s = 0;
        }
        float n = (float) c * 2f;
        float a = (float) s * 2f;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
                if (gameManage.getMainMenu().isViewable()) {
                    gameManage.getMainMenu().setViewable(false);
                    gameManage.setFocusGameGui(null);
                } else {
                    w = gameManage.getFrame().getWidth();
                    h = gameManage.getFrame().getHeight();
                    gameManage.getMainMenu().reset((int) (w * 0.35f), (int) (h * 0.2f), (int) (w * 0.3f), (int) (h * 0.6f));
                    gameManage.getMainMenu().setViewable(true);
                }
                break;
            case KeyEvent.VK_C:
                if(gameManage.getUser().isCtrl()){
                    gameManage.getUser().setIsCtrl(false);
                }else{
                    gameManage.getUser().setIsCtrl(true);
                }
                break;
            case KeyEvent.VK_F1:
                frame.setBounds(0, 0, 500, 500);
                break;
            case KeyEvent.VK_F2:
                frame.setBounds(0, 0, 1200, 800);
                break;
            case KeyEvent.VK_F3:
                Toolkit kit = Toolkit.getDefaultToolkit();
                frame.setBounds(0, 0, kit.getScreenSize().width, kit.getScreenSize().height);
                break;
            case KeyEvent.VK_F4:
                gameManage.getPanel().setNotice("欢迎来到沙石世界，在这里你将带领你的人民去探索");
                break;
            case KeyEvent.VK_F5:
                int x = gameManage.getFrame().getWidth() / 2 - (int) (gameManage.getFrame().getHeight() * 0.2f);
                int y = gameManage.getFrame().getHeight() / 2 - (int) (gameManage.getFrame().getHeight() * 0.1f);
                //x上剪去大小/2.5
                //y上剪去大小/5
                gameManage.getPanel().getStrLinkedList().add(new GameString("欢迎来到沙石世界II", x, y, 0.05f, 10, 20, Color.GRAY));
                break;
            case KeyEvent.VK_F10:
                gameManage.getMap().lightAll(GameThread.gameTime+10);
                break;
            case KeyEvent.VK_F11:
                gameManage.getUser().setIsView(false);
                break;
            case KeyEvent.VK_F12:
                gameManage.getUser().setIsView(true);
                break;
            case KeyEvent.VK_LEFT:
                gameManage.getUser().setX(gameManage.getUser().getX() - 0.1f);
                break;
            case KeyEvent.VK_RIGHT:
                gameManage.getUser().setX(gameManage.getUser().getX() + 0.1f);
                break;
            case KeyEvent.VK_UP:
                gameManage.getUser().setY(gameManage.getUser().getY() - 0.1f);
                break;
            case KeyEvent.VK_DOWN:
                gameManage.getUser().setY(gameManage.getUser().getY() + 0.1f);
                break;
            case KeyEvent.VK_W:
                num = gameManage.getUser().getChooseNpcNum();
                tempNpc = gameManage.getUser().getChooseNpc();
                for (int i = 0; i < num; i++) {
                    if (tempNpc[i].isEnable()) {
                        tempNpc[i].setMove(tempNpc[i].getX() - a, tempNpc[i].getY() - n);
                    }
                }
                break;
            case KeyEvent.VK_S:
                num = gameManage.getUser().getChooseNpcNum();
                tempNpc = gameManage.getUser().getChooseNpc();
                for (int i = 0; i < num; i++) {
                    if (tempNpc[i].isEnable()) {
                        tempNpc[i].setMove(tempNpc[i].getX() + a, tempNpc[i].getY() + n);
                    }
                }
                break;
            case KeyEvent.VK_A:
                num = gameManage.getUser().getChooseNpcNum();
                tempNpc = gameManage.getUser().getChooseNpc();
                for (int i = 0; i < num; i++) {
                    if (tempNpc[i].isEnable()) {
                        tempNpc[i].setMove(tempNpc[i].getX() - n, tempNpc[i].getY() + a);
                    }
                }
                break;
            case KeyEvent.VK_D:
                num = gameManage.getUser().getChooseNpcNum();
                tempNpc = gameManage.getUser().getChooseNpc();
                for (int i = 0; i < num; i++) {
                    if (tempNpc[i].isEnable()) {
                        tempNpc[i].setMove(tempNpc[i].getX() + n, tempNpc[i].getY() - a);
                    }
                }
                break;
            case KeyEvent.VK_V:
                gameManage.getUser().setIsBuild(true,0);
                break;
            case KeyEvent.VK_B:
                gameManage.getUser().setIsBuild(false,-1);
                break;
            case KeyEvent.VK_SPACE:
                num = gameManage.getUser().getChooseNpcNum();
                tempNpc = gameManage.getUser().getChooseNpc();
                for (int i = 0; i < num; i++) {
                    if (tempNpc[i].isEnable()) {
                        tempNpc[i].setAddHigh(5f);
                    }
                }
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (gameManage.getFocusGameGui() != null) {
            return;
        }
        if (!GameGLEventListener.isFirst) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DELETE:
                    gameManage.getUser().setIsTurn(false);
                    break;
            }
        }
    }
}
