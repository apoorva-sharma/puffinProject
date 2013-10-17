package com.appiusmaximus.puffin;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;

public class Controller extends Activity implements SensorEventListener{
    
    // CONSTANTS
    String COMPUTER_IP = "134.173.219.157";
    String COMPUTER_PORT =  "4238";

    TextView statusTextLabel;
    CenterButton centerButton;

    private static final String TAG = "de.tavendo.puffin";

    private final WebSocketConnection mConnection = new WebSocketConnection();

    private SensorManager mSensorManager;
    private Sensor mRV;

    private float[] mRotationVector = new float[3];
    private float[] currQuat = new float[4];

    private float[] startRotationMatrix = new float[16]; // This is the start position
    private float[] mRotationMatrix = new float[16]; // This is the updated position
    private float[] mOrientation = new float[3];

    private boolean calibrated = false;

    private View.OnTouchListener centerButtonListener;
    private View.OnClickListener reconnector;

    private void startWebSocket(String ip, String port) {
        final String wsuri = "ws://" + ip +":" + port +"/";

        try {
            mConnection.connect(wsuri, new WebSocketHandler() {

                @Override
                public void onOpen() {
                    Log.d(TAG, "Status: Connected to " + wsuri);

                    statusTextLabel.setText("connected");

                    mConnection.sendTextMessage("Hello, world!");
                }

                @Override
                public void onTextMessage(String payload) {
                    Log.d(TAG, "Got echo: " + payload);
                }

                @Override
                public void onClose(int code, String reason) {
                    Log.d(TAG, "Connection lost.");

                    statusTextLabel.setText("touch to connect");
                }
            });
        } catch (WebSocketException e) {

            Log.d(TAG, e.toString());

            statusTextLabel.setText("error");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextLabel = (TextView) findViewById(R.id.statusLabel);
        centerButton = (CenterButton) findViewById(R.id.center);
        startWebSocket(COMPUTER_IP, COMPUTER_PORT);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mRV = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        centerButtonListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    startTracking();
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    stopTracking();
                }
                return false;
            }
        };

        centerButton.setOnTouchListener(centerButtonListener);

        reconnector = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startWebSocket(COMPUTER_IP, COMPUTER_PORT);
            }
        };

        statusTextLabel.setOnClickListener(reconnector);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.controller, menu);
        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        mRotationVector = event.values.clone();

        SensorManager.getQuaternionFromVector(currQuat,mRotationVector);

        SensorManager.getRotationMatrixFromVector(mRotationMatrix,currQuat);

        if (!calibrated) {
            calibrate();
        }

        SensorManager.getAngleChange(mOrientation,mRotationMatrix,startRotationMatrix);

        if (mConnection.isConnected()) {
            mConnection.sendTextMessage(stringify(mOrientation));
        }
    }

    protected static String stringify(float[] arr) {
        String result = "";
        for (float a : arr) {
            result = result.concat(String.valueOf(a));
            result = result.concat("\t");
        }
        return result;
    }

    protected void calibrate() {
        startRotationMatrix = mRotationMatrix.clone();
        // Reset the position
        calibrated = true;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        // do nothing
    }

    private void startTracking() {
        mSensorManager.registerListener(this,mRV,SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void stopTracking() {
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopTracking();
    }
}
