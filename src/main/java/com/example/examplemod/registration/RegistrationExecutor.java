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
     * 指定されたDSL定義の登録処理を順に実行する。
     *
     * @param definitions 登録対象の GameElementDefinition の一覧。各要素について登録処理が順番に実行される。
     */
    public void registerAll(List<GameElementDefinition> definitions) {
        for (GameElementDefinition definition : definitions) {
            // ここを各ローダーの登録APIに置換すれば本番登録が完成します。
            System.out.println("[REGISTER] " + definition.type() + " -> " + definition.id() + " => " + definition.implClass());
        }
    }

    /**
     * 登録をスキップする要素とその理由を挿入順を保ったマップとして提供する。
     *
     * <p>返されるマップは、JSON出力などで「どの要素を登録しないか」を明示するための
     * キー（要素識別子）と、初心者向けの理由テキスト（なぜスキップするか）を対応付けたものです。
     *
     * @return キーが要素識別子、値がそのスキップ理由であるマップ（挿入順を保持する）
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
