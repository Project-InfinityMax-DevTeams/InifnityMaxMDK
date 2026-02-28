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
     * 単一のゲーム要素定義インスタンスを作成する。
     *
     * @param type 要素の種類
     * @param id 要素の一意識別子。ここを変更すると別要素として扱われる
     * @param implClass 要素の実装クラスの完全修飾名
     * @param properties 要素に関連する追加設定。挿入順を保持した変更不可のマップとして保持される
     */
    public GameElementDefinition(GameElementType type, String id, String implClass, Map<String, Object> properties) {
        this.type = type;
        this.id = id;
        this.implClass = implClass;
        this.properties = Collections.unmodifiableMap(new LinkedHashMap<>(properties));
    }

    /**
 * 要素の種別を返す。
 *
 * @return `GameElementType`で表される要素の種別。
 */
    public GameElementType type() { return type; }

    /**
 * 要素の一意識別子を返す。
 *
 * @return 要素の一意識別子
 */
    public String id() { return id; }

    /**
 * 要素を実装するクラスの完全修飾クラス名を返す。
 *
 * @return 実装クラスの完全修飾クラス名（例: com.example.MyImpl）
 */
    public String implClass() { return implClass; }

    /**
 * 要素に関連する追加設定を順序を保持した読み取り専用マップとして返す。
 *
 * @return 要素に関連付けられた追加設定の読み取り専用マップ（挿入順を保持）
 */
    public Map<String, Object> properties() { return properties; }
}
