package com.example.examplemod.dsl

import com.example.examplemod.customlogic.ModCommonLogic
import com.yuyuto.infinitymaxapi.api.libs.Phase
import com.yuyuto.infinitymaxapi.api.libs.behavior
import com.yuyuto.infinitymaxapi.api.libs.registry

/**
 * InfinityMaxAPI の登録DSL/振る舞いDSLを利用してゲーム要素を登録するクラスです。
 *
 * 重要:
 * - 文字列IDや数値を置き換えるだけで、独自MOD向け定義へ変更できます。
 * - このクラスは「登録とJavaロジックへの紐づけ」専用です。
 */
object InfinityMaxDslRegistration {

    /**
     * InfinityMaxAPI が提供する registry DSL で要素登録を実行します。
     */
    fun registerWithInfinityMaxDsl() {
        registry {
            // ここを任意の文字列に変更: アイテムID
            item("example_item", Any()) {
                // ここを任意の数値に変更: スタック数
                stack = 64
                durability = 0
            }

            // ここを任意の文字列に変更: ブロックID
            block("example_block", Any()) {
                // ここを任意の数値に変更: 強度
                strength = 3.5f
                noOcclusion = false
            }

            // ここを任意の文字列に変更: エンティティID
            entity<Any, Any>("example_entity", Any()) {
                // ここを列挙型の任意値に変更: カテゴリ
                category = null
                width = 0.8f
                height = 1.95f
            }

            // ここを任意の文字列に変更: パケットID
            packet<Any>("example_packet") {
                // パケット方向などは利用API実装に合わせて置換してください。
            }
        }
    }

    /**
     * InfinityMaxAPI の behavior DSL で、登録済み要素へ Java カスタムロジックを紐づけます。
     */
    fun bindBehaviors() {
        behavior {
            block("example_block") {
                resourceId = "models/block/example_block"
                phase = Phase.INTERACT
                // ここを任意の数値に変更: 消費値
                meta("power_cost", 20)
                connector = ModCommonLogic::onBlockInteract
            }

            item("example_item") {
                resourceId = "textures/item/example_item"
                phase = Phase.USE
                // ここを任意の数値に変更: クールダウン
                meta("cooldown", 40)
                connector = ModCommonLogic::onItemUse
            }

            entity("example_entity") {
                resourceId = "entities/example_entity"
                phase = Phase.TICK
                connector = ModCommonLogic::onEntityTick
            }

            keybind("open_example_gui") {
                resourceId = "keybind/open_example_gui"
                phase = Phase.PRESS
                connector = ModCommonLogic::onClientKeyPress
            }

            ui("example_screen") {
                resourceId = "ui/example_screen"
                phase = Phase.RENDER
                connector = ModCommonLogic::onGuiRender
            }

            packet<Any>("example_packet") {
                resourceId = "network/example_packet"
                phase = Phase.RECEIVE
                connector = ModCommonLogic::onPacketReceived
            }
        }
    }
}
