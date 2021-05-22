package com.pushkarraja.vaccinetracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ConterRVAdapter(private val centerList : List<CenterRVModal>) :
    RecyclerView.Adapter<ConterRVAdapter.CenterRVViewHolder>() {
    class CenterRVViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val centerNameTV: TextView = itemView.findViewById(R.id.center_name)
        val centerAddressTV: TextView = itemView.findViewById(R.id.Location)
        val centerTimingsTV: TextView = itemView.findViewById(R.id.Timings)
        val vaccineNameTV: TextView = itemView.findViewById(R.id.Vaccine)
        val vaccineFeesTV: TextView = itemView.findViewById(R.id.vaccineFees)
        val ageLimitTV: TextView = itemView.findViewById(R.id.tvagelimit)
        val slotsTV: TextView = itemView.findViewById(R.id.tvSlots1)
        val slotsTV2 : TextView = itemView.findViewById(R.id.tvSlots2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CenterRVViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.center_rv_item,parent,false)
        return CenterRVViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CenterRVViewHolder, position: Int) {
        val center = centerList[position]
        holder.centerNameTV.text = center.centerName
        holder.centerAddressTV.text = center.centerAddress
        holder.centerTimingsTV.text = ("From : "+center.centerFromTime+" To : "+center.centerToTime)
        holder.vaccineNameTV.text = center.vaccineName
        holder.vaccineFeesTV.text = center.fee_type
        holder.ageLimitTV.text = ("Age Limit : "+center.ageLimit.toString())
        holder.slotsTV.text = ("Dose 1 : "+center.slots1.toString())
        holder.slotsTV2.text = ("Dose 2 : "+center.slots2.toString())
    }

    override fun getItemCount(): Int {
        return centerList.size
    }

}