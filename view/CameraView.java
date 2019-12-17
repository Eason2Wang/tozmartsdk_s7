package com.tozmart.tozisdk.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.Fragment;
import android.util.AttributeSet;

import android.view.Gravity;
import android.widget.TextView;
import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;
import com.tozmart.tozisdk.R;
import com.tozmart.tozisdk.api.ICameraView;
import com.tozmart.tozisdk.utils.PickPhotoFromGallery;

import static android.content.Context.SENSOR_SERVICE;

/**
 * created by tozi
 */
public class CameraView extends CameraKitView implements ICameraView {

    public static final int FACING_BACK = CameraKit.FACING_BACK;
    public static final int FACING_FRONT = CameraKit.FACING_FRONT;
    protected int mFacing;

    private final int ANGLE_BOUND = 5;
    private boolean isDevicePoseOk = true;
    private Context mContext;

    public static int SENSOR_FIX_COUNT = 10000;//传感器固定频率100Hz
    public static float SENSOR_FIX_ALPHA = 0.97f;
    private SensorManager mSensorManager;
    private float[] sensorValuesTemp = new float[3];
    //记录取值次数
    public static long start_time;
    private float X, Y;
    private Sensor aSensor;
    private Sensor mSensor;
    private float[] accelerometerValues = new float[3];
    private float[] magneticFieldValues = new float[3];
    final SensorEventListener myListener = new SensorEventListener() {
        public void onSensorChanged(SensorEvent sensorEvent) {
            synchronized (this) {
                //检测沿三个物理轴(x,y,z)的磁场强度μT,创建指南针
                if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
                    magneticFieldValues = sensorEvent.values.clone();
                //测量设备在三个物理轴(x,y,z)上的加速度（m/s²），包括重力,运动检测（摇动，倾斜等）
                if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
                    accelerometerValues = sensorEvent.values.clone();
                calculateOrientation();
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    public CameraView(Context context) {
        super(context);
        mContext = context;
        init(null, 0);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(attrs, 0);
    }

    public CameraView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        try {
            initSensor();
//            setPermissions(PERMISSION_CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addLogoView();
    }

    /**
     * 添加logo标志
     */
    private void addLogoView() {
        TextView logoText = new TextView(mContext);
        logoText.setText(getResources().getString(R.string.public_logo_text));
        logoText.setTextColor(Color.WHITE);
        logoText.setGravity(Gravity.RIGHT | Gravity.BOTTOM);
        logoText.setPadding(0, 0, 60, 60);
        logoText.setTextSize(10);
        addView(logoText);
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
    }

    /**
     *
     */
    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    /**
     * @param callback
     */
    public void captureImage(final ImageCallback callback) {
        super.captureImage(new CameraKitView.ImageCallback() {
            @Override
            public void onImage(CameraKitView view, byte[] jpeg) {
                callback.onImage(CameraView.this, jpeg);
            }
        });
    }

    /**
     * set camera facing
     *
     * @param facing CameraKit.FACING_FRONT or CameraKit.FACING_BACK
     */
    public void setFacing(int facing) {
        mFacing = facing;
        super.setFacing(facing);
    }

    public int getFacing() {
        return mFacing;
    }

    /**
     * switch camera facing
     */
    public void toggleFacing() {
        if (getFacing() == CameraKit.FACING_BACK) {
            setFacing(CameraKit.FACING_FRONT);
        } else {
            setFacing(CameraKit.FACING_BACK);
        }
    }

    /**
     * open default gallery
     *
     * @param activity
     * @param requestCode
     */
    public void openGalleryFromActivity(Activity activity, int requestCode) {
        PickPhotoFromGallery.pickImage(activity, requestCode);
    }

    /**
     * open default gallery
     *
     * @param fragment
     * @param requestCode
     */
    public void openGalleryFromFragment(Fragment fragment, int requestCode) {
        PickPhotoFromGallery.pickImage(fragment, requestCode);
    }

    /**
     * initialize mobile pose sensors
     */
    public void initSensor() throws Exception {
        mSensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        aSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (aSensor == null || mSensor == null) {
            throw new Exception(getContext().getString(R.string.lack_sensors));
        }
    }

    /**
     * if lack required sensors or not
     *
     * @return
     */
    public boolean lackRequiredSensors() {
        return aSensor == null || mSensor == null;
    }

    /**
     * register sensors
     */
    public void registerSensor() {
        mSensorManager.registerListener(myListener, aSensor, SENSOR_FIX_COUNT);
        mSensorManager.registerListener(myListener, mSensor, SENSOR_FIX_COUNT);
    }

    /**
     * unregister sensors
     */
    public void unregisterSensor() {
        if (mSensorManager != null)
            mSensorManager.unregisterListener(myListener);
    }

    /**
     * 计算传感器角度
     */
    private void calculateOrientation() {
        float[] values = new float[3];
        float[] temp = new float[9];
        float[] Rr = new float[9];
        SensorManager.getRotationMatrix(temp, null, accelerometerValues, magneticFieldValues);
        //Remap to cameraMode's point-of-view
        //坐标转换位API用于getRotationMatrix()和getOrientation()之间
        SensorManager.remapCoordinateSystem(temp,
                SensorManager.AXIS_X,
                SensorManager.AXIS_Z, Rr);
        SensorManager.getOrientation(Rr, values);

        values[0] = (float) Math.toDegrees(values[0]);
        values[1] = (float) Math.toDegrees(values[1]);
        values[2] = (float) Math.toDegrees(values[2]);

        //传感器数据去噪方法，一阶差分
        sensorValuesTemp[1] = SENSOR_FIX_ALPHA * sensorValuesTemp[1] + (1 - SENSOR_FIX_ALPHA) * values[1];
        sensorValuesTemp[2] = SENSOR_FIX_ALPHA * sensorValuesTemp[2] + (1 - SENSOR_FIX_ALPHA) * values[2];
        X = sensorValuesTemp[1];
        Y = sensorValuesTemp[2];
        if (sensorListener != null) {
            sensorListener.onSensorAngle(X, Y);
        }

        double dis = Math.sqrt(Math.pow(X, 2) + Math.pow(Y, 2));
        if (sensorListener != null) {
            if (dis < ANGLE_BOUND) {
                if (!isDevicePoseOk) {
                    sensorListener.onSensorOk();
                }
                isDevicePoseOk = true;
            } else {
                if (isDevicePoseOk) {
                    sensorListener.onSensorError();
                }
                isDevicePoseOk = false;
            }
        }
    }

    private OnSensorListener sensorListener;

    public void setOnSensorListener(OnSensorListener listener) {
        this.sensorListener = listener;
    }

    public interface OnSensorListener {

        // 手机姿态正确
        void onSensorOk();

        // 手机姿态错误
        void onSensorError();

        /**
         * @param sensorFB 获取手机前后倾斜的角度; get the front-back angle of your device; 手机朝前倾斜是负数度数，向后是正数; front negative, back positive
         * @param sensorLR 获取手机左右倾斜角度; get the left-right angle of your device; 左负右正；left negative, right positive
         */
        void onSensorAngle(float sensorFB, float sensorLR);
    }

    /**
     * image call back
     */
    public interface ImageCallback {

        /**
         * @param view
         * @param jpeg
         */
        void onImage(CameraView view, byte[] jpeg);

    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
