package com.example.tp_flutter_plugin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.tokenpocket.opensdk.base.TPListener;
import com.tokenpocket.opensdk.base.TPManager;
import com.tokenpocket.opensdk.innerwallet.model.LinkAction;
import com.tokenpocket.opensdk.simple.model.Authorize;
import com.tokenpocket.opensdk.simple.model.Signature;
import com.tokenpocket.opensdk.simple.model.Transaction;
import com.tokenpocket.opensdk.simple.model.Transfer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
    } else if ("transfar".equals(call.method)) {
      transfar(call.arguments, result);
    } else if ("sign".equals(call.method)) {
      sign(call.arguments, result);
    } else if ("pushTx".equals(call.method)) {
      pushTx(call.arguments, result);
    }else {
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
      resMap.put("status", "1");
      resMap.put("message", "我收到了：" + testMessage);
      tpAuthorize();
      //channel.invokeMethod("getAuthInfo", "阿迪是的撒打算打算");
      result.success(resMap);
    } catch (Exception e) {
      e.printStackTrace();
      channel.invokeMethod("getAuthInfo", e.getMessage());
    }
  }

  private void transfar(Object args, Result result) {
    HashMap map = (HashMap) args;
    try {
      String from = map.get("from").toString();
      String to = map.get("to").toString();
      String contract = map.get("contract").toString();
      String symbol = map.get("symbol").toString();
      double amount = Double.valueOf(map.get("amount")==null?"0":map.get("amount").toString());
      String desc = map.get("desc").toString();
      Map<String, Object> resMap = new HashMap<>();
      resMap.put("status", "1");
      resMap.put("message", "transfar");
      transfer(from,to,contract,symbol,amount,desc);
      //channel.invokeMethod("getAuthInfo", "阿迪是的撒打算打算");
      result.success(resMap);
    } catch (Exception e) {
      e.printStackTrace();
      channel.invokeMethod("getAuthInfo", e.getMessage());
    }
  }

  private void sign(Object args, Result result) {
    HashMap map = (HashMap) args;
    try {
      Map<String, Object> resMap = new HashMap<>();
      resMap.put("status", "1");
      resMap.put("message", "sign：");
      sign();
      result.success(resMap);
    } catch (Exception e) {
      e.printStackTrace();
      channel.invokeMethod("getSignInfo", e.getMessage());
    }
  }

  private void pushTx(Object args, Result result) {
    HashMap map = (HashMap) args;
    try {
      String actions = map.get("actions").toString();
      Map<String, Object> resMap = new HashMap<>();
      resMap.put("status", "1");
      resMap.put("message", "actions：" + actions);
      pushTx(actions);
      result.success(resMap);
    } catch (Exception e) {
      e.printStackTrace();
      channel.invokeMethod("getTxInfo", e.getMessage());
    }
  }

  private void sign() {
    Signature signature = new Signature();
    signature.setBlockchain("EOS");
    signature.setProtocol("TokenPocket");
    signature.setVersion("1.0");
    signature.setDappName("Test demo");
    signature.setDappIcon("https://eosknights.io/img/icon.png");
    //signature.setActionId("web-db4c5466-1a03-438c-90c9-2172e8becea5");
    UUID uuid = UUID.randomUUID();
    signature.setActionId("web-"+uuid.toString());
    signature.setMemo("demo");
    signature.setAction("sign");
    signature.setMessage("message to sign");
    TPManager.getInstance().signature(context, signature, new TPListener() {
      @Override
      public void onSuccess(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        channel.invokeMethod("getSignInfo", s);
      }

      @Override
      public void onError(String s) {
        channel.invokeMethod("getSignInfo", s);
      }

      @Override
      public void onCancel(String s) {
        channel.invokeMethod("getSignInfo", s);
      }
    });
  }

  private void transfer(String from,String to,String contract,String symbol,double amount,String desc) {
    Transfer transfer = new Transfer();
    transfer.setBlockchain("EOS");
    transfer.setProtocol("TokenPocket");
    transfer.setVersion("1.0");
    transfer.setDappName("Test demo");
    transfer.setDappIcon("https://eosknights.io/img/icon.png");
    //transfer.setActionId("web-db4c5466-1a03-438c-90c9-2172e8becea5");
    UUID uuid = UUID.randomUUID();
    transfer.setActionId("web-"+uuid.toString());
    transfer.setMemo("demo");
    transfer.setAction("transfer");
    transfer.setFrom(from);
    transfer.setTo(to);
    transfer.setPrecision(4);
    transfer.setContract(contract);
    transfer.setAmount(amount);
    transfer.setSymbol(symbol);
    transfer.setDesc(desc);
    TPManager.getInstance().transfer(context, transfer, new TPListener() {
      @Override
      public void onSuccess(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        //channel.invokeMethod("pushTx", s);
        channel.invokeMethod("getTransInfo", s);
      }

      @Override
      public void onError(String s) {
        channel.invokeMethod("getTransInfo", s);
      }

      @Override
      public void onCancel(String s) {
        channel.invokeMethod("getTransInfo", s);
      }
    });
  }

  private void pushTx(String actions) {
    Transaction transaction = new Transaction();
    transaction.setBlockchain("EOS");
    transaction.setProtocol("TokenPocket");
    transaction.setVersion("1.0");
    transaction.setDappName("Test demo");
    transaction.setDappIcon("https://eosknights.io/img/icon.png");
    //transaction.setActionId("web-db4c5466-1a03-438c-90c9-2172e8becea5");
    UUID uuid = UUID.randomUUID();
    transaction.setActionId("web-"+uuid.toString());
    transaction.setAction("pushTransaction");
    transaction.setLinkActions(new ArrayList<LinkAction>());
    transaction.setActions(actions);
    TPManager.getInstance().pushTransaction(context, transaction, new TPListener() {
      @Override
      public void onSuccess(String s) {
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
        channel.invokeMethod("getTxInfo", s);
      }

      @Override
      public void onError(String s) {
        channel.invokeMethod("getTxInfo", s);
      }

      @Override
      public void onCancel(String s) {
        channel.invokeMethod("getTxInfo", s);
      }
    });
  }

  private void tpAuthorize() {
    Authorize authorize = new Authorize();
    authorize.setBlockchain("EOS");
    authorize.setProtocol("TokenPocket");
    authorize.setVersion("1.0");
    authorize.setDappName("Test demo");
    authorize.setDappIcon("https://eosknights.io/img/icon.png");
    //authorize.setActionId("web-db4c5466-1a03-438c-90c9-2172e8becea5");
    UUID uuid = UUID.randomUUID();
    //transfer.setActionId("web-"+uuid.toString());
    authorize.setMemo("demo");
    authorize.setAction("login");
    authorize.setExpired(1000);
    TPManager tpManager = TPManager.getInstance();
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
