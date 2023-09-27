package com.woobadeau.gbjam;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.sound.SoundFactory;
import com.woobadeau.tinyengine.things.Spawner;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.physics.Collider;
import com.woobadeau.tinyengine.things.physics.Vector2D;

import static com.woobadeau.gbjam.MainClass.RANDOM;

public class TrashSpawner extends Spawner {
    private static final Sound PICKUP_CAN_CLIP = SoundFactory.getSound("pickup_can.ogg");

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

    class Trash extends Thing implements Collider {

        private final TrashType type;
        private final TextureRegion image;

        public Trash(TrashType type) {
            //super(, 1);
            image = MainClass.SPRITE_SHEET.getSubImage(type.imageIndex);
            this.type = type;
            addBehavior(new WobbleBehavior());
            moveTo(new Vector2D(RANDOM.nextInt(MainClass.WIDTH - 8), RANDOM.nextInt(MainClass.HEIGHT - 22) + 21));
        }

        @Override
        public void draw(SpriteBatch spriteBatch) {
            spriteBatch.draw(image, (float) getPosition().x, (float) getPosition().y);
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
            type.clip.play(0.3f);
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
        private final Sound clip;
        private final int imageIndex;

        TrashType(int imageIndex, int score, Sound clip) {
            this.imageIndex = imageIndex;
            this.score = score;
            this.clip = clip;
        }
    }
}
