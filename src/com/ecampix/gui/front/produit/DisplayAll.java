package com.ecampix.gui.front.produit;

import com.codename1.components.ImageViewer;
import com.codename1.components.InteractionDialog;
import com.codename1.components.ShareButton;
import com.codename1.io.FileSystemStorage;
import com.codename1.io.Log;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.Border;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.ImageIO;
import com.codename1.ui.util.Resources;
import com.ecampix.entities.Produit;
import com.ecampix.gui.front.AccueilFront;
import com.ecampix.gui.front.commande.Manage;
import com.ecampix.services.ProduitService;
import com.ecampix.utils.Statics;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

public class DisplayAll extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");
    TextField searchTF;
    ArrayList<Component> componentModels;

    public DisplayAll() {
        super("Produits", new BoxLayout(BoxLayout.Y_AXIS));
        addGUIs();
        super.getToolbar().hideToolbar();
    }

    private void addGUIs() {
        ArrayList<Produit> listProduits = ProduitService.getInstance().getAll();
        componentModels = new ArrayList<>();

        searchTF = new TextField("", "Chercher un produit par type");
        searchTF.addDataChangedListener((d, t) -> {
            if (componentModels.size() > 0) {
                for (Component componentModel : componentModels) {
                    this.removeComponent(componentModel);
                }
            }
            componentModels = new ArrayList<>();
            for (Produit listProduit : listProduits) {
                if (listProduit.getType().startsWith(searchTF.getText())) {
                    Component model = makeProduitModel(listProduit);
                    this.add(model);
                    componentModels.add(model);
                }
            }
            this.revalidate();
        });
        this.add(searchTF);

        if (listProduits.size() > 0) {
            for (Produit listProduit : listProduits) {
                Component model = makeProduitModel(listProduit);
                this.add(model);
                componentModels.add(model);
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    Label typeLabel, typeActionLabel, disponibiliteLabel, prixLabel, dateDebutLabel, dateFinLabel;
    ImageViewer imageIV;
    Button commanderButton;
    Container btnsContainer;

    private Component makeProduitModel(Produit produit) {

        Container produitModel = makeModelWithoutButtons(produit);

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        commanderButton = new Button("Commander");
        commanderButton.setUIID("buttonMain");
        commanderButton.addActionListener(l -> new Manage(AccueilFront.accueilFrontForm, produit).show());

        Button btnAfficherScreenshot = new Button("Partager");
        btnAfficherScreenshot.addActionListener(listener -> share(produit));

        btnsContainer.add(BorderLayout.WEST, commanderButton);
        btnsContainer.add(BorderLayout.EAST, btnAfficherScreenshot);

        produitModel.add(btnsContainer);

        return produitModel;
    }

    private Container makeModelWithoutButtons(Produit produit) {
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

        produitModel.addAll(
                imageIV,
                typeLabel, typeActionLabel, disponibiliteLabel, prixLabel, dateDebutLabel, dateFinLabel
        );

        return produitModel;
    }

    private void share(Produit produit) {
        Form form = new Form();
        form.add(new Label("Produit " + produit.getType()));
        form.add(makeModelWithoutButtons(produit));
        String imageFile = FileSystemStorage.getInstance().getAppHomePath() + "screenshot.png";
        Image screenshot = Image.createImage(
                com.codename1.ui.Display.getInstance().getDisplayWidth(),
                com.codename1.ui.Display.getInstance().getDisplayHeight()
        );
        form.revalidate();
        form.setVisible(true);
        form.paintComponent(screenshot.getGraphics(), true);
        form.removeAll();
        try (OutputStream os = FileSystemStorage.getInstance().openOutputStream(imageFile)) {
            ImageIO.getImageIO().save(screenshot, os, ImageIO.FORMAT_PNG, 1);
        } catch (IOException err) {
            Log.e(err);
        }
        Form screenShotForm = new Form("Partager le produit", new BoxLayout(BoxLayout.Y_AXIS));
        ImageViewer screenshotViewer = new ImageViewer(screenshot.fill(1000, 2000));
        screenshotViewer.setFocusable(false);
        screenshotViewer.setUIID("screenshot");
        ShareButton btnPartager = new ShareButton();
        btnPartager.setText("Partager ");
        btnPartager.setTextPosition(LEFT);
        btnPartager.setImageToShare(imageFile, "image/png");
        btnPartager.setTextToShare(produit.toString());
        screenShotForm.addAll(screenshotViewer, btnPartager);
        screenShotForm.getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> AccueilFront.accueilFrontForm.showBack());
        screenShotForm.show();
        // FIN API PARTAGE
    }
}