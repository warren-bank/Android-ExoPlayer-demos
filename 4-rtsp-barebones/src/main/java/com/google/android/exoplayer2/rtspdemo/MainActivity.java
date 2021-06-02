/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.exoplayer2.rtspdemo;

import android.app.Activity;
import android.os.Bundle;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.rtsp.RtspMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.Util;

public final class MainActivity extends Activity {

  private static final String TAG = "MainActivity";

  private static final String RTSP_MEDIA_URI = "rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_175k.mov";
  private static final String RTSP_MIME_TYPE = "application/x-rtsp";

  private PlayerView playerView;
  private SimpleExoPlayer player;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main_activity);
    playerView = findViewById(R.id.player_view);
  }

  @Override
  public void onStart() {
    super.onStart();
    if (Util.SDK_INT > 23) {
      initializePlayer();
      if (playerView != null) {
        playerView.onResume();
      }
    }
  }

  @Override
  public void onResume() {
    super.onResume();
    if (Util.SDK_INT <= 23 || player == null) {
      initializePlayer();
      if (playerView != null) {
        playerView.onResume();
      }
    }
  }

  @Override
  public void onPause() {
    super.onPause();
    if (Util.SDK_INT <= 23) {
      if (playerView != null) {
        playerView.onPause();
      }
      releasePlayer();
    }
  }

  @Override
  public void onStop() {
    super.onStop();
    if (Util.SDK_INT > 23) {
      if (playerView != null) {
        playerView.onPause();
      }
      releasePlayer();
    }
  }

  private void initializePlayer() {
    SimpleExoPlayer player = new SimpleExoPlayer.Builder(getApplicationContext()).build();

    MediaItem mediaItem = new MediaItem.Builder().setUri(RTSP_MEDIA_URI).setMimeType(RTSP_MIME_TYPE).build();
    MediaSource mediaSource = (MediaSource) new RtspMediaSource.Factory().createMediaSource(mediaItem);

    if (playerView != null) {
      playerView.setPlayer(player);
    }

    player.setPlayWhenReady(true);
    player.setMediaSource(mediaSource);
    player.prepare();

    this.player = player;
  }

  private void releasePlayer() {
    if (playerView != null) {
      playerView.setPlayer(null);
    }

    if (player != null) {
      player.release();
      player = null;
    }
  }
}
