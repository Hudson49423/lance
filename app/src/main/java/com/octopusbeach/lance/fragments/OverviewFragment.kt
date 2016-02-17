package com.octopusbeach.lance.fragments;


import android.app.Fragment
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.mikephil.charting.charts.HorizontalBarChart

import com.octopusbeach.lance.R;
import com.octopusbeach.lance.views.HourChart

class OverviewFragment : Fragment() {
    companion object{
        val ID = 0
    }

    var totalWeeklyHours: HourChart? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        totalWeeklyHours = view.findViewById(R.id.weekly_hours_chart) as HourChart
        totalWeeklyHours?.setHours(35f)


        return view
    }
}
