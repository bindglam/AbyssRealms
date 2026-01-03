package com.bindglam.abyssrealms.client.render.vertex;

import com.bindglam.abyssrealms.util.Destroyable;

import static org.lwjgl.opengles.GLES20.*;

public final class VBO implements Bindable, Destroyable {
    private final int id;

    public VBO(float[] vertices) {
        this.id = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, this.id);
        glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
    }

    @Override
    public void bind() {
        glBindBuffer(GL_ARRAY_BUFFER, id);
    }

    @Override
    public void unbind() {
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void destroy() {
        glDeleteBuffers(id);
    }
}
