## Android OneMeasureSDKLite2.0 Documentation

*Lighter weight, easier integration, and highly customizable*

### 1. Setup
 
####1.1 Gradle Configuration

Add the following configurations to your build.gradle in your module：

- Add SDK dependency：

```
dependencies {
    ...
    implementation 'com.tozmart:tozmartSDK-s5:2.3.8'
    ...
}
```
### 2. API Instruction
 
####2.1 SDK Initialization

You must initialize the SDK before calling each interface of the SDK.

**2.1.1 Initialization**

 Add the following code to where you need to initialize the SDK：
 
```
new OneMeasureSDKLite.Builder()
                .withActivity(this)
                .setAppKey("xxx")
                .setAppSecret("xxx")
                .setName("wys")
                .setGender(Gender.MALE)
                .setHeight(180)
                .setWeight(75)
                .setUserId("123456")
                .setLanguage(Language.ENGLISH)
                .setUnit(Unit.METRIC)
                .build();
```

Then you can get all information configured including height and weight by 
```
OneMeasureSDKLite.getInstance().getOneMeasureSDKInfo()
```

**2.1.2 Parameters Instruction**

| Parameters | Type | Required | API | Description |
| ------ | ------ | ------ | ------ | ------ |
| activity | android.app.Activity | Yes | withActivity(activity)|Current activity to get context|
| appKey | String | Yes | setAppKey(appKey)|AppKey assigned to developers by TOZI Cloud|
| appSecret | String | Yes | setAppSecret(appSecret)|AppSecret assigned to developers by TOZI Cloud|
|name|String|Yes|setName(name)|User's name|
|gender|int|Yes|setGender(gender)|User's gender：Gender.MALE、Gender.FEMALE|
|height|float|Yes|setHeight(height)|User's height(cm)|
|weight|float|Yes|setWeight(weight)|User's weight(kg)|
|userId|String| No | setUserId(userId)|User id of the developer's own account system|
|language|int|No|setLanguage(language)|Language used by measurements showing（Be noticed that this parameter will not change the language of your APP）：Language.ENGLISH（Default）、Language.CHINESE（Simple Chinese）、Language.TRADITION_CHINESE（Traditional Chinese）、Language.JAPANESE|
|unit|int|No|setUnit(unit)|Unit used by measurements showing：Unit.METRIC（cm/kg, Default）、IUnit.IMPERIAL(inch/lbs)|

####2.2 CameraView（Optional）

In order to reduce users' development time, the SDK provide CameraView widget whitch integrates the functions of camera taking and device posture detection. If users have their own camera widget, there is no need to use this widget.（Since the accuracy of the measurements is greatly affected by the quality of the picture, we strongly recommend using the CameraView widget. At the same time, we suggest to raise the preview screen of the rear camera by about 1/4 of the device's screen height to obtain the best shooting experience. Please refer to demo for details.）

**2.2.1 Instruction**

Add the following code to your XML layout：

```
<com.tozmart.tozisdk.view.CameraView
    android:id="@+id/camera_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
</com.tozmart.tozisdk.view.CameraView>
```
Then initialze CameraView in the related Activity and add the following code, including register the camera and the sensors：

```
CameraView cameraView;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_front_camera);
    
    cameraView = findViewById(R.id.camera_view);
    ...
}

@Override
public void onStart() {
    super.onStart();
    try {
        cameraView.onStart();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

@Override
public void onResume() {
    super.onResume();
    try {
        cameraView.onResume();
    } catch (Exception e) {
        e.printStackTrace();
    }
    // 注册传感器; register sensors
    cameraView.registerSensor();
}

@Override
public void onPause() {
    try {
        cameraView.onPause();
    } catch (Exception e) {
        e.printStackTrace();
    }
    // 取消传感器; unregister sensors
    cameraView.unregisterSensor();
    super.onPause();
}

@Override
public void onStop() {
    try {
        cameraView.onStop();
    } catch (Exception e) {
        e.printStackTrace();
    }
    super.onStop();
}

@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    //申请camera权限; Apply for camera permission
    cameraView.onRequestPermissionsResult(requestCode, permissions, grantResults);
}
```

**2.2.2 CameraView related interface**

 

 - **captureImage**

Photo taking and callback interface：

```
cameraView.captureImage(new CameraView.ImageCallback() {
    @Override
    public void onImage(CameraView view, byte[] jpeg) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(jpeg, 0, jpeg.length);
        ...
    }
});
```
 - **setFacing**

interface of setting up to use front camera or back camera：
 

```
// CameraView.FACING_FRONT or CameraView.FACING_BACK
cameraView.setFacing(CameraView.FACING_BACK);
```
 - **getFacing**

interface of acquiring the state of the current camera：

```
// return CameraView.FACING_FRONT or CameraView.FACING_BACK
cameraView.getFacing();
```
 - **toggleFacing**

interface of switching front and back cameras. If the current camera is front, switch to back and vice versa：

```
toggle.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        cameraView.toggleFacing();
    }
});
```
 - **openGalleryFromActivity / openGalleryFromFragment**

Open gallery from the current Activity or Fragment and get bitmap from onActivityResult callback：

```
public static final int REQUEST_GALLERY = 9162;
...
cameraView.openGalleryFromActivity(FrontCameraActivity.this, REQUEST_GALLERY);
...
@Override
public void onActivityResult(int requestCode, int resultCode, Intent result) {
    super.onActivityResult(requestCode, resultCode, result);
    if (requestCode == REQUEST_GALLERY && resultCode == RESULT_OK) {
        if (result.getData() != null) {
            try {
                Bitmap imageResult = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getData());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
```
 - **lackRequiredSensors**

Make sure whether the device lacks the necessary sensors for detecting the posture of the device, it is necessary to detect whether the sensors are qualified before acquiring relevant data of the sensors：

```
if (cameraView.lackRequiredSensors()) {
    Toast.makeText(this, "lack sensors", Toast.LENGTH_SHORT).show();
}  else {
    ...
}
```
 - **setOnSensorListener**

Acquire relevant data of the sensor：

```
cameraView.setOnSensorListener(new CameraView.OnSensorListener() {
    @Override
    public void onSensorOk() {
    	// 手机姿态正确，可进行拍照；device pose is ok, you can take piture
        Log.i("sensor:", "ok");
    }

    @Override
    public void onSensorError() {
        // 手机姿态错误，不可进行拍照；device pose is not ok, you can not take piture
        Log.i("sensor:", "error");
    }

    @Override
    public void onSensorAngle(float sensorFB, float sensorLR) {
        // sensorFB 获取设备前后倾斜的角度; get the front-back angle of your device; 设备朝前倾斜是负数度数，向后是正数; front negative, back positive
        // sensorLR 获取设备左右倾斜角度; get the left-right angle of your device; 左负右正；left negative, right positive
        ...
   }
});
```

####2.3 EditOutlineView（Optional）

EditOutlineView is a widget provided by SDK for users to adjust contour lines. Users can use this widget to adjust the generated contour lines again, which is helpful to improve the accuracy of the generated measurements and recommended size.

**2.3.1 Instruction**

Add the following code to your XML layout:

```
<com.tozmart.tozisdk.view.EditOutlineView
    android:id="@+id/photo_drawee_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:placeholderImageScaleType="fitCenter"/>
```
Then initialize EditOutlineView in the related Activity and add the following code：

```
EditOutlineView editOutlineView;

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_edit_outline);
    
    editOutlineView = findViewById(R.id.photo_drawee_view);
    setUpPhotoView();
    ...
}

private void setUpPhotoView() {
    if (flag == Constant.FRONT){
        editOutlineView.initialize(
                flag,
                BitmapHolder.getFrontBitmap(),
                profile2ModelData,
                false
        );
    } else {
        editOutlineView.initialize(
                flag,
                BitmapHolder.getSideBitmap(),
                profile2ModelData,
                true
        );
    }
    ...
}    
```

 - initialize related parameters

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| flag | int | flag = Constant.FRONT or Constant.SIDE，on behalf of the adjustment of front photo or side photo respectively |
| image | Bitmap | the photo corresponding to the outline, the required photo is obtained by the interface 2.5|
| profile2ModelData | Profile2ModelData | The data returned by the interface 2.5 |
| coverFace | boolean | true：cover the face，false：do not cover the face |

**2.3.2 EditOutlineView related interfaces**

 
 - **setOutlineGoodColor**

Define the color of the outline which is good:

| Format | Method  |
| ------ | ------ | 
| XML | app:outlineGoodColor="@color/outline_color" | 
| Java | editOutlineView.setOutlineGoodColor(ContextCompat.getColor(this, R.color.outline_color)) | 

 - **setOutlineBadColor**

Define the color of the outline which needs to be edited:

| Format | Method  |
| ------ | ------ | 
| XML | app:outlineBadColor="@color/outline_errorcolor" | 
| Java | editOutlineView.setOutlineBadColor(ContextCompat.getColor(this, R.color. outline_errorcolor)) | 

 - **setMovePanColor**

Define the color of the circle pan on the outline:

| Format | Method  |
| ------ | ------ | 
| XML | app:movePanColor="@color/colorAccent" | 
| Java | editOutlineView.setMovePanColor(ContextCompat.getColor(this, R.color.colorAccent)) | 

 - **setMoveRectSrc**

Define the drawable of the line handle:

| Format | Method  |
| ------ | ------ | 
| XML | app:moveRectSrc="@drawable/move_rect" | 
| Java | editOutlineView.setMoveRectSrc(ContextCompat.getDrawable(this, R.drawable.move_rect)) | 

 - **setFaceCoverSrc**

Define the drawable resource of the face cover image:

| Format | Method  |
| ------ | ------ | 
| XML | app:faceCoverRes="@drawable/ic_cover_face" | 
| Java | editOutlineView.setFaceCoverSrc(R.drawable.ic_cover_face) | 


 - **undo**

Undo edit operation：

```
editOutlineView.undo();
```

 - **getSavedProfile2ModelData**

Get the adjusted Profile2ModelData which needs to be passed in as a parameter by the interface 2.7&：

```
editOutlineView.getSavedProfile2ModelData();
```

####2.4 Interface of processing photos

 - processImage

**2.4.1 Activity Configuration**

Since the interface needs a long time of network requests, the lifecycle of the related Activity needs to be bound. Simply make the related Activity extend RxAppCompatActivity to automatically bind the lifecycle：

```
public class ProfileActivity extends RxAppCompatActivity {
    ...
}
```
**2.4.2 Interface Instruction**

```
OneMeasureSDKLite.getInstance().processImage(FrontCameraActivity.this, bitmap, PhotoType.FRONT, cameraAngle, taskId, new ProcessCallback() {
        @Override
        public void onResponse(SdkResponse sdkResponse, String taskId, ProcessData processData) {
        	...
        }
    });
```

 - Request parameters

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| activity | com.tozmart.tozisdk.activity.RxAppCompatActivity | Current Activity to bind lifecycle|
| bitmap | Bitmap | photo needs to processed|
| photoType | PhotoType | photo type: PhotoType.FRONT, PhotoType.SIDE|
| cameraAngle | CameraAngle | the angle of your device while taking photo|
| taskId* | String | the id of the current task|
|callback| ProcessCallback |interface callback for getting result|

*Due to the asynchronous task mechanism adopted in the background, calling each interface requires uploading the taskId created at the beginning of the task, and the taskId of a complete task must be the same; Pass null when there is no taskId at the beginning of the task. A complete task: take front photo -> process front photo -> take side photo -> process side photo -> get profile/measurements/recommend sizes

Structure of CameraAngle：

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| XAng | float | the front-back angle of your device, front negative and back positive|
| YAng | float | the left-right angle of your device, left negative and right positive|

 - Return parameters：

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| sdkResponse | SdkResponse | the response of the interface|
| taskId | String | the id of the current task|
| processData | ProcessData | processing data returned by interface|

Structure of SdkResponse：

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| serverStatusCode | String | The code returned by the server request, "0" indicates success|
| serverStatusText | String | message returned by the server request|

Structure of ProcessData：

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| imageProcessFeedback | ImageProcessFeedback | feedback of photo processing|

Structure of ImageProcessFeedback：

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| frontImageErrors | List< ErrorWarn> | error message of front photo|
| frontImageWarns | List< ErrorWarn> | warning message of front photo|
| sideImageErrors | List< ErrorWarn> | error message of side photo|
| sideImageWarns | List< ErrorWarn> | warning message of side photo|

Structure of ErrorWarn:

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| content | String | the content of error or warning |


####2.5 Interface of obtaining body profile by photos

 - getProfile

**2.5.1 Activity Configuration**

Since the interface needs a long time of network requests, the lifecycle of the related Activity needs to be bound. Simply make the related Activity extend RxAppCompatActivity to automatically bind the lifecycle：

```
public class ProfileActivity extends RxAppCompatActivity {
    ...
}
```
**2.5.2 Interface Instruction**

```
OneMeasureSDKLite.getInstance().getProfile(ProfileActivity.this, taskId, new GetProfileCallback() {
        @Override
        public void onResponse(SdkResponse sdkResponse, Profile2ModelData profile2ModelData) {
            ...
        }
    });
```

 - Request parameters

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| activity | com.tozmart.tozisdk.activity.RxAppCompatActivity | Current Activity to bind lifecycle|
| taskId | String | the id of the current task|
|callback| GetProfileCallback |interface callback for getting result|

 - Return parameters：

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| sdkResponse | SdkResponse | the response of the interface|
| profile2ModelData | Profile2ModelData | processing data returned by interface|


Structure of Profile2ModelData：

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| frontFaceRect | RectF | the face rect of front photo|
| sideFaceRect | RectF | the face rect of side photo|
| frontAllPoints | ArrayList< PointF> | the profile points of the front photo|
| sideAllPoints |ArrayList< PointF>|the profile points of the side photo|
| frontProcessedBitmap | Bitmap | The front photo proccessed by server|
| sideProcessedBitmap |Bitmap|The side photo proccessed by server|
| imageProcessFeedback | ImageProcessFeedback(Refer to interface 2.4) | feedback of photo processing|


####2.6 Interface of obtaining body measurements and 3D URL by profile data (Make sure that profile related data is not manually adjusted)

 - getMeasurementsByTask

**2.6.1 Activity Configuration**

Since the interface needs a long time of network requests, the lifecycle of the related Activity needs to be bound. Simply make the related Activity extend RxAppCompatActivity to automatically bind the lifecycle：

```
public class ProfileActivity extends RxAppCompatActivity {
    ...
}
```
**2.6.2 Interface Instruction**

```
OneMeasureSDKLite.getInstance().getMeasurementsByTask(MeasurementsActivity.this, taskId, new GetMeasurementsCallback() {
        @Override
        public void onResponse(SdkResponse sdkResponse, MeasurementsData measurementsData) {
	        ...
	    }
	});
```

 - Request parameters

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| activity | com.tozmart.tozisdk.activity.RxAppCompatActivity | Current Activity to bind lifecycle|
| taskId | String | the id of the current task |
| callback | GetMeasurementsCallback |interface callback for getting result|

 - Return parameters：

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| sdkResponse | SdkResponse | the response of the interface|
| measurementsData | MeasurementsData | processing data returned by interface|
 
Structure of MeasurementsData：

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| measurementEntities | List< MeasurementEntity> | A list of measurements of various parts of the body, please refer to the following table for the structure|
| model3dUrl | String | 3D URL |

Structure of MeasurementEntity:

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| meaValue | float | Measurement of a certain body part|
| imageUrl | String | Thumbnail url of a certain body part|
| sizeName | String |Name url of a certain body part，such as Chest, Waist|
| sizeIntro |String | Instruction of measure methods for a certain body part，such as how is the chest circumference measured|
| unit | String | Unit of "meaValue"，this parameter has three formats, "cm"、"''"(inch) and "°"|


####2.7 Interface of obtaining body measurements and 3D URL by profile data (This interface can be called on the premise that the profile related data has been manually adjusted.)

 - getMeasurementsByProfile

**2.7.1 Activity Configuration**

Since the interface needs a long time of network requests, the lifecycle of the related Activity needs to be bound. Simply make the related Activity extend RxAppCompatActivity to automatically bind the lifecycle：

```
public class ProfileActivity extends RxAppCompatActivity {
    ...
}
```
**2.7.2 Interface Instruction**

```
OneMeasureSDKLite.getInstance().getMeasurementsByProfile(MeasurementsActivity.this, profile2ModelData, new GetMeasurementsCallback() {
        @Override
        public void onResponse(SdkResponse sdkResponse, final MeasurementsData measurementsData) {
	        ...
	    }
	});
```

 - Request parameters

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| activity | com.tozmart.tozisdk.activity.RxAppCompatActivity | Current Activity to bind lifecycle|
| profile2ModelData | Profile2ModelData | the data returned by interface 2.5 and has been manually adjusted |
| callback | GetMeasurementsCallback |interface callback for getting result|

 - Return parameters：

The same as interface 2.6.


####2.8 Interface of obtaining body measurements and 3D URL by photos directly

 - getMeasurements

**2.8.1 Activity Configuration**

Since the interface needs a long time of network requests, the lifecycle of the related Activity needs to be bound. Simply make the related Activity extend RxAppCompatActivity to automatically bind the lifecycle：

```
public class RecomSizeActivity extends RxAppCompatActivity {
    ...
}
```
**2.8.2 Interface Instruction**

```
OneMeasureSDKLite.getInstance().getMeasurements(MeasurementsActivity.this, taskId, new GetMeasurementsCallback() {
        @Override
        public void onResponse(SdkResponse sdkResponse, final MeasurementsData measurementsData) {
					...
			}
	});
```

 - Request parameters: 
The same as interface 2.6.

 - Return parameters: 
The same as interface 2.6.

*Please Note：Although this interface parameters are the same as interface 2.6, after taking two photos, you can directly call this interface to obtain the body measurements without calling interface 2.4 first.*


####2.9 Interface of obtaining recommend size by profile data (Make sure that profile related data is not manually adjusted)

 - getRecomSizeByTask

**2.9.1 Activity Configuration**

Since the interface needs a long time of network requests, the lifecycle of the related Activity needs to be bound. Simply make the related Activity extend RxAppCompatActivity to automatically bind the lifecycle：

```
public class RecomSizeActivity extends RxAppCompatActivity {
    ...
}
```
**2.9.2 Interface Instruction**

```
OneMeasureSDKLite.getInstance().getRecomSizeByTask(RecomSizeActivity.this, taskId, new GetRecommendSizeCallback() {
        @Override
        public void onResponse(SdkResponse sdkResponse, RecommendSizeData recommendSizeData) {
					...
			}
	});
```

 - Request parameters: 

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| activity | com.tozmart.tozisdk.activity.RxAppCompatActivity | Current Activity to bind lifecycle|
| taskId | String | the id of the current task |
| callback | GetRecommendSizeCallback |interface callback for getting result|


 - Return parameters：

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| sdkResponse | SdkResponse | the response of the interface|
| recommendSizeData | RecommendSizeData | processing data returned by interface|

Structure of RecommendSizeData：

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| recommendSizeList | List< RecommendSize> | A list of recommend size, please refer to the following table for the structure|

Structure of RecommendSize：

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| hasFitSize | boolean | if has recommend size or not|
| type | String | Recommend size type|
| fitSize | String | hasFitSize = true -> Recommend size；hasFitSize = false -> No recommend size|


####2.10 Interface of obtaining recommend size by profile data (This interface can be called on the premise that the profile related data has been manually adjusted.)

 - getRecomSizeByProfile

**2.10.1 Activity Configuration**

Since the interface needs a long time of network requests, the lifecycle of the related Activity needs to be bound. Simply make the related Activity extend RxAppCompatActivity to automatically bind the lifecycle：

```
public class RecomSizeActivity extends RxAppCompatActivity {
    ...
}
```
**2.10.2 Interface Instruction**

```
OneMeasureSDKLite.getInstance().getRecomSizeByProfile(RecomSizeActivity.this, profile2ModelData, new GetRecommendSizeCallback() {
            @Override
            public void onResponse(SdkResponse sdkResponse, RecommendSizeData recommendSizeData) {
					...
			}
	});
```

 - Request parameters: 

| Parameters | Type  | Description |
| ------ | ------ | ------ |
| activity | com.tozmart.tozisdk.activity.RxAppCompatActivity | Current Activity to bind lifecycle|
| profile2ModelData | Profile2ModelData | the data returned by interface 2.5 and has been manually adjusted |
| callback | GetRecommendSizeCallback |interface callback for getting result|


 - Return parameters：
The same as interface 2.9.

####2.11 Interface of obtaining recommend size by photos directly

 - getRecommendSize

**2.11.1 Activity Configuration**

Since the interface needs a long time of network requests, the lifecycle of the related Activity needs to be bound. Simply make the related Activity extend RxAppCompatActivity to automatically bind the lifecycle：

```
public class RecomSizeActivity extends RxAppCompatActivity {
    ...
}
```
**2.11.2 Interface Instruction**

```
OneMeasureSDKLite.getInstance().getRecommendSize(RecomSizeActivity.this, taskId, new GetRecommendSizeCallback() {
            @Override
            public void onResponse(SdkResponse sdkResponse, RecommendSizeData recommendSizeData) {
					...
			}
	});
```

 - Request parameters: 
The same as interface 2.9.


 - Return parameters：
The same as interface 2.9.

*Please Note：Although this interface parameters are the same as interface 2.9, after taking two photos, you can directly call this interface to obtain recommend size without calling interface 2.4 first.*


### 3. Proguard Rules

Please add the following rules to your Proguard file:

```
-keep class com.tozmart.tozisdk.** { *; }
```

### 4. Human Posture Tracking and Detection

The new SDK also expands the functions of human posture tracking and detection, and enriches the use scenarios of the SDK.

####4.1 Integration Method

Since the human posture tracking and detection function will greatly increase the package size of the SDK, we package it separately into another SDK. If developers do not need this function, they can integrate the simplified SDK according to the above document.

Only three modifications are needed to integrate this function. Other methods and interfaces are the same as the simplified SDK:

 1. Remove the original SDK dependency and replace it with the following:
 
 ```
dependencies {
		...
		implementation 'com.tozmart:tozmartSDK-s4:2.3.8'
		...
}
```

2. Replace CameraView with CameraViewWithPose and add the following interfaces:

 - **startPoseTrack**

  Open human posture tracking.

  Please note:

  - Using this interface requires adding the following configuration to gradle:

  ```
android {
		...
		aaptOptions {
			noCompress "tflite"
		}
		...
}
```

  - This interface must be called before cameraView.onStart().
 
  ```
cameraView.startPoseTrack();
```

  - **setOnHumanPoseListener**

  Listener to the posture of human is correct or not (only check whether the front posture is correct or not).

  Please note:

  - Using this interface requires calling the startPoseTrack() interface first, otherwise an exception will be thrown:

  ```
cameraView.setOnHumanPoseListener(new OnHumanPoseListener() {
		@Override
		public void onPoseOk() {
			Log.i("pose: ", "pose ok");
		}
		
		@Override
		public void onPoseError() {
			Log.i("pose: ", "pose error");
		}
});
```

3. Add proguard rules

 Please add the following rules to your Proguard file:

 ```
-keep class com.tozmart.tozisdk_withpose.** { *; }
-keep class com.tozmart.tozisdk.** { *; }
```
