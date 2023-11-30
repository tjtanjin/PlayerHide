<p align="center">
  <img width=300 src="https://i.imgur.com/MEb5x1f.png" />
</p>
<h1 align="center">PlayerHide</h1>

## Table of Contents
* [Introduction](#introduction)
* [Features](#features)
* [Technologies](#technologies)
* [Setup](#setup)
* [Team](#team)
* [Contributing](#contributing)
* [Others](#others)

### Introduction
PlayerHide is a spigot plugin that provides the simple functionality of toggling player 
visibility on a minecraft server. The primary motivation behind this was for use in events such 
as parkour and mazes, where player visibility can disrupt the player experience. This plugin has 
also found use in PvE situations where players hope to take on mobs without the hitboxes of 
other players getting in the way. The spigot link to download the plugin can be found [here](https://www.spigotmc.org/resources/playerhide.105677/).

### Features
PlayerHide currently supports the following features:
-   Option to fully hide all players or replace players with particles
-   Option to force hide players on join
-   Option to toggle visibility through commands
-   Option to toggle visibility through a given item with an option to disallow players from throwing them (useful for lobbies)
-   Fully customizable messages (with options for your own language files!)

### Technologies
Technologies used by PlayerHide are as below:
##### Done with:

<p align="center">
  <img height="150" width="150" src="https://brandlogos.net/wp-content/uploads/2013/03/java-eps-vector-logo.png"/>
</p>
<p align="center">
Java
</p>

##### Project Repository
```
https://github.com/tjtanjin/PlayerHide
```

### Setup
Setting up the PlayerHide project locally is very easy!
* First, cd to the directory of where you wish to store the project and clone this repository. An example is provided below:
```
$ cd /home/user/exampleuser/projects/
$ git clone https://github.com/tjtanjin/PlayerHide.git
```
Next, make any updates/changes you wish to the code. Once ready, you may build the plugin with the following command:
```
mvn clean install
```
That's all!
### Contributors
* [Tan Jin](https://github.com/tjtanjin)

### Contributing
If you have code to contribute to the project, open a pull request and describe clearly the changes and what they are intended to do (enhancement, bug fixes etc). Alternatively, you may simply raise bugs or suggestions by opening an issue.

### Others
For any questions regarding the implementation of the project, please drop an email to: cjtanjin@gmail.com.