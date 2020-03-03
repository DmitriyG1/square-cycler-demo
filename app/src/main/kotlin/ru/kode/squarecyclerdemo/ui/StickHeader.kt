package ru.kode.squarecyclerdemo.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import ru.kode.squarecyclerdemo.R

class StickHeader : StickHeaderItemDecoration.StickyHeaderInterface {
  var data: List<Fruits> = emptyList()

  private var header: AppCompatTextView? = null

  override fun getHeaderPositionForItem(position: Int): Int {
    var headerPosition = -1
    var itemPosition = position
    do {
      if (isHeader(itemPosition)) {
        headerPosition = itemPosition
        break
      }
      itemPosition -= 1
    } while (itemPosition >= 0)
    return headerPosition
  }

  override fun getHeaderView(context: Context, root: ViewGroup, headerPosition: Int): View {
    return header ?: LayoutInflater.from(context).inflate(R.layout.item_header, root, false).also {
      header = it as AppCompatTextView
    }
  }

  override fun bindHeaderData(header: View, headerPosition: Int) {


    // No type checking, application will crash on error
    this.header?.text = data[headerPosition].name
  }

  override fun isHeader(itemPosition: Int): Boolean {
    return data[itemPosition] is Header
  }
}
