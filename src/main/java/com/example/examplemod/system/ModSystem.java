package com.example.examplemod.system;

/**
 * 純Javaで組めるMOD独自システムの最小インターフェースです。
 */
public interface ModSystem {
    /**
     * 初期化時に呼び出します。
     * @param context システム共有コンテキスト
     */
    void initialize(ModSystemContext context);

    /**
     * tickごとの処理。
     * @param context システム共有コンテキスト
     */
    void tick(ModSystemContext context);
}
