package com.example.examplemod.registration;

/**
 * DSLで扱うゲーム要素の種類です。
 * <p>
 * 初心者向けに、今回要求された対象を列挙しています。
 * 必要があれば {@link #CUSTOM} を使って新しい種別を追加できます。
 */
public enum GameElementType {
    /** ブロック。 */
    BLOCK,
    /** エンティティ。 */
    ENTITY,
    /** ブロックエンティティ。 */
    BLOCK_ENTITY,
    /** クライアント向け処理。 */
    CLIENT,
    /** GUI定義。 */
    GUI,
    /** ディメンション定義。 */
    DIMENSION,
    /** バイオーム定義。 */
    BIOME,
    /** アイテム定義。 */
    ITEM,
    /** パケット定義。 */
    PACKET,
    /** DataGen定義。 */
    DATAGEN,
    /** DSLにまだ無いものを拡張するための汎用種別。 */
    CUSTOM
}
