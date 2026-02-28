package com.example.examplemod;

import com.example.examplemod.customlogic.ModCommonLogic;
import com.example.examplemod.system.ModSystem;
import com.example.examplemod.system.ModSystemContext;
import com.example.examplemod.system.SystemPipeline;

/**
 * @brief Java側デモクラス。 / This is Java side Demo Class.
 * 
 * @details このクラスは「カスタムゲームロジック全般」をJavaで記述する方針に沿って、
 * @details 純Javaシステムと補助ロジックの利用例を示します。
 * @details This class demonstrates examples of pure Java systems and auxiliary logic usage,
 * @details following the policy of writing “all custom game logic” in Java.
 */
public final class ExampleModJavaDemo {
    private ExampleModJavaDemo() {
    }

    /**
     * @brief 純Javaシステムの起動例。 / Example of pure Java system launch.
     */
    public static void runJavaSystems() {
        SystemPipeline pipeline = new SystemPipeline();
        pipeline.addSystem(new ExampleEnergySystem());
        pipeline.initialize();
        pipeline.tick();

        // Kotlin DSLから呼ぶメソッドと同じロジックをJava単体でも実行できます。 / The same logic called from the Kotlin DSL can also be executed in Java standalone.
        ModCommonLogic.onItemUse("local-java-demo-context");
    }

    /**
     * @brief 純Javaで記述されたエネルギーシステム例。 / An example of an energy system writen in pure Java.
     */
    public static final class ExampleEnergySystem implements ModSystem {
        /** {@inheritDoc} */
        @Override
        public void initialize(ModSystemContext context) {
            // ここを任意の数値に置換可能 / This can be replaced with any value: 初期エネルギー / Init energy.
            context.put("energy", 100);
        }

        /** {@inheritDoc} */
        @Override
        public void tick(ModSystemContext context) {
            Object raw = context.get("energy");
            int energy = raw instanceof Integer ? (Integer) raw : 0;
            // ここを任意の数値に置換可能 / This can be replaced with any value: 1tickあたりの減衰値 / Damping value per every ticks.
            context.put("energy", energy - 1);
        }
    }
}
