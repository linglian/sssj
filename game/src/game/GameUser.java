package game;

//********************************************
import function.GameFunction;
import npc.Build;
import function.GameFunctionBuild;
import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import npc.NpcObject;
import tool.Vector3;

//*类名:Game
//*作者:凌恋      时间:2016-8-13 20:00:19
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameUser {

    int gameSpeed = 20;
    int mouseX;
    int mouseY;
    int lastMouseX;
    int lastMouseY;
    int releasedMouseX;
    int releasedMouseY;
    int team;
    int gold;
    int wood;
    int water;
    int rock;
    int population;
    int maxPopulation;
    int peopleNumber;
    int maxpeopleNumber;
    int buildBuildId;
    int moveState;

    float x;
    float y;
    float high = 12.725f;
    float wide = 7.5f;
    float moveSpeed = 0.5f;

    double rotate = 0.0;

    boolean isView = false;
    boolean isBuild = false;
    boolean isTurn = false;
    boolean isClicked = false;
    boolean isDragged = false;
    boolean isChooseDragged = false;
    boolean isCtrl = false;
    boolean flow = false;

    Vector3 mouseOfWorld;
    Vector3 draggedMouseOfWorld;

    private NpcObject[] chooseNpc;//选择ing的npc
    int chooseNpcNum;
    private Build[] chooseBuild;//选择ing的建筑
    int chooseBuildNum;
    NpcObject choseNpc;//鼠标停留的npc
    Build choseBuild;//鼠标停留的建筑

    GameFunction[] gameFunction;
    GameFunctionBuild[] npcBuildFunction;

    public int range = 13;
    public float lastX;
    public float lastY;
    public static final int MOVE_STOP = 0;
    public static final int MOVE_UP = 1;
    public static final int MOVE_DOWN = 2;
    public static final int MOVE_LEFT = 3;
    public static final int MOVE_RIGHT = 4;
    public static final int MOVE_UP_RIGHT = 5;
    public static final int MOVE_UP_LEFT = 6;
    public static final int MOVE_DOWN_LEFT = 7;
    public static final int MOVE_DOWN_RIGHT = 8;

    public static final int TEAM_1 = 1;
    public static final int TEAM_2 = 2;
    public static final int TEAM_3 = 3;
    public static final int TEAM_4 = 4;
    public static final int TEAM_5 = 5;
    public static final int TEAM_6 = 6;
    public static final int TEAM_7 = 7;

    GameManage gameManage;
    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    public GameUser(int x, int y, GameManage gameManage) {
        this.gameManage = gameManage;
        this.chooseNpc = new NpcObject[100];
        chooseNpcNum = 0;
        mouseOfWorld = new Vector3(0, 0, 0);
        draggedMouseOfWorld = new Vector3(0, 0, 0);
        this.x = x;
        this.y = y;
        population = 0;
        maxPopulation = 40;
        Random r = new Random();
        this.team = r.nextInt(7) + 1;
        this.npcBuildFunction = new GameFunctionBuild[20];
        for (int i = 0; i < 20; i++) {
            this.npcBuildFunction[i] = new GameFunctionBuild(gameManage, i);
        }
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getMaxPopulation() {
        return maxPopulation;
    }

    public void setMaxPopulation(int maxPopulation) {
        this.maxPopulation = maxPopulation;
    }

    public void setNpcBuild() {
        for (int i = 0; i < 20; i++) {
            this.gameFunction = this.npcBuildFunction;
        }
    }

    public GameFunction[] getGameFunction() {
        return gameFunction;
    }

    public void setGameFunction(GameFunction[] gameFunction) {
        this.gameFunction = gameFunction;
    }

    public boolean isView() {
        return isView;
    }

    public void setIsView(boolean isView) {
        this.isView = isView;
    }

    public boolean isCtrl() {
        return isCtrl;
    }

    public void setIsCtrl(boolean isCtrl) {
        this.isCtrl = isCtrl;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getWood() {
        return wood;
    }

    public void setWood(int wood) {
        this.wood = wood;
    }

    public int getWater() {
        return water;
    }

    public void setWater(int water) {
        this.water = water;
    }

    public int getRock() {
        return rock;
    }

    public void setRock(int rock) {
        this.rock = rock;
    }

    public int getPeopleNumber() {
        return peopleNumber;
    }

    public void setPeopleNumber(int peopleNumber) {
        this.peopleNumber = peopleNumber;
    }

    public int getMaxpeopleNumber() {
        return maxpeopleNumber;
    }

    public void setMaxpeopleNumber(int maxpeopleNumber) {
        this.maxpeopleNumber = maxpeopleNumber;
    }

    public Build getChoseBuild() {
        return choseBuild;
    }

    public void setChoseBuild(Build choseBuild) {
        this.choseBuild = choseBuild;
        if (chooseBuild != null) {
            this.choseNpc = null;
        }
    }

    public void setChooseBuild(Build[] tempBuild, int num) {
        try {
            lock.writeLock().lock();
            chooseBuild = tempBuild;
            chooseBuildNum = num;
            if (chooseBuild != null) {
                this.chooseNpcNum = 0;
                this.chooseNpc = null;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Build[] getChooseBuild() {
        try {
            lock.readLock().lock();
            return chooseBuild;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getChooseBuildNum() {
        try {
            lock.readLock().lock();
            return chooseBuildNum;
        } finally {
            lock.readLock().unlock();
        }
    }

    public NpcObject getChoseNpc() {
        return choseNpc;
    }

    public void setChoseNpc(NpcObject choseNpc) {
        this.choseNpc = choseNpc;
        if (chooseNpc != null) {
            this.choseBuild = null;
        }
    }

    public void setChooseNpc(NpcObject[] tempNpc, int num) {
        try {
            lock.writeLock().lock();
            chooseNpc = tempNpc;
            chooseNpcNum = num;
            if (chooseNpc != null) {
                this.chooseBuildNum = 0;
                this.chooseBuild = null;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public NpcObject[] getChooseNpc() {
        try {
            lock.readLock().lock();
            return chooseNpc;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getChooseNpcNum() {
        try {
            lock.readLock().lock();
            return chooseNpcNum;
        } finally {
            lock.readLock().unlock();
        }
    }

    public int getGameSpeed() {
        return gameSpeed;
    }

    public void setGameSpeed(int gameSpeed) {
        this.gameSpeed = gameSpeed;
    }

    public void replay(int x, int y) {
        this.x = x;
        this.y = y;
        Random r = new Random();
        this.team = r.nextInt(7) + 1;
        this.chooseNpc = new NpcObject[100];
        chooseNpcNum = 0;
        mouseOfWorld = new Vector3(0, 0, 0);
        draggedMouseOfWorld = new Vector3(0, 0, 0);
    }

    public boolean isCantHandle() {
        if (flow) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isFlow() {
        return flow;
    }

    public void setFlow(boolean flow) {
        this.flow = flow;
    }

    public int getBuildBuildId() {
        return buildBuildId;
    }

    public boolean isBuild() {
        return isBuild;
    }

    public void setIsBuild(boolean isBuild, int build) {
        this.isBuild = isBuild;
        this.buildBuildId = build;
    }

    public double getRotate() {
        return rotate;
    }

    public void setRotate(double rotate) {
        this.rotate = rotate;
    }

    public boolean isTurn() {
        return isTurn;
    }

    public void setIsTurn(boolean isTurn) {
        this.isTurn = isTurn;
    }

    public int getTeam() {
        return team;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getWide() {
        return wide;
    }

    public void setWide(float wide) {
        this.wide = wide;
    }

    public Vector3 getDraggedMouseOfWorld() {
        return draggedMouseOfWorld;
    }

    public void setDraggedMouseOfWorld(Vector3 draggedMouseOfWorld) {
        this.draggedMouseOfWorld = draggedMouseOfWorld;
    }

    public int getReleasedMouseX() {
        return releasedMouseX;
    }

    public void setReleasedMouseX(int releasedMouseX) {
        this.releasedMouseX = releasedMouseX;
    }

    public int getReleasedMouseY() {
        return releasedMouseY;
    }

    public void setReleasedMouseY(int releasedMouseY) {
        this.releasedMouseY = releasedMouseY;
    }

    public boolean isChooseDragged() {
        return isChooseDragged;
    }

    public void setIsChooseDragged(boolean isChooseDragged) {
        this.isChooseDragged = isChooseDragged;
    }

    public boolean isDragged() {
        return isDragged;
    }

    public void setIsDragged(boolean isDragged) {
        this.isDragged = isDragged;
    }

    public int getLastMouseX() {
        return lastMouseX;
    }

    public void setLastMouseX(int lastMouseX) {
        this.lastMouseX = lastMouseX;
    }

    public int getLastMouseY() {
        try {
            lock.readLock().lock();
            return lastMouseY;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void setLastMouseY(int lastMouseY) {
        this.lastMouseY = lastMouseY;
    }

    public Vector3 getMouseOfWorld() {
        return mouseOfWorld;
    }

    public void setMouseOfWorld(Vector3 mouseOfWorld) {
        this.mouseOfWorld = mouseOfWorld;
    }

    public boolean isClicked() {
        return isClicked;
    }

    public void setIsClicked(boolean isClicked) {
        this.isClicked = isClicked;
    }

    public int getMouseX() {
        return mouseX;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getMoveState() {
        return moveState;
    }

    public void setMoveState(int move) {
        this.moveState = move;
    }

    public float getMoveSpeed() {
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

}
