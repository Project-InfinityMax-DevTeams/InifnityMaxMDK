# Infinity Event API

---

## 1. EventAPI概要

Infinity Event API は、

* 型安全なイベント配信
* 優先度制御
* キャンセル機構
* 継承イベント対応
* MOD単位管理対応

を目的とした拡張性重視のイベントシステムです。

## 2. 基本構造

```id="f3n8ya"
InfinityEvent（基底クラス）
 ├── CancelableEvent（キャンセル可能）
 ├── PlayerEvent
 │     ├── PlayerJoinEvent
 │     └── PlayerQuitEvent
 └── WorldEvent
```

---

### コアクラス

| クラス                      | 役割           |
| ------------------------ | ------------ |
| InfinityEvent            | 全イベントの基底     |
| InfinityEventListener<T> | リスナーインターフェース |
| InfinityEventBus         | イベント管理       |
| EventPriority            | 優先度列挙型       |

---

## 3. 優先度仕様

### EventPriority

```id="9c8kpa"
HIGH
NORMAL
LOW
```

実行順：

```id="u6m3cz"
HIGH → NORMAL → LOW
```

登録時にソートされるため、
post時は高速処理されます。

---

## 4. CancelableEvent仕様

### CancelableEvent

キャンセル可能なイベントは以下を継承します：

```id="i3w91f"
public abstract class CancelableEvent extends InfinityEvent {
    private boolean cancelled;

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
```

---

### 動作

* setCancelled(true) でキャンセル
* 以降のリスナー実行停止
* post()は即return

---

## 5. 継承イベント仕様

### 継承対応

EventBusはクラス階層を遡ってリスナーを実行します。

例：

```id="k0m6zx"
register(PlayerEvent.class, ...)
```

は

* PlayerJoinEvent
* PlayerQuitEvent

にも反応します。

---

### 実行順序

1. 具体イベント
2. 親イベント
3. InfinityEvent

---

### メリット

* 抽象フックが可能
* ログ監視MODが全イベント監視可能
* 拡張に強い設計

---

## 6. unregister仕様

### unregister

```id="7r0hmb"
InfinityEventBus.unregister(EventClass, listener);
```

指定イベント型からリスナーを削除。

---

### unregisterAll

```id="3b7xqn"
InfinityEventBus.unregisterAll(listener);
```

全イベントから削除。

MODアンロード時に使用。

---

## 7. API使用例

ここからは実際の使用クラス例。

---

### ① カスタムイベント定義

#### PlayerJoinEvent.java

```java id="u8h91z"
package com.example.mod.event;

import com.yuyuto.infinitymaxapi.gamelibs.event.InfinityEvent;

public class PlayerJoinEvent extends InfinityEvent {

    private final String playerName;

    public PlayerJoinEvent(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }
}
```

---

### ② Cancelableイベント例

#### PlayerDamageEvent.java

```java id="r3p9q1"
package com.example.mod.event;

import com.yuyuto.infinitymaxapi.gamelibs.event.CancelableEvent;

public class PlayerDamageEvent extends CancelableEvent {

    private final String playerName;
    private double damage;

    public PlayerDamageEvent(String playerName, double damage) {
        this.playerName = playerName;
        this.damage = damage;
    }

    public String getPlayerName() {
        return playerName;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }
}
```

---

### ③ リスナー登録例

#### ExampleModEvent.java

```java id="m7zq52"
package com.example.mod;

import com.yuyuto.infinitymaxapi.gamelibs.event.*;
import com.example.mod.event.*;

public class ExampleModEvent {

    public void onEnable() {

        // プレイヤー参加ログ
        InfinityEventBus.register(
                PlayerJoinEvent.class,
                EventPriority.NORMAL,
                event -> {
                    System.out.println(
                            event.getPlayerName() + " joined the server."
                    );
                }
        );

        // ダメージキャンセル例
        InfinityEventBus.register(
                PlayerDamageEvent.class,
                EventPriority.HIGH,
                event -> {

                    if (event.getDamage() > 100) {
                        event.setCancelled(true);
                        System.out.println("Damage cancelled!");
                    }
                }
        );

        // 抽象イベント登録（継承対応）
        InfinityEventBus.register(
                InfinityEvent.class,
                EventPriority.LOW,
                event -> {
                    System.out.println(
                            "[Debug] Event fired: "
                            + event.getClass().getSimpleName()
                    );
                }
        );
    }
}
```

---

### ④ イベント発火例

```java id="z3f9lm"
public class EventTriggerExample {

    public static void main(String[] args) {

        // プレイヤー参加
        InfinityEventBus.post(
                new PlayerJoinEvent("InfinityHero")
        );

        // ダメージイベント
        InfinityEventBus.post(
                new PlayerDamageEvent("InfinityHero", 150)
        );
    }
}
```

---

## 動作フロー例

PlayerDamageEvent(150) 発火時：

1. HIGH優先度リスナー実行
2. ダメージ>100 → setCancelled(true)
3. 即停止
4. LOWリスナーは実行されない