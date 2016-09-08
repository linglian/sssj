package md2;

//********************************************
import tool.Vector3;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;

//*类名:GameMD2Animate
//*作者:凌恋      时间:2016-8-8 15:01:20
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class MD2Animate {

    MD2Model m_3DModel;
    int currentAnimId;//当前块
    public int currentFrame;//当前帧
    public int nextFrame;//下一帧
    int lastAnimId;//上一个帧

    public int ChangeId(int id) {
        currentAnimId = id;
        return id;
    }

    public int ChangeId(String name) {
        int size = m_3DModel.numOfAnimations;
        int found = 0;
        for (int i = 0; i < size; i++) {
            if (m_3DModel.pAnimations.get(i).strName.equals(name)) {
                currentAnimId = i;
                found = 1;
                return i;
            }
        }
        if(found==0){
            currentAnimId = -1;
        }
        return -1;
    }

    public MD2Animate(MD2Model model) {
        m_3DModel = model;
    }

    public void AnimateMD2Model(GL2 gl, boolean isTexture) {
        if (m_3DModel.pObject.size() <= 0) {
            return;
        }

        if(currentAnimId==-1){
            return;
        }
        gl.glPushMatrix();
        gl.glPushAttrib(GL2.GL_CURRENT_BIT);
        if (isTexture) {
            gl.glEnable(GL2.GL_TEXTURE_2D);
        }
        // 获取当前动作信息 */
        MD2AnimationInfo pAnim = m_3DModel.pAnimations.get(currentAnimId);
        // System.out.println(pAnim.strName);
        if (lastAnimId != currentAnimId) {
            lastAnimId = currentAnimId;
            currentFrame = pAnim.startFrame;
        }
        // 计算下一帧 */

        // 如果下一帧为结束帧,则设为开始帧 */
        if (currentFrame > pAnim.endFrame) {
            currentFrame = pAnim.startFrame;
        }
        nextFrame = currentFrame + 1;
        if (nextFrame > pAnim.endFrame) {
            nextFrame = pAnim.startFrame;
        }
        //System.out.println(currentFrame);
        // 获取当前帧信息 */
        MD2Object pFrame = m_3DModel.pObject.get(currentFrame);

        // 获取下一帧信息 */
        MD2Object pNextFrame = m_3DModel.pObject.get(nextFrame);

        // 获取第一帧信息 */
        MD2Object pFirstFrame = m_3DModel.pObject.get(0);
        //System.out.println(m_3DModel.pObject.size());
        // 返回t,进行帧插值 */
        float t = 1;

        if (isTexture) {
            gl.glBindTexture(GL2.GL_TEXTURE_2D, m_3DModel.m_textures[pFrame.materialID]);
        }
        // 开始绘制 */
        if (isTexture) {
            gl.glBegin(GL2.GL_TRIANGLES);
        }
        // 遍历所有面 */
        //
        for (int j = 0; j < pFrame.numOfFaces; j++) {
            // 遍历面的所有顶点 */
            if (!isTexture) {
                gl.glBegin(GL2.GL_LINES);
            }
            //System.out.println(pFirstFrame.pFaces.get(j));
            //   System.out.println(pFirstFrame.pFaces.get(j).vertIndex[0]+","+pFirstFrame.pFaces.get(j).vertIndex[1]+","+pFirstFrame.pFaces.get(j).vertIndex[2]);
            for (int whichVertex = 0; whichVertex < 3; whichVertex++) {
                // 获得该面中顶点和纹理索引 */
                // System.out.println(pFirstFrame.pFaces.size());
                int vertIndex = pFirstFrame.pFaces.get(j).vertIndex[whichVertex];
                //System.out.println(pFirstFrame.pFaces.get(j).vertIndex[whichVertex]);
                int texIndex = pFirstFrame.pFaces.get(j).coordIndex[whichVertex];

                // 检查是否具有纹理 */
                if (isTexture && pFirstFrame.pTexVerts.size() != 0) {
                    // 指定纹理坐标 */
                    gl.glTexCoord2f(pFirstFrame.pTexVerts.get(texIndex).x, pFirstFrame.pTexVerts.get(texIndex).y);
                }

                // 下面开始进行帧插值 */
                Vector3 vPoint1 = pFrame.pVerts.get(vertIndex);
                //System.out.println(pFirstFrame.pFaces.get(j).vertIndex[0]+","+pFirstFrame.pFaces.get(j).vertIndex[1]+","+pFirstFrame.pFaces.get(j).vertIndex[2]);
                Vector3 vPoint2 = pNextFrame.pVerts.get(vertIndex);

                // 利用公式: p(t) = p0 + t(p1 - p0),得到插值点 */
                gl.glVertex3f(vPoint1.x + t * (vPoint2.x - vPoint1.x), vPoint1.y + t * (vPoint2.y - vPoint1.y), vPoint1.z + t * (vPoint2.z - vPoint1.z));
                //gl.glVertex3f(vPoint1.x , vPoint1.y , vPoint1.z );
                //System.out.println((vPoint1.x + t * (vPoint2.x - vPoint1.x)) + "\t" + (vPoint1.y + t * (vPoint2.y - vPoint1.y)) + "\t" + (vPoint1.z + t * (vPoint2.z - vPoint1.z)));
            }
            if (!isTexture) {
                gl.glEnd();
            }
        }
        // 渲染结束 */
        if (isTexture) {
            gl.glEnd();
        }
        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glPopMatrix();
        gl.glPopAttrib();
    }
}
