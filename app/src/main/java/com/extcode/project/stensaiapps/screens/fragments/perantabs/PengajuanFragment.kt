package com.extcode.project.stensaiapps.screens.fragments.perantabs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.Fragment
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.other.typeOfPengajuan
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.field_pengajuan.*

class PengajuanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pengajuan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAutoCompleteAdapter(typeOfPengajuan, pengajuanType)

    }


    private fun setAutoCompleteAdapter(items: List<String>, textInputLayout: TextInputLayout) {
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_item, items)
        (textInputLayout.editText as AutoCompleteTextView).setAdapter(adapter)
    }

}