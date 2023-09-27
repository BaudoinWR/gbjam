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

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.physics.Vector2D;

import static com.woobadeau.gbjam.MainClass.SPRITE_FONT_TEXT;

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
    public void draw(SpriteBatch spriteBatch) {
        Sprite text = SPRITE_FONT_TEXT.getText(this.text, 1);
        //NestableFrameBuffer nestableFrameBuffer = TinyEngine.getFrameBuffer();
        //nestableFrameBuffer.begin();
        //ScreenUtils.clear(Color.CLEAR);
        //SpriteBatch spriteBatch = TinyEngine.getSpriteBatch();
        //spriteBatch.begin();
        spriteBatch.draw(text, (int) this.getPosition().x + 1, (int) this.getPosition().y + 1);
        //text.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
        //Sprite sprite = new Sprite(text);
        //sprite.flip(false, true);
        //sprite.setSize((float)TinyEngine.width, (float)TinyEngine.height);
        //text.setPosition(0.0F, 0.0F);
        //sprite.draw(spriteBatch);
        //TinyEngine.drawBufferToBatch(spriteBatch, nestableFrameBuffer);
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
