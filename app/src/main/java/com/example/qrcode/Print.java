package com.example.qrcode;

import android.accessibilityservice.AccessibilityService;
import android.app.AlertDialog;
import android.bld.print.configuration.PrintConfig;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.info.LogDicMaterialInformationTrueAdapter;
import com.example.lc_print_sdk.PrintUtil;
import com.example.model.ApiPostModel;
import com.example.model.LogDicMaterialInformationTrue;
import com.example.model.MB52;
import com.example.model.ResponseModel;
import com.example.utils.BitmapUtils;
import com.example.utils.CommonUtils;
import com.example.utils.DialogUtils;
import com.example.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * @Author: lcd
 * @Date: 2023/12/26 15:44
 * @Version 1.0
 * @Description:打印查询页面
 */
public class Print extends BaseActivity implements PrintUtil.PrinterBinderListener {
    IntentIntegrator intentIntegrator =new IntentIntegrator(Print.this);
    public static final String Matnr = "matnr";
    public static final String Charg = "charg";
    public static final String url = "http://bi.guilinpharma.com:7080/api/getMaterialInformation";
    private String mPageType;
    private androidx.recyclerview.widget.RecyclerView recyclerView;

    private LogDicMaterialInformationTrueAdapter adapter;
    private List<LogDicMaterialInformationTrue> logDicMaterialInformationTrueList;

    private Context mContext;
    private EditText editNoTempCon;
    PrintUtil printUtil;
//    public static final String url = "http://127.0.0.1:8801/api/getMaterialInformation";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //将状态栏透明
        StatusBar statusBar = new StatusBar(Print.this);
        statusBar.setStatusBarColor(R.color.purple_500);
        setContentView(R.layout.print);
        //去除默认标题栏
        androidx.appcompat.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("打印查询");
            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.hide();
        }
        String matnr = getIntent().getStringExtra(Matnr);
        String charg = getIntent().getStringExtra(Charg);

        if(matnr != null && charg != null){
            AlertDialog dialog = DialogUtils.showLoding(this);
            try {
                ApiPostModel apiPostModel = new ApiPostModel();
                apiPostModel.setMatnr(matnr);
                apiPostModel.setCharg(charg);
                Gson gson = new Gson();
                String json = gson.toJson(apiPostModel);
                Log.d("json",json);
                MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
                OkHttpUtils.postString().url(url).addHeader("Content-Type", "application/json").addHeader("Accept", "*/*").content(json).mediaType(mediaType).build().execute(new StringCallback()
                {
                    @Override
                    public void onError(Call call, Exception e) {
                        Log.d("error","接口请求失败");
                        Log.d("error",e.toString());
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
                            Gson gson = new Gson();
                            Type type = new TypeToken<ResponseModel<List<LogDicMaterialInformationTrue>>>(){}.getType();
                            ResponseModel<List<LogDicMaterialInformationTrue>> responseModel = gson.fromJson(response,type);
                            if("0".equals(responseModel.getCode())){
                                logDicMaterialInformationTrueList = responseModel.getData();
                                Log.d("请求返回",logDicMaterialInformationTrueList.toString());
                                setCanBack(logDicMaterialInformationTrueList);
                                adapter = new LogDicMaterialInformationTrueAdapter(logDicMaterialInformationTrueList);
                                recyclerView = findViewById(R.id.recyclerView);
                                recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(Print.this));
                                recyclerView.setAdapter(adapter);

                            }else{
                                DialogUtils.showCustomDialog(Print.this,"提示","数据获取失败          ",null);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d("error","接口请求解析json失败");
                            Log.d("error",e.toString());
                        }

                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("error","接口请求解析json失败");
                Log.d("error",e.toString());
            }
            if (dialog.isShowing()) {
                dialog.dismiss(); // 关闭对话框
            }
        }
    }

    public void setCanBack(List<LogDicMaterialInformationTrue> logs){
        if(logs.size() != 0) {

            for (int i = 0; i < logs.size(); i++) {
                LogDicMaterialInformationTrue log = logs.get(i);
                DialogInterface.OnClickListener myCanback = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("点击了确认", "点击了确认");
                        printTest(log);

                    }
                };
                log.setCanback(myCanback);
            }
        }
    }
    private boolean isPrintEnable = false;
    public void printTest(LogDicMaterialInformationTrue log){
        Log.d("开始打印", log.getMaktx());
        try {
            printUtil=PrintUtil.getInstance (this);
            printUtil.setPrintEventListener (this);
            isPrintEnable = true;
            if ("1".equals (mPageType)) {
                printUtil.printEnableMark (true);
            } else {
                printUtil.printEnableMark (false);
            }
            printTextTemplate(log);
//            log.setPrintTime(CommonUtils.getNowTime());
//            //把log对象转换成json字符串
//            Gson gson = new Gson();
//                         String allData = FileUtils.getFileName(this,"allData.json");
//            if (allData == null) {
//                allData = "[]";
//            }



        }catch (Exception e){
            e.printStackTrace();
            Log.d("error",e.toString());
            Log.d("打印失败","打印失败");
            DialogUtils.showAlertDialog(this, "打印失败", "请检查当前设备是否手持打印机", 3000);
        }

    }

    public void printTextTemplate(LogDicMaterialInformationTrue log) {
        //数据检查
        if(log == null){
            DialogUtils.showAlertDialog(this, "打印失败", "打印数据获取失败", 3000);
        }else{
            if(log.getFydat()==null){
                log.setFydat("/");
            }
            if(log.getVfdat()==null){
                log.setVfdat("/");
            }
            if(log.getStorage()==null){
                log.setStorage("/");
            }
            if(log.getMaker()==null){
                log.setMaker("/");
            }
            if(log.getMnum()==null){
                log.setMnum("/");
            }
            if(log.getNum()==null){
                log.setNum("");
            }
            //打印浓度
//            printUtil.printConcentration(26);

            //标题
//            printUtil.printText(PrintConfig.Align.ALIGN_CENTER, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "------------------------------------------------------\n");

            printUtil.printText(PrintConfig.Align.ALIGN_CENTER, PrintConfig.FontSize.TOP_FONT_SIZE_LARGE, true, false, "进厂物料标签\n");

            printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_XSMALL, false, false, "文件号: SOP-LD-000001-A03-01\n");

//            printUtil.printText(PrintConfig.Align.ALIGN_CENTER, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "------------------------------------------------------\n");

//        printUtil.printLine (1);


            //物料信息
            printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "物料代码: \t"+log.getMatnr()+"\n");
            printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "物料描述: \t"+log.getMaktx()+"\n");
            printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "生产商编码: \t"+log.getMnum()+"\n");
            printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "自编批号: \t"+log.getCharg()+"\n");

            printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "供应商批号: \t"+log.getMaker()+"\n");

            printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "数量: "+log.getNum()+"\n");
            printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "有效期至: \t"+log.getVfdat()+"\n");
            printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "复验期: \t"+log.getFydat()+"\n");
            printUtil.printText(PrintConfig.Align.ALIGN_LEFT, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "储存条件: \t"+log.getStorage()+"\n");
//            printUtil.printText(PrintConfig.Align.ALIGN_CENTER, PrintConfig.FontSize.TOP_FONT_SIZE_MIDDLE, false, false, "------------------------------------------------------\n");

            String text=log.getMatnr()+","+log.getCharg();
            Bitmap bitmap=BitmapUtils.encode2dAsBitmap (text, BarcodeFormat.QR_CODE, 300, 300);
            bitmap=BitmapUtils.compressPic (bitmap, 300, 300, 80);
            printUtil.printBitmap (PrintConfig.Align.ALIGN_CENTER, bitmap);

//        printUtil.printBitmap(bitmap);

            printUtil.printLine (4);
            printUtil.start();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy ();
        if(isPrintEnable) printUtil.removePrintListener(this);
        recyclerView = null;
    }
    public boolean canBack = true;
    @Override
    protected boolean titleCanBack() {
        return  canBack;
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
    public void onPrintCallback(int state) {
        Log.e ("testtest", "  state:" + state);

        canBack=true;
        if (PrintConfig.IErrorCode.ERROR_NO_ERROR == state) {
            //打印成功
            showToast(getString (R.string.toast_print_success));
        } else if (PrintConfig.IErrorCode.ERROR_PRINT_NOPAPER == state) {
            showToast(getString (R.string.toast_no_paper));
        } else if (PrintConfig.IErrorCode.ERROR_DATA_INPUT == state) {
            showToast(getString (R.string.toast_input_parameter_error));
        } else if (PrintConfig.IErrorCode.ERROR_CMD == state) {
            showToast(getString (R.string.toast_Instruction_error));
        } else if (PrintConfig.IErrorCode.ERROR_DATA_INVALID == state) {
            showToast(getString (R.string.toast_data_is_invalid));
        } else if (PrintConfig.IErrorCode.ERROR_DEV_BMARK == state) {
            showToast(getString (R.string.toast_abnormal_black_mark_detection));
        } else if (PrintConfig.IErrorCode.ERROR_DEV_FEED == state) {
            showToast(getString (R.string.toast_moving_paper));
        } else if (PrintConfig.IErrorCode.ERROR_DEV_IS_BUSY == state) {
            showToast(getString (R.string.toast_device_busy));
        } else if (PrintConfig.IErrorCode.ERROR_DEV_NOT_OPEN == state) {
            showToast(getString (R.string.toast_device_is_not_turned_on));
        } else if (PrintConfig.IErrorCode.ERROR_DEV_NO_BATTERY == state) {
            showToast(getString (R.string.toast_low_electricity));
        } else if (PrintConfig.IErrorCode.ERROR_DEV_PRINT == state) {
            showToast(getString (R.string.toast_print_now));
        } else if (PrintConfig.IErrorCode.ERROR_GRAY_INVALID == state) {
            showToast(getString (R.string.toast_illegal_concentrationr));
        } else if (PrintConfig.IErrorCode.ERROR_NO_DATA == state) {
            showToast(getString (R.string.toast_no_data));
        } else if (PrintConfig.IErrorCode.ERROR_PRINT_BARCODE == state) {
            showToast(getString (R.string.toast_error_printing_barcode));
        } else if (PrintConfig.IErrorCode.ERROR_PRINT_BITMAP == state) {
            showToast(getString (R.string.toast_error_printing_bitmap));
        } else if (PrintConfig.IErrorCode.ERROR_PRINT_BITMAP_WIDTH_OVERFLOW == state) {
            showToast(getString (R.string.toast_print_bitmap_width_overflow));
        } else if (PrintConfig.IErrorCode.ERROR_PRINT_DATA_MAC == state) {
            showToast(getString (R.string.toast_mac_check_error));
        } else if (PrintConfig.IErrorCode.ERROR_PRINT_HOT == state) {
            showToast(getString (R.string.toast_high_temperature));
        } else if (PrintConfig.IErrorCode.ERROR_PRINT_ILLEGAL_ARGUMENT == state) {
            showToast(getString (R.string.toast_parameter_error));
        } else if (PrintConfig.IErrorCode.ERROR_PRINT_QRCODE == state) {
            showToast(getString (R.string.toast_error_printing_qrcode));
        } else if (PrintConfig.IErrorCode.ERROR_PRINT_TEXT == state) {
            showToast(getString (R.string.toast_print_text_error));
        } else if (PrintConfig.IErrorCode.ERROR_PRINT_UNKNOWN == state) {
            showToast(getString (R.string.toast_unknown_error));
        } else if (PrintConfig.IErrorCode.ERROR_RESULT_EXIST == state) {
            showToast(getString (R.string.toast_result_already_exists));
        } else if (PrintConfig.IErrorCode.ERROR_TIME_OUT == state) {
            showToast(getString (R.string.toast_overtime));
        } else {
            showToast("Printer error. state=" + state);
        }
    }

    @Override
    public void onVersion(String s) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void onBackPressed() {
        if (!canBack) {
            return;
        }
        super.onBackPressed ();
    }

    //获取sap数据
    public void getSapData(){
        for (int i = 0; i < logDicMaterialInformationTrueList.size(); i++) {
            LogDicMaterialInformationTrue item = logDicMaterialInformationTrueList.get(i);
            ApiPostModel apiPostModel = new ApiPostModel();
            apiPostModel.setMatnr(item.getMatnr());
            apiPostModel.setCharg(item.getCharg());
            Gson gson = new Gson();
            String json = gson.toJson(apiPostModel);
            Log.d("json",json);
            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
            OkHttpUtils.postString().url(url).addHeader("Content-Type", "application/json").addHeader("Accept", "*/*").content(json).mediaType(mediaType).build().execute(new StringCallback()
            {
                @Override
                public void onError(Call call, Exception e) {
                    Log.d("error","接口请求失败");
                    Log.d("error",e.toString());
                }

                @Override
                public void onResponse(String response) {
                    try {
                        //请求成功的处理
                        Log.d("success","接口请求成功");
                        Log.d("success",response);
                        //解析json
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<MB52>>(){}.getType();
                        List<MB52> responseModel = gson.fromJson(response,type);
                        if(responseModel.size()>0){
                            MB52 item  = responseModel.get(0);
                            item.setCharg(item.getCharg());

                        }else{
                            DialogUtils.showCustomDialog(Print.this,"提示","数据获取失败          ",null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("error","接口请求解析json失败");
                        Log.d("error",e.toString());
                    }

                }

            });
        }
    }

}
