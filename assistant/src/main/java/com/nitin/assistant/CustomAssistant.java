package com.nitin.assistant;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;
import uk.co.samuelwall.materialtaptargetprompt.extras.backgrounds.FullscreenPromptBackground;
import uk.co.samuelwall.materialtaptargetprompt.extras.focals.RectanglePromptFocal;

public class CustomAssistant {

    public static final String KEY_WORK_INPUT = "key_input_data";
    private static List<HashMap<String, Object>> data;
    private static Boolean isAnimation;
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;


    private static Iterator<String> textIterator, viewIterator, audioIterator;

    public static void init(@NonNull Application application, String journey, String language) {

        CustomAssistant.parseJSON(application.getApplicationContext(), journey, language);


        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                CustomAssistant.guide(activity);

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });

    }

    public static void parseJSON(@NonNull Context context, String journey, String languageCode) {

        StringBuilder json = new StringBuilder();
        sharedPreferences = context.getSharedPreferences("Assistant", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();

        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(context.getResources().getAssets().open("config.json")));

            String temp;
            while ((temp = bufferedReader.readLine()) != null) {
                json.append(temp);
            }

            JSONObject obj = new JSONObject(json.toString());
            JSONObject jsonObject;
            if (obj.has(languageCode))
                jsonObject = obj.getJSONObject(languageCode);
            else
                jsonObject = obj.getJSONObject("en");

            getData(jsonObject, journey);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        if (!sharedPreferences.getBoolean("sharedAudio", false)) {
            offlineDownload(context);
            editor.putBoolean("sharedAudio", true);
            editor.commit();
        }

    }

    private static void offlineDownload(@NonNull Context context) {
        String[] audioArray = new String[data.size()];

        for (int i = 0; i < data.size(); i++) {
            audioArray[i] = (String) data.get(i).get("audioUrl");
        }

        Data inputData = new Data.Builder()
                .putStringArray(KEY_WORK_INPUT, audioArray)
                .build();

        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(MyWorker.class)
//                .setConstraints(constraints)
                .setInputData(inputData)
                .build();
        WorkManager.getInstance(context).enqueue(request);
    }

    private static void getData(@NotNull JSONObject object) throws JSONException {

        JSONObject jsonObj = object.getJSONObject("sample");
        Boolean isAnimation = jsonObj.getBoolean("pulse");
        JSONArray jsonArray = jsonObj.getJSONArray("journey");

        data = new ArrayList<>();
        HashMap<String, Object> map;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            map = new HashMap<>();
            map.put("activity", jsonObject.getString("activity"));
            map.put("text", jsonObject.getString("text"));
            map.put("audioUrl", jsonObject.getString("audioUrl"));
            map.put("view", jsonObject.getString("view"));

            data.add(map);
        }
    }

    private static void getData(@NotNull JSONObject object, String name) throws JSONException {
        JSONObject jsonObj = object.getJSONObject(name);
        isAnimation = jsonObj.getBoolean("pulse");
        JSONArray jsonArray = jsonObj.getJSONArray("journey");

        data = new ArrayList<>();
        HashMap<String, Object> map;

        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            map = new HashMap<>();
            map.put("activity", jsonObject.getString("activity"));
            map.put("text", jsonObject.getString("text"));
            map.put("audioUrl", jsonObject.getString("audioUrl"));
            map.put("view", jsonObject.getString("view"));

            data.add(map);
        }
    }

    public static void guide(@NonNull Activity activity) {

        String activityName = activity.getClass().getSimpleName();
        ArrayList<String> textList = new ArrayList<>();
        ArrayList<String> viewList = new ArrayList<>();
        ArrayList<String> audioList = new ArrayList<>();


        for (int i = 0; i < data.size(); i++) {

            if (data.get(i).get("activity").equals(activityName)) {

                textList.add((String) data.get(i).get("text"));
                viewList.add((String) data.get(i).get("view"));
                audioList.add((String) data.get(i).get("audioUrl"));
            }
        }

        textIterator = textList.iterator();
        viewIterator = viewList.iterator();
        audioIterator = audioList.iterator();

        try {

            if (viewIterator.hasNext()) {
                String view = viewIterator.next();
                String text = textIterator.next();
                String audio = audioIterator.next();
                while (sharedPreferences.getBoolean(view, false)) {
                    view = viewIterator.next();
                    text = textIterator.next();
                    audio = audioIterator.next();
                }
                editor.putBoolean(view, true);
                editor.commit();


                showPrompt(activity, text, view, audio);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void showPrompt(final Activity activity, final String text, final String res, final String audio) {

        new MaterialTapTargetPrompt.Builder(activity)
                .setTarget(activity.getResources().getIdentifier(res, "id", activity.getPackageName()))
                .setPrimaryText(text)
                .setPrimaryTextColour(Color.parseColor("#ffffff"))
                .setBackButtonDismissEnabled(true)
                .setIdleAnimationEnabled(isAnimation)
                .setBackgroundColour(Color.parseColor("#D0000000"))
                .setPromptBackground(new FullscreenPromptBackground())
                .setPromptFocal(new RectanglePromptFocal())
                .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                    @Override
                    public void onPromptStateChanged(@NonNull MaterialTapTargetPrompt prompt, int state) {

//                        MediaHandlerThread mediaHandlerThread = new MediaHandlerThread("MediaPlayer");
//                        mediaHandlerThread.start();
//                        Message message = Message.obtain();
//                        message.obj = "play";
//                        mediaHandlerThread.getHandler().sendMessage(message);


                        MediaPlayerManager mediaPlayerManager = MediaPlayerManager.getInstance();

//                        TTSManager ttsManager = TTSManager.getInstance();
//                        ttsManager.init(activity);


                        if (state == MaterialTapTargetPrompt.STATE_REVEALING) {
//                            ttsManager.play(text);

                            mediaPlayerManager.play(activity, audio);
                        }

                        if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {
                            mediaPlayerManager.stop();
//                            ttsManager.stop();
                            nextState(activity);
                        }


                        if (state == MaterialTapTargetPrompt.STATE_NON_FOCAL_PRESSED || state == MaterialTapTargetPrompt.STATE_BACK_BUTTON_PRESSED) {
                            mediaPlayerManager.stop();
//                            ttsManager.stop();

                            View rootView = activity.getWindow().getDecorView().getRootView();
                            rootView.setOnTouchListener(new View.OnTouchListener() {
                                @Override
                                public boolean onTouch(View v, MotionEvent event) {
                                    nextState(activity);
                                    return true;
                                }
                            });

                        }

                    }
                })
                .show();
    }

    private static void nextState(Activity activity) {
        if (viewIterator.hasNext()) {
            String view = viewIterator.next();
            editor.putBoolean(view, true);
            editor.commit();
            showPrompt(activity, textIterator.next(), view, audioIterator.next());
        }
    }

}

