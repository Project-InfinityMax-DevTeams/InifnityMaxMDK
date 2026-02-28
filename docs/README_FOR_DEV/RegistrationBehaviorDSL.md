# InfinityMaxAPI 登録DSL / 振る舞い接続DSL ガイド

このドキュメントは以下を説明します。

1. 書き方（基本構文）
2. DSLで登録・接続できるもの一覧
3. 各スコープで記述できる内容

---

## 1) コンセプト

InfinityMaxAPI は DSL を次の2層に分離します。

- **登録DSL (`registry {}`)**
  - オブジェクトそのものを登録する。
  - 例: item / block / entity / event / packet
- **振る舞い接続DSL (`behavior {}`)**
  - 登録済みIDに対してロジックを紐づける。
  - ロジック本体は Java のメソッド参照として渡す。
  - 例: block / item / entity / keybind / ui / packet

この分離により、
- データ定義（何を登録するか）
- 実行ロジック（何をさせるか）
を独立して保守できます。

---

## 2) 登録DSL (`registry {}`) の書き方

```kotlin
import com.yuyuto.infinitymaxapi.api.libs.registry

registry {
    item("copper_ingot", CopperIngot()) {
        stack = 64
        durability = 0
    }

    block("copper_block", CopperBlock()) {
        strength = 5.0f
        noOcclusion = false
    }

    entity<AnyEntityType, AnyCategory>("copper_golem", CopperGolemEntity()) {
        category = AnyCategory.CREATURE
        width = 0.8f
        height = 2.2f
    }

    event<SomeEvent> {
        priority = com.yuyuto.infinitymaxapi.api.event.EventPriority.HIGH
        async = false
        handler = java.util.function.Consumer { event ->
            // event handling
        }
    }

    packet<SyncEnergyPacket>("sync_energy") {
        direction = com.yuyuto.infinitymaxapi.api.libs.packet.PacketDirection.S2C
        decoder = { buf -> SyncEnergyPacket.decode(buf) }
        encoder = com.yuyuto.infinitymaxapi.api.libs.packet.SimplePacket.PacketEncoder { packet, buf ->
            packet.encode(buf)
        }
        handler = com.yuyuto.infinitymaxapi.api.libs.packet.PacketHandler { packet, ctx ->
            packet.handle(ctx)
        }
    }
}
```

### 登録DSLで扱える対象

- `item`
- `block`
- `entity`
- `event`
- `packet`

### 登録DSLで記述できる内容（概要）

- `item`: `stack`, `durability`, `tab`
- `block`: `strength`, `noOcclusion`
- `entity`: `category`, `width`, `height`
- `event`: `priority`, `async`, `handler`
- `packet`: `direction`, `decoder`, `encoder`, `handler`

> `build()` 呼び出しは不要です。DSL終了時に登録されます。

---

## 3) 振る舞い接続DSL (`behavior {}`) の書き方

```kotlin
import com.yuyuto.infinitymaxapi.api.libs.behavior
import com.yuyuto.infinitymaxapi.logic.BlockLogic
import com.yuyuto.infinitymaxapi.logic.PacketLogic

behavior {
    block("copper_block") {
        resourceId = "models/block/copper_block"
        phase = Phase.INTERACT
        meta("power_cost", 20)
        connector = BlockLogic::onCopperBlockInteract
    }

    item("copper_wand") {
        resourceId = "textures/item/copper_wand"
        phase = Phase.USE
        meta("cooldown", 40)
        connector = BlockLogic::onCopperWandUse
    }

    entity("copper_golem") {
        resourceId = "entities/copper_golem"
        phase = Phase.TICK
        connector = BlockLogic::onCopperGolemTick
    }

    keybind("open_energy_ui") {
        resourceId = "keybind/open_energy_ui"
        phase = Phase.PRESS
        connector = BlockLogic::onOpenEnergyUiKey
    }

    ui("energy_screen") {
        resourceId = "ui/energy_screen"
        phase = Phase.RENDER
        connector = BlockLogic::onEnergyScreenRender
    }

    packet<SyncEnergyPacket>("sync_energy") {
        resourceId = "network/sync_energy"
        phase = Phase.RECEIVE
        connector = PacketLogic::onSyncEnergyPacket
    }
}
```

### 振る舞い接続DSLで扱える対象

- `block`
- `item`
- `entity`
- `keybind`
- `ui`
- `packet`

### 振る舞い接続DSLで記述できる内容（共通）

- `resourceId`: 外部リソース識別子
- `phase`: 実行タイミング識別子（文字列）
- `meta(key, value)`: 任意メタデータ
- `connector`: Java 側メソッド参照（必須）

### packet スコープで記述できる内容

- 共通項目 + `connector`（`PacketBehaviorConnector<T>` 型）

---

## 4) Java ロジック実装例（メソッド参照先）

```java
package com.yuyuto.infinitymaxapi.logic;

import com.yuyuto.infinitymaxapi.api.libs.behavior.BehaviorContext;
import com.yuyuto.infinitymaxapi.api.libs.behavior.PacketBehaviorConnector;

public final class BlockLogic {

    private BlockLogic() {}

    public static void onCopperBlockInteract(BehaviorContext context) {
        // context.targetId(), context.resourceId(), context.metadata() などを使用
    }

    public static void onCopperWandUse(BehaviorContext context) {
        // item logic
    }

    public static void onCopperGolemTick(BehaviorContext context) {
        // entity logic
    }

    public static void onOpenEnergyUiKey(BehaviorContext context) {
        // keybind logic
    }

    public static void onEnergyScreenRender(BehaviorContext context) {
        // ui logic
    }
}
```

```java
package com.yuyuto.infinitymaxapi.logic;

import com.yuyuto.infinitymaxapi.api.libs.behavior.BehaviorContext;

public final class PacketLogic {

    private PacketLogic() {}
    // SyncEnergyPacket はユーザー定義のパケットクラス（例）
    public static void onSyncEnergyPacket(BehaviorContext context, SyncEnergyPacket packet) {
        // packet logic
    }
}
```

```java
package com.yuyuto.infinitymaxapi.example;

import com.yuyuto.infinitymaxapi.api.libs.Behavior;
import com.yuyuto.infinitymaxapi.logic.BlockLogic;
import com.yuyuto.infinitymaxapi.logic.PacketLogic;

public final class MyModInitializer {

    // mod初期化時（例: FabricならonInitialize、ForgeならFMLCommonSetupEvent）に呼び出す
    public static void init() {
        // Behavior.connect() でKotlin DSLにブリッジ
        Behavior.connect(scope -> {
            // block: BehaviorConnector を使用
            scope.block("copper_block", binding -> {
                binding.setResourceId("models/block/copper_block");
                binding.setPhase("interact");
                binding.meta("power_cost", 20);
                // BlockLogic::onCopperBlockInteract が BehaviorContext を受け取る
                binding.setConnector(BlockLogic::onCopperBlockInteract);
            });

            // item: 同様にメソッド参照を渡す
            scope.item("copper_wand", binding -> {
                binding.setResourceId("textures/item/copper_wand");
                binding.setPhase("use");
                binding.meta("cooldown", 40);
                binding.setConnector(BlockLogic::onCopperWandUse);
            });

            // entity
            scope.entity("copper_golem", binding -> {
                binding.setResourceId("entities/copper_golem");
                binding.setPhase("tick");
                binding.setConnector(BlockLogic::onCopperGolemTick);
            });

            // keybind
            scope.keybind("open_energy_ui", binding -> {
                binding.setResourceId("keybind/open_energy_ui");
                binding.setPhase("press");
                binding.setConnector(BlockLogic::onOpenEnergyUiKey);
            });

            // ui
            scope.ui("energy_screen", binding -> {
                binding.setResourceId("ui/energy_screen");
                binding.setPhase("render");
                binding.setConnector(BlockLogic::onEnergyScreenRender);
            });

            // packet: PacketBehaviorConnector<T> を使用
            scope.packet("sync_energy", binding -> {
                binding.setResourceId("network/sync_energy");
                binding.setPhase("receive");
                // PacketLogic::onSyncEnergyPacket は (BehaviorContext, SyncEnergyPacket) を受け取る
                binding.setConnector(PacketLogic::onSyncEnergyPacket);
            });
        });
    }
}
```
---

## 5) 設計ルール（推奨）

- 登録DSLには生成・登録のみを書く。
- 振る舞い接続DSLにはIDとロジックの紐づけのみを書く。
- ロジック本体は Java クラスに寄せる。
- 外部リソースとの対応関係は `resourceId` で明示する。
- `phase` は用途別にチーム内命名規約を決める（`init` / `tick` / `interact` など）。

