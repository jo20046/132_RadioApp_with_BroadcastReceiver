package de.jo20046.a132_radioapp_with_broadcastreceiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class RadioActivity extends Activity {

    WebView webView;
    RadioService radioService;
    boolean serviceBound = false;
    String tag = "mytag";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_radio);
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(getIntent().getExtras().getString("URL"));

        Button btnStop = (Button) findViewById(R.id.btn_stop);
        Button btnPlay = (Button) findViewById(R.id.btn_play);
        Button btnPause = (Button) findViewById(R.id.btn_pause);
        btnStop.setOnClickListener(v -> {
            if (serviceBound) {
                radioService.stopMediaPlayer();
                btnStop.setEnabled(false);
                btnPlay.setEnabled(true);
                btnPlay.setText(R.string.play);
                btnPause.setEnabled(false);
            }
        });
        btnPlay.setOnClickListener(v -> {
            if (serviceBound) {
                radioService.restartMediaPlayer();
                btnStop.setEnabled(true);
                btnPlay.setEnabled(false);
                btnPlay.setText(R.string.play);
                btnPause.setEnabled(true);
            }
        });
        btnPause.setOnClickListener(v -> {
            if (serviceBound) {
                radioService.pauseMediaPlayer();
                btnStop.setEnabled(false);
                btnPlay.setEnabled(true);
                btnPlay.setText(R.string._continue);
                btnPause.setEnabled(false);
            }
        });

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStart() {
        Log.d(tag, "onStart() - Activity");
        super.onStart();
        Intent intent = new Intent(this, RadioService.class);
        intent.putExtra("Stream", getIntent().getExtras().getString("Stream"));
        startService(intent);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, RadioService.class);
        stopService(intent);
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            switch (wifiState) {
                case WifiManager.WIFI_STATE_ENABLED:
                    Toast.makeText(RadioActivity.this, "Wifi enabled", Toast.LENGTH_SHORT).show();
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    Toast.makeText(RadioActivity.this, "Wifi disabled", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    private final ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(tag, "onServiceConnected() - Activity");
            RadioService.MyBinder myBinder = (RadioService.MyBinder) service;
            radioService = myBinder.getService();
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };
}


