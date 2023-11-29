package hu.aut.android.kotlinbeveragelist.touch

interface BeverageTouchHelperAdapter {

    fun onItemDismissed(position: Int)

    fun onItemMoved(fromPosition: Int, toPosition: Int)
}