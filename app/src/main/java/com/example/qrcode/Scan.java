package com.example.qrcode;

import android.bld.print.configuration.PrintConfig;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utils.DialogUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.lc_print_sdk.PrintUtil;

public class Scan extends AppCompatActivity {
    IntentIntegrator intentIntegrator =new IntentIntegrator(Scan.this);
    public static final String Text = "tex";
    public static final String Type = "type";
    public boolean canBack = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将状态栏透明
        StatusBar statusBar = new StatusBar(Scan.this);
        statusBar.setStatusBarColor(R.color.transparent);

        //去除默认标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        intentIntegrator.setPrompt("         请将识别框对准二维码");
        intentIntegrator.setBeepEnabled(true);
        intentIntegrator.setOrientationLocked(true);
        intentIntegrator.setBarcodeImageEnabled(true);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setCaptureActivity(Capture.class);
        intentIntegrator.initiateScan();
    }



    //消息回传  处理扫描界面获得的二维码信息并在当前活动进行处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(data==null){
            finish();
        }
        IntentResult intentResult=IntentIntegrator.parseActivityResult(requestCode,resultCode,data);  //获取回传信息
        //不是URL 直接将文本输出
        //使用一个文本确认框的形式
        Intent intent = new Intent(Scan.this, Print.class);  //intent是Android里用于activity之间信息传递的类

        String text = intentResult.getContents();
        //判断是否是112005353,P231227002格式
        try{
            if (text.length() == 20) {
                String[] split = text.split(",");
                if (split.length == 2) {
                    if (split[0].length() == 9 && split[1].length() == 10) {
                        Log.d("success","二维码内容正确");
                        intent.putExtra(Print.Matnr, split[0]);
                        intent.putExtra(Print.Charg, split[1]);
                        startActivity(intent);
                        finish();
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
            DialogUtils.showCustomDialog(Scan.this, "二维码内容不正确", (dialog, which) -> {
                dialog.dismiss();
                intentIntegrator.initiateScan();
            });
        }



    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    public static class RegexUtill {
        //判断字符串是否是URL的类
        public boolean verifyUrl(String url){
            // URL验证规则
            String regEx ="^([hH][tT]{2}[pP]://|[hH][tT]{2}[pP][sS]://)" +
                    "(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~/])+$";
            // 编译正则表达式
            Pattern pattern = Pattern.compile(regEx);

            Matcher matcher = pattern.matcher(url);
            // 字符串是否与正则表达式相匹配
            return matcher.matches();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); // 返回上一页
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
