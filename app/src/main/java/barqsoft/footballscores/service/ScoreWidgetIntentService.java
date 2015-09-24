package barqsoft.footballscores.service;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.widget.RemoteViews;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.widget.ScoreWidgetProvider;

/**
 * Created by Ragnar on 9/24/2015.
 */
public class ScoreWidgetIntentService extends IntentService {

    public static final int COL_HOME = 1;
    public static final int COL_AWAY = 2;
    public static final int COL_HOME_GOALS = 3;
    public static final int COL_AWAY_GOALS = 4;

    private static final String[] SCORE_COLUMNS = {

            DatabaseContract.scores_table.DATE_COL,
            DatabaseContract.scores_table.HOME_COL,
            DatabaseContract.scores_table.AWAY_COL,
            DatabaseContract.scores_table.HOME_GOALS_COL,
            DatabaseContract.scores_table.AWAY_GOALS_COL,
            DatabaseContract.scores_table.MATCH_ID,
            DatabaseContract.scores_table.LEAGUE_COL
    };


    public ScoreWidgetIntentService() {
        super("ScoreWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager
                .getAppWidgetIds(new ComponentName(this, ScoreWidgetProvider.class));

        Uri dateUri = DatabaseContract.scores_table.buildScoreWithDate();
        String[] date = new String[1];
        date[0] = Utilies.getFragmentDate(0);
        Cursor data = getContentResolver().query(dateUri, SCORE_COLUMNS, DatabaseContract.PATH_DATE, date, null);

        if (data == null) {
            return;
        }

        if (!data.moveToFirst()) {
            data.close();
            return;
        }
        String homeName = data.getString(COL_HOME);
        String awayName = data.getString(COL_AWAY);
        int homeScore = data.getInt(COL_HOME_GOALS);
        int awayScore = data.getInt(COL_AWAY_GOALS);

        data.close();

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(getPackageName(), R.layout.widget_layout);

            String scores = Utilies.getScores(homeScore, awayScore);

            views.setTextViewText(R.id.home_name, homeName);
            views.setTextViewText(R.id.away_name, awayName);
            views.setTextViewText(R.id.score_textview, scores);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

}
