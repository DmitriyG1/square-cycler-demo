package ru.kode.squarecyclerdemo.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.cycler.*

class StickyHeaderSpec : ExtensionSpec<Fruits> {
  override fun create(): Extension<Fruits> {
    return StickyHeaderExt()
  }
}

class StickyHeaderExt : Extension<Fruits> {
  private val stickHeader = StickHeader()
  private var d: RecyclerData<Fruits> = RecyclerData.empty()

  override var data: RecyclerData<Fruits>
    get() = d
    set(value) {
      d = value
      // TODO update stickHeader: clean up, redraw view and etc
      stickHeader.data = d.data.toList()
    }

  override fun attach(recycler: Recycler<Fruits>) {

    stickHeader.data = data.data.toList()
    val itemDecoration = StickHeaderItemDecoration(
      stickHeader,
      recycler.view.layoutManager as LinearLayoutManager
    )

    recycler.view.addItemDecoration(itemDecoration)
  }
}

fun <T> DataSource<T>.toList(): List<T> {
  return (0 until this.size).map {
    this[it]
  }
}