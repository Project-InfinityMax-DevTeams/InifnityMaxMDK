package com.example.examplemod.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @brief Fabric専用の固有Mixin配置例です。 / Example of Fabric-specific Mixin placement. 
 * 
 * @details 変更ポイント / Change points:
 * 
 * @details    - 対象クラスを変える / Change target class: {@code @Mixin(TitleScreen.class)}
 * @details    - Inject処理を追加して独自ロジックへ拡張する / Add an Inject process to extend custom logic.
 * 
 */
@Mixin(TitleScreen.class)
public abstract class ExampleClientMixin {
    // 初心者向け / For beginners: まずは空Mixinとして配置し、必要なInjectを後から追加してください。 / Start by placing it as an empty Mixin then add the necessary Injects later.
}
