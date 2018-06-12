package com.example.michaelkibenko.ballaba.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringImageComparionActivity;
import com.example.michaelkibenko.ballaba.Activities.Scoring.ScoringSelfieActivity;
import com.example.michaelkibenko.ballaba.Entities.BallabaBaseEntity;
import com.example.michaelkibenko.ballaba.Managers.BallabaResponseListener;
import com.example.michaelkibenko.ballaba.Managers.ConnectionsManager;
import com.example.michaelkibenko.ballaba.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CameraFragment extends android.app.Fragment {

    public @interface CAMERA_TYPES{
        int BACK = 1;
        int FRONT = 2;
    }
    private String TAG = CameraFragment.class.getSimpleName();
    private TextureView textureView;
    private Context context;
    private static final SparseIntArray ORIENTATION = new SparseIntArray();
    private @CAMERA_TYPES int currentType = CAMERA_TYPES.BACK;
    private RelativeLayout cameraLayout;

    static {
        ORIENTATION.append(Surface.ROTATION_0, 90);
        ORIENTATION.append(Surface.ROTATION_90, 0);
        ORIENTATION.append(Surface.ROTATION_180, 180);
        ORIENTATION.append(Surface.ROTATION_270, 270);
    }

    private String cameraID;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSessions;
    private CaptureRequest.Builder captureRequestBuilder;
    private Size imageDimension;
    private ImageReader reader;

//    private File file;
    public static final int REQUEST_CAMERA_PERMISSION = 200;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;
    private ProgressBar progressBar;
    private byte[] byteArray;


    public TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            if(cameraDevice != null){
                cameraDevice.close();
            }
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            if(cameraDevice != null) {
                cameraDevice.close();
                cameraDevice = null;
            }
        }
    };

    public int getCurrentType() {
        return currentType;
    }

    public void setCurrentType(@CAMERA_TYPES int currentType) {
        this.currentType = currentType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.scoring_camera_fragment, container, false);
        textureView = v.findViewById(R.id.texture_view);
        assert textureView != null;
        textureViewTransform();
        progressBar = v.findViewById(R.id.fragment_camera_progress_bar);
        startBackgroundThread();
        textureView.setSurfaceTextureListener(textureListener);
        cameraLayout = (RelativeLayout) v.findViewById(R.id.scoring_camera_fragment_layout);
        cameraLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraLayout.setClickable(false);
                takePicture();
            }
        });
        return v;
    }

    private void textureViewTransform() {
        if (textureView == null) {
            return;
        }
//        if(currentType == CAMERA_TYPES.BACK) {
//            textureView.setRotation(270);
//        }
    }

    private void openCamera() {
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        Log.e(TAG, "is camera open");
        try {
            try {
                String[] cameras = manager.getCameraIdList();
                if (currentType == CAMERA_TYPES.BACK) {
                    cameraID = cameras[0];
                } else if (currentType == CAMERA_TYPES.FRONT) {
                    cameraID = cameras[1];
                }
            }catch (NullPointerException ex){
                ex.printStackTrace();
                //TODO set empty state
            }
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraID);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            assert map != null;
            imageDimension = map.getOutputSizes(SurfaceTexture.class)[0];
            // Add permission for camera and let user grant the permission
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraID, stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "openCamera X");
    }

    private void takePicture() {
        if (cameraDevice == null) {
            return;
        }
        CameraManager manager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        try {
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraDevice.getId());
            Size[] jpegSizes;
            if (characteristics != null) {
                jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP).getOutputSizes(ImageFormat.JPEG);
                int width = 640;
                int height = 480;
//                if (jpegSizes != null && jpegSizes.length > 0) {
//                    width = jpegSizes[0].getWidth();
//                    height = jpegSizes[0].getHeight();
//                }

                reader = ImageReader.newInstance(width, height, ImageFormat.JPEG, 1);
                List<Surface> outputSurface = new ArrayList<>();
                outputSurface.add(reader.getSurface());
                outputSurface.add(new Surface(textureView.getSurfaceTexture()));

                final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                captureBuilder.addTarget(reader.getSurface());
                captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_AF_MODE_AUTO);
//                captureBuilder.set(CaptureRequest.JPEG_QUALITY, (byte)100);

                // orientation
                int rotation = getActivity().getWindowManager().getDefaultDisplay().getRotation();
                captureBuilder.set(CaptureRequest.COLOR_CORRECTION_ABERRATION_MODE, ORIENTATION.get(rotation));

//                file = new File(Environment.getExternalStorageDirectory() + "/pic.jpg");

                ImageReader.OnImageAvailableListener readerListener = new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {
                        Log.d(TAG, "onImageAvailable: ");
                        Image image = null;
                        try {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.VISIBLE);
                                }
                            });
                            image = reader.acquireLatestImage();
                            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                            byte[] bytes = new byte[buffer.capacity()];
                            byteArray = bytes;
                            buffer.get(bytes);
//                            save(bytes);

                            String encoded = Base64.encodeToString(bytes, Base64.DEFAULT);
                            JSONObject object = new JSONObject();
                            try {
                                object.put("image", encoded);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (currentType == CAMERA_TYPES.BACK){
                                sendScoringID(object);
                            }else {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                                Intent intent = new Intent(context , ScoringImageComparionActivity.class);
                                //intent.putExtra("IMAGE" , encoded);
                                startActivity(intent);
                            }
                        }  finally {
                            if (image != null) {
                                image.close();
                            }
                        }
                    }

//                    private void save(byte[] bytes) throws IOException {
//                        OutputStream outputStream = null;
//                        try {
//                            outputStream = new FileOutputStream(file);
//                            outputStream.write(bytes);
//                        } finally {
//                            if (outputStream != null) {
//                                outputStream.close();
//                            }
//                        }
//                    }
                };

                reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
                final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                    @Override
                    public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                        super.onCaptureCompleted(session, request, result);
                    }
                };
                cameraDevice.createCaptureSession(outputSurface, new CameraCaptureSession.StateCallback() {
                    @Override
                    public void onConfigured(@NonNull CameraCaptureSession session) {

                        try {
                            session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);

                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                    }
                }, mBackgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void sendScoringID(JSONObject object) {
        ConnectionsManager.getInstance(getActivity()).uploadScoringID(object, currentType == CAMERA_TYPES.BACK, new BallabaResponseListener() {
            @Override
            public void resolve(BallabaBaseEntity entity) {
                cameraLayout.setClickable(true);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
                Intent intent = new Intent(getActivity(), ScoringSelfieActivity.class);
                //intent.putExtra("USER_IMAGE", byteArray);
                startActivity(intent);
            }

            @Override
            public void reject(BallabaBaseEntity entity) {
                cameraLayout.setClickable(true);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
                openCamera();
                Toast.makeText(getActivity(), "We couldn't manage to detect an id in this picture..\nplease try again", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(imageDimension.getWidth(), imageDimension.getHeight());
            Surface surface = new Surface(texture);
            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);
            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (cameraDevice == null) return;
                    cameraCaptureSessions = session;
                    updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(getActivity(), "Changed", Toast.LENGTH_SHORT).show();
                }
            }, null);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void updatePreview() {
        if (cameraDevice == null) {
            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
        }
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_AF_MODE_AUTO);
//        captureRequestBuilder.set(CaptureRequest.JPEG_QUALITY, (byte) 100);

        try {
            cameraCaptureSessions.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    public void stopBackgourndThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        context = activity;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
