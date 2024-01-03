package com.example.qrcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lc_print_sdk.PrintUtil;
import com.example.utils.DialogUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MainActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private Dialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将状态栏透明
        StatusBar statusBar = new StatusBar(MainActivity.this);
        statusBar.setStatusBarColor(R.color.purple_500);
//
//        //设置当前界面UI样式
        setContentView(R.layout.activity_main);
//
//        //去除默认标题栏
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
//            actionBar.hide();
            actionBar.setTitle("首页");
        }


        LinearLayout searchLayout = this.findViewById(R.id.searchLayout);    //搜索
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showText("测试按钮点击搜索");
            }
        });

        LinearLayout sttingLayout = this.findViewById(R.id.settingsLayout);    //设置
//        sttingLayout.setOnClickListener(new changeXmlListener());
        sttingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
//        Button generate = this.findViewById(R.id.Generate);    //生成二维码
//        generate.setOnClickListener(new changeXmlListener());  //切换页面 开启新的activity

        LinearLayout scan = this.findViewById(R.id.scanLayout);    //扫描二维码
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 获得权限
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Scan();
                } else { //未获得权限
                    // 若未获得相机权限
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
                    }
                }
            }
        });


    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Scan();
                } else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                    alertDialog.setTitle("提示");
                    alertDialog.setMessage("无法访问某些权限可能会影响部分功能的使用，是否要跳转到应用设置页面更改授权？");
                    alertDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                            startActivity(intent);
                        }
                    });
                    alertDialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        }
    }

    public void Scan() {
        Intent intent = new Intent(MainActivity.this, Scan.class);
        startActivity(intent);
    }
    //测试跳转
    public class changeXmlListener implements View.OnClickListener {
        //切换界面的类
        public void onClick(View v) {
//            Intent intent = new Intent(MainActivity.this, InputActivity.class);  //intent是Android里用于activity之间信息传递的类
//            startActivity(intent);
//            showDialog();
            DialogUtils.showCustomDialog(MainActivity.this,"确认", "是否要跳转到输入界面？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String text = "112005353,P231227002";
                    try{
                        if (text.length() == 20) {
                            String[] split = text.split(",");
                            if (split.length == 2) {
                                if (split[0].length() == 9 && split[1].length() == 10) {
                                    Log.d("success","二维码内容正确");
                                    Intent intent = new Intent(MainActivity.this, Print.class);
                                    intent.putExtra(Print.Matnr, split[0]);
                                    intent.putExtra(Print.Charg, split[1]);
                                    startActivity(intent);
                                }else{
                                    throw new Exception("二维码内容长度不正确");
                                }
                            }else {
                                throw new Exception("二维码内容无法分割");
                            }
                        }else {
                            Log.d("二维码长度不正确",text.length()+"");
                            throw new Exception("二维码长度不正确");
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                        Log.d("error",e.toString());

                    }
                }
            });
//            DialogUtils.showLoding(MainActivity.this);

        }
    }

    public void showText(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }


}