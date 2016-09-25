/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package npc;

import npc.Build;
import game.GameManage;
import game.GameMap;
import game.GameThread;
import skill.GreenHP;

/**
 *
 * @author HP
 */
public class Flame extends Build {

    int view = 2;
    int time;

    public Flame(int id, int lvl, int x, int y, float high, float width, float face, int team) {
        super(id, lvl, x, y, high, width, face, team);
    }

    @Override
    public void run() {
        super.run();
        for (int i = (int)x - view; i <= (int)x + view; i++) {
            for (int j = (int)y - view; j <= (int)y + view; j++) {
                if (i < 0 || j < 0 || i >= GameMap.width || j >= GameMap.height) {
                    continue;
                }
                //回血
                if (GameThread.mainNpc[i][j] != null && GameThread.mainNpc[i][j].getTeam() == team) {
                    GameThread.mainNpc[i][j].setHp(GameThread.mainNpc[i][j].getHp() * 0.025 + GameThread.mainNpc[i][j].getHp());
                    time++;
                    if (time >= 10) {
                        GreenHP temp = new GreenHP(GameThread.mainNpc[i][j].getX(), GameThread.mainNpc[i][j].getY(), GameThread.mainNpc[i][j].getHigh(), 1);
                        temp.start();
                        GameManage.skill.add(temp);
                        time = 0;
                    }
                }
                //照明
                GameMap.isLooked[team][i][j] = true;
                GameMap.isLooking[team][i][j] = GameThread.gameTime + 10;
            }
        }
    }
}
