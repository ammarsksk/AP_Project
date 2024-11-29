package io.github.approject.ap_project.Materials;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
import io.github.approject.ap_project.Pigs.FreakPig;
import io.github.approject.ap_project.Pigs.KingPig;
import io.github.approject.ap_project.Screens.WoodPlank;

public class Material {
    private final Texture texture;
    private int cur_hits;
    private int max_hits;

    public Material(String textureFilePath) {
        this.texture = new Texture(textureFilePath);
        cur_hits = 0;
        if (this instanceof IceBlock || this instanceof WoodPlank) max_hits = 6;
        else if (this instanceof WoodBlock) max_hits = 7;
        else max_hits = 8;
    }

    public Texture getTexture() {
        return texture;
    }

    public Body getBoxBody(World world, float x, float y, float pWidth, float pHeight) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.fixedRotation = false; // This allows the body to rotate
        bodyDef.position.set(x, y);

        PolygonShape boxShape = new PolygonShape();
        boxShape.setAsBox(pWidth/200f, pHeight/200f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boxShape;
        fixtureDef.density = 5.0f;
        fixtureDef.friction = 1f;
        fixtureDef.restitution = 0f;

        Body body = world.createBody(bodyDef);
        body.createFixture(fixtureDef);
        boxShape.dispose();

        return body;
    }


    public boolean isDestroyed() {
        return (max_hits - cur_hits) <= 0;
    }

    public int getCur_hits() {return cur_hits;}
    public void hitMaterial(int hits){cur_hits+=hits;}
    public TextureRegion getTextureRegion() {
        return new TextureRegion(texture);
    }
}
