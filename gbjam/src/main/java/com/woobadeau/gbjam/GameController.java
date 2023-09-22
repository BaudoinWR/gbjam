package com.woobadeau.gbjam;

import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.physics.Vector2D;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.woobadeau.gbjam.GBJam.HEIGHT;
import static com.woobadeau.gbjam.GBJam.WIDTH;
import static com.woobadeau.gbjam.PlayerUI.DARK_GREEN;

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
        TinyEngine.addKeyBinding("A", () -> {
            if (isGameOver()) {
                reset();
                try {
                    setup();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
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
    public void draw(Graphics graphics) {
        if (closing) {
            BufferedImage closingScreen = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
            Graphics closerGraphics = closingScreen.getGraphics();
            closerGraphics.setColor(DARK_GREEN);
            closerGraphics.fillRect(0, 0, WIDTH, (int) closingTicks);
            closerGraphics.fillRect(0, 0, (int) closingTicks, HEIGHT);
            closerGraphics.fillRect((int) (WIDTH - closingTicks), 0, WIDTH, HEIGHT);
            closerGraphics.fillRect(0, (int) (HEIGHT - closingTicks), WIDTH, HEIGHT);
            graphics.drawImage(closingScreen, 0, 0, null);
            if (isGameOver()) {
                BufferedImage gameOver = playerUI.spriteFontText.getText("GAME OVER", 1);
                BufferedImage pressA = playerUI.spriteFontText.getText("PRESS A TO RESTART", 1);
                graphics.drawImage(gameOver, (WIDTH - gameOver.getWidth()) / 2, (HEIGHT - gameOver.getHeight()) / 2 - 10, null);
                graphics.drawImage(pressA, (WIDTH - pressA.getWidth()) / 2, (HEIGHT - pressA.getHeight()) / 2 + 10, null);
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
    }

    void reset() {
        player.destroy();
        playerUI.destroy();
        trashSpawner.destroy();
        ship.destroy();
        asteroidSpawner.destroy();
    }
}
