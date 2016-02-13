package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.models.IThreadBase;
import com.android.nitecafe.whirlpoolnews.utilities.IWatchedThreadIdentifier;
import com.jakewharton.rxbinding.view.RxMenuItem;
import com.jakewharton.rxbinding.view.RxView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.PublishSubject;

public abstract class ThreadBaseAdapter<T extends IThreadBase> extends UltimateViewAdapter<ThreadBaseAdapter.ThreadViewHolder> {

    protected List<T> threadsList = new ArrayList<>();
    private IWatchedThreadIdentifier mWatchedThreadIdentifier;

    private PublishSubject<Integer> OnThreadClickedObservable = PublishSubject.create();
    private PublishSubject<Integer> OnWatchClickedObservable = PublishSubject.create();
    private PublishSubject<Integer> OnMarkAsReadClickedObservable = PublishSubject.create();
    private PublishSubject<Integer> OnUnwatchClickedObservable = PublishSubject.create();

    public Observable<T> getOnWatchClickedObservable() {
        return OnWatchClickedObservable.map(integer -> threadsList.get(integer)).asObservable();
    }

    public Observable<T> getOnMarkAsClickedObservable() {
        return OnMarkAsReadClickedObservable.map(integer -> threadsList.get(integer)).asObservable();
    }

    public Observable<T> getOnUnwatchedObservable() {
        return OnUnwatchClickedObservable.map(integer -> threadsList.get(integer)).asObservable();
    }

    public Observable<T> getOnThreadClickedObservable() {
        return OnThreadClickedObservable.map(integer -> threadsList.get(integer)).asObservable();
    }

    public ThreadBaseAdapter(IWatchedThreadIdentifier watchedThreadIdentifier) {
        this.mWatchedThreadIdentifier = watchedThreadIdentifier;
    }

    public void SetThreads(List<T> threads) {
        threadsList = threads;
        notifyDataSetChanged();
        resetHeader();
    }

    protected void resetHeader() {
    }

    @Override
    public ThreadViewHolder onCreateViewHolder(ViewGroup parent) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thread, parent, false);
        return new ThreadViewHolder(inflate, OnThreadClickedObservable, OnWatchClickedObservable, OnMarkAsReadClickedObservable, OnUnwatchClickedObservable);
    }

    @Override
    public int getAdapterItemCount() {
        return threadsList.size();
    }

    public class ThreadViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public View itemView;
        private PublishSubject<Integer> mWatchedClickedSubject;
        private PublishSubject<Integer> mOnMarkAsReadClickedObservable;
        private PublishSubject<Integer> mOnUnwatchClickedObservable;
        @Bind(R.id.thread_title) TextView threadTitle;
        @Bind(R.id.thread_total_page) TextView threadTotalPage;
        @Bind(R.id.thread_last_post_info) TextView threadLastPostInfo;

        ThreadViewHolder(View itemView, PublishSubject<Integer> onThreadClickedObservable,
                         PublishSubject<Integer> watchedClickedSubject,
                         PublishSubject<Integer> onMarkAsReadClickedObservable,
                         PublishSubject<Integer> onUnwatchClickedObservable) {
            super(itemView);
            this.itemView = itemView;
            mWatchedClickedSubject = watchedClickedSubject;
            mOnMarkAsReadClickedObservable = onMarkAsReadClickedObservable;
            mOnUnwatchClickedObservable = onUnwatchClickedObservable;
            itemView.setOnCreateContextMenuListener(this);
            RxView.clicks(itemView).map(aVoid -> getAdapterPosition()).subscribe(onThreadClickedObservable);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Select an Action");
            final T t = threadsList.get(getAdapterPosition());
            if (mWatchedThreadIdentifier.isThreadWatched(t.getID())) {
                MenuItem add = menu.add("Unwatch Thread");
                MenuItem markAsRead = menu.add("Mark as Read");
                RxMenuItem.clicks(add).map(aVoid -> getAdapterPosition()).subscribe(mWatchedClickedSubject);
                RxMenuItem.clicks(markAsRead).map(aVoid -> getAdapterPosition()).subscribe(mOnMarkAsReadClickedObservable);
            } else {
                MenuItem add = menu.add("Watch Thread");
                RxMenuItem.clicks(add).map(aVoid -> getAdapterPosition()).subscribe(mOnUnwatchClickedObservable);
            }
        }
    }
}