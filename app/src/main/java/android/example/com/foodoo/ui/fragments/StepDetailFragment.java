package android.example.com.foodoo.ui.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.example.com.foodoo.R;
import android.example.com.foodoo.config.Config;
import android.example.com.foodoo.databinding.StepDetailBinding;
import android.example.com.foodoo.models.Step;
import android.example.com.foodoo.ui.StepDetailActivity;
import android.example.com.foodoo.ui.StepListActivity;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.app.NotificationCompat.MediaStyle;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link StepListActivity}
 * in two-pane mode (on tablets) or a {@link StepDetailActivity}
 * on handsets.
 */
public class StepDetailFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = StepDetailFragment.class.getSimpleName();
    private StepDetailBinding binding;
    private Step step;
    private int currentStepIndex = 0;
    private int lastStepIndex = 0;
    private SimpleExoPlayer exoPlayer;
    private static MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder mStateBuilder;
    private NotificationManager mNotificationManager;
    private OnStepDetailNavigationClickObserver stepDetailNavigationChangeObserver;
    private long currentPosition = -1;


    public interface OnStepDetailNavigationClickObserver {
        void onCurrentStepDetailChanged(int currentStepIndex);
    }

    public StepDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            currentPosition = savedInstanceState.getLong("currentPos");
        }

        Bundle bundle = getArguments();
        if (getArguments().containsKey(Config.DATA_KEY_STEP)) {
            step = getArguments().getParcelable(Config.DATA_KEY_STEP);
            if (step != null) {
                currentStepIndex = step.getPosition();
                // Initialize the Media Session.
                initializeMediaSession();
                getActivity().setTitle(step.getShortDescription());
            }
        }

        if (getArguments().containsKey(Config.DATA_KEY_LAST_STEP_INDEX)) {
            lastStepIndex = getArguments().getInt(Config.DATA_KEY_LAST_STEP_INDEX);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // this is being called before onStop or onPause on my device which is
        // a Motorola G5 running Android 7.0
        // So I think it's quite unlikely that the implementations I've seen on the web, actually work

        if (currentPosition != -1) {
            outState.putLong("currentPos", currentPosition);
        } else {
            if (exoPlayer != null)
                outState.putLong("currentPos", exoPlayer.getCurrentPosition());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.step_detail, container, false);
        binding.setStep(step);



        if (step != null) {
            // Initialize the player view.
            if ( !TextUtils.isEmpty(step.getVideoURL()) ) {
                // Load an image as the background image until whatever
                binding.playerView.setDefaultArtwork(BitmapFactory.decodeResource
                        (getResources(), R.drawable.cupcake));

                initializePlayer(Uri.parse(step.getVideoURL()));



            } else {
                binding.playerView.setVisibility(View.GONE);
            }

            if (currentStepIndex == 0) {
                binding.stepPrevious.setVisibility(View.INVISIBLE);
            }
            if (currentStepIndex == lastStepIndex) {
                binding.stepNext.setVisibility(View.INVISIBLE);
            }

            // This translates to checking if in 2 pane mode or not
            if(getActivity().getLocalClassName().equals("ui.StepDetailActivity")) {
                binding.stepNext.setOnClickListener(navigationButtonsOnclickListener);
                binding.stepPrevious.setOnClickListener(navigationButtonsOnclickListener);
            } else {
                binding.stepPrevious.setVisibility(View.INVISIBLE);
                binding.stepNext.setVisibility(View.INVISIBLE);
            }
        }
        // returns root view
        return binding.getRoot();
    }

    private View.OnClickListener navigationButtonsOnclickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            if (view.equals(binding.stepNext)) {

                binding.stepPrevious.setVisibility(View.VISIBLE);
                if (currentStepIndex < lastStepIndex) {
                    stepDetailNavigationChangeObserver.onCurrentStepDetailChanged(++currentStepIndex);
                }
                if (currentStepIndex >= lastStepIndex) {
                    binding.stepNext.setVisibility(View.INVISIBLE);
                }

            } else {

                binding.stepNext.setVisibility(View.VISIBLE);
                if (currentStepIndex > 0) {
                    stepDetailNavigationChangeObserver.onCurrentStepDetailChanged(--currentStepIndex);
                }
                if (currentStepIndex <= 0) {
                    binding.stepPrevious.setVisibility(View.INVISIBLE);
                }

            }
        }
    };

    /**
     * Initializes the Media Session to be enabled with media buttons, transport controls, callbacks
     * and media controller.
     */
    private void initializeMediaSession() {

        // Create a MediaSessionCompat.
        mediaSession = new MediaSessionCompat(getContext(), TAG);

        // Enable callbacks from MediaButtons and TransportControls.
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);

        // Do not let MediaButtons restart the player when the app is not visible.
        mediaSession.setMediaButtonReceiver(null);

        // Set an initial PlaybackState with ACTION_PLAY, so media buttons can start the player.
        mStateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mediaSession.setPlaybackState(mStateBuilder.build());


        // MySessionCallback has methods that handle callbacks from a media controller.
        mediaSession.setCallback(new MySessionCallback());

        // Start the Media Session since the activity is active.
        mediaSession.setActive(true);

    }


    /**
     * Release the player (by the latest) when the activity is destroyed.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mediaSession.setActive(false);
    }

    /**
     * Hint by Code Reviewer
     */
    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer(Uri.parse(step.getVideoURL()));
        }
    }

    /**
     * Hint by Code Reviewer
     */
    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
            initializePlayer(Uri.parse(step.getVideoURL()));
        }
    }

    /**
     * Hint by Code Reviewer
     */
    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    /**
     * Hint by Code Reviewer
     */
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }
    /**
     * Called when the timeline and/or manifest has been refreshed.
     * <p>
     * Note that if the timeline has changed then a position discontinuity may also have occurred.
     * For example the current period index may have changed as a result of periods being added or
     * removed from the timeline. The will <em>not</em> be reported via a separate call to
     * {@link #onPositionDiscontinuity()}.
     *
     * @param timeline The latest timeline. Never null, but may be empty.
     * @param manifest The latest manifest. May be null.
     */
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    /**
     * Called when the available or selected tracks change.
     *
     * @param trackGroups     The available tracks. Never null, but may be of length zero.
     * @param trackSelections The track selections for each {@link //Renderer}. Never null and always
     *                        of length {@link #//getRendererCount()}, but may contain null elements.
     */
    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    /**
     * Called when the player starts or stops loading the source.
     *
     * @param isLoading Whether the source is currently being loaded.
     */
    @Override
    public void onLoadingChanged(boolean isLoading) {
        if (!isLoading) {
            Toast.makeText(getContext(), "zzZZZZ", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Called when the value returned from either {@link #//getPlayWhenReady()} or
     * {@link #//getPlaybackState()} changes.
     *
     * @param playWhenReady Whether playback will proceed when ready.
     * @param playbackState One of the {@code STATE} constants defined in the {@link ExoPlayer}
     */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PLAYING,
                    exoPlayer.getCurrentPosition(), 1f);
        } else if ((playbackState == ExoPlayer.STATE_READY)) {
            mStateBuilder.setState(PlaybackStateCompat.STATE_PAUSED,
                    exoPlayer.getCurrentPosition(), 1f);
        }
        mediaSession.setPlaybackState(mStateBuilder.build());
        showNotification(mStateBuilder.build());
    }


    /**
     * Called when an error occurs. The playback state will transition to {@link #//STATE_IDLE}
     * immediately after this method is called. The player instance can still be used, and
     * {@link #//release()} must still be called on the player should it no longer be required.
     *
     * @param error The error.
     */
    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    /**
     * Called when a position discontinuity occurs without a change to the timeline. A position
     * discontinuity occurs when the current window or period index changes (as a result of playback
     * transitioning from one period in the timeline to the next), or when the playback position
     * jumps within the period currently being played (as a result of a seek being performed, or
     * when the source introduces a discontinuity internally).
     * <p>
     * When a position discontinuity occurs as a result of a change to the timeline this method is
     * <em>not</em> called. {@link #onTimelineChanged(Timeline, Object)} is called in this case.
     */
    @Override
    public void onPositionDiscontinuity() {

    }


    /**
     * Shows Media Style notification, with actions that depend on the current MediaSession
     * PlaybackState.
     *
     * @param state The PlaybackState of the MediaSession.
     */
    private void showNotification(PlaybackStateCompat state) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(), Config.NOTIFICATION_COMPAT_CHANNEL_ID);
        //Notification.Builder builder = new Notification.Builder(getContext(), Config.NOTIFICATION_COMPAT_CHANNEL_ID);
        int icon;
        String play_pause;
        if (state.getState() == PlaybackStateCompat.STATE_PLAYING) {
            icon = R.drawable.exo_controls_pause;
            play_pause = getString(R.string.pause);
        } else {
            icon = R.drawable.exo_controls_play;
            play_pause = getString(R.string.play);
        }


        NotificationCompat.Action playPauseAction = new NotificationCompat.Action(
                icon, play_pause,
                MediaButtonReceiver.buildMediaButtonPendingIntent(getContext(),
                        PlaybackStateCompat.ACTION_PLAY_PAUSE));

        NotificationCompat.Action restartAction = new android.support.v4.app.NotificationCompat
                .Action(R.drawable.exo_controls_previous, getString(R.string.restart),
                MediaButtonReceiver.buildMediaButtonPendingIntent
                        (getContext(), PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS));

        PendingIntent contentPendingIntent = PendingIntent.getActivity
                (getContext(), 0, new Intent(getContext(), StepDetailFragment.class), 0);

        builder.setContentTitle(step.getShortDescription())
                .setContentText(getString(R.string.notification_text))
                .setContentIntent(contentPendingIntent)
                //android.app.RemoteServiceException: Bad notification posted from package
                // android.example.com.foodoo: Couldn't create icon:
                // StatusBarIcon(pkg=android.example.com.foodoouser=0 id=0x7f07006f
                // level=0 visible=true num=0 )


                .addAction(restartAction)
                .addAction(playPauseAction)
                .setStyle(new MediaStyle()
                        .setMediaSession(mediaSession.getSessionToken())
                        .setShowActionsInCompactView(0, 1));

        if (Build.VERSION.SDK_INT >= 21) {
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        if (Build.VERSION.SDK_INT >= 23) {
            builder.setSmallIcon(R.drawable.ic_music_note); // Error is caused by this call on API 19
        }

        mNotificationManager = (NotificationManager) getContext().getSystemService(NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());
    }


    /**
     * Initialize ExoPlayer.
     *
     * @param mediaUri The URI of the sample to play.
     */
    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            binding.playerView.setPlayer(exoPlayer);

            // Set the ExoPlayer.EventListener to this activity.
            exoPlayer.addListener(this);

            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);

            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
            if (currentPosition != -1) {
                exoPlayer.seekTo(currentPosition);
                currentPosition = -1;
            }
        }
    }


    /**
     * Release ExoPlayer.
     * Store current Player position
     */
    private void releasePlayer() {
        if (exoPlayer != null) {
            currentPosition = exoPlayer.getCurrentPosition();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
        if (mNotificationManager != null) {
            mNotificationManager.cancelAll();
        }
    }

    /**
     * Media Session Callbacks, where all external clients control the player.
     */
    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            super.onPlay();
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            exoPlayer.seekTo(0);
        }
    }

    /**
     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients.
     */
    public static class MediaReceiver extends BroadcastReceiver {

        public MediaReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            MediaButtonReceiver.handleIntent(mediaSession, intent);
        }
    }

    public void setCurrentStepIndex(int index) {
        currentStepIndex = index;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            stepDetailNavigationChangeObserver = (OnStepDetailNavigationClickObserver) context;
        } catch (ClassCastException ex) {
            throw new ClassCastException(context.toString() + " must implement stepDetailNavigationChangeObserver");
        }
    }
}
