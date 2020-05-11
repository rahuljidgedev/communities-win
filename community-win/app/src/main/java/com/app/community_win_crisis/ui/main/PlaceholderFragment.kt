package com.app.community_win_crisis.ui.main

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.app.community_win_crisis.R
import com.app.community_win_crisis.models.UserSession
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.hbb20.CountryCodePicker

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var pageViewModel: PageViewModel
    private var session: UserSession? = null
    private var tabType: Int = 1
    private var sectionNumber: Int = 0

    private var listener: OnRegisterButtonTappedListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        session = GsonBuilder().create().fromJson(arguments?.getString(ARG_USER_DATA), UserSession::class.java)
        tabType = arguments?.getInt(ARG_TAB_TYPE) ?: 1
        sectionNumber = arguments?.getInt(ARG_SECTION_NUMBER) ?: 0
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel::class.java).apply {
            setIndex(sectionNumber)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View?
        when(tabType) {
            0 ->{
                if (session?.userToken.equals("0")){
                    root = inflater.inflate(R.layout.fragment_contact_safety, container, false)
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
                        val ccp: CountryCodePicker = root.findViewById(R.id.ccp)
                        ccp.registerCarrierNumberEditText(etMobileNumber)
                        val cbPrivacyPolicy: CheckBox = root.findViewById(R.id.cb_check_privacy)

                        if (etMobileNumber.text?.toString()!!.isEmpty()) {
                            Toast.makeText(root.context, getString(R.string.enter_mobile_number), Toast.LENGTH_SHORT).show()
                            return@OnClickListener
                        }
                        if(!cbPrivacyPolicy.isChecked){
                            Toast.makeText(root.context, getString(R.string.accept_privacy_policy), Toast.LENGTH_SHORT).show()
                            return@OnClickListener
                        }
                        listener?.onRegisterButtonTapped(ccp.fullNumber)
                    })
                }else{
                    root= inflater.inflate(R.layout.fragment_contact_safety_update, container, false)
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
                }
            }
            1 ->{
                root = inflater.inflate(R.layout.fragment_safety_contact_list, container, false)
                root.findViewById<ImageView>(R.id.iv_contact_image).visibility = View.GONE
                root.findViewById<TextView>(R.id.tv_conatct_name).text = "This is GROCERY tab."
                root.findViewById<TextView>(R.id.tv_last_updated).visibility = View.GONE
                root.findViewById<ImageView>(R.id.iv_whats_up).visibility = View.GONE
                root.findViewById<ImageView>(R.id.iv_messages).visibility = View.GONE
            }
            else ->{
                root = inflater.inflate(R.layout.fragment_safety_contact_list, container, false)
                root.findViewById<ImageView>(R.id.iv_contact_image).visibility = View.GONE
                root.findViewById<TextView>(R.id.tv_conatct_name).text = "This is OTHERS tab."
                root.findViewById<TextView>(R.id.tv_last_updated).visibility = View.GONE
                root.findViewById<ImageView>(R.id.iv_whats_up).visibility = View.GONE
                root.findViewById<ImageView>(R.id.iv_messages).visibility = View.GONE
            }
        }
        return root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnRegisterButtonTappedListener) {
            listener = context
        } else {
            //throw RuntimeException(context.toString() + " must implement OnListFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun updateUIOnRegistrationSuccess(userToken: String, userContact: String){
        session?.userToken = userToken
        session?.contactNumber = userContact
    }


    interface OnRegisterButtonTappedListener {
        fun onRegisterButtonTapped(contact: String)
    }


    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_TAB_TYPE = "tab-type"
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_USER_DATA = "user-data"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int, tabType: Int, userSession: UserSession): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_TAB_TYPE, tabType)
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                    putString(ARG_USER_DATA, Gson().toJson(userSession))
                }
            }
        }
    }
}