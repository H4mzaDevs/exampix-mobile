/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hamzaghariani.forms;

import com.codename1.components.FloatingHint;
import com.codename1.ui.Button;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.TextField;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.util.Resources;

/**
 *
 * @author MSI
 */
public class formExample extends baseForm {

    public formExample(Resources res) {
        super(new BorderLayout());
        add(BorderLayout.NORTH, new Label(res.getImage("Logo.png"), "LogoLabel"));
        TextField titre_Blog = new TextField("", "Titre", 20, TextField.ANY);
        TextField desc_Blog = new TextField("", "Description", 40, TextField.ANY);
        TextField image_Blog = new TextField("", "Image", 20, TextField.ANY);
        Button ajouter = new Button("Ajouter");
        Button annuler = new Button("Annuler");
                Container content = BoxLayout.encloseY(
                createLineSeparator(),                
                new FloatingHint(titre_Blog),
                createLineSeparator(),
                new FloatingHint(desc_Blog),
                createLineSeparator(),
                new FloatingHint(image_Blog),
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
