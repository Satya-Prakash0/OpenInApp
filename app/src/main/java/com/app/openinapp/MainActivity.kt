package com.app.openinapp

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.app.openinapp.databinding.ActivityMainBinding
import com.app.openinapp.viewmodel.MainViewModel
import com.app.openinapp.adapter.MyFragmentAdapter
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var mainViewModel: MainViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var adapter : MyFragmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)

        mainViewModel= ViewModelProvider(this).get(MainViewModel::class.java)

        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Top Links"))
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Recent Links"))

        val fragmentManager :FragmentManager =supportFragmentManager
        adapter = MyFragmentAdapter(fragmentManager,lifecycle)
        binding.viewPager2.adapter =adapter

        val calendar :Calendar =Calendar.getInstance()
        val mdformat =SimpleDateFormat("HH")
        val strDate : String =mdformat.format(calendar.time)
        val time =strDate.toInt()

        binding.viewPager2.isUserInputEnabled =false
        binding.viewPager2.setPageTransformer(null)
        binding.tabLayout.addOnTabSelectedListener(object  : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                binding.viewPager2.setCurrentItem(tab!!.position,false)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

        setGreeting(time)
        fetchData()
        fetchDataAndPlotChart()
    }

    private fun setGreeting(time:Int){
        when{
            time in 13..16 -> binding.txtGreeting.setText("Good AfterNoon")
            time < 12 -> binding.txtGreeting.setText("Good Morning")
            time > 17 -> binding.txtGreeting.setText("Good Evening")
        }
    }

    private fun fetchData(){
        mainViewModel.dashBoardData.observe(this) {

            binding.txtTotalClicks.text = it.total_clicks.toString()
            binding.txtTotalClicks.text = it.today_clicks.toString()
            binding.txtTopLocation.text = if(it.top_location.isEmpty()) "N/A" else it.top_location
            binding.txtTotalLinks.text = it.total_links.toString()
            binding.txtBestTime.text = if(it.startTime.isEmpty()) "NA" else  it.startTime
            binding.txtTopSource.text = if(it.top_source.isEmpty()) "NA" else it.top_source
        }
    }

    private fun fetchDataAndPlotChart() {
        mainViewModel.dashBoardData.observe(this) {

            val overallUrlChart = it.data.overall_url_chart
            val entries = mutableListOf<Entry>()

            if (overallUrlChart != null) {
                overallUrlChart.forEach { (date, clickCount) ->
                    val floatDate = date.substringAfterLast('-').toFloat()
                    entries.add(Entry(floatDate, clickCount.toFloat()))
                }
            }

            entries.sortBy { it.x }
            val groupedEntries = groupDataByInterval(entries, 5)
            val xAxis = binding.lineChart.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(
                generateXAxisLabels(
                    groupedEntries,
                    overallUrlChart.keys.toList()
                )
            )
            binding.lineChart.setPinchZoom(false)
            binding.lineChart.isDragEnabled = false
            binding.lineChart.isScaleXEnabled = false
            xAxis.granularity = 1f
            xAxis.setDrawGridLines(false)
            xAxis.labelRotationAngle = -45f
            binding.lineChart.xAxis.labelRotationAngle = 0f
            binding.lineChart.isDoubleTapToZoomEnabled = false
            binding.lineChart.description.text = "Overview"
            binding.lineChart.description.textColor = Color.BLACK

            binding.lineChart.description.xOffset = 0f
            binding.lineChart.description.yOffset = -20f


            val dataSet = LineDataSet(groupedEntries, "Click Count")
            dataSet.color = Color.parseColor("#0e6ffe")
            dataSet.setCircleColor(Color.parseColor("#0e6ffe"))
            dataSet.setDrawCircleHole(false)
            dataSet.enableDashedLine(12f, 8f, 0f)
            dataSet.lineWidth = 4f
            val lineData = LineData(dataSet)
            binding.lineChart.data = lineData
            binding.lineChart.invalidate()
        }
    }

    private fun generateXAxisLabels(groupedEntries: List<Entry>, dates: List<String>): List<String> {
        val xValues = mutableListOf<String>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val calendar = Calendar.getInstance()

        val interval = 5
        val totalDates = dates.size

        if (totalDates > 0) {
            val endIndex = totalDates - 1
            val startIndex = maxOf(endIndex - (interval * 6), 0)
            val step = maxOf((endIndex - startIndex) / 6, 1)

            for (i in startIndex..endIndex step step) {
                val date = dates[i]
                val parsedDate = dateFormat.parse(date)
                calendar.time = parsedDate ?: Date(0)
                val formattedDate = SimpleDateFormat("MMM dd", Locale.US).format(calendar.time)
                xValues.add(formattedDate)
            }
        }

        return xValues
    }
    private fun groupDataByInterval(entries: List<Entry>, interval: Int): List<Entry> {
        val groupedEntries = mutableListOf<Entry>()
        var sum = 0f
        var count = 0
        var groupIndex = 0

        entries.forEachIndexed { index, entry ->
            sum += entry.y
            count++

            if (count == interval || index == entries.size - 1) {
                val average = sum / count
                val groupEntry = Entry(groupIndex.toFloat(), average)
                groupedEntries.add(groupEntry)
                sum = 0f
                count = 0
                groupIndex++
            }
        }

        return groupedEntries
    }
}

