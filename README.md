# Simple RSA Encryption

Flutter plugin to encrypt, decrypt (RSA/ECB/PCSK1), verify and sign string with a public and a private key

Support for ANDROID and iOS(thanks to adlanarifzr)

## Installation

To use the plugin, add `simple_rsa` as a
[dependency in your pubspec.yaml file](https://flutter.io/platform-plugins/).

## Usage

First, initialize private and public key. Preferably in BASE64 format.

```
final publicKey = '...';
final privateKey = '...';
```

After that, you can encrypt or decyrpt text

```
let plainText = 'something';
final encryptedText = await encryptString(plainText, utf8.decode(base64.decode(publicKey)));
final decryptedText = await decryptString(encryptedText, utf8.decode(base64.decode(privateKey)));


## Example

See the [example application](https://github.com/macroswang/simple_rsa_objc/tree/master/example) source
for a complete sample app using the Simple RSA encryption.

### Contributions
[Adlan Arif Zakaria (adlanarifzr)](https://github.com/macroswang) iOS compatibility, sign and verify method.
