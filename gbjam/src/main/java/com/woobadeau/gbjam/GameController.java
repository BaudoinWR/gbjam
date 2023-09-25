package com.woobadeau.gbjam;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.physics.Vector2D;
import java.io.IOException;
import javax.sound.sampled.Clip;

import static com.woobadeau.gbjam.MainClass.HEIGHT;
import static com.woobadeau.gbjam.MainClass.MUSIC;
import static com.woobadeau.gbjam.MainClass.WIDTH;

public class GameController extends Thing {

    private Player player;
    private PlayerUI playerUI;
    private TrashSpawner trashSpawner;
    private Ship ship;
    private boolean closing;
    private long closingTicks;
    private AsteroidSpawner asteroidSpawner;

    public GameController() throws IOException {
        setup();
        setZIndex(Integer.MAX_VALUE);
        //TinyEngine.addKeyBinding("A", () -> {
        //    if (isGameOver()) {
        //        reset();
        //        try {
        //            setup();
        //        } catch (IOException e) {
        //            throw new RuntimeException(e);
        //        }
        //    }
        //});
    }

    @Override
    public void update() {
        if (player != null && player.getOxygen() <= 0 && !closing) {
            player.setActive(false);
            playerUI.setActive(false);
            trashSpawner.setActive(false);
            ship.setActive(false);
            asteroidSpawner.setActive(false);
            closing = true;
        } else if (closing) {
            closingTicks+=2;
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        if (closing) {
            //BufferedImage closingScreen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            //Graphics closerGraphics = closingScreen.getGraphics();
            //closerGraphics.setColor(DARK_GREEN);
            //closerGraphics.fillRect(0, 0, WIDTH, (int) closingTicks);
            //closerGraphics.fillRect(0, 0, (int) closingTicks, HEIGHT);
            //closerGraphics.fillRect((int) (WIDTH - closingTicks), 0, WIDTH, HEIGHT);
            //closerGraphics.fillRect(0, (int) (HEIGHT - closingTicks), WIDTH, HEIGHT);
            //graphics.drawImage(closingScreen, 0, 0, null);
            if (isGameOver()) {
                //Texture gameOver = SPRITE_FONT_TEXT.getText("GAME OVER", 1);
                //Texture score = SPRITE_FONT_TEXT.getText("SCORE:" + player.getScore(), 1);
                //Texture pressA = SPRITE_FONT_TEXT.getText("PRESS A TO RESTART", 1);
                //graphics.drawImage(gameOver, (WIDTH - gameOver.getWidth()) / 2, (HEIGHT - gameOver.getHeight()) / 2 - 15, null);
                //graphics.drawImage(score, (WIDTH - score.getWidth()) / 2, (HEIGHT - score.getHeight()) / 2, null);
                //graphics.drawImage(pressA, (WIDTH - pressA.getWidth()) / 2, (HEIGHT - pressA.getHeight()) / 2 + 15, null);
            }
        }
    }

    private boolean isGameOver() {
        return closingTicks > Math.max(WIDTH / 2, HEIGHT / 2);
    }

    private void setup() throws IOException {
        TinyEngine.resetTicks();
        player = new Player();
        player.moveTo(new Vector2D(80, 72));
        playerUI = new PlayerUI(player);
        trashSpawner = new TrashSpawner();
        ship = new Ship();
        closing = false;
        closingTicks = 0;
        asteroidSpawner = new AsteroidSpawner();
        MUSIC.loop(Clip.LOOP_CONTINUOUSLY);
    }

    void reset() {
        MUSIC.stop();
        player.destroy();
        playerUI.destroy();
        trashSpawner.destroy();
        ship.destroy();
        asteroidSpawner.destroy();
    }
}
