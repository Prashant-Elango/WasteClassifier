package studio.actinic.games.demo;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.engine.OpenCVEngineInterface;
import org.opencv.imgproc.Imgproc;
import org.opencv.osgi.OpenCVNativeLoader;

public class MainActivity extends AppCompatActivity  {
    private static final  String TAG = "__MainActivity__";
    private Mat FixOrientation;
    private Mat ___temp____;
    DetectorClass detectorClass = new DetectorClass();
    JavaCameraView javaCameraView;
    static {
        OpenCVLoader.initDebug();
    }
    JavaCameraView.CvCameraViewListener2 cvCameraViewListener2 = new JavaCameraView.CvCameraViewListener2()
    {

        @Override
        public void onCameraViewStarted(int width, int height) {
            FixOrientation = new Mat(width,height, CvType.CV_8UC4);
        }

        @Override
        public void onCameraViewStopped() {

        }
        Mat downScale,downScale_grey;
        @Override
        public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
            //Core.flip(FixOrientation,FixOrientation,-2);
            try {
                downScale = inputFrame.rgba();
                downScale_grey = inputFrame.gray();
                detectorClass.queue.offer(downScale);
                detectorClass.queue_grey.offer(downScale_grey);
                detectorClass.FaceClassify();
                ___temp____ = detectorClass.outQueue.take();
                detectorClass.outQueue.remove(___temp____);
            }
            catch (/*Interrupted*/ Exception e)
            {
                e.printStackTrace();
            }
            return ___temp____;
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread beast = new Thread(detectorClass);
        beast.setDaemon(true);
        beast.start();
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        javaCameraView = findViewById(R.id.surfaceView);
        javaCameraView.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_BACK);
        javaCameraView.setCvCameraViewListener(cvCameraViewListener2);
        javaCameraView.setVisibility(View.VISIBLE);
        javaCameraView.enableFpsMeter();
        javaCameraView.enableView();
        Log.d(TAG,"JavaCamera View ===> "+"Crreated");


    }

}
