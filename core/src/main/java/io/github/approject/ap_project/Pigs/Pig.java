package io.github.approject.ap_project.Pigs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;

public class Pig {
    private final Texture texture;
    private int cur_hits;
    private int max_hits;

    public Pig(String textureFilePath) {
        this.texture = new Texture(textureFilePath);
        this.cur_hits = 0;
        if (this instanceof NPCPig) this.max_hits = 6;
        else if (this instanceof FreakPig) this.max_hits = 7;
        else this.max_hits = 8;
    }

    public Texture getTexture() {
        return texture;
    }

    public int getCur_hits() {return cur_hits;}
    public void hitPig(int hits){cur_hits+=hits;}


    public Body getPigBody(World world, float x, float y, float pWidth) {
        BodyDef bodydef = new BodyDef();
        bodydef.type = BodyDef.BodyType.DynamicBody;
        bodydef.fixedRotation = false; // This allows the body to rotate
        bodydef.position.set(x, y);

        CircleShape ball_shape = new CircleShape();
        ball_shape.setRadius(pWidth/200f);

        FixtureDef fixture_def = new FixtureDef();
        fixture_def.shape = ball_shape;
        fixture_def.density = 5.0f;
        fixture_def.restitution = 0f;
        fixture_def.friction = 100f;

        Body body = world.createBody(bodydef);
        body.createFixture(fixture_def);
        ball_shape.dispose();

        return body;
    }


    public boolean isDead() {
        return (max_hits - cur_hits) <= 0;
    }
    public TextureRegion getTextureRegion() {
        return new TextureRegion(texture);
    }


}
