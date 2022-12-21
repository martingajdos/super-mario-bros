package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.MarioGameTest;
import com.mygdx.game.screens.PlayScreen;

public class Mario extends Sprite {
    public World world;
    public Body body;
    private TextureRegion marioStand;

    public Mario(World world, PlayScreen screen) {
        super(screen.getAtlas().findRegion("little_mario"));
        this.world = world;
        defineMario();

        // get first sprite from the implemented Sprite texture, 16 by 16 wide
        marioStand = new TextureRegion(getTexture(), 0, 0, 16, 16);

        // Sets the position and size of the sprite when drawn, before scaling and rotation are applied.
        setBounds(0, 0, 16 / MarioGameTest.PPM, 16 / MarioGameTest.PPM);

        // region is actual texture that is associated with this sprite
        setRegion(marioStand);
    }

    private void defineMario() {
        // Body definition
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(32 / MarioGameTest.PPM, 32 / MarioGameTest.PPM);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        // Create body in the world
        body = world.createBody(bodyDef);

        // Fixture and shape
        FixtureDef fixtureDef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6 / MarioGameTest.PPM);
        fixtureDef.shape = shape;

        // Create the fixture in body
        body.createFixture(fixtureDef);
    }

    public void tick(float dt) {
        // set the position of the texture
        // body.getPosition().x - pos of fixture
        // getWidth() - width of the sprite (because of extending the sprite) - parent class
        setPosition(body.getPosition().x - ((getWidth() / 2) + 2 / MarioGameTest.PPM),
                body.getPosition().y - ((getHeight() / 2) - 1 / MarioGameTest.PPM));
    }
}
