#import "TpFlutterPlugin.h"
#if __has_include(<tp_flutter_plugin/tp_flutter_plugin-Swift.h>)
#import <tp_flutter_plugin/tp_flutter_plugin-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "tp_flutter_plugin-Swift.h"
#endif

@implementation TpFlutterPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftTpFlutterPlugin registerWithRegistrar:registrar];
}
@end
