package com.ecampix.gui.front.commande;

import com.codename1.components.ToastBar;
import com.codename1.l10n.ParseException;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BoxLayout;
import com.ecampix.entities.Commande;
import com.ecampix.entities.Produit;
import com.ecampix.services.CommandeService;

public class Manage extends Form {

    Produit selectedProduit;
    Form previous;
    int prixTotal;

    Label typeLabel, prixLabel, nombreLabel, totalLabel;
    Button manageButton;
    TextField nombreTF;
    PickerComponent datePicker;


    public Manage(Form previous, Produit selectedProduit) {
        super("Nouvelle commande", new BoxLayout(BoxLayout.Y_AXIS));
        this.previous = previous;

        this.selectedProduit = selectedProduit;

        addGUIs();
        addActions();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    private void addGUIs() {

        prixTotal = selectedProduit.getPrix();

        typeLabel = new Label("Produit : " + selectedProduit.getType());
        typeLabel.setUIID("labelDefault");

        prixLabel = new Label("Prix : " + selectedProduit.getPrix() + " D");
        prixLabel.setUIID("labelDefault");

        nombreLabel = new Label("Nombre : ");
        nombreLabel.setUIID("labelDefault");

        nombreTF = new TextField("");
        nombreTF.addDataChangedListener((d, i) -> {
            try {
                prixTotal = (int) (selectedProduit.getPrix() * Float.parseFloat(nombreTF.getText()));
                totalLabel.setText("Total : " + prixTotal + "D");
            } catch (NumberFormatException e) {
                totalLabel.setText("Tapez un nombre valide");
            }
            this.revalidate();
        });

        totalLabel = new Label("Total : ");
        totalLabel.setUIID("labelDefault");

        datePicker = PickerComponent.createDate(null).label("Date : ");

        manageButton = new Button("Commander");
        manageButton.setUIID("buttonMain");

        Container container = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        container.setUIID("containerRounded");

        container.addAll(
                typeLabel, prixLabel, nombreLabel, nombreTF, totalLabel, datePicker,
                manageButton
        );

        this.addAll(container);
    }

    private void addActions() {
        manageButton.addActionListener(action -> {
            if (controleDeSaisie()) {
                int responseCode = CommandeService.getInstance().add(
                        new Commande(
                                selectedProduit.getPrix(),
                                (int) Float.parseFloat(nombreTF.getText()),
                                prixTotal,
                                0,
                                datePicker.getPicker().getDate(),
                                selectedProduit
                        )
                );
                if (responseCode == 200) {
                    Dialog.show("Succés", "", new Command("Retour"));
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    ToastBar.getInstance().setPosition(TOP);
                                    ToastBar.Status status = ToastBar.getInstance().createStatus();
                                    status.setShowProgressIndicator(false);
                                    status.setMessage("Produit commandé avec succes");
                                    status.setExpires(5000);
                                    status.show();
                                }
                            },
                            2000
                    );
                } else {
                    Dialog.show("Erreur", "Erreur d'ajout de commande. Code d'erreur : " + responseCode, new Command("Ok"));
                }

                showBackAndRefresh();
            }
        });
    }

    private void showBackAndRefresh() {
        previous.showBack();
    }

    private boolean controleDeSaisie() {

        if (nombreTF.getText().equals("")) {
            Dialog.show("Avertissement", "Veuillez saisir le nombre", new Command("Ok"));
            return false;
        }
        try {
            Float.parseFloat(nombreTF.getText());
        } catch (NumberFormatException e) {
            Dialog.show("Avertissement", nombreTF.getText() + " n'est pas un nombre valide", new Command("Ok"));
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