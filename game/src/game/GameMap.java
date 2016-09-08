package game;

//********************************************
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import java.util.LinkedList;
import java.util.Random;
import map.Map;
import sd3.Game3DS;

//*类名:GameMap
//*作者:凌恋      时间:2016-8-13 16:52:06
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameMap {

    public static int width;
    public static int height;
    public static Map[][][] map;
    public static boolean[][][] isLooked;
    public static int[][][] isLooking;
    public static LinkedList<Game3DS> mapModel;
    public boolean isQuanliang = false;
    GameManage gameManage;

    public GameMap(int w, int h, GameManage gameManage) {
        this.gameManage = gameManage;
        width = w;
        height = h;
    }

    public void drawMap(GL2 gl) {
        if (gameManage.getEvent().isReplay) {
            return;
        }
        int team = gameManage.user.team;
        gl.glPushMatrix();
        gl.glPushAttrib(GL2.GL_CURRENT_BIT);
        gl.glEnable(GL2.GL_BLEND);
        GameUser user = gameManage.getUser();
        int x = (int) user.getX();
        int y = (int) user.getY();
        int range = user.range;
        for (int i = x - range; i < x + range; i++) {
            for (int j = y - range; j < y + range; j++) {
                if (i < 0 || j < 0 || i >= width || j >= height) {
                    continue;
                }
                gl.glEnable(GL2.GL_LIGHTING);
                if (isLooked[team][i][j]) {
                    for (int k = 0; k < 10; k++) {
                        if (map[i][j][k] != null) {
                            if (map[i][j][k].id == -1) {
                                continue;
                            }
                            if (map[i][j][k].isLight) {
                                gl.glDisable(GL2.GL_LIGHTING);
                            }
                            gl.glPushMatrix();
                            gl.glTranslatef(i, k, j);
                            gl.glScalef(1f, map[i][j][k].getHigh(), 1f);
                            if (map[i][j][k].id != 0) {
                                switch (map[i][j][k].num) {
                                    case 1:
                                        gl.glColor4f(1f, 1f, 0f, 0.5f);
                                        break;
                                    case 2:
                                        gl.glColor4f(0.2f, 0.5f, 0.9f, 0.5f);
                                        break;
                                    case 3:
                                        gl.glColor4f(0f, 1f, 0f, 0.5f);
                                        break;
                                    case 4:
                                        gl.glColor4f(0f, 1f, 1f, 0.5f);
                                        break;
                                    case 5:
                                        gl.glColor4f(0f, 0f, 1f, 0.5f);
                                        break;
                                    case 6:
                                        gl.glColor4f(1f, 0f, 1f, 0.5f);
                                        break;
                                    case 7:
                                        gl.glColor4f(0f, 0.8f, 0.25f, 0.5f);
                                        break;
                                    default:
                                        gl.glColor4f(0.8f, 0.8f, 0.8f, 1f);
                                        break;
                                }
                            } else {
                                gl.glColor4f(1f, 1f, 1f, 1f);
                            }
                            if(isLooking[team][i][j]-GameThread.gameTime<=0){
                                gl.glColor4f(0.2f,0.2f,0.2f,0.5f);
                            }
                            mapModel.get(map[i][j][k].id).draw(gl);
                            gl.glPopMatrix();
                        }
                    }
                } else {
                    gl.glPushMatrix();
                    gl.glTranslatef(i, 0, j);
                    gl.glScalef(1f, map[i][j][0].getHigh(), 1f);
                    gl.glColor4f(0.03f, 0.03f, 0.03f, 1f);
                    mapModel.get(0).draw(gl);
                    gl.glPopMatrix();
                }
            }
        }
        gl.glDisable(GL2.GL_BLEND);
        gl.glPopAttrib();
        gl.glPopMatrix();
    }

    public static float getHigh(int x, int y) {
        float high = 0;
        for (int i = 0; i < 10; i++) {
            if (map[x][y][i] == null || map[x][y][i].id == -1) {
                break;
            }
            high += map[x][y][i].getHigh();
        }
        return high;
    }

    public static int getHigher(int x, int y) {
        int higher = 0;
        for (int i = 0; i < 10; i++) {
            if (map[x][y][i] == null || map[x][y][i].id == -1) {
                break;
            }
            higher++;
        }
        return higher;
    }

    public static boolean isCanMoveHigh(int x, int y) {
        for (int i = 0; i < 10; i++) {
            if (GameMap.map[x][y][i].id != -1 &&( !GameMap.map[x][y][i].isCanMove || GameMap.map[x][y][i].isHasBuild)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isCanMoved(int x, int y, float high) {
        if (x < 0 || x >= width || y < 0 || y >= height) {
            return false;
        } else if (!isCanMoveHigh(x, y) || GameMap.getHigh(x, y) > high + 1.3f) {
            return false;
        } else {
            return true;
        }
    }

    public static int[][][] getIsLooking() {
        return isLooking;
    }

    public static void setIsLooking(int[][][] isLooking) {
        GameMap.isLooking = isLooking;
    }

    public boolean[][][] getIsLooked() {
        return isLooked;
    }

    public void setIsLooked(boolean[][][] isLooked) {
        GameMap.isLooked = isLooked;
    }

    public void setRandomMap(int x, int y) {
        int h = GameMap.getHigher(x, y);
        Random r = new Random();
        if (r.nextInt(25) == 0) {
            this.map[x][y][h] = new Map(x, y, h, r.nextInt(2), r.nextInt(8) + 1, 1f);
        }
    }

    public void replay() {
        gameManage.getMainBar().setText("正在初始化地图");
        int w = width;
        int h = height;
        isLooked = new boolean[8][w][h];
        isLooking = new int[8][w][h];
        this.map = new Map[w][h][10];
        for(int k=0;k<8;k++)
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                if (this.isQuanliang) {
                    isLooked[k][i][j] = true;
                    isLooking[k][i][j] = 1;
                } else {
                    isLooked[k][i][j] = false;
                    isLooking[k][i][j] =0;
                }
            }
        }
        Random r = new Random();
        for (int i = 0; i < w; i++) {
            gameManage.getMainBar().barNum += 1f / 25f;
            this.map[i] = new Map[h][10];
            for (int j = 0; j < h; j++) {
                this.map[i][j] = new Map[10];
                for (int k = 0; k < 10; k++) {
                    if (k == 0) {
                        this.map[i][j][k] = new Map(i, j, k, 0, r.nextInt(8), 1f);
                    } else if (k == 1) {
                        int ran = r.nextInt(100);
                        if (ran <= 80) {
                            this.map[i][j][k] = new Map(i, j, k, -1, 0, 1f);
                        } else {
                            this.map[i][j][k] = new Map(i, j, k, 0, r.nextInt(8), 1f);
                        }
                    } else {
                        this.map[i][j][k] = new Map(i, j, k, -1, 0, 1f);
                    }
                }
            }
        }

    }

    public void init(GLAutoDrawable glad) {
        gameManage.getMainBar().setText("正在读取地图模型");
        mapModel = new LinkedList();
        Game3DS temp = new Game3DS(glad);
        temp.init("model\\3ds\\block.3DS", "model\\3ds");
        mapModel.add(temp);
        temp = new Game3DS(glad);
        temp.init("model\\3ds\\huodui.3DS", "model\\3ds");
        mapModel.add(temp);
        gameManage.getMainBar().barNum++;
        replay();
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

    public Map getMap(int x, int y, int z) {
        return map[x][y][z];
    }

    public void setMap(int x, int y, int z, Map map) {
        if (z > 9 || z < 0) {
            return;
        }
        this.map[x][y][z] = map;
    }

}
