package com.ecampix.gui.front;

import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.ecampix.gui.LoginForm;

public class AccueilFront extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    Label label;

    public static Form accueilFrontForm;

    public AccueilFront() {
        super(new BorderLayout());
        accueilFrontForm = this;
        addGUIs();
    }

    private void addGUIs() {
        Tabs tabs = new Tabs();

        tabs.addTab("Produits", FontImage.MATERIAL_ARCHIVE, 5,
                new com.ecampix.gui.front.produit.DisplayAll()
        );
        tabs.addTab("Plus", FontImage.MATERIAL_MENU, 5, moreGUI());

        this.add(BorderLayout.CENTER, tabs);
    }

    private Container moreGUI() {

        ImageViewer userImage = new ImageViewer(theme.getImage("default.jpg").fill(200, 200));
        userImage.setUIID("candidatImage");
        label = new Label("Username"/*MainApp.getSession().getEmail()*/);
        label.setUIID("links");
        Button btnDeconnexion = new Button();
        btnDeconnexion.setUIID("buttonLogout");
        btnDeconnexion.setMaterialIcon(FontImage.MATERIAL_ARROW_FORWARD);
        btnDeconnexion.addActionListener(action -> {
            LoginForm.loginForm.showBack();
        });

        Container userContainer = new Container(new BorderLayout());
        userContainer.setUIID("userContainer");
        userContainer.add(BorderLayout.WEST, userImage);
        userContainer.add(BorderLayout.CENTER, label);
        userContainer.add(BorderLayout.EAST, btnDeconnexion);

        Container menuContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        menuContainer.addAll(
                userContainer,
                makeMesCommandesButton()
        );

        return (menuContainer);
    }

    private Button makeMesCommandesButton() {
        Button button = new Button("   Mes commandes");
        button.setUIID("buttonMenu");
        button.setMaterialIcon(FontImage.MATERIAL_SHOPPING_CART);
        button.addActionListener(action -> new com.ecampix.gui.front.commande.DisplayMy(AccueilFront.accueilFrontForm).show());
        return button;
    }
}
