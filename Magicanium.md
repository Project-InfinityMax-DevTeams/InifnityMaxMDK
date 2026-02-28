
# MagicaniumAPI

---

## 1. 概要

`Magicanium` は **魔力の本質** を表す不安定物質です。
物理エネルギーとは異なり、**「密度状態 × 情報 × 構造」** で魔術が決定されます。

* **密度**：空間や物体ごとに `ρ⁺`（正密度） / `ρ⁻`（負密度） を持つ
* **安定度**：密度が高いほど不安定になり、暴走しやすい
* **魔術**：密度差により放出された Magicanium に情報が付着して発現する

---

## 2. 基本量と数式

| 名称  | 記号 | 説明                             |
| --- | -- | ------------------------------ |
| 正密度 | ρ⁺ | 魔力を外へ放出する性質                    |
| 負密度 | ρ⁻ | 魔力を吸収する性質                      |
| 安定度 | σ  | `σ = 1 / (1 + λρ⁺)`、密度が高いほど不安定 |

### 2.1 密度平均化の法則

```
Δρ = ρ⁺_source − ρ⁺_target
M_flow = α × Δρ × σ_source
```

* 密度差が大きいほど流れる
* 不安定なほど流れやすい

---

### 2.2 魔術効力の法則

魔術の対象への効果 Φ:

```
Φ = κ × M_flow × e^(−γρ⁻_target)
Φ_final = Φ × (1 + log(1 + Iκ))
```

* κ：術式の構造係数
* I：情報ベクトル（多次元情報）
* ρ⁻：対象の負密度
* 負密度が高いと効きにくい

---

### 2.3 魔力吸収体

吸収可能量上限：

```
ρ⁻_max(total) = Σ ρ⁻_max
```

* 高負密度の物体は魔力を吸収
* 複数物体融合で上限が合算される

---

## 3. クラス設計

### 3.1 MagicalBody

```java
MagicalBody body = new MagicalBody(10.0, 2.0);
double rhoPos = body.getState().getRhoPositive();
```

* 魔力を持つ物体
* 正/負密度を保持
* 安定度計算 `getStability()`

---

### 3.2 MagicaniumState

```java
MagicaniumState state = body.getState();
state.addPositive(1.0); // 正密度増加
state.addNegative(0.5); // 負密度増加
```

* 密度操作用
* 安定度計算済み

---

### 3.3 MagicaniumField

```java
MagicaniumField field = new MagicaniumField();
field.addBody(body1);
field.addBody(body2);
field.tick(0.05); // deltaTime 秒だけ進める
```

* 複数の MagicalBody を保持
* 時間発展処理 (`tick`)
* 自然崩壊 + 密度平均化を実施

**自然崩壊式：**

```
dρ⁺/dt = -β ρ⁺ (1 - σ) + ε ρ⁺² - ζ ρ⁺³
dρ⁻/dt = -μ ρ⁻
```

* β, μ, ε, ζ は `WorldConstants`

---

### 3.4 DensityFlowEngine

```java
double flow = DensityFlowEngine.calculateFlow(source, target);
DensityFlowEngine.applyFlow(source, target);
```

* 2体間の密度流量計算
* 自動で正密度を減算/加算

---

### 3.5 InformationVector

```java
InformationVector info = new InformationVector();
info.add("fire", 1.0);
info.add("ice", 0.5);

double similarity = info.cosineSimilarity(otherInfo);
```

* 多次元魔術情報ベクトル
* 内積・コサイン類似度計算
* 魔術干渉や情報増幅で使用

---

### 3.6 SpellStructure

```java
SpellStructure spell = new SpellStructure(1.5, info);
```

* 魔術の構造係数 κ と情報ベクトル I を保持

---

### 3.7 SpellEngine

```java
double effect = SpellEngine.castSpell(caster, target, spell, targetResponse);
```

* 流れた魔力 × 構造係数 × 負密度減衰 × 情報増幅で最終効果を算出

---

### 3.8 SpellInterferenceEngine

```java
double totalEffect = SpellInterferenceEngine.applyInterference(
    effects, infos, positions
);
```

* 距離依存の魔術干渉を計算
* ベクトル共鳴・相殺を考慮
* 減衰 `exp(-distance/YOTA)`

---

### 3.9 MagicAbsorber

```java
MagicAbsorber absorber = new MagicAbsorber(5.0, 0.0, 20.0);
absorber.absorb(3.0);
```

* 吸収体クラス
* 最大負密度を超えない範囲で吸収

---

## 4. 使用例

```java
// 魔力体生成
MagicalBody caster = new MagicalBody(10.0, 1.0);
MagicAbsorber target = new MagicAbsorber(5.0, 0.0, 20.0);

// 魔術情報作成
InformationVector fireInfo = new InformationVector();
fireInfo.add("fire", 1.0);
SpellStructure fireSpell = new SpellStructure(1.2, fireInfo);

// 魔力場生成
MagicaniumField field = new MagicaniumField();
field.addBody(caster);
field.addBody(target);

// 魔術発動
double effect = SpellEngine.castSpell(caster, target, fireSpell, target.getState().getInfoVector());

// 魔力干渉
List<Double> effects = List.of(effect);
List<InformationVector> infos = List.of(fireInfo);
List<Vector3d> positions = List.of(new Vector3d(0,0,0), new Vector3d(1,0,0));

double totalEffect = SpellInterferenceEngine.applyInterference(effects, infos, positions);

// 時間発展
field.tick(0.05);
```

---

## 5. WorldConstants

| 定数      | 意味        | デフォルト値  |
| ------- | --------- | ------- |
| LAMBDA  | 不安定係数     | 0.1     |
| ALPHA   | 密度拡散係数    | 0.05    |
| GAMMA   | 魔術効力減衰    | 0.02    |
| BETA    | 正密度自然崩壊係数 | 0.03    |
| MU      | 負密度減衰係数   | 0.01    |
| DELTA   | 空間平均化係数   | 0.02    |
| EPSILON | 自己増殖係数    | 0.0005  |
| ZETA    | 過飽和崩壊係数   | 0.00001 |
| ETA     | 干渉係数      | 0.1     |
| YOTA    | 干渉減衰長     | 5.0     |