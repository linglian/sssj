package game;

//********************************************
import npc.Build;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.util.texture.TextureIO;
import granule.SkillAnimation;
import gui.MainLoadBar;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;
import java.util.Random;
import map.Map;
import npc.Npc;
import npc.NpcObject;
import sd3.Game3DS;
import skill.skill;
import tool.Vector2;
import tool.Vector3;
import toolkit.GameString;

//*类名:GameGLEventListener
//*作者:凌恋      时间:2016-8-13 17:03:21
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class GameGLEventListener implements GLEventListener {

    GLU glu = new GLU();
    GLUT glut = new GLUT();
    float lightPositionR[] = {125f, 20f, 125f, 1f};//红灯位置-Position(位置)
    float lightPositionG[] = {125f, 20f, 125f, 1f};
    float lightPositionB[] = {125f, 20f, 125f, 1f};
    float diffuseLightR[] = {1f, 0f, 0f, 1f};//漫射光
    float diffuseLightG[] = {0f, 1f, 0f, 1f};
    float diffuseLightB[] = {0f, 0f, 1f, 1f};
    float specularLightR[] = {1f, 0f, 0f, 1f};//镜面光
    float specularLightG[] = {0f, 1f, 0f, 1f};
    float specularLightB[] = {0f, 0f, 1f, 1f};
    //默认光
    float lightPosition[] = {0f, 50f, 0f, 1f};
    float diffuseLight[] = {1f, 1f, 1f, 1f};
    float specularLight[] = {1f, 1f, 1f, 1f};

    float rirt = 0;
    GameManage gameManage;

    public static boolean isFirst = true;
    public static boolean isDisFirst = true;
    public static boolean isBegin = false;
    //float ambientLight[] = {0.5f, 0.5f, 0.5f, 1.0f};

    public GameGLEventListener(GameManage gameManage) {
        this.gameManage = gameManage;
    }

    @Override
    public void display(GLAutoDrawable glad) {
        if (!isBegin) {
            return;
        }
        if (isDisFirst) {
            isDisFirst = false;
            gameManage.getMainBar().reset(0, 0, gameManage.getFrame().getWidth(), gameManage.getFrame().getHeight(), 35f, "正在初始化游戏Ing");
            gameManage.addGameGUI(gameManage.getMainBar());
            gameManage.getMainBar().setText("正在加载UI");
            gameManage.getPanel().init2();
            gameManage.getMainBar().barNum += 6f;
            gameManage.getMap().init(glad);
            gameManage.getNpc().init(glad);
            gameManage.getBuild().init(glad);
            gameManage.getMainBar().setViewable(false);
            gameManage.setFocusGameGui(null);
            gameManage.removeGameGUI(gameManage.getMainBar());
            isFirst = false;
            gameManage.getPanel().getStrLinkedList().add(new GameString("欢迎来到沙石世界II", gameManage.getFrame().getWidth() / 2 - (int) (gameManage.getFrame().getHeight() * 0.2f), gameManage.getFrame().getHeight() / 2 - (int) (gameManage.getFrame().getHeight() * 0.1f), 0.05f, 10, 20, Color.GRAY));
        } else if (!isFirst) {
            if (gameManage.getEvent().isReplay || gameManage.getEvent().isLoad || gameManage.getEvent().isSave) {
                return;
            }
            GL2 gl = glad.getGL().getGL2();
            //清空缓存
            gl.glClearColor(0f, 0f, 0f, 0f);
            gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
            gl.glLoadIdentity();//重置坐标系
            double c = Math.cos(Math.toRadians(gameManage.getUser().getRotate()));
            if (c < -1.5 || c > 1.5) {
                c = 0;
            }
            double s = Math.sin(Math.toRadians(gameManage.getUser().getRotate()));
            if (s < -1.5 || s > 1.5) {
                s = 0;
            }
            double tz = c * gameManage.getUser().getWide();
            double tx = s * gameManage.getUser().getWide();
            float lightPosition[] = {(float) gameManage.getUser().getX() + (float) tx, (float) gameManage.getUser().getHigh(), (float) gameManage.getUser().getY() + (float) tz, 1f};
            gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, FloatBuffer.wrap(lightPosition));
            if (gameManage.getUser().isView() && gameManage.getUser().getChooseNpcNum() >= 1) {
                float x = gameManage.getUser().getChooseNpc()[0].getX();
                float y = gameManage.getUser().getChooseNpc()[0].getY();
                float high = gameManage.getUser().getChooseNpc()[0].getHigh();
                c = Math.cos(Math.toRadians(gameManage.getUser().getChooseNpc()[0].getFace() - 270f));
                if (c < -1.5 || c > 1.5) {
                    c = 0;
                }
                s = Math.sin(Math.toRadians(gameManage.getUser().getChooseNpc()[0].getFace() - 270f));
                if (s < -1.5 || s > 1.5) {
                    s = 0;
                }
                gameManage.getUser().setX(x);
                gameManage.getUser().setY(y);
                glu.gluLookAt(x, high + 1.1f, y + 0.2f, x + 10f * s, high + 1.1f, y + 10f * c, 0f, 1f, 0f);
            } else {
                glu.gluLookAt(gameManage.getUser().getX() + tx, gameManage.getUser().getHigh(), gameManage.getUser().getY() + tz, gameManage.getUser().getX(), 0f, gameManage.getUser().getY(), 0f, 1f, 0f);
            }//画地图
            gl.glEnable(GL2.GL_LIGHTING);
            gameManage.getMap().drawMap(gl);
            //画Npc
            gl.glDisable(GL2.GL_LIGHTING);
            gameManage.getNpc().drawNpc(gl);
            //画建筑
            gameManage.getBuild().drawBuild(gl);
            //画粒子
            drawSkill(gl);
            //检测鼠标
            detectionMouse(gl);
            drawMakeBuild(gl);
        }
    }

    public void drawSkill(GL2 gl) {
        LinkedList<SkillAnimation> skill = gameManage.getSkill();
        int size = skill.size();
        for (int i = 0; i < size; i++) {
            if (skill.get(i).aliveNumber <= 0) {
                skill.remove(i);
                i--;
                size--;
            } else {
                skill.get(i).draw(gl);
            }
        }
    }

    public void drawMakeBuild(GL2 gl) {
        if (gameManage.getEvent().isOtherGUI) {
            return;
        }
        GameUser user = gameManage.getUser();
        if (user.isBuild()) {
            gl.glPushMatrix();
            gl.glTranslatef((int) (user.getMouseOfWorld().x + 0.5f), gameManage.getMap().getHigh((int) (user.getMouseOfWorld().x + 0.5f), (int) (user.getMouseOfWorld().z + 0.5f)), (int) (user.getMouseOfWorld().z + 0.5f));
            int id = gameManage.getUser().getBuildBuildId();
            if (id != -1) {
                if (GameMap.isCanMoveHigh((int) (user.getMouseOfWorld().x + 0.5f), (int) (user.getMouseOfWorld().z + 0.5f))) {
                    gl.glColor4f(0f, 1f, 0f, 0.6f);
                } else {
                    gl.glColor4f(1f, 0f, 0f, 0.6f);
                }
                gameManage.getBuild().buildModel.get(id).draw(gl);
            }
            gl.glPopMatrix();
        }
    }

    //检测鼠标函数
    public void detectionMouse(GL2 gl) {
        if (gameManage.getEvent().isOtherGUI) {
            return;
        }
        GameUser user = gameManage.getUser();
        if (user.isChooseDragged()) {
            int xx = user.getReleasedMouseX();
            int yy = user.getReleasedMouseY();
            user.setDraggedMouseOfWorld(getMousePostion(xx, yy, gl));
            user.setIsDragged(false);
        } else if (user.getLastMouseX() != user.getMouseX() || user.getLastMouseY() != user.getMouseY()) {
            int x = user.getMouseX();
            int y = user.getMouseY();
            user.setMouseOfWorld(getMousePostion(x, y, gl));
            gameManage.getNpc().isChange = true;
        }
    }

    //转换坐标
    public Vector3 getMousePostion(int x, int y, GL2 gl) {
        float[] depth = new float[2];
        float[] point = new float[3];
        float[] p2 = new float[3];
        float[] model = new float[16];
        float[] project = new float[16];
        gl.glGetFloatv(GL2.GL_MODELVIEW_MATRIX, FloatBuffer.wrap(model));
        gl.glGetFloatv(GL2.GL_PROJECTION_MATRIX, FloatBuffer.wrap(project));
        int[] viewport = {0, 0, gameManage.getFrame().getWidth(), gameManage.getFrame().getHeight()};
        //转换3维
        float winX = (float) x;
        float winY = (float) viewport[3] - (float) y;
        gl.glReadPixels(x, (int) winY, 1, 1, GL2.GL_DEPTH_COMPONENT, GL2.GL_FLOAT, FloatBuffer.wrap(depth));
        glu.gluUnProject(winX, winY, (float) depth[0], FloatBuffer.wrap(model), FloatBuffer.wrap(project), IntBuffer.wrap(viewport), FloatBuffer.wrap(point));
        //转换2维
        GameUser user = gameManage.getUser();
        GameNpc npc = gameManage.getNpc();
        int size = npc.getSize();
        int height = gameManage.getFrame().getHeight();
        for (int i = 0; i < size; i++) {
            NpcObject tNpc = npc.getNpc(i);
            if (tNpc.isEnable()) {
                float tx = tNpc.getX() - 0.5f;
                float ty = tNpc.getY();
                float h = tNpc.getHigh() + 1.5f;
                if (tx <= user.getX() + user.range && tx >= user.getX() - user.range && ty <= user.getY() + user.range && ty >= user.getY() - user.range) {
                    if (user.lastY != user.y || user.lastX != user.x || tx != tNpc.v.x || ty != tNpc.v.z || h != tNpc.v.y) {
                        glu.gluProject(tx, h, ty, FloatBuffer.wrap(model), FloatBuffer.wrap(project), IntBuffer.wrap(viewport), FloatBuffer.wrap(p2));
                        tNpc.setScrnPoint(new Vector2(p2[0], height - p2[1]));
                        float t = p2[0];
                        glu.gluProject(tx + 1f, h, ty, FloatBuffer.wrap(model), FloatBuffer.wrap(project), IntBuffer.wrap(viewport), FloatBuffer.wrap(p2));
                        tNpc.setWide(p2[0] - t);
                    }
                }
            }
        }
        return new Vector3(point[0], point[1], point[2]);
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int width, int height) {
        GL2 gl = glad.getGL().getGL2();
        if (height <= 0) {
            height = 1;
        }
        float h = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        glu.gluPerspective(45f, h, 0.1f, 1000f);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void init(GLAutoDrawable glad) {
        GL2 gl = glad.getGL().getGL2();

        gl.glShadeModel(GL2.GL_SMOOTH);
        gl.glClearDepth(1.0f);// 设置深度缓存   
        gl.glEnable(GL2.GL_DEPTH_TEST);// 启用深度测试   
        gl.glDepthFunc(GL2.GL_LEQUAL);// 所作深度测试的类型  
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        //gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, GL2.GL_TRUE);
        // gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
        // gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, FloatBuffer.wrap(ambientLight));
        this.gameManage.setGL(glu, gl, glut);
        gl.glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, FloatBuffer.wrap(lightPosition));
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_DIFFUSE, FloatBuffer.wrap(diffuseLight));
        gl.glLightfv(GL2.GL_LIGHT0, GL2.GL_SPECULAR, FloatBuffer.wrap(specularLight));
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_POSITION, FloatBuffer.wrap(lightPositionR));
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_DIFFUSE, FloatBuffer.wrap(diffuseLightR));
        gl.glLightfv(GL2.GL_LIGHT1, GL2.GL_SPECULAR, FloatBuffer.wrap(specularLightR));
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_POSITION, FloatBuffer.wrap(lightPositionG));
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_DIFFUSE, FloatBuffer.wrap(diffuseLightG));
        gl.glLightfv(GL2.GL_LIGHT2, GL2.GL_SPECULAR, FloatBuffer.wrap(specularLightG));
        gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_POSITION, FloatBuffer.wrap(lightPositionB));
        gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_DIFFUSE, FloatBuffer.wrap(diffuseLightB));
        gl.glLightfv(GL2.GL_LIGHT3, GL2.GL_SPECULAR, FloatBuffer.wrap(specularLightB));

        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
    }

    @Override
    public void dispose(GLAutoDrawable glad) {

    }
}
