package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.ScrapedThread;

import java.util.List;

public interface ISearchResultFragment extends IThreadActionMessageFragment {

    void DisplaySearchResults(List<ScrapedThread> scrapedThreads);

    void HideSearchProgressBar();
}
