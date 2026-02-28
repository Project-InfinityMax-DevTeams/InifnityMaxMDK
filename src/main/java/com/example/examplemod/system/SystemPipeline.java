package com.example.examplemod.system;

import java.util.ArrayList;
import java.util.List;

/**
 * 複数の {@link ModSystem} を順番に実行する純Javaパイプラインです。
 * <p>
 * 初心者向け: `addSystem` に実装を追加するだけで拡張できます。
 */
public final class SystemPipeline {
    private final List<ModSystem> systems = new ArrayList<>();
    private final ModSystemContext context = new ModSystemContext();

    /**
     * システムを追加します。
     * @param system 追加するシステム
     */
    public void addSystem(ModSystem system) {
        systems.add(system);
    }

    /**
     * 登録されたすべての ModSystem に対して順番に初期化を実行します。
     *
     * 各システムには共有の ModSystemContext を渡します。
     */
    public void initialize() {
        for (ModSystem system : systems) {
            system.initialize(context);
        }
    }

    /**
     * 登録された全ての ModSystem に対して、1ティック分の処理を順番に実行します。
     *
     * <p>システムは登録された順序で呼び出され、各システムの tick(context) が実行されます。</p>
     */
    public void tick() {
        for (ModSystem system : systems) {
            system.tick(context);
        }
    }
}
