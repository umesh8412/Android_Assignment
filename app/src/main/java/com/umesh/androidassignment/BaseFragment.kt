package com.umesh.androidassignment

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.TimeZone

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null

    protected val binding: VB
        get() = _binding ?: throw IllegalStateException("Binding is accessed before it's initialized or after it's destroyed")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }
    abstract fun inflateBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    fun handleBack(callback: () -> Unit) {
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                callback()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun setUpViews()
    abstract fun setUpClickListeners()
    abstract fun setUpObservers()

    fun showSnackBar(message: String){
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }


    fun formatTime(timeISO: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")
        val then = inputFormat.parse(timeISO)
        val now = Date()

        val diffInMillis = now.time - (then?.time ?: now.time)
        val diffInMinutes = diffInMillis / (60 * 1000)
        val diffInHours = diffInMillis / (60 * 60 * 1000)
        val diffInDays = diffInMillis / (24 * 60 * 60 * 1000)
        val diffInWeeks = diffInDays / 7
        val diffInMonths = diffInDays / 30

        return when {
            diffInMinutes < 1 -> "just now"
            diffInMinutes < 60 -> "$diffInMinutes min ago"
            diffInHours < 24 -> "$diffInHours hour${if (diffInHours > 1) "s" else ""} ago"
            diffInDays < 7 -> "$diffInDays day${if (diffInDays > 1) "s" else ""} ago"
            diffInWeeks < 4 -> "$diffInWeeks week${if (diffInWeeks > 1) "s" else ""} ago"
            diffInMonths <= 12 -> "$diffInMonths month${if (diffInMonths > 1) "s" else ""} ago"
            else -> {
                val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                outputFormat.format(then ?: now)
            }
        }
    }
    class TimeValueFormatter : ValueFormatter() {
        private val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            // Convert the float value back to a timestamp
            val date = Date(value.toLong())
            return sdf.format(date)
        }
    }
}