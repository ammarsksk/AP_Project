package io.github.approject.ap_project.Materials;

import com.badlogic.gdx.graphics.Texture;

public class Material {
    private final Texture texture;

    public Material(String textureFilePath) {
        this.texture = new Texture(textureFilePath);
    }

    public Texture getTexture() {
        return texture;
    }
}
