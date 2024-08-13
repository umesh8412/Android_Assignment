package com.umesh.androidassignment.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.umesh.androidassignment.BaseAdapter
import com.umesh.androidassignment.BaseFragment
import com.umesh.androidassignment.R
import com.umesh.androidassignment.databinding.FragmentLinksBinding
import com.umesh.androidassignment.databinding.ItemAnalyticsBinding
import com.umesh.androidassignment.databinding.ItemLinksBinding
import com.umesh.androidassignment.network.AnalyticsItem
import com.umesh.androidassignment.network.ClickData
import com.umesh.androidassignment.network.DashboardResponse
import com.umesh.androidassignment.network.DashboardViewModel
import com.umesh.androidassignment.network.Link
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class FragmentLinks : BaseFragment<FragmentLinksBinding>() {
    private var username:String="Team"
    private val viewModel: DashboardViewModel by viewModels()
    private lateinit var adapter: BaseAdapter<AnalyticsItem>
    private lateinit var linkAdapter: BaseAdapter<Link>
    private lateinit var lineList: ArrayList<Entry>
    private lateinit var lineDataSet: LineDataSet
    private lateinit var lineData: LineData
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentLinksBinding {
        return FragmentLinksBinding.inflate(inflater, container, false)

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpObservers()
//        setupChart(dummyClickData)
        setGreetingMessage()
        binding.tvName.text = username

    }
    override fun setUpViews() {
    }
    override fun setUpClickListeners() {
    }
    @SuppressLint("SetTextI18n")
    private fun setGraphValues(urlChartResponse: Map<String, Int>?) {
        lineList = ArrayList()

        val timeFormat = SimpleDateFormat("HH:mm", Locale.US)

        // Prepare to find the time ranges with counts
        val timeRanges = mutableListOf<String>()
        var startTime: String? = null
        var endTime: String? = null
        var hasCount = false

        // Populate lineList and timeRanges
        urlChartResponse?.forEach { (key, value) ->
            val time = timeFormat.parse(key)
            val minutesFromMidnight = (time?.hours ?: 0) * 60 + (time?.minutes ?: 0)
            lineList.add(Entry(minutesFromMidnight.toFloat(), value.toFloat()))

            if (value > 0) {
                if (!hasCount) {
                    startTime = key
                }
                endTime = key
                hasCount = true
            } else if (hasCount) {
                timeRanges.add("${startTime} - ${endTime}")
                startTime = null
                endTime = null
                hasCount = false
            }
        }
        if (hasCount) {
            timeRanges.add("${startTime} - ${endTime}")
        }
        val timeRangeText = timeRanges.joinToString(", ")
        binding.tvDuration.text = timeRangeText
        lineDataSet = LineDataSet(lineList, null).apply {
            color = Color.parseColor("#0E6FFF")
            setDrawValues(false)
            setDrawCircles(false)
            setDrawFilled(true)
            fillDrawable = createGradientDrawable(Color.parseColor("#0E6FFF"), Color.TRANSPARENT)
        }
        lineData = LineData(lineDataSet)
        binding.chart.data = lineData

        val xAxis = binding.chart.xAxis
        xAxis?.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawAxisLine(true)
            setDrawLabels(true)
            valueFormatter = object : ValueFormatter() {
                private val format = SimpleDateFormat("HH:mm", Locale.US)
                override fun getFormattedValue(value: Float): String {
                    val hours = (value.toInt() / 60) % 24
                    val minutes = value.toInt() % 60
                    return String.format("%02d:%02d", hours, minutes)
                }
            }
            granularity = 240f

            labelCount = 6
            axisMinimum = 0f
            axisMaximum = 24 * 60f
        }

        with(binding.chart) {
            axisRight?.isEnabled = false
            description?.isEnabled = false
            legend?.isEnabled = false
            axisLeft?.apply {
                axisMinimum = 0f // Ensure the y-axis starts from 0
                axisMaximum = (urlChartResponse?.values?.maxOrNull()?.toFloat()?.let { Math.ceil((it / 5).toDouble()) * 5 } ?: 5f).toFloat()
                granularity = 1f // Show labels every unit
            }

            invalidate()
        }
    }

    private fun setGreetingMessage() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)

        val greeting = when {
            hour in 0..11 -> "Good Morning"
            hour in 12..17 -> "Good Afternoon"
            else -> "Good Evening"
        }

        binding.tvHello.text = greeting
    }

    private fun createGradientDrawable(startColor: Int, endColor: Int): GradientDrawable {
        return GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(startColor, endColor)
        )
    }
    private fun rvLinks(data: DashboardResponse) {
        Log.d("FragmentLinks", "Initializing linkAdapter")
        linkAdapter = BaseAdapter<Link>().apply {
            expressionOnCreateViewHolder = { parent ->
                ItemLinksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }
            expressionViewHolderBinding = { item, binding ->
                val itemBinding = binding as ItemLinksBinding
                itemBinding.apply {
                    linkName.text = item.title
                    tvLinkDate.text = formatTime(item.created_at)
                    tvCountClicks.text = item.total_clicks.toString()
                    tvUrl.text = item.smart_link
                    Glide.with(requireContext())
                        .load(item.original_image)
                        .placeholder(R.drawable.link)
                        .error(R.drawable.link)
                        .into(imgCompany)
                }
            }
        }
        linkAdapter.listOfItems = data.data.top_links.toMutableList()
        binding.rvLinks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvLinks.adapter = linkAdapter
        Log.d("FragmentLinks", "linkAdapter set to RecyclerView")
    }
    private fun setupChipGroupListener(data: DashboardResponse) {
        binding.toggleGroup.addOnButtonCheckedListener { toggleButtonGroup, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btnTopLinks -> {
                        linkAdapter.listOfItems?.clear()
                        linkAdapter.listOfItems?.addAll(data.data.top_links)
                    }

                    R.id.btnRecentLinks -> {
                        linkAdapter.listOfItems?.clear()
                        linkAdapter.listOfItems?.addAll(data.data.recent_links)
                    }
                }
                linkAdapter.notifyDataSetChanged()
            }
        }
    }
    private fun rvAnalytic(response: DashboardResponse){
        adapter = BaseAdapter<AnalyticsItem>().apply {
            expressionOnCreateViewHolder = { parent ->
                ItemAnalyticsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            }
            expressionViewHolderBinding = { item, binding ->
                val itemBinding = binding as ItemAnalyticsBinding
                itemBinding.tv1.text = item.tv1Text
                itemBinding.tv2.text = item.tv2Text
                itemBinding.imgClick.setImageResource(item.imgResource)
            }
        }
        binding.rvAnalytic.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvAnalytic.adapter = adapter

        val apiData = listOf(if (response.today_clicks.toString().isEmpty()) "N/A" else response.today_clicks.toString(),
            response.top_location?.takeIf { it.isNotEmpty() } ?: "--",
            response.top_source?.takeIf { it.isNotEmpty() } ?: "--"
        )

        val customData = listOf(
            AnalyticsItem(R.drawable.today_clicks,apiData[0], "Today's clicks"),
            AnalyticsItem(R.drawable.location,apiData[1], "Top Location"),
            AnalyticsItem(R.drawable.top_source,apiData[2], "Top source")
        )
        adapter.listOfItems = customData.toMutableList()

    }

    override fun setUpObservers() {
        viewModel.loadDashboardData()
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
        }
        viewModel.responseData.observe(viewLifecycleOwner) { response ->
            response?.let {
                rvAnalytic(it)
                rvLinks(it)
                setupChipGroupListener(it)
                setGraphValues(it.data.overall_url_chart)
            }
        }
        viewModel.failure.observe(viewLifecycleOwner) { error ->
            error?.let {

            }
        }

    }

}