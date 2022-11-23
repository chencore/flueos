import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:eos_flutter_plugin/TPListener.dart';
import 'package:eos_flutter_plugin/tp_flutter_plugin.dart';
import 'package:dart_esr/dart_esr.dart' as Esr;

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> implements TPListener {
  String _platformVersion = 'Unknown';

  @override
  void initState() {
    TpFlutterPlugin.addTPListener(this);
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;

    try {
      // platformVersion =
      //     await TpFlutterPlugin.platformVersion ?? 'Unknown platform version';
      // platformVersion = await TpFlutterPlugin.authorize("bbb");
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    if (!mounted) return;

    setState(() {
      //_platformVersion = platformVersion;
    });
  }

  //Text('Running on: $_platformVersion\n'),
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
        home: Scaffold(
      appBar: AppBar(
        title: const Text('Plugin example app'),
      ),
      body: Center(
        child: Row(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            ElevatedButton(
              onPressed: () async {
                await TpFlutterPlugin.authorize("授权");
              },
              child: const Text('TP授权'),
            ),
            ElevatedButton(
              onPressed: () async {
                await TpFlutterPlugin.transfer("edenbettest1", "edenbettest2",
                    "eos.io", "EOS", "0.001", "testaab");
              },
              child: const Text('TP转账测试'),
            ),
            ElevatedButton(
              onPressed: () async {
                var esr = Esr.EOSIOSigningrequest('https://api.eosn.io', 'v1',
                    chainName: Esr.ChainName.EOS);
                var auth = <Esr.Authorization>[
                  Esr.Authorization()
                    ..actor = 'testName1111'
                    ..permission = 'active'
                ];

                var data = <String, String>{'name': 'data'};

                var actions = <Esr.Action>[
                  Esr.Action()
                    ..account = 'eosnpingpong'
                    ..name = 'ping'
                    ..authorization = auth
                    ..data = data,
                ];

                var transaction = Esr.Transaction()..actions = actions;

                var encoded = await esr.encodeTransaction(transaction);
              },
              child: const Text('Anchor_test'),
            )
          ],
        ),
      ),
    ));
  }

  @override
  void receiveMsg(String message) {
    // TODO: implement receiveMsg
    print(message);
  }
}
