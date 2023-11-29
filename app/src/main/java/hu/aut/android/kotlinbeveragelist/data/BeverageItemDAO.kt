package hu.aut.android.kotlinbeveragelist.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
/*
Itt az adatbázis műveletek találhatóak.
Új adattagkor (új ShippingItem adattag), nem szükséges módosítani itt.
 */
@Dao
interface BeverageItemDAO {

    //Az összes listázása
    @Query("SELECT * FROM beverageitem")
    fun findAllItems(): List<BeverageItem>

    //Egy elem beszúrása
    @Insert
    fun insertItem(item: BeverageItem): Long
    //Egy törlése
    @Delete
    fun deleteItem(item: BeverageItem)
    //Egy módosítása
    @Update
    fun updateItem(item: BeverageItem)

}
