package com.app.app_demo.ui.main

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.app.app_demo.R
import com.app.app_demo.models.UserSession
import com.app.app_demo.network_interfacing.utils.HttpConstants.Companion.REQ_APP
import com.app.app_demo.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_NAME_APP
import com.app.app_demo.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_NAME_CEL
import com.app.app_demo.network_interfacing.utils.HttpConstants.Companion.REQ_BODY_NAME_DEVICE_DETAILS
import com.app.app_demo.network_interfacing.utils.HttpConstants.Companion.SERVICE_REQUEST_TOKEN_UPDATE
import com.app.app_demo.network_interfacing.utils.HttpRequestsUtils.Companion.httpTokenUpdateRequest
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hbb20.CountryCodePicker

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var session: UserSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
        session = GsonBuilder().create().fromJson(arguments?.getString(ARG_USER_DATA), UserSession::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (session?.userToken?.isEmpty()!!){
            val root: View = inflater.inflate(R.layout.fragment_contact_safety, container, false)
            val spinner: Spinner = root.findViewById(R.id.spinner)
            ArrayAdapter.createFromResource(
                root.context,
                R.array.country_name_array,
                android.R.layout.simple_spinner_item
            ).also {adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

            val btnSend: Button = root.findViewById(R.id.btn_send)
            btnSend.setOnClickListener(View.OnClickListener {
                val etMobileNumber: EditText = root.findViewById(R.id.et_mobile_number)
                //val etCountryCode: EditText = root.findViewById(R.id.et_country_code)
                val ccp: CountryCodePicker = root.findViewById(R.id.ccp)
                ccp.registerCarrierNumberEditText(etMobileNumber)
                val cbPrivacyPolicy: CheckBox = root.findViewById(R.id.cb_check_privacy)
                if(!cbPrivacyPolicy.isChecked){
                    Toast.makeText(root.context, getString(R.string.accept_privacy_policy), Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }

                if (etMobileNumber.text?.toString()!!.isNotEmpty()){
                    val map: HashMap<String,String> = HashMap(3)
                    val mobile: String = ccp.fullNumber
                    map[REQ_BODY_NAME_CEL] = mobile
                    map[REQ_BODY_NAME_APP] = REQ_APP
                    map[REQ_BODY_NAME_DEVICE_DETAILS] = Build.MODEL
                    httpTokenUpdateRequest(SERVICE_REQUEST_TOKEN_UPDATE, map, root.context)
                }else{
                    Toast.makeText(root.context, "Enter correct mobile number", Toast.LENGTH_SHORT).show()
                }
            })
            return root
        }else{
            val root: View = inflater.inflate(R.layout.fragment_contact_safety_update, container, false)
            val spinner: Spinner = root.findViewById(R.id.spinner_safe)
            ArrayAdapter.createFromResource(
                root.context,
                R.array.i_am_safe,
                android.R.layout.simple_spinner_item
            ).also {adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }

            val etMobileNumber: EditText = root.findViewById(R.id.et_mobile_number)
            etMobileNumber.setText(session?.contactNumber)
            val btnUpdate: Button = root.findViewById(R.id.btn_update)
            btnUpdate.setOnClickListener(View.OnClickListener {
                Toast.makeText(root.context, "Pressed the update.", Toast.LENGTH_SHORT).show()
            })
            val btnCancel: Button = root.findViewById(R.id.btn_cancel)
            btnCancel.setOnClickListener(View.OnClickListener {
                Toast.makeText(root.context, "Pressed the Cancel.", Toast.LENGTH_SHORT).show()
            })
            return root
        }
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_USER_DATA = "user-data"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int, viewType: Int, userSession: UserSession): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                    putString(ARG_USER_DATA, Gson().toJson(userSession))
                }
            }
        }
    }
}