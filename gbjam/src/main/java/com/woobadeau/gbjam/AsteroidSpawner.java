package com.woobadeau.gbjam;

import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.things.Spawner;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.physics.Collider;
import com.woobadeau.tinyengine.things.physics.Vector2D;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;

import static com.woobadeau.gbjam.GBJam.BIG_SPRITE_SHEET;
import static com.woobadeau.gbjam.GBJam.HEIGHT;
import static com.woobadeau.gbjam.GBJam.RANDOM;
import static com.woobadeau.gbjam.GBJam.SPRITE_SHEET;
import static com.woobadeau.gbjam.GBJam.WIDTH;

public class AsteroidSpawner extends Spawner {
    private static final long MIN_TIME_BETWEEN_ASTEROIDS = 5000;
    private static final long PERCENT_CHANCE_SPAWN = 5;
    private static final int ROTATION_SPEED = 5;

    long lastSpawned = 0;

    @Override
    protected Thing spawn() {
        lastSpawned = Instant.now().toEpochMilli();
        return new Asteroid();
    }

    @Override
    protected int shouldSpawn() {
        if (Instant.now().toEpochMilli() - lastSpawned < MIN_TIME_BETWEEN_ASTEROIDS) {
            return 0;
        }
        return RANDOM.nextInt(100) < PERCENT_CHANCE_SPAWN ? 1 : 0;
    }

    private class Asteroid extends Thing implements Collider {
        private static final double MAX_SPEED = 5;
        private static final double MIN_SPEED = 2;
        private static final int WARNING_TIME = 2000;
        private static final BufferedImage WARNING_SPRITE = SPRITE_SHEET.getImage(5);
        boolean isWarning = true;
        final Quadrant quadrant;
        BufferedImage asteroidSprite;
        double speed;

        private Asteroid() {
            this.quadrant = Quadrant.values()[RANDOM.nextInt(Quadrant.values().length)];
            speed = RANDOM.nextDouble(MIN_SPEED, MAX_SPEED);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    isWarning = false;
                    asteroidSprite = BIG_SPRITE_SHEET.getImage(RANDOM.nextInt(2));
                }
            }, WARNING_TIME);
            moveTo(new Vector2D(quadrant.xSpawn, quadrant.ySpawn));
        }

        @Override
        public void update() {
            super.update();
            if (!isWarning) {
                move(new Vector2D(quadrant.xSpeed * speed, quadrant.ySpeed * speed));
            }
        }

        @Override
        public void draw(Graphics graphics) {
            if (isWarning) {
                graphics.drawImage(WARNING_SPRITE, quadrant.xWarn, quadrant.yWarn, null);
            } else {
                double rotationRequired = Math.toRadians(TinyEngine.getTicks() * ROTATION_SPEED);
                double locationX = asteroidSprite.getWidth() / 2;
                double locationY = asteroidSprite.getHeight() / 2;
                AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
                AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

                graphics.drawImage(op.filter(asteroidSprite, null), (int) getPosition().x, (int) getPosition().y, null);
            }
        }

        @Override
        public void collides(Collider collider) {

        }

        @Override
        public Rectangle getCollidingZone() {
            if (isWarning) {
                return new Rectangle(-10, -10, 0, 0);
            }
            return new Rectangle((int) getPosition().x + 3, (int) getPosition().y + 3, asteroidSprite.getWidth() - 6, asteroidSprite.getHeight() - 3);
        }

        enum Quadrant {
            TOP_LEFT_TOP(0, 0, true, 8, -31, 0, 1),
            TOP_LEFT_LEFT(0, 0, false, -31, 13, 1, 0),
            TOP_RIGHT_TOP(WIDTH - 96, 0, true, WIDTH - 35, -31, 0, 1),
            TOP_RIGHT_RIGHT(WIDTH - 32, 0, false, WIDTH, 13, -1, 0),
            BOTTOM_LEFT_BOTTOM(0, HEIGHT - 32, true, 0, HEIGHT - 5, 0, -1),
            BOTTOM_LEFT_LEFT(0, HEIGHT - 96, false, -31, HEIGHT - 31, 1, 0),
            BOTTOM_RIGHT_BOTTOM(WIDTH - 96, HEIGHT - 32, true, WIDTH - 31, HEIGHT - 5, 0, -1),
            BOTTOM_RIGHT_RIGHT(WIDTH - 32, HEIGHT - 96, false, WIDTH - 5, HEIGHT - 31, -1, 0);

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
}