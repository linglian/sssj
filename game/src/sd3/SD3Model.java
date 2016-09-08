package sd3;

//********************************************

import java.util.LinkedList;

//*类名:t3DModel
//*作者:凌恋      时间:2016-8-13 16:25:48
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class SD3Model {

  public   int numOfObjects;//模型中对象的数目
  public  int numOfMaterials;//模型中材质的数目
  public   LinkedList<SD3MatInfo> pMaterials;//材质链表信息
   public  LinkedList<SD3Object> pObject;//模型中对象链表的信息

    public SD3Model() {
        pMaterials = new LinkedList();
        pObject = new LinkedList();
    }
}
