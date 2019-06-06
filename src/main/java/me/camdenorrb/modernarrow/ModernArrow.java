package me.camdenorrb.modernarrow;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import static java.lang.Math.*;


public final class ModernArrow extends JavaPlugin implements Listener {

	private boolean shouldApplyToMobs, shouldApplyToPlayers;


	@Override
	public void onLoad() {

		saveDefaultConfig();

		shouldApplyToMobs = getConfig().getBoolean("shouldApplyToMobs", false);
		shouldApplyToPlayers = getConfig().getBoolean("shouldApplyToPlayers", true);
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
	}


	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
	public void onBowShoot(final EntityShootBowEvent event) {

		if (!shouldApplyToMobs && event.getEntityType() != EntityType.PLAYER) {
			return;
		}

		if (!shouldApplyToPlayers && event.getEntityType() == EntityType.PLAYER) {
			return;
		}

		final Location eyeLocation = event.getEntity().getEyeLocation();
		final Vector newVelocity = calcVelocityForArrow(eyeLocation.getYaw(), eyeLocation.getPitch(), event.getForce());

		event.getProjectile().setVelocity(newVelocity);
	}


	private Vector calcVelocityForArrow(final double yaw, final double pitch, final double power) {

		final double actualPower = power * 3;

		final double xVel = (-sin(toRadians(yaw))) * cos(toRadians(pitch));
		final double yVel = (-sin(toRadians(pitch)));
		final double zVel = cos(toRadians(yaw)) * cos(toRadians(pitch));

		return new Vector(xVel, yVel, zVel).normalize().multiply(actualPower);
	}

}
