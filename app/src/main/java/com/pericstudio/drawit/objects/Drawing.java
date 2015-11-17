package com.pericstudio.drawit.objects;

import com.cloudmine.api.db.LocallySavableCMObject;

public class Drawing extends LocallySavableCMObject {

    private String title;
    private String description;

    public Drawing() {
        super();
    }

    public Drawing(String title, String description) {
        this();
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
