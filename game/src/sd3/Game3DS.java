package sd3;

//********************************************
import tool.Vector3;
import tool.Vector2;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

//*类名:Game3DS
//*作者:凌恋      时间:2016-8-4 9:51:29
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Game3DS {

    GLAutoDrawable glad;
    GL2 gl;
    ByteBuffer m_FilePointer;			  //文件					
    SD3Chunk m_CurrentChunk;             // 读取过程中当前块 
    SD3Chunk m_TempChunk;                //临时块
    int[] m_textures = new int[MAX_TEXTURES];    //纹理
    SD3Model m_3DModel;                   //模型
    public boolean isFirstNpc = true;
    public String textureFolder;
    public static final short MAX_TEXTURES = 100;
    //3ds基础块，文件开头，用来鉴别3ds文件
    public static final short PRIMARY = 0x4D4D;
    //主块
    public static final short OBJECTINFO = 0x3D3D;//网格对象的版本号
    public static final short VERSION = 0x0002;//.3ds文件的版本
    public static final short EDITKEYFRAME = (short) 0xB000;//所有关键帧信息的头部
    //对象的次级定义
    public static final short MATERIAL = (short) 0xAFFF;//纹理信息
    public static final short OBJECT = 0x4000;//对象的面、顶点等信息
    //材质的次级定义
    public static final short MATNAME = (short) 0xA000;//材质名称
    public static final short MATDIFFUSE = (short) 0xA020;//对象/材质的颜色
    public static final short MATMAP = (short) 0xA200;//新材质的头部
    public static final short MATMAPFILE = (short) 0xA300;//保存纹理的文件名
    public static final short OBJ_MESH = 0x4100;//新的网格对象
    //网格对象的次级定义
    public static final short OBJ_VERTICES = 0x4110;//对象顶点
    public static final short OBJ_FACES = 0x4120;//对象的面
    public static final short OBJ_MATERIAL = 0x4130;//对象的材质
    public static final short OBJ_UV = 0x4140;//对象的UV纹理坐标

    public Game3DS(GLAutoDrawable glad) {
        this.glad = glad;
        this.gl = glad.getGL().getGL2();
        m_CurrentChunk = new SD3Chunk();	 //为当前块分配空间
        m_TempChunk = new SD3Chunk();		 // 为临时块分配空间 
        m_3DModel = new SD3Model();
        m_3DModel.numOfObjects = 0;
        m_3DModel.numOfMaterials = 0;
        for (int i = 0; i < MAX_TEXTURES; i++) {
            m_textures[i] = 0;
        }
    }

    public void draw(GL2 gl) {

        int num = 0;
        int num2 = 0;
        int num3 = 0;

        gl.glPushMatrix();
        gl.glPushAttrib(GL2.GL_CURRENT_BIT);
        gl.glDisable(GL2.GL_TEXTURE_2D);
        // 遍历模型中所有的对象
        for (int i = 0; i < m_3DModel.numOfObjects; i++) {
            if (m_3DModel.pObject.size() <= 0) {
                break;                   // 如果对象的大小小于0，则退出 
            }
            SD3Object pObject = m_3DModel.pObject.get(i);// 获得当前显示的对象 

            if (pObject.hasTexTure) // 判断该对象是否有纹理映射 
            {
                gl.glEnable(GL2.GL_TEXTURE_2D);             // 打开纹理映射 
                gl.glBindTexture(GL2.GL_TEXTURE_2D, m_textures[pObject.materialID]);
            } else {
                gl.glDisable(GL2.GL_TEXTURE_2D);                // 关闭纹理映射
            }
            //byte b = (byte) 255;
            // gl.glColor3ub(b, b, b);
            gl.glBegin(GL2.GL_TRIANGLES);
            for (int j = 0; j < pObject.numOfFaces; j++) // 遍历所有的面 
            {
                for (int tex = 0; tex < 3; tex++) // 遍历三角形的所有点
                {
                    int index = pObject.pFaces[j].vertIndex[tex];	// 获得面对每个点的索引

                    gl.glNormal3f(pObject.pNormals.get(index).x, pObject.pNormals.get(index).y, pObject.pNormals.get(index).z);		// 给出法向量 

                    if (pObject.hasTexTure) //如果对象具有纹理 
                    {
                        if (pObject.pTexVerts.size() != 0) // 确定是否有UVW纹理坐标 
                        {
                            gl.glTexCoord2f(pObject.pTexVerts.get(index).x, pObject.pTexVerts.get(index).y);
                        }
                    } else {
                        if (m_3DModel.pMaterials.size() != 0 && pObject.materialID >= 0) {
                            byte[] pColor = m_3DModel.pMaterials.get(pObject.materialID).color;
                            gl.glColor3f(pColor[0], pColor[1], pColor[2]);
                        }
                    }
                    //gl.glNormal3f(pObject.pNormals.get(index).x,pObject.pNormals.get(index).y,pObject.pNormals.get(index).z);
                    gl.glVertex3f(pObject.pVerts.get(index).x, pObject.pVerts.get(index).y, pObject.pVerts.get(index).z);
                    // System.out.println(pObject.pVerts.get(index).x+","+ pObject.pVerts.get(index).y+","+pObject.pVerts.get(index).z);
                }
            }
            gl.glEnd();
        }
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glPopAttrib();   // 恢复前一属性 
        gl.glPopMatrix();
    }

    public void init(String fileName, String textureFolder) {
        this.textureFolder = textureFolder;
        //** 将3ds文件装入到模型结构体中 */
        Import3DS(m_3DModel, fileName);

        for (int i = 0; i < m_3DModel.numOfMaterials; i++) {
            if (m_3DModel.pMaterials.get(i).strFile.length() > 0) //< 判断是否是一个文件名 */
            {
                if (textureFolder != null) {
                    LoadTexture(textureFolder.concat("\\" + m_3DModel.pMaterials.get(i).strFile), i);//< 使用纹理文件名称来装入位图 */		 
                } else {
                    LoadTexture(textureFolder.concat("\\" + m_3DModel.pMaterials.get(i).strFile), i);//< 使用纹理文件名称来装入位图 */	
                }
            }
            //设置材质的纹理ID
            m_3DModel.pMaterials.get(i).textureId = i;
        }
    }

    public void LoadTexture(GL2 gl) {
        if (isFirstNpc) {
            for (int i = 0; i < m_3DModel.numOfMaterials; i++) {
                if (m_3DModel.pMaterials.get(i).strFile.length() > 0) //< 判断是否是一个文件名 */
                {
                    File file = new File(textureFolder.concat("\\" + m_3DModel.pMaterials.get(i).strFile));
                    //System.out.println(fileName);
                    try {
                        TextureIO.newTexture(file, true).getTextureObject(gl);
                    } catch (IOException ex) {
                        System.out.println(ex.getMessage() + "出错了" + "-LoadTexture");
                    } catch (GLException ex) {
                        System.out.println(ex.getMessage() + "出错了" + "-LoadTexture");
                    }
                }
            }
            //gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
            gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
        }
    }

    void LoadTexture(String fileName, int textureID) {
        File file = new File(fileName);
        System.out.println(fileName);
        try {
            m_textures[textureID] = TextureIO.newTexture(file, true).getTextureObject(gl);
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + "出错了" + "-LoadTexture");
        } catch (GLException ex) {
            System.out.println(ex.getMessage() + "出错了" + "-LoadTexture");
        }
        //gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR_MIPMAP_LINEAR);
    }

    boolean Import3DS(SD3Model pModel, String strFileName) {
        try {
            File file = new File(strFileName);
            m_FilePointer = getByteBuffer(Channels.newChannel(new FileInputStream(file)), (int) file.length());
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + "--Import3DS");
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + "--Import3DS");
        }
        if (m_FilePointer == null) {
            System.out.println("读取失败-没有文件");
            return false;
        }
        ReadChunk(m_CurrentChunk);
        if (m_CurrentChunk.id != PRIMARY) {
            System.out.println("读取失败-文件不对");
            return false;
        }
        ReadNextChunk(pModel, m_CurrentChunk);
        ComputeNormals(pModel);

        return true;
    }

    public ByteBuffer getByteBuffer(ReadableByteChannel channel, int channelSize)
            throws IOException {
        ByteBuffer chunkBuffer = ByteBuffer.allocate(channelSize);
        chunkBuffer.order(ByteOrder.LITTLE_ENDIAN);
        channel.read(chunkBuffer);
        chunkBuffer.position(0);
        return chunkBuffer;
    }

    void ReadChunk(SD3Chunk pChunk) {
        pChunk.bytesRead = 2;
        pChunk.bytesRead += 4;
        pChunk.id = m_FilePointer.getShort();
        pChunk.lenth = m_FilePointer.getInt();
    }

    void ReadNextChunk(SD3Model pModel, SD3Chunk pPreChunk) {

        // 用来添加到对象链表
        // 用来添加到材质链表
        int version = 0;

        //保存文件版本
        // 用来跳过不需要的数据
        m_CurrentChunk = new SD3Chunk();
        // 为新的块分配空间
        // 继续读入子块 
        while (pPreChunk.bytesRead < pPreChunk.lenth) {
            ReadChunk(m_CurrentChunk);
            switch (m_CurrentChunk.id) {
                case VERSION:
                    //读入文件的版本号
                    m_CurrentChunk.bytesRead += m_CurrentChunk.lenth - m_CurrentChunk.bytesRead;
                    version = m_FilePointer.getInt();
                    if (version > 0x03) {
                        System.out.println("该3DS文件版本大于3.0,可能不能正确读取");
                    }
                    break;
                case OBJECTINFO:
                    ReadChunk(m_TempChunk);
                    m_TempChunk.bytesRead += m_TempChunk.lenth - m_TempChunk.bytesRead;
                    version = this.m_FilePointer.getInt();
                    m_CurrentChunk.bytesRead += m_TempChunk.bytesRead;
                    ReadNextChunk(pModel, m_CurrentChunk);
                    break;

                case MATERIAL:
                    pModel.numOfMaterials++;
                    pModel.pMaterials.add(new SD3MatInfo());
                    ReadNextMatChunk(pModel, m_CurrentChunk);
                    break;
                case OBJECT:
                    pModel.numOfObjects++;
                    pModel.pObject.add(new SD3Object());
                    pModel.pObject.get(pModel.numOfObjects - 1).strName = GetString();
                    m_CurrentChunk.bytesRead += pModel.pObject.get(pModel.numOfObjects - 1).strName.length() + 1;
                    ReadNextObjChunk(pModel, pModel.pObject.get(pModel.numOfObjects - 1), m_CurrentChunk);
                    break;
                case EDITKEYFRAME:
                    m_CurrentChunk.bytesRead += skip(m_CurrentChunk.lenth - m_CurrentChunk.bytesRead);
                    break;
                default:
                    m_CurrentChunk.bytesRead += skip(m_CurrentChunk.lenth - m_CurrentChunk.bytesRead);

                    break;
            }
            pPreChunk.bytesRead += m_CurrentChunk.bytesRead;

        }
        m_CurrentChunk = pPreChunk;
    }

    public int skip(int lenth) {
        for (int i = 0; i < lenth; i++) {
            m_FilePointer.get();
        }
        return lenth;
    }

    public String GetString() {
        StringBuffer stringBuffer = new StringBuffer();
        char charIn = (char) this.m_FilePointer.get();
        while (charIn != 0) {
            stringBuffer.append(charIn);
            charIn = (char) this.m_FilePointer.get();
        }
        return stringBuffer.toString();
    }

    //读取下一个对象 
    void ReadNextObjChunk(SD3Model pModel, SD3Object pObject, SD3Chunk pPreChunk) {
        m_CurrentChunk = new SD3Chunk();
        while (pPreChunk.bytesRead < pPreChunk.lenth) {
            ReadChunk(m_CurrentChunk);
            switch (m_CurrentChunk.id) {
                case OBJ_MESH:
                    ReadNextObjChunk(pModel, pObject, m_CurrentChunk);
                    break;
                case OBJ_VERTICES:
                    ReadVertices(pObject, m_CurrentChunk);
                    break;
                case OBJ_FACES:
                    ReadVertexIndices(pObject, m_CurrentChunk);
                    break;
                case OBJ_MATERIAL:
                    ReadObjMat(pModel, pObject, m_CurrentChunk);
                    break;
                case OBJ_UV:
                    ReadUVCoordinates(pObject, m_CurrentChunk);
                    break;
                default:
                    m_CurrentChunk.bytesRead += skip(m_CurrentChunk.lenth - m_CurrentChunk.bytesRead);
                    break;
            }
            pPreChunk.bytesRead += m_CurrentChunk.bytesRead;
        }
        m_CurrentChunk = pPreChunk;
    }

    // 读取下一个材质块
    void ReadNextMatChunk(SD3Model pModel, SD3Chunk pPreChunk) {
        m_CurrentChunk = new SD3Chunk();
        String temp;
        int num;
        while (pPreChunk.bytesRead < pPreChunk.lenth) {
            ReadChunk(m_CurrentChunk);
            switch (m_CurrentChunk.id) {
                case MATNAME:
                    temp = GetString();
                    m_CurrentChunk.bytesRead += temp.length() + 1;
                    pModel.pMaterials.get(pModel.numOfMaterials - 1).strName = temp;
                    break;
                case MATDIFFUSE:
                    ReadColor(pModel.pMaterials.get(pModel.numOfMaterials - 1), m_CurrentChunk);
                    break;
                case MATMAP:
                    ReadNextMatChunk(pModel, m_CurrentChunk);
                    break;
                case MATMAPFILE:
                    temp = GetString();
                    m_CurrentChunk.bytesRead += temp.length() + 1;
                    pModel.pMaterials.get(pModel.numOfMaterials - 1).strFile = temp;
                    break;
                default:
                    m_CurrentChunk.bytesRead += skip(m_CurrentChunk.lenth - m_CurrentChunk.bytesRead);
                    break;
            }
            pPreChunk.bytesRead += m_CurrentChunk.bytesRead;
        }
        m_CurrentChunk = pPreChunk;
    }

    // 读取对象颜色的RGB值
    void ReadColor(SD3MatInfo pMaterial, SD3Chunk pChunk) {
        ReadChunk(m_TempChunk);
        byte[] b = new byte[m_TempChunk.lenth - m_TempChunk.bytesRead];
        m_TempChunk.bytesRead += m_TempChunk.lenth - m_TempChunk.bytesRead;
        m_FilePointer.get(b);
        pMaterial.color = b;
        pChunk.bytesRead += m_TempChunk.bytesRead;
    }

    // 读取对象的顶点信息 
    void ReadVertices(SD3Object pObject, SD3Chunk pPreChunk) {
        pObject.numOfVerts = m_FilePointer.getShort();
        pPreChunk.bytesRead += 2;
        pObject.pVerts = new LinkedList();
        pPreChunk.bytesRead += pPreChunk.lenth - pPreChunk.bytesRead;
        for (int j = 0; j < pObject.numOfVerts; j++) {
            pObject.pVerts.add(new Vector3(m_FilePointer.getFloat(), m_FilePointer.getFloat(), m_FilePointer.getFloat()));
        }

        for (int i = 0; i < pObject.numOfVerts; i++) {
            float fTempY = pObject.pVerts.get(i).y;
            pObject.pVerts.get(i).y = pObject.pVerts.get(i).z;
            pObject.pVerts.get(i).z = -fTempY;
        }
    }

    // 读取对象的面信息 
    void ReadVertexIndices(SD3Object pObject, SD3Chunk pPreChunk) {
        short index = 0;
        pObject.numOfFaces = m_FilePointer.getShort();
        pPreChunk.bytesRead += 2;
        pObject.pFaces = new SD3Face[pObject.numOfFaces];
        for (int i = 0; i < pObject.numOfFaces; i++) {
            pObject.pFaces[i] = new SD3Face();
        }
        for (int i = 0; i < pObject.numOfFaces; i++) {
            for (int j = 0; j < 4; j++) {
                index = m_FilePointer.getShort();
                pPreChunk.bytesRead += 2;
                if (j < 3) {
                    pObject.pFaces[i].vertIndex[j] = index;
                }
            }
        }
    }

    // 读取对象的纹理坐标 
    void ReadUVCoordinates(SD3Object pObject, SD3Chunk pPreChunk) {
        pObject.numTexVertex = m_FilePointer.getShort();
        pPreChunk.bytesRead += 2;
        pObject.pTexVerts = new LinkedList();

        pPreChunk.bytesRead += pPreChunk.lenth - pPreChunk.bytesRead;
        for (int i = 0; i < pObject.numTexVertex; i++) {
            pObject.pTexVerts.add(new Vector2(m_FilePointer.getFloat(), m_FilePointer.getFloat()));
        }

    }

    // 读入对象的材质名称
    void ReadObjMat(SD3Model pModel, SD3Object pObject, SD3Chunk pPreChunk) {
        String strMaterial;
        strMaterial = GetString();
        pPreChunk.bytesRead += strMaterial.length() + 1;
        for (int i = 0; i < pModel.numOfMaterials; i++) {
            if (strMaterial.equals(pModel.pMaterials.get(i).strName)) {
                pObject.materialID = i;
                if (pModel.pMaterials.get(i).strFile.length() > 0) {
                    pObject.hasTexTure = true;
                }
                break;
            } else {
                pObject.materialID = -1;
            }
        }
        pPreChunk.bytesRead += skip(pPreChunk.lenth - pPreChunk.bytesRead);
    }

    // 计算对象顶点的法向量 
    void ComputeNormals(SD3Model pModel) {
        Vector3 vVector1 = new Vector3();
        Vector3 vVector2 = new Vector3();
        Vector3 vNormal = new Vector3();
        Vector3[] vPoly = new Vector3[3];
        for (int i = 0; i < 3; i++) {
            vPoly[i] = new Vector3();
        }

        if (pModel.numOfObjects <= 0) {
            return;
        }
        for (int index = 0; index < pModel.numOfObjects; index++) {
            SD3Object pObject = pModel.pObject.get(index);
            Vector3[] pNormals = new Vector3[pObject.numOfFaces];
            Vector3[] pTempNormals = new Vector3[pObject.numOfFaces];
            pObject.pNormals = new LinkedList();
            for (int i = 0; i < pObject.numOfFaces; i++) {
                pNormals[i] = new Vector3();
                pTempNormals[i] = new Vector3();
            }
            for (int i = 0; i < pObject.numOfVerts; i++) {
                pObject.pNormals.add(new Vector3());
            }
            for (int i = 0; i < pObject.numOfFaces; i++) {
                vPoly[0] = pObject.pVerts.get(pObject.pFaces[i].vertIndex[0]);
                vPoly[1] = pObject.pVerts.get(pObject.pFaces[i].vertIndex[1]);
                vPoly[2] = pObject.pVerts.get(pObject.pFaces[i].vertIndex[2]);
                vVector1 = vPoly[0].cut(vPoly[2]);
                vVector2 = vPoly[2].cut(vPoly[1]);
                vNormal = vVector1.crossProduct(vVector2);
                pTempNormals[i] = vNormal;
                vNormal = vNormal.normalize();
                pNormals[i] = vNormal;
            }
            Vector3 vSum = new Vector3(0.0f, 0.0f, 0.0f);
            Vector3 vZero = vSum;
            int shared = 0;
            for (int i = 0; i < pObject.numOfVerts; i++) {
                for (int j = 0; j < pObject.numOfFaces; j++) {
                    if (pObject.pFaces[j].vertIndex[0] == i || pObject.pFaces[j].vertIndex[1] == i || pObject.pFaces[j].vertIndex[2] == i) {
                        vSum = vSum.add(pTempNormals[j]);
                        shared++;
                    }
                }
                pObject.pNormals.set(i, vSum.divide((float) -shared));
                pObject.pNormals.set(i, pObject.pNormals.get(i).normalize());
                vSum = vZero;
                shared = 0;
            }
        }
    }

}
