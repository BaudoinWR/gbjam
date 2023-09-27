package com.woobadeau.gbjam;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.behavior.DestroyOutOfScreenBehavior;
import com.woobadeau.tinyengine.sound.SoundFactory;
import com.woobadeau.tinyengine.things.Spawner;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.physics.Collider;
import com.woobadeau.tinyengine.things.physics.Vector2D;

import static com.woobadeau.gbjam.MainClass.BIG_SPRITE_SHEET;
import static com.woobadeau.gbjam.MainClass.HEIGHT;
import static com.woobadeau.gbjam.MainClass.RANDOM;
import static com.woobadeau.gbjam.MainClass.SPRITE_SHEET;
import static com.woobadeau.gbjam.MainClass.WIDTH;

public class AsteroidSpawner extends Spawner {
    private static final double MAX_SPEED = 5;
    private static final double MIN_SPEED = 2;
    private static final int WARNING_TIME = 2000;
    private static final TextureRegion WARNING_SPRITE = SPRITE_SHEET.getSubImage(5);
    private static final long MIN_TIME_BETWEEN_ASTEROIDS = 5000;
    private static final long PERCENT_CHANCE_SPAWN = 3;
    private static final int ROTATION_SPEED = 5;
    private static final Sound WARNING_SOUND = SoundFactory.getSound("warning.ogg");
    private int exist = 0;

    long lastSpawned = System.currentTimeMillis();

    @Override
    protected Thing spawn() {
        lastSpawned = System.currentTimeMillis();
        exist++;
        return new Asteroid();
    }

    @Override
    protected int shouldSpawn() {
        if (System.currentTimeMillis() - lastSpawned < MIN_TIME_BETWEEN_ASTEROIDS - (TinyEngine.getTicks() * 5)) {
            return 0;
        }
        return RANDOM.nextInt(100) < PERCENT_CHANCE_SPAWN / (exist + 1)  ? 1 : 0;
    }


    public class Asteroid extends Thing implements Collider {
        private final long warningStart;
        boolean isWarning = true;
        final Quadrant quadrant;
        TextureRegion asteroidSprite;
        double speed;

        private Asteroid() {
            this.quadrant = Quadrant.values()[RANDOM.nextInt(Quadrant.values().length)];
            speed = MIN_SPEED + RANDOM.nextDouble() * (MAX_SPEED - MIN_SPEED);
            WARNING_SOUND.loop(0.2f);
            warningStart = System.currentTimeMillis();
            moveTo(new Vector2D(quadrant.xSpawn, quadrant.ySpawn));
        }

        @Override
        public void onRemove() {
            super.onRemove();
            exist--;
        }

        @Override
        public void update() {
            super.update();
            if (isWarning && warningStart + WARNING_TIME < System.currentTimeMillis()) {
                WARNING_SOUND.stop();
                asteroidSprite = BIG_SPRITE_SHEET.getSubImage(RANDOM.nextInt(2));
                isWarning = false;
            }
            lastSpawned = System.currentTimeMillis();
            if (!isWarning) {
                move(new Vector2D(quadrant.xSpeed * speed, quadrant.ySpeed * speed));
            }
            if (warningStart + WARNING_TIME + 1000 < System.currentTimeMillis()) {
                getBehaviors().add(new DestroyOutOfScreenBehavior());
            }
        }

        @Override
        public void draw(SpriteBatch spriteBatch) {
            //SpriteBatch spriteBatch = TinyEngine.getSpriteBatch();
            //spriteBatch.begin();
            if (isWarning) {
                spriteBatch.draw(WARNING_SPRITE, quadrant.xWarn, quadrant.yWarn);
            } else {
                double rotationRequired = Math.toRadians(TinyEngine.getTicks() * ROTATION_SPEED);
                double locationX = asteroidSprite.getRegionWidth() / 2;
                double locationY = asteroidSprite.getRegionHeight() / 2;

                spriteBatch.draw(asteroidSprite, (int) getPosition().x, (int) getPosition().y);
            }
            //spriteBatch.end();
        }

        @Override
        public void collides(Collider collider) {

        }

        @Override
        public Rectangle getCollidingZone() {
            if (isWarning) {
                return new Rectangle(-10, -10, 0, 0);
            }
            return new Rectangle((int) getPosition().x + 3, (int) getPosition().y + 3, asteroidSprite.getRegionWidth() - 6, asteroidSprite.getRegionHeight() - 3);
        }
    }

    enum Quadrant {
        TOP_LEFT_TOP(0, 0, true, 8, -31, 0, 1),
        MIDDLE_TOP(70, 0, false, 68, -31, 0, 1),
        TOP_LEFT_LEFT(0, 0, false, -31, 13, 1, 0),
        TOP_RIGHT_TOP(WIDTH - 20, 0, true, WIDTH - 35, -31, 0, 1),
        MIDDLE_RIGHT(WIDTH - 20, 60, true, WIDTH, 65, -1, 0),
        TOP_RIGHT_RIGHT(WIDTH - 32, 0, false, WIDTH, 13, -1, 0),
        BOTTOM_LEFT_BOTTOM(0, HEIGHT - 32, true, 0, HEIGHT - 5, 0, -1),
        MIDDLE_LEFT(0, HEIGHT / 2, true, 0, HEIGHT / 2, 1, 0),
        BOTTOM_LEFT_LEFT(0, HEIGHT - 20, false, -31, HEIGHT - 31, 1, 0),
        BOTTOM_RIGHT_BOTTOM(WIDTH - 20, HEIGHT - 32, true, WIDTH - 31, HEIGHT - 36, 0, -1),
        MIDDLE_BOTTOM(WIDTH / 2, HEIGHT - 32, false, WIDTH / 2, HEIGHT - 36, 0, -1),
        BOTTOM_RIGHT_RIGHT(WIDTH - 32, HEIGHT - 20, false, WIDTH - 5, HEIGHT - 31, -1, 0);

        final int xWarn;
        final int yWarn;
        final boolean warnVertical;
        final int xSpawn;
        final int ySpawn;
        final int xSpeed;
        final int ySpeed;

        Quadrant(int xWarn, int yWarn, boolean warnVertical, int xSpawn, int ySpawn, int xSpeed, int ySpeed) {
            this.xWarn = xWarn;
            this.yWarn = yWarn;
            this.warnVertical = warnVertical;
            this.xSpawn = xSpawn;
            this.ySpawn = ySpawn;
            this.xSpeed = xSpeed;
            this.ySpeed = ySpeed;
        }
    }
}