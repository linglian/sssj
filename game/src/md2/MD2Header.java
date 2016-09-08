package md2;

//********************************************
//*类名:tMd2Header
//*作者:凌恋      时间:2016-8-8 15:05:35
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class MD2Header {

    public int magic;					// 文件标志 */
    public int version;					// 文件版本号 */
    public int skinWidth;				// 纹理宽度 */
    public int skinHeight;				// 纹理高度 */
    public int frameSize;				// 每一帧的字节数 */
    public int numSkins;				// 纹理数目 */
    public int numVertices;				// 顶点数目(每一帧中) */
    public int numTexCoords;			// 纹理坐标数目 */
    public int numTriangles;			// 三角行数目 */
    public int numGlCommands;			// gl命令数目 */
    public int numFrames;				// 总帧数 */
    public int offsetSkins;				// 纹理的偏移位置 */
    public int offsetTexCoords;			// 纹理坐标的偏移位置 */
    public int offsetTriangles;			// 三角形索引的偏移位置 */
    public int offsetFrames;			// 第一帧的偏移位置 */
    public int offsetGlCommands;		// OPenGL命令的偏移位置 */
    public int offsetEnd;				// 文件结尾偏移位置 */

    public MD2Header(int[] a) {
        this.magic = a[0];
        this.version = a[1];
        this.skinWidth = a[2];
        this.skinHeight = a[3];
        this.frameSize = a[4];
        this.numSkins = a[5];
        this.numVertices = a[6];
        this.numTexCoords = a[7];
        this.numTriangles = a[8];
        this.numGlCommands = a[9];
        this.numFrames = a[10];
        this.offsetSkins = a[11];
        this.offsetTexCoords = a[12];
        this.offsetTriangles = a[13];
        this.offsetFrames = a[14];
        this.offsetGlCommands = a[15];
        this.offsetEnd = a[16];
    }

    @Override
    public String toString() {
        return "tMd2Header{" + "magic=" + magic + ", version=" + version + ", skinWidth=" + skinWidth + ", skinHeight=" + skinHeight + ", frameSize=" + frameSize + ", numSkins=" + numSkins + ", numVertices=" + numVertices + ", numTexCoords=" + numTexCoords + ", numTriangles=" + numTriangles + ", numGlCommands=" + numGlCommands + ", numFrames=" + numFrames + ", offsetSkins=" + offsetSkins + ", offsetTexCoords=" + offsetTexCoords + ", offsetTriangles=" + offsetTriangles + ", offsetFrames=" + offsetFrames + ", offsetGlCommands=" + offsetGlCommands + ", offsetEnd=" + offsetEnd + '}';
    }

}
