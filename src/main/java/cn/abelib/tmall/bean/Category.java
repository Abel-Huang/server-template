package cn.abelib.tmall.bean;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by abel on 2017/11/4.
 */
@Component
public class Category implements Serializable{
    private static final long serialVersionUID = 3973444640638254166L;

    private Integer id;
    private String categoryName;
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName == null ? null : categoryName.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null :note.trim();
    }
}
