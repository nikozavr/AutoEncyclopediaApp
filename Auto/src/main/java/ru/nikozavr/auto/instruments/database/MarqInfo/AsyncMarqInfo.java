package ru.nikozavr.auto.instruments.database.MarqInfo;

import ru.nikozavr.auto.model.Marque;

/**
 * Created by Никита on 09.03.14.
 */
public interface AsyncMarqInfo {
    void getMarqInfoFinish(Marque result);
}
