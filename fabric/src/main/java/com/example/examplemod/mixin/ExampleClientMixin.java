package com.example.examplemod.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;

/**
 * Fabric専用の固有Mixin配置例です。
 * <p>
 * 変更ポイント:
 * <ul>
 *     <li>対象クラスを変える: {@code @Mixin(TitleScreen.class)}</li>
 *     <li>Inject処理を追加して独自ロジックへ拡張する</li>
 * </ul>
 */
@Mixin(TitleScreen.class)
public abstract class ExampleClientMixin {
    // 初心者向け: まずは空Mixinとして配置し、必要なInjectを後から追加してください。
}
