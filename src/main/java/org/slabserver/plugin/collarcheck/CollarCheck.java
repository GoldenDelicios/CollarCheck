package org.slabserver.plugin.collarcheck;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

public class CollarCheck extends JavaPlugin implements Listener {
	Set<Player> players = new HashSet<>();
	
	public CollarCheck() {
		
	}

	public CollarCheck(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
		super(loader, description, dataFolder, file);
	}

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("petcheck").setTabCompleter((sender, command, alias, args) -> Collections.emptyList());
	}

	@Override
	public void onDisable() {
		
	}
	
	@EventHandler
	public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent event) {
		Player player = event.getPlayer();
		Entity entity = event.getRightClicked();
		if (event.getHand() == EquipmentSlot.HAND && players.contains(player)) {
			if (entity instanceof Tameable) {
				Tameable pet = (Tameable) entity;
				AnimalTamer owner = pet.getOwner();
				if (owner == null)
					player.sendMessage(pet.getName() + " has no owner");
				else
					player.sendMessage(pet.getName() + " is owned by " + owner.getName());
			}
			else {
				player.sendMessage(entity.getName() + " is not tameable");
			}
		}
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("petcheck")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (players.remove(player)) {
					sender.sendMessage("Pet inspector disabled");
				}
				else {
					players.add(player);
					sender.sendMessage("Pet inspector enabled. Right click a pet to check its owner");
				}
			}
			else {
				sender.sendMessage("Command may only be run by a player");
			}
		}
		
		return true;
	}

}
