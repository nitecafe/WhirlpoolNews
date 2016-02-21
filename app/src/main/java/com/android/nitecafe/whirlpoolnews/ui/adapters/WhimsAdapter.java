package com.android.nitecafe.whirlpoolnews.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.nitecafe.whirlpoolnews.R;
import com.android.nitecafe.whirlpoolnews.models.Whim;
import com.android.nitecafe.whirlpoolnews.utilities.WhirlpoolDateUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.subjects.PublishSubject;

public class WhimsAdapter extends UltimateViewAdapter<WhimsAdapter.WhimViewHolder> {

    private List<Whim> whims = new ArrayList<>();
    private PublishSubject<Integer> OnWhimClickedSubject = PublishSubject.create();

    public void SetWhims(List<Whim> whims) {
        this.whims = whims;
        notifyDataSetChanged();
    }

    @Override
    public WhimViewHolder getViewHolder(View view) {
        return null;
    }

    @Override
    public WhimViewHolder onCreateViewHolder(ViewGroup parent) {
        final View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_whims, parent, false);
        return new WhimViewHolder(inflate);
    }

    @Override public void onBindViewHolder(WhimViewHolder holder, int position) {
        Whim whim = whims.get(position);
        holder.whimFrom.setText(whim.getFROM().getNAME());
        final Date localDateFromString = WhirlpoolDateUtils.getLocalDateFromString(whim.getDATE());
        String sentDate = new SimpleDateFormat("yyyy-MM-dd hh:mm aa", Locale.getDefault()).format(localDateFromString);
        holder.whimSentTime.setText(sentDate);
        String message = whim.getMESSAGE();
        String blurb = message.substring(0, Math.min(message.length(), 50));
        holder.whimContent.setText(blurb + "...");

        if (whim.getVIEWED() == 0)
            holder.itemView.setBackgroundResource(R.color.primary_light);
        else
            holder.itemView.setBackgroundResource(R.color.white);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getAdapterItemCount() {
        return whims.size();
    }

    @Override
    public long generateHeaderId(int position) {
        return 0;
    }

    public Observable<Whim> getOnWhimClickedSubject() {
        return OnWhimClickedSubject.map(integer -> whims.get(integer)).asObservable();
    }

    public class WhimViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.whim_from) TextView whimFrom;
        @Bind(R.id.whim_sent_time) TextView whimSentTime;
        @Bind(R.id.whim_content) TextView whimContent;

        public WhimViewHolder(View itemView) {
            super(itemView);
            RxView.clicks(itemView).map(aVoid -> getAdapterPosition()).subscribe(OnWhimClickedSubject);
            ButterKnife.bind(this, itemView);
        }
    }
}
