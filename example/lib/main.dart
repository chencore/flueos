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
          title: const Text('FluEOS example app'),
        ),
        body: Column(
          //mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            buildTitleSection(context),
            ElevatedButton(
              onPressed: () async {
                await TpFlutterPlugin.authorize("授权");
              },
              child: const Text('TP钱包授权'),
            ),
            ElevatedButton(
              onPressed: () async {
                await TpFlutterPlugin.transfer("edenbettest4", "edenbettest2",
                    "eos.io", "EOS", "0.001", "11111ssd");
              },
              child: const Text('TP钱包转账'),
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
              child: const Text('Anchor钱包转账'),
            )
          ],
        ),
      ),
    );
  }

  Widget buildTitleSection(BuildContext context) {
    return Container(
      padding: const EdgeInsets.all(32),
      child: Row(
        children: [
          Expanded(
            /*1*/
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                /*2*/
                Container(
                  padding: const EdgeInsets.only(bottom: 8),
                  child: const Text(
                    'Oeschinen Lake Campground',
                    style: TextStyle(
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                Text(
                  'Kandersteg, Switzerland',
                  style: TextStyle(
                    color: Colors.grey[500],
                  ),
                ),
              ],
            ),
          ),
          /*3*/
          Icon(
            Icons.star,
            color: Colors.red[500],
          ),
          const Text('41'),
        ],
      ),
    );
  }

  @override
  void receiveMsg(String message) {
    print(message);
  }
}
