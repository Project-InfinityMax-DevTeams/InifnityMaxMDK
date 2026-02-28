# ElectricalAPI – Usage Guide

## 1. Overview

ElectricalAPI is a resistive electrical network simulation API based on **Nodal Analysis**.

### Features

* Resistive network simulation
* Current source support
* Internal-resistance voltage source (`ChargedEnergyNode`)
* Transmission loss calculation (I²R)
* Overcurrent / breakdown protection (`ConnectionState`)
* Time-based simulation using `deltaTime`

The solver computes node voltages by solving:

```
G * V = I
```

Where:

* **G** = Conductance matrix (1 / R)
* **V** = Node voltage vector
* **I** = Injected current vector

Gaussian elimination is used internally.

---

# 2. Basic Usage

Simulation consists of five simple steps.

---

## Step 1. Create the solver and network

```java
EnergySolver solver = new MatrixEnergySolver();
EnergyNetwork network = new EnergyNetwork(solver);
```

---

## Step 2. Create nodes

### Basic node

```java
EnergyNode nodeA = new BasicEnergyNode();
EnergyNode nodeB = new BasicEnergyNode();
```

### Voltage source (battery)

```java
ChargedEnergyNode battery = new SimpleBatteryNode(
        1000.0,   // initial charge
        0.05      // internal resistance (Ohm)
);
```

`ChargedEnergyNode` behaves as a voltage source with internal resistance.

---

## Step 3. Add nodes to the network

```java
network.addNode(nodeA);
network.addNode(nodeB);
network.addNode(battery);
```

---

## Step 4. Connect nodes

```java
network.connect(
        battery,
        nodeA,
        2.0,     // resistance (Ohm)
        50.0,    // max current
        120.0,   // breakdown voltage
        500.0    // loss threshold
);
```

Each connection automatically creates a `ConnectionState`
that monitors electrical safety conditions.

---

## Step 5. Advance the simulation

```java
double deltaTime = 0.05; // 50 ms
network.tick(deltaTime);
```

This will:

* Solve node voltages
* Compute currents
* Update battery charge
* Apply transmission loss
* Evaluate connection protection logic

---

# 3. Reading Node Data

```java
double voltage = nodeA.getPotential();
double injectedCurrent = nodeA.getInjectedCurrent();
```

---

# 4. Battery Behavior

`ChargedEnergyNode` internally models:

```
I = (Vnode - Vsource) / Rinternal
```

* Voltage is derived from stored charge
* Charge decreases when current flows out
* Internal resistance limits current

The battery automatically updates its charge using:

```
charge += -totalCurrent * deltaTime
```

---

# 5. Connection Protection

Each connection has a `ConnectionState` that monitors:

* Overcurrent
* Breakdown voltage
* Excessive transmission loss

Example:

```java
for (ConnectionState state : network.getConnectionStates()) {
    if (!state.isActive()) {
        System.out.println("Connection disabled.");
    }
}
```

If protection conditions are exceeded, the connection becomes inactive.

---

# 6. Ground Handling

The solver automatically fixes:

```
V₀ = 0
```

One node is used as ground to avoid a singular matrix.

---

# 7. Recommended Usage Patterns

* One `EnergyNetwork` = one circuit
* Call `tick()` once per game update
* Use seconds for `deltaTime`
* Never use zero resistance (infinite current risk)

---

# 8. Minimal Example

```java
EnergySolver solver = new MatrixEnergySolver();
EnergyNetwork net = new EnergyNetwork(solver);

ChargedEnergyNode battery = new SimpleBatteryNode(1000, 0.1);
EnergyNode lamp = new BasicEnergyNode();

net.addNode(battery);
net.addNode(lamp);

net.connect(battery, lamp, 5.0, 20.0, 200.0, 1000.0);

net.tick(0.05);

System.out.println("Lamp Voltage: " + lamp.getPotential());
```
---
# ElectricalAPI – Japanese Usage Guide

## 1. 概要

ElectricalAPI はノード解析（Nodal Analysis）を用いた
抵抗ベースの電気ネットワークシミュレーションAPIです。

特徴：

* 抵抗ネットワーク対応
* 電流源対応
* 内部抵抗付き電圧源（ChargedEnergyNode）
* 送電損失（I²R）計算
* 過電流・過電圧による接続遮断（ConnectionState）
* 時間発展対応（deltaTime）

---

# 2. 基本的な流れ

使用手順はたったの5ステップです。

---

## Step 1. ソルバーを作成

```java
EnergySolver solver = new MatrixEnergySolver();
EnergyNetwork network = new EnergyNetwork(solver);
```

---

## Step 2. ノードを作成

### 通常ノード

```java
EnergyNode nodeA = new BasicEnergyNode();
EnergyNode nodeB = new BasicEnergyNode();
```

### 電圧源ノード（バッテリー）

```java
ChargedEnergyNode battery = new SimpleBatteryNode(
        1000.0,   // 初期電荷
        0.05      // 内部抵抗
);
```

---

## Step 3. ノードをネットワークへ追加

```java
network.addNode(nodeA);
network.addNode(nodeB);
network.addNode(battery);
```

---

## Step 4. ノードを接続

```java
network.connect(
        battery,
        nodeA,
        2.0,     // 抵抗(Ω)
        50.0,    // 最大電流
        120.0,   // 絶縁破壊電圧
        500.0    // 損失しきい値
);
```

---

## Step 5. シミュレーションを進める

```java
double deltaTime = 0.05; // 50ms

network.tick(deltaTime);
```

これで：

* 各ノードの電圧が更新される
* 電流が計算される
* バッテリーの電荷が変化する
* 送電損失が適用される
* 過電流・過電圧が評価される

---

# 3. ノード情報の取得

```java
double voltage = nodeA.getPotential();
double injected = nodeA.getInjectedCurrent();
```

---

# 4. バッテリーの動作

ChargedEnergyNode は：

* 電荷 → 電圧へ変換
* 内部抵抗付き電圧源として動作
* 放電すると電圧が低下

内部では：

```
I = (Vnode - Vsource) / Rinternal
```

で計算されます。

---

# 5. 接続の状態管理

各接続は ConnectionState を持ちます。

以下を監視します：

* 最大電流超過
* 絶縁破壊電圧超過
* 損失しきい値超過

状態取得：

```java
for (ConnectionState state : network.getConnectionStates()) {
    if (!state.isActive()) {
        System.out.println("Connection disabled!");
    }
}
```

---

# 6. シミュレーションモデル

ElectricalAPI は以下の式を解きます：

```
G * V = I
```

* G = コンデクタンス行列（1/R）
* V = ノード電圧
* I = 注入電流ベクトル

ガウス消去法で毎tick解きます。

---

# 7. 推奨設計

✔ 1ネットワーク = 1電気回路
✔ tick はゲームの1フレーム毎に呼ぶ
✔ deltaTime は秒単位で渡す
✔ 抵抗0は禁止（∞電流になる）

---

# 8. 例：最小回路

```java
EnergySolver solver = new MatrixEnergySolver();
EnergyNetwork net = new EnergyNetwork(solver);

ChargedEnergyNode battery = new SimpleBatteryNode(1000, 0.1);
EnergyNode lamp = new BasicEnergyNode();

net.addNode(battery);
net.addNode(lamp);

net.connect(battery, lamp, 5.0, 20.0, 200.0, 1000.0);

net.tick(0.05);

System.out.println("Lamp Voltage: " + lamp.getPotential());
```