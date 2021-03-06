package com.android.nitecafe.whirlpoolnews.ui.interfaces;

import com.android.nitecafe.whirlpoolnews.models.Forum;

import java.util.List;

public interface IForumFragment {
    void UpdateFavouriteSection();

    void DisplayForum(List<Forum> forums);

    void HideCenterProgressBar();

    void DisplayErrorMessage();

    void DisplayAddToFavouriteForumMessage();

    void DisplayRemoveFromFavouriteForumMessage();

    void HideRefreshLoader();
}
