package com.bymarcin.openglasses.integration.computronics;

import pl.asie.computronics.api.chat.ChatAPI;
import pl.asie.computronics.api.chat.IChatListener;

public class ComputronicsHelper {

    public static void register(Object handler) {
        ChatAPI.registry.registerChatListener((IChatListener) handler);
    }

    public static void unregister(Object handler) {
        ChatAPI.registry.unregisterChatListener((IChatListener) handler);
    }
}
