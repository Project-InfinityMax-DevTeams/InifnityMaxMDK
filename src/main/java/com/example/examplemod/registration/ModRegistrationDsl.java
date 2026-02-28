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
 *     <li>DSLに存在しない要素も {@link #custom(String, String, Consumer)} で追加できる</li>
 * </ul>
 */
public final class ModRegistrationDsl {
    private final List<GameElementDefinition> definitions = new ArrayList<>();

    /**
     * ブロック要素をDSLに登録する。
     *
     * @param id 登録するブロックの識別子
     * @param implClass ブロックの実装クラス名（通常は完全修飾名）
     * @param spec 要素の実装クラスや追加プロパティを設定するためのElementSpecコンシューマ
     */
    public void block(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.BLOCK, id, implClass, spec);
    }

    /**
     * エンティティ要素をDSLに登録する。
     *
     * @param id 登録する要素の識別子
     * @param implClass 要素の実装クラス名（完全修飾名や参照名）
     * @param spec ElementSpecを受け取り要素固有の設定を行うConsumer
     */
    public void entity(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.ENTITY, id, implClass, spec);
    }

    /**
     * ブロックエンティティをDSLに登録する。
     *
     * @param id        登録する要素の識別子（文字列ID）
     * @param implClass 実装クラス名または実装を示す識別子
     * @param spec      要素ごとの設定を受け取るビルダー（impl クラスやプロパティを指定するための Consumer）
     */
    public void blockEntity(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.BLOCK_ENTITY, id, implClass, spec);
    }

    /**
     * クライアント用の初期化要素をDSLに登録する。
     *
     * 指定したIDと実装クラスでクライアント要素の定義を作成し、`spec` で追加の実装クラス上書きやプロパティを設定する。
     *
     * @param id 登録に使う要素の識別子
     * @param implClass 要素の実装クラス（完全修飾名）
     * @param spec 要素の追加設定を行う `ElementSpec` を受け取る `Consumer`
     */
    public void client(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.CLIENT, id, implClass, spec);
    }

    /**
     * GUI要素を登録する。
     *
     * @param id        登録するGUI要素の識別子（文字列ID）
     * @param implClass GUI要素の実装クラス名（フルクラス名を想定）
     * @param spec      要素ごとの追加設定を行う `ElementSpec` を受け取るコンシューマ
     */
    public void gui(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.GUI, id, implClass, spec);
    }

    /**
     * 指定したIDと実装クラスでディメンションを登録する。
     *
     * @param id 登録に使用する要素の識別子
     * @param implClass 要素の実装クラス名（完全修飾名を想定）
     * @param spec 要素固有の設定を行うための {@link ElementSpec} を受け取るコンシューマ
     */
    public void dimension(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.DIMENSION, id, implClass, spec);
    }

    /**
     * バイオーム要素を登録する。
     *
     * 指定したIDと実装クラスでバイオームを登録し、specで要素固有の設定を行う。
     *
     * @param id 登録する要素の識別ID
     * @param implClass 実装クラスの完全修飾名
     * @param spec 要素設定を行うためのElementSpecを受け取るコンシューマ
     */
    public void biome(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.BIOME, id, implClass, spec);
    }

    /**
     * アイテム要素をDSLに登録する。
     *
     * @param id 登録に使う要素の文字列ID
     * @param implClass 要素の実装クラス名（完全修飾名を想定）
     * @param spec 要素固有の追加設定を行うためのビルダー受け取り
     */
    public void item(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.ITEM, id, implClass, spec);
    }

    /**
     * DSLにパケット要素を登録する。
     *
     * @param id パケットの識別ID
     * @param implClass 実際の実装クラスの完全修飾名
     * @param spec ElementSpecを受け取り実装クラスや追加プロパティを設定するための処理
     */
    public void packet(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.PACKET, id, implClass, spec);
    }

    /**
     * データ生成用の要素を登録する。
     *
     * @param id 登録する要素の識別子
     * @param implClass 要素の実装クラス名（完全修飾名を想定）
     * @param spec 要素の設定を行う `ElementSpec` を受け取る `Consumer`
     */
    public void dataGen(String id, String implClass, Consumer<ElementSpec> spec) {
        add(GameElementType.DATAGEN, id, implClass, spec);
    }

    /**
     * DSLに含まれない要素種類を追加するための拡張ポイントを提供します。
     *
     * 指定した種類名で要素定義を作成し、Consumerで与えたElementSpecの設定を適用します。
     * 指定した種類名は要素のプロパティにキー `"customType"` として格納されます。
     *
     * @param typeName 任意の種類名（例: "spell"）
     * @param id 登録する要素の識別子
     * @param spec 要素の実装クラスや追加プロパティを設定するためのコンシューマ（ElementSpec）
     */
    public void custom(String typeName, String id, Consumer<ElementSpec> spec) {
        ElementSpec elementSpec = new ElementSpec();
        spec.accept(elementSpec);
        elementSpec.put("customType", typeName);
        definitions.add(new GameElementDefinition(GameElementType.CUSTOM, id, elementSpec.implClass, elementSpec.properties));
    }

    /**
     * 指定した種類とIDでゲーム要素の定義を作成して内部の定義一覧に追加する。
     *
     * @param type 登録する要素の種類
     * @param id 登録に使用する識別文字列
     * @param implClass デフォルトの実装クラス名
     * @param spec 要素の実装クラスや追加プロパティを設定するための {@link ElementSpec} を受け取る処理。設定された値が作成される定義に反映される。
     */
    private void add(GameElementType type, String id, String implClass, Consumer<ElementSpec> spec) {
        ElementSpec elementSpec = new ElementSpec();
        elementSpec.implClass = implClass;
        spec.accept(elementSpec);
        definitions.add(new GameElementDefinition(type, id, elementSpec.implClass, elementSpec.properties));
    }

    /**
     * DSLで定義されたゲーム要素の登録定義一覧を取得する。
     *
     * @return 作成済みの GameElementDefinition を登録順で保持する不変のリスト。
     */
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

        /**
         * 要素の実装クラス名を設定する。
         *
         * @param value 実装クラスの完全修飾名（例: "com.example.MyImpl"）
         * @return この ElementSpec のインスタンス（メソッドチェーン用）
         */
        public ElementSpec impl(String value) {
            this.implClass = value;
            return this;
        }

        /**
         * 要素に任意のプロパティを追加または上書きします。
         *
         * @param key   プロパティ名（キー）
         * @param value プロパティの値
         * @return      この ElementSpec インスタンス（メソッドチェーン用）
         */
        public ElementSpec set(String key, Object value) {
            properties.put(key, value);
            return this;
        }

        /**
         * propertiesマップに指定したキーと値を格納する。
         *
         * @param key   格納するキー
         * @param value キーに対応する値
         */
        private void put(String key, Object value) {
            properties.put(key, value);
        }
    }
}
