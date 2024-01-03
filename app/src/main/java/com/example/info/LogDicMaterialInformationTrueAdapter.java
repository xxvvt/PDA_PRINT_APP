package com.example.info;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.model.LogDicMaterialInformationTrue;
import com.example.qrcode.R;
import com.example.utils.DialogUtils;

import lombok.NonNull;

import java.util.List;

public class LogDicMaterialInformationTrueAdapter extends RecyclerView.Adapter<LogDicMaterialInformationTrueAdapter.ViewHolder> {

    private List<LogDicMaterialInformationTrue> infoBlocks;

    public LogDicMaterialInformationTrueAdapter(List<LogDicMaterialInformationTrue> infoBlocks) {
        this.infoBlocks = infoBlocks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_info_block, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogDicMaterialInformationTrue infoBlock = infoBlocks.get(position);
        //给每个控件赋值
        holder.tv_charg_v.setText(infoBlock.getCharg());
        holder.tv_material_code_v.setText(infoBlock.getMatnr());
        holder.tv_material_description_v.setText(infoBlock.getMaktx());

        holder.btn_print.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogUtils.showCustomDialog(v.getContext(), "确认打印","物料："+infoBlock.getMaktx()+"？",infoBlock.getCanback());
            }
        });

    }

    @Override
    public int getItemCount() {
        return infoBlocks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_charg_v;
        public TextView tv_material_code_v;
        public TextView tv_material_description_v;
        public Button btn_print;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_charg_v = itemView.findViewById(R.id.tv_charg_v);
            tv_material_code_v = itemView.findViewById(R.id.tv_material_code_v);
            tv_material_description_v = itemView.findViewById(R.id.tv_material_description_v);
            btn_print = itemView.findViewById(R.id.btn_print);
//            textValue = itemView.findViewById(R.id.textValue);
        }
    }
}
