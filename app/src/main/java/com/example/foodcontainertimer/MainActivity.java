package com.example.foodcontainertimer;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MainActivity extends AppCompatActivity {

    private EditText dayEditText, hourEditText, minuteEditText, secondEditText;
    private TextView countdownTextView;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dayEditText = findViewById(R.id.dayEditText);
        hourEditText = findViewById(R.id.hourEditText);
        minuteEditText = findViewById(R.id.minuteEditText);
        secondEditText = findViewById(R.id.secondEditText);
        countdownTextView = findViewById(R.id.countdownTextView);

        findViewById(R.id.bt_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountdown();
            }
        });

        findViewById(R.id.bt_stop).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopCountdown();
            }
        });
    }

    private void startCountdown() {
        long totalTimeInMillis = getTotalTimeInMillis();

        if (totalTimeInMillis > 0) {
            countDownTimer = new CountDownTimer(totalTimeInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    updateCountdownText(millisUntilFinished);
                }

                @Override
                public void onFinish() {
                    updateCountdownText(0);
                    sendNotification("Countdown Finished", "Your countdown has finished!");
                }
            };

            countDownTimer.start();
        } else {
            Toast.makeText(this, "Please enter a valid time", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            updateCountdownText(0);
            Toast.makeText(this, "Countdown stopped", Toast.LENGTH_SHORT).show();
        }
    }

    private long getTotalTimeInMillis() {
        String daysStr = dayEditText.getText().toString();
        String hoursStr = hourEditText.getText().toString();
        String minutesStr = minuteEditText.getText().toString();
        String secondsStr = secondEditText.getText().toString();

        long days = daysStr.isEmpty() ? 0 : Long.parseLong(daysStr);
        long hours = hoursStr.isEmpty() ? 0 : Long.parseLong(hoursStr);
        long minutes = minutesStr.isEmpty() ? 0 : Long.parseLong(minutesStr);
        long seconds = secondsStr.isEmpty() ? 0 : Long.parseLong(secondsStr);

        return (days * 24 * 60 * 60 + hours * 60 * 60 + minutes * 60 + seconds) * 1000;
    }

    private void updateCountdownText(long millisUntilFinished) {
        int seconds = (int) (millisUntilFinished / 1000) % 60;
        int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
        int hours = (int) ((millisUntilFinished / (1000 * 60 * 60)) % 24);
        int days = (int) ((millisUntilFinished / (1000 * 60 * 60 * 24)));

        String countdownText = String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds);
        countdownTextView.setText(countdownText);
    }

    private void sendNotification(String title, String message) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "YOUR_CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_launcher_background) // Set your notification icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is not in the Support Library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}