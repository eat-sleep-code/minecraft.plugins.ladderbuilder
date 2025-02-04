package com.eatsleepcode.ladderbuilder;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;

public class LadderBuilder extends PluginBase {

    @Override
    public void onEnable() {
        getLogger().info(TextFormat.GREEN + "LadderBuilder Loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info(TextFormat.RED + "LadderBuilder Disabled!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ladder")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(TextFormat.RED + "This command can only be used by a player.");
                return true;
            }

            Player player = (Player) sender;
            int currentY = (int) player.getPosition().y;
            int targetY = 20;

            // Set default target Y to 20 if no argument is provided
            if (args.length > 0) {
                try {
                    targetY = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    player.sendMessage(TextFormat.RED + "Usage: /ladder [depthCoordinate]");
                    return true;
                }
            }

            // Ensure the target Y is below the player's current Y position
            if (targetY >= currentY) {
                player.sendMessage(TextFormat.RED + "Target depth must be below player's current position.");
                return true;
			}

			Level level = player.getLevel();
			Vector3 start = new Vector3(player.getFloorX(), player.getFloorY() - 1, player.getFloorZ());
			
			buildLadder(level, player, start, getPlayerDirection(player), targetY);

			player.sendMessage(TextFormat.GREEN + "Stairs built successfully");
			return true;
        }
        return false;
    }



	public void buildLadder(Level level, Player player, Vector3 start, String direction, int depth) {
	for (int y = (int) start.y; y >= depth; y--) { // Depth
			for (int x = -1; x <= 1; x++) { // Width (centered, left-to-right)
				for (int z = 0; z < 4; z++) { // Length (front-to-back)
					// Transform coordinates based on player direction
					Vector3 blockPos = transformCoordinates(start, x, z, y, direction);
	
					int chunkX = blockPos.getFloorX() >> 4;
					int chunkZ = blockPos.getFloorZ() >> 4;
	
					if (!level.isChunkLoaded(chunkX, chunkZ)) {
						level.loadChunk(chunkX, chunkZ, true);
					}
	
					// Clear the room
					level.setBlock(blockPos, Block.get(Block.AIR), true);
	
					// Build outer walls and floor
					if (y == depth || x == -1 || x == 1 || z == 0 || z == 3) {
						level.setBlock(blockPos, Block.get(Block.STONE), true);
					}
	
					// Build ladder
					else if (x == 0 && (z == 2) && y > depth) {
							level.setBlock(blockPos, Block.get(Block.LADDER), true);
					}

					// Fill with air
					else {
						level.setBlock(blockPos, Block.get(Block.AIR), true);
					}

					if (x == 0 && z == 0 && y < start.y && (y % 20 == 0)) {
						level.setBlock(blockPos, Block.get(Block.SEA_LANTERN), true);
					}
				}
			}
		}
	}
	
	
	
    private Vector3 transformCoordinates(Vector3 start, int x, int z, int y, String direction) {
		// Adjust coordinates based on player direction
		switch (direction) {
			case "north":
				return new Vector3(start.x + x, y, start.z - z);
			case "south":
				return new Vector3(start.x + x, y, start.z + z);
			case "east":
				return new Vector3(start.x + z, y, start.z + x);
			case "west":
				return new Vector3(start.x - z, y, start.z + x);
			default:
				return new Vector3(start.x + x, y, start.z + z); // Default to south
		}
	}



    private String getPlayerDirection(Player player) {
		float yaw = (float) player.getYaw(); // Cast to float for Nukkit
		if (yaw < 0) {
			yaw += 360;
		}
		yaw %= 360;

		if (yaw >= 315 || yaw < 45) {
			return "south";
		} else if (yaw >= 45 && yaw < 135) {
			return "west";
		} else if (yaw >= 135 && yaw < 225) {
			return "north";
		} else {
			return "east";
		}
	}
}