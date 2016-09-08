package md2;

//********************************************
import java.util.LinkedList;
import tool.Vector2;
import tool.Vector3;

//*类名:t3DObject
//*作者:凌恋      时间:2016-8-8 15:08:40
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class MD2Object {

    public int numOfVerts;		      // 模型中顶点的数目 */
    public int numOfFaces;			  // 模型中面的数目 */
    public int numTexVertex;			  // 模型中纹理坐标的数目 */
    public int materialID;                               // 纹理ID */
    public boolean bHasTexture;			  // 是否具有纹理映射 */
    public String strName;			  // 对象的名称 */
    public LinkedList<Vector3> pVerts;			  // 对象的顶点 */
    public LinkedList<Vector3> pNormals;		      // 对象的法向量 */
    public LinkedList<Vector2> pTexVerts;		  // 纹理UV坐标 */
    public LinkedList<MD2FaceIndex> pFaces;		      // 对象的面信息 */

    public MD2Object() {
        pVerts = new LinkedList();
        pTexVerts = new LinkedList();
        pNormals = new LinkedList();
        pFaces = new LinkedList();
    }
}
