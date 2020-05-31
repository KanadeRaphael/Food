package com.example.food.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.Surface;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.food.R;
import com.example.food.activity.identify.MaterialResultActivity;
import com.example.food.activity.identify.MenuResultActivity;
import com.example.food.bean.ClassifyResult;
import com.example.food.config.CameraPreview;
import com.example.food.utils.ConstantUtils;
import com.example.food.utils.MessageUtils;
import com.example.food.utils.WebUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ClassifierCamera extends AppCompatActivity {

    public static int MATERAIL_CODE = 0;
    public static int DISH_CODE = 1;

    @BindView(R.id.camera_preview)
    FrameLayout cameraLayout;
    @BindView(R.id.results_text_view)
    TextView resultsTextView;
    @BindView(R.id.take_photo_capture)
    ImageView takePhotoCapture;
    @BindView(R.id.take_photo_ok)
    ImageView takePhotoOk;
    @BindView(R.id.camera_cover_linearlayout)
    FrameLayout cameraCoverLinearlayout;

    private Camera camera;
    private CameraPreview preview;
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_BACK;

    private ArrayList<ClassifyResult> resultList = new ArrayList<>();

    private int code = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        code = intent.getIntExtra("CODE", -1);
        //取消toolbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_camera);
        ButterKnife.bind(this);

        if (!checkCameraHardware(this)) {
            MessageUtils.MakeToast("不支持相机");
        } else {
            openCamera();
        }

        setCameraDisplayOrientation(this, cameraId, camera);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

    /**
     * 检查当前设备是否有相机
     *
     * @param context
     * @return
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 打开相机
     */
    private void openCamera() {
        if (null == camera) {
            camera = getCameraInstance();
            preview = new CameraPreview(this, camera);
            cameraLayout.addView(preview);
            camera.startPreview();
        }
    }

    /**
     * 获取相机
     *
     * @return 相机对象
     */
    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();

            Camera.Parameters mParameters = c.getParameters();
            List<Camera.Size> sizes = mParameters.getSupportedPreviewSizes();

            mParameters.setPictureSize(2048, 1536);
            mParameters.setPreviewSize(2048, 1536);
            c.setParameters(mParameters);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return c;
    }

    /**
     * 对焦回调,对焦完成后进行拍照
     */
    private Camera.AutoFocusCallback mAutoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {
            if (success) {
                camera.takePicture(null, null, mPictureCallback);
            }
        }
    };

    /**
     * 拍照回调
     */
    private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
        @RequiresApi(api = Build.VERSION_CODES.FROYO)
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            bitmap = rotateBitmapByDegree(bitmap, 90);
            //缩放
            bitmap = Bitmap.createScaledBitmap(bitmap, 720, 1280, false);
            try {
                final File pictureDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                final String pictureName = System.currentTimeMillis() + ".jpg";
                final String picturePath = pictureDir + File.separator + pictureName;
                File file = new File(picturePath);
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                bos.flush();
                bos.close();

                String imgStr = Base64.encodeToString(data, Base64.DEFAULT);
                String imgParam = URLEncoder.encode(imgStr, "UTF-8");
                final String param = "image=" + imgParam + "&top_num=" + 1;
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String result = null;
                        try {
                            if (code == MATERAIL_CODE) {
                                result = WebUtil.HttpPost(ConstantUtils.BD_MATERIAL_URL,
                                        ConstantUtils.BD_ACCESS_TOKEN, param);
                                Logger.d(result);
                                JSONObject jsonObject = new JSONObject(result);
                                JSONArray resultObject = jsonObject.getJSONArray("result");
                                jsonObject = resultObject.getJSONObject(0);
                                ClassifyResult classifyResult = new ClassifyResult(ClassifyResult.MATERIAL);
                                classifyResult.setName(jsonObject.getString("name"));
                                resultList.add(classifyResult);
                                refreshUI();
                            } else if (code == DISH_CODE) {
                                result = WebUtil.HttpPost(ConstantUtils.BD_DISH_URL,
                                        ConstantUtils.BD_ACCESS_TOKEN, param);
                                JSONObject jsonObject = new JSONObject(result);
                                ClassifyResult classifyResult = new ClassifyResult(ClassifyResult.DISH);
                                JSONArray resultObject = jsonObject.getJSONArray("result");
                                jsonObject = resultObject.getJSONObject(0);
                                classifyResult.setCalorie(jsonObject.getInt("calorie"));
                                Logger.d(jsonObject.getInt("calorie"));
                                classifyResult.setHas_calorie(jsonObject.getBoolean("has_calorie"));
                                classifyResult.setProbability(jsonObject.getDouble("probability"));
                                classifyResult.setName(jsonObject.getString("name"));
                                classifyResult.getMenu();
                                classifyResult.setImgPath(picturePath);
                                resultList.add(classifyResult);
                                refreshUI();
                            } else {
                                Logger.e("拍照code为-1");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                thread.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
//            MessageUtils.MakeToast("拍照！");
            camera.startPreview();
        }
    };


    /**
     * 两个按钮的事件
     *
     * @param view
     */
    @OnClick({R.id.take_photo_capture, R.id.take_photo_ok, R.id.results_text_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.take_photo_capture:
                camera.autoFocus(mAutoFocusCallback);
                cameraCoverLinearlayout.setVisibility(View.VISIBLE);
                break;
            case R.id.take_photo_ok:
                final Intent intent;
                if (code == DISH_CODE) {
                    intent = new Intent(ClassifierCamera.this, MenuResultActivity.class);
                    intent.putExtra("LIST", resultList);
                    startActivity(intent);

                } else {
                    intent = new Intent(ClassifierCamera.this, MaterialResultActivity.class);
                    //把拍照结果的食材名字放到新的List：materials里面
                    ArrayList<String> materials = new ArrayList<>();
                    for (ClassifyResult classifyResult : resultList) {
                        materials.add(classifyResult.getName());
                    }
                    intent.putExtra("LIST", materials);
                    startActivity(intent);
                }
                resultList.clear();
                refreshUI();
                finish();
                break;
            case R.id.results_text_view:
                resultList.remove(resultList.size() - 1);
                refreshUI();
                break;
        }
    }

    //设置相机成竖屏
    public static void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {

        int degrees = 0;

        //可以获得摄像头信息
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        //获取屏幕旋转方向
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }


    private void refreshUI() {
        resultsTextView.post(new Runnable() {
            @Override
            public void run() {
                String text = "";
                for (int i = 0; i < resultList.size(); i++) {
                    text += resultList.get(i).getName() + " ";
                }
                resultsTextView.setText(text);
                cameraCoverLinearlayout.setVisibility(View.INVISIBLE);
            }
        });
    }

    //修改图片保存方向
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        //Matrix图片动作（旋转平移）
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);

        try {
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {

        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }
}
