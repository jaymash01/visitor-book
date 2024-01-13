package com.jaymash.visitorbook.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.jaymash.visitorbook.R;
import com.jaymash.visitorbook.activities.MainActivity;
import com.jaymash.visitorbook.data.Visitor;
import com.jaymash.visitorbook.fragments.VisitorsFragment;
import com.jaymash.visitorbook.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class VisitorsAdapter extends PaginationAdapter<Visitor> {

    private final Animation animSlideDown;
    private final Animation animRotate180;
    private final List<Integer> expanded;

    public VisitorsAdapter(Activity activity, Context context, List<Visitor> dataset) {
        super(activity, context, dataset);
        this.activity = activity;
        this.context = context;

        animSlideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        animRotate180 = AnimationUtils.loadAnimation(context, R.anim.rotate_180);
        expanded = new ArrayList<>();
    }

    @Override
    protected RecyclerView.ViewHolder createItemViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.visitors_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MainActivity activity1 = (MainActivity) activity;
        Fragment fragment = activity1.getCurrentFragment();
        int viewType = holder.getItemViewType();

        if (viewType == TYPE_ITEM) {
            ItemViewHolder holder1 = (ItemViewHolder) holder;
            Visitor visitor = dataset.get(holder.getLayoutPosition());
            String visitDate = DateUtils.formatDate(DateUtils.parseDate(visitor.getVisitDate(),
                    "yyyy-MM-dd"), "MMM dd, yyyy") + " \u00B7 " + visitor.getTimeIn().substring(0, 5);
            String timeOut = visitor.getTimeOut();

            if (timeOut != null) {
                visitDate += " - " + timeOut.substring(0, 5);
            }

            holder1.txtName.setText(visitor.getName());
            holder1.txtVisitDate.setText(visitDate);
            holder1.txtWhereFrom.setText(visitor.getWhereFrom());
            holder1.txtWhereTo.setText(visitor.getWhereTo());
            holder1.txtHost.setText(visitor.getHost());
            holder1.txtVisitReason.setText(visitor.getVisitReason());
            holder1.txtPhone.setText(visitor.getPhone());

            holder1.btnToggleDetails.setRotation(0);
            holder1.detailsContainer.setVisibility(View.GONE);
            expanded.remove((Integer) visitor.getId());

            holder1.btnToggleDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    animRotate180.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            if (!expanded.contains(visitor.getId())) {
                                view.setRotation(180);
                                holder1.detailsContainer.setVisibility(View.VISIBLE);
                                holder1.detailsContainer.startAnimation(animSlideDown);
                                expanded.add(visitor.getId());
                            } else {
                                view.setRotation(0);
                                holder1.detailsContainer.setVisibility(View.GONE);
                                expanded.remove((Integer) visitor.getId());
                            }
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                    view.startAnimation(animRotate180);
                }
            });

            if (fragment instanceof VisitorsFragment) {
                holder1.btnSignOut.setVisibility(View.VISIBLE);
                holder1.btnSignOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (fragment instanceof VisitorsFragment) {
                            activity1.confirm(R.string.confirm_sign_out_title, R.string.confirm_sign_out_message, new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    activity1.closeConfirmation();
                                    ((VisitorsFragment) fragment).signOutVisitor(visitor);
                                }
                            });
                        }
                    }
                });
            } else {
                holder1.btnSignOut.setVisibility(View.GONE);
            }
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public View detailsContainer;
        public TextView txtName, txtVisitDate, txtWhereFrom, txtWhereTo, txtHost, txtVisitReason, txtPhone;
        public MaterialButton btnToggleDetails, btnSignOut;

        public ItemViewHolder(View view) {
            super(view);

            detailsContainer = view.findViewById(R.id.ll_visitor_details);
            txtName = (TextView) view.findViewById(R.id.txt_name);
            txtVisitDate = (TextView) view.findViewById(R.id.txt_visit_date);
            txtWhereFrom = (TextView) view.findViewById(R.id.txt_where_from);
            txtWhereTo = (TextView) view.findViewById(R.id.txt_where_to);
            txtHost = (TextView) view.findViewById(R.id.txt_host);
            txtVisitReason = (TextView) view.findViewById(R.id.txt_visit_reason);
            txtPhone = (TextView) view.findViewById(R.id.txt_phone);
            btnToggleDetails = (MaterialButton) view.findViewById(R.id.btn_toggle_details);
            btnSignOut = (MaterialButton) view.findViewById(R.id.btn_sign_out);
        }
    }
}
