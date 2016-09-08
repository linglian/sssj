package md2;

//********************************************
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import tool.ByteLoad;
import tool.Vector2;
import tool.Vector3;

//*类名:GameMD2
//*作者:凌恋      时间:2016-8-6 14:39:13
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class MD2Loader {

    float time;
    int lastAnim;
    GLAutoDrawable glad;
    GL2 gl;
    public float kAnimationSpeed = 5.0f;
    public String tMd2Skin;
    public String strTexture;
    public String strFileName;
    public int MAX_TEXTURES = 100;
    public final static int MD2_MAX_TRIANGLES = 4096;
    public final static int MD2_MAX_VERTICES = 2048;
    public final static int MD2_MAX_TEXCOORDS = 2048;
    public final static int MD2_MAX_FRAMES = 512;
    public final static int MD2_MAX_SKINS = 32;
    public final static int MD2_MAX_FRAMESIZE = (MD2_MAX_VERTICES * 4 + 128);
    public boolean isFirstNpc = true;
    ByteBuffer m_FilePointer;                                 // 文件指针 */
    MD2Header m_Header;			         // 文件头 */
    String[] m_pSkins;			         // 纹理数据指针 */
    MD2TexCoord[] m_pTexCoords;		         // 纹理坐标指针 */
    MD2Face[] m_pTriangles;		         // 三角形面指针 */
    MD2Frame[] m_pFrames;			         // 关键帧 */
    MD2Model m_3DModel;
    float elapsedTime = 0.0f;
    float lastTime = 0.0f;

    public MD2Model get3DModel() {
        return m_3DModel;
    }

    public MD2Loader(GLAutoDrawable glad, String strFileName, String strTexture) {
        this.strFileName = strFileName;
        this.strTexture = strTexture;
        this.glad = glad;
        this.gl = glad.getGL().getGL2();
        //初时化指针为空 */
        m_pSkins = null;
        m_pTexCoords = null;
        m_pTriangles = null;
        m_pFrames = null;
        init(strFileName, strTexture);

    }

    public void init(String strFileName, String strTexture) {

        //载入MD2文件
        m_3DModel = ImportMD2(m_3DModel, strFileName, strTexture);
        if (m_3DModel == null) {
            System.out.println("读取失败");
        }

        for (int i = 0; i < m_3DModel.numOfMaterials; i++) {
            if (m_3DModel.pMaterials.get(i).strFile.length() > 0) //判断是否是一个文件名 */
            {
                LoadTexture(m_3DModel.pMaterials.get(i).strFile, i);// 使用纹理文件名称来装入位图 */		 
            }
            //设置材质的纹理ID
            m_3DModel.pMaterials.get(i).texureId = i;
        }
        /*
        for (int i = 0; i < m_3DModel.numOfAnimations; i++) {
            System.out.println( m_3DModel.pAnimations.get(i).strName+","+m_3DModel.pAnimations.get(i).startFrame+","+m_3DModel.pAnimations.get(i).endFrame);
        }*/
    }

    MD2Model GetModel() {
        return m_3DModel;
    }

    MD2Model ImportMD2(MD2Model pModel, String strFileName, String strTexture) {
        try {
            File file = new File(strFileName);
            m_FilePointer = ByteLoad.getByteBuffer(new FileInputStream(file), (int) file.length());
        } catch (FileNotFoundException ex) {
            System.out.println("文件读取失败");
        } catch (IOException ex) {
            System.out.println("文件读取失败");
        }
        //读取版本号
        int[] a = new int[17];
        for (int i = 0; i < 17; i++) {
            a[i] = m_FilePointer.getInt();
        }
        m_Header = new MD2Header(a);
        //System.out.println(m_Header.toString());
        //检查版本号
        if (m_Header.version != 8) {
            System.out.println("版本号不为8");
            return null;
        }
        //读取MD2文件数据
        ReadMD2Data();
        pModel = ConvertDataStructures(pModel);
        if (strTexture != null) {
            MD2MaterialInfo texture = new MD2MaterialInfo();
            texture.strFile = strTexture;
            texture.texureId = 0;
            texture.uTile = texture.uTile = 1;
            pModel.numOfMaterials = 1;
            pModel.pMaterials.add(texture);
        }
        return pModel;
    }

    public String getString(int index) {
        StringBuffer stringBuffer = new StringBuffer();
        char charIn = (char) this.m_FilePointer.get(index);
        int i = 1;
        while (charIn != 0) {
            stringBuffer.append(charIn);
            charIn = (char) this.m_FilePointer.get(index + i);
            i++;
        }
        return stringBuffer.toString();
    }

    public MD2TexCoord getTMd2TexCoord(int index) {
        MD2TexCoord temp = new MD2TexCoord(m_FilePointer.getShort(index), m_FilePointer.getShort(index + 2));
        return temp;
    }

    public MD2AliasFrame getTMd2AliasFrame(int index, int frameSize) {
        MD2AliasFrame temp = new MD2AliasFrame();
        temp.scale[0] = this.m_FilePointer.getFloat(index);
        temp.scale[1] = this.m_FilePointer.getFloat(index + 4);
        temp.scale[2] = this.m_FilePointer.getFloat(index + 8);
        temp.translate[0] = this.m_FilePointer.getFloat(index + 12);
        temp.translate[1] = this.m_FilePointer.getFloat(index + 16);
        temp.translate[2] = this.m_FilePointer.getFloat(index + 20);
        //System.out.print(temp.scale[0]+","+temp.scale[1]+","+temp.scale[2]);
        //System.out.println(","+temp.translate[0]+","+temp.translate[1]+","+temp.translate[2]);
        temp.name = getString(index + 24);
        int f = frameSize - 40;
        temp.aliasVertices = new MD2AliasTriangle[f / 4];
        int flag = 40;
        for (int i = 0; i < f / 4; i++) {
            //System.out.println(f/4);
            temp.aliasVertices[i] = new MD2AliasTriangle(this.m_FilePointer.get(index + flag + i * 4), this.m_FilePointer.get(index + flag + i * 4 + 1), this.m_FilePointer.get(index + flag + i * 4 + 2), this.m_FilePointer.get(index + flag + i * 4 + 3));
            //System.out.println(temp.aliasVertices[i].vertex[0]+","+temp.aliasVertices[i].vertex[1]+","+temp.aliasVertices[i].vertex[2]+","+temp.aliasVertices[i].lightNormalIndex);
        }
        return temp;
    }

    public MD2Face getTMd2Face(int index) {
        MD2Face temp = new MD2Face();
        temp.vertexIndices[0] = this.m_FilePointer.getShort(index);
        temp.vertexIndices[1] = this.m_FilePointer.getShort(index + 2);
        temp.vertexIndices[2] = this.m_FilePointer.getShort(index + 4);
        temp.textureIndices[0] = this.m_FilePointer.getShort(index + 6);
        temp.textureIndices[1] = this.m_FilePointer.getShort(index + 8);
        temp.textureIndices[2] = this.m_FilePointer.getShort(index + 10);
        //System.out.println(temp.vertexIndices[0]+","+temp.vertexIndices[1]+","+temp.vertexIndices[2]+","+temp.textureIndices[0]+","+temp.textureIndices[1]+","+temp.textureIndices[2]);
        return temp;
    }

    void ReadMD2Data() {

        m_pSkins = new String[m_Header.numSkins];
        m_pTexCoords = new MD2TexCoord[m_Header.numTexCoords];
        m_pTriangles = new MD2Face[m_Header.numTriangles];
        m_pFrames = new MD2Frame[m_Header.numFrames];

        for (int i = 0; i < m_Header.numSkins; i++) {
            if (i != 0) {
                m_pSkins[i] = getString(m_Header.offsetSkins + m_pSkins[i - 1].length() * i);
            } else {
                m_pSkins[i] = getString(m_Header.offsetSkins);
            }
            //System.out.println(m_pSkins[i]);
        }

        for (int i = 0; i < m_Header.numTexCoords; i++) {
            m_pTexCoords[i] = getTMd2TexCoord(m_Header.offsetTexCoords + 4 * i);
        }

        // System.out.println(m_Header.offsetTriangles);
        for (int i = 0; i < m_Header.numTriangles; i++) {
            m_pTriangles[i] = getTMd2Face(m_Header.offsetTriangles + 12 * i);
        }

        for (int i = 0; i < m_Header.numFrames; i++) {
            m_pFrames[i] = new MD2Frame();
            MD2AliasFrame pFrame;

            m_pFrames[i].pVertices = new MD2Triangle[m_Header.numVertices];

            for (int j = 0; j < m_Header.numVertices; j++) {
                m_pFrames[i].pVertices[j] = new MD2Triangle();
            }
            // System.out.println( m_Header.frameSize);
            pFrame = getTMd2AliasFrame(m_Header.offsetFrames + i * m_Header.frameSize, m_Header.frameSize);

            m_pFrames[i].strName = pFrame.name;

            MD2Triangle[] pVertices = m_pFrames[i].pVertices;

            for (int j = 0; j < m_Header.numVertices; j++) {
                pVertices[j].vertex[0] = ((float) pFrame.aliasVertices[j].vertex[0] * pFrame.scale[0]) + pFrame.translate[0];
                pVertices[j].vertex[2] = -(((float) pFrame.aliasVertices[j].vertex[1] * pFrame.scale[1]) + pFrame.translate[1]);
                //System.out.println(pFrame.aliasVertices[j].vertex[1]+","+pFrame.scale[1]+","+pFrame.translate[1]+","+(-1 * (pFrame.aliasVertices[j].vertex[1] * pFrame.scale[1] + pFrame.translate[1])));
                pVertices[j].vertex[1] = ((float) pFrame.aliasVertices[j].vertex[2] * pFrame.scale[2]) + pFrame.translate[2];
                //System.out.println(pVertices[j].vertex[0]+","+ pVertices[j].vertex[1]+","+ pVertices[j].vertex[2]);
            }
        }
    }

    public void LoadTexture(GL2 gl) {
        if (isFirstNpc) {
            isFirstNpc = false;
            File file = new File(strTexture);
            for (int i = 0; i < m_3DModel.numOfMaterials; i++) {
                try {
                    TextureIO.newTexture(file, true).getTextureObject(gl);
                } catch (IOException ex) {
                    System.out.println(ex.getMessage() + "出错了" + "-LoadTexture");
                } catch (GLException ex) {
                    System.out.println(ex.getMessage() + "出错了" + "-LoadTexture");
                }
            }
            //gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
        }
    }

    void LoadTexture(String fileName, int textureID) {
        File file = new File(fileName);
        // System.out.println(fileName);
        try {
            m_3DModel.m_textures[textureID] = TextureIO.newTexture(file, true).getTextureObject(gl);
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + "出错了" + "-LoadTexture");
        } catch (GLException ex) {
            System.out.println(ex.getMessage() + "出错了" + "-LoadTexture");
        }
        //gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
    }

    public static String delNumber(String s) {
        char[] temp = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            if (temp[i] >= '0' && temp[i] <= '9') {
                return s.substring(0, i);
            }
        }
        return s;
    }

    void ParseAnimations(MD2Model pModel) {

        MD2AnimationInfo animation = new MD2AnimationInfo();;
        String strLastName = "";
        //** 循环处理 */
        for (int i = 0; i < pModel.numOfObjects; i++) {
            //** 保存帧名 */
            String strName = m_pFrames[i].strName;
            int frameNum = 0;
            char[] temp = strName.toCharArray();
            int found = 0;
            for (int k = 0; k < strName.length(); k++) {
                if (temp[k] >= '0' && temp[k] <= '9') {
                    found = 1;
                    for (int j = k; j < strName.length(); j++) {
                        if (temp[j] >= '0' && temp[j] <= '9') {
                            found = 1;
                        } else {
                            found = 0;
                            break;
                        }
                    }
                    if (found == 1) {
                        frameNum = Integer.parseInt(strName.substring(k));
                        strName = strName.substring(0, k);
                        break;
                    }
                }
            }
            //** 如果该帧名字和前一帧不同或者该帧为结束帧 */
            if (strName.equals(strLastName) == false || i == pModel.numOfObjects - 1) {
                //** 检查是否为开始帧 */
                if (strLastName != "") {
                    //** 复制帧名称 */
                    animation.strName = strLastName;

                    //** 设置动作结束帧序号为i */
                    animation.endFrame = i-1;

                    //*/* 将该帧添加到动作列表中 */
                    pModel.pAnimations.add(animation);
                    animation = new MD2AnimationInfo();

                    //** 动作数加一 */
                    pModel.numOfAnimations++;
                }

                //** 设置开始帧 */
                animation.startFrame = frameNum+ i;
                //System.out.println(animation.startFrame);
            }
            //** 保存帧的名字 */
            strLastName = strName;
        }
    }

    MD2Model ConvertDataStructures(MD2Model pModel) {
        int j = 0, i = 0;
        // 分配内存 */
        pModel = new MD2Model();
        // 设置对象数目为关键帧数目 */
        pModel.numOfObjects = m_Header.numFrames;
        // 产生动作信息 */
        ParseAnimations(pModel);
        // 将各帧顶点数据添加到模型对象链表中 */
        for (i = 0; i < pModel.numOfObjects; i++) {
            // 保存第一帧数据 */
            MD2Object currentFrame = new MD2Object();
            // 指定顶点 纹理坐标和面的个数 */
            currentFrame.numOfVerts = m_Header.numVertices;
            currentFrame.numTexVertex = m_Header.numTexCoords;
            currentFrame.numOfFaces = m_Header.numTriangles;
            // 分配内存 */
            currentFrame.pVerts = new LinkedList();
            for (int m = 0; m < currentFrame.numOfVerts; m++) {
                currentFrame.pVerts.add(new Vector3());
            }
            // 循环处理所有顶点 */
            for (j = 0; j < currentFrame.numOfVerts; j++) {
                currentFrame.pVerts.get(j).x = m_pFrames[i].pVertices[j].vertex[0];
                currentFrame.pVerts.get(j).y = m_pFrames[i].pVertices[j].vertex[1];
                currentFrame.pVerts.get(j).z = m_pFrames[i].pVertices[j].vertex[2];
                //System.out.println(m_pFrames[i].pVertices[j].vertex[0]+","+m_pFrames[i].pVertices[j].vertex[1]+","+m_pFrames[i].pVertices[j].vertex[2]);
            }
            // 检查是否超过第一帧 */
            if (i > 0) {
                // 将该帧添加到模型对象中 */
                pModel.pObject.add(currentFrame);
                continue;
            }
            currentFrame.pTexVerts = new LinkedList();
            for (int m = 0; m < currentFrame.numTexVertex; m++) {
                currentFrame.pTexVerts.add(new Vector2());
            }
            currentFrame.pFaces = new LinkedList();
            for (int m = 0; m < currentFrame.numOfFaces; m++) {
                currentFrame.pFaces.add(new MD2FaceIndex());
            }
            // 处理纹理坐标 */
            for (j = 0; j < currentFrame.numTexVertex; j++) {
                currentFrame.pTexVerts.get(j).x = m_pTexCoords[j].u / (float) (m_Header.skinWidth);
                currentFrame.pTexVerts.get(j).y = 1 - m_pTexCoords[j].v / (float) (m_Header.skinHeight);
            }
            // 保存面数据信息 */
            for (j = 0; j < currentFrame.numOfFaces; j++) {
                // 为面顶点坐标索引赋值 */
                currentFrame.pFaces.get(j).vertIndex[0] = m_pTriangles[j].vertexIndices[0];
                currentFrame.pFaces.get(j).vertIndex[1] = m_pTriangles[j].vertexIndices[1];
                currentFrame.pFaces.get(j).vertIndex[2] = m_pTriangles[j].vertexIndices[2];
                //System.out.println(currentFrame.pFaces.get(j).vertIndex[0]+","+currentFrame.pFaces.get(j).vertIndex[1]+","+currentFrame.pFaces.get(j).vertIndex[2]);
                // 为面顶点纹理坐标索引赋值 */
                currentFrame.pFaces.get(j).coordIndex[0] = m_pTriangles[j].textureIndices[0];
                currentFrame.pFaces.get(j).coordIndex[1] = m_pTriangles[j].textureIndices[1];
                currentFrame.pFaces.get(j).coordIndex[2] = m_pTriangles[j].textureIndices[2];
            }
            // 将该帧添加到模型中 */
            pModel.pObject.add(currentFrame);
        }
        return pModel;
    }

}
