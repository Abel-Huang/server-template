package cn.abelib.tmall.bean;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by abel on 2017/11/6.
 */
@Component
public class Property implements Serializable {
    private static final long serialVersionUID = -4201332505872302186L;

    private Integer id;
    private Integer categoryId;
    private String propertyName;
    private String note;

    private Category category;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName == null ? null : propertyName.trim();
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null :note.trim();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
