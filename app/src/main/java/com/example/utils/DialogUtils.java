package com.example.utils;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcode.AboutActivity;
import com.example.qrcode.R;

import java.util.function.Function;

public class DialogUtils {
    public static void showCustomDialog(Context context,String title, String message, DialogInterface.OnClickListener confirmListener) {
        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_layout);

        TextView textView = dialog.findViewById(R.id.textMessage);
        textView.setText(message);

        TextView btnCancel = dialog.findViewById(R.id.btnCancel);
        TextView btnConfirm = dialog.findViewById(R.id.btnConfirm);

        TextView textTitle = dialog.findViewById(R.id.textTitle);
        textTitle.setText(title);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 取消按钮点击事件...
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 确认按钮点击事件...
                if(confirmListener != null) {
                    confirmListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                }
            }
        });

        dialog.show();
    }

    public static void showCustomDialog(Context context, String message, DialogInterface.OnClickListener confirmListener) {
        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_layout);

        TextView textView = dialog.findViewById(R.id.textMessage);
        textView.setText(message);

        TextView btnCancel = dialog.findViewById(R.id.btnCancel);
        TextView btnConfirm = dialog.findViewById(R.id.btnConfirm);

        TextView textTitle = dialog.findViewById(R.id.textTitle);
        textTitle.setText("确认");

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 取消按钮点击事件...
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 确认按钮点击事件...
                if(confirmListener != null) {
                    confirmListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                }
            }
        });

        dialog.show();
    }

    //通知框
    public static void showAlertDialog(Context context, String title,String message, int delayMs) {
        // 创建对话框实例
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        // 使用自定义布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_notification, null);
        dialogBuilder.setView(dialogView);

        // 设置标题和内容
        TextView titleTextView = dialogView.findViewById(R.id.textTitle);
        TextView messageTextView = dialogView.findViewById(R.id.textMessage);
        titleTextView.setText(title);
        messageTextView.setText(message);

        // 创建对话框
        AlertDialog dialog = dialogBuilder.create();

        // 显示对话框
        dialog.show();

        // 设置自动关闭的延时时间（单位：毫秒）
        //        int delayMs = 3000; // 例如，延时 3 秒后自动关闭

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss(); // 关闭对话框
                }
            }
        }, delayMs);
    }
    //警告框
    public static void showWaringDialog(Context context, String title,String message, int delayMs) {
        // 创建对话框实例
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        // 使用自定义布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_notification, null);
        dialogBuilder.setView(dialogView);

        // 设置标题和内容
        TextView titleTextView = dialogView.findViewById(R.id.textTitle);
        TextView messageTextView = dialogView.findViewById(R.id.textMessage);
        titleTextView.setText(title);
        messageTextView.setText(message);
        messageTextView.setTextColor(context.getResources().getColor(R.color.red));

        // 创建对话框
        AlertDialog dialog = dialogBuilder.create();

        // 显示对话框
        dialog.show();

        // 设置自动关闭的延时时间（单位：毫秒）
        //        int delayMs = 3000; // 例如，延时 3 秒后自动关闭

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss(); // 关闭对话框
                }
            }
        }, delayMs);
    }

    //加载框
    public static AlertDialog showLoding(Context context){
        // 创建对话框构建器
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        // 使用自定义布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);
        dialogBuilder.setView(dialogView);

        // 创建对话框
        AlertDialog dialog = dialogBuilder.create();
        // 设置对话框属性
        dialog.setCancelable(false); // 点击对话框外部不可关闭
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 设置对话框背景为透明
        // 显示对话框
        dialog.show();
        return dialog;
    }
    //加载框
    public static void showLoding(Context context,int delayMs){
        // 创建对话框构建器
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);

        // 使用自定义布局
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_loading, null);
        dialogBuilder.setView(dialogView);

        // 创建对话框
        AlertDialog dialog = dialogBuilder.create();
        // 设置对话框属性
        dialog.setCancelable(false); // 点击对话框外部不可关闭
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // 设置对话框背景为透明
        // 显示对话框
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog.isShowing()) {
                    dialog.dismiss(); // 关闭对话框
                }
            }
        }, delayMs);
    }
    public static void showText(Context context,String text ) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);

//        View tv = toast.getView();
//        tv.setBackgroundResource(R.drawable.linear_layout_style_blue);
//        //设置文字颜色
//        TextView tvt = (TextView) toast.getView().findViewById(android.R.id.message);
//        tvt.setTextColor(getResources().getColor(R.color.white));
//
//        toast.setView(tv);
        toast.show();
    }

    public static void showCustomDialogWithInput(Context context, String title,String message, Function confirmListener) {
        Dialog dialog = new Dialog(context, R.style.CustomDialog);
        dialog.setContentView(R.layout.dialog_with_input);

        TextView textTitle = dialog.findViewById(R.id.textTitle);
        textTitle.setText(title);
        TextView textMessage = dialog.findViewById(R.id.textMessage);
        textMessage.setText(message);
        EditText editTextInput = dialog.findViewById(R.id.editTextInput);

        TextView btnCancel = dialog.findViewById(R.id.btnCancel);
        TextView btnConfirm = dialog.findViewById(R.id.btnConfirm);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // 取消按钮点击事件...
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                String inputText = editTextInput.getText().toString();
                int o = Integer.parseInt(inputText);
                if (o < 1 || o > 30) {
                    editTextInput.setError("请输入1-30的整数");
                    return;
                }
                dialog.dismiss();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextInput.getWindowToken(), 0);
                // 确认按钮点击事件...
                if (confirmListener != null) {
                    confirmListener.apply(inputText);
                }
            }
        });

        dialog.show();
    }


}
