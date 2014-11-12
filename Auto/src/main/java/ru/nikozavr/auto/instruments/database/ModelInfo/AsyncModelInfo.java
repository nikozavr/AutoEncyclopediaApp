package ru.nikozavr.auto.instruments.database.ModelInfo;

import ru.nikozavr.auto.model.Model;

/**
 * Created by nikozavr on 4/10/14.
 */
public interface AsyncModelInfo {
    void getModelInfoFinish(Model result);
}
