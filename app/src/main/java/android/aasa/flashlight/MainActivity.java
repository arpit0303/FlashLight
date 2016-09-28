package android.aasa.flashlight;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ToggleButton;

import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {

    Camera camera;

    Camera.Parameters parameters;
    ToggleButton mFlashSwitch;
    Boolean checkFlash;
    static int click = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StartAppSDK.init(this, "203511945", false);
        StartAppAd.disableSplash();

        mFlashSwitch = (ToggleButton) findViewById(R.id.toggleButton);

        checkFlash = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        mFlashSwitch.setOnClickListener(this);
    }

    public void releaseCameraAndPreview() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (camera == null) {
            try {
                camera = Camera.open();
                parameters = camera.getParameters();
            } catch (RuntimeException e) {
                Log.e("Error: ", e.getMessage());
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseCameraAndPreview();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toggleButton:
                if (checkFlash) {
                    click++;
                    if (click % 2 == 0) {
                        if (camera != null || parameters != null) {
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            camera.setParameters(parameters);
                            camera.startPreview();
                        }

                        mFlashSwitch.setChecked(false);
                    } else {
                        if (camera != null || parameters != null) {
                            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            camera.setParameters(parameters);
                            camera.stopPreview();
                        }

                        mFlashSwitch.setChecked(true);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        StartAppAd.onBackPressed(this);
        super.onBackPressed();
    }
}
