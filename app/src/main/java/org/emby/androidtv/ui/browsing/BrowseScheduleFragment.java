package org.emby.androidtv.ui.browsing;

import org.emby.androidtv.ui.livetv.TvManager;
import org.emby.androidtv.ui.presentation.CardPresenter;

public class BrowseScheduleFragment extends EnhancedBrowseFragment {

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setupQueries(final RowLoader rowLoader) {
        TvManager.getScheduleRowsAsync(this, null, new CardPresenter(true), mRowsAdapter);
    }
}
