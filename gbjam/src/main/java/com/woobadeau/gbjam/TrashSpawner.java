package com.woobadeau.gbjam;

import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.things.Spawner;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.physics.Collider;
import com.woobadeau.tinyengine.things.physics.Vector2D;
import com.woobadeau.tinyengine.things.sprites.Sprite;
import java.awt.*;

import static com.woobadeau.gbjam.GBJam.RANDOM;

public class TrashSpawner extends Spawner {
    private static int spawned = 0;
    private static int maxSpawned = 5;
    @Override
    protected Thing spawn() {
        spawned++;
        return new Trash(Trash.TrashType.values()[RANDOM.nextInt(Trash.TrashType.values().length)]);
    }

    @Override
    protected int shouldSpawn() {
        return spawned < maxSpawned && TinyEngine.getTicks() % 100 == 0 ? 1 : 0;
    }

    class Trash extends Sprite implements Collider {
        public Trash(TrashType type) {
            super(GBJam.SPRITE_SHEET.getImage(type.imageIndex), 1);
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

        private enum TrashType {
            CAN(0, 100), BROKEN_CAN(1, 200), BAG(2, 500);

            private final int score;
            private final int imageIndex;

            TrashType(int imageIndex, int score) {
                this.imageIndex = imageIndex;
                this.score = score;
            }

            public int getScore() {
                return score;
            }

            public int getImageIndex() {
                return imageIndex;
            }
        }
    }
}
