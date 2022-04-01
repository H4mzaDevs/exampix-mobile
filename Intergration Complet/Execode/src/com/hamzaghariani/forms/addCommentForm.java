/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hamzaghariani.forms;

import com.codename1.components.FloatingHint;
import com.codename1.l10n.SimpleDateFormat;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Dialog;
import com.codename1.ui.Display;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.spinner.Picker;
import com.codename1.ui.util.Resources;
import com.hamzaghariani.entities.Blog;
import com.hamzaghariani.entities.Comment;
import com.hamzaghariani.myapp.Blogs;
import com.hamzaghariani.services.ServiceBlog;

/**
 *
 * @author MSI
 */
public class addCommentForm extends BaseFormBlogs {
    public addCommentForm(Resources res,int idBlog){
        super(new BorderLayout());
        getTitleArea().setUIID("Container");
        setUIID("SignIn");
        Picker datePicker  = new Picker();
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        datePicker.setFormatter(simpleDateFormat);
        datePicker.setType(Display.PICKER_TYPE_DATE);        
        add(BorderLayout.NORTH, new Label(res.getImage("Logo.png"), "LogoLabel"));
        TextField cmnt = new TextField("", "Commentaire", 40, TextField.ANY);
        Button ajouter = new Button("Ajouter");
        Button annuler = new Button("Annuler");
        annuler.addActionListener(e -> new Blogs(res).show());
        ajouter.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt) {
                Comment c = new Comment(idBlog,datePicker.getText(),"Hamza Ghariani",cmnt.getText());
                if(ServiceBlog.getInstance().addComment(c))
                    Dialog.show("Success", "Ajout Avec Succes !", "OK", null);
                else
                    Dialog.show("Echec", "Probleme lors de l'ajout", "OK", null);
            }
        
        });
        Container content = BoxLayout.encloseY(
                datePicker,
                createLineSeparator(),                
                new FloatingHint(cmnt),
                createLineSeparator(),
                ajouter,
                annuler
              /*  signIn,
                FlowLayout.encloseCenter(doneHaveAnAccount, signUp)*/
        );
        content.setScrollableY(true);
        add(BorderLayout.CENTER, content);
        
    }    
}
