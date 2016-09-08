package sd3;

//********************************************
//*类名:tFace
//*作者:凌恋      时间:2016-8-13 16:27:08
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class SD3Face {

    public int[] vertIndex;//顶点索引
    public int[] coordIndex;//纹理坐标

    public SD3Face() {
        vertIndex = new int[3];
        coordIndex = new int[3];
    }

    public SD3Face(int v1, int v2, int v3, int c1, int c2, int c3) {
        vertIndex = new int[3];
        vertIndex[0] = v1;
        vertIndex[1] = v2;
        vertIndex[2] = v2;
        coordIndex = new int[3];
        coordIndex[0] = c1;
        coordIndex[1] = c2;
        coordIndex[2] = c3;
    }

    public void display() {
        //System.out.println(vertIndex[0] + "," + vertIndex[2] + "," + vertIndex[2] + "-" + coordIndex[0] + "," + coordIndex[1] + "," + coordIndex[2]);
    }
}
