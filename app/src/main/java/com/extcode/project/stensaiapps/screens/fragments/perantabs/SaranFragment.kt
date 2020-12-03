package com.extcode.project.stensaiapps.screens.fragments.perantabs

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.extcode.project.stensaiapps.screens.activity.SignInActivity
import com.extcode.project.stensaiapps.viewmodel.DashboardViewModel
import com.extcode.project.stensaiapps.viewmodel.PeranViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.field_saran.*
import kotlinx.android.synthetic.main.fragment_saran.*

class SaranFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saran, container, false)
    }

    private lateinit var peranViewModel: PeranViewModel
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        peranViewModel = ViewModelProvider(this)[PeranViewModel::class.java]
        dashboardViewModel = ViewModelProvider(requireActivity())[DashboardViewModel::class.java]

        getAllEvent()

        sendSaranButton.setOnClickListener {
            val saran = saranType.editText?.text.toString()
            val deskripsi = saranDescription.editText?.text.toString().trim()
            when {
                saran.isEmpty() -> Toast.makeText(
                    context,
                    "Harap isi Nama Event",
                    Toast.LENGTH_SHORT
                ).show()
                deskripsi.isEmpty() -> Toast.makeText(
                    context,
                    "Harap isi Deskripsi",
                    Toast.LENGTH_SHORT
                ).show()
                else -> {
                    sendSaran(saran, deskripsi)
                }
            }
        }
    }

    private fun getAllEvent() {
        dashboardViewModel.setEvent()
        dashboardViewModel.getEvent().observe(viewLifecycleOwner, Observer {
            if (it.success!!) {
                val arrEvent = ArrayList<String>()
                for (eventItem in it.data!!) {
                    arrEvent.add(eventItem.nama!!)
                    Log.d("eventNama", eventItem.nama)
                }
                setAutoCompleteAdapter(arrEvent, saranType)
            } else {
                Toast.makeText(context, "Gagal mengambil Event", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sendSaran(saran: String, deskripsi: String) {

        isLoading(true)

        val siswaId =
            context?.getSharedPreferences(SignInActivity::class.simpleName, Context.MODE_PRIVATE)
                ?.getInt(
                    kUid, 0
                )
        val siswaB = if (siswaId == 0) "0" else "1"

        dashboardViewModel.getEvent().observe(viewLifecycleOwner, Observer {
            for (event in it.data!!) {
                if (event.nama == saran) {
                    postSaran(event.id.toString(), deskripsi, siswaId.toString(), siswaB)
                    break
                }
            }
        })

    }

    private fun postSaran(eventId: String, deskripsi: String, siswaId: String, siswaB: String) {
        Log.d("aseloleEventId", eventId)
        peranViewModel.setPostSaran(eventId, deskripsi, siswaId, siswaB)
        peranViewModel.getPostSaran().observe(viewLifecycleOwner, Observer {
            if (it.success!!) {
                isLoading(false)
                Toast.makeText(context, "Data Berhasil dikirim", Toast.LENGTH_SHORT).show()
                saranType.editText?.setText("")
                saranDescription.editText?.setText("")
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