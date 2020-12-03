package com.extcode.project.stensaiapps.screens.fragments.perantabs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.extcode.project.stensaiapps.R
import com.extcode.project.stensaiapps.other.kUid
import com.extcode.project.stensaiapps.other.typeOfPengajuan
import com.extcode.project.stensaiapps.screens.activity.SignInActivity
import com.extcode.project.stensaiapps.viewmodel.PeranViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.field_pengajuan.*
import kotlinx.android.synthetic.main.fragment_pengajuan.*

class PengajuanFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pengajuan, container, false)
    }

    private lateinit var peranViewModel: PeranViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        peranViewModel = ViewModelProvider(this)[PeranViewModel::class.java]

        setAutoCompleteAdapter(typeOfPengajuan, pengajuanType)
        sendPengajuanButton.setOnClickListener {

            val pengajuan = pengajuanType.editText?.text.toString()
            val deskripsi = pengajuanDescription.editText?.text.toString().trim()

            when {
                pengajuan.isEmpty() -> Toast.makeText(
                    context,
                    "Harap isi Jenis Pengajuan",
                    Toast.LENGTH_SHORT
                ).show()
                deskripsi.isEmpty() -> Toast.makeText(
                    context,
                    "Harap isi Deskripsi",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    sendPengajuan(pengajuan, deskripsi)
                }
            }
        }
    }

    private fun sendPengajuan(pengajuan: String, deskripsi: String) {

        isLoading(true)

        val siswaId =
            context?.getSharedPreferences(SignInActivity::class.simpleName, Context.MODE_PRIVATE)
                ?.getInt(
                    kUid, 0
                )
        val siswaB = if (siswaId == 0) "0" else "1"

        peranViewModel.setPostPengajuan(pengajuan, deskripsi, siswaId.toString(), siswaB)
        peranViewModel.getPostPengajuan().observe(viewLifecycleOwner, Observer {
            if (it.success!!) {
                isLoading(false)
                Toast.makeText(context, "Data Berhasil diajukan", Toast.LENGTH_SHORT).show()
                pengajuanType.editText?.setText("")
                pengajuanDescription.editText?.setText("")
            } else {
                isLoading(false)
                Toast.makeText(context, "Data Gagal dikirim", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setAutoCompleteAdapter(items: List<String>, textInputLayout: TextInputLayout) {
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_list_item, items)
        (textInputLayout.editText as AutoCompleteTextView).setAdapter(adapter)
    }

    private fun isLoading(bool: Boolean) {
        if (bool) {
            progressBar.visibility = View.VISIBLE
            loadingBackground.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
            loadingBackground.visibility = View.GONE
        }
    }


}