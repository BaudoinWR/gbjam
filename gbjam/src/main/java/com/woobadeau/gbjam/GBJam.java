package com.woobadeau.gbjam;

import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.things.physics.Vector2D;
import com.woobadeau.tinyengine.things.sprites.SpriteFactory;
import com.woobadeau.tinyengine.things.sprites.SpriteSheet;
import java.io.IOException;
import java.security.SecureRandom;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class GBJam {
    public static final SecureRandom RANDOM = new SecureRandom();

    public static final SpriteSheet SPRITE_SHEET;
    public static final SpriteSheet OBJECT_SPRITE_SHEET;
    public static final SpriteSheet BIG_SPRITE_SHEET;
    public static final int WIDTH = 160;
    public static final int HEIGHT = 144;

    static {
        try {
            SPRITE_SHEET = SpriteFactory.createSpriteSheet("/gbitems.png", 2, 3);
            OBJECT_SPRITE_SHEET = SpriteFactory.createSpriteSheet("/gbobjects.png", 3, 3);
            BIG_SPRITE_SHEET = SpriteFactory.createSpriteSheet("/gbbigstuff.png", 2, 2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String... args) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        new TinyEngine(WIDTH, HEIGHT, 5, () -> {}).start();
        TinyEngine.debug = true;
        new GameController();
    }
}
