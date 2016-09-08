package md2;

//********************************************
import com.jogamp.opengl.GLAutoDrawable;
import java.util.LinkedList;
import sd3.Game3DS;

//*类名:t3DModel
//*作者:凌恋      时间:2016-8-8 15:10:15
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class MD2Model {

// 模型信息结构体 */
    public int numOfObjects;			        // 模型中对象的数目 */
    public int numOfMaterials;			        // 模型中材质的数目 */
    public int numOfAnimations;                // 模型中动作的数目 */
    private int currentAnim;                    // 帧索引 */
    private int currentFrame;                   // 当前帧 */
    public LinkedList<MD2AnimationInfo> pAnimations; // 帧信息链表 */
    public LinkedList<MD2MaterialInfo> pMaterials;	// 材质链表信息 */
    public LinkedList<MD2Object> pObject;	        // 模型中对象链表信息 */
    public int[] m_textures;    // 纹理数组 */

    public MD2Model() {
        pAnimations = new LinkedList();
        pMaterials = new LinkedList();
        pObject = new LinkedList();
        m_textures = new int[100];
    }
}
