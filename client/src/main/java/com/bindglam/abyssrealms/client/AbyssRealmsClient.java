package com.bindglam.abyssrealms.client;

import com.bindglam.abyssrealms.client.io.Window;
import lombok.Getter;

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
            window.clear();

            window.update();
        }

        window.destroy();
    }
}
