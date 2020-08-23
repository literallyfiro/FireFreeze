Overview
--------
FireFreeze is the best freeze plugin on the market! It has several new features never seen in other plugins, such as anydesk-id task, freeze history, freeze glowing, titles support, and more...

Features
--------
* Titles Support
* GUI Support
* 100% Configurable (messages.yml and config.yml)
* Easy to understand
* Freeze History command
* All data stored on SQLite/MySQL
* Possibility to merge /unfreeze to /freeze
* Location Support
* **NEW!** Anydesk task. When a player gets frozen, he has 1 minute (you can modify the time in the config) to give the anydesk id in chat, otherwhise the plugin will send a message to all the staff that says "This player has not given the anydesk id!"
* **Lightweight and resources free!**

API
--------
There are 3 events in the plugin: **PlayerFreezeAddEvent, PlayerFreezeRemoveEvent, and PlayerFreezeQuitEvent**
* **PlayerFreezeAddEvent** is called when the player is frozen. You can get the staff, and the frozen player
* **PlayerFreezeRemoveEvent** is called when the player is unfrozen. You can get the staff, and the frozen player
* **PlayerFreezeQuitEvent** is called when the player quits the server while frozen. You can get only the frozen player

**Usage of the FreezeProfile class**
The FreezeProfile class is the custom player object. You can freeze/unfreeze a player and teleport him to the freeze location
You can call it by using
```java
FreezeProfile profile = new FreezeProfile(playerObject);

profile.setFrozen(boolean) //true to freeze, false to unfreeze
profile.isFrozen() //return true if the player is frozen
profile.freezeTeleport(Player staff, LocationType type) //Send a player to the freeze location
```
