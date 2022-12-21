package com.mygdx.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.MarioGameTest;
import com.mygdx.game.gui.Hud;
import com.mygdx.game.sprites.Goomba;
import com.mygdx.game.sprites.Mario;
import com.mygdx.game.tools.B2WorldCreator;
import com.mygdx.game.tools.WorldContactListener;

public class PlayScreen implements Screen {

    private MarioGameTest game;
    private OrthographicCamera camera;
    private Viewport gamePort;

    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    // BOX2D Variables
    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;

    private Mario player;

    private TextureAtlas atlas;

    private Hud hud;

    private Music music;

    // temp enemy
    private Goomba goomba;

    public PlayScreen(MarioGameTest game) {
        this.atlas = new TextureAtlas("Mario_and_Enemies.pack");
        this.game = game;
        this.hud = new Hud(game.batch);
        camera = new OrthographicCamera();
        gamePort = new FitViewport(MarioGameTest.V_WIDTH / MarioGameTest.PPM, MarioGameTest.V_HEIGHT / MarioGameTest.PPM, camera);

        initMapStuff();

        camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);

        initBox2dStuff();
        new B2WorldCreator(this);

        player = new Mario(this);

        // set contact listener
        world.setContactListener(new WorldContactListener());

        music = MarioGameTest.manager.get("audio/music/mario_music.ogg", Music.class);
        music.setLooping(true);
        music.setVolume(0.05f);
        //music.play();

        goomba = new Goomba(this, .32f, .32f);
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    private void initMapStuff() {
        mapLoader = new TmxMapLoader();
        map = mapLoader.load("level1.tmx");
        renderer = new OrthogonalTiledMapRenderer(map, 1 / MarioGameTest.PPM);
    }

    private void initBox2dStuff() {
        world = new World(new Vector2(0, -10f), true);
        box2DDebugRenderer = new Box2DDebugRenderer();
    }

    float tx;
    float ty;
    float constant = 0.25f;

    private void tick(float dt) {
        handleInput(dt);
        world.step(1 / 60f, 6, 2);
        player.tick(dt);
        goomba.tick(dt);
        hud.tick(dt);

//        tx = player.body.getPosition().x-(camera.viewportWidth/2)/MarioGameTest.PPM+player.getWidth()/2;
//        ty = player.body.getPosition().y-(camera.viewportHeight/2)/MarioGameTest.PPM+player.getHeight()/2;
//        camera.position.x += (tx-camera.position.x)*constant;
//        camera.position.y += (ty-camera.position.y)*constant;

        camera.position.x = player.body.getPosition().x;

        camera.update();
        renderer.setView(camera);
    }

    float jsp = 4f;
    float ssp = 0.1f;
    float maxVel = 2;

    private void handleInput(float dt) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            player.body.applyLinearImpulse(new Vector2(0, jsp), player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && player.body.getLinearVelocity().x <= maxVel) {
            player.body.applyLinearImpulse(new Vector2(ssp, 0), player.body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A) && player.body.getLinearVelocity().x >= -maxVel) {
            player.body.applyLinearImpulse(new Vector2(-ssp, 0), player.body.getWorldCenter(), true);
        }


    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        tick(delta);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(100.0f / 255.0f, 172.0f / 255.0f, 254.0f / 255.0f, 1.0f);

        {
            // render world and debug
            renderer.render();
            box2DDebugRenderer.render(world, camera.combined);

            // render hud
            game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
            hud.stage.draw();

            // render player
            game.batch.setProjectionMatrix(camera.combined);
            game.batch.begin();

            player.draw(game.batch);
            goomba.draw(game.batch);

            game.batch.end();
        }
    }

    public TiledMap getMap() {
        return map;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public void resize(int width, int height) {
        gamePort.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        world.dispose();
        box2DDebugRenderer.dispose();
        hud.dispose();
    }
}
