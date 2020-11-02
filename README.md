[![Codacy Badge](https://app.codacy.com/project/badge/Grade/b55c0281d01d4fa889a66ea1aeec2296)](https://www.codacy.com/gh/ImOnlyFire/FireFreeze/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ImOnlyFire/FireFreeze&amp;utm_campaign=Badge_Grade)

# Overview
FireFreeze is the best freeze plugin on the market! It has several new features never seen in other plugins, such as anydesk-id task, freeze history, freeze glowing, titles support, and more...

*   Titles Support
*   GUI Support
*   100% Configurable (messages.yml and config.yml)
*   Easy to understand
*   Freeze History command
*   All data stored on SQLite/MySQL
*   Possibility to merge /unfreeze to /freeze
*   Location Support
*   **NEW!** Anydesk task. When a player gets frozen, he has 1 minute (you can modify the time in the config) to give the anydesk id in chat, otherwhise the plugin will send a message to all the staff that says "This player has not given the anydesk id!"
*   **Lightweight and resources free!**

## API
There are 3 events in the plugin: **PlayerFreezeAddEvent, PlayerFreezeRemoveEvent, and PlayerFreezeQuitEvent**
*   **PlayerFreezeAddEvent** is called when the player is frozen. You can get the staff, and the frozen player
*   **PlayerFreezeRemoveEvent** is called when the player is unfrozen. You can get the staff, and the frozen player
*   **PlayerFreezeQuitEvent** is called when the player quits the server while frozen. You can get only the frozen player
**Usage of the FreezeProfile class:**

The FreezeProfile class is the custom player object. You can freeze/unfreeze a player and teleport him to the freeze location
You can call it by using:
```java
public class FireFreezeTest {

    public void onEnable() {
        FreezeProfile profile = new FreezeProfile(playerObject);
    
        profile.freeze(); //To freeze the player
        profile.unfreeze(); //To unfreeze the player
        profile.forceUnfreeze(); //It's just a normal unfreeze but with the EntryType set to FORCED
        profile.getWhoFroze(); //Returns the staff member who froze the player
    }

}
```
