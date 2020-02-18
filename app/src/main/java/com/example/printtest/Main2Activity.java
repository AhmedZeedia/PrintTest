package com.example.printtest;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.starmicronics.stario.StarIOPort;
import com.starmicronics.stario.StarIOPortException;
import com.starmicronics.starioextension.ICommandBuilder;
import com.starmicronics.starioextension.StarIoExt;

import java.nio.charset.Charset;

public class Main2Activity extends AppCompatActivity {


    public static final String IDENTIFIER_BUNDLE_KEY     = "IDENTIFIER_BUNDLE_KEY";
    public static final String INTERFACE_TYPE_BUNDLE_KEY = "INTERFACE_TYPE_BUNDLE_KEY";

    private StarIOPort mPort;
    private byte[] mCommands ;
    private String macAddress = IDENTIFIER_BUNDLE_KEY;
    private String mPortType;
    private String mPortIdentifier;
    private TextView mPrinterName;
    private TextView mMelody;
    private TextView mScanner;
    private TextView mScreenDisplay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mPortType = getIntent().getStringExtra(INTERFACE_TYPE_BUNDLE_KEY);
        mPortIdentifier = getIntent().getStringExtra(IDENTIFIER_BUNDLE_KEY);
        mPrinterName = findViewById(R.id.printerName);
        mMelody = findViewById(R.id.melody);
        mScanner = findViewById(R.id.Scanner);
        mScreenDisplay = findViewById(R.id.customerFacing);
        mPrinterName.setText(mPortType);


    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mPort == null) {
            try {
                mPort = StarIOPort.getPort("BT:" + mPortIdentifier, "", 10000, this);
                byte[] command = createTextReceiptData(StarIoExt.Emulation.StarLine);
                mPort.writePort(command,0,command.length);

            } catch (StarIOPortException e) {
                Log.e("Star Error","", e);
            }
            finally {
                try {
                    StarIOPort.releasePort(mPort);
                }
                catch (StarIOPortException e) {}
            }
        }
        else{
            Toast toast = Toast.makeText(this, "Error", Toast.LENGTH_SHORT);
            toast.show();
        }



    }

    byte[] createTextReceiptData(StarIoExt.Emulation emulation) {
        ICommandBuilder builder = StarIoExt.createCommandBuilder(emulation);

        builder.beginDocument();
        builder.appendCodePage(ICommandBuilder.CodePageType.UTF8);

        builder.append((" " +
                "\"Star Clothing Boutique test impact \\n\" +\n" +
                "\"123 Star Road\\n\" +\n" +
                "\"City, State 12345\\n\" +\n" +
                "\"\\nDescription          Total\\n\" +\n" +
                "\"300678566       PLAIN T-SHIRT        10.99\\n\" +\n" +
                "\"300692003       BLACK DENIM          29.99\\n\" +\n" +
                "\"300651148       BLUE DENIM           29.99\\n\" +\n" +
                "\"300642980       STRIPED DRESS        49.99\\n\" +\n" +
                "\"300638471       BLACK BOOTS          35.99\\n\" +\n" +
                "\"\\n\" +\n" +
                "\"Subtotal                            156.95\\n\" +\n" +
                "\"Tax                                   0.00\\n\" +\n" +
                "\"------------------------------------------\\n\" +\n" +
                "\"Total                              $156.95\\n\" +\n" +

                "\"\" +\n" +
                "\"Charge\\n\" +\n" +
                "\"156.95\\n\" +\n" +
                "\"Visa XXXX-XXXX-XXXX-0123\\n\"").getBytes(Charset.forName("UTF-8")));
        builder.appendCutPaper(ICommandBuilder.CutPaperAction.PartialCutWithFeed);

        builder.endDocument();

        return builder.getCommands();
    }




}

