package com.Kotlette.ecommerce.adapter

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import com.Kotlette.ecommerce.file.FileManager


class SpinnerActivity : Activity(), AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val category = FileManager(this)
        val Choice = parent.getItemAtPosition(pos).toString()
        category.writeToFile("category.txt", Choice)

    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        // Another interface callback
    }
}
