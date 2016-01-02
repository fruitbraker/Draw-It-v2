package com.pericstudio.drawit.objects;

import android.graphics.Bitmap;

import com.cloudmine.api.db.LocallySavableCMObject;

public class PictureDummy extends LocallySavableCMObject {

    private Bitmap stuff;

    public PictureDummy() {
        super();
    }

    public PictureDummy(Bitmap dummy) {
        super();
        stuff = dummy;
    }

    public Bitmap getStuff() {
        return stuff;
    }

    public void setStuff(Bitmap stuff) {
        this.stuff = stuff;
    }
}
