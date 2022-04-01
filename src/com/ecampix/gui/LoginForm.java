package com.ecampix.gui;

import com.codename1.ui.Button;
import com.codename1.ui.Form;
import com.codename1.ui.layouts.BoxLayout;

public class LoginForm extends Form {

    public static Form loginForm;

    public LoginForm() {
        super("Connexion", new BoxLayout(BoxLayout.Y_AXIS));
        loginForm = this;
        addGUIs();
    }

    private void addGUIs() {

        Button backendBtn = new Button("Backend");
        backendBtn.addActionListener(l -> {
            new com.ecampix.gui.back.AccueilBack().show();
        });

        Button frontendBtn = new Button("Frontend");
        frontendBtn.addActionListener(l -> {
            new com.ecampix.gui.front.AccueilFront().show();
        });

        this.addAll(backendBtn, frontendBtn);
    }
}
