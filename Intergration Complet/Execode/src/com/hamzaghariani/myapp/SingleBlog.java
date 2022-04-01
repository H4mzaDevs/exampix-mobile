/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hamzaghariani.myapp;

import com.codename1.components.ScaleImageLabel;
import com.codename1.components.SpanLabel;
import com.codename1.components.ToastBar;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Component;
import static com.codename1.ui.Component.RIGHT;
import com.codename1.ui.Container;
import com.codename1.ui.Display;
import com.codename1.ui.FontImage;
import com.codename1.ui.Image;
import com.codename1.ui.Label;
import com.codename1.ui.TextArea;
import com.codename1.ui.TextField;
import com.codename1.ui.Toolbar;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.layouts.GridLayout;
import com.codename1.ui.layouts.LayeredLayout;
import com.codename1.ui.plaf.Style;
import com.codename1.ui.util.Resources;
import com.hamzaghariani.entities.Blog;
import com.hamzaghariani.entities.Comment;
import com.hamzaghariani.forms.BaseFormBlogs;
import com.hamzaghariani.forms.addCommentForm;
import com.hamzaghariani.services.ServiceBlog;
import java.util.ArrayList;

/**
 *
 * @author MSI
 */
public class SingleBlog extends BaseFormBlogs {
    public int idBlog;

    public SingleBlog(int idBlog,String title,Resources res,String desc,String dateb,Image imgblog) {
        super("Newsfeed", BoxLayout.y());
        this.idBlog = idBlog;
        Toolbar tb = new Toolbar(true);
        setToolbar(tb);
        getTitleArea().setUIID("Container");
        setTitle(title);
        getContentPane().setScrollVisible(false);
        
        super.addSideMenu(res);
        
        tb.addSearchCommand(e -> {});
        
        
        //Image img = res.getImage("profile-background.jpg");
        if(imgblog.getHeight() > Display.getInstance().getDisplayHeight() / 3) {
            imgblog = imgblog.scaledHeight(Display.getInstance().getDisplayHeight() / 3);
        }
        ScaleImageLabel sl = new ScaleImageLabel(imgblog);
        sl.setUIID("BottomPad");
        sl.setBackgroundType(Style.BACKGROUND_IMAGE_SCALED_FILL);

        Label myDate = new Label(dateb, res.getImage("facebook-logo.png"), "BottomPad");
        //Label twitter = new Label("486 followers", res.getImage("twitter-logo.png"), "BottomPad");
        myDate.setTextPosition(BOTTOM);

        
        add(LayeredLayout.encloseIn(
                sl,
                BorderLayout.south(
                    GridLayout.encloseIn(3, 
                            myDate,
                            FlowLayout.encloseCenter(
                                new Label(res.getImage("profile-pic.jpg"), "PictureWhiteBackgrond"))
                            
                    )
                )
        ));

        Label titre = new Label(title);
        titre.setUIID("TextFieldBlack");
        add(titre);
        SpanLabel d = new SpanLabel(desc);
        d.setUIID("SpanLabel");
        add(d);
        Button ajouterCommentaire = new Button("Ajouter un commentaire");
        ajouterCommentaire.addActionListener(e -> new addCommentForm(res,idBlog).show());
        add(ajouterCommentaire);
      

        ArrayList<Comment> comments ;
        comments = ServiceBlog.getInstance().getAllComments(idBlog);
       // Comment nc = new Comment(idBlog,"No Comment","No Comment","No Comment");
       // comments.add(nc);
        for(Comment c : comments){
                addButton(res,res.getImage("comment.jpg"), c.getNom_ut_comment(),c.getCmnt(),c.getDate_comment());
        }         
        
    }
    
    private void addStringValue(String s, Component v) {
        add(BorderLayout.west(new Label(s, "PaddedLabel")).
                add(BorderLayout.CENTER, v));
        add(createLineSeparator(0xeeeeee));
            
    }
    
       private void addButton(Resources res,Image img, String nomUt , String comment, String dateCmnt) {
       int height = Display.getInstance().convertToPixels(11.5f);
       int width = Display.getInstance().convertToPixels(14f);
       Button image = new Button(img.fill(width, height));
       image.setUIID("Label");
       Container cnt = BorderLayout.west(image);
       cnt.setLeadComponent(image);
       TextArea ta = new TextArea(nomUt+"   "+dateCmnt);
       Label com = new Label(comment);
       
       
       ta.setUIID("NewsTopLine");
       ta.setEditable(false);
       
       
       cnt.add(BorderLayout.CENTER, 
               BoxLayout.encloseY(
                       ta,
                       com
               ));
       add(cnt);
       image.addActionListener(e -> ToastBar.showMessage(comment, FontImage.MATERIAL_INFO));
   }
    
}
