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
        if(infoBlock.getFydat()==null){
            infoBlock.setFydat("/");
        }
        if(infoBlock.getVfdat()==null){
            infoBlock.setVfdat("/");
        }
        if(infoBlock.getStorage()==null){
            infoBlock.setStorage("/");
        }
        if(infoBlock.getMaker()==null){
            infoBlock.setMaker("/");
        }
        if(infoBlock.getMnum()==null){
            infoBlock.setMnum("/");
        }
        if(infoBlock.getNum()==null){
            infoBlock.setNum("");
        }
        //给每个控件赋值
        holder.tv_charg_v.setText(infoBlock.getCharg());
        holder.tv_material_code_v.setText(infoBlock.getMatnr());
        holder.tv_material_description_v.setText(infoBlock.getMaktx());
        holder.tv_maker_v.setText(infoBlock.getMaker());
        holder.tv_vfdat_v.setText(infoBlock.getVfdat());
        holder.tv_fydat_v.setText(infoBlock.getFydat());
        holder.tv_mnum_v.setText(infoBlock.getMnum());
        holder.tv_num_v.setText(infoBlock.getNum());
        holder.tv_storage_v.setText(infoBlock.getStorage());
        holder.tv_zspecno_v.setText(infoBlock.getZspecno());
        holder.tv_zspeno_v.setText(infoBlock.getZspeno());
        //设置sap状态
        setStatus(holder,infoBlock);
        holder.btn_print.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DialogUtils.showCustomDialog(v.getContext(), "确认打印","物料："+infoBlock.getMaktx()+"？",infoBlock.getCanback());
            }
        });

    }
    public void setStatus(@NonNull ViewHolder holder,LogDicMaterialInformationTrue infoBlocks) {
        if(infoBlocks.getInsme()>0){
            holder.tv_status_v.setText("待验");
            //指定文字颜色为@color/insme
            holder.tv_status_v.setTextColor(holder.itemView.getResources().getColor(R.color.insme));
        }
        else if(infoBlocks.getSpeme()>0){
            holder.tv_status_v.setText("不合格");
            holder.tv_status_v.setTextColor(holder.itemView.getResources().getColor(R.color.speme));
        }
        else if(infoBlocks.getLabst()>0){
            holder.tv_status_v.setText("合格");
            holder.tv_status_v.setTextColor(holder.itemView.getResources().getColor(R.color.labst));
        }
    }

    @Override
    public int getItemCount() {
        return infoBlocks.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_charg_v;
        public TextView tv_material_code_v;
        public TextView tv_material_description_v;
        public TextView tv_maker_v;
        public TextView tv_vfdat_v;
        public TextView tv_fydat_v;
        public TextView tv_mnum_v;
        public TextView tv_num_v;
        public TextView tv_storage_v;
        public TextView tv_status_v;
        public TextView tv_zspecno_v;
        public TextView tv_zspeno_v;

        public Button btn_print;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_charg_v = itemView.findViewById(R.id.tv_charg_v);
            tv_material_code_v = itemView.findViewById(R.id.tv_material_code_v);
            tv_material_description_v = itemView.findViewById(R.id.tv_material_description_v);
            tv_maker_v = itemView.findViewById(R.id.tv_maker_v);
            tv_vfdat_v = itemView.findViewById(R.id.tv_vfdat_v);
            tv_fydat_v = itemView.findViewById(R.id.tv_fydat_v);
            tv_mnum_v = itemView.findViewById(R.id.tv_mnum_v);
            tv_num_v = itemView.findViewById(R.id.tv_num_v);
            tv_storage_v = itemView.findViewById(R.id.tv_storage_v);
            btn_print = itemView.findViewById(R.id.btn_print);
            tv_status_v = itemView.findViewById(R.id.tv_status_v);
            tv_zspecno_v = itemView.findViewById(R.id.tv_zspecno_v);
            tv_zspeno_v = itemView.findViewById(R.id.tv_zspeno_v);

//            textValue = itemView.findViewById(R.id.textValue);
        }
    }
}
