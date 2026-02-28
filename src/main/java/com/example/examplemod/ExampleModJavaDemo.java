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
    private ExampleModJavaDemo() {
    }

    /**
     * MOD初期化時に呼ぶ想定のエントリーポイント例です。
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
        System.out.println("[Demo] Pipeline initialized");
        pipeline.tick();
        System.out.println("[Demo] Pipeline tick completed");
    }

    /**
     * 純Javaで実装された独自システム例です。
     */
    public static final class ExampleEnergySystem implements ModSystem {
        /** {@inheritDoc} */
        @Override
        public void initialize(ModSystemContext context) {
            // ここを変えると初期値を変更できます。
            context.put("energy", 100);
        }

        /** {@inheritDoc} */
        @Override
        public void tick(ModSystemContext context) {
            Object raw = context.get("energy");
            int energy = raw instanceof Integer ? (Integer) raw : 0;
            // ここを変えると毎tickの増減ロジックを変更できます。
            context.put("energy", Math.max(0, energy - 1));
        }
    }
}
