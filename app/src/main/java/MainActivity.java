package com.example.projekt_x;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.nfc.Tag;
import android.os.Handler;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    final ColorStateList csl_siva=new ColorStateList(new int[][]{new int[0]},new int[]{0xff5a595b});
    final ColorStateList csl_plava=new ColorStateList(new int[][]{new int[0]},new int[]{0xff80cbc4});
    final ColorStateList csl_plavaBoja=new ColorStateList(new int[][]{new int[0]},new int[]{0xff0000ff});
    final ColorStateList csl_crvena=new ColorStateList(new int[][]{new int[0]},new int[]{0xffff0000});
    final ColorStateList csl_zelena=new ColorStateList(new int[][]{new int[0]},new int[]{0xff00ff00});
    final ColorStateList csl_zuta=new ColorStateList(new int[][]{new int[0]},new int[]{0xffffff00});

    Button kocka;
    Button piramida;
    Button kugla;
    Button valjak;

    Button crvena;
    Button zelena;
    Button plava;
    Button zuta;

    Button sortiraj;
    Button bluetooth;
    Button zaustavi;

    Switch oblik;
    Switch boja;
    Switch masa;

    SeekBar masa_bar;

    String oblik_char="N";
    String boja_char="N";
    String masa_char="N";

    BluetoothAdapter blue_Adapter;
    BluetoothDevice blue_Device;
    final int REQUEST_ENABLE=0x1;
    final int REQUEST_DISCOVERABLE	= 0x2;

    private static final int REQUEST_DISCOVERY = 0x1;
    private BluetoothSocket socket = null;
    OutputStream outputStream;
    String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        blue_Adapter=BluetoothAdapter.getDefaultAdapter();

        kocka=(Button)findViewById(R.id.btn_kocka);
        piramida=(Button)findViewById(R.id.btn_piramida);
        kugla=(Button)findViewById(R.id.btn_kugla);
        valjak=(Button)findViewById(R.id.btn_valjak);
        crvena=(Button)findViewById(R.id.btn_crvena);
        zelena=(Button)findViewById(R.id.btn_zelena);
        plava=(Button)findViewById(R.id.btn_plava);
        zuta=(Button)findViewById(R.id.btn_zuta);
        sortiraj=(Button)findViewById(R.id.btn_sortiraj);
        bluetooth=(Button)findViewById(R.id.btn_blue);
        zaustavi=(Button)findViewById(R.id.btn_zaustavi);

        oblik=(Switch)findViewById(R.id.switch_oblik);
        boja=(Switch)findViewById(R.id.switch_boja);
        masa=(Switch)findViewById(R.id.switch_masa);

        masa_bar=(SeekBar)findViewById(R.id.seekBar);

        kocka.setEnabled(false);
        piramida.setEnabled(false);
        kugla.setEnabled(false);
        valjak.setEnabled(false);
        crvena.setEnabled(false);
        zelena.setEnabled(false);
        plava.setEnabled(false);
        zuta.setEnabled(false);
        masa_bar.setEnabled(false);

        oblik.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    kocka.setEnabled(true);
                    piramida.setEnabled(true);
                    kugla.setEnabled(true);
                    valjak.setEnabled(true);
                }
                else{
                    kocka.setEnabled(false);
                    piramida.setEnabled(false);
                    kugla.setEnabled(false);
                    valjak.setEnabled(false);
                    ponisti_boje_oblika();
                    oblik_char="N";
                }
            }
        });

        boja.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    crvena.setEnabled(true);
                    zelena.setEnabled(true);
                    plava.setEnabled(true);
                    zuta.setEnabled(true);
                }
                else{
                    crvena.setEnabled(false);
                    zelena.setEnabled(false);
                    plava.setEnabled(false);
                    zuta.setEnabled(false);
                    ponisti_boje_boja();
                    boja_char="N";
                }
            }
        });

        masa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    masa_bar.setEnabled(true);
                }
                else{
                    masa_bar.setEnabled(false);
                    masa_char="N";
                }
            }
        });

        kocka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kocka.setBackgroundTintList(csl_plava);
                piramida.setBackgroundTintList(csl_siva);
                kugla.setBackgroundTintList(csl_siva);
                valjak.setBackgroundTintList(csl_siva);

                oblik_char="C";
            }
        });
        piramida.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kocka.setBackgroundTintList(csl_siva);
                piramida.setBackgroundTintList(csl_plava);
                kugla.setBackgroundTintList(csl_siva);
                valjak.setBackgroundTintList(csl_siva);

                oblik_char="P";
            }
        });
        kugla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kocka.setBackgroundTintList(csl_siva);
                piramida.setBackgroundTintList(csl_siva);
                kugla.setBackgroundTintList(csl_plava);
                valjak.setBackgroundTintList(csl_siva);

                oblik_char="B";
            }
        });
        valjak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                kocka.setBackgroundTintList(csl_siva);
                piramida.setBackgroundTintList(csl_siva);
                kugla.setBackgroundTintList(csl_siva);
                valjak.setBackgroundTintList(csl_plava);

                oblik_char="R";
            }
        });
        crvena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crvena.setBackgroundTintList(csl_crvena);
                zelena.setBackgroundTintList(csl_siva);
                plava.setBackgroundTintList(csl_siva);
                zuta.setBackgroundTintList(csl_siva);

                boja_char="R";
            }
        });
        zelena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crvena.setBackgroundTintList(csl_siva);
                zelena.setBackgroundTintList(csl_zelena);
                plava.setBackgroundTintList(csl_siva);
                zuta.setBackgroundTintList(csl_siva);

                boja_char="G";
            }
        });
        plava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crvena.setBackgroundTintList(csl_siva);
                zelena.setBackgroundTintList(csl_siva);
                plava.setBackgroundTintList(csl_plavaBoja);
                zuta.setBackgroundTintList(csl_siva);

                boja_char="L";
            }
        });
        zuta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                crvena.setBackgroundTintList(csl_siva);
                zelena.setBackgroundTintList(csl_siva);
                plava.setBackgroundTintList(csl_siva);
                zuta.setBackgroundTintList(csl_zuta);

                boja_char="Y";
            }
        });
        sortiraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortiraj.setBackgroundTintList(csl_plava);
                zaustavi.setBackgroundTintList(csl_siva);

                string = "E"+oblik_char+boja_char+masa_char;
                try {
                    outputStream.write(string.getBytes(Charset.forName("UTF-8")));

                }catch (IOException e){
                    Log.e("greske", "vojko VV");
                }
            }
        });
        bluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ukljucivanje_bluetooth();
            }
        });
        zaustavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortiraj.setBackgroundTintList(csl_siva);
                zaustavi.setBackgroundTintList(csl_plava);

                string="Q";
                try {
                    outputStream.write(string.getBytes(Charset.forName("UTF-8")));

                }catch (IOException e){
                    Log.e("greske", "vojko VV");
                }

            }
        });

        masa_bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(seekBar.getProgress()==0){
                    masa_char="1";
                }
                if(seekBar.getProgress()==1){
                    masa_char="2";
                }
                if(seekBar.getProgress()==2){
                    masa_char="3";
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void ukljucivanje_bluetooth(){
        blue_Adapter.enable();
        if (blue_Adapter.isEnabled()) {
            SharedPreferences prefs_btdev = getSharedPreferences("btdev", 0);
            String btdevaddr=prefs_btdev.getString("btdevaddr","?");

            if (btdevaddr == "?")
            {
                BluetoothDevice device = blue_Adapter.getRemoteDevice("B8:27:EB:74:07:64");

                UUID SERIAL_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb"); // bluetooth serial port service
                //UUID SERIAL_UUID = device.getUuids()[0].getUuid(); //if you don't know the UUID of the bluetooth device service, you can get it like this from android cache

                BluetoothSocket socket = null;

                try {
                    socket = device.createRfcommSocketToServiceRecord(SERIAL_UUID);
                } catch (Exception e) {Log.e("greske","Error creating socket");}

                try {
                    socket.connect();
                    Log.e("greske","Connected1");
                } catch (IOException e) {
                    Log.e("greske",e.getMessage());
                    try {
                        Log.e("greske","trying fallback...");

                        socket =(BluetoothSocket) device.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(device,1);
                        socket.connect();

                        try {
                            outputStream = socket.getOutputStream();
                        }catch (IOException e2){
                            Log.e("greske", "vojko VV");
                        }

                        Log.e("greske","Connected2");
                    }
                    catch (Exception e2) {
                        Log.e("greske", "Couldn't establish Bluetooth connection!");
                    }
                }
            }
            else
            {
                Log.e("greske","BT device not selected");
            }
        }
    }
    public void ponisti_boje_oblika(){
        kocka.setBackgroundTintList(csl_siva);
        piramida.setBackgroundTintList(csl_siva);
        kugla.setBackgroundTintList(csl_siva);
        valjak.setBackgroundTintList(csl_siva);
    }
    public void ponisti_boje_boja(){
        crvena.setBackgroundTintList(csl_siva);
        plava.setBackgroundTintList(csl_siva);
        zelena.setBackgroundTintList(csl_siva);
        zuta.setBackgroundTintList(csl_siva);
    }

}
