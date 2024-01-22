package com.jaymash.visitorbook.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.RecyclerView;

import com.jaymash.visitorbook.R;

import java.util.List;

abstract public class PaginationAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    final protected static int TYPE_ITEM = 1;
    final protected static int TYPE_FOOTER = -1;

    protected Activity activity;
    protected Context context;
    protected List<T> dataset;

    private FooterViewHolder footerViewHolder;

    public PaginationAdapter(Activity activity, Context context, List<T> dataset) {
        this.activity = activity;
        this.context = context;
        this.dataset = dataset;
    }

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            return createItemViewHolder(parent);
        }

        RecyclerView.ViewHolder viewHolder = createFooterViewHolder(parent);
        footerViewHolder = (FooterViewHolder) viewHolder;
        return viewHolder;
    }

    abstract protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent);

    private RecyclerView.ViewHolder createFooterViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.pagination_adapter_footer,
                parent, false);
        return new FooterViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position == dataset.size() - 1 ? TYPE_FOOTER : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public void addAll(List<T> items) {
        int size = dataset.size();
        dataset.addAll(size >= 1 ? size - 1 : 0, items);
    }

    public void add(T item) {
        int size = dataset.size();
        dataset.add(size >= 1 ? size - 1 : 0, item);
    }

    public final void showFooterProgressBar() {
        if (footerViewHolder != null) {
            ((View) footerViewHolder.progressBar.getParent()).setVisibility(View.VISIBLE);
            footerViewHolder.txtMessage.setVisibility(View.GONE);
            footerViewHolder.progressBar.setVisibility(View.VISIBLE);
        }
    }

    public final void showFooterMessage(@StringRes int message) {
        if (footerViewHolder != null) {
            ((View) footerViewHolder.progressBar.getParent()).setVisibility(View.VISIBLE);
            footerViewHolder.progressBar.setVisibility(View.GONE);
            footerViewHolder.txtMessage.setVisibility(View.VISIBLE);
            footerViewHolder.txtMessage.setText(message);
        }
    }

    public final void hideFooter() {
        if (footerViewHolder != null) {
            ((View) footerViewHolder.progressBar.getParent()).setVisibility(View.GONE);
        }
    }

    public static class FooterViewHolder extends RecyclerView.ViewHolder {

        public ProgressBar progressBar;
        public TextView txtMessage;

        public FooterViewHolder(View view) {
            super(view);

            progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
            txtMessage = (TextView) view.findViewById(R.id.txt_message);
        }
    }
}