package org.emby.androidtv.ui.playback.overlay;

import static java.lang.Math.round;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.leanback.media.PlaybackTransportControlGlue;
import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;
import androidx.leanback.widget.Action;
import androidx.leanback.widget.ArrayObjectAdapter;
import androidx.leanback.widget.PlaybackControlsRow;
import androidx.leanback.widget.PlaybackRowPresenter;
import androidx.leanback.widget.PlaybackTransportRowPresenter;
import androidx.leanback.widget.PlaybackTransportRowView;
import androidx.leanback.widget.RowPresenter;

import org.emby.androidtv.R;
import org.emby.androidtv.preference.UserPreferences;
import org.emby.androidtv.preference.constant.ClockBehavior;
import org.emby.androidtv.ui.playback.PlaybackController;
import org.emby.androidtv.ui.playback.overlay.action.AndroidAction;
import org.emby.androidtv.ui.playback.overlay.action.ChannelBarChannelAction;
import org.emby.androidtv.ui.playback.overlay.action.ChapterAction;
import org.emby.androidtv.ui.playback.overlay.action.ClosedCaptionsAction;
import org.emby.androidtv.ui.playback.overlay.action.CustomAction;
import org.emby.androidtv.ui.playback.overlay.action.FastForwardAction;
import org.emby.androidtv.ui.playback.overlay.action.GuideAction;
import org.emby.androidtv.ui.playback.overlay.action.PlaybackSpeedAction;
import org.emby.androidtv.ui.playback.overlay.action.PreviousLiveTvChannelAction;
import org.emby.androidtv.ui.playback.overlay.action.RecordAction;
import org.emby.androidtv.ui.playback.overlay.action.RewindAction;
import org.emby.androidtv.ui.playback.overlay.action.SelectAudioAction;
import org.emby.androidtv.ui.playback.overlay.action.SelectQualityAction;
import org.emby.androidtv.ui.playback.overlay.action.SkipNextAction;
import org.emby.androidtv.ui.playback.overlay.action.SkipPreviousAction;
import org.emby.androidtv.ui.playback.overlay.action.ZoomAction;
import org.emby.androidtv.ui.playback.overlay.action.IntroAction;
import org.emby.androidtv.ui.playback.overlay.action.OutroAction;
import org.emby.androidtv.ui.playback.overlay.action.EpisodeAction;
import org.emby.androidtv.ui.playback.IntroOutroStore;
import org.jellyfin.sdk.model.api.BaseItemDto;
import org.emby.androidtv.util.DateTimeExtensionsKt;
import org.koin.java.KoinJavaComponent;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CustomPlaybackTransportControlGlue extends PlaybackTransportControlGlue<VideoPlayerAdapter> {

    // Primary actions
    private EpisodeAction episodeAction;
    private PlaybackSpeedAction playbackSpeedAction;
    private ClosedCaptionsAction closedCaptionsAction;
    private SelectAudioAction selectAudioAction;
    private IntroAction introAction;
    private OutroAction outroAction;

    // Secondary actions
    private ChapterAction chapterAction;
    private ZoomAction zoomAction;
    private SelectQualityAction selectQualityAction;
    private RewindAction rewindAction;
    private FastForwardAction fastForwardAction;
    private SkipPreviousAction skipPreviousAction;
    private SkipNextAction skipNextAction;

    // TV actions
    private PreviousLiveTvChannelAction previousLiveTvChannelAction;
    private ChannelBarChannelAction channelBarChannelAction;
    private GuideAction guideAction;
    private RecordAction recordAction;

    private final PlaybackController playbackController;
    private ArrayObjectAdapter primaryActionsAdapter;
    private ArrayObjectAdapter secondaryActionsAdapter;
    private IntroOutroStore introOutroStore = KoinJavaComponent.get(IntroOutroStore.class);

    // Injected views
    private TextView mEndsText = null;

    private final Handler mHandler = new Handler();
    private Runnable mRefreshEndTime;
    private Runnable mRefreshViewVisibility;

    private LinearLayout mButtonRef;

    CustomPlaybackTransportControlGlue(Context context, VideoPlayerAdapter playerAdapter, PlaybackController playbackController) {
        super(context, playerAdapter);
        this.playbackController = playbackController;

        mRefreshEndTime = () -> {
            setEndTime();
            if (!isPlaying()) {
                mHandler.postDelayed(mRefreshEndTime, 30000);
            }
        };

        mRefreshViewVisibility = () -> {
            if (mButtonRef != null && mButtonRef.getVisibility() != mEndsText.getVisibility())
                mEndsText.setVisibility(mButtonRef.getVisibility());
            else
                mHandler.postDelayed(mRefreshViewVisibility, 100);
        };

        initActions(context);
    }

    @Override
    protected void onDetachedFromHost() {
        mHandler.removeCallbacks(mRefreshEndTime);
        mHandler.removeCallbacks(mRefreshViewVisibility);

        closedCaptionsAction.removePopup();
        playbackSpeedAction.dismissPopup();
        selectAudioAction.dismissPopup();
        selectQualityAction.dismissPopup();
        zoomAction.dismissPopup();

        super.onDetachedFromHost();
    }

    @Override
    protected PlaybackRowPresenter onCreateRowPresenter() {
        final AbstractDetailsDescriptionPresenter detailsPresenter = new AbstractDetailsDescriptionPresenter() {
            @Override
            protected void onBindDescription(ViewHolder vh, Object item) {
            }
        };
        PlaybackTransportRowPresenter rowPresenter = new PlaybackTransportRowPresenter() {
            @Override
            protected RowPresenter.ViewHolder createRowViewHolder(ViewGroup parent) {
                RowPresenter.ViewHolder vh = super.createRowViewHolder(parent);

                ClockBehavior showClock = KoinJavaComponent.<UserPreferences>get(UserPreferences.class).get(UserPreferences.Companion.getClockBehavior());

                if (showClock == ClockBehavior.ALWAYS || showClock == ClockBehavior.IN_VIDEO) {
                    Context context = parent.getContext();
                    mEndsText = new TextView(context);
                    mEndsText.setTextAppearance(context, androidx.leanback.R.style.Widget_Leanback_PlaybackControlsTimeStyle);
                    setEndTime();

                    LinearLayout view = (LinearLayout) vh.view;

                    PlaybackTransportRowView bar = (PlaybackTransportRowView) view.getChildAt(1);
                    FrameLayout v = (FrameLayout) bar.getChildAt(0);
                    mButtonRef = (LinearLayout) v.getChildAt(0);

                    bar.removeViewAt(0);
                    RelativeLayout rl = new RelativeLayout(context);
                    RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    rl.addView(v);

                    RelativeLayout.LayoutParams rlp2 = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT);
                    rlp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    rlp2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    rl.addView(mEndsText, rlp2);
                    bar.addView(rl, 0, rlp);
                }

                return vh;
            }

            protected View onCreateActionButton(ViewGroup parent) {
                Context ctx = parent.getContext();
                TextView button = new TextView(ctx);
                button.setBackgroundResource(R.drawable.button_text_bg);
                button.setTextColor(ctx.getColorStateList(R.drawable.button_default_text));
                button.setTextSize(14);
                button.setGravity(Gravity.CENTER);
                button.setPadding(
                    ctx.getResources().getDimensionPixelSize(R.dimen.action_button_padding_horizontal),
                    ctx.getResources().getDimensionPixelSize(R.dimen.action_button_padding_vertical),
                    ctx.getResources().getDimensionPixelSize(R.dimen.action_button_padding_horizontal),
                    ctx.getResources().getDimensionPixelSize(R.dimen.action_button_padding_vertical)
                );
                button.setMinWidth(ctx.getResources().getDimensionPixelSize(R.dimen.action_button_min_width));
                button.setClickable(true);
                button.setFocusable(true);
                return button;
            }

            protected void onBindActionButton(View view, Action action, int actionIndex) {
                CharSequence label = action.getLabel1();
                if (view instanceof TextView && label != null) {
                    ((TextView) view).setText(label);
                }
            }

            @Override
            protected void onProgressBarClicked(PlaybackTransportRowPresenter.ViewHolder vh) {
                // No play/pause action, so do nothing on progress bar click
            }

            @Override
            protected void onBindRowViewHolder(RowPresenter.ViewHolder vh, Object item) {
                super.onBindRowViewHolder(vh, item);
                vh.setOnKeyListener(CustomPlaybackTransportControlGlue.this);
            }

            @Override
            protected void onUnbindRowViewHolder(RowPresenter.ViewHolder vh) {
                super.onUnbindRowViewHolder(vh);
                vh.setOnKeyListener(null);
            }
        };
        rowPresenter.setDescriptionPresenter(detailsPresenter);
        return rowPresenter;
    }

    private void initActions(Context context) {
        // Primary actions
        playbackSpeedAction = new PlaybackSpeedAction(context, this, playbackController, KoinJavaComponent.get(UserPreferences.class));
        playbackSpeedAction.setLabels(new String[]{context.getString(R.string.lbl_playback_speed)});
        closedCaptionsAction = new ClosedCaptionsAction(context, this);
        closedCaptionsAction.setLabels(new String[]{context.getString(R.string.lbl_subtitle_track)});
        selectAudioAction = new SelectAudioAction(context, this);
        selectAudioAction.setLabels(new String[]{context.getString(R.string.lbl_audio_track)});
        introOutroStore = KoinJavaComponent.get(IntroOutroStore.class);
        introAction = new IntroAction(context, this, playbackController, introOutroStore);
        introAction.setLabels(new String[]{context.getString(R.string.lbl_intro)});
        outroAction = new OutroAction(context, this, playbackController, introOutroStore);
        outroAction.setLabels(new String[]{context.getString(R.string.lbl_outro)});
        episodeAction = new EpisodeAction(context, this);
        episodeAction.setLabels(new String[]{context.getString(R.string.lbl_episodes)});

        // Secondary actions
        chapterAction = new ChapterAction(context, this);
        chapterAction.setLabels(new String[]{context.getString(R.string.lbl_chapters)});
        zoomAction = new ZoomAction(context, this);
        zoomAction.setLabels(new String[]{context.getString(R.string.lbl_zoom)});
        selectQualityAction = new SelectQualityAction(context, this, KoinJavaComponent.get(UserPreferences.class));
        selectQualityAction.setLabels(new String[]{context.getString(R.string.lbl_quality_profile)});
        rewindAction = new RewindAction(context);
        rewindAction.setLabels(new String[]{context.getString(R.string.lbl_rewind)});
        fastForwardAction = new FastForwardAction(context);
        fastForwardAction.setLabels(new String[]{context.getString(R.string.lbl_fast_forward)});
        skipPreviousAction = new SkipPreviousAction(context);
        skipNextAction = new SkipNextAction(context);

        // TV actions
        previousLiveTvChannelAction = new PreviousLiveTvChannelAction(context, this);
        previousLiveTvChannelAction.setLabels(new String[]{context.getString(R.string.lbl_prev_item)});
        channelBarChannelAction = new ChannelBarChannelAction(context, this);
        channelBarChannelAction.setLabels(new String[]{context.getString(R.string.lbl_other_channels)});
        guideAction = new GuideAction(context, this);
        guideAction.setLabels(new String[]{context.getString(R.string.lbl_live_tv_guide)});
        recordAction = new RecordAction(context, this);
        recordAction.setLabels(new String[]{
                context.getString(R.string.lbl_record),
                context.getString(R.string.lbl_cancel_recording)
        });
    }

    @Override
    protected void onCreatePrimaryActions(ArrayObjectAdapter primaryActionsAdapter) {
        this.primaryActionsAdapter = primaryActionsAdapter;
    }

    @Override
    protected void onCreateSecondaryActions(ArrayObjectAdapter secondaryActionsAdapter) {
        this.secondaryActionsAdapter = secondaryActionsAdapter;
    }

    void addMediaActions() {
        if (primaryActionsAdapter.size() > 0)
            primaryActionsAdapter.clear();
        if (secondaryActionsAdapter.size() > 0)
            secondaryActionsAdapter.clear();

        VideoPlayerAdapter playerAdapter = getPlayerAdapter();
        BaseItemDto item = playbackController.getCurrentlyPlayingItem();

        // Update intro/outro with current item info
        if (item != null) {
            java.util.UUID seriesId = item.getSeriesId();
            introAction.setSeriesId(seriesId);
            outroAction.setSeriesId(seriesId);
            long duration = playbackController.getDuration();
            outroAction.setDuration(duration);
        }

        // ---- Primary Actions ----
        // 选集 (only if multi-episode)
        boolean hasMultiEpisodes = playerAdapter.hasPreviousItem() || playerAdapter.hasNextItem();
        if (hasMultiEpisodes) {
            primaryActionsAdapter.add(episodeAction);
        }

        // 速度
        if (!playerAdapter.isLiveTv()) {
            primaryActionsAdapter.add(playbackSpeedAction);
        }

        // 字幕
        if (playerAdapter.hasSubs()) {
            primaryActionsAdapter.add(closedCaptionsAction);
        }

        // 音轨
        if (playerAdapter.hasMultiAudio()) {
            primaryActionsAdapter.add(selectAudioAction);
        }

        // 片头、片尾
        if (!playerAdapter.isLiveTv()) {
            primaryActionsAdapter.add(introAction);
            primaryActionsAdapter.add(outroAction);
        }

        // ---- Secondary Actions ----
        // 章节
        if (playerAdapter.hasChapters()) {
            secondaryActionsAdapter.add(chapterAction);
        }

        // 缩放
        secondaryActionsAdapter.add(zoomAction);

        // 画质
        if (!playerAdapter.isLiveTv()) {
            secondaryActionsAdapter.add(selectQualityAction);
        }

        // 快退、快进
        if (playerAdapter.canSeek()) {
            secondaryActionsAdapter.add(rewindAction);
            secondaryActionsAdapter.add(fastForwardAction);
        }

        // 上一集、下一集
        if (playerAdapter.hasPreviousItem()) {
            secondaryActionsAdapter.add(skipPreviousAction);
        }
        if (playerAdapter.hasNextItem()) {
            secondaryActionsAdapter.add(skipNextAction);
        }

        // Live TV actions
        if (playerAdapter.isLiveTv()) {
            secondaryActionsAdapter.add(channelBarChannelAction);
            secondaryActionsAdapter.add(guideAction);
            secondaryActionsAdapter.add(previousLiveTvChannelAction);
            if (playerAdapter.canRecordLiveTv()) {
                secondaryActionsAdapter.add(recordAction);
                recordingStateChanged();
            }
        }
    }

    @Override
    public void onActionClicked(Action action) {
        if (action instanceof AndroidAction) {
            ((AndroidAction) action).onActionClicked(getPlayerAdapter());
        }
        notifyActionChanged(action);
    }

    public void onCustomActionClicked(Action action, View view) {
        if (action instanceof CustomAction) {
            ((CustomAction) action).handleClickAction(playbackController, getPlayerAdapter(), getContext(), view);
        }

        if (action == playbackSpeedAction) {
            mHandler.postDelayed(mRefreshEndTime, 5000);
        }
    }

    private void setEndTime() {
        if (mEndsText == null || getPlayerAdapter().getDuration() < 1)
            return;
        long msLeft = getPlayerAdapter().getDuration() - getPlayerAdapter().getCurrentPosition();
        long realTimeLeft = round(msLeft / playbackController.getPlaybackSpeed());

        LocalDateTime endTime = LocalDateTime.now().plus(realTimeLeft, ChronoUnit.MILLIS);
        mEndsText.setText(getContext().getString(R.string.lbl_playback_control_ends, DateTimeExtensionsKt.getTimeFormatter(getContext()).format(endTime)));
    }

    public void notifyActionChanged(Action action) {
        ArrayObjectAdapter adapter = primaryActionsAdapter;
        if (adapter.indexOf(action) >= 0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action), 1);
            return;
        }
        adapter = secondaryActionsAdapter;
        if (adapter.indexOf(action) >= 0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action), 1);
        }
    }

    void setInitialPlaybackDrawable() {
    }

    void invalidatePlaybackControls() {
        if (primaryActionsAdapter.size() > 0)
            primaryActionsAdapter.clear();
        if (secondaryActionsAdapter.size() > 0)
            secondaryActionsAdapter.clear();
        addMediaActions();
    }

    void recordingStateChanged() {
        if (getPlayerAdapter().isRecording()) {
            recordAction.setIndex(RecordAction.INDEX_RECORDING);
        } else {
            recordAction.setIndex(RecordAction.INDEX_INACTIVE);
        }
        notifyActionChanged(recordAction);
    }

    void updatePlayState() {
        setEndTime();
        if (!isPlaying()) {
            mHandler.removeCallbacks(mRefreshEndTime);
            mHandler.postDelayed(mRefreshEndTime, 30000);
        } else {
            mHandler.removeCallbacks(mRefreshEndTime);
        }
    }

    public void setInjectedViewsVisibility() {
        if (mButtonRef != null && mButtonRef.getVisibility() != mEndsText.getVisibility())
            mEndsText.setVisibility(mButtonRef.getVisibility());
        mHandler.removeCallbacks(mRefreshViewVisibility);
        mHandler.postDelayed(mRefreshViewVisibility, 100);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() != KeyEvent.ACTION_UP) {
            return super.onKey(v, keyCode, event);
        }

        // Handle DPAD_UP/DOWN for intro/outro time adjustment
        if ((keyCode == KeyEvent.KEYCODE_DPAD_UP || keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
            && introAction != null && outroAction != null) {
            View focused = v.findFocus();
            if (focused != null) {
                CharSequence desc = focused.getContentDescription();
                if (desc != null) {
                    String label = desc.toString();
                    CharSequence introLabelCs = introAction.getLabel(0);
                    CharSequence outroLabelCs = outroAction.getLabel(0);
                    String introLabel = introLabelCs != null ? introLabelCs.toString() : null;
                    String outroLabel = outroLabelCs != null ? outroLabelCs.toString() : null;
                    boolean introMatch = introLabel != null && label.equals(introLabel) && !introLabel.equals("--:--");
                    boolean outroMatch = outroLabel != null && label.equals(outroLabel) && !outroLabel.equals("--:--");
                    if (introMatch || outroMatch) {
                        int delta = keyCode == KeyEvent.KEYCODE_DPAD_UP ? 1 : -1;
                        if (introMatch) introAction.adjustTime(delta);
                        else outroAction.adjustTime(delta);
                        return true;
                    }
                }
            }
        }

        VideoPlayerAdapter playerAdapter = getPlayerAdapter();

        if (playerAdapter.hasSubs() && keyCode == KeyEvent.KEYCODE_CAPTIONS) {
            closedCaptionsAction.handleClickAction(playbackController, getPlayerAdapter(), getContext(), v);
        }
        if (playerAdapter.hasMultiAudio() && keyCode == KeyEvent.KEYCODE_MEDIA_AUDIO_TRACK) {
            selectAudioAction.handleClickAction(playbackController, getPlayerAdapter(), getContext(), v);
        }
        return super.onKey(v, keyCode, event);
    }
}