package com.ecampix;


import com.codename1.io.Log;
import com.codename1.ui.Display;
import com.codename1.ui.Form;
import com.codename1.ui.Toolbar;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.ecampix.entities.User;
import com.ecampix.gui.LoginForm;

import static com.codename1.ui.CN.addNetworkErrorListener;
import static com.codename1.ui.CN.updateNetworkThreadCount;

/**
 * This file was generated by <a href="https://www.codenameone.com/">Codename One</a> for the purpose 
 * of building native mobile applications using Java.
 */
public class MainApp {

    private Form current;
    private Resources theme;
    private static User session;

    public static User getSession() {
        return session;
    }

    public static void setSession(User session) {
        MainApp.session = session;
    }

    public void init(Object context) {
        // use two network threads instead of one
        updateNetworkThreadCount(3);

        theme = UIManager.initFirstTheme("/theme");

        // Pro only feature
        Log.bindCrashProtection(true);

        // Enable Toolbar on all Forms by default
        Toolbar.setGlobalToolbar(true);

        addNetworkErrorListener(err -> {
            err.consume();
            if (err.getError() != null) {
                Log.e(err.getError());
            }
            Log.sendLogAsync();
            //Dialog.show("Connection Error", "There was a networking error in the connection to " + err.getConnectionRequest().getUrl(), "OK", null);
        });
    }

    public void start() {
        if (current != null) {
            current.show();
            return;
        }
        new LoginForm().show();
    }

    public void stop() {
        current = Display.getInstance().getCurrent();
    }

    public void destroy() {
    }

}
