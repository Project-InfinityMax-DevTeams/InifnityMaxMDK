package com.example.examplemod

import com.example.examplemod.bootstrap.ExampleModRegistrations
import com.example.examplemod.registration.RegistrationExecutor

/**
 * Kotlin から Java DSL を呼ぶ最小サンプルです。
 *
 * 変更箇所は [ExampleModRegistrations] 側の文字列・数値です。
 */
object ExampleModKotlinDemo {

    /**
     * Kotlinからモジュールを初期化し、DSLの定義を登録するエントリーポイント。
     *
     * DSLインスタンスを作成し、その定義をRegistrationExecutorに渡して全て登録する。
     */
    fun initializeFromKotlin() {
        val dsl = ExampleModRegistrations.createDsl()
        RegistrationExecutor().registerAll(dsl.definitions())
    }
}
