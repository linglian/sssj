package game;

//********************************************
import java.util.logging.Level;
import java.util.logging.Logger;
import npc.NpcObject;

//*类名:GameThread
//*作者:凌恋      时间:2016-8-14 11:47:18
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameThread implements Runnable {

    public static NpcObject[][] mainNpc;
    GameManage gameManage;
    Thread mainThread;
    int time = 0;
    public static int gameTime = 0;

    public GameThread(GameManage gameManage) {
        this.gameManage = gameManage;
    }

    public void start() {
        mainThread = new Thread(this);
        mainThread.start();
    }

    public void stop() {
        Thread temp = mainThread;
        mainThread = null;
        try {
            temp.join();
        } catch (InterruptedException ex) {
            System.out.println("线程等待结束失败");
        }
    }

    public void viewMove() {
        GameUser user = gameManage.getUser();
        if (user.isFlowNpc() && user.getChooseNpcNum() >= 1) {
            user.setX(user.getChooseNpc()[0].getX());
            user.setY(user.getChooseNpc()[0].getY());
        } else {
            int state = user.getMoveState();
            float speed = user.getMoveSpeed();
            double c = Math.cos(Math.toRadians(gameManage.getUser().getRotate()));
            if (c < -1.5 || c > 1.5) {
                c = 0;
            }
            double s = Math.sin(Math.toRadians(gameManage.getUser().getRotate()));
            if (s < -1.5 || s > 1.5) {
                s = 0;
            }
            float n = (float) c * speed;
            float a = (float) s * speed;
            switch (state) {
                case GameUser.MOVE_UP:
                    user.setY(user.getY() - n);
                    user.setX(user.getX() - a);
                    break;
                case GameUser.MOVE_DOWN:
                    user.setY(user.getY() + n);
                    user.setX(user.getX() + a);
                    break;
                case GameUser.MOVE_LEFT:
                    user.setX(user.getX() - n);
                    user.setY(user.getY() + a);
                    break;
                case GameUser.MOVE_RIGHT:
                    user.setX(user.getX() + n);
                    user.setY(user.getY() - a);
                    break;
                case GameUser.MOVE_UP_RIGHT:
                    user.setY(user.getY() - n);
                    user.setX(user.getX() + n);
                    user.setX(user.getX() - a);
                    user.setY(user.getY() - a);
                    break;
                case GameUser.MOVE_DOWN_RIGHT:
                    user.setY(user.getY() + n);
                    user.setX(user.getX() + n);
                    user.setX(user.getX() + a);
                    user.setY(user.getY() - a);
                    break;
                case GameUser.MOVE_UP_LEFT:
                    user.setY(user.getY() - n);
                    user.setX(user.getX() - n);
                    user.setX(user.getX() - a);
                    user.setY(user.getY() + a);
                    break;
                case GameUser.MOVE_DOWN_LEFT:
                    user.setY(user.getY() + n);
                    user.setX(user.getX() - n);
                    user.setX(user.getX() + a);
                    user.setY(user.getY() + a);
                    break;
                default:
                    break;
            }
        }
    }

    public void detectionChoose() {
        GameUser user = gameManage.getUser();
        NpcObject[] tempNpc = new NpcObject[100];
        int num = 0;
        int team = gameManage.user.team;
        if (!user.isDragged() && user.isChooseDragged()) {//拖拽
            int size = user.getChooseNpcNum();
            tempNpc = user.getChooseNpc();
            for (int i = 0; i < size; i++) {
                tempNpc[i].setState(NpcObject.STATE_NOTHING);
            }
            GameNpc npc = gameManage.getNpc();
            size = npc.getSize();
            for (int i = 0; i < size; i++) {
                NpcObject temp = npc.getNpc(i);
                if (temp.isEnable() && temp.getTeam() == team) {
                    if (temp.isChoseRange(user.getMouseOfWorld().x, user.getMouseOfWorld().z, user.getDraggedMouseOfWorld().x, user.getDraggedMouseOfWorld().z)) {
                        temp.setState(NpcObject.STATE_CHOOSE);
                        tempNpc[num] = temp;
                        temp.setIsChoosed(true);
                        num++;
                    } else {
                        temp.setIsChoosed(false);
                    }
                }
            }
            user.setChooseNpc(tempNpc, num);
            user.setIsChooseDragged(false);
        } else if (user.isClicked()) {//点击
            boolean isFirst = true;
            int size = user.getChooseNpcNum();
            tempNpc = user.getChooseNpc();
            for (int i = 0; i < size; i++) {
                tempNpc[i].setState(NpcObject.STATE_NOTHING);
            }
            GameNpc npc = gameManage.getNpc();
            size = npc.getSize();
            for (int i = 0; i < size; i++) {
                NpcObject temp = npc.getNpc(i);
                if (temp.isEnable() && temp.getTeam() == team) {
                    if (isFirst && temp.isChoseRange(user.getMouseOfWorld().x, user.getMouseOfWorld().z)) {
                        isFirst = false;
                        temp.setState(NpcObject.STATE_CHOOSE);
                        gameManage.getPanel().setNotice(temp.getBase());
                        tempNpc[num] = temp;
                        temp.setIsChoosed(true);
                        num++;

                    } else {
                        temp.setIsChoosed(false);
                    }
                }
            }
            user.setChooseNpc(tempNpc, num);
            user.setIsClicked(false);
        }
    }

    @Override
    public void run() {
        while (mainThread != null) {
            try {
                Thread.sleep(1000 / gameManage.getUser().getGameSpeed());
            } catch (InterruptedException ex) {
                System.out.println("线程休眠失败");
            }
            if (gameManage.getEvent().isReplay || gameManage.getEvent().isLoad || gameManage.getEvent().isSave || GameGLEventListener.isFirst) {

            } else {
                time++;
                gameTime++;
                if (gameTime >= 10000) {
                    gameTime = 0;
                }
                if (time >= 20) {
                    time = 0;
                }
                viewMove();
                detectionChoose();
                //npc
                mainNpc = new NpcObject[250][250];
                GameNpc gameNpc = gameManage.getNpc();
                int size = gameNpc.getSize();
                for (int i = 0; i < size; i++) {
                    if (gameNpc.getNpc(i).isEnable()) {
                        NpcObject tNpc = gameNpc.getNpc(i);
                        int x = (int) (tNpc.getX() + 0.5f);
                        int y = (int) (tNpc.getY() + 0.5f);
                        mainNpc[x][y] = tNpc;
                    }
                }
                for (int i = 0; i < size; i++) {
                    if (gameNpc.getNpc(i).isEnable()) {
                        NpcObject tNpc = gameNpc.getNpc(i);
                        tNpc.run();
                        if (tNpc.isAlive() && tNpc.isGuard() && tNpc.getAttackNpc() == null) {
                            int view = tNpc.getView();
                            int x = (int) (tNpc.getX() + 0.5f);
                            int y = (int) (tNpc.getY() + 0.5f);
                            int flag = 0;
                            for (int k = x - view; k <= x + view && flag == 0; k++) {
                                for (int m = y - view; m <= y + view && flag == 0; m++) {
                                    if (k < 0 || m < 0 || m >= GameMap.width || k >= GameMap.height) {
                                        continue;
                                    }
                                    if (mainNpc[k][m] != null && mainNpc[k][m] != tNpc&&mainNpc[k][m].getTeam()!=tNpc.getTeam()) {
                                        tNpc.setAttack(mainNpc[k][m]);
                                        flag = 1;
                                    }
                                }
                            }
                        }
                    }
                }
                //建筑
                GameBuild gameBuild = gameManage.getBuild();
                size = gameBuild.getSize();
                for (int i = 0; i < size; i++) {
                    if (gameBuild.getBuild(i).isEnable()) {
                        gameBuild.getBuild(i).run();
                    }
                }
                //转向
                GameUser user = gameManage.getUser();
                if (user.isTurn()) {
                    user.setRotate(user.getRotate() + 5);
                    if (user.getRotate() > 90) {
                        user.setRotate(90);
                    }
                } else {
                    user.setRotate(user.getRotate() - 5);
                    if (user.getRotate() < 0) {
                        user.setRotate(0f);
                    }
                }
            }
        }
    }

}
