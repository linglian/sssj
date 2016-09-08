package granule;

//********************************************
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

//*类名:SkillAnimation
//*作者:凌恋      时间:2016-8-19 22:16:26
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public abstract class SkillAnimation implements Runnable {

    protected float x, y, high;
    protected Granule[] granule;
    protected int number;
    public int aliveNumber;
    protected float gravity;
    protected float xAngle;
    protected float yAngle;
    protected float zAngle;
    protected float angleSpeed;
    protected float rotate;
    protected Thread mainThread;
    protected boolean hasGra = false;
    protected boolean isRotate = false;

    private void setGra() {
        for (int i = 0; i < number; i++) {
            granule[i].gravity = 0.001f;
        }
    }

    public void setRotate(float speed, float xAngle, float yAngle, float zAngle) {
        this.angleSpeed = speed;
        this.xAngle = xAngle;
        this.yAngle = yAngle;
        this.zAngle = zAngle;
        this.isRotate = true;
    }

    public void start() {
        mainThread = new Thread(this);
        mainThread.start();
    }

    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glPushAttrib(GL2.GL_CURRENT_BIT);
        gl.glDisable(GL2.GL_TEXTURE_2D);
        for (int i = 0; i < number; i++) {
            Granule t = granule[i];
            if (granule[i].isAlive) {
                gl.glPushMatrix();
                gl.glTranslatef(t.x, t.high, t.y);
                gl.glScaled(t.width, t.width, t.width);
                gl.glColor4f(t.r, t.g, t.b, t.mix);
                drawRect(gl);
                gl.glPopMatrix();
            }
        }
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glPopAttrib();
        gl.glPopMatrix();
    }

    protected void drawRect(GL2 gl) {
        gl.glBegin(GL2.GL_QUADS); // Start Drawing The Cube

        gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Top)
        gl.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Top)
        gl.glVertex3f(-1.0f, 1.0f, 1.0f); // Bottom Left Of The Quad (Top)
        gl.glVertex3f(1.0f, 1.0f, 1.0f); // Bottom Right Of The Quad (Top)

        gl.glVertex3f(1.0f, -1.0f, 1.0f); // Top Right Of The Quad 
        gl.glVertex3f(-1.0f, -1.0f, 1.0f); // Top Left Of The Quad 
        gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad 
        gl.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad 

        gl.glVertex3f(1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Front)
        gl.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Left Of The Quad (Front)
        gl.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad 
        gl.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad 

        gl.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad 
        gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad
        gl.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Back)
        gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Back)

        gl.glVertex3f(-1.0f, 1.0f, 1.0f); // Top Right Of The Quad (Left)
        gl.glVertex3f(-1.0f, 1.0f, -1.0f); // Top Left Of The Quad (Left)
        gl.glVertex3f(-1.0f, -1.0f, -1.0f); // Bottom Left Of The Quad 
        gl.glVertex3f(-1.0f, -1.0f, 1.0f); // Bottom Right Of The Quad 

        gl.glVertex3f(1.0f, 1.0f, -1.0f); // Top Right Of The Quad (Right)
        gl.glVertex3f(1.0f, 1.0f, 1.0f); // Top Left Of The Quad 
        gl.glVertex3f(1.0f, -1.0f, 1.0f); // Bottom Left Of The Quad 
        gl.glVertex3f(1.0f, -1.0f, -1.0f); // Bottom Right Of The Quad 
        gl.glEnd(); // Done Drawing The Quad
    }

    @Override
    public void run() {
        while (this.aliveNumber > 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                System.out.println("休眠失败");
            }
            for (int i = 0; i < number; i++) {
                Granule t = granule[i];
                if (t.isAlive) {
                    t.run();
                    if (!t.isAlive) {
                        this.aliveNumber--;
                    }
                }
            }
        }
    }

}
