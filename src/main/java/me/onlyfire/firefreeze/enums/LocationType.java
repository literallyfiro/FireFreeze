package me.onlyfire.firefreeze.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.onlyfire.firefreeze.Firefreeze;
import org.bukkit.Bukkit;
import org.bukkit.World;

@AllArgsConstructor
@Getter
public enum LocationType {
    FROZEN(
            Bukkit.getWorld(Firefreeze.getInstance().getLocationFile().getString("frozen.world")),
            Firefreeze.getInstance().getLocationFile().getDouble("frozen.x"),
            Firefreeze.getInstance().getLocationFile().getDouble("frozen.y"),
            Firefreeze.getInstance().getLocationFile().getDouble("frozen.z"),
            (float) Firefreeze.getInstance().getLocationFile().getDouble("frozen.yaw"),
            (float) Firefreeze.getInstance().getLocationFile().getDouble("frozen.pitch"),
            Firefreeze.getInstance().getLocationFile().getBoolean("frozen.use"),
            "Frozen"
    ),

    STAFF(
            Bukkit.getWorld(Firefreeze.getInstance().getLocationFile().getString("staff.world")),
            Firefreeze.getInstance().getLocationFile().getDouble("staff.x"),
            Firefreeze.getInstance().getLocationFile().getDouble("staff.y"),
            Firefreeze.getInstance().getLocationFile().getDouble("staff.z"),
            (float) Firefreeze.getInstance().getLocationFile().getDouble("staff.yaw"),
            (float) Firefreeze.getInstance().getLocationFile().getDouble("staff.pitch"),
            Firefreeze.getInstance().getLocationFile().getBoolean("staff.use"),
            "Staff"
    ),


    FINAL(
            Bukkit.getWorld(Firefreeze.getInstance().getLocationFile().getString("final.world")),
            Firefreeze.getInstance().getLocationFile().getDouble("final.x"),
            Firefreeze.getInstance().getLocationFile().getDouble("final.y"),
            Firefreeze.getInstance().getLocationFile().getDouble("final.z"),
            (float) Firefreeze.getInstance().getLocationFile().getDouble("final.yaw"),
            (float) Firefreeze.getInstance().getLocationFile().getDouble("final.pitch"),
            Firefreeze.getInstance().getLocationFile().getBoolean("final.use"),
            "Final"
    );

    private World world;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean enabled;
    private String name;
}
