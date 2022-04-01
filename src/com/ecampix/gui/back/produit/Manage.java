package com.ecampix.gui.back.produit;

import com.codename1.capture.Capture;
import com.codename1.components.ImageViewer;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.ecampix.entities.Produit;
import com.ecampix.gui.back.commande.ChooseProduit;
import com.ecampix.services.ProduitService;
import com.ecampix.utils.Statics;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Manage extends Form {

    Resources theme = UIManager.initFirstTheme("/theme");

    boolean imageEdited = false;
    boolean isChoose;

    Produit currentProduit;
    String selectedImage;

    Label typeLabel, typeActionLabel, disponibiliteLabel, prixLabel, dateDebutLabel, dateFinLabel, imageLabel;
    TextField typeTF, typeActionTF, disponibiliteTF, prixTF;
    Calendar calendar;
    ImageViewer imageIV;
    Button selectImageButton, manageButton;

    Form previous;

    public Manage(Form previous, boolean isChoose) {
        super(DisplayAll.currentProduit == null ? "Nouveau produit" :  "Modifier le produit", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;
        this.isChoose = isChoose;

        currentProduit = DisplayAll.currentProduit;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    boolean selectedStart = false;
    Date selectedStartDate = null;
    Date selectedEndDate = null;

    private void addGUIs() {

        typeLabel = new Label("Type : ");
        typeLabel.setUIID("labelDefault");
        typeTF = new TextField();
        typeTF.setHint("Tapez le type du produit");

        typeActionLabel = new Label("Type action : ");
        typeActionLabel.setUIID("labelDefault");
        typeActionTF = new TextField();
        typeActionTF.setHint("Tapez le type du produit");

        disponibiliteLabel = new Label("Disponibilite : ");
        disponibiliteLabel.setUIID("labelDefault");
        disponibiliteTF = new TextField();
        disponibiliteTF.setHint("Tapez le disponibilite du produit");

        prixLabel = new Label("Prix : ");
        prixLabel.setUIID("labelDefault");
        prixTF = new TextField();
        prixTF.setHint("Tapez le prix du produit");

        dateDebutLabel = new Label("Date expo & date de fin : ");
        dateDebutLabel.setUIID("labelDefault");

        calendar = new Calendar();

        imageLabel = new Label("Image : ");
        imageLabel.setUIID("labelDefault");
        selectImageButton = new Button("Ajouter une image");

        if (currentProduit == null) {
            imageIV = new ImageViewer(theme.getImage("default.jpg").fill(1100, 500));
            manageButton = new Button("Ajouter");
        } else {
            selectedStartDate = currentProduit.getDateDebut();
            selectedEndDate = currentProduit.getDateFin();

            typeTF.setText(currentProduit.getType());
            typeActionTF.setText(currentProduit.getTypeAction());
            disponibiliteTF.setText(currentProduit.getDisponibilite());
            prixTF.setText(String.valueOf(currentProduit.getPrix()));

            calendar.highlightDate(selectedStartDate, "dateStart");
            calendar.highlightDate(selectedEndDate, "dateEnd");

            if (currentProduit.getImage() != null) {
                String url = Statics.IMAGE_URL + currentProduit.getImage();
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

            selectImageButton.setText("Modifier l'image");
            selectedImage = currentProduit.getImage();

            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                typeLabel, typeTF,
                typeActionLabel, typeActionTF,
                disponibiliteLabel, disponibiliteTF,
                prixLabel, prixTF,
                dateDebutLabel, calendar,
                imageLabel, imageIV, selectImageButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        selectImageButton.addActionListener(a -> {
            selectedImage = Capture.capturePhoto(900, -1);
            try {
                imageEdited = true;
                imageIV.setImage(Image.createImage(selectedImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectImageButton.setText("Modifier l'image");
        });

        calendar.addActionListener(l -> {
            if (!selectedStart) {
                if (selectedStartDate != null) {
                    calendar.unHighlightDate(selectedStartDate);
                    calendar.unHighlightDate(selectedEndDate);
                }

                selectedStartDate = calendar.getDate();
                selectedEndDate = null;

                calendar.highlightDate(calendar.getDate(), "dateStart");
            } else {
                selectedEndDate = calendar.getDate();

                calendar.highlightDate(calendar.getDate(), "dateEnd");
            }

            selectedStart = !selectedStart;
        });

        if (currentProduit == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ProduitService.getInstance().add(
                            new Produit(
                                    typeTF.getText(),
                                    typeActionTF.getText(),
                                    disponibiliteTF.getText(),
                                    (int) Float.parseFloat(prixTF.getText()),
                                    selectedStartDate,
                                    selectedEndDate,
                                    selectedImage
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Produit ajouté avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de produit. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = ProduitService.getInstance().edit(
                            new Produit(
                                    currentProduit.getId(),
                                    typeTF.getText(),
                                    typeActionTF.getText(),
                                    disponibiliteTF.getText(),
                                    (int) Float.parseFloat(prixTF.getText()),
                                    selectedStartDate,
                                    selectedEndDate,
                                    selectedImage
                            ),
                            imageEdited
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Produit modifié avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de produit. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        }
    }

    private void showBackAndRefresh() {
        if (isChoose) {
            ((ChooseProduit) previous).refresh();
        } else {
            ((DisplayAll) previous).refresh();
        }
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (typeTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le type", new Command("Ok"));
            return false;
        }

        if (typeActionTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le type d'action", new Command("Ok"));
            return false;
        }

        if (disponibiliteTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir la disponibilite", new Command("Ok"));
            return false;
        }

        if (prixTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le prix", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(prixTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", prixTF.getText() + " n'est pas un prix valide", new Command("Ok"));
            return false;
        }

        if (selectedStartDate == null) {
            Dialog.show("Avertissement", "Veuillez saisir la date d'expo", new Command("Ok"));
            return false;
        }
        if (selectedEndDate == null) {
            Dialog.show("Avertissement", "Veuillez saisir la date de fin", new Command("Ok"));
            return false;
        }
        if (dateIsAfter(selectedEndDate, selectedStartDate)) {
            Dialog.show("Avertissement", "Date de d'expo doit etre superieure a la date de fin", new Command("Ok"));
            return false;
        }

        if (selectedImage == null) {
            Dialog.show("Avertissement", "Veuillez choisir une image", new Command("Ok"));
            return false;
        }
        return true;
    }


    boolean dateIsAfter(Date d1, Date d2) {

        int day1 = (int) Float.parseFloat(new SimpleDateFormat("dd").format(d1));
        int month1 = (int) Float.parseFloat(new SimpleDateFormat("MM").format(d1));
        int year1 = (int) Float.parseFloat(new SimpleDateFormat("yyyy").format(d1));

        int day2 = (int) Float.parseFloat(new SimpleDateFormat("dd").format(d2));
        int month2 = (int) Float.parseFloat(new SimpleDateFormat("MM").format(d2));
        int year2 = (int) Float.parseFloat(new SimpleDateFormat("yyyy").format(d2));

        if (year1 <= year2) {
            if (month1 <= month2) {
                return day1 <= day2;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}