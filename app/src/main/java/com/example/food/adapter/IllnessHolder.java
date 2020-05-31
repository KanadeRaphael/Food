package com.example.food.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.food.R;
import com.example.food.application.Food;
import com.example.food.utils.ConstantUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IllnessHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.text)
    TextView text;

    private IllAdapter adapter;

    public IllnessHolder(View itemView, IllAdapter adapter) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.adapter = adapter;
    }

    public void bindView(final String illness, final int position) {
        text.setText(illness);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext())
                        .setTitle("删除").setMessage("确定删除该项？");
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.deleteItem(position);
                        if (ConstantUtils.getFlavour().contains(illness)) {
                            Food.flavourCount -= (ConstantUtils.getFlavour().indexOf(illness) + 1);
                        }
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        });
    }
}
