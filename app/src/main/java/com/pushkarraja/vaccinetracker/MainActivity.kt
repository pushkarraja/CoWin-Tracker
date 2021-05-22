package com.pushkarraja.vaccinetracker

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import es.dmoral.toasty.Toasty
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var getSlots : Button
    lateinit var pincodeEdt : EditText
    lateinit var centerRV : RecyclerView
    lateinit var loadingPB : ProgressBar
    lateinit var centerList : List<CenterRVModal>
    lateinit var centerRVAdapter: ConterRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSlots = findViewById(R.id.getSlots)
        pincodeEdt = findViewById(R.id.getPincode)
        centerRV = findViewById(R.id.idRcenters)
        loadingPB = findViewById(R.id.Pbloading)
        centerList = ArrayList<CenterRVModal>()

        getSlots.setOnClickListener {
            val pincode = pincodeEdt.text.toString()
            closeKeyBoard()

            if (pincode.length != 6) {
                Toasty.error(this, "Enter a Valid Pincode", Toast.LENGTH_SHORT).show()
            }
            else {
                (centerList as ArrayList<CenterRVModal>).clear()

                val c = Calendar.getInstance()
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                val dpd = DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
                    loadingPB.setVisibility(View.VISIBLE)
                    val datestr: String = """$dayOfMonth-${month+1}-$year"""
                    getAppointmentDetails(pincode,datestr)
                },
                year,
                month,
                day
                )
                dpd.show()
            }
        }

    }

    private fun getAppointmentDetails(pinCode: String, date : String){
        val url = "https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByPin?pincode=$pinCode&date=$date"
        val queue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(Request.Method.GET,url,null,{
            response->
                try {
                    val centerArray = response.getJSONArray("centers")
                    loadingPB.setVisibility(View.GONE)
                    if(centerArray.length().equals(0)){
                        Toasty.info(this,"No Slots Available",Toast.LENGTH_SHORT).show()
                    }

                    for(i in 0 until centerArray.length()){
                        val centerObj = centerArray.getJSONObject(i)
                        val centerName: String = centerObj.getString("name")
                        val centerAddress: String = centerObj.getString("address")
                        val centerFromTime: String = centerObj.getString("from")
                        val centerToTime: String = centerObj.getString("to")
                        val fee_type: String = centerObj.getString("fee_type")

                        val sessionObj = centerObj.getJSONArray("sessions").getJSONObject(0)
                        val slots1: Int = sessionObj.getInt("available_capacity_dose1")
                        val slots2: Int = sessionObj.getInt("available_capacity_dose2")
                        val ageLimit: Int = sessionObj.getInt("min_age_limit")
                        val vaccineName : String = sessionObj.getString("vaccine")

                        val center = CenterRVModal(
                            centerName, centerAddress,centerFromTime,centerToTime,fee_type,ageLimit,vaccineName,slots1,slots2
                        )

                        centerList = centerList+center

                    }
                    centerRVAdapter = ConterRVAdapter(centerList)
                    centerRV.layoutManager = LinearLayoutManager(this)
                    centerRV.adapter = centerRVAdapter


                }catch (e : JSONException){
                    loadingPB.setVisibility(View.GONE)
                    e.printStackTrace()
                }
        },
        {
            error->
            loadingPB.setVisibility(View.GONE)
            Toasty.error(this,"Error",Toast.LENGTH_SHORT).show()
        }
        )
        queue.add(request)


    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


}
