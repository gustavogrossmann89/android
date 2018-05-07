package br.edu.utfpr.ceiot.appceiot;

import android.content.Context;
import android.support.annotation.NonNull;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.Driver;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.util.concurrent.TimeUnit;

/**
 * TODO (31) Configurar uma tarefa agendada
 */
public class NodeFirebaseJobServiceUtil {

    private static final int INTERVAL_MINUTES = 1;
    private static final int INTERVAL_SECONDS = (int) (TimeUnit.MINUTES.toSeconds(INTERVAL_MINUTES));
    private static final int SYNC_FLEXTIME_SECONDS = INTERVAL_SECONDS;

    private static final String TAG = "NodeFirebaseJobService";

    private static boolean sInitialized;

    synchronized public static void schedule(@NonNull final Context context) {

        if (sInitialized) return;

        Driver driver = new GooglePlayDriver(context);

        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(driver);

        Job constraintReminderJob = dispatcher.newJobBuilder()
                // the JobService that will be called
                .setService(NodeFirebaseJobService.class)
                // uniquely identifies the job
                .setTag(TAG)
                // constraints that need to be satisfied for the job to run
                .setConstraints(Constraint.ON_ANY_NETWORK)
                //setLifetime sets how long this job should persist. The options are to keep the
                //Job "forever" or to have it die the next time the device boots up.
                .setLifetime(Lifetime.FOREVER)
                //Reminders to continuously happen
                .setRecurring(true)
                // start every configurable time
                .setTrigger(Trigger.executionWindow(
                        INTERVAL_MINUTES,
                        INTERVAL_SECONDS + SYNC_FLEXTIME_SECONDS))
                //overwrite an existing job with the same tag
                .setReplaceCurrent(true)
                .build();

        dispatcher.schedule(constraintReminderJob);

        sInitialized = true;
    }
}
