package md2;

//********************************************
//*类名:tMaterialInfo
//*作者:凌恋      时间:2016-8-8 15:07:58
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class MD2MaterialInfo {

    public String strName;			   // 纹理名称 */
    public String strFile;			 // 纹理文件名称 */
    public byte[] color = new byte[3];				// 对象的RGB颜色 */
    public int texureId;				 // 纹理ID */
    public float uTile;				// u 重复 */
    public float vTile;				  // v重复 */
    public float uOffset;			      // u 纹理偏移 */
    public float vOffset;				  // v 纹理偏移 */
}
