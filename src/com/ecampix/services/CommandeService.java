package com.ecampix.services;

import com.codename1.components.InfiniteProgress;
import com.codename1.io.*;
import com.codename1.ui.events.ActionListener;
import com.ecampix.entities.Produit;
import com.ecampix.entities.Commande;
import com.ecampix.utils.Statics;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CommandeService {

    public static CommandeService instance = null;
    public int resultCode;
    private ConnectionRequest cr;
    private ArrayList<Commande> listCommandes;

    private CommandeService() {
        cr = new ConnectionRequest();
    }

    public static CommandeService getInstance() {
        if (instance == null) {
            instance = new CommandeService();
        }
        return instance;
    }

    public ArrayList<Commande> getAll() {
        listCommandes = new ArrayList<>();

        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/commande");
        cr.setHttpMethod("GET");

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {

                if (cr.getResponseCode() == 200) {
                    listCommandes = getList();
                }

                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listCommandes;
    }

    private ArrayList<Commande> getList() {
        try {
            Map<String, Object> parsedJson = new JSONParser().parseJSON(new CharArrayReader(
                    new String(cr.getResponseData()).toCharArray()
            ));
            List<Map<String, Object>> list = (List<Map<String, Object>>) parsedJson.get("root");

            for (Map<String, Object> obj : list) {
                Commande commande = new Commande(
                        (int) Float.parseFloat(obj.get("id").toString()),
                        (int) Float.parseFloat(obj.get("prix").toString()),
                        (int) Float.parseFloat(obj.get("total").toString()),
                        (int) Float.parseFloat(obj.get("numero").toString()),
                        (int) Float.parseFloat(obj.get("idUser").toString()),
                        new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("date")),
                        makeProduit((Map<String, Object>) obj.get("produit"))
                );

                listCommandes.add(commande);
            }
        } catch (IOException | ParseException ex) {
            ex.printStackTrace();
        }
        return listCommandes;
    }

    public Produit makeProduit(Map<String, Object> obj) {
        if (obj == null) {
            return null;
        }

        try {
            return new Produit(
                    (int) Float.parseFloat(obj.get("id").toString()),
                    (String) obj.get("type"),
                    (String) obj.get("typeAction"),
                    (String) obj.get("disponibilite"),
                    (int) Float.parseFloat(obj.get("prix").toString()),
                    new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateDebut")),
                    new SimpleDateFormat("dd-MM-yyyy").parse((String) obj.get("dateFin")),
                    (String) obj.get("image")
            );
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int add(Commande commande) {
        return manage(commande, false);
    }

    public int edit(Commande commande) {
        return manage(commande, true);
    }

    public int manage(Commande commande, boolean isEdit) {
        cr = new ConnectionRequest();
        cr.setHttpMethod("POST");
        if (isEdit) {
            cr.setUrl(Statics.BASE_URL + "/commande/edit");
            cr.addArgument("id", String.valueOf(commande.getId()));
        } else {
            cr.setUrl(Statics.BASE_URL + "/commande/add");
        }

        cr.addArgument("prix", String.valueOf(commande.getPrix()));
        cr.addArgument("total", String.valueOf(commande.getTotal()));
        cr.addArgument("numero", String.valueOf(commande.getNumero()));
        cr.addArgument("idUser", String.valueOf(commande.getIdUser()));
        cr.addArgument("date", new SimpleDateFormat("dd-MM-yyyy").format(commande.getDate()));
        cr.addArgumentNoEncoding("produit", String.valueOf(commande.getProduit().getId()));

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                resultCode = cr.getResponseCode();
                cr.removeResponseListener(this);
            }
        });
        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception ignored) {

        }
        return resultCode;
    }

    public int delete(int commandeId) {
        cr = new ConnectionRequest();
        cr.setUrl(Statics.BASE_URL + "/commande/delete");
        cr.setHttpMethod("POST");
        cr.addArgument("id", String.valueOf(commandeId));

        cr.addResponseListener(new ActionListener<NetworkEvent>() {
            @Override
            public void actionPerformed(NetworkEvent evt) {
                cr.removeResponseListener(this);
            }
        });

        try {
            cr.setDisposeOnCompletion(new InfiniteProgress().showInfiniteBlocking());
            NetworkManager.getInstance().addToQueueAndWait(cr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cr.getResponseCode();
    }
}
