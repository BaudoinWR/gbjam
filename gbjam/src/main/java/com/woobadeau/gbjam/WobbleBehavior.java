package com.woobadeau.gbjam;

import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.behavior.Behavior;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.physics.Vector2D;
import java.util.Random;

public class WobbleBehavior implements Behavior {
    private final int offset;

    public WobbleBehavior() {
        offset = new Random().nextInt(9);
    }

    @Override
    public void accept(Thing thing) {
        Vector2D wobble = new Vector2D(0, getYWobble());
        thing.move(wobble);
    }

    private int getYWobble() {
        long ticks = TinyEngine.getTicks() + offset;
        if (ticks % 40 == 0) {
            return 1;
        }
        if (ticks % 30 == 0) {
            return 1;
        }
        if (ticks % 20 == 0) {
            return -1;
        }
        if (ticks % 10 == 0) {
            return -1;
        }
        return 0;
    }
}
