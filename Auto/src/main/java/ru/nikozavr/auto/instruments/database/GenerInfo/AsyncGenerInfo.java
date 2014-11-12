package ru.nikozavr.auto.instruments.database.GenerInfo;

import ru.nikozavr.auto.model.Generation;

/**
 * Created by Никита on 20.04.2014.
 */
public interface AsyncGenerInfo {
    void getGenerInfoFinish(Generation result);
}
