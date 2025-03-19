<?php
function encryptAES128($data, $key) {
    $iv = random_bytes(16); 
    $encryptedData = openssl_encrypt($data, 'AES-128-CBC', $key, OPENSSL_RAW_DATA, $iv);
    return ['encryptedData' => $encryptedData, 'iv' => $iv];
}
function decryptAES128($encryptedData, $key, $iv) {
    return openssl_decrypt($encryptedData, 'AES-128-CBC', $key, OPENSSL_RAW_DATA, $iv);
}

function generateHash($password, $salt) {
    return hash('sha256', $password . $salt);
}
?>