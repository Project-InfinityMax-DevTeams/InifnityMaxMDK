# Physics API ガイド

このドキュメントは `gamelibs/physics` の基本的な使い方をまとめたものです。  

---

## 1. 物理状態 `PhysicalState` とは

`PhysicalState` は以下をまとめた「1セル分の物理状態」です。

- 温度 `Temperature`
- 圧力 `Pressure`
- 密度 `Density`
- 内部エネルギー `Energy`
- 相（固体/液体/気体/プラズマ） `Phase`
- 質量 `Mass`

### 作成例

```java
Temperature temp = new Temperature(300.0, Temperature.KELVIN);
Pressure pressure = new Pressure(101325.0, Pressure.PASCAL);
Density density = new Density(1.2, Density.KG_PER_M3);
Energy energy = new Energy(1000.0, Energy.JOULE);
Mass mass = new Mass(1.0, Mass.KILOGRAM);

PhysicalState state = new PhysicalState(
        temp,
        pressure,
        density,
        energy,
        PhaseResolver.resolve(temp),
        mass
);
```

---

## 2. ジュール熱 JouleHeating の使い方

JouleHeating は電流による抵抗損失（I²R）から
物体の温度上昇を計算するためのAPIです。

### 🔬 物理モデル

内部では以下の式を使用します：

P = I²R
Q = P × Δt
ΔT = Q / (m c)

- I : 電流（アンペア）
- R : 抵抗（オーム）
- Δt : 経過時間（秒）
- m : 物体質量（kg）
- c : 比熱（J/kgK）

### 🔌 推奨使用例（EnergyAPI連携）
```
EnergyNode a = new EnergyNode();
EnergyNode b = new EnergyNode();

a.setPotential(12.0);  // 12V
b.setPotential(0.0);   // 0V

// 抵抗2Ω → I = 12 / 2 = 6A
EnergyConnection wire = new EnergyConnection(a, b, 2.0);

JouleHeating heating = new JouleHeating(
        wire,
        4200.0 // 比熱（例：水）
);

// 0.1秒分発熱計算
PhysicalState next = heating.apply(state, 0.1);
```

### 🔥 数値の目安

比熱の目安

|物質|比熱 J/(kgK)|
|---|-----------|
|鉄|450|
|銅|385|
|水|4200|
|空気|1005|

抵抗の目安

|機械|抵抗|
|---|---|
|太い銅線|0.01〜0.1Ω|
|小型ヒーター|2〜10Ω|
|魔導回路|任意設定|

### 🏭 想定ユースケース

** 🔧 機械の発熱　**
- 発電機
- モーター
- コンピュータ
- 魔導炉


### 🔥 過熱判定
```
if (state.getTemperature().getSI() > 1200) {
    explode();
}
```

### ❄ 冷却との併用

JouleHeating と HeatTransfer を組み合わせることで：
- 発熱
- 冷却
- 熱暴走
- 自然平衡
が再現できる。

### 🏭 機械設計例

小型ヒーター
```
resistance = 5Ω
voltage = 24V

I = 24 / 5 = 4.8A
P = 4.8² × 5 ≈ 115W
```
→ 約115J/秒の発熱

魔導コイル（危険）
```
resistance = 1Ω
voltage = 100V

I = 100A
P = 10000W
```
→ 10kJ/秒 → 即過熱

---
## 3. 熱伝導 `HeatTransfer`

`HeatTransfer` はフーリエの法則ベースの簡易モデルです。

```java
HeatTransfer transfer = new HeatTransfer(
        0.6,   // 熱伝導率
        1.0,   // 面積
        0.1,   // 距離
        4200.0 // 比熱
);

PhysicalState cooled = transfer.apply(state, 0.05);
```

---

## 4. 相判定 `PhaseResolver`

材質がある場合：

```java
Material ironLike = new Material(1811.0, 3134.0, 12000.0);
Phase phase = PhaseResolver.resolve(ironLike, temp);
```

材質未指定（簡易判定）：

```java
Phase phase = PhaseResolver.resolve(temp);
```

> ⚠️ 注意: 材質未指定の場合、デフォルト材質（水相当：融点0℃（273.15 K）、沸点100℃（373.15 K））で判定されます。

---

## 5. 実装の注意点

- `Energy` は SI 単位ジュール（J）です。
- `PhysicalState` は immutable（更新時は新インスタンスを返す）です。
- 時間刻み `deltaTime` は「秒」で渡してください。

##6.実際の使用コード

わからない場合はコレをみて実装していけば良いかなと思います。なおこれは一例です。
```Java
package com.yuyuto.infinitymax.object;

import com.yuyuto.infinitymaxapi.gamelibs.physics.*;

/**
 * FireProjectile
 *
 * <p>
 * 高温物質を持つ投射体（火球など）を表す物理オブジェクト。
 * </p>
 *
 * <h2>特徴</h2>
 * <ul>
 *     <li>放物運動（重力あり）</li>
 *     <li>高温状態を保持</li>
 *     <li>時間経過で冷却</li>
 *     <li>温度に応じて相（Phase）を再計算</li>
 * </ul>
 *
 * <h2>単位系</h2>
 * <ul>
 *     <li>長さ: 1m = 1block</li>
 *     <li>速度: m/s</li>
 *     <li>質量: kg</li>
 *     <li>温度: Kelvin</li>
 *     <li>エネルギー: Joule</li>
 * </ul>
 *
 * <h2>数値の意味</h2>
 * <ul>
 *     <li>1800K = 非常に高温の火炎（溶岩級）</li>
 *     <li>0.5kg = 小型火球想定</li>
 *     <li>5000J = 衝突時のエネルギー基準</li>
 *     <li>50K/sec 冷却 = 空気中での簡易冷却モデル</li>
 * </ul>
 */
public class FireProjectile extends PhysicsObject {

    /**
     * 火球を生成するコンストラクタ
     *
     * @param startPos  初期位置（block単位）
     * @param direction 発射方向（正規化不要）
     * @param speed     初速（m/s）
     * @param material  使用する物質特性
     */
    public FireProjectile(Vector3 startPos,
                          Vector3 direction,
                          double speed,
                          Material material) {

        super(
                // --- 運動成分 ---
                new MotionComponent(
                        startPos,
                        // 方向ベクトルを正規化し、速度を掛ける
                        direction.normalize().multiply(speed),
                        MovementType.PROJECTILE // 重力あり
                ),

                // --- 物理状態 ---
                createFireState(material)
        );
    }

    /**
     * 火球の初期物理状態を作成する
     *
     * @param material 材料特性（融点・沸点など）
     * @return 初期PhysicalState
     */
    private static PhysicalState createFireState(Material material) {

        double initialTemperature = 1800; // K
        double initialPressure = 101325;  // Pa（標準大気圧）
        double initialDensity = 0.8;      // kg/m3（高温気体想定）
        double initialEnergy = 5000;      // J
        double initialMass = 0.5;         // kg

        return new PhysicalState(
                new Temperature(initialTemperature, Temperature.KELVIN),
                new Pressure(initialPressure, Pressure.PASCAL),
                new Density(initialDensity, Density.KG_PER_M3),
                new Energy(initialEnergy, Energy.JOULE),

                // 温度に基づき相を決定
                PhaseResolver.resolve(
                        material,
                        new Temperature(initialTemperature, Temperature.KELVIN)
                ),

                new Mass(initialMass, Mass.KILOGRAM),
                material
        );
    }

    /**
     * 毎tick呼ばれる物理更新処理
     *
     * @param deltaTime 経過時間（秒）
     */
    @Override
    protected void onPhysicsUpdate(double deltaTime) {

        // ----------------------------
        // 1. 冷却処理
        // ----------------------------
        double currentTemp = physicalState.getTemperature().getSI();

        // 50K/秒の簡易冷却モデル
        double cooledTemp = currentTemp - (50 * deltaTime);

        // 温度が絶対零度未満にならないよう制限
        if (cooledTemp < 0) {
            cooledTemp = 0;
        }

        Temperature newTemp =
                new Temperature(cooledTemp, Temperature.KELVIN);

        physicalState = physicalState.withTemperature(newTemp);

        // ----------------------------
        // 2. 相の再計算
        // ----------------------------
        Phase newPhase = PhaseResolver.resolve(
                physicalState.getMaterial(),
                newTemp
        );

        physicalState = physicalState.withPhase(newPhase);

        // ----------------------------
        // 3. 追加拡張ポイント
        // ----------------------------
        // ・一定温度以下で消滅
        // ・エネルギー減衰
        // ・衝突判定時に爆発
    }
}
```