package com.bindglam.abyssrealms.client.render.vertex;

import com.bindglam.abyssrealms.util.Destroyable;

import static org.lwjgl.opengles.GLES30.*;

public final class VAO implements Bindable, Destroyable {
    private final int id;

    public VAO() {
        this.id = glGenVertexArrays();
    }

    public void linkVBO(VBO vbo, int layout, int numComponents, int type, int stride, long offset) {
        vbo.bind();
        glVertexAttribPointer(layout, numComponents, type, false, stride, offset);
        glEnableVertexAttribArray(layout);
        vbo.unbind();
    }

    @Override
    public void bind() {
        glBindVertexArray(id);
    }

    @Override
    public void unbind() {
        glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        glDeleteVertexArrays(id);
    }
}
