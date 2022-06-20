package com.example.tp_flutter_plugin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.tokenpocket.opensdk.base.TPListener;
import com.tokenpocket.opensdk.base.TPManager;
import com.tokenpocket.opensdk.simple.model.Authorize;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry;

/** TpFlutterPlugin */
public class TpFlutterPlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {

  private MethodChannel channel;
  private Context context;
  //private Activity activity;
  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "tp_flutter_plugin");
    channel.setMethodCallHandler(this);
    //context = flutterPluginBinding.getApplicationContext();
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }else if ("authorize".equals(call.method)) {
      authorize(call.arguments, result);
    }  else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  private void authorize(Object args, Result result) {
    HashMap map = (HashMap) args;
    try {
      String testMessage = map.get("memo").toString();
      Map<String, Object> resMap = new HashMap<>();
      resMap.put("status", "aaaaaaa");
      resMap.put("message", "我收到了：" + testMessage);
      tpAuthorize();
      //channel.invokeMethod("getAuthInfo", "阿迪是的撒打算打算");
      result.success(resMap);
    } catch (Exception e) {
      e.printStackTrace();
      channel.invokeMethod("getAuthInfo", e.getMessage());
    }
  }

  private void tpAuthorize() {
    Authorize authorize = new Authorize();
    authorize.setBlockchain("EOS");
    authorize.setProtocol("TokenPocket");
    authorize.setVersion("1.0");
    authorize.setDappName("Test demo");
    authorize.setDappIcon("https://eosknights.io/img/icon.png");
    authorize.setActionId("web-db4c5466-1a03-438c-90c9-2172e8becea5");
    authorize.setMemo("demo");
    authorize.setAction("login");
    authorize.setExpired(1000);
    TPManager tpManager = TPManager.getInstance();
    if(context==null){
      channel.invokeMethod("getAuthInfo", "context is null");
    }else{
      try {
        channel.invokeMethod("getAuthInfo", "context is not null");
        Toast.makeText(context, "sssss", Toast.LENGTH_LONG).show();
        PackageManager packageManager = context.getPackageManager();

        PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(),0);
        int val3 = packageInfo.applicationInfo.labelRes;
        //getPackageName:com.example.tp_flutter_plugin_example
        channel.invokeMethod("getAuthInfo", "getPackageName:" + context.getPackageName());
        channel.invokeMethod("getAuthInfo", "labelRes:"+ val3);
        String value = context.getResources().getString(val3);
        channel.invokeMethod("getAuthInfo", "value:"+ value);
      } catch (PackageManager.NameNotFoundException e) {
        e.printStackTrace();
      }
    }
    tpManager.authorize(context, authorize, new TPListener() {
      @Override
      public void onSuccess(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        // 主动返回数据给Flutter
        channel.invokeMethod("getAuthInfo", s);
      }

      @Override
      public void onError(String s) {
        channel.invokeMethod("getAuthInfo", s);
      }

      @Override
      public void onCancel(String s) {
        channel.invokeMethod("getAuthInfo", s);
      }
    });

  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    this.context = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
    context = binding.getActivity();
  }

  @Override
  public void onDetachedFromActivity() {

  }
}
