package com.xreddot.simple_rsa_objc;

import android.util.Log;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** SimpleRsaObjcPlugin */
public class SimpleRsaObjcPlugin implements MethodCallHandler {
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "simple_rsa_objc");
    channel.setMethodCallHandler(new SimpleRsaObjcPlugin());
  }

  @Override
  public void onMethodCall(MethodCall call, Result result) {
    Log.i("onMethodCall","this mehod is:"+call.method);
    if (call.method.equals("encrypt")) {
      String text = call.argument("txt");
      String publicKey = call.argument("publicKey");
      result.success(RSAUtil.publicEncrypt(text, publicKey));
    }else if (call.method.equals("decrypt")) {
      String text = call.argument("txt");
      String privateKey = call.argument("privateKey");
      result.success(RSAUtil.privateDecrypt(text,privateKey));
    }
    else {
      result.notImplemented();
    }
  }
}
