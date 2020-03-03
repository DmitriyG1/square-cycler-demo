package ru.kode.squarecyclerdemo.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.cycler.ItemComparator
import com.squareup.cycler.Recycler
import com.squareup.cycler.toDataSource
import ru.kode.squarecyclerdemo.R

class CyclerDemoActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_cycler_demo)

    testCycler()
  }

  private fun testCycler() {
    val recyclerView = findViewById<RecyclerView>(R.id.recycler)
    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    val recycler = Recycler.adopt<Fruits>(recyclerView) {

      row<Apple, AppleView> {
        create(R.layout.item_apple) {
          bind { fruit ->
            view.fruitName = fruit.name
            view.setOnClickListener {
              fruit.clickListener?.invoke(fruit)
            }
          }
        }
      }

      row<Watermelon, View> {
        create(R.layout.item_watermelon) {
          val t = view.findViewById<TextView>(R.id.text)
          bind { fruit ->
            t.text = fruit.name
            view.setOnClickListener {
              fruit.clickListener?.invoke(fruit)
            }
          }
        }
      }

      row<Header, View> {

        create(R.layout.item_header) {
          val t = view.findViewById<TextView>(R.id.text)
          bind { fruit ->
            t.text = fruit.name
            view.setOnClickListener {
              fruit.clickListener?.invoke(fruit)
            }
          }
        }
      }

      extension(StickyHeaderSpec())

      itemComparator = object : ItemComparator<Fruits> {
        override fun areSameContent(oldItem: Fruits, newItem: Fruits): Boolean {
          return oldItem == newItem
        }

        override fun areSameIdentity(oldItem: Fruits, newItem: Fruits): Boolean {
          return oldItem.javaClass == newItem.javaClass
        }
      }

    }

    recycler.data = getTestData().toDataSource()
  }

  private fun getTestData(): List<Fruits> {
    return getHeader("Apples 1") +
      getApples(0, 5) +
      getHeader("Watermelons 1") +
      getWatermelons(0, 5) +
      getHeader("Apples 2") +
      getApples(5, 8) +
      getHeader("Watermelons 2") +
      getWatermelons(5, 25)
  }

  private fun getHeader(title: String): List<Fruits> {
    return listOf(
      Header(
        id = "1",
        name = title,
        clickListener = fruitClickListener
      )
    )
  }

  private fun getApples(seed: Int, count: Int): List<Fruits> {
    return (1..count).map { i ->
      Apple(
        id = "1",
        name = "Apple ${i + seed}",
        clickListener = fruitClickListener
      )
    }
  }

  private fun getWatermelons(seed: Int, count: Int): List<Fruits> {
    return (1..count).map { i ->
      Watermelon(
        id = "1",
        name = "Watermelon ${i + seed}",
        clickListener = fruitClickListener
      )
    }
  }

  private val fruitClickListener = { t: Fruits ->
    Toast.makeText(this, t.name, Toast.LENGTH_SHORT).show()
  }
}
