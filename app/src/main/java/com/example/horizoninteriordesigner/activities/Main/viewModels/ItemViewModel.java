package com.example.horizoninteriordesigner.activities.Main.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.horizoninteriordesigner.models.Item;
import com.google.ar.sceneform.rendering.Renderable;


/**
 * Manages UI-related data which is shared amongst the fragments in MainActivity
 * {@link com.example.horizoninteriordesigner.activities.Main.MainActivity}
 *
 */
public class ItemViewModel extends ViewModel {
    private MutableLiveData<Item> item = new MutableLiveData<Item>();
    private MutableLiveData<Renderable> renderable = new MutableLiveData<Renderable>();


    /*
         Methods for Item
     */
    public MutableLiveData<Item> getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item.setValue(item);
    }


    /*
         Methods for Renderable
     */
    public MutableLiveData<Renderable> getRenderable() {
        return renderable;
    }

    public void setRenderable(Renderable renderable) {
        this.renderable.setValue(renderable);
    }
}
