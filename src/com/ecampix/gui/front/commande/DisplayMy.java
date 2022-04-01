package com.ecampix.gui.front.commande;

import com.codename1.components.InteractionDialog;
import com.codename1.ui.*;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.plaf.UIManager;
import com.codename1.ui.util.Resources;
import com.ecampix.entities.Commande;
import com.ecampix.services.CommandeService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayMy extends Form {

    public static Commande currentCommande = null;

    public DisplayMy(Form previous) {
        super("Commandes", new BoxLayout(BoxLayout.Y_AXIS));

        addGUIs();

        getToolbar().addMaterialCommandToLeftBar("  ", FontImage.MATERIAL_ARROW_BACK, e -> previous.showBack());
    }

    public void refresh() {
        this.removeAll();
        addGUIs();
        this.refreshTheme();
    }

    private void addGUIs() {
        ArrayList<Commande> listCommandes = CommandeService.getInstance().getAll();
        if (listCommandes.size() > 0) {
            for (Commande listCommande : listCommandes) {
                this.add(makeCommandeModel(listCommande));
            }
        } else {
            this.add(new Label("Aucune donnée"));
        }
    }

    Label prixLabel, totalLabel, numeroLabel, dateLabel, produitLabel;
    Button deleteBtn;
    Container btnsContainer;

    private Component makeCommandeModel(Commande commande) {
        Container commandeModel = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        commandeModel.setUIID("containerRounded");

        prixLabel = new Label("Prix : " + commande.getPrix());
        prixLabel.setUIID("labelDefault");

        totalLabel = new Label("Total : " + commande.getTotal());
        totalLabel.setUIID("labelDefault");

        numeroLabel = new Label("Numero : " + commande.getNumero());
        numeroLabel.setUIID("labelDefault");

        dateLabel = new Label("Date : " + new SimpleDateFormat("dd-MM-yyyy").format(commande.getDate()));
        dateLabel.setUIID("labelDefault");

        produitLabel = new Label("Produit : " + commande.getProduit().getType());
        produitLabel.setUIID("labelDefault");

        btnsContainer = new Container(new BorderLayout());
        btnsContainer.setUIID("containerButtons");

        deleteBtn = new Button("Supprimer");
        deleteBtn.setUIID("buttonDanger");
        deleteBtn.addActionListener(action -> {
            InteractionDialog dlg = new InteractionDialog("Confirmer la suppression");
            dlg.setLayout(new BorderLayout());
            dlg.add(BorderLayout.CENTER, new Label("Voulez vous vraiment supprimer ce commande ?"));
            Button btnClose = new Button("Annuler");
            btnClose.addActionListener((ee) -> dlg.dispose());
            Button btnConfirm = new Button("Confirmer");
            btnConfirm.addActionListener(actionConf -> {
                int responseCode = CommandeService.getInstance().delete(commande.getId());

                if (responseCode == 200) {
                    currentCommande = null;
                    dlg.dispose();
                    commandeModel.remove();
                    this.refreshTheme();
                } else {
                    Dialog.show("Erreur", "Erreur de suppression de la commande. Code d'erreur : " + responseCode, new Command("Ok"));
                }
            });
            Container btnContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
            btnContainer.addAll(btnClose, btnConfirm);
            dlg.addComponent(BorderLayout.SOUTH, btnContainer);
            dlg.show(800, 800, 0, 0);
        });

        btnsContainer.add(BorderLayout.CENTER, deleteBtn);

        commandeModel.addAll(
                produitLabel,
                prixLabel, totalLabel, numeroLabel, dateLabel,
                btnsContainer
        );

        return commandeModel;
    }
}