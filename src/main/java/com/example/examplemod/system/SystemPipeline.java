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

    /** 初期化を実行します。 */
    public void initialize() {
        for (ModSystem system : systems) {
            system.initialize(context);
        }
    }

    /** 1 tick 分の処理を実行します。 */
    public void tick() {
        for (ModSystem system : systems) {
            system.tick(context);
        }
    }
}
