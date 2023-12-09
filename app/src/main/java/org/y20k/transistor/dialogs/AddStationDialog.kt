/*
 * AddStationDialog.kt
 * Implements the AddStationDialog class
 * A AddStationDialog shows a dialog with list of stations to import
 *
 * This file is part of
 * TRANSISTOR - Radio App for Android
 *
 * Copyright (c) 2015-23 - Y20K.org
 * Licensed under the MIT-License
 * http://opensource.org/licenses/MIT
 */


package org.y20k.transistor.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.y20k.transistor.R
import org.y20k.transistor.core.Station
import org.y20k.transistor.search.SearchResultAdapter


/*
 * AddStationDialog class
 */
class AddStationDialog (private val context: Context, private val stationList: List<Station>, private val listener: AddStationDialogListener): SearchResultAdapter.SearchResultAdapterListener {

    /* Interface used to communicate back to activity */
    interface AddStationDialogListener {
        fun onAddStationDialog(station: Station) {
        }
    }

    /* Define log tag */
    private val TAG = AddStationDialog::class.java.simpleName


    /* Main class variables */
    private lateinit var dialog: AlertDialog
    private lateinit var stationSearchResultList: RecyclerView
    private lateinit var searchResultAdapter: SearchResultAdapter
    private var station: Station = Station()


    /* Overrides onSearchResultTapped from SearchResultAdapterListener */
    override fun onSearchResultTapped(result: Station) {
        station = result
        // make add button clickable
        activateAddButton()
    }


    /* Construct and show dialog */
    fun show() {
        // prepare dialog builder
        val builder: MaterialAlertDialogBuilder = MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)

        // set title
        builder.setTitle(R.string.dialog_add_station_title)

        // get views
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_add_station, null)
        stationSearchResultList = view.findViewById(R.id.station_list)

        // set up list of search results
        setupRecyclerView(context)

        // add okay ("Add") button
        builder.setPositiveButton(R.string.dialog_find_station_button_add) { _, _ ->
            // listen for click on add button
            listener.onAddStationDialog(station)
        }
        // add cancel button
        builder.setNegativeButton(R.string.dialog_generic_button_cancel) { _, _ ->
        }
        // handle outside-click as "no"
        builder.setOnCancelListener {
        }

        // set dialog view
        builder.setView(view)

        // create and display dialog
        dialog = builder.create()
        dialog.show()

        // initially disable "Add" button
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false
    }


    /* Sets up list of results (RecyclerView) */
    private fun setupRecyclerView(context: Context) {
        searchResultAdapter = SearchResultAdapter(this, stationList)
        stationSearchResultList.adapter = searchResultAdapter
        val layoutManager: LinearLayoutManager = object: LinearLayoutManager(context) {
            override fun supportsPredictiveItemAnimations(): Boolean {
                return true
            }
        }
        stationSearchResultList.layoutManager = layoutManager
        stationSearchResultList.itemAnimator = DefaultItemAnimator()
    }


    /* Makes the "Add" button clickable */
    private fun activateAddButton() {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
    }


}
