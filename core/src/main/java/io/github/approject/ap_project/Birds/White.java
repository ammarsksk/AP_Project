package io.github.approject.ap_project.Birds;

import com.badlogic.gdx.physics.box2d.*;

public class White extends Bird {
    float PPM = 100f;

    public White() {
        super("white.png");
        this.canlayegg = true;
    }
    public Body layEgg(World world, float x, float y){
        return getEggBody(world, x, y);
    }
}
