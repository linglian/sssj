package sd3;

//********************************************
//*类名:tMatInfo
//*作者:凌恋      时间:2016-8-13 16:26:42
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class SD3MatInfo {

    public String strName;//纹理名称
    public String strFile;//纹理文件
    public byte[] color;//对象的RGB颜色
    public int textureId;//纹理id
    public float uTile;//u重复
    public float vTile;//v重复
    public float uOffset;//u纹理偏移
    public float vOffset;//v纹理偏移

    public SD3MatInfo() {
        strName = new String();
        strFile = new String();
        color = new byte[3];
    }
}
