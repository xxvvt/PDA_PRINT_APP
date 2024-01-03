package com.example.qrcode;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import com.example.model.LogDicMaterialInformationTrue;
import com.example.model.ResponseModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

public class Result extends AppCompatActivity {
    public static final String url = "http://bi.guilinpharma.com:7080/api/getMaterialInformation";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将状态栏透明
        StatusBar statusBar = new StatusBar(Result.this);
        statusBar.setStatusBarColor(R.color.transparent);

        setContentView(R.layout.result);

        //去除默认标题栏
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();
        }
        EditText edittext = findViewById(R.id.ResultText);
        edittext.setText(getIntent().getStringExtra(Scan.Text));

        findViewById(R.id.copy).setOnClickListener(new CopyListener());

        ImageView ret = findViewById(R.id.Ret);
        ret.setOnClickListener(new changeXmlListener());
    }

    public class CopyListener implements View.OnClickListener {
        public void onClick(View v) {
            EditText text = findViewById(R.id.ResultText);
            String tex = text.getText().toString();
            //获取系统剪贴板服务
            ClipboardManager clipboardManager = (ClipboardManager) Result.this.getSystemService(Context.CLIPBOARD_SERVICE);
            //获取剪贴板管理器：
            ClipData mClipData = ClipData.newPlainText("Label", tex);
            // 将ClipData内容放到系统剪贴板里。
            clipboardManager.setPrimaryClip(mClipData);
            Toast.makeText(Result.this,"复制成功", Toast.LENGTH_SHORT).show();
        }
    }

    public class changeXmlListener implements View.OnClickListener {
        public void onClick(View v) {
            finish();
        }
    }

}
