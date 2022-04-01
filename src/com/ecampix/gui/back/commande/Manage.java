package com.ecampix.gui.back.commande;

import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.ecampix.entities.Produit;
import com.ecampix.entities.Commande;
import com.ecampix.services.CommandeService;
import com.ecampix.utils.Statics;

import java.util.Date;

public class Manage extends Form {

    public static Produit selectedProduit;
    Commande currentCommande;

    Label prixLabel, totalLabel, numeroLabel, produitLabel, selectedProduitLabel;
    TextField prixTF, totalTF, numeroTF;
    Button selectProduitButton, manageButton;
    PickerComponent datePicker;
    Form previous;

    public Manage(Form previous) {
        super(DisplayAll.currentCommande == null ? "Nouvelle commande" : "Modifier la commande", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        selectedProduit = null;
        currentCommande = DisplayAll.currentCommande;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refreshProduit() {
        selectedProduitLabel.setText(selectedProduit.getType());
        selectProduitButton.setText("Modifier le produit");
        this.refreshTheme();
    }

    private void addGUIs() {

        prixLabel = new Label("Prix : ");
        prixLabel.setUIID("labelDefault");
        prixTF = new TextField();
        prixTF.setHint("Tapez le prix de la commande");

        totalLabel = new Label("Total : ");
        totalLabel.setUIID("labelDefault");
        totalTF = new TextField();
        totalTF.setHint("Tapez le total de la commande");

        numeroLabel = new Label("Numero : ");
        numeroLabel.setUIID("labelDefault");
        numeroTF = new TextField();
        numeroTF.setHint("Tapez le numero de la commande");

        datePicker = PickerComponent.createDate(null).label("Date");

        if (currentCommande == null) {
            selectedProduit = null;
            manageButton = new Button("Ajouter");
        } else {
            prixTF.setText(String.valueOf(currentCommande.getPrix()));
            totalTF.setText(String.valueOf(currentCommande.getTotal()));
            numeroTF.setText(String.valueOf(currentCommande.getNumero()));
            datePicker.getPicker().setDate(currentCommande.getDate());

            selectedProduit = currentCommande.getProduit();

            manageButton = new Button("Modifier");
        }
        manageButton.setUIID("buttonMain");

        produitLabel = new Label("Produit : ");
        produitLabel.setUIID("labelDefault");
        if (selectedProduit != null) {
            selectedProduitLabel = new Label(selectedProduit.getType());
            selectProduitButton = new Button("Modifier le produit");
        } else {
            selectedProduitLabel = new Label("Aucun produit selectionné");
            selectProduitButton = new Button("Choisir un produit");
        }

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                prixLabel, prixTF,
                totalLabel, totalTF,
                numeroLabel, numeroTF,
                datePicker,
                produitLabel, selectedProduitLabel, selectProduitButton,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        selectProduitButton.addActionListener(l -> new ChooseProduit(this).show());

        if (currentCommande == null) {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CommandeService.getInstance().add(
                            new Commande(
                                    (int) Float.parseFloat(prixTF.getText()),
                                    (int) Float.parseFloat(totalTF.getText()),
                                    (int) Float.parseFloat(numeroTF.getText()),
                                    0,
                                    datePicker.getPicker().getDate(),
                                    selectedProduit
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Commande ajouté avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur d'ajout de commande. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        } else {
            manageButton.addActionListener(action -> {
                if (controleDeSaisie()) {
                    int responseCode = CommandeService.getInstance().edit(
                            new Commande(
                                    currentCommande.getId(),
                                    (int) Float.parseFloat(prixTF.getText()),
                                    (int) Float.parseFloat(totalTF.getText()),
                                    (int) Float.parseFloat(numeroTF.getText()),
                                    0,
                                    datePicker.getPicker().getDate(),
                                    selectedProduit
                            )
                    );
                    if (responseCode == 200) {
                        Dialog.show("Succés", "Commande modifié avec succes", new Command("Ok"));
                    } else {
                        Dialog.show("Erreur", "Erreur de modification de commande. Code d'erreur : " + responseCode, new Command("Ok"));
                    }

                    showBackAndRefresh();
                }
            });
        }
    }

    private void showBackAndRefresh() {
        ((DisplayAll) previous).refresh();
        previous.showBack();
    }

    private boolean controleDeSaisie() {

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

        if (totalTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le total", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(totalTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", totalTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        if (numeroTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le numero", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(numeroTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", numeroTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
            return false;
        }

        if (datePicker.getPicker().getDate() == null) {
            Dialog.show("Avertissement", "Veuillez saisir la date", new Command("Ok"));
            return false;
        }

        if (selectedProduit == null) {
            Dialog.show("Avertissement", "Veuillez choisir un produit", new Command("Ok"));
            return false;
        }

        return true;
    }
}