package com.woobadeau.gbjam;

import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.sprites.SpriteFactory;
import com.woobadeau.tinyengine.things.sprites.SpriteFontText;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.woobadeau.gbjam.GBJam.SPRITE_SHEET;

public class PlayerUI extends Thing {
    private final Player player;
    private final SpriteFontText spriteFontText;
    private static final BufferedImage lifeOn = SPRITE_SHEET.getImage(3);
    private static final BufferedImage lifeOff = SPRITE_SHEET.getImage(4);

    public PlayerUI(Player player) throws IOException {
        this.player = player;
        spriteFontText = SpriteFactory.createSpriteFontText("/gbfont.png");
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);
        graphics.drawImage(spriteFontText.getText("" + player.getTrashBag().size(), 0), GBJam.WIDTH - 19, 3, null);
//        for (int i = 0; i < player.getMaxLives(); i++) {
//             if (player.getLives() >= i) {
//                graphics.drawImage(lifeOn, 3 + i * 10, 1, null);
//            } else {
//                graphics.drawImage(lifeOff, 3 + i * 10, 1, null);
//            }
//        }
    }
}
