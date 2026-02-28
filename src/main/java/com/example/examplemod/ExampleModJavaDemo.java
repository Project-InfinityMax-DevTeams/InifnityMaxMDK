package com.example.examplemod;

import com.example.examplemod.bootstrap.ExampleModRegistrations;
import com.example.examplemod.registration.ModRegistrationDsl;
import com.example.examplemod.registration.RegistrationExecutor;
import com.example.examplemod.system.ModSystem;
import com.example.examplemod.system.ModSystemContext;
import com.example.examplemod.system.SystemPipeline;

/**
 * 初心者向けの統合デモです。
 * <p>
 * このクラスは以下を 1 か所で確認できます。
 * <ul>
 *     <li>ゲーム要素のDSL登録</li>
 *     <li>登録実行（置換ポイント明示）</li>
 *     <li>純JavaのMOD独自システム実行</li>
 * </ul>
 */
public final class ExampleModJavaDemo {
    /**
     * インスタンス化を防ぐためのプライベートコンストラクタ。
     *
     * <p>このクラスは静的メンバーのみを提供するデモ用ユーティリティであり、インスタンスを生成できません。</p>
     */
    private ExampleModJavaDemo() {
    }

    /**
     * モッドの初期化手順を示すエントリーポイントの例を実行します。
     *
     * <p>DSL定義を生成して登録し、純Java実装のシステム（ExampleEnergySystem）をパイプラインに追加して初期化・一刻分の進行を行います。</p>
     */
    public static void initializeMod() {
        // 1) DSL定義を生成（文字列・数値を変更するだけで流用可能）
        ModRegistrationDsl dsl = ExampleModRegistrations.createDsl();

        // 2) 定義を登録（本番はRegistrationExecutor内部を各ローダーAPIへ置換）
        new RegistrationExecutor().registerAll(dsl.definitions());

        // 3) 純Javaの独自システムを起動
        SystemPipeline pipeline = new SystemPipeline();
        pipeline.addSystem(new ExampleEnergySystem());
        pipeline.initialize();
        pipeline.tick();
    }

    /**
     * 純Javaで実装された独自システム例です。
     */
    public static final class ExampleEnergySystem implements ModSystem {
        /**
         * システムコンテキストに初期エネルギー値を設定する。
         *
         * 初期値としてキー `"energy"` に整数 `100` を格納する。初期値を変更するにはこの値を書き換える。
         *
         * @param context システムの状態を読み書きするコンテキスト
         */
        @Override
        public void initialize(ModSystemContext context) {
            // ここを変えると初期値を変更できます。
            context.put("energy", 100);
        }

        /**
         * 各ティックでコンテキスト内の "energy" 値を1減らす。
         *
         * コンテキストからキー `"energy"` を読み取り、整数であればその値を1減らして再格納する。値が存在しないか整数でない場合は0として扱い、0から1減らした結果を格納する。
         *
         * @param context 操作対象のシステムコンテキスト（"energy" キーの読み書きに使用する）
         */
        @Override
        public void tick(ModSystemContext context) {
            Object raw = context.get("energy");
            int energy = raw instanceof Integer ? (Integer) raw : 0;
            // ここを変えると毎tickの増減ロジックを変更できます。
            context.put("energy", energy - 1);
        }
    }
}
