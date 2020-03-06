package app.com.customviews.activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.X509EncodedKeySpec;
import java.util.TreeSet;

import android.util.Base64;

import javax.crypto.Cipher;


import app.com.customviews.R;

public class ChecksumActivity extends AppCompatActivity {

    TextInputEditText etString;

    MaterialButton btnConvert;

    TextView tvOutput;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checksum_);

        etString = findViewById(R.id.et_string);

        btnConvert = findViewById(R.id.btn_convert);

        tvOutput = findViewById(R.id.tv_output);

        btnConvert.setOnClickListener(view -> convertString());
    }


    void convertString() {
        if (!TextUtils.isEmpty(etString.getText().toString())) {

            try {
                PublicKey publicKey = readPublicKey();
                Log.d("ChecksumActivity--> ", "readpublickey--> " + publicKey);
                byte[] encrypted = encrypt(etString.getText().toString(), publicKey);
                Log.d("ChecksumActivity--> ", "encryptedBytesofKey--> " + encrypted);
//                tvOutput.setText("Encrypted bytes Hex String: \n" + encrypted);
                tvOutput.setText("Encrypted bytes Hex String: \n" + bytesToHexString(encrypted));
                Log.d("ChecksumActivity--> ", "finalOutputHex--> " + bytesToHexString(encrypted).toUpperCase());

                TreeSet<String> algs = new TreeSet<>();
                for (Provider provider : Security.getProviders()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        provider.getServices().stream().filter(s -> "Cipher".equals(s.getType())).map(Provider.Service::getAlgorithm).forEach(algs::add);
                    }
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    algs.stream().forEach(System.out::println);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Toast.makeText(this, "Enter string", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    public PublicKey readPublicKey() throws Exception {

        //InputStream inputPublickey = RsaEncrDecryption.class.getClassLoader().getResourceAsStream("Public_Key.pem");
        //InputStream inputPublickey = RsaEncrDecryption.class.getClassLoader().getResourceAsStream("public_key1.pem");
        /*InputStream inputPublickey = RsaEncrDecryption.class.getClassLoader().getResourceAsStream(Key_Path);
        byte[] keyBytes = new byte[inputPublickey.available()];
        inputPublickey.read(keyBytes);
        inputPublickey.close();*/
        InputStream inputPublickey = getAssets().open("kayome_public_key.txt");
        byte[] keyBytes = new byte[inputPublickey.available()];
        inputPublickey.read(keyBytes);
        inputPublickey.close();
        Log.d("ChecksumActivity--> ", "Keybytes--> " + keyBytes);

        String pubKey = new String(keyBytes, "UTF-8");
        pubKey = pubKey.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
        Log.d("ChecksumActivity--> ", "pubKeyBeforeEncode--> " + pubKey);

        keyBytes = Base64.decode(pubKey, Base64.DEFAULT);

        // generate public key


        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);

        System.out.println(String.format("public Key format: %s, algorithm: %s",
                publicKey.getFormat(), publicKey.getAlgorithm()));
        Log.d("ChecksumActivity--> ", "public Key format: + " + " algorithm: %s" +
                publicKey.getFormat() + publicKey.getAlgorithm());

        System.out.println("Public Key : " + publicKey.getEncoded().toString());
        Log.d("ChecksumActivity--> ", "Public Key : " + publicKey.getEncoded().toString());

        return publicKey;
    }

    public static byte[] encrypt(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
            // get an RSA cipher object and print the provider

            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");


            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text.getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null || bytes.length <= 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            int v = bytes[i] & 0xff;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
