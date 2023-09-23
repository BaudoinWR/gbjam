package com.woobadeau.gbjam;

import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.library.Pixel;
import com.woobadeau.tinyengine.things.physics.Vector2D;
import com.woobadeau.tinyengine.things.sprites.Sprite;
import com.woobadeau.tinyengine.things.sprites.SpriteFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.woobadeau.gbjam.GBJam.HEIGHT;
import static com.woobadeau.gbjam.GBJam.OBJECT_SPRITE_SHEET;
import static com.woobadeau.gbjam.GBJam.RANDOM;
import static com.woobadeau.gbjam.GBJam.SPRITE_FONT_TEXT;
import static com.woobadeau.gbjam.GBJam.SPRITE_SHEET;
import static com.woobadeau.gbjam.GBJam.WIDTH;

public class PlayerUI extends Thing {
    static final Color LIGHT_GREEN = new Color(224, 248, 208);
    static final Color MID_GREEN = new Color(52, 104, 86);
    static final Color DARK_GREEN = new Color(8, 24, 32);
    private final Player player;
    private final Integer STARS = 100;
    private static final BufferedImage lifeOn = SPRITE_SHEET.getImage(3);
    private static final BufferedImage lifeOff = SPRITE_SHEET.getImage(4);

    public PlayerUI(Player player) throws IOException {
        this.player = player;


        generateBackground();
        setZIndex(11);
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);
        // Items in bag
        graphics.drawImage(SPRITE_FONT_TEXT.getText("" + player.getTrashBag().size(), 0), WIDTH - 19, 3, null);

        // Oxygen
        int oxygenXStart = WIDTH - 36;
        int oxygenYStart = 13;
        graphics.setColor(LIGHT_GREEN);
        graphics.fillRect(oxygenXStart, oxygenYStart, 31, 5);
        double ratioOxygen = player.getOxygen() / player.getMaxOxygen() * 10;
        for (int i = 0; i < ratioOxygen; i++) {
            graphics.setColor(DARK_GREEN);
            graphics.fillRect(oxygenXStart + 1 + i * 3, oxygenYStart + 1, 2, 3);
        }

        // Score
        graphics.drawImage(SPRITE_FONT_TEXT.getText("Score:" + player.getScore() + "+" +player.getTemporaryScore(), 1), 3, 3, null);
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
            @Override
            public void draw(Graphics graphics) {
                graphics.setColor(DARK_GREEN);
                graphics.fillRect((int) getPosition().x, (int) getPosition().y, WIDTH, HEIGHT);
            }
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
        for (int i = 0; i < 3; i++) {
            Sprite sprite = SpriteFactory.createSprite(OBJECT_SPRITE_SHEET.getImage(RANDOM.nextInt(5)));
            sprite.moveTo(new Vector2D(RANDOM.nextInt(WIDTH - 16), 11 + RANDOM.nextInt(HEIGHT - 27)));
            sprite.setZIndex(-1);
            getThings().add(sprite);
        }
        Sprite uiBar = SpriteFactory.createSprite("/gbuibar.png");
        uiBar.moveTo(new Vector2D(0, 0));
        uiBar.setZIndex(10);
        getThings().add(uiBar);
    }
}
