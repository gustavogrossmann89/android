package br.edu.utfpr.ceiot.appceiot;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.eclipse.californium.core.CoapResource;
import org.eclipse.californium.core.CoapServer;
import org.eclipse.californium.core.server.resources.CoapExchange;

/**
 * TODO (15) Implementação do Server como Service
 */
public class CaliforniumCoapServer extends Service {

    CoapServer server;

    @Override
    public void onCreate() {
        this.server = new CoapServer();
        server.add(new CoapServerResource("CEIOTCoapServer"));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        server.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        server.destroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * TODO (16) Implementação do recurso do Service
     */
    class CoapServerResource extends CoapResource {

        public CoapServerResource(String name) {
            super(name);
        }

        @Override
        public void handleGET(CoapExchange exchange) {

            // respond to the request
            exchange.respond("Olá CEIOT");
        }
    }
}
