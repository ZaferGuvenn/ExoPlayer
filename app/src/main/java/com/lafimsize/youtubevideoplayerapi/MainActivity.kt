package com.lafimsize.youtubevideoplayerapi

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.media3.common.MediaItem
import androidx.media3.common.MimeTypes
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.exoplayer.ExoPlayer
import com.lafimsize.youtubevideoplayerapi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var player: ExoPlayer? = null
    private var playWhenReady=true
    private var playbackPosition=0L
    private var mediaItemIndex=0

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)


        viewBinding.videoView.useController=false
        viewBinding.button.text=getString(R.string.main_act_video_pause)

        viewBinding.button.setOnClickListener {
            changeVideoStatus()
        }


    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    private fun changeVideoStatus(){
        player?.let {
            if (it.isPlaying){
                it.pause()
                viewBinding.button.text=getString(R.string.main_act_video_resume)
            }else{
                it.play()
                viewBinding.button.text=getString(R.string.main_act_video_pause)
            }
        }
    }

    private fun initializePlayer(){
        player=ExoPlayer.Builder(this).build()
            .also {exoPlayer->
                viewBinding.videoView.player=exoPlayer
                val mediaItem=MediaItem.Builder()
                    .setUri(getString(R.string.media_url_dash))
                    .setMimeType(MimeTypes.APPLICATION_MPD)
                    .build()
                exoPlayer.setMediaItems(listOf(mediaItem),mediaItemIndex,playbackPosition)
                exoPlayer.playWhenReady=playWhenReady
                exoPlayer.prepare()
                exoPlayer.repeatMode= Player.REPEAT_MODE_OFF
                exoPlayer.addListener(object : Player.Listener{
                    override fun onVideoSizeChanged(videoSize: VideoSize) {
                        super.onVideoSizeChanged(videoSize)
                        //buraya kadar bir progress bar kullanarak yükleme ekranı gösterebilirsin!
                    }
                })

            }
    }

    private fun releasePlayer(){
        player?.let { exoPlayer ->

            playbackPosition=exoPlayer.currentPosition
            mediaItemIndex=exoPlayer.currentMediaItemIndex
            playWhenReady=exoPlayer.playWhenReady
            exoPlayer.release()
        }
        player=null
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }
}