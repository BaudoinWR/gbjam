package com.woobadeau.gbjam;

import com.badlogic.gdx.audio.Music;
import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.sound.SoundFactory;
import com.woobadeau.tinyengine.things.sprites.SpriteFactory;
import com.woobadeau.tinyengine.things.sprites.SpriteFontText;
import com.woobadeau.tinyengine.things.sprites.SpriteSheet;
import java.io.IOException;
import java.util.Random;

public class MainClass {
    public static final Random RANDOM = new Random();

    public static final SpriteSheet SPRITE_SHEET;
    public static final SpriteSheet OBJECT_SPRITE_SHEET;
    public static final SpriteSheet BIG_SPRITE_SHEET;
    public static final SpriteFontText SPRITE_FONT_TEXT;
    public static final Music MUSIC;

    static {
        SPRITE_FONT_TEXT = SpriteFactory.createSpriteFontText("gbfont.png");
        SPRITE_SHEET = SpriteFactory.createSpriteSheet("gbitems.png", 2, 3);
        OBJECT_SPRITE_SHEET = SpriteFactory.createSpriteSheet("gbobjects.png", 3, 3);
        BIG_SPRITE_SHEET = SpriteFactory.createSpriteSheet("gbbigstuff.png", 2, 2);
        MUSIC = SoundFactory.getMusic("music.ogg");
    }
    public static final int WIDTH = 160;
    public static final int HEIGHT = 144;

    public static void main(String... args) throws IOException {
        //TinyEngine.debug = true;
        new GameController();
        new TinyEngine(WIDTH, HEIGHT, 5, () -> {}).start();
    }
}
