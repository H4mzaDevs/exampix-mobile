package com.ecampix.gui.back.commande;

import com.codename1.components.ImageViewer;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.ecampix.entities.Produit;
import com.ecampix.services.ProduitService;
import com.ecampix.utils.Statics;

import java.util.ArrayList;

public class ChooseProduit extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");

    Form previousForm;

    public ChooseProduit(Form previous) {
        super("Choisir un produit", new BoxLayout(BoxLayout.Y_AXIS));

        previousForm = previous;
        addGUIs();
        addActions();

        super.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        addActions();
        this.refreshTheme();
    }

    Button addBtn;

    private void addGUIs() {
        addBtn = new Button("Ajouter produit");
        this.add(addBtn);

        ArrayList<Produit> listProduits = ProduitService.getInstance().getAll();
        if (listProduits.size() > 0) {
            for (Produit produits : listProduits) {
                this.add(makeProduitModel(produits));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    private void addActions() {
        addBtn.addActionListener(action -> new com.ecampix.gui.back.produit.Manage(this, true).show());
    }

    Label typeLabel, typeActionLabel, disponibiliteLabel, prixLabel, dateDebutLabel, dateFinLabel;
    ImageViewer imageIV;
    Button chooseBtn;
    Container btnsContainer;

    private Component makeProduitModel(Produit produit) {
        Container produitModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        produitModel.setUIID("containerRounded");

        typeLabel = new Label("Type : " + produit.getType());
        typeLabel.setUIID("labelDefault");

        typeActionLabel = new Label("Type action : " + produit.getTypeAction());
        typeActionLabel.setUIID("labelDefault");

        disponibiliteLabel = new Label("Disponibilité : " + produit.getDisponibilite());
        disponibiliteLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + produit.getPrix());
        prixLabel.setUIID("labelDefault");

        dateDebutLabel = new Label("Date de debut : " + new SimpleDateFormat("dd-MM-yyyy").format(produit.getDateDebut()));
        dateDebutLabel.setUIID("labelDefault");

        dateFinLabel = new Label("Date de fin : " + new SimpleDateFormat("dd-MM-yyyy").format(produit.getDateFin()));
        dateFinLabel.setUIID("labelDefault");

        if (produit.getImage() != null) {
            String url = Statics.IMAGE_URL + produit.getImage();
            Image image = URLImage.createToStorage(
                    EncodedImage.createFromImage(theme.getImage("default.jpg").fill(1100, 500), false),
                    url,
                    url,
                    URLImage.RESIZE_SCALE
            );
            imageIV = new ImageViewer(image);
        } else {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));
        }
        imageIV.setFocusable(false);

        chooseBtn = new Button("Choisir");
        chooseBtn.addActionListener(l -> {
            Manage.selectedProduit = produit;
            ((Manage) previousForm).refreshProduit();
            previousForm.showBack();
        });

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");
        btnsContainer.add(BorderLayout.CENTER, chooseBtn);

        produitModel.addAll(
                imageIV,
                typeLabel, typeActionLabel, disponibiliteLabel, prixLabel, dateDebutLabel, dateFinLabel,
                btnsContainer
        );

        return produitModel;
    }
}