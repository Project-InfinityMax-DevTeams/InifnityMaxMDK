package com.example.examplemod.system;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * MOD独自システムで共通利用するコンテキストです。
 * <p>
 * Minecraft APIに依存しない純Javaクラスとして設計しています。
 */
public final class ModSystemContext {
    private final Map<String, Object> state = new LinkedHashMap<>();

    /**
     * 指定したキーに値を格納する。
     *
     * @param key   格納に使用するキー。既存のエントリがある場合は値を上書きする（null を許容）。
     * @param value 格納する値（null を許容）。
     */
    public void put(String key, Object value) {
        state.put(key, value);
    }

    /**
     * 値を取得します。
     * @param key 任意キー
     * @return 保存値（未設定時はnull）
     */
    public Object get(String key) {
        return state.get(key);
    }
}
