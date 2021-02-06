package de.jo20046.a132_radioapp_with_broadcastreceiver;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {


    final String[] items = {"1live", "dradio", "fusion101"};
    final String[] sources = {"https://1liveuni-lh.akamaihd.net/i/1LIVE_HDS@179577/index_1_a-p.m3u8",
            "https://st01.sslstream.dlf.de/dlf/01/128/mp3/stream.mp3", "http://hazel.torontocast.com:2910/;?type=http"};
    final String[] urls = {"https://www1.wdr.de/radio/1live/", "https://www.deutschlandradio.de/", "http://fusion101radio.com/"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.list_menu);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
        registerForContextMenu(listView);
        listView.setOnItemClickListener((parent, view, position, id) -> {
//            startMediaPlayer(position);
            Intent intent = new Intent(this, RadioActivity.class);
            intent.putExtra("Stream", sources[position]);
            intent.putExtra("URL", urls[position]);
            startActivity(intent);
        });
    }
}