package com.davygeeroms.dartsgames.entities

/**
 * A player.
 * @param number: The player's number in the current game.
 * @param name: The player's name.
 * @param color: The player's preferred color (chosen in "new game"-screen)
 */
class Player(var number: Int, val name:String, val color: String = "#ff0000")