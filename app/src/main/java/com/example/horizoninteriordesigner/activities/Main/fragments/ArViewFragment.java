package com.example.horizoninteriordesigner.activities.Main.fragments;

import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.horizoninteriordesigner.R;
import com.example.horizoninteriordesigner.activities.Main.MainActivity;
import com.example.horizoninteriordesigner.models.Item;
import com.google.android.filament.Material;
import com.google.android.filament.MaterialInstance;
import com.google.android.filament.gltfio.FilamentAsset;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Session;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.collision.Box;
import com.google.ar.sceneform.collision.CollisionShape;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.Renderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.rendering.Texture;
import com.google.ar.sceneform.ux.BaseArFragment;
import com.google.ar.sceneform.ux.BaseTransformableNode;
import com.google.ar.sceneform.ux.TransformableNode;


import java.lang.ref.WeakReference;
import java.util.List;

import static com.example.horizoninteriordesigner.activities.Main.MainActivity.ITEM_SELECT_TAG;


public class ArViewFragment extends Fragment implements BaseArFragment.OnTapArPlaneListener, BaseArFragment.OnSessionInitializationListener {
    private SceneformFragment sceneformFragment;
    private Renderable renderable;
    private Item item;

    private AnchorNode currentAnchorNode;

    FloatingActionButton takePhotoBtn;
    FloatingActionButton selectItemsBtn;
    FloatingActionButton deleteItemBtn;

    public ArViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sceneformFragment = new SceneformFragment();
        sceneformFragment.setOnTapArPlaneListener(this::onTapPlane);
        sceneformFragment.setOnSessionInitializationListener(this::onSessionInitialization);

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_ar, sceneformFragment).commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ar_view, container, false);

        initialiseButtons(view);

        return view;
    }

    private void initialiseButtons(View view) {
        takePhotoBtn = view.findViewById(R.id.btn_take_photo);
        selectItemsBtn = view.findViewById(R.id.btn_select_items);
        deleteItemBtn = view.findViewById(R.id.btn_delete_item);

        takePhotoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        selectItemsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentAnchorNode = null;
                showItemSelectionFragment();
            }
        });

        deleteItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeAnchorNode(currentAnchorNode);
            }
        });
    }

    /**
     * Opens the item selection page.
     */
    private void showItemSelectionFragment() {
        ((MainActivity) getActivity()).manageFragmentTransaction(ITEM_SELECT_TAG);
    }

    private void removeAnchorNode(AnchorNode nodeToRemove) {
        if (nodeToRemove != null) {
            sceneformFragment.getArSceneView().getScene().removeChild(nodeToRemove);
            nodeToRemove.getAnchor().detach();
            nodeToRemove.setParent(null);
            nodeToRemove = null;
        }
    }

    @Override
    public void onTapPlane(HitResult hitResult, Plane plane, MotionEvent motionEvent) {
        Log.i("ArViewFragment", "Tapped on plane!");

        Renderable renderable = ((MainActivity)getActivity()).getRenderable();

        if (renderable == null) {
            Toast.makeText(getActivity(), "Select a model", Toast.LENGTH_SHORT).show();
            return;
        }

        Anchor anchor = hitResult.createAnchor();

        if (currentAnchorNode != null) {
            currentAnchorNode.setAnchor(anchor);

        } else {
            AnchorNode anchorNode = new AnchorNode(anchor);
            anchorNode.setParent(sceneformFragment.getArSceneView().getScene());

            // Create the transformable model and add it to the anchor.
            TransformableNode model = new TransformableNode(sceneformFragment.getTransformationSystem());
            model.setParent(anchorNode);
            model.setRenderable(renderable);
            model.select();
            model.setOnTapListener(new Node.OnTapListener() {
                @Override
                public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {
                    deleteItemBtn.setVisibility(View.VISIBLE);
                    selectItemsBtn.setVisibility(View.INVISIBLE);
                    takePhotoBtn.setVisibility(View.INVISIBLE);

                    //Node selectedNode = hitTestResult.getNode();

                   // Renderable selectedRenderable = selectedNode.getRenderable();

                   // BaseTransformableNode transformableNode = sceneformFragment.getTransformationSystem().getSelectedNode();

                   // FilamentAsset filamentAsset = selectedNode.getRenderableInstance().getFilamentAsset();

                   // MaterialInstance[] materialInstances = filamentAsset.getMaterialInstances();

                  /*  for (MaterialInstance materialInstance : materialInstances) {
                        Material material = materialInstance.getMaterial();
                        materialInstance.setParameter("baseColorFactor", 0.3f, 0.5f, 0.7f); // Values for Red, Green and Blue
                    }*/
                }
            });

            currentAnchorNode = anchorNode;
        }
    }

   /* public void loadTexture() {
        Texture.builder()
                .setSampler(Texture.Sampler.builder()
                        .setMinFilter(Texture.Sampler.MinFilter.LINEAR_MIPMAP_LINEAR)
                        .setMagFilter(Texture.Sampler.MagFilter.LINEAR)
                        .setWrapMode(Texture.Sampler.WrapMode.REPEAT)
                        .build())
                .setSource(this, Uri.parse("textures/parquet.jpg"))
                .setUsage(Texture.Usage.COLOR)
                .build()
                .thenAccept(
                        texture -> {
                            ArCameraActivity_old activity = weakActivity.get();
                            if (activity != null) {
                                activity.texture = texture;
                            }
                        })
                .exceptionally(
                        throwable -> {
                            Toast.makeText(this, "Unable to load texture", Toast.LENGTH_LONG).show();
                            return null;
                        });
    }*/

    @Override
    public void onSessionInitialization(Session session) {
        Scene scene = sceneformFragment.getArSceneView().getScene();
        scene.addOnPeekTouchListener(new Scene.OnPeekTouchListener() {
            @Override
            public void onPeekTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                if (hitTestResult.getNode() != null) {
                    currentAnchorNode = (AnchorNode) hitTestResult.getNode().getParent();

                    takePhotoBtn.setVisibility(View.INVISIBLE);
                    selectItemsBtn.setVisibility(View.INVISIBLE);
                    deleteItemBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        scene.setOnTouchListener(new Scene.OnTouchListener() {
            @Override
            public boolean onSceneTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
                takePhotoBtn.setVisibility(View.VISIBLE);
                selectItemsBtn.setVisibility(View.VISIBLE);
                deleteItemBtn.setVisibility(View.INVISIBLE);

                return false;
            }
        });

        scene.addOnUpdateListener(new Scene.OnUpdateListener() {
            @Override
            public void onUpdate(FrameTime frameTime) {

            }
        });
    }
}