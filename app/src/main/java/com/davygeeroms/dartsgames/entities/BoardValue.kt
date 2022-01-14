package com.davygeeroms.dartsgames.entities

/**
 * Class representing a field on the darts board
 * @param id: Unique id
 * @param name: Specific name for value, like T20
 * @param value: The base value of the field
 * @param modifier: Will multiply the value of the field, for instance Triple 20 = 60 = 3 * 20 -> modifier = 3
 * @param description: Description of the field
 */
class BoardValue(val id: Int, val name: String, val value: Int, val modifier: Int, val description: String) {

}