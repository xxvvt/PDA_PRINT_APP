package com.example.qrcode;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.info.LogDicMaterialInformationTrueAdapter;
import com.example.info.NetHelper;
import com.example.lc_print_sdk.PrintUtil;
import com.example.model.LogDicMaterialInformationTrue;
import com.example.model.ResponseModel;
import com.example.utils.DialogUtils;
import com.example.utils.SharedPreferencesUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.ZLoadingView;
import com.zyao89.view.zloading.Z_TYPE;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


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


        LinearLayout searchLayout = this.findViewById(R.id.testLayout);    //测试
        //隐藏searchLayout
        searchLayout.setVisibility(View.GONE);

        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(MainActivity.this, Print.class);
//                intent.putExtra(Print.Matnr, "112005353");
//                intent.putExtra(Print.Charg, "P231227002");
//                startActivity(intent);
//                使用okhttp发起get请求

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
        if (!NetHelper.IsHaveInternet(getActivity())) {
            DialogUtils.showWaringDialog(this, "提示", "未连接网络,请检查网络设置", 3000);
            return;
        }
        Intent intent = new Intent(MainActivity.this, Scan.class);
        startActivity(intent);
    }

    public Context getActivity() {
        return this;
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
    //判断网络是否连接
    public boolean isNetworkConnected(){

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Network network = cm.getActiveNetwork();
                if (network == null) {
                    // 未连接网络
                    DialogUtils.showText(this,"未连接网络");
                    return false;
                } else {
                    NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
                    if (capabilities == null) {
                        DialogUtils.showText(this,"无法获取网络能力");
                        return false;
                        // 无法获取网络能力
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        //网络通过WiFi连接
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        //网络通过蜂窝数据连接
                    } else {
                        // 其他网络类型，比如以太网
                    }
                }
            }
        }
        return true;
    }
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity(); // 关闭所有关联的Activity并退出应用
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "再次点击返回键退出应用", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000); // 2秒钟内再次点击返回键才会真正退出应用
    }

    //设置编辑框内容
    public Function getInputFunction(){
        return new Function() {
            @Override
            public Object apply(Object o) {
                DialogUtils.showText(MainActivity.this,"输入了"+o.toString());
                return null;
            }
        };
    }

    //解析请求结果
    public  void parseResponse(String jsonResponse) {
        String jsonBody = jsonResponse.substring(jsonResponse.indexOf("{"), jsonResponse.lastIndexOf("}") + 1);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(jsonBody);

            if (jsonNode.has("status") && jsonNode.get("status").asText().equals("success")) {
                String accessToken = jsonNode.get("accessToken").asText();
                String url = jsonNode.get("url").asText();
                String status = jsonNode.get("status").asText();
                System.out.println("accessToken: " + accessToken);
                System.out.println("url: " + url);
                System.out.println("status: " + status);
            } else if (jsonNode.has("status") && jsonNode.get("status").asText().equals("fail")) {
                String errorCode = jsonNode.get("errorCode").asText();
                String errorMsg = jsonNode.get("errorMsg").asText();

                System.out.println("errorCode: " + errorCode);
                System.out.println("errorMsg: " + errorMsg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}