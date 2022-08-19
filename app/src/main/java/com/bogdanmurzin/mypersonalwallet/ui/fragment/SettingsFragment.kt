package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.common.Constants.COLOR_RESULT
import com.bogdanmurzin.mypersonalwallet.common.Constants.NOTIFICATION_WORKER_TAG
import com.bogdanmurzin.mypersonalwallet.databinding.FragmentSettingsBinding
import com.bogdanmurzin.mypersonalwallet.services.SaveToFileForegroundService
import com.bogdanmurzin.mypersonalwallet.ui.viewmodel.SettingsViewModel
import com.bogdanmurzin.mypersonalwallet.util.Event
import com.bogdanmurzin.mypersonalwallet.util.NotifyWorker
import com.bogdanmurzin.mypersonalwallet.util.getNavigationResultLiveData
import com.google.android.material.snackbar.BaseTransientBottomBar.LENGTH_LONG
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)

        binding.settingsThemeColor.setOnClickListener {
            viewModel.openThemeColorChooser()
        }
        binding.reminderSwitch.setOnClickListener {
            val isChecked = (it as SwitchMaterial).isChecked
            viewModel.changeReminderSwitch(isChecked)

            if (isChecked) {
                setPeriodicallySendingNotifications()
            } else {
                WorkManager.getInstance(requireContext())
                    .cancelAllWorkByTag(NOTIFICATION_WORKER_TAG)
            }
        }
        binding.saveBtn.setOnClickListener {
            requireContext().applicationContext.startService(
                Intent(requireContext().applicationContext, SaveToFileForegroundService::class.java)
            )
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.action.observe(viewLifecycleOwner) { event ->
            if (event == Event.OpenThemeColorChooser) {
                findNavController().navigate(
                    SettingsFragmentDirections
                        .actionSettingsFragmentToSettingsFragmentDialog()
                )
            }
        }

        viewModel.reminderOn.observe(viewLifecycleOwner) { isReminderOn ->
            binding.reminderSwitch.isChecked = isReminderOn
        }

        // get result from Choosing color
        val themeColorResult =
            getNavigationResultLiveData<Int>(COLOR_RESULT)
        themeColorResult?.observe(viewLifecycleOwner) { themeId ->
            viewModel.changeThemeColor(themeId)
        }
    }

    private fun setPeriodicallySendingNotifications() {
        val customCalendar = Calendar.getInstance()
        val currentTime = System.currentTimeMillis()

        // Set 19.00
        customCalendar.set(Calendar.HOUR_OF_DAY, REMINDER_HOURS)
        customCalendar.set(Calendar.MINUTE, REMINDER_MINUTES)
        customCalendar.set(Calendar.SECOND, REMINDER_SECONDS)
        // Add 1 day
        customCalendar.add(Calendar.DATE, REMINDER_INTERVAL_DAYS)

        val customTime = customCalendar.time.time
        if (customTime > currentTime) {
            val delay = customTime - currentTime
            scheduleNotification(delay)

            val titleNotificationSchedule = getString(R.string.notification_schedule_title)
            val patternNotificationSchedule = getString(R.string.notification_schedule_pattern)
            Snackbar.make(
                binding.root,
                titleNotificationSchedule + SimpleDateFormat(
                    patternNotificationSchedule, Locale.getDefault()
                ).format(customCalendar.time).toString(),
                LENGTH_LONG
            ).show()
        } else {
            val errorNotificationSchedule = getString(R.string.notification_schedule_error)
            Snackbar.make(binding.root, errorNotificationSchedule, LENGTH_LONG).show()
        }
    }

    private fun scheduleNotification(delay: Long) {
        val notificationWork =
            PeriodicWorkRequest
                .Builder(NotifyWorker::class.java, REMINDER_INTERVAL_DAYS.toLong(), TimeUnit.DAYS)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag(NOTIFICATION_WORKER_TAG)
                .build()

        val instanceWorkManager = WorkManager.getInstance(requireContext())
        instanceWorkManager
            .enqueueUniquePeriodicWork(
                NOTIFICATION_WORKER_TAG,
                ExistingPeriodicWorkPolicy.REPLACE,
                notificationWork
            )
    }

    companion object {
        const val REMINDER_HOURS = 19
        const val REMINDER_MINUTES = 0
        const val REMINDER_SECONDS = 0
        const val REMINDER_INTERVAL_DAYS = 1
    }

}