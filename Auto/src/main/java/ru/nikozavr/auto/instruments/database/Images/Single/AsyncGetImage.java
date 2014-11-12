package ru.nikozavr.auto.instruments.database.Images.Single;

import ru.nikozavr.auto.AutoEncyclopedia;
import ru.nikozavr.auto.model.ImageItem;

/**
 * Created by nikozavr on 3/24/14.
 */
public interface AsyncGetImage {
    AutoEncyclopedia getAutoEncyclopedia();

    void finishGetPic(ImageItem result);
}
