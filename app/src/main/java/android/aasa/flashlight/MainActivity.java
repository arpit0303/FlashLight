package android.aasa.flashlight;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ToggleButton;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class MainActivity extends ActionBarActivity {

    Camera camera;

    Camera.Parameters parameters;
    ToggleButton mFlashSwitch;
    WindowManager.LayoutParams localLayoutParams;
    Boolean isFlashLightOn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFlashSwitch = (ToggleButton) findViewById(R.id.toggleButton);

        localLayoutParams = getWindow().getAttributes();

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Boolean checkFlash = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (checkFlash) {
            cameraController();

            mFlashSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cameraController();
                }
            });
        }
    }

    private void cameraController() {
        if (mFlashSwitch.isChecked()) {
            camera = Camera.open();
            parameters = camera.getParameters();
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
            camera.startPreview();

            mFlashSwitch.setChecked(false);
        } else {
            camera.stopPreview();

            mFlashSwitch.setChecked(true);
        }
    }

    private void releaseCameraAndPreview() {
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }
}
