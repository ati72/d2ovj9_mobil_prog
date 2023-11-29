package hu.aut.android.kotlinbeveragelist.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import hu.aut.android.kotlinbeveragelist.MainActivity
import hu.aut.android.kotlinbeveragelist.R
import hu.aut.android.kotlinbeveragelist.adapter.BeverageAdapter.ViewHolder
import hu.aut.android.kotlinbeveragelist.data.AppDatabase
import hu.aut.android.kotlinbeveragelist.data.BeverageItem
import hu.aut.android.kotlinbeveragelist.touch.BeverageTouchHelperAdapter
import kotlinx.android.synthetic.main.row_item.view.*
import java.util.*

class BeverageAdapter : RecyclerView.Adapter<ViewHolder>, BeverageTouchHelperAdapter {
    /* ShoppingItem elemek listája*/
    private val items = mutableListOf<BeverageItem>()
    private val context: Context

    constructor(context: Context, items: List<BeverageItem>) : super() {
        this.context = context
        this.items.addAll(items)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.row_item, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*Itt kérjük le az egyes ShoppingItem elemek adattagjait, itt is szükséges az adattaggal a bővítés*/
        holder.tvManufacturer.text = items[position].manufacturer
        holder.tvName.text = items[position].name
        holder.tvPrice.text = items[position].price.toString()
        holder.cbLike.isChecked = items[position].like
        holder.tvColor.text = items[position].color
        holder.tvTaste.text = items[position].taste
        holder.tvPoints.text = items[position].points.toString()
        /*Delete gomb eseménykezeője (a főoldalon)*/
        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }
        /*Edit gomb eseménykezelője (a főoldalon), megnyitja az edit dialógust, átadja az adott ShoppingItem-et neki*/
        holder.btnEdit.setOnClickListener {
            (holder.itemView.context as MainActivity).showEditItemDialog(
                items[holder.adapterPosition]
            )
        }
        /*Checkbox eseménykezelője, állítja a checkbox értékét, azaz a ShoppingItem-nek, az isChecked adattagját.
        Az adatbázisban is frissíti
         */
        holder.cbLike.setOnClickListener {
            items[position].like = holder.cbLike.isChecked
            val dbThread = Thread {
                //Itt frissíti a DB-ben
                AppDatabase.getInstance(context).beverageItemDAO().updateItem(items[position])
            }
            dbThread.start()
        }
    }

    /*Új elem hozzáadásakor hívódik meg*/
    fun addItem(item: BeverageItem) {
        items.add(item)
        notifyItemInserted(items.lastIndex)
    }

    /*Elem törlésekor hívódik meg. Az adatbázisból törli az elemet (DAO-n keresztül)*/
    fun deleteItem(position: Int) {
        val dbThread = Thread {
            AppDatabase.getInstance(context).beverageItemDAO().deleteItem(
                items[position]
            )
            (context as MainActivity).runOnUiThread {
                items.removeAt(position)
                notifyItemRemoved(position)
            }
        }
        dbThread.start()
    }

    /*Update-kor hívódik meg*/
    fun updateItem(item: BeverageItem) {
        val idx = items.indexOf(item)
        items[idx] = item
        notifyItemChanged(idx)
    }

    override fun onItemDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)

        notifyItemMoved(fromPosition, toPosition)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        /*a ShoppingItem elemek, ide kell a bővítés új taggal*/
        /*Itt a gombokat, checkboxot is lekérjük*/
        val tvManufacturer: TextView = itemView.tvManufacturer
        val tvName: TextView = itemView.tvName
        val tvPrice: TextView = itemView.tvPrice
        val cbLike: CheckBox = itemView.cbLike
        val btnDelete: Button = itemView.btnDelete
        val btnEdit: Button = itemView.btnEdit
        val tvColor: TextView = itemView.tvColor
        val tvTaste: TextView = itemView.tvTaste
        val tvPoints: TextView = itemView.tvPoints
    }
}