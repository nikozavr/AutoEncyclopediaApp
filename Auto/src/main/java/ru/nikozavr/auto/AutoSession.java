package ru.nikozavr.auto;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AutoSession extends Service {
    public AutoSession() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
