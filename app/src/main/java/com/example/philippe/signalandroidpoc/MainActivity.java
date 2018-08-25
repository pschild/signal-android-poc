package com.example.philippe.signalandroidpoc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.whispersystems.libsignal.SessionCipher;
import org.whispersystems.libsignal.protocol.CiphertextMessage;

public class MainActivity extends AppCompatActivity {

    private SignalWrapper signalWrapper = new SignalWrapper();
    private TextView outputTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        outputTextView = (TextView)findViewById(R.id.content);

        try {
            startSignalPoc();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startSignalPoc() throws Exception {
        outputTextView.append("1) Registering alice...");
        SignalUser alice = signalWrapper.register("alice");
        outputTextView.append("\nalice registered!");

        outputTextView.append("\n\n2) Registering bob...");
        SignalUser bob = signalWrapper.register("bob");
        outputTextView.append("\nbob registered!");

        outputTextView.append("\n\n3) Alice: Starting session to chat with bob...");
        signalWrapper.initSession(alice, bob);
        outputTextView.append("\nSession started!");

        outputTextView.append("\n\n4) Alice: Encrypting message for bob...");
        SessionCipher aliceSc = signalWrapper.createSessionCipher(alice, bob);
        CiphertextMessage ciphertext = signalWrapper.encrypt(aliceSc, "Hi Bob, this is Alice! How are you?");
        outputTextView.append("\nEncrypted => " + ciphertext.serialize());

        outputTextView.append("\n\n5) Bob: Decrypting alice's message...");
        SessionCipher bobSc = signalWrapper.createSessionCipher(bob, alice);
        String plaintext = signalWrapper.decrypt(bobSc, ciphertext);
        outputTextView.append("\nDecrypted => " + plaintext);

        outputTextView.append("\n\n6) Bob: Starting session to chat with alice...");
        signalWrapper.initSession(bob, alice);
        outputTextView.append("\nSession started!");

        outputTextView.append("\n\n7) Bob: Encrypting message for alice...");
        ciphertext = signalWrapper.encrypt(bobSc, "Hi Alice, this is Bob! I'm fine!");
        outputTextView.append("\nEncrypted => " + ciphertext.serialize());

        outputTextView.append("\n\n8) Alice: Decrypting bob's message...");
        plaintext = signalWrapper.decrypt(aliceSc, ciphertext);
        outputTextView.append("\nDecrypted => " + plaintext);
    }
}
