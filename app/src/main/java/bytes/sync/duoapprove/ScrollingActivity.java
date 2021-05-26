package bytes.sync.duoapprove;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;

import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

import bytes.sync.duoapprove.databinding.ActivityScrollingBinding;
import bytes.sync.duoapprove.databinding.ContentScrollingBinding;

public class ScrollingActivity extends AppCompatActivity {

    private final String TAG = ScrollingActivity.class.getName();
    private ActivityScrollingBinding binding;
    private PowerManager powerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityScrollingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = binding.toolbarLayout;
        toolBarLayout.setTitle(getTitle());

        binding.contentLayout.listenerSettingsButton.setOnClickListener(v -> openNotificationListenerSettingsApp());

        binding.contentLayout.optimizationSettingsButton.setOnClickListener(v -> openBatteryOptimizationSettingsApp());

        binding.contentLayout.timestampTextview.setText(getLastNotificationTimestamp());

        powerManager = (PowerManager) getSystemService(POWER_SERVICE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isListenerEnabled = NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName());
        if(isListenerEnabled) {
            Log.d(TAG, "notification listener enabled, app will listen for notifications");
            binding.contentLayout.listenerStatusTextview.setText("ENABLED");
        } else {
            Log.d(TAG, "notification listener disabled, app will not work");
            binding.contentLayout.listenerStatusTextview.setText("DISABLED");
        }

        boolean isOptimizationIgnored = powerManager.isIgnoringBatteryOptimizations(getPackageName());
        if(isOptimizationIgnored) {
           binding.contentLayout.optimizationStatusTextview.setText("ENABLED");
        } else {
            binding.contentLayout.optimizationStatusTextview.setText("DISABLED");
        }
    }

    private void openNotificationListenerSettingsApp() {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        startActivity(intent);
    }

    private void openBatteryOptimizationSettingsApp() {
        Intent intent = new Intent(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
        startActivity(intent);
    }

    private String getLastNotificationTimestamp() {
        SharedPreferences sharedPreferences = getSharedPreferences("preferences", Context.MODE_PRIVATE);
        return sharedPreferences.getString("TIMESTAMP", "No data yet");
    }

}