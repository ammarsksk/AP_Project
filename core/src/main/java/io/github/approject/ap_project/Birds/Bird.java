package io.github.approject.ap_project.Birds;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;

public class Bird {
    private final Texture texture;
    private int damage;
    protected boolean canlayegg = false;

    public Bird(String textureFilePath) {
        this.texture = new Texture(textureFilePath);
        if (this instanceof White) damage = 4;
        else if (this instanceof Red) damage = 3;
        else if (this instanceof Yellow) damage = 3;
        else damage = 2;
    }

    public int getDamage() {
        return damage;
    }

    public Texture getTexture() {
        return texture;
    }


    public Body getBirdBody(World world,float x, float y){
        BodyDef bodydef = new BodyDef();
        bodydef.type = BodyDef.BodyType.KinematicBody;
        bodydef.fixedRotation = false; // This allows the body to rotate
        bodydef.position.set(x,y);

        CircleShape ball_shape = new CircleShape();
        ball_shape.setRadius(0.5f);

        FixtureDef fixture_def = new FixtureDef();
        fixture_def.shape = ball_shape;
        fixture_def.density = 5.0f;
        fixture_def.friction = 1f;
        fixture_def.restitution = 0.75f;

        Body body = world.createBody(bodydef);
        body.createFixture(fixture_def);
        ball_shape.dispose();

        return body;
    }

    public Body getEggBody(World world, float x, float y){
        BodyDef bodydef = new BodyDef();
        bodydef.type = BodyDef.BodyType.DynamicBody;
        bodydef.fixedRotation = false; // This allows the body to rotate
        bodydef.position.set(x,y);

        CircleShape ball_shape = new CircleShape();
        ball_shape.setRadius(0.175f);

        FixtureDef fixture_def = new FixtureDef();
        fixture_def.shape = ball_shape;
        fixture_def.density = 5.0f;
        fixture_def.friction = 1f;
        fixture_def.restitution = 0.75f;

        Body body = world.createBody(bodydef);
        body.createFixture(fixture_def);
        ball_shape.dispose();

        return body;
    }

    public TextureRegion getTextureRegion() {
        return new TextureRegion(texture);
    }

    public boolean isCanlayegg() {
        return canlayegg;
    }

    public void setCanlayegg(boolean canlayegg) {
        this.canlayegg = canlayegg;
    }
}
