package com.example.examplemod.registration;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * DSL定義を実行するためのサンプル実装です。
 * <p>
 * 実際のMODでは、このクラス内で Fabric/Forge の実レジストリ呼び出しに置き換えてください。
 */
public final class RegistrationExecutor {

    /**
     * 登録処理を実行します。
     * @param definitions DSL定義一覧
     */
    public void registerAll(List<GameElementDefinition> definitions) {
        for (GameElementDefinition definition : definitions) {
            // ここを各ローダーの登録APIに置換すれば本番登録が完成します。
            System.out.println("[REGISTER] " + definition.type() + " -> " + definition.id() + " => " + definition.implClass());
        }
    }

    /**
     * 登録不要要素（JSON出力対象）を返します。
     * <p>
     * 例: 今回使わない要素を明示して、GradleでJSON化します。
     */
    public static Map<String, String> skipRegistrationReasons() {
        Map<String, String> reasons = new LinkedHashMap<>();
        // ここを true/false 運用にしてもOK。初心者向けに理由テキスト形式にしています。
        reasons.put("client", "DedicatedServer向けビルドではクライアント処理を登録しない");
        reasons.put("gui", "サーバー専用構成ではGUI登録を省略");
        reasons.put("datagen", "通常ランタイムではDataGen登録を実行しない");
        return reasons;
    }
}
