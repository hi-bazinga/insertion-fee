package com.zxczone.insertionfee.pojo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Category of products
 * 
 * @author Jason Zhao
 * @date Mar 18, 2017
 */
public class Category {

    private int id;

    private String name;

    private Integer price;

    @JsonIgnore
    private Integer parentId;

    @JsonIgnore
    private Integer firstChildId;

    @JsonIgnore
    private Integer nextSiblingId;

    private int indent;

    private List<Category> children = new ArrayList<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getFirstChildId() {
        return firstChildId;
    }

    public void setFirstChildId(Integer firstChildId) {
        this.firstChildId = firstChildId;
    }

    public Integer getNextSiblingId() {
        return nextSiblingId;
    }

    public void setNextSiblingId(Integer nextSiblingId) {
        this.nextSiblingId = nextSiblingId;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public int getIndent() {
        return indent;
    }

    public void setIndent(int indent) {
        this.indent = indent;
    }

    public String toString() {
        StringBuilder childrenSb = new StringBuilder();
        StringBuilder indentSb = new StringBuilder();
        for (Category child : children) {
            while (indentSb.length() < child.indent) { indentSb.append(" "); }
            childrenSb.append(indentSb).append(child.toString()).append("\n");
        };
        return String.format(
                "Id: %d, Name: %s, Price: %s %s", id, name, price,
                childrenSb.length() > 0 ? "\n" + childrenSb.toString().substring(0, childrenSb.length() - 2) : "");
        
//        return String.format(
//                "Id: %d, Name: %s, Price: %s, Parent Id: %s, First Child: %s, Next Sibling: %s %s", 
//                id, name, price, parentId, firstChildId, nextSiblingId, 
//                childrenSb.length() > 0 ? "\n" + childrenSb.toString().substring(0, childrenSb.length() - 2) : "");
    }
    
    public String toTreeHtml() {
        return toString().replaceAll("\n", "<br>").replaceAll("  ", "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
    }

}
