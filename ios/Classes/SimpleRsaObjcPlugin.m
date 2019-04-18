#import "SimpleRsaObjcPlugin.h"
#import "RSAUtil.h"

@implementation SimpleRsaObjcPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  FlutterMethodChannel* channel = [FlutterMethodChannel
      methodChannelWithName:@"simple_rsa_objc"
            binaryMessenger:[registrar messenger]];
  SimpleRsaObjcPlugin* instance = [[SimpleRsaObjcPlugin alloc] init];
  [registrar addMethodCallDelegate:instance channel:channel];
}

- (void)handleMethodCall:(FlutterMethodCall*)call result:(FlutterResult)result {
    NSLog(@"this method is:%@",call.method);
    NSDictionary *argsMap = call.arguments;
    if ([@"encrypt" isEqualToString:call.method]) {
        NSString *text = argsMap[@"txt"];
        NSString *publicKey = argsMap[@"publicKey"];
        NSString *resultStr = [RSAUtil encryptString:text publicKey:publicKey];
        result(resultStr);
    }else if ([@"decrypt" isEqualToString:call.method]) {
        NSString *text = argsMap[@"txt"];
        NSString *privateKey = argsMap[@"privateKey"];
        NSString *resultStr = [RSAUtil decryptString:text privateKey:privateKey];
        result(resultStr);
    }
    else {
        result(FlutterMethodNotImplemented);
    }
}

@end
