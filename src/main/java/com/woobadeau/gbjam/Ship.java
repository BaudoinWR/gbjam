package com.woobadeau.gbjam;

import com.woobadeau.tinyengine.behavior.ContainedBehavior;
import com.woobadeau.tinyengine.things.Thing;
import com.woobadeau.tinyengine.things.physics.Collider;
import com.woobadeau.tinyengine.things.physics.Vector2D;
import com.woobadeau.tinyengine.things.sprites.SpriteFactory;
import java.awt.*;
import java.io.IOException;

import static com.woobadeau.gbjam.GBJam.HEIGHT;

public class Ship extends Thing implements Collider {
    protected Ship() throws IOException {
        getThings().add(SpriteFactory.createSprite("/gbship.png"));
        addBehavior(new ContainedBehavior(2, HEIGHT - 20, 5, HEIGHT - 30));
        addBehavior(new WobbleBehavior());
        moveTo(new Vector2D(2, HEIGHT - 25));
    }

    @Override
    public void update() {
        super.update();
        getThings().forEach(
                thing -> thing.moveTo(new Vector2D(getPosition().x, getPosition().y))
        );
    }

    @Override
    public Rectangle getCollidingZone() {
        return new Rectangle((int) getPosition().x + 28, (int) getPosition().y + 2, 22, 11);
    }

    @Override
    public void collides(Collider collider) {
    }
}
