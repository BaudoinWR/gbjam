package com.woobadeau.gbjam;

import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.behavior.ContainedBehavior;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.library.Pixel;
import com.woobadeau.tinyengine.things.physics.Vector2D;
import com.woobadeau.tinyengine.things.sprites.Sprite;
import com.woobadeau.tinyengine.things.sprites.SpriteFactory;
import com.woobadeau.tinyengine.things.sprites.SpriteSheet;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.SecureRandom;

public class GBJam {
    public static final SecureRandom RANDOM = new SecureRandom();

    private static final int STARS = 100;
    public static final SpriteSheet SPRITE_SHEET;
    public static final SpriteSheet OBJECT_SPRITE_SHEET;
    public static final int WIDTH = 160;
    public static final int HEIGHT = 144;

    static {
        try {
            SPRITE_SHEET = SpriteFactory.createSpriteSheet("/gbitems.png", 2, 3);
            OBJECT_SPRITE_SHEET = SpriteFactory.createSpriteSheet("/gbobjects.png", 3, 3);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) throws IOException {
        new TinyEngine(WIDTH, HEIGHT, 5, () -> {}).start();
        TinyEngine.debug = true;
        Thing background = new Thing() {
            @Override
            public void draw(Graphics graphics) {
                graphics.setColor(new Color(8, 24, 32));
                graphics.fillRect((int) getPosition().x, (int) getPosition().y, WIDTH, HEIGHT);
            }
        };
        BufferedImage text = SpriteFactory.createSpriteFontText("/gbfont.png").getText("GBJam 2021", 1);
        SpriteFactory.createSprite(text).moveTo(new Vector2D(10, 50));
        background.moveTo(new Vector2D(0, 0));
        background.setZIndex(-2);
        for (int i = 0; i < STARS; i++) {
            new Pixel(RANDOM.nextInt(WIDTH), 11 + RANDOM.nextInt(HEIGHT - 11), new Color(224,248,208)).setZIndex(-2);
        }
        for (int i = 0; i < 3; i++) {
            Sprite sprite = SpriteFactory.createSprite(OBJECT_SPRITE_SHEET.getImage(RANDOM.nextInt(5)));
            sprite.moveTo(new Vector2D(RANDOM.nextInt(WIDTH - 16), 11 + RANDOM.nextInt(HEIGHT - 27)));
            sprite.setZIndex(-1);
        }
        Player player = new Player();
        player.moveTo(new Vector2D(80, 72));
        new PlayerUI(player);
        new TrashSpawner();
        Sprite uiBar = SpriteFactory.createSprite("/gbuibar.png");
        uiBar.moveTo(new Vector2D(0, 0));
        Sprite ship = SpriteFactory.createSprite("/gbship.png");
        ship.moveTo(new Vector2D(2, HEIGHT - 25));
        ship.getBehaviors().add(new ContainedBehavior(2, HEIGHT - 20, 5, HEIGHT - 30));
        ship.getBehaviors().add(new WobbleBehavior());
    }
}
