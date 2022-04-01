/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hamzaghariani.services;

import com.codename1.io.CharArrayReader;
import com.codename1.io.ConnectionRequest;
import com.codename1.io.JSONParser;
import com.codename1.io.NetworkEvent;
import com.codename1.io.NetworkManager;
import com.codename1.ui.events.ActionListener;
import com.hamzaghariani.entities.Blog;
import com.hamzaghariani.entities.Comment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author MSI
 */
public class ServiceBlog {
    public ConnectionRequest req;
    private static ServiceBlog instance;
    boolean resultOk;
    ArrayList<Blog> blogs;
    ArrayList<Comment> comments;
    
    
    
    private ServiceBlog(){
        req = new ConnectionRequest();
    }
    public static ServiceBlog getInstance(){
        if(instance == null)
            instance = new ServiceBlog();
        return instance;
    }
    
    
    public boolean addBlog(Blog b){
        String arg = b.getDate_blog()+"&"+b.getNom_ut_blog()+"&"+b.getTitre_blog()+"&"+b.getDesc_blog()+"&"+
                b.getImage_blog()+"&"+b.getLikes_blog();
        
        String url = "http://localhost:8081/addBlog/"+arg;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>(){
            @Override
            public void actionPerformed(NetworkEvent evt) {
                if (req.getResponseCode()==200)
                     resultOk = true;
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueue(req);
        return resultOk;
    }
    
    
    public boolean addComment(Comment c){
        String arg = c.getId_blog()+"&"+c.getDate_comment()+"&"+c.getNom_ut_comment()+"&"+c.getCmnt();
        String url = "http://localhost:8081/addComment/"+arg;
        req.setUrl(url);
        req.setPost(false);
        req.addResponseListener(new ActionListener<NetworkEvent>(){
            @Override
            public void actionPerformed(NetworkEvent evt) {
                if (req.getResponseCode()==200)
                     resultOk = true;
                req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueue(req);
        return resultOk;
    }    
    
    public ArrayList<Blog> getAllBlogs(){
       // ArrayList<Blog> blogs = new ArrayList<>();
        String url = "http://localhost:8081/Blogs";
        req.setPost(false);
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>(){
            @Override
            public void actionPerformed(NetworkEvent evt) {
             blogs = parseBlog(new String(req.getResponseData()));
             req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueue(req);
        return blogs;
    }
    
    
     public ArrayList<Comment> getAllComments(int id){
       // ArrayList<Blog> blogs = new ArrayList<>();
        String url = "http://localhost:8081/Comments/"+id;
        req.setPost(false);
        req.setUrl(url);
        req.addResponseListener(new ActionListener<NetworkEvent>(){
            @Override
            public void actionPerformed(NetworkEvent evt) {
             comments = parseComment(new String(req.getResponseData()));
             req.removeResponseListener(this);
            }
        });
        NetworkManager.getInstance().addToQueue(req);
        return comments;
    }   
    
    private ArrayList<Blog> parseBlog(String jsonText){
        try{
            blogs = new ArrayList<Blog>();
            JSONParser j = new JSONParser();
            Map<String,Object> blogsListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list =  (List<Map<String, Object>>) blogsListJson.get("root");
            for(Map<String, Object> obj : list){
                float idBlog = Float.parseFloat(obj.get("id").toString());
                String date_blog = obj.get("date_blog").toString();
                String nom_ut_blog = obj.get("nom_ut_blog").toString();
                String titre_blog = obj.get("titre_blog").toString();
                String desc_blog = obj.get("desc_blog").toString();
                String image_blog = obj.get("image_blog").toString();
                int likes_blog = 0;
                Blog b = new Blog((int)idBlog,date_blog,nom_ut_blog,titre_blog,desc_blog,image_blog,likes_blog);
                blogs.add(b);
                }   }
        catch(IOException e){
            System.out.print(e.getMessage());
        }
        return blogs;
    }
    
     private ArrayList<Comment> parseComment(String jsonText){
        try{
            comments = new ArrayList<Comment>();
            JSONParser j = new JSONParser();
            Map<String,Object> blogsListJson = j.parseJSON(new CharArrayReader(jsonText.toCharArray()));
            List<Map<String, Object>> list =  (List<Map<String, Object>>) blogsListJson.get("root");
            for(Map<String, Object> obj : list){
                float idBlog = Float.parseFloat(obj.get("id").toString());
                String date_comment = obj.get("date_comment").toString();
                String nom_ut_comment = obj.get("nom_ut_comment").toString();
                String cmnt = obj.get("cmnt").toString();
                Comment c = new Comment((int)idBlog,date_comment,nom_ut_comment,cmnt);
                comments.add(c);
                }   }
        catch(IOException e){
            System.out.print(e.getMessage());
        }
        return comments;
    }   
    
    
}
