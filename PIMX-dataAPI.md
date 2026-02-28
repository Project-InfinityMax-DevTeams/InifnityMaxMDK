# PIMX Data API
---
##  1. データ規格概要

### ■ 目的

PIMX（Persistent Infinity Modular eXchange）は、

* MOD単位でのデータ管理
* 型安全な保存
* JSONによる永続化
* 競合ポリシー制御
* 将来的な拡張性

を目的としたデータ規格です。

---

### ■ 基本構造

データは以下の単位で管理されます：

```
PIMXData
 └── Owner（scope + identifier）
 └── Map<String, PIMXEntry<?>>
```

---

### ■ 設計思想

* 1 Owner = 1 データ空間
* Keyは文字列
* Valueは型登録されたクラスのみ
* 同一キー衝突時はConflictPolicyで制御
* バージョン明示により将来互換を確保

---

## 2. JSONフォーマット仕様

### ■ ルート構造

```json
{
  "version": "1.0",
  "scope": "MOD",
  "owner": "examplemod",
  "data": { ... }
}
```

---

### ■ 各フィールド説明

| フィールド   | 説明                     |
| ------- | ---------------------- |
| version | データ規格バージョン             |
| scope   | データスコープ（例: MOD, WORLD） |
| owner   | 所有者識別子                 |
| data    | 実データマップ                |

---

### ■ Entry構造

```json
"some.key": {
  "type": "int",
  "value": 40,
  "sync": {
    "apply": "IMMEDIATE",
    "conflict": "REPLACE"
  }
}
```

---

### ■ Entryフィールド

| フィールド         | 説明                       |
| ------------- | ------------------------ |
| type          | PIMXTypeRegistryで登録された型名 |
| value         | 実データ                     |
| sync.apply    | 適用ポリシー                   |
| sync.conflict | 競合ポリシー                   |

---

## 3. API使用例

### ■ ExampleModData.java

```java
package com.example.mod;

import com.yuyuto.infinitymaxapi.gamelibs.data.*;
import com.google.gson.JsonObject;
import java.nio.file.Files;
import java.nio.file.Path;

public class ExampleModData {

    private PIMXData data;

    public void onEnable() {
        PIMXOwner owner = new PIMXOwner(
                PIMXScope.MOD,
                "examplemod"
        );

        data = new PIMXData(owner);

        registerDefaultData();
    }

    private void registerDefaultData() {

        PIMXSync sync = new PIMXSync(
                ApplyPolicy.IMMEDIATE,
                ConflictPolicy.REPLACE
        );

        data.registryEntry(
            PIMXEntry.create(
                "player.max_health",
                Integer.class,
                40,
                sync
            )
        );

        data.registryEntry(
            PIMXEntry.create(
                "player.name",
                String.class,
                "InfinityHero",
                sync
            )
        );
    }

    public void save(Path path) throws Exception {
        JsonObject json = data.toJson();
        Files.writeString(path, json.toString());
    }

    public void load(Path path) throws Exception {
        String content = Files.readString(path);
        JsonObject root = com.google.gson.JsonParser
                .parseString(content)
                .getAsJsonObject();

        data = PIMXData.fromJson(root);
    }

    public int getHealth() {
        return data.getValue("player.max_health", Integer.class);
    }
}
```

---

## 4. ConflictPolicy仕様

ConflictPolicyは、同一キー登録時の動作を定義します。

---

### ■ REPLACE

既存エントリを上書きします。(old → new に完全置換)

### ■ ERROR

例外をスローします。(IllegalStateException)

用途：

* 意図しない上書き防止
* 設計上必ず一意であるべきキー

### ■ MERGE

型に応じて値を結合します。

### 標準実装

| 型       | 動作    |
| ------- | ----- |
| Integer | 加算    |
| Double  | 加算    |
| String  | 文字列連結 |
| Boolean | OR演算  |

※ 拡張可能

---

## 5. 型登録（PIMXTypeRegistry）

PIMXでは型安全のため、
利用可能な型を事前登録します。

---

### ■ 型登録例

```java
PIMXTypeRegistry.register("int", Integer.class);
PIMXTypeRegistry.register("double", Double.class);
PIMXTypeRegistry.register("string", String.class);
PIMXTypeRegistry.register("boolean", Boolean.class);
```

---

### ■ カスタム型登録例

```java
PIMXTypeRegistry.register("vec3", Vec3.class);
```

### ■ 制約

* 登録されていない型はロード不可
* JSON保存時は型名で保存
* ロード時は型名→Classへ変換

---

## 6. バージョン互換ポリシー

### ■ versionフィールド

```json
"version": "1.0"
```

現在サポート：

```
1.0
```

---

### ■ 非対応バージョン

```java
if (!"1.0".equals(version)) {
    throw new IllegalStateException("Unsupported PIMX version");
}
```