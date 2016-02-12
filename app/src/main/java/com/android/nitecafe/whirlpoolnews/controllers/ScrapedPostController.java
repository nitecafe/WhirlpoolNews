package com.android.nitecafe.whirlpoolnews.controllers;

import com.android.nitecafe.whirlpoolnews.interfaces.IWhirlpoolRestClient;
import com.android.nitecafe.whirlpoolnews.scheduler.ISchedulerManager;
import com.android.nitecafe.whirlpoolnews.ui.fragments.ScrapedPostFragment;
import com.android.nitecafe.whirlpoolnews.ui.interfaces.IScrapedPostFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

public class ScrapedPostController {

    private IWhirlpoolRestClient whirlpoolRestClient;
    private ISchedulerManager schedulerManager;
    private IScrapedPostFragment postFragment;
    private int currentPage = 1;
    private int mPageCount;

    @Inject
    @Singleton
    public ScrapedPostController(IWhirlpoolRestClient whirlpoolRestClient, ISchedulerManager schedulerManager) {
        this.whirlpoolRestClient = whirlpoolRestClient;
        this.schedulerManager = schedulerManager;
    }

    public void GetScrapedPosts(int threadId, int page) {

        if (page < 1 || threadId < 1)
            throw new IllegalArgumentException("Need valid thread id or page number");

        currentPage = page;
        loadScrapedPosts(threadId, currentPage);
    }

    public void loadScrapedPosts(int threadId, int page) {
        whirlpoolRestClient.GetScrapedPosts(threadId, page)
                .observeOn(schedulerManager.GetMainScheduler())
                .subscribeOn(schedulerManager.GetIoScheduler())
                .subscribe(posts -> {
                    mPageCount = posts.getPageCount();
                    if (postFragment != null) {
                        postFragment.DisplayPosts(posts.getScrapedPosts());
                        postFragment.SetupPageSpinner(mPageCount, page);
                        HideAllProgressBar();
                    }
                }, throwable -> {
                    if (postFragment != null) {
                        postFragment.DisplayErrorMessage();
                        HideAllProgressBar();
                    }
                });
    }

    public void loadNextPage(int threadId) {
        if (IsAtLastPage())
            throw new IllegalArgumentException("Current page is the last page.");

        postFragment.ShowRefreshLoader();
        loadScrapedPosts(threadId, ++currentPage);
    }

    public boolean IsAtLastPage() {
        return currentPage == mPageCount;
    }

    public void loadPreviousPage(int threadId) {
        if (IsAtFirstPage())
            throw new IllegalArgumentException("Current page is the first page.");

        postFragment.ShowRefreshLoader();
        loadScrapedPosts(threadId, --currentPage);
    }

    public boolean IsAtFirstPage() {
        return currentPage == 1;
    }

    private void HideAllProgressBar() {
        postFragment.HideCenterProgressBar();
        postFragment.HideRefreshLoader();
    }

    public void attach(ScrapedPostFragment postFragment) {
        this.postFragment = postFragment;
    }

}
