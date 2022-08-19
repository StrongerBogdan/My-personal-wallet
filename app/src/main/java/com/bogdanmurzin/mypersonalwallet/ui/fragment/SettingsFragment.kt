package com.bogdanmurzin.mypersonalwallet.ui.fragment

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.bogdanmurzin.mypersonalwallet.R
import com.bogdanmurzin.mypersonalwallet.common.Constants.COLOR_RESULT
import com.bogdanmurzin.mypersonalwallet.common.Constants.NOTIFICATION_WORKER_TAG
import com.bogdanmurzin.mypersonalwallet.common.Constants.PERMISSION_REQUEST_CODE
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

    private val getResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                startSaveService()
            }
        }

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
            if (checkPermission()) {
                startSaveService()
            } else {
                displayRationale()
            }

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

    private fun startSaveService() {
        requireContext().applicationContext.startService(
            Intent(
                requireContext().applicationContext,
                SaveToFileForegroundService::class.java
            )
        )
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

    private fun checkPermission(): Boolean {
        return if (SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            val result =
                ContextCompat.checkSelfPermission(requireContext(), READ_EXTERNAL_STORAGE)
            val result1 =
                ContextCompat.checkSelfPermission(requireActivity(), WRITE_EXTERNAL_STORAGE)
            result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory(INTENT_CATEGORY)
                intent.data = Uri.parse("package:${getApplicationContext<Context>().packageName}")
                getResult.launch(intent)
            } catch (e: Exception) {
                val intent = Intent()
                intent.action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                getResult.launch(intent)
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun displayRationale() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_permission_title))
            .setMessage(getString(R.string.dialog_permission_message))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.dialog_permission_button_positive)) { _, _ ->
                requestPermission()
            }
            .show()
    }

    companion object {
        const val REMINDER_HOURS = 19
        const val REMINDER_MINUTES = 0
        const val REMINDER_SECONDS = 0
        const val REMINDER_INTERVAL_DAYS = 1
        const val INTENT_CATEGORY = "android.intent.category.DEFAULT"
    }
}