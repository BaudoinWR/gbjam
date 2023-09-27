package com.woobadeau.gbjam;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.woobadeau.tinyengine.TinyEngine;
import com.woobadeau.tinyengine.things.sprites.SpriteFactory;
import com.woobadeau.tinyengine.things.sprites.SpriteFontText;
import java.io.IOException;
import java.util.Random;


public class GBJam extends ApplicationAdapter {
    private static final Random RANDOM = new Random();
    private static int tick = 0;
    private static final double STARS = 100;
    private static final int WIDTH = 160;
    private static final int HEIGHT = 144;
    private static final int LIGHT_GREEN = 0xE0F8D0FF;
    public static final int SCALE = 4;
    private SpriteFontText SPRITE_FONT_TEXT;
    SpriteBatch batch;
	Texture img;
    TinyEngine engine;
    private OrthographicCamera camera;
    private Viewport viewpoert;
    static {
    }
    @Override
	public void create () {
        SPRITE_FONT_TEXT = SpriteFactory.createSpriteFontText("gbfont.png");

        //camera = new OrthographicCamera();
        //camera.setToOrtho(false, 160*4, 144*4);

        //camera = new OrthographicCamera(160,144*4);
        //camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);

        engine = new TinyEngine(WIDTH, HEIGHT, 1, new Runnable() {
            @Override
            public void run() {
//
            }
        });
        batch = TinyEngine.getSpriteBatch();
        //try {
        //    SpriteFactory.createSprite("badlogic.jpg");
        //} catch (IOException e) {
        //    throw new RuntimeException(e);
        //}
        //new PixelSpawner(new Rectangle(0, 0));
        //for (int i = 0; i < STARS; i++) {
        //    int x = RANDOM.nextInt(WIDTH);
        //    int y = RANDOM.nextInt(HEIGHT - 11);
        //    System.out.printf("%s:%s\n", x, y);
        //    Pixel star = new Pixel(x, y, LIGHT_GREEN);
        //    star.setZIndex(-2)aaa;
        //    //getThings().add(star);
        //}
        //viewpoert = new StretchViewport(160*4, 144*4, camera);
        //viewpoert.apply();
        try {
            new GameController();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
        engine.start();

    }

   //@Override
   //public void resize(int width, int height) {
   //    viewpoert.update(width, height);

   //}

    @Override
	public void render () {
        System.out.println("tick: " + tick++);
        batch.begin();
        engine.tick();
        TinyEngine.draw(batch);
        batch.end();
        //camera.update();
    }

	@Override
	public void dispose () {
		//batch.dispose();
		img.dispose();
	}
}
