package com.inzaana.pos.models;

import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;
import com.inzaana.pos.utils.Pair;

@XmlRootElement
public class Category extends DataModel {

    final static public String ID = "ID";
    final static public String USER_ID = "USER_ID";
    final static public String NAME = "NAME";
    final static public String PARENTID = "PARENTID";
    final static public String IMAGE = "IMAGE";
    final static public String TEXTTIP = "TEXTTIP";
    final static public String CATSHOWNAME = "CATSHOWNAME";

    private String id;
    private String userId;
    private String name;
    private String parentId;
    private String image;
    private String textTip;
    private boolean catShowName;

    private ArrayList<Pair<String, String>> categoryTableStringList;
    private ArrayList<Pair<String, Boolean>> categoryTableBooleanList;

    public Category() {
        categoryTableStringList = new ArrayList<Pair<String, String>>();
        categoryTableBooleanList = new ArrayList<Pair<String, Boolean>>();
    }

    /**
     * @param id
     * @param name
     * @param parentId
     * @param image
     * @param textTip
     * @param catShowName
     */
    public Category(String id, String name, String parentId, String image, String textTip, boolean catShowName) {
        categoryTableStringList = new ArrayList<Pair<String, String>>();
        categoryTableBooleanList = new ArrayList<Pair<String, Boolean>>();

        this.setId(id);
        this.setName(name);
        this.setParentId(parentId);
        this.setImage(image);
        this.setTextTip(textTip);
        this.setCatShowName(catShowName);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        categoryTableStringList.add(new Pair<String, String>(ID, id));
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        categoryTableStringList.add(new Pair<String, String>(NAME, name));
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
        categoryTableStringList.add(new Pair<String, String>(PARENTID, parentId));
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
        categoryTableStringList.add(new Pair<String, String>(IMAGE, image));
    }

    public String getTextTip() {
        return textTip;
    }

    public void setTextTip(String textTip) {
        this.textTip = textTip;
        categoryTableStringList.add(new Pair<String, String>(TEXTTIP, textTip));
    }

    public boolean getCatShowName() {
        return catShowName;
    }

    public void setCatShowName(boolean catShowName) {
        this.catShowName = catShowName;
        categoryTableBooleanList.add(new Pair<String, Boolean>(CATSHOWNAME, catShowName));
    }

    @Override
    public boolean insertRecordIntoDB(String userID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateRecordInDB(String userID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
