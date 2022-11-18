# Flutter SDK for eos（FluEOS）

FluEOS 是一个基于eos链底层使用EOSIO RPC API的SDK插件.

Flutter SDK for eos is an API for integrating with EOSIO-based blockchains using the EOSIO RPC API. 


### TP 使用步骤
因为插件代码中的Tp钱包的sdk没有开源导致该插件无法通过pub.dev的审核，所以只能通过下载源码的方式使用该插件
#### 下载源码
`
git clone https://github.com/chencore/flueos.git
`

#### 在工程的pubspec.yaml文件中引用插件

`
  eos_flutter_plugin:
    path: /flueos_code_path/
`
具体使用可以参考flueos中的example工程




