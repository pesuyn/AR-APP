package com.example.ar_app;


import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.ar.core.Anchor;
import com.google.ar.core.AugmentedFace;
import com.google.ar.core.Camera;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.rendering.ViewRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.AugmentedFaceNode;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {

    private ImageButton ib_switch_camera;
    private ModelRenderable modelRenderable;
    private Texture texture;
    private boolean isAdded = false;
    private ImageButton no_filter_btn, mask2, op, snap;
    private int typeFace = 0;
    private ArFragment arFragment;


    private static final int MASK[] = {
            R.id.no_filter,
            R.id.hair,
            R.id.op,
            R.id.snap,
            R.id.glasses2,
            R.id.glasses3,
            R.id.glasses4,
            R.id.glasses5,
            R.id.mask,
            R.id.mask2,
            R.id.mask3,
            R.id.dog,
            R.id.cat2
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ARFragment);
        op = (ImageButton) findViewById(R.id.op);
        snap = (ImageButton) findViewById(R.id.snap);
        mask2 = (ImageButton) findViewById(R.id.mask2);
        no_filter_btn = (ImageButton) findViewById(R.id.no_filter);

        ImageButton no_filter = (ImageButton) findViewById(R.id.no_filter);
        no_filter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v2) {
               Intent intent = new Intent(MainActivity.this,MainActivity.class);
               startActivity(intent);
            }
        });

        ImageButton op = (ImageButton) findViewById(R.id.op);
        op.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 9;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
                getFilter_op();
            }
        });
        ImageButton mask2 = (ImageButton) findViewById(R.id.mask2);
        mask2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 9;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
                getFilter1();
            }
        });

        ImageButton snap = (ImageButton) findViewById(R.id.snap);
        snap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v1) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 9;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
                getFilter_Fox();
            }
        });
        ImageButton hair = (ImageButton) findViewById(R.id.hair);
        hair.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v1) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 9;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
                getFilter_hair();
            }
        });
        ImageButton glasses = (ImageButton) findViewById(R.id.glasses2);
        glasses .setOnClickListener(new View.OnClickListener() {
            public void onClick(View v1) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 9;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
                getFilter_glasses();
            }
        });
        ImageButton dog = (ImageButton) findViewById(R.id.dog);
        dog .setOnClickListener(new View.OnClickListener() {
            public void onClick(View v1) {
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background);
                typeFace = 9;
                findViewById(MASK[typeFace]).setBackgroundResource(R.drawable.round_background_select);
                getFilter();
            }
        });
        ImageButton camera = (ImageButton) findViewById(R.id.camera);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScreenshotButton(v);
            }
        });

    }

    public void ScreenshotButton(View view){
        View view1 = getWindow().getDecorView().getRootView();
        view1.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view1.getDrawingCache());
        view1.setDrawingCacheEnabled(false);

        String filePath = Environment.getExternalStorageDirectory()+"/Facebook/"+ Calendar.getInstance().getTime().toString()+".jpg";
        File fileScreenshot = new File(filePath);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(fileScreenshot);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(fileScreenshot);
        intent.setDataAndType(uri,"image/jpeg");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

    private void getFilter() {
        CustomArFragment customARFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.ARFragment);

        final CompletableFuture<Void> voidCompletableFuture = ModelRenderable.builder()
                .setSource(this, R.raw.mask)
                .build()
                .thenAccept(renderable -> {
                    modelRenderable = renderable;

                    modelRenderable.setShadowReceiver(false);
                    modelRenderable.setShadowCaster(false);
                });

        Texture.builder()
                .setSource(this, R.drawable.dog3)
                .build()
                .thenAccept(texture -> this.texture =texture );

        customARFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        customARFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {

            if(modelRenderable == null && texture == null)
                return;

            Frame frame = customARFragment.getArSceneView().getArFrame();

            Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);

            for (AugmentedFace augmentedFace : augmentedFaces) {
                if(isAdded)
                    return;

                AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customARFragment.getArSceneView().getScene());
                augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);
                augmentedFaceNode.setFaceMeshTexture(texture);

                isAdded = true;
            }


        });
    }

    private void getFilter_hair() {
        CustomArFragment customARFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.ARFragment);

        final CompletableFuture<Void> voidCompletableFuture = ModelRenderable.builder()
                .setSource(this, R.raw.cat)
                .build()
                .thenAccept(renderable -> {
                    modelRenderable = renderable;

                    modelRenderable.setShadowReceiver(false);
                    modelRenderable.setShadowCaster(false);
                });

        Texture.builder()
                .setSource(this, R.drawable.hair)
                .build()
                .thenAccept(texture -> this.texture =texture );

        customARFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        customARFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {

            if(modelRenderable == null && texture == null)
                return;

            Frame frame = customARFragment.getArSceneView().getArFrame();

            Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);

            for (AugmentedFace augmentedFace : augmentedFaces) {
                if(isAdded)
                    return;

                AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customARFragment.getArSceneView().getScene());
                augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);
                augmentedFaceNode.setFaceMeshTexture(texture);

                isAdded = true;
            }


        });
    }

    private void getFilter_op() {
        CustomArFragment customARFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.ARFragment);

        final CompletableFuture<Void> voidCompletableFuture = ModelRenderable.builder()
                .setSource(this, R.raw.mask)
                .build()
                .thenAccept(renderable -> {
                    modelRenderable = renderable;

                    modelRenderable.setShadowReceiver(false);
                    modelRenderable.setShadowCaster(false);
                });

        Texture.builder()
                .setSource(this, R.drawable.op2)
                .build()
                .thenAccept(texture -> this.texture =texture );

        customARFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        customARFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {

            if(modelRenderable == null && texture == null)
                return;

            Frame frame = customARFragment.getArSceneView().getArFrame();

            Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);

            for (AugmentedFace augmentedFace : augmentedFaces) {
                if(isAdded)
                    return;

                AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customARFragment.getArSceneView().getScene());
                augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);
                augmentedFaceNode.setFaceMeshTexture(texture);

                isAdded = true;
            }


        });
    }
    private void getFilter_Fox() {
        CustomArFragment customARFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.ARFragment);

        final CompletableFuture<Void> voidCompletableFuture = ModelRenderable.builder()
                .setSource(this, R.raw.fox_face)
                .build()
                .thenAccept(renderable -> {
                    modelRenderable = renderable;

                    modelRenderable.setShadowReceiver(false);
                    modelRenderable.setShadowCaster(false);
                });

        Texture.builder()
                .setSource(this, R.drawable.fox_face_mesh_texture)
                .build()
                .thenAccept(texture -> this.texture =texture );

        customARFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        customARFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {

            if(modelRenderable == null && texture == null)
                return;

            Frame frame = customARFragment.getArSceneView().getArFrame();

            Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);

            for (AugmentedFace augmentedFace : augmentedFaces) {
                if(isAdded)
                    return;

                AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customARFragment.getArSceneView().getScene());
                augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);
                augmentedFaceNode.setFaceMeshTexture(texture);

                isAdded = true;
            }


        });
    }
    private void getFilter_glasses() {
        CustomArFragment customARFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.ARFragment);

        final CompletableFuture<Void> voidCompletableFuture = ModelRenderable.builder()
                .setSource(this, R.raw.mask)
                .build()
                .thenAccept(renderable -> {
                    modelRenderable = renderable;

                    modelRenderable.setShadowReceiver(false);
                    modelRenderable.setShadowCaster(false);
                });

        Texture.builder()
                .setSource(this, R.drawable.glasses2)
                .build()
                .thenAccept(texture -> this.texture =texture );

        customARFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        customARFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {

            if(modelRenderable == null && texture == null)
                return;

            Frame frame = customARFragment.getArSceneView().getArFrame();

            Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);

            for (AugmentedFace augmentedFace : augmentedFaces) {
                if(isAdded)
                    return;

                AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customARFragment.getArSceneView().getScene());
                augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);
                augmentedFaceNode.setFaceMeshTexture(texture);

                isAdded = true;
            }


        });
    }
    private void getFilter1() {
        CustomArFragment customARFragment = (CustomArFragment) getSupportFragmentManager().findFragmentById(R.id.ARFragment);

        ModelRenderable.builder()
                .setSource(this, R.raw.mario_hat)
                .build()
                .thenAccept(renderable -> {
                    modelRenderable = renderable;

                    modelRenderable.setShadowReceiver(false);
                    modelRenderable.setShadowCaster(false);
                });

        Texture.builder()
                .setSource(this, R.drawable.mask2)
                .build()
                .thenAccept(texture -> this.texture =texture );

        customARFragment.getArSceneView().setCameraStreamRenderPriority(Renderable.RENDER_PRIORITY_FIRST);

        customARFragment.getArSceneView().getScene().addOnUpdateListener(frameTime -> {

            if(modelRenderable == null && texture == null)
                return;

            Frame frame = customARFragment.getArSceneView().getArFrame();

            Collection<AugmentedFace> augmentedFaces = frame.getUpdatedTrackables(AugmentedFace.class);

            for (AugmentedFace augmentedFace : augmentedFaces) {
                if(isAdded)
                    return;

                AugmentedFaceNode augmentedFaceNode = new AugmentedFaceNode(augmentedFace);
                augmentedFaceNode.setParent(customARFragment.getArSceneView().getScene());
                augmentedFaceNode.setFaceRegionsRenderable(modelRenderable);
                augmentedFaceNode.setFaceMeshTexture(texture);

                isAdded = true;
            }


        });
        customARFragment.isRemoving();
    }







}