package com.example.examplemod.system;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MOD独自システムで共通利用するコンテキストです。
 * Minecraft APIに依存しない純Javaクラスとして設計しています。
 */
public final class ModSystemContext {
    private final Map<String, Object> state = new LinkedHashMap<>();

    /**
     * 値を保存します。 / Puts Value.
     * @param key 任意キー / Any Key.
     * @param value 任意値 / Any Value.
     */
    public void put(String key, Object value) {
        state.put(key, value);
    }

    /**
     * 値を取得します。 / Get Value.
     * @param key 任意キー
     * @return 保存値（未設定時はnull）
     */
    public Object get(String key) {
        return state.get(key);
    }
}
