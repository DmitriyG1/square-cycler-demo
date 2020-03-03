package ru.kode.squarecyclerdemo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * For more information see https://medium.com/@saber.solooki/sticky-header-for-recyclerview-c0eb551c3f68
 * https://gist.github.com/saber-solooki/edeb57be63d2a60ef551676067c66c71
 */
public class StickHeaderItemDecoration extends RecyclerView.ItemDecoration {
  private StickyHeaderInterface listener;
  private LinearLayoutManager layoutManager;

  private int stickyHeaderHeight;

  public StickHeaderItemDecoration(@NonNull StickyHeaderInterface listener, @NonNull LinearLayoutManager layoutManager) {
    this.listener = listener;
    this.layoutManager = layoutManager;
  }

  @Override
  public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
    super.onDrawOver(c, parent, state);

    final int topChildPosition = layoutManager.findFirstVisibleItemPosition();
    if (topChildPosition == RecyclerView.NO_POSITION) {
      return;
    }

    int headerPos = listener.getHeaderPositionForItem(topChildPosition);

    if (headerPos < 0) {
      return;
    }
    final View currentHeader = getHeaderViewForItem(headerPos, parent);
    fixLayoutSize(parent, currentHeader);

    final int contactPoint = currentHeader.getBottom();
    final View childInContact = getChildInContact(parent, contactPoint, headerPos);
    if (childInContact != null && listener.isHeader(parent.getChildAdapterPosition(childInContact))) {
      moveHeader(c, currentHeader, childInContact);
      return;
    }

    drawHeader(c, currentHeader);
  }

  private View getHeaderViewForItem(int headerPosition, RecyclerView parent) {
    final View header = listener.getHeaderView(parent.getContext(), parent, headerPosition);
    listener.bindHeaderData(header, headerPosition);
    return header;
  }

  private void drawHeader(Canvas c, View header) {
    c.save();
    c.translate(0, 0);
    header.draw(c);
    c.restore();
  }

  private void moveHeader(Canvas c, View currentHeader, View nextHeader) {
    c.save();
    c.translate(0, nextHeader.getTop() - currentHeader.getHeight());
    currentHeader.draw(c);
    c.restore();
  }

  private View getChildInContact(RecyclerView parent, int contactPoint, int currentHeaderPos) {
    View childInContact = null;
    for (int i = 0; i < parent.getChildCount(); i++) {
      int heightTolerance = 0;
      View child = parent.getChildAt(i);
      //measure height tolerance with child if child is another header
      if (currentHeaderPos != i) {
        boolean isChildHeader = listener.isHeader(parent.getChildAdapterPosition(child));
        if (isChildHeader) {
          heightTolerance = stickyHeaderHeight - child.getHeight();
        }
      }
      //add heightTolerance if child top be in display area
      int childBottomPosition;
      if (child.getTop() > 0) {
        childBottomPosition = child.getBottom() + heightTolerance;
      } else {
        childBottomPosition = child.getBottom();
      }
      if (childBottomPosition > contactPoint) {
        if (child.getTop() <= contactPoint) {
          // This child overlaps the contactPoint
          childInContact = child;
          break;
        }
      }
    }
    return childInContact;
  }

  /**
   * Properly measures and layouts the top sticky header.
   *
   * @param parent ViewGroup: RecyclerView in this case.
   */
  private void fixLayoutSize(ViewGroup parent, View view) {
    // Specs for parent (RecyclerView)
    int widthSpec = View.MeasureSpec.makeMeasureSpec(parent.getWidth(), View.MeasureSpec.EXACTLY);
    int heightSpec = View.MeasureSpec.makeMeasureSpec(parent.getHeight(), View.MeasureSpec.UNSPECIFIED);
    // Specs for children (headers)
    int childWidthSpec = ViewGroup.getChildMeasureSpec(widthSpec, parent.getPaddingLeft() + parent.getPaddingRight(), view.getLayoutParams().width);
    int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, parent.getPaddingTop() + parent.getPaddingBottom(), view.getLayoutParams().height);
    view.measure(childWidthSpec, childHeightSpec);
    view.layout(0, 0, view.getMeasuredWidth(), stickyHeaderHeight = view.getMeasuredHeight());
  }

  public interface StickyHeaderInterface {
    /**
     * This method gets called by {@link StickHeaderItemDecoration} to fetch the position of the header item in the adapter
     * that is used for (represents) item at specified position.
     *
     * @param itemPosition int. Adapter's position of the item for which to do the search of the position of the header item.
     * @return int. Position of the header item in the adapter.
     */
    int getHeaderPositionForItem(int itemPosition);

    /**
     * This method gets called by {@link StickHeaderItemDecoration} to get layout resource id for the header item at specified adapter's position.
     *
     * @param headerPosition int. Position of the header item in the adapter.
     * @return int. Layout resource id.
     */
    View getHeaderView(Context context, ViewGroup root, int headerPosition);

    /**
     * This method gets called by {@link StickHeaderItemDecoration} to setup the header View.
     *
     * @param header         View. Header to set the data on.
     * @param headerPosition int. Position of the header item in the adapter.
     */
    void bindHeaderData(View header, int headerPosition);

    /**
     * This method gets called by {@link StickHeaderItemDecoration} to verify whether the item represents a header.
     *
     * @param itemPosition int.
     * @return true, if item at the specified adapter's position represents a header.
     */
    boolean isHeader(int itemPosition);
  }
}
