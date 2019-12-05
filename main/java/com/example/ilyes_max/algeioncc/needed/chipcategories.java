package com.example.ilyes_max.algeioncc.needed;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.tylersuehr.chips.Chip;

public class chipcategories extends Chip {



    private Object id;
    private Uri avatarUri;
    private Drawable avatarDrawable;
    private String label;
    private String info;

    public chipcategories(@NonNull Object id, @Nullable Uri avatarUri, @NonNull String label, @Nullable String info) {
        this.id = id;
        this.avatarUri = avatarUri;
        this.label = label;
        this.info = info;
    }

    public chipcategories(@NonNull Object id, @Nullable Drawable avatarDrawable, @NonNull String label, @Nullable String info) {
        this.id = id;
        this.avatarDrawable = avatarDrawable;
        this.label = label;
        this.info = info;
    }

    public chipcategories(@Nullable Uri avatarUri, @NonNull String label, @Nullable String info) {
        this.avatarUri = avatarUri;
        this.label = label;
        this.info = info;
    }

    public chipcategories(@Nullable Drawable avatarDrawable, @NonNull String label, @Nullable String info) {
        this.avatarDrawable = avatarDrawable;
        this.label = label;
        this.info = info;
    }

    public chipcategories(@NonNull Object id, @NonNull String label, @Nullable String info) {
        this.id = id;
        this.label = label;
        this.info = info;
    }

    public chipcategories(@NonNull String label, @Nullable String info) {
        this.label = label;
        this.info = info;
    }


    @Nullable
    @Override
    public Object getId() {
        return id;
    }

    @NonNull
    @Override
    public String getTitle() {
        return label;
    }

    @Nullable
    @Override
    public String getSubtitle() {
        return info;
    }

    @Nullable
    @Override
    public Uri getAvatarUri() {
        return avatarUri;
    }

    @Nullable
    @Override
    public Drawable getAvatarDrawable() {
        return avatarDrawable;
    }
}
