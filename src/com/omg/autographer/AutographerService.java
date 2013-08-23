package com.omg.autographer;

import java.util.ArrayList;

import com.omg.autographer.util.ALog;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

public class AutographerService extends Service {
	private static final String TAG = "AutographerService";

	ArrayList<Messenger> clients = new ArrayList<Messenger>();

	// commands for the service
	static final int REGISTER_CLIENT = 1;
	static final int UNREGISTER_CLIENT = 2;
	static final int SET_VALUE = 3;

	int value;

	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case REGISTER_CLIENT:
				clients.add(msg.replyTo);
				break;
			case UNREGISTER_CLIENT:
				clients.remove(msg.replyTo);
				break;
			case SET_VALUE:
				value = msg.arg1;
				for (int i = clients.size() - 1; i >= 0; i--) {
					try {
						clients.get(i).send(Message.obtain(null, SET_VALUE, value, 0));
					} catch (RemoteException e) {
						// The client is dead. Remove it from the list;
						// we are going through the list from back to front
						// so this is safe to do inside the loop.
						clients.remove(i);
					}
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	final Messenger messenger = new Messenger(new IncomingHandler());

	@Override
	public void onCreate() {
		ALog.d(TAG, "onCreate");
	}

	@Override
	public void onDestroy() {
		ALog.d(TAG, "onDestroy");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return messenger.getBinder();
	}

}
