# LadderBuilder Minecraft (Nukkit) Plugin

Build a lighted ladder shaft descending to the specified depth coordinate.

## Prerequisites
- [Nukkit Minecraft Server](https://github.com/PetteriM1/NukkitPetteriM1Edition/releases)

## Installation 
- Place the `LadderBuilder.jar` file in the `<Nukkit Installation Folder>/plugins/` folder.

## Usage

- Create a stone-walled, lighted ladder shaft to a depth of Y=20:

  `/ladder 20`

## Known Issues

- When gravel, sand, or water is found within the ladder area it can sometimes overwhelm the creation of the ladder shaft.   This will result in some debris that needs to be cleaned up. 

## Building Project

Run `mvn clean package`.   The output will be saved to `/target/LadderBuilder.jar`