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
     * 値を保存します。
     * @param key 任意キー
     * @param value 任意値
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

    /**
     * 型安全に値を取得します。
     /**
      * 型安全に値を取得します。
      * `@param` key 任意キー
      * `@param` type 期待する型
      * `@return` 保存値（未設定時はnull、型不一致時もnull）
      */
     */
    public <T> T get(String key, Class<T> type) {
        Object value = state.get(key);
        if (type.isInstance(value)) {
            return type.cast(value);
        }
        return null;
    }
        }
        return null;
    }  
}
