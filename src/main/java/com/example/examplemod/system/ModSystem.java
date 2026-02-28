package com.example.examplemod.system;

/**
 * 純Javaで組めるMOD独自システムの最小インターフェースです。
 */
public interface ModSystem {
    /**
 * システムの初期化処理を行う。
 *
 * @param context 初期化時に使用する、システム全体で共有されるコンテキスト
 */
    void initialize(ModSystemContext context);

    /**
 * 毎ティックごとにシステムの更新処理を実行する。
 *
 * システムの状態更新や定期実行が必要な処理をここで行う。
 *
 * @param context システム間で共有されるコンテキスト。状態やサービスへアクセスするために使用する。
 */
    void tick(ModSystemContext context);
}
