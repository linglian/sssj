package granule;

//********************************************
//*类名:Granule
//*作者:凌恋      时间:2016-8-19 21:43:49
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Granule {

    public float x, y, high;
    public float r, g, b;
    public float mix;
    public float width;
    public float xSpeed;
    public float ySpeed;
    public float highSpeed;
    public float addXSpeed;
    public float addYSpeed;
    public float addHighSpeed;
    public float speed;
    public float life;
    public float gravity;
    public boolean isColorChange;
    public boolean isAlive = true;
    boolean addR;
    boolean addG;
    boolean addB;

    public Granule(float x, float y, float high, float r, float g, float b, float mix, float width, float xSpeed, float ySpeed, float highSpeed, float speed, float life, boolean isColorChange) {
        this.x = x;
        this.y = y;
        this.high = high;
        this.r = r;
        this.g = g;
        this.b = b;
        this.mix = mix;
        this.width = width;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.highSpeed = highSpeed;
        this.speed = speed;
        this.life = life;
        this.isColorChange = isColorChange;
    }

    public void run(){
        life -= speed;
        if(life <= 0){
            this.isAlive = false;
            return;
        }
        changeColor();
        changePos();
    }

    public void changePos(){
        if(this.gravity!=0){
            highSpeed-=this.gravity;
        }
        xSpeed+=addXSpeed;
        ySpeed+=addYSpeed;
        highSpeed+=addHighSpeed;
        x += xSpeed;
        y += ySpeed;
        high += highSpeed;
    }
    public void changeColor() {
        float cr = 0.05f;
        float cg = 0.05f;
        float cb = 0.05f;
        if (isColorChange) {

            if (addR) {
                r += cr;
            } else {
                r -= cr;
            }
            if (r <= 0) {
                r = 0;
                addR = true;
            }
            if (r >= 1) {
                r = 1;
                addR = false;
            }

            if (addG) {
                g += cg;
            } else {
                g -= cg;
            }
            if (g <= 0) {
                g = 0;
                addG = true;
            }
            if (g >= 1) {
                g = 1;
                addG = false;
            }

            if (addB) {
                b += cb;
            } else {
                b -= cb;
            }
            if (b <= 0) {
                b = 0;
                addB = true;
            }
            if (b >= 1) {
                b = 1;
                addB = false;
            }
        }
    }
}
