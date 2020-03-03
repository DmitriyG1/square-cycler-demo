package ru.kode.squarecyclerdemo.ui

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class WatermelonView @JvmOverloads constructor(
  context: Context,
  attrs: AttributeSet? = null,
  defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {

  var fruitName: CharSequence
    set(value) {
      this.text = value
    }
    get() = this.text
}
