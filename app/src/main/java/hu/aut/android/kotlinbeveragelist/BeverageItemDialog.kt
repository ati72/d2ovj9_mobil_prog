package hu.aut.android.kotlinbeveragelist

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import hu.aut.android.kotlinbeveragelist.data.BeverageItem
import kotlinx.android.synthetic.main.dialog_create_item.view.*
import java.util.*

/*
Ez a dialógus ablak szolgál az új Shipping Item felvitelére, és a meglevő Shopping Item módosítására
 */

class BeverageItemDialog : DialogFragment() {

    private lateinit var beverageItemHandler: BeverageItemHandler

    //Shopping Item elemek text-ben, ide szükséges a bővítés a Shopping Item új adattagja esetén
    private lateinit var etManufacturer: EditText
    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etColor: EditText
    private lateinit var etTaste: EditText
    private lateinit var etPoints: EditText

    interface BeverageItemHandler {
        fun beverageItemCreated(item: BeverageItem)

        fun beverageItemUpdated(item: BeverageItem)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is BeverageItemHandler) {
            beverageItemHandler = context
        } else {
            throw RuntimeException("The Activity does not implement the ShoppingItemHandler interface")
        }
    }

    /*Új Shopping Item felvitelekor ez hívódik meg. A felirat a New Item lesz*/
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle("New Item")

        initDialogContent(builder)

        builder.setPositiveButton("OK") { dialog, which ->
            // keep it empty
        }
        return builder.create()
    }

    private fun initDialogContent(builder: AlertDialog.Builder) {
        /*etItem = EditText(activity)
        builder.setView(etItem)*/

        //dialog_create_item.xml-el dolgozunk
        val rootView = requireActivity().layoutInflater.inflate(R.layout.dialog_create_item, null)
        //Shopping Item tagok az xml-ből (EditText elemek)
        //Itt is szükséges a bővítés új Shopping Item adattag esetén
        etManufacturer = rootView.etManufacturer
        etName = rootView.etName
        etPrice = rootView.etPrice
        etColor = rootView.etColor
        etTaste = rootView.etTaste
        etPoints = rootView.etPoints
        builder.setView(rootView)
        //Megnézzük, hogy kapott-e argumentumot (a fő ablakból), ha igen, akkor az adattagokat beállítjuk erre
        // tehát az Edittext-ek kapnak értéket, és az ablak címét beállítjuk
        val arguments = this.arguments
        if (arguments != null &&
            arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)
        ) {
            val item = arguments.getSerializable(
                MainActivity.KEY_ITEM_TO_EDIT
            ) as BeverageItem
            //Itt is szükséges a bővítés új Shopping Item adattag esetén
            etManufacturer.setText(item.manufacturer)
            etName.setText(item.name)
            etPrice.setText(item.price.toString())
            etColor.setText(item.color)
            etTaste.setText(item.taste)
            etPoints.setText(item.points.toString())

            builder.setTitle("Edit Beverage")
        }
    }


    override fun onResume() {
        super.onResume()

        val dialog = dialog as AlertDialog
        val positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE)
        //OK gomb a dialógus ablakon
        //vizsgálja az eseménykezelője, hogy a dialógus ablak kapott-e paramétereket
        //Ha kapott, akkor a handleItemEdit() hívódik meg (edit)
        //Ha nem kapott, akor a handleItemCreate() hívódik meg (create)
        positiveButton.setOnClickListener {
            if (etManufacturer.text.isNotEmpty()) {
                val arguments = this.arguments
                if (arguments != null &&
                    arguments.containsKey(MainActivity.KEY_ITEM_TO_EDIT)
                ) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }

                dialog.dismiss()
            } else {
                etManufacturer.error = "This field can not be empty"
            }
        }
    }

    //Új elem esetén hvódik meg, egy új ShoppingItem-et hoz létre
    //az itemId azért null, mert a DB adja a kulcsot
    //Itt is szükséges a bővítés új Shopping Item adattag esetén
    private fun handleItemCreate() {
        beverageItemHandler.beverageItemCreated(
            BeverageItem(
                null,
                etManufacturer.text.toString(),
                etName.text.toString(),
                etPrice.text.toString().toInt(),
                false,
                etColor.text.toString(),
                etTaste.text.toString(),
                etPoints.text.toString().toInt()
            )
        )
    }

    //Edit esetén hívódik meg, az edit-et csinálja, paraméterként átadja az adatokat
    //Itt is szükséges a bővítés új Shopping Item adattag esetén
    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
            MainActivity.KEY_ITEM_TO_EDIT
        ) as BeverageItem
        itemToEdit.manufacturer = etManufacturer.text.toString()
        itemToEdit.name = etName.text.toString()
        itemToEdit.price = etPrice.text.toString().toInt()
        itemToEdit.color = etColor.text.toString()
        itemToEdit.taste = etTaste.text.toString()
        itemToEdit.points = etPoints.text.toString().toInt()

        beverageItemHandler.beverageItemUpdated(itemToEdit)
    }
}
