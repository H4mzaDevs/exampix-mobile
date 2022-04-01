/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.hamzaghariani.entities;

/**
 *
 * @author MSI
 */
public class Comment {
    private int idComment;
    private int id_blog;
    private String date_comment;
    private String nom_ut_comment;
    private String cmnt;

    public Comment() {
    }

    public Comment(int id_blog, String date_comment, String nom_ut_comment, String cmnt) {
        this.id_blog = id_blog;
        this.date_comment = date_comment;
        this.nom_ut_comment = nom_ut_comment;
        this.cmnt = cmnt;
    }

    public int getIdComment() {
        return idComment;
    }

    public void setIdComment(int idComment) {
        this.idComment = idComment;
    }

    public int getId_blog() {
        return id_blog;
    }

    public void setId_blog(int id_blog) {
        this.id_blog = id_blog;
    }

    public String getDate_comment() {
        return date_comment;
    }

    public void setDate_comment(String date_comment) {
        this.date_comment = date_comment;
    }

    public String getNom_ut_comment() {
        return nom_ut_comment;
    }

    public void setNom_ut_comment(String nom_ut_comment) {
        this.nom_ut_comment = nom_ut_comment;
    }

    public String getCmnt() {
        return cmnt;
    }

    public void setCmnt(String cmnt) {
        this.cmnt = cmnt;
    }
    
    
}
