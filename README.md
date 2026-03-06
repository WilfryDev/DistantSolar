<h1 align="center">  ⚡ xAlmas - Premium PvP Soul Economy ⚡</h1>

<p align="center">
  <b>The ultimate, highly customizable PvP economy plugin for your Minecraft server.</b><br>
</p>

![banner](https://cdn.modrinth.com/data/cached_images/41574217c9227b618e5e0ca2683bd86cf85cfa4e.jpeg)
---

## 📖 Overview

**xAlmas** is a premium Minecraft plugin designed to reward players for their PvP skills. Every time a player gets a kill, they can collect the victim's "soul" as a physical, highly customizable item. It features a robust anti-farming system, modern HEX color support, and a dedicated Developer API. 

Whether you want to use standard dyes or custom Base64 player heads, **xAlmas** gives you complete control over your server's kill-reward economy!

## ✨ Key Features

* 💀 **Custom Soul Items:** Create infinite types of currencies! Support for standard materials, custom lore, enchantments, hidden flags, and **Base64 Custom Heads**.
* 🛡️ **Advanced Anti-Farming System:** Prevent abuse with built-in IP-matching blocks and configurable per-player cooldowns.
* 🎨 **Modern Formatting:** Full support for **MiniMessage** and **HEX Colors** (`&#HEX` or `<#HEX>`). Make your items and chat messages look stunning.
* 🎲 **Dynamic Drops:** Choose whether souls go directly into the killer's `INVENTORY` or `DROP` on the ground. You can even set the dropped item to be `random` from your configured list!
* 🌍 **World Whitelist/Blacklist:** Disable soul drops in specific worlds (e.g., spawn or end).
* 📊 **PlaceholderAPI Support:** Display player soul stats on scoreboards or menus using `%xa_almas%`.
* 🔊 **Custom Sounds:** Play specific sounds (with adjustable pitch and volume) when a player collects a soul.
* ⚙️ **Developer API:** Includes a powerful API with custom events so other plugins can interact with the economy.

---

## 💻 Commands & Permissions

All commands are manageable under a single, easy-to-use base command with full Tab-Completer support.

| Command | Description | Permission |
| :--- | :--- | :--- |
| `/xa help` | Shows the custom help menu. | `xalmas.admin` |
| `/xa give <currency> <amount>` | Gives a specific amount of souls to a player. | `xalmas.admin` |
| `/xa reload` | Reloads `config.yml`, `almas.yml`, and data safely. | `xalmas.admin` |

---

## 🔌 PlaceholderAPI

Make sure you have [PlaceholderAPI](https://modrinth.com/plugin/placeholderapi) installed to use this feature!

* `%xa_almas%` — Returns the total amount of souls the player has collected.

---

## 🛠️ Configuration Snapshot

Setting up your items in `almas.yml` is incredibly easy. Here is an example of a premium Base64 head setup:

```yaml
items:
  alma_premium:
    material: "PLAYER_HEAD"
    base64: "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGRjODgwNTNjMWE4NTNmNzE2NmM5ZTkzYmIzYzUxN2YwODE5NTQ0NzM5ZGJlYWJkNDhhODU5Y2VkNDIwYjcwYiJ9fX0="
    name: "&#FACBCB☠ Premium Soul &#FACBCB&lF&#EED1D0&lA&#E2D8D5&lN&#D5DED9&lI&#C9E4DE&lA &#FACBCB♫"
    lore:
      - ""
      - "&8 ℹ Information  "
      - ""
      - "&f Exchange this rare coin for  "
      - "&f VIP rewards at spawn.  "
      - ""
      - "&#FACBCB ▸ Special Edition!   "
      - ""
    enchantments:
      - "DURABILITY:3"
    flags:
      - "HIDE_ENCHANTS"
```

---

## 🚀 For Developers (API)

**xAlmas** is built with extensibility in mind. You can use our API via JitPack to hook into the plugin, manipulate soul balances, and listen to custom events.

### 1. Dependency Installation
First, add the JitPack repository and the xAlmas dependency to your project. *(Make sure to replace `VERSION` with the latest release, e.g., `v1.0.0`)*.

**Gradle (Kotlin DSL)**
```kotlin
repositories {
    mavenCentral()
    maven("[https://jitpack.io](https://jitpack.io)")
}

dependencies {
    compileOnly("com.github.WilfryDev:xAlmas:VERSION") 
}
```

**Gradle (Groovy)**
```gradle
repositories {
    mavenCentral()
    maven { url '[https://jitpack.io](https://jitpack.io)' }
}

dependencies {
    compileOnly 'com.github.WilfryDev:xAlmas:VERSION'
}
```

**Maven**
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>[https://jitpack.io](https://jitpack.io)</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.WilfryDev</groupId>
        <artifactId>xAlmas</artifactId>
        <version>v1.0.1</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### 2. Available Events

**`PlayerReceiveSoulEvent`**
This event is fired right before a player receives a soul item from a PvP kill.
* `isCancelled()` / `setCancelled(boolean)`: Cancel the event to prevent the drop.
* `getSoulType()` / `setSoulType(String)`: View or dynamically change the type of soul awarded based on killer's ranks or permissions.
* `getKiller()` / `getVictim()`: Get the exact players involved in the event.

```java
@EventHandler
public void onSoulReceive(PlayerReceiveSoulEvent event) {
    if (event.getKiller().hasPermission("vip.doublesouls")) {
        // You can change the soul dropped to a premium one dynamically
        event.setSoulType("alma_premium");
    }
}
```

### 3. API Methods

You can easily interact with a player's saved data using the `XAlmasAPI` class.

```java
import jn.willfrydev.xalmas.api.XAlmasAPI;

// Get the total amount of souls a player has
int totalSouls = XAlmasAPI.getSouls(player.getUniqueId());

// Add souls directly to a player's data file
XAlmasAPI.addSouls(player.getUniqueId(), 5);

// Remove souls safely (will not drop below 0)
XAlmasAPI.removeSouls(player.getUniqueId(), 2);
```

---
*Optimized for Paper 1.16.5+.*
> 💗 By xPlugins :=)
