package io.github.approject.ap_project.Screens;

import java.io.Serializable;

public class BodyState implements Serializable {
    public float[] position;
    public float[] impulse; // Impulse instead of velocity
    public float angle;     // Angle of rotation
    public String bodyType;
    public boolean canlayegg;

    public BodyState(float[] position, float[] impulse, float angle, String bodyType, boolean canlayegg) {
        this.position = position;
        this.impulse = impulse;
        this.angle = angle;
        this.bodyType = bodyType;
        this.canlayegg = canlayegg;

    }
}


