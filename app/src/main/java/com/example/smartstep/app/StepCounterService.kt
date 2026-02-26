@file:OptIn(FlowPreview::class)

package com.example.smartstep.app

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.smartstep.R
import com.example.smartstep.smart.domain.step_counter.StepTrackerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.sample
import org.koin.android.ext.android.inject
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.roundToInt

class StepCounterService : Service(), SensorEventListener {
    private val serviceScope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val sensorManager by lazy {
        getSystemService(SENSOR_SERVICE) as SensorManager
    }

    private val stepSensor by lazy {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
    }

    private val stepTrackerManager: StepTrackerManager by inject()
    private lateinit var pendingIntent: PendingIntent
    private var serviceStartTime: Long = 0
    private var observationJob: Job? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        stepSensor?.let {
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL,
                10_000_000
            )
        }
        serviceStartTime = System.currentTimeMillis()

        val intent = Intent(applicationContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        pendingIntent =
            PendingIntent.getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stopSelf()
        }
        return START_STICKY
    }

    private fun start() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification =
            createCustomNotification(steps = "0", calories = "0", 0)

        val usFormatter = NumberFormat.getNumberInstance(Locale.US)

        observationJob?.cancel()

        observationJob = stepTrackerManager.data
            .sample(1000L)
            .onEach { data ->
                val updatedNotification = createCustomNotification(
                    steps = usFormatter.format(data.steps.roundToInt()),
                    calories = usFormatter.format(data.calories.roundToInt()),
                    progress = (data.steps / data.goal * 100).roundToInt()
                )
                notificationManager.notify(1, updatedNotification)
            }
            .launchIn(serviceScope)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE)
            startForeground(1, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH)
        else
            startForeground(1, notification)
    }

    private fun createCustomNotification(
        steps: String,
        calories: String,
        progress: Int
    ): Notification {
        val remoteViews = RemoteViews(packageName, R.layout.notification_small)
        val remoteViewsExpanded = RemoteViews(packageName, R.layout.notification_expanded)

        remoteViews.setTextViewText(R.id.text_left, steps)
        remoteViews.setTextViewText(R.id.text_right, calories)
        remoteViews.setProgressBar(R.id.notification_progress, 100, progress, false)
        remoteViewsExpanded.setTextViewText(R.id.text_left, steps)
        remoteViewsExpanded.setTextViewText(R.id.text_right, calories)
        remoteViewsExpanded.setProgressBar(R.id.notification_progress, 100, progress, false)

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.steps)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViewsExpanded)
            .setStyle(NotificationCompat.DecoratedCustomViewStyle())
            .setOngoing(true)
            .setWhen(serviceStartTime)
            .setShowWhen(true)
            .setUsesChronometer(true)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        sensorManager.unregisterListener(this)
        serviceScope.cancel()
        stepTrackerManager.reset()
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

    override fun onSensorChanged(sensorEvent: SensorEvent?) {
        when (sensorEvent?.sensor?.type) {
            Sensor.TYPE_STEP_COUNTER -> {
                stepTrackerManager.updateSteps(sensorEvent.values[0])
            }
        }
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}