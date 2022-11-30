package com.example.myactivityapplication

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import androidx.constraintlayout.widget.ConstraintSet.Constraint
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.myactivityapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn2ndActivity.setOnClickListener {
            // 기본적으로 context 끼리는 자원을 공유하지 않는다.
            // -> Activity간 데이터, 객체 공유가 없다.
            // 정보 공유를 위해 INTENT를 사용.
            val intent = Intent(this, SecondActivity::class.java)

            startActivity(intent)
        }

        binding.btnService.setOnClickListener {
            val intent = Intent(this, ProgressService::class.java)
            //startService(intent) -> 예전에 사용하던 방식.
            ContextCompat.startForegroundService(this, intent)
        }

        binding.btnWorker.setOnClickListener {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .setRequiresCharging(true)
                .build()
            val workRequest = OneTimeWorkRequest.Builder(CountWorker::class.java)
                .setConstraints(constraints)
                .build()

            WorkManager.getInstance(applicationContext).enqueue(workRequest)
        }

        binding.btnAlarm.setOnClickListener {
            val intent = Intent(this, AlarmReceiver::class.java)

            intent.putExtra("message", "Exact Alarm Triggered")

            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            getSystemService(AlarmManager::class.java).setExact(
                AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime() + 10*1000,
                pendingIntent)
        }
    }
}