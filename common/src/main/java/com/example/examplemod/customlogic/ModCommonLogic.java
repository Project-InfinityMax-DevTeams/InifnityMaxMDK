package com.example.examplemod.customlogic;

import com.example.examplemod.customlogic.util.ModDeveloperMathUtil;
import com.example.examplemod.customlogic.util.ModDeveloperTextUtil;
import com.yuyuto.infinitymaxapi.api.libs.behavior.BehaviorContext;

/**
 * @brief   Mod開発者が純Javaでカスタムゲームロジックを書くための集約クラス。 / An aggregate class for mod developers to write custom game logic in pure Java.
 * 
 * @details Kotlin DSL からこのクラスのメソッド参照を行い、
 * @details ゲーム要素とロジックを接続します。
 * @details From the Kotlin DSL, reference this class's methods to connect game elements and logic.
 */
public final class ModCommonLogic {

    private ModCommonLogic() {
    }

    /**
     * @brief ブロック操作時の処理例。 / Example of processing during block operations.
     * @param context ローダー側で渡される任意コンテキスト / Arbitrary context passed by the loader side.
     */
    public static void onBlockInteract(BehaviorContext context) {
        // ここを任意の数値に変更 / Change this to any desired value: 消費エネルギー / Consume energy.
        int cost = 20;
        int result = ModDeveloperMathUtil.clamp(cost * 2, 0, 9999);
        System.out.println(ModDeveloperTextUtil.prefix("BlockInteract") + " result=" + result + ", ctx=" + context);
    }

    /**
     * @brief アイテム使用時の処理例。 / Example of item usage processing.
     * @param context ローダー側で渡される任意コンテキスト / Arbitrary context passed by the loader side.
     */
    public static void onItemUse(BehaviorContext context) {
        // ここを任意文字列に変更 / Change this to any string: ログでの表示名 / Log Display Name.
        String display = "ExampleItem";
        System.out.println(ModDeveloperTextUtil.prefix(display) + " used, ctx=" + context);
    }

    /**
     * @brief エンティティtick処理例。 / Example of entity tick processing.
     * @param context ローダー側で渡される任意コンテキスト / Arbitrary context passed by the loader side.
     */
    public static void onEntityTick(BehaviorContext context) {
        System.out.println(ModDeveloperTextUtil.prefix("EntityTick") + " ctx=" + context);
    }

    /**
     * @brief クライアントキー入力時処理例。 / Example of processing when entering a client key.
     * @param context ローダー側で渡される任意コンテキスト / Arbitrary context passed by the loader side.
     */
    public static void onClientKeyPress(BehaviorContext context) {
        System.out.println(ModDeveloperTextUtil.prefix("ClientKey") + " ctx=" + context);
    }

    /**
     * @brief GUI描画時処理例。 / Example of GUI render.
     * @param context ローダー側で渡される任意コンテキスト / Arbitrary context passed by the loader side.
     */
    public static void onGuiRender(BehaviorContext context) {
        System.out.println(ModDeveloperTextUtil.prefix("GuiRender") + " ctx=" + context);
    }

    /**
     * `@brief` パケット受信時処理例。 / Example of packet reception processing.
     * `@param` context ローダー側で渡される任意コンテキスト / Arbitrary context passed by the loader side.
     * `@param` packet 受信パケット / Received packet.
     */
    public static void onPacketReceived(BehaviorContext context, Object packet) {
        System.out.println(ModDeveloperTextUtil.prefix("Packet") + " packet=" + packet + ", ctx=" + context);
    }
}
