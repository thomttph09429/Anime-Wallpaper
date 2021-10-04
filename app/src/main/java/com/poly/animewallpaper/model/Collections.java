package com.poly.animewallpaper.model;

import java.util.ArrayList;
import java.util.List;

public class Collections {
    private String name;
    private String preview;

    public Collections() {
    }

    public Collections(String name, String preview) {
        this.name = name;
        this.preview = preview;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

}
