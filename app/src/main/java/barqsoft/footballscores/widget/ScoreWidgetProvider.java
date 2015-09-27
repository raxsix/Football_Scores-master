package barqsoft.footballscores.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import barqsoft.footballscores.service.ScoreWidgetIntentService;

/**
 * Created by Ragnar on 9/24/2015.
 */
public class ScoreWidgetProvider extends AppWidgetProvider {

    private static final String TAG = ScoreWidgetProvider.class.getSimpleName();

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        context.startService(new Intent(context, ScoreWidgetIntentService.class));

    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {

        context.startService(new Intent(context, ScoreWidgetIntentService.class));
    }


}

