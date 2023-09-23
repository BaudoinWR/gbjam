/*
 * ADOBE CONFIDENTIAL
 * ___________________
 *
 * Copyright 2023 Adobe
 * All Rights Reserved.
 *
 * NOTICE: All information contained herein is, and remains
 * the property of Adobe and its suppliers, if any. The intellectual
 * and technical concepts contained herein are proprietary to Adobe
 * and its suppliers and are protected by all applicable intellectual
 * property laws, including trade secret and copyright laws.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe.
 */
package com.woobadeau.gbjam;

import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.physics.Vector2D;
import java.awt.*;
import java.awt.image.BufferedImage;

import static com.woobadeau.gbjam.GBJam.SPRITE_FONT_TEXT;

public class Popup extends Thing {
    private final static int speed = 1;
    private final String text;
    private final int lifeSpan;
    private int alive = 0;

    public Popup(String text, Vector2D position, int lifeSpan) {
        this.text = text;
        this.lifeSpan = lifeSpan;
        this.moveTo(position);
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);
        //graphics.setColor(PlayerUI.MID_GREEN);
        //graphics.fillRoundRect((int) this.getPosition().x, (int) this.getPosition().y, 2 + text.length() * 4, 7, 2, 0);
        BufferedImage textImage = SPRITE_FONT_TEXT.getText(text, 1);
        graphics.drawImage(textImage, (int) this.getPosition().x + 1, (int) this.getPosition().y + 1, null);
    }

    @Override
    public void update() {
        super.update();
        this.move(new Vector2D(0, -speed));
        if (alive++ > lifeSpan) {
            this.destroy();
        }
    }
}
