package cn.abelib.tmall.bean;

import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * Created by abel on 2017/11/11.
 */
@Component
public class User implements Serializable {
    private static final long serialVersionUID = -7161230730002779452L;

    private Integer id;
    private String userName;
    private String userPassword;
    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
