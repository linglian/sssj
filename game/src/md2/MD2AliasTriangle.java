package md2;

//********************************************
//*类名:tMd2AliasTriangle
//*作者:凌恋      时间:2016-8-8 15:11:08
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class MD2AliasTriangle {

    public int[] vertex = new int[3];
    public int lightNormalIndex;

    public MD2AliasTriangle(byte a, byte b, byte c, byte d) {
        vertex[0] = a & 0xff;
        vertex[1] = b & 0xff;
        vertex[2] = c & 0xff;
        lightNormalIndex = d & 0xff;
    }
}
