
import 'dart:async';

import 'package:flutter/services.dart';

import 'TPListener.dart';

class TpFlutterPlugin {
  static MethodChannel _channel = MethodChannel('tp_flutter_plugin')..setMethodCallHandler(_handler);
  ///监听
  static final tpListeners = <TPListener>[];

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> authorize(String memo) async {
    Map map = await _channel.invokeMethod("authorize", {
      "memo" : memo
    });
    return map["status"];
  }

  //处理安卓原生送过来的消息
  static Future<dynamic> _handler(MethodCall methodCall) {
    if ("getAuthInfo" == methodCall.method) {
      // 监听
      receiveData(methodCall.arguments);
    }
    return Future.value(true);
  }

  /// [map]即Native传回来的arguments
  static void receiveData(String data) {
    for (var listener in tpListeners) {
      listener.receiveMsg(data);
    }
  }

  /// 添加该监听
  static void addTPListener(TPListener tpListener) {
    tpListeners.add(tpListener);
  }
}
