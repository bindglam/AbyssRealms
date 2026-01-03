package com.bindglam.abyssrealms.client;

import com.bindglam.abyssrealms.client.io.Window;
import lombok.Getter;
import org.joml.Vector4f;

public final class AbyssRealmsClient implements Runnable {
    @Getter
    private static AbyssRealmsClient instance;

    @Getter
    private final Window window = new Window(1280, 720, "AbyssRealms");

    public AbyssRealmsClient() {
        if(AbyssRealmsClient.instance != null)
            throw new IllegalStateException();

        AbyssRealmsClient.instance = this;
    }

    @Override
    public void run() {
        window.init();

        while(!window.shouldClose()) {
            window.setBackgroundColor(new Vector4f(1f, 0f, 0f, 1f));
            window.clear();

            window.update();
        }

        window.destroy();
    }
}
