/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package skill;

import com.jogamp.opengl.GL2;
import granule.Granule;
import granule.SkillAnimation;

/**
 *
 * @author HP
 */
public class GreenHP extends SkillAnimation implements Runnable {

    public GreenHP(float x, float y, float high, int size) {
        this.x = x;
        this.y = y;
        this.high = high;
        granule = new Granule[size];
        number = size;
        aliveNumber = size;
        for (int i = 0; i < size; i++) {
            granule[i] = new Granule(0.1f, 0.1f,0.5f+0.02f*i, 0f, 1f, 0f, 0.5f, 0.02f, 0f, 0f, 0f, 5.5f, 100f, false);
        }
        this.setRotate(18f, 0, 1f, 0f);
    }

    @Override
    public void draw(GL2 gl) {
        gl.glPushMatrix();
        gl.glPushAttrib(GL2.GL_CURRENT_BIT);
        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glTranslated(x, high, y);
        if (this.isRotate) {
            gl.glRotatef(rotate, this.xAngle, this.yAngle, this.zAngle);
            rotate += angleSpeed;
            if (rotate >= 360f) {
                rotate = 0f;
            }
        }
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

    @Override
    public void start() {
        mainThread = new Thread(this);
        mainThread.start();
    }

    @Override
    public void run() {
        while (aliveNumber > 0) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {

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
