package com.woobadeau.gbjam;

import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.behavior.Behavior;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.physics.Vector2D;

public class WobbleBehavior implements Behavior {
    @Override
    public void accept(Thing thing) {
        Vector2D wobble = new Vector2D(0, getYWobble());
        thing.move(wobble);
    }

    private static int getYWobble() {
        long ticks = TinyEngine.getTicks();
        if (ticks % 40 == 0) {
            return 2;
        }
        if (ticks % 30 == 0) {
            return 1;
        }
        if (ticks % 20 == 0) {
            return -1;
        }
        if (ticks % 10 == 0) {
            return -2;
        }
        return 0;
    }
}
