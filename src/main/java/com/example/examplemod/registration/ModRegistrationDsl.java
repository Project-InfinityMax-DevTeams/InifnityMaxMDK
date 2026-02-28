package com.example.examplemod.registration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 初心者向けの登録DSLです。
 * <p>
 * 目的:
 * <ul>
 *     <li>文字列IDと数値を変えるだけでMOD独自要素を定義できる</li>
 *     <li>DSLに存在しない要素も {`@link` `#custom`(String, String, String, Consumer)} で追加できる</li>
 * </ul>
 */
public final class ModRegistrationDsl {
    private final List<GameElementDefinition> definitions = new ArrayList<>();

    /**
     * ブロックを登録します。
     * @param id 変更ポイント: ブロックID
     * @param implClass 変更ポイント: 実装クラス
     * @param spec 詳細設定
     */
    public void block(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.BLOCK, id, implClass, spec);
    }

    /** エンティティ登録。 */
    public void entity(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.ENTITY, id, implClass, spec);
    }

    /** ブロックエンティティ登録。 */
    public void blockEntity(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.BLOCK_ENTITY, id, implClass, spec);
    }

    /** クライアント初期化要素登録。 */
    public void client(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.CLIENT, id, implClass, spec);
    }

    /** GUI登録。 */
    public void gui(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.GUI, id, implClass, spec);
    }

    /** ディメンション登録。 */
    public void dimension(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.DIMENSION, id, implClass, spec);
    }

    /** バイオーム登録。 */
    public void biome(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.BIOME, id, implClass, spec);
    }

    /** アイテム登録。 */
    public void item(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.ITEM, id, implClass, spec);
    }

    /** パケット登録。 */
    public void packet(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.PACKET, id, implClass, spec);
    }

    /** DataGen登録。 */
    public void dataGen(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.DATAGEN, id, implClass, spec);
    }

    /**
     * DSLに無い種類を直感的に追加するための拡張ポイントです。
     * @param typeName 任意の種類名（例: "spell"）
     */
    public void custom(String typeName, String id, String implClass, Consumer<ElementSpec> spec) {
        if (spec == null) {
            throw new IllegalArgumentException("spec must not be null");
        }
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be null or blank");
        }
        if (implClass == null || implClass.isBlank()) {
            throw new IllegalArgumentException("implClass must not be null or blank");
        }
        if (typeName == null || typeName.isBlank()) {
            throw new IllegalArgumentException("typeName must not be null or blank");
        }
        ElementSpec elementSpec = new ElementSpec();
        elementSpec.implClass = implClass;
        spec.accept(elementSpec);
        if (elementSpec.implClass == null || elementSpec.implClass.isBlank()) {
            throw new IllegalArgumentException("implClass must not be null or blank after spec");
        }
        elementSpec.put("customType", typeName);
        definitions.add(new GameElementDefinition(GameElementType.CUSTOM, id, elementSpec.implClass, elementSpec.properties));
    }

    private void add(GameElementType type, String id, String implClass, Consumer<ElementSpec> spec) {
        if (spec == null) {
            throw new IllegalArgumentException("spec must not be null");
        }
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be null or blank");
        }
        if (implClass == null || implClass.isBlank()) {
            throw new IllegalArgumentException("implClass must not be null or blank");
        }
        ElementSpec elementSpec = new ElementSpec();
        elementSpec.implClass = implClass;
        spec.accept(elementSpec);
        if (elementSpec.implClass == null || elementSpec.implClass.isBlank()) {
            throw new IllegalArgumentException("implClass must not be null or blank after spec");
        }
        definitions.add(new GameElementDefinition(type, id, elementSpec.implClass, elementSpec.properties));
    }

    /** @return DSLで作成した登録定義一覧 */
    public List<GameElementDefinition> definitions() {
        return List.copyOf(definitions);
    }

    /**
     * 各要素の設定ビルダー。
     * <p>
     * 初心者は `set` の key/value を編集するだけでOKです。
     */
    public static final class ElementSpec {
        private String implClass;
        private final Map<String, Object> properties = new LinkedHashMap<>();

        /** 実装クラスを差し替えます。 */
        public ElementSpec impl(String value) {
            this.implClass = value;
            return this;
        }

        /** 任意設定を追加します。 */
        public ElementSpec set(String key, Object value) {
            properties.put(key, value);
            return this;
        }

        private void put(String key, Object value) {
            properties.put(key, value);
        }
    }
}
