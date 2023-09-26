package com.woobadeau.gbjam;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.library.Pixel;
import com.woobadeau.tinyengine.things.physics.Vector2D;
import com.woobadeau.tinyengine.things.sprites.Sprite;
import com.woobadeau.tinyengine.things.sprites.SpriteFactory;
import java.awt.*;
import java.io.IOException;
import java.util.Random;

import static com.woobadeau.gbjam.MainClass.HEIGHT;
import static com.woobadeau.gbjam.MainClass.RANDOM;
import static com.woobadeau.gbjam.MainClass.SPRITE_FONT_TEXT;
import static com.woobadeau.gbjam.MainClass.SPRITE_SHEET;
import static com.woobadeau.gbjam.MainClass.WIDTH;

public class PlayerUI extends Thing {
    //static final Color LIGHT_GREEN = new Color(224, 248, 208);
    static final int LIGHT_GREEN = 0xE0F8D0FF;
    static final Color MID_GREEN = new Color(52, 104, 86);
    static final Color DARK_GREEN = new Color(8, 24, 32);
    private final Player player;
    private final Integer STARS = 100;
    //private static final Texture lifeOn = SPRITE_SHEET.getImage(3);
    //private static final Texture lifeOff = SPRITE_SHEET.getImage(4);

    public PlayerUI(Player player) throws IOException {
        this.player = player;

        generateBackground();
        setZIndex(11);
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        //super.draw(graphics);
        //// Items in bag
        com.badlogic.gdx.graphics.g2d.Sprite text = SPRITE_FONT_TEXT.getText("" + player.getTrashBag().size(), 0);
        spriteBatch.draw(text, WIDTH - 19, HEIGHT - 8);
        //graphics.drawImage(SPRITE_FONT_TEXT.getText("" + player.getTrashBag().size(), 0), WIDTH - 19, 3, null);
//
        //// Oxygen
        int oxygenXStart = WIDTH - 36;
        int oxygenYStart = HEIGHT - 16;
        //graphics.setColor(LIGHT_GREEN);
        //graphics.fillRect(oxygenXStart, oxygenYStart, 31, 5);
        double ratioOxygen = player.getOxygen() / player.getMaxOxygen() * 10;
        //for (int i = 0; i < ratioOxygen; i++) {
        //    graphics.setColor(DARK_GREEN);
        //    graphics.fillRect(oxygenXStart + 1 + i * 3, oxygenYStart + 1, 2, 3);
        //}
        Pixmap pixmap = new Pixmap(31, 5, Pixmap.Format.RGB888);
        pixmap.setColor(LIGHT_GREEN);
        pixmap.fill();
        pixmap.setColor(com.badlogic.gdx.graphics.Color.RED);
        for (int i = 0; i < ratioOxygen; i++) {
                //graphics.setColor(DARK_GREEN);
            pixmap.drawRectangle(1 + i * 3, 1, 2, 3);
        }
        Texture pixmaptex = new Texture(pixmap);
        spriteBatch.draw(pixmaptex, (float)oxygenXStart, (float)oxygenYStart);
        pixmap.dispose();

//
        //// Score
        text = SPRITE_FONT_TEXT.getText("Score:" + player.getScore() + "+" +player.getTemporaryScore(), 1);
        spriteBatch.draw(text, 3, HEIGHT - 8);

//        for (int i = 0; i < player.getMaxLives(); i++) {
//             if (player.getLives() >= i) {
//                graphics.drawImage(lifeOn, 3 + i * 10, 1, null);
//            } else {
//                graphics.drawImage(lifeOff, 3 + i * 10, 1, null);
//            }
//        }
    }

    private void generateBackground() throws IOException {
        Thing background = new Thing() {
            //@Override
                    //public void draw() {
                //    graphics.setColor(DARK_GREEN);
                //    graphics.fillRect((int) getPosition().x, (int) getPosition().y, WIDTH, HEIGHT);
                //}
        };
        //BufferedImage text = SpriteFactory.createSpriteFontText("/gbfont.png").getText("GBJam 2021", 1);
        //SpriteFactory.createSprite(text).moveTo(new Vector2D(10, 50));
        background.moveTo(new Vector2D(0, 0));
        background.setZIndex(-2);
        getThings().add(background);
        for (int i = 0; i < STARS; i++) {
            Pixel star = new Pixel(RANDOM.nextInt(WIDTH), 11 + RANDOM.nextInt(HEIGHT - 11), LIGHT_GREEN);
            star.setZIndex(-2);
            getThings().add(star);
        }
        //for (int i = 0; i < 3; i++) {
        //    Sprite sprite = SpriteFactory.createSprite(OBJECT_SPRITE_SHEET.getImage(RANDOM.nextInt(5)));
        //    sprite.moveTo(new Vector2D(RANDOM.nextInt(WIDTH - 16), 11 + RANDOM.nextInt(HEIGHT - 27)));
        //    sprite.setZIndex(-1);
        //    getThings().add(sprite);
        //}
        Sprite uiBar = SpriteFactory.createSprite("gbuibar.png");
        uiBar.moveTo(new Vector2D(0, TinyEngine.height - uiBar.getImage().getHeight()));
        uiBar.setZIndex(10);
        getThings().add(uiBar);
    }
}
