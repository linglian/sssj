package game;

//********************************************
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import npc.NpcObject;
import tool.Vector2;
import tool.Vector3;

//*类名:NpcGLEventListener
//*作者:凌恋      时间:2016-8-18 15:59:54
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class NpcGLEventListener implements GLEventListener {

    GLU glu = new GLU();
    GLUT glut = new GLUT();

    float rirt = 0;
    GameManage gameManage;

    public boolean isFirst = true;
    float ambientLight[] = {1f, 1f, 1.0f, 1.0f};

    public NpcGLEventListener(GameManage gameManage) {
        this.gameManage = gameManage;
    }

    public void drawBackground(GL2 gl) {
        gl.glPushMatrix();
        gl.glPushAttrib(GL2.GL_CURRENT_BIT);
        gl.glEnable(GL2.GL_BLEND);
        int team = gameManage.getUser().getTeam();
        switch (team) {
            case GameUser.TEAM_1:
                gl.glColor4f(1f, 1f, 0f, 1f);
                break;
            case GameUser.TEAM_2:
                gl.glColor4f(0.2f, 0.5f, 0.9f, 1f);
                break;
            case GameUser.TEAM_3:
                gl.glColor4f(0f, 1f, 0f, 1f);
                break;
            case GameUser.TEAM_4:
                gl.glColor4f(0f, 1f, 1f, 1f);
                break;
            case GameUser.TEAM_5:
                gl.glColor4f(0f, 0f, 1f, 1f);
                break;
            case GameUser.TEAM_6:
                gl.glColor4f(1f, 0f, 1f, 1f);
                break;
            case GameUser.TEAM_7:
                gl.glColor4f(0f, 0.8f, 0.25f, 1f);
                break;
            default:
                gl.glColor4f(0f, 0f, 0f, 1f);
                break;
        }
        int h = 5;
        for (int i = 0; i < h; i++) {
            for (int j = 0; j < h; j++) {
                gl.glPushMatrix();
                gl.glTranslatef(-h / 2 + i, h / 2 - j, -h / 2);
                GameMap.mapModel.get(0).draw(gl);
                gl.glPopMatrix();
            }
        }
        gl.glDisable(GL2.GL_BLEND);
        gl.glPopAttrib();
        gl.glPopMatrix();
    }

    @Override
    public void display(GLAutoDrawable glad) {
        GL2 gl = glad.getGL().getGL2();
        if (isFirst && !GameGLEventListener.isFirst) {
            isFirst = false;
            int mapSize = GameMap.mapModel.size();
            for (int i = 0; i < mapSize; i++) {
                GameMap.mapModel.get(i).LoadTexture(gl);
            }
            int npcSize = GameNpc.npcModelExample.size();
            for (int i = 0; i < npcSize; i++) {
                int s = GameNpc.npcModelExample.get(i).size();
                for (int j = 0; j < s; j++) {
                    GameNpc.npcModelExample.get(i).get(j).LoadTexture(gl);
                }
            }
        }
        if (isFirst || gameManage.getEvent().isReplay || gameManage.getEvent().isLoad || gameManage.getEvent().isSave) {
            return;
        }
        //清空缓存
        gl.glClearColor(0f, 0f, 0f, 0f);
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();//重置坐标系 
        glu.gluLookAt(0f, 0.5f, 1f, 0f, 0.5f, 0f, 0f, 1f, 0f);
        int num = gameManage.getUser().getChooseNpcNum();
        NpcObject[] tNpc = gameManage.getUser().getChooseNpc();
        if (num >= 1) {
            gl.glColor4f(1f, 1f, 1f, 1f);
            NpcObject tNpcObject = tNpc[0];
            gl.glPushMatrix();
            gl.glPushAttrib(GL2.GL_CURRENT_BIT);
            float width;
            //gl.glColor4f(0.5f, 0.5f, 0.5f, 1f);
            gl.glTranslatef(0f, 0f, 0f);
            gl.glRotatef(270f, 0f, 1f, 0f);
            width = tNpcObject.getWidth();
            gl.glScaled(width, width, width);
            tNpcObject.draw(gl, true);
            gl.glPopAttrib();
            gl.glPopMatrix();
            drawBackground(gl);
        }
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
        gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
        gl.glLightModelfv(GL2.GL_LIGHT_MODEL_AMBIENT, FloatBuffer.wrap(ambientLight));
    }

    @Override
    public void dispose(GLAutoDrawable glad) {

    }
}
