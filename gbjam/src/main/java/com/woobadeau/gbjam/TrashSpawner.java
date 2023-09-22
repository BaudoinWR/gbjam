package com.woobadeau.gbjam;

import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.sound.SoundFactory;
import com.woobadeau.tinyengine.things.Spawner;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.physics.Collider;
import com.woobadeau.tinyengine.things.physics.Vector2D;
import com.woobadeau.tinyengine.things.sprites.Sprite;
import java.awt.*;
import java.io.IOException;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import static com.woobadeau.gbjam.GBJam.RANDOM;

public class TrashSpawner extends Spawner {
    private static final Clip PICKUP_CAN_CLIP;

    static {
        try {
            PICKUP_CAN_CLIP = SoundFactory.getClip("/pickup_can.wav");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }
    private int spawned = 0;
    private static final int MAX_SPAWNED = 5;
    @Override
    protected Thing spawn() {
        spawned++;
        return new Trash(TrashType.values()[RANDOM.nextInt(TrashType.values().length)]);
    }

    @Override
    protected int shouldSpawn() {
        return spawned < MAX_SPAWNED && TinyEngine.getTicks() % 100 == 0 ? 1 : 0;
    }

    class Trash extends Sprite implements Collider {

        private final TrashType type;

        public Trash(TrashType type) {
            super(GBJam.SPRITE_SHEET.getImage(type.imageIndex), 1);
            this.type = type;
            addBehavior(new WobbleBehavior());
            moveTo(new Vector2D(RANDOM.nextInt(GBJam.WIDTH - 8), RANDOM.nextInt(GBJam.HEIGHT - 22) + 13));
        }

        public void onRemove() {
            super.onRemove();
            spawned--;
        }

        @Override
        public void collides(Collider collider) {
            // nothing
        }

        @Override
        public Rectangle getCollidingZone() {
            return new Rectangle((int) getPosition().x, (int) getPosition().y, 8, 8);
        }

        public void playClip() {
            type.clip.setFramePosition(0);
            type.clip.start();
        }

        public int getScore() {
            return type.score;
        }

    }
    private enum TrashType {
        CAN(0, 100, PICKUP_CAN_CLIP),
        BROKEN_CAN(1, 200, PICKUP_CAN_CLIP),
        BAG(2, 500, PICKUP_CAN_CLIP);

        private final int score;
        private final Clip clip;
        private final int imageIndex;

        TrashType(int imageIndex, int score, Clip clip) {
            this.imageIndex = imageIndex;
            this.score = score;
            this.clip = clip;
        }
    }
}
