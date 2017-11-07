package org.smartregister.tbr.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.smartregister.Context;
import org.smartregister.tbr.activity.LoginActivity;
import org.smartregister.tbr.application.TbrApplication;
import org.smartregister.tbr.repository.ConfigurableViewsRepository;

import static org.smartregister.util.Log.logError;

/**
 * Created by SGithengi on 19/10/2017.
 * An {@link IntentService} subclass for handling asynchronous tasks for fetching the user configurable views
 * <p>
 */
public class PullConfigurableViewsIntentService extends IntentService {
    public static final String VIEWS_URL = "/rest/viewconfiguration/sync";

    private static final String TAG = PullConfigurableViewsIntentService.class.getCanonicalName();

    private ConfigurableViewsRepository configurableViewsRepository;

    private PullConfigurableViewsServiceHelper pullConfigurableViewsServiceHelper;

    public PullConfigurableViewsIntentService() {
        super("PullConfigurableViewsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null)
            try {
                pullConfigurableViewsServiceHelper.processIntent();
                Intent refreshLoginIntentFilter = new Intent();
                refreshLoginIntentFilter.setAction(LoginActivity.REFRESH_LOGIN_ACTION);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(refreshLoginIntentFilter);
            } catch (Exception e) {
                logError("Error fetching configurable Views");
            }

    }

    @Override
    public void onCreate() {
        super.onCreate();
        configurableViewsRepository = TbrApplication.getInstance().getConfigurableViewsRepository();
        Context context = TbrApplication.getInstance().getContext();
        pullConfigurableViewsServiceHelper = new PullConfigurableViewsServiceHelper(getApplicationContext(),
                configurableViewsRepository, context.getHttpAgent(), context.configuration().dristhiBaseURL());
    }

}