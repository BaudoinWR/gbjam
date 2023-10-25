package com.woobadeau.gbjam;

import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.behavior.ContainedBehavior;
import com.woobadeau.tinyengine.sound.SoundFactory;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.physics.Collider;
import com.woobadeau.tinyengine.things.physics.Vector2D;
import com.woobadeau.tinyengine.things.sprites.AnimatedSprite;
import com.woobadeau.tinyengine.things.sprites.SpriteFactory;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Timer;
import java.util.logging.Logger;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Player extends Thing implements Collider {
    private static final Clip ROCKET_CLIP;

    static {
        try {
            ROCKET_CLIP = SoundFactory.getClip("/rockets.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    private static final Timer TIMER = new Timer();
    private static final Logger LOGGER = Logger.getLogger(Player.class.getName());
    private static final int MAX_TRASH = 9;
    private static final double FUEL_OXYGEN_USAGE = 0.15;
    private static final double BREATHING_OXYGEN_USAGE = 0.05;
    private final AnimatedSprite animatedSprite;
    private short lives = 3;
    private short maxLives = 3;
    private short animationStep;
    private float maxSpeed = 1.5f;
    private float speedX = 0;
    private float speedY = 0;
    private float acceleration = 0.2f;
    private float deceleration = 0.05f;
    private boolean moveUp;
    private boolean moveDown;
    private boolean moveLeft;
    private boolean moveRight;
    private double maxOxygen = 100;
    private double oxygen = maxOxygen;
    private Collection<TrashSpawner.Trash> trashBag = new ArrayList<>();

    public Player() throws IOException {
        animatedSprite = SpriteFactory.createAnimatedSprite("/gbspacedude.png", 2, 2);
        getThings().add(animatedSprite);
        addBehavior(new ContainedBehavior(-2, 12, 160-16, 144-16));
        addBehavior(new WobbleBehavior());
        TinyEngine.addKeyBinding("pressed UP", () -> moveUp = true );
        TinyEngine.addKeyBinding("released UP", () -> moveUp = false );
        TinyEngine.addKeyBinding("pressed DOWN", () -> moveDown = true );
        TinyEngine.addKeyBinding("released DOWN", () -> moveDown = false );
        TinyEngine.addKeyBinding("pressed LEFT", () -> moveLeft = true );
        TinyEngine.addKeyBinding("released LEFT", () -> moveLeft = false );
        TinyEngine.addKeyBinding("pressed RIGHT", () -> moveRight = true );
        TinyEngine.addKeyBinding("released RIGHT", () -> moveRight = false );
    }

    @Override
    public void update() {
        super.update();
        if (TinyEngine.getTicks() % 10 == 0) {
            // if 0 then 1, if 1 then 0, if 2 then 3, if 3 then 2
            animationStep = animationStep == 1 ? 0 : animationStep == 3 ? 2 : (short) (animationStep + 1);
        }
        if (isVisible()) {
            oxygen -= BREATHING_OXYGEN_USAGE;
            doMove();
            animatedSprite.setState(animationStep);
            animatedSprite.moveTo(this.getPosition());
        }
    }

    private void doMove() {
        boolean rocketOn = false;
        if (!moveUp && !moveDown) {
            if (speedY < 0) {
                speedY = speedY + deceleration;
            } else if (speedY > 0) {
                speedY = speedY - deceleration;
            }
            if (Math.abs(speedY) < deceleration) {
                speedY = 0;
            }
        } else {
            rocketOn = true;
            if (moveUp) {
                speedY = speedY - acceleration;
                if (animationStep < 2) {
                    animationStep = 2;
                }
            }
            if (moveDown) {
                speedY = speedY + acceleration;
                if (animationStep > 2) {
                    animationStep = 0;
                }
            }
        }
        if (!moveLeft && !moveRight) {
            if (speedX < 0) {
                speedX = speedX + deceleration;
            } else if (speedX > 0) {
                speedX = speedX - deceleration;
            }
            if (Math.abs(speedX) < deceleration) {
                speedX = 0;
            }
        } else {
            rocketOn = true;
            if (moveLeft) {
                speedX = speedX - acceleration;
                animatedSprite.setxFlipped(true);
            }
            if (moveRight) {
                speedX = speedX + acceleration;
                animatedSprite.setxFlipped(false);
            }
        }
        if (speedX > maxSpeed) {
            speedX = maxSpeed;
        }
        if (speedY > maxSpeed) {
            speedY = maxSpeed;
        }
        if (speedX < -maxSpeed) {
            speedX = -maxSpeed;
        }
        if (speedY < -maxSpeed) {
            speedY = -maxSpeed;
        }
        if (rocketOn) {
        oxygen -= (rocketOn ? 1 : 0)
                * FUEL_OXYGEN_USAGE;
            if (!ROCKET_CLIP.isActive()) {
                ROCKET_CLIP.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } else {
            ROCKET_CLIP.stop();
        }
        move(new Vector2D(speedX, speedY));
    }

    public short getLives() {
        return lives;
    }

    public short getMaxLives() {
        return maxLives;
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        getThings().forEach(thing -> thing.setVisible(visible));
    }

    @Override
    public void collides(Collider collider) {
        if (collider instanceof TrashSpawner.Trash trash && trashBag.size() < MAX_TRASH) {
            trash.destroy();
            trash.playClip();
            trashBag.add(trash);
        } else if (collider instanceof Ship) {
            if (trashBag.size() > 0) {
                setVisible(false);
                oxygen = maxOxygen;
                speedX = speedY = 0;
                TIMER.schedule(new java.util.TimerTask() {
                    @Override
                    public void run() {
                        setVisible(true);
                    }
                }, 2000);
                trashBag.clear();
                ROCKET_CLIP.stop();
            }
        }
    }

    public double getMaxOxygen() {
        return maxOxygen;
    }

    public double getOxygen() {
        return oxygen;
    }

    @Override
    public Rectangle getCollidingZone() {
        return new Rectangle((int) getPosition().x+5, (int) getPosition().y+3, 8, 10);
    }

    public Collection<TrashSpawner.Trash> getTrashBag() {
        return trashBag;
    }

    public void setTrashBag(Collection<TrashSpawner.Trash> trashBag) {
        this.trashBag = trashBag;
    }
}
