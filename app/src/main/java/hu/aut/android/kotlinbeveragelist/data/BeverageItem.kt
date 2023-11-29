package hu.aut.android.kotlinbeveragelist.data

import java.io.Serializable

import android.arch.persistence.room.Entity
import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.PrimaryKey

/*
Adatbázis táblát készti el.
Táblanév:shoppingitem.
Oszlopok:itemId, name,  price, bought.
@PrimaryKey(autoGenerate = true): elsődleges kulcs, automatikusan generálva.
Ide szükséges a bővítés új adattal.
 */
@Entity(tableName = "beverageitem")
data class BeverageItem(
    @PrimaryKey(autoGenerate = true) var itemId: Long?,
    @ColumnInfo(name = "manufacturer") var manufacturer: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "price") var price: Int,
    @ColumnInfo(name = "like") var like: Boolean,
    @ColumnInfo(name = "color") var color: String,
    @ColumnInfo(name = "taste") var taste: String,
    @ColumnInfo(name = "points") var points: Int
) : Serializable
