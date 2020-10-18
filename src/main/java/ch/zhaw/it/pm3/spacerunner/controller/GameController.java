package ch.zhaw.it.pm3.spacerunner.controller;

import ch.zhaw.it.pm3.spacerunner.model.Game;

public class GameController {

    private boolean isRunning = false;
    private boolean isPaused = false;

    private Game game;
    private GameView gameView;

    public void setView(GameView gameView){
        this.gameView = gameView;
    }


    public void startGame() throws Exception {
        game = new Game();

        if(gameView == null){
            //TODO: Make own exception types and handle
            throw new Exception();
        }

        isRunning = true;
        while(isRunning && isPaused == false){

            //TODO: Get KeyEvent and
            boolean keypressed = false;
            if(keypressed){
                game.moveSpaceShip(null);
            }
            //TODO: Draw Elements to canvas
            gameView.drawSpaceElements(game.getDrawables());
            game.collisonDetector();

            game.generateObstacles();
            game.moveElements();
        }

        //TODO: Add collected coins to playerProfile and save it!

    }


    /**
     * continues or stops game logic according to clicking pause/resume button
     * */
    public void togglePause() {
        isPaused = !isPaused;
    }



}
