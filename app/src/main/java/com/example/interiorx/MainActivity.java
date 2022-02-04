package com.example.interiorx;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.MediaRouteButton;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.transition.ArcMotion;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.ar.core.Anchor;
import com.google.ar.core.ArCoreApk;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    private MediaRouteButton mArButton;
    private ArFragment arCam;
    private int clickNo = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);
        if (isFirstRun) {
            startActivity(new Intent(MainActivity.this, Boarding.class));
            finish();
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        setContentView(R.layout.activity_main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setBackground(null);

//        maybeEnableArButton();
        if (checkSystemSupport(this)) {
            arCam = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.arCameraArea);
            arCam.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {

                clickNo++;

                // the 3d model comes to the scene only the first time we tap the screen
                if (clickNo == 1) {

                    Anchor anchor = hitResult.createAnchor();
                    ModelRenderable.builder()
                            .setSource(this,R.raw.chair12)
                            .setIsFilamentGltf(true)
                            .build()
                            .thenAccept(modelRenderable -> addModel(anchor, modelRenderable))
                            .exceptionally(throwable -> {
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setMessage("Something is not right" + throwable.getMessage()).show();
                                return null;
                            });
                }
            });
        } else {
            return;
        }
    }




//    void maybeEnableArButton(){
//        ArCoreApk.Availability availability= ArCoreApk.getInstance().checkAvailability(this);
//        if(availability.isTransient()){
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    maybeEnableArButton();
//
//                }
//            },200);
//        }
//
//        if(availability.isSupported()){
//            mArButton.setVisibility(View.VISIBLE);
//            mArButton.setEnabled(true);
//        }else{
//            mArButton.setVisibility(View.INVISIBLE);
//            mArButton.setEnabled(false);
//        }
//    }
public static boolean checkSystemSupport(Activity activity) {

    // checking whether the API version of the running Android >= 24
    // that means Android Nougat 7.0
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

        String openGlVersion = ((ActivityManager) Objects.requireNonNull(activity.getSystemService(Context.ACTIVITY_SERVICE))).getDeviceConfigurationInfo().getGlEsVersion();

        // checking whether the OpenGL version >= 3.0
        if (Double.parseDouble(openGlVersion) >= 3.0) {
            return true;
        } else {
            Toast.makeText(activity, "App needs OpenGl Version 3.0 or later", Toast.LENGTH_SHORT).show();
            activity.finish();
            return false;
        }
    } else {
        Toast.makeText(activity, "App does not support required Build Version", Toast.LENGTH_SHORT).show();
        activity.finish();
        return false;
    }
}
    private void addModel(Anchor anchor, ModelRenderable modelRenderable) {

        // Creating a AnchorNode with a specific anchor
        AnchorNode anchorNode = new AnchorNode(anchor);

        // attaching the anchorNode with the ArFragment
        anchorNode.setParent(arCam.getArSceneView().getScene());
        TransformableNode transform = new TransformableNode(arCam.getTransformationSystem());

        // attaching the anchorNode with the TransformableNode
        transform.setParent(anchorNode);

        // attaching the 3d model with the TransformableNode that is
        // already attached with the node
        transform.setRenderable(modelRenderable);
        transform.select();
    }

}








