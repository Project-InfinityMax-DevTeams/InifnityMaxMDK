package com.example.examplemod.registration;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 1つのゲーム要素定義を表します。
 * <p>
 * 各値は文字列・数値を書き換えるだけで再利用できる構造にしています。
 */
public final class GameElementDefinition {
    private final GameElementType type;
    private final String id;
    private final String implClass;
    private final Map<String, Object> properties;

    /**
     * @param type 要素種別
     * @param id 一意ID（ここを変えると別要素として登録）
     * @param implClass 実装クラス名（ここを差し替えるだけでロジック変更可能）
     * @param properties 追加設定値
     */
    public GameElementDefinition(GameElementType type, String id, String implClass, Map<String, Object> properties) {
        this.type = type;
        this.id = id;
        this.implClass = implClass;
        this.properties = Collections.unmodifiableMap(new LinkedHashMap<>(properties));
    }

    /** @return 種別 */
    public GameElementType type() { return type; }

    /** @return ID */
    public String id() { return id; }

    /** @return 実装クラス */
    public String implClass() { return implClass; }

    /** @return 追加設定 */
    public Map<String, Object> properties() { return properties; }
}
