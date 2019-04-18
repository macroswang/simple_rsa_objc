import 'dart:async';

import 'package:flutter/services.dart';

const MethodChannel _channel =
      const MethodChannel('simple_rsa_objc');

Future<String> encryptString(String txt, String publicKey) async {
  try {
    publicKey = publicKey.replaceAll("-----BEGIN PUBLIC KEY-----", "").replaceAll("-----END PUBLIC KEY-----", "");
    final String result = await _channel
        .invokeMethod('encrypt', {"txt": txt, "publicKey": publicKey});
    return result;
  } on PlatformException catch (e) {
    throw "Failed to get string encoded: '${e.message}'.";
  }
}

Future<String> decryptString(String txt, String privateKey) async {
  try {
    privateKey = privateKey.replaceAll("-----BEGIN PRIVATE KEY-----", "").replaceAll("-----END PRIVATE KEY-----", "");
    privateKey = privateKey.replaceAll("-----BEGIN RSA PRIVATE KEY-----", "").replaceAll("-----END RSA PRIVATE KEY-----", "");
    final String result = await _channel
        .invokeMethod('decrypt', {"txt": txt, "privateKey": privateKey});
    return result;
  } on PlatformException catch (e) {
    throw "Failed decoded string: '${e.message}'.";
  }
}