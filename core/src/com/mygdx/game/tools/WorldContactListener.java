package com.mygdx.game.tools;

import com.badlogic.gdx.physics.box2d.*;
import com.mygdx.game.sprites.InteractiveTileObject;

public class WorldContactListener implements ContactListener {

    // gets called when 2 fixtures begin to collide
    @Override
    public void beginContact(Contact contact) {
        // get colliding fixtures
        Fixture fixtureA = contact.getFixtureA();
        Fixture fixtureB = contact.getFixtureB();

        // check if the collision is between an object and Mario's head
        if (fixtureA.getUserData() == "head" || fixtureB.getUserData() == "head") {

            // get the fixture which of those two is a head
            Fixture head = fixtureA.getUserData() == "head" ? fixtureA : fixtureB;

            // get the fixture which of those is the object that Mario has collided with
            Fixture object = head == fixtureA ? fixtureB : fixtureA;

            // if the object is an interactive tile object (it extends that class)
            if (object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())) {

                // execute the head hit method of the interactive tile object
                ((InteractiveTileObject) object.getUserData()).onHeadHit();
            }
        }
    }

    // gets called when 2 fixtures disconnect with each other
    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }
}