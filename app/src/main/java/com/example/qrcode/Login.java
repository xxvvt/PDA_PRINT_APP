package com.example.qrcode;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.info.LogDicMaterialInformationTrueAdapter;
import com.example.model.ApiPostModel;
import com.example.model.LogDicMaterialInformationTrue;
import com.example.model.PdaAppUser;
import com.example.model.ResponseModel;
import com.example.utils.DialogUtils;
import com.example.utils.SharedPreferencesUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

public class Login extends AppCompatActivity {
    private EditText et_account; //账号输入框
    private EditText et_password;//密码输入框
    private Button btn_login;
    private String account;
    private String password;
    public static final String url = "http://bi.guilinpharma.com:7080/api/appLogin";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将状态栏透明
        StatusBar statusBar = new StatusBar(Login.this);
        statusBar.setStatusBarColor(R.color.transparent);
        //去除默认标题栏
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        setContentView(R.layout.login);

        btn_login = findViewById(R.id.login);
        btn_login.setEnabled(false);
        btn_login.setOnClickListener(v -> {
            login_fr();
//            Intent intent = new Intent(Login.this, MainActivity.class);
//            startActivity(intent);
        });
        et_account = findViewById(R.id.account);
        et_password = findViewById(R.id.password);

        //监听用户名输入框的变化
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 这个方法在文本被改变之前调用，你可以在这里的实现预处理操作
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 这个方法在文本改变时调用，你可以在这里处理改变后的文本
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 这个方法在文本被改变且改变后的文本显示在界面上之后调用
                controlLoginButton();
            }
        });
        //监听密码输入框的变化
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 这个方法在文本被改变之前调用，你可以在这里的实现预处理操作
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 这个方法在文本改变时调用，你可以在这里处理改变后的文本
            }
            @Override
            public void afterTextChanged(Editable s) {
                // 这个方法在文本被改变且改变后的文本显示在界面上之后调用
                controlLoginButton();
            }
        });
        String s_account = new SharedPreferencesUtils(Login.this).getString("username","");
        String s_password = new SharedPreferencesUtils(Login.this).getString("passwords","");
        Log.d("s_account",s_password);
        if(s_account!=null && s_password!=null &&  s_account.length()>0 && s_password.length()>0){
            et_account.setText(s_account);
//            et_password.setText(s_password);
//            login();
        }
    }

    //控制登录按钮是否可用
    private void controlLoginButton() {
        account = et_account.getText().toString();
        password = et_password.getText().toString();
        if (account!=null && password!=null && account.length() > 0 && password.length() > 0) {
            btn_login.setEnabled(true);
        } else {
            btn_login.setEnabled(false);
        }
    }
    /**
     * 登录操作
     * 请求到后端，验证账号密码是否正确
     */
    private void login() {
        AlertDialog dialog = DialogUtils.showLoding(Login.this);
        try{
            //登录操作
            ApiPostModel apiPostModel = new ApiPostModel();
            account = et_account.getText().toString();
            password = et_password.getText().toString();
            Log.d("登录",password);

            apiPostModel.setUsername(account);
            apiPostModel.setPassword(password);
            //从string.xml中获取token
            String token = getResources().getString(R.string.token);
            apiPostModel.setToken(token);
            Gson gson = new Gson();
            String json = gson.toJson(apiPostModel);
            //请求后端
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            OkHttpUtils.postString().url(url).addHeader("Content-Type", "application/json").addHeader("Accept", "*/*").content(json).mediaType(mediaType).build().execute(new StringCallback()
            {
                @Override
                public void onError(Call call, Exception e) {
                    Log.d("error","接口请求失败");
                    Log.d("error",e.toString());
                    DialogUtils.showWaringDialog(Login.this,"提示","登录失败，请检查网络",30000);
                    if (dialog.isShowing()) {
                        dialog.dismiss(); // 关闭对话框
                    }
                }
                @Override
                public void onResponse(String response) {
                    try {
                        //请求成功的处理
                        Log.d("success","接口请求成功");
                        Log.d("success",response);
                        //解析json
                        Type type = new TypeToken<ResponseModel<PdaAppUser>>(){}.getType();
                        ResponseModel<PdaAppUser> responseModel = gson.fromJson(response,type);
                        if("0".equals(responseModel.getCode())){
                            //登录成功
                            PdaAppUser pdaAppUser = responseModel.getData();
                            //保存用户信息
                            SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(Login.this);
                            sharedPreferencesUtils.saveString("username",pdaAppUser.getUsername());
                            sharedPreferencesUtils.saveString("passwords",password);
                            sharedPreferencesUtils.saveString("Data",gson.toJson(pdaAppUser));
                            String s_password = new SharedPreferencesUtils(Login.this).getString("passwords","");
                            DialogUtils.showText(Login.this,"登录成功");
                            //跳转到主界面
                            Intent intent = new Intent(Login.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            DialogUtils.showCustomDialog(Login.this,"提示",responseModel.getMessage(),null);
                            //登录失败清除缓存中的账号密码
                            SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(Login.this);
                            sharedPreferencesUtils.clearAll();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        DialogUtils.showCustomDialog(Login.this,"提示","用户名或密码不正确",null);

                        Log.d("error","接口请求解析json失败");
                        Log.d("error",e.toString());
                    }
                    if (dialog.isShowing()) {
                        dialog.dismiss(); // 关闭对话框
                    }
                }
            });

        }catch (Exception e) {
            e.printStackTrace();
            Log.d("error", "接口请求失败");
            Log.d("error", e.toString());
            DialogUtils.showWaringDialog(Login.this, "提示", "登录失败", 30000);
            if (dialog.isShowing()) {
                Log.d("关闭对话框", "关闭对话框1");
                dialog.dismiss(); // 关闭对话框
            }
        }
    }

    //转码传入的参数
    public String encode(String str){
        String encode = "";
        try {
            encode = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encode;
    }

    //使用帆软系统登录
    public void login_fr(){
        account = et_account.getText().toString();
        password = et_password.getText().toString();

        String url = "http://bi.guilinpharma.com:7080/webroot/decision/login/cross/domain?fine_username="+encode(account)+"&fine_password="+encode(password)+"&validity=-1";

        AlertDialog dialog = DialogUtils.showLoding(Login.this);
        OkHttpUtils.get().url(url).addHeader("Content-Type", "application/json").addHeader("Accept", "*/*").build().execute(new StringCallback()
        {
            @Override
            public void onError(Call call, Exception e) {
                Log.d("error","接口请求失败");
                Log.d("error",e.toString());

                if (dialog.isShowing()) {
                    Log.d("关闭对话框","关闭对话框1");
                    dialog.dismiss(); // 关闭对话框
                }
                DialogUtils.showWaringDialog(Login.this,"提示","登录失败，请检查网络",30000);
            }

            @Override
            public void onResponse(String response) {
                try {
                    //请求成功的处理
                    Log.d("success","接口请求成功");
                    Log.d("success",response);

                    String jsonBody = response.substring(response.indexOf("{"), response.lastIndexOf("}") + 1);
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(jsonBody);
                    if (jsonNode.has("status") && jsonNode.get("status").asText().equals("success")) {
                        getUserInfo();

                    } else if (jsonNode.has("status") && jsonNode.get("status").asText().equals("fail")) {
                        String errorCode = jsonNode.get("errorCode").asText();
                        String errorMsg = jsonNode.get("errorMsg").asText();
                        DialogUtils.showWaringDialog(Login.this,"提示","登录失败，用户名不存在或密码错误",30000);
                        System.out.println("errorCode: " + errorCode);
                        System.out.println("errorMsg: " + errorMsg);
                    }
                    if (dialog.isShowing()) {
                        Log.d("关闭对话框","关闭对话框1");
                        dialog.dismiss(); // 关闭对话框
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    DialogUtils.showWaringDialog(Login.this,"提示","系统错误",30000);
                    Log.d("error","接口请求解析json失败");
                    Log.d("error",e.toString());
                    if (dialog.isShowing()) {
                        Log.d("关闭对话框","关闭对话框1");
                        dialog.dismiss(); // 关闭对话框
                    }
                }

            }

        });
    }

    //单纯获取数据
    public void getUserInfo(){
        AlertDialog dialog = DialogUtils.showLoding(Login.this);
        try{
            //登录操作
            ApiPostModel apiPostModel = new ApiPostModel();
            account = et_account.getText().toString();
            password = "It@2534683";
            apiPostModel.setUsername(account);
            apiPostModel.setPassword(password);
            //从string.xml中获取token
            String token = getResources().getString(R.string.token);
            apiPostModel.setToken(token);
            Gson gson = new Gson();
            String json = gson.toJson(apiPostModel);
            //请求后端
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            OkHttpUtils.postString().url(url).addHeader("Content-Type", "application/json").addHeader("Accept", "*/*").content(json).mediaType(mediaType).build().execute(new StringCallback()
            {
                @Override
                public void onError(Call call, Exception e) {
                    Log.d("error","接口请求失败");
                    Log.d("error",e.toString());
                    DialogUtils.showWaringDialog(Login.this,"提示","获取用户信息失败，请检查网络",30000);
                    if (dialog.isShowing()) {
                        dialog.dismiss(); // 关闭对话框
                    }
                }
                @Override
                public void onResponse(String response) {
                    try {
                        //请求成功的处理
                        Log.d("success","接口请求成功");
                        Log.d("success",response);
                        //解析json
                        Type type = new TypeToken<ResponseModel<PdaAppUser>>(){}.getType();
                        ResponseModel<PdaAppUser> responseModel = gson.fromJson(response,type);
                        if("0".equals(responseModel.getCode())){
                            //登录成功
                            PdaAppUser pdaAppUser = responseModel.getData();
                            //保存用户信息
                            SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(Login.this);
                            sharedPreferencesUtils.saveString("username",pdaAppUser.getUsername());
                            sharedPreferencesUtils.saveString("passwords",password);
                            sharedPreferencesUtils.saveString("Data",gson.toJson(pdaAppUser));
                            String s_password = new SharedPreferencesUtils(Login.this).getString("passwords","");
                            DialogUtils.showText(Login.this,"登录成功");
                            //跳转到主界面
                            Intent intent = new Intent(Login.this,MainActivity.class);
                            startActivity(intent);
                        }else{
                            DialogUtils.showCustomDialog(Login.this,"提示",responseModel.getMessage(),null);
                            //登录失败清除缓存中的账号密码
                            SharedPreferencesUtils sharedPreferencesUtils = new SharedPreferencesUtils(Login.this);
                            sharedPreferencesUtils.clearAll();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        DialogUtils.showCustomDialog(Login.this,"提示","用户名或密码不正确",null);

                        Log.d("error","接口请求解析json失败");
                        Log.d("error",e.toString());
                    }
                    if (dialog.isShowing()) {
                        dialog.dismiss(); // 关闭对话框
                    }
                }
            });

        }catch (Exception e) {
            e.printStackTrace();
            Log.d("error", "接口请求失败");
            Log.d("error", e.toString());
            DialogUtils.showWaringDialog(Login.this, "提示", "登录失败", 30000);
            if (dialog.isShowing()) {
                Log.d("关闭对话框", "关闭对话框1");
                dialog.dismiss(); // 关闭对话框
            }
        }
    }

}
