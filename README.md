# Ultimate-FireworkShow
Minecraft plugin: Create a fully customizable firework show with minecraft fireworks!

This plugin makes use of the bukkit serialization system, editing a firework show in the data files is a bad idea!

####Check my website: https://mcdev.igufguf.com

## Commands
Aliases: /fireworkshow - /fws

* **/fws play <showname>**  
  - Start a firework show
  - fireworkshow.play

* **/fws create <showname>**  
  - Create a new firework show
  - fireworkshow.create

* **/fws delete <showname>**  
  - Delete a fireworksshow
  - fireworkshow.delete

* **/fws addframe <showname> <delay>**  
  - Add a frame to the show, delay is in ticks*
  - fireworkshow.addframe

* **/fws delframe <showname> <frameid>**  
  - Delete a frame
  - fireworkshow.delframe

* **/fws dupframe <showname> <frameid>**  
  - Duplicate a frame
  - fireworkshow.dupframe

* **/fws nwfw <showname>**  
  - Add the firework you're holding to the last frame on your current position
  - fireworkshow.newfw
  
* **/fws nwfw <showname> <frameid>**  
  - Add the firework you're holding to the given frame on your current position
  - fireworkshow.newfw