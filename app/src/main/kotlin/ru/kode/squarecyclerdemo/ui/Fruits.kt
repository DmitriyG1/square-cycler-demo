package ru.kode.squarecyclerdemo.ui

interface Fruits {
  val id: String
  val name: String
  val clickListener: ((Fruits) -> Unit)?
}

data class Apple(
  override val id: String,
  override val name: String,
  override val clickListener: ((Fruits) -> Unit)? = null
) : Fruits

data class Watermelon(
  override val id: String,
  override val name: String,
  override val clickListener: ((Fruits) -> Unit)? = null
) : Fruits

data class Header(
  override val id: String,
  override val name: String,
  override val clickListener: ((Fruits) -> Unit)? = null
) : Fruits
