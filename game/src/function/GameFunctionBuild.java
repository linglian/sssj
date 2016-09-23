/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package function;

import game.GameManage;

/**
 *
 * @author HP
 */
public class GameFunctionBuild implements GameFunction {

    int id;
    GameManage gameManage;

    public GameFunctionBuild(GameManage gameManage, int id) {
        this.gameManage = gameManage;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public void function() {
        gameManage.getUser().setIsBuild(true, id);
    }

}
