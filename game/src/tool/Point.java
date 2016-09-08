package tool;

//********************************************
import game.GameMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Stack;

//*类名:Point
//*作者:凌恋      时间:2016-8-16 14:52:17
//* 介绍:
//* 
//* 
//* 
//* 
//********************************************
public class Point implements Comparable<Point> {

    float x;
    float y;
    float high;
    Point parent;
    int f;
    int g;
    int h;

    public Point(float x, float y) {
        this.x = x;
        this.y = y;
        this.f = 0;
        this.g = 0;
        this.h = 0;
    }

    public Point(float x, float y, float high) {
        this.x = x;
        this.high = high;
        this.y = y;
        this.f = 0;
        this.g = 0;
        this.h = 0;
    }

    public static LinkedList<Point> printPath(Point start, Point end, float high, int time) {
        int[] dx = {0, 0, 1, -1, 1, -1, -1, 1};
        int[] dy = {1, -1, 0, 0, 1, 1, -1, -1};
        int pay = 0;
        int n = 0;
        start.high = high;
        if (Point.getDis(start, end) <= 10) {
            return null;
        }
        ArrayList<Point> openTable = new ArrayList<>();
        ArrayList<Point> closeTable = new ArrayList<>();
        openTable.clear();
        closeTable.clear();
        LinkedList<Point> pathStack = new LinkedList<>();
        start.parent = null;

        Point currentPoint = new Point(start.x, start.y, start.high);
        boolean flag = true;

        while (flag) {
            for (int i = 0; i < 8; i++) {
                float fx = currentPoint.x + dx[i];
                float fy = currentPoint.y + dy[i];
                Point tempPoint = new Point(fx, fy, currentPoint.high);
                //这边是终止条件，其他地方不用太大改动
                if (fx < 0 || fy < 0 || fx >= GameMap.width || fy >= GameMap.height || !GameMap.isCanMoved((int) (fx + 0.5f), (int) (fy + 0.5f), tempPoint.high)) {
                    continue;
                } else {
                    float ht = GameMap.getHigh((int) (fx + 0.5f), (int) (fy + 0.5f));
                    if (tempPoint.high != ht) {
                        tempPoint.high = ht;
                    }
                    if (end.equals(tempPoint)) {
                        flag = false;
                        end.parent = currentPoint;
                    }
                    if (i < 4) {
                        tempPoint.g = currentPoint.g + 10;
                    } else {
                        tempPoint.g = currentPoint.g + 15;
                    }
                    tempPoint.h = Point.getDis(tempPoint, end);
                    tempPoint.f = tempPoint.h + tempPoint.g;
                    if (ht > high) {
                        ht = GameMap.getHigh((int) (fx + 0.5f), (int) (fy + 0.5f));
                    }
                    if (openTable.contains(tempPoint)) {
                        int pos = openTable.indexOf(tempPoint);
                        Point temp = openTable.get(pos);
                        if (temp.f > tempPoint.f) {
                            openTable.remove(pos);
                            openTable.add(tempPoint);
                            tempPoint.parent = currentPoint;
                        }
                    } else if (closeTable.contains(tempPoint)) {
                        int pos = closeTable.indexOf(tempPoint);
                        Point temp = closeTable.get(pos);
                        if (temp.f > tempPoint.f) {
                            closeTable.remove(pos);
                            openTable.add(tempPoint);
                            tempPoint.parent = currentPoint;
                        }
                    } else {
                        openTable.add(tempPoint);
                        tempPoint.parent = currentPoint;
                    }
                }
            }
            pay++;
            if (pay >= time) {
                return null;
            }
            if (flag == false) {
                break;
            }
            openTable.remove(currentPoint);
            closeTable.add(currentPoint);
            Collections.sort(openTable);
            if (openTable.isEmpty()) {
                return null;
            }
            currentPoint = openTable.get(0);
        }
        Point node = end;
        while (node.parent != null) {
            pathStack.push(node);
            node = node.parent;
        }
        return pathStack;
    }

    public static int getDis(Point p1, Point p2) {
        int n = 10;
        int dis = (int) Math.abs(p1.x - p2.x) * n + (int) Math.abs(p1.y - p2.y) * n;
        return dis;
    }

    @Override
    public boolean equals(Object obj) {
        Point point = (Point) obj;
        if ((int)(point.x+0.5f) == (int)(this.x+0.5f) &&  (int)(point.y+0.5f) ==  (int)(this.y+0.5f)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.x + "," + this.y;
    }

    @Override
    public int compareTo(Point o) {
        return this.f - o.f;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public static void sPath(LinkedList<Point> p) {
        for (int i = 0; i < p.size(); i++) {
            System.out.println("第" + (i + 1) + "步" + p.get(i).getX() + "," + p.get(i).getY());
        }
    }
}
