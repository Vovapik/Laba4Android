package com.example.laba4;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.media3.common.MediaItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {

    private ExoPlayer player;
    private PlayerView playerView;

    private ActivityResultLauncher<String> filePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playerView = findViewById(R.id.playerView);

        player = new ExoPlayer.Builder(this).build();
        playerView.setPlayer(player);

        Button btnPlay = findViewById(R.id.btnPlay);
        Button btnPause = findViewById(R.id.btnPause);
        Button btnStop = findViewById(R.id.btnStop);
        Button btnChooseFile = findViewById(R.id.btnChooseFile);
        Button btnLoadUrl = findViewById(R.id.btnLoadUrl);
        EditText etUrl = findViewById(R.id.etUrl);
        RadioGroup rgMediaType = findViewById(R.id.rgMediaType);

        btnPlay.setOnClickListener(v -> player.play());

        btnPause.setOnClickListener(v -> player.pause());

        btnStop.setOnClickListener(v -> player.stop());

        filePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        playMedia(uri);
                    }
                }
        );

        btnChooseFile.setOnClickListener(v -> {
            int selectedId = rgMediaType.getCheckedRadioButtonId();

            String mimeType;

            if (selectedId == R.id.rbAudio) {
                mimeType = "audio/*";
            } else {
                mimeType = "video/*";
            }

            filePickerLauncher.launch(mimeType);
        });

        btnLoadUrl.setOnClickListener(v -> {
            String url = etUrl.getText().toString();
            if (!url.isEmpty()) {
                Uri uri = Uri.parse(url);
                playMedia(uri);
            }
        });
    }

    private void playMedia(Uri uri) {
        MediaItem mediaItem = MediaItem.fromUri(uri);
        player.setMediaItem(mediaItem);
        player.prepare();
        player.play();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }
}