package com.survey.surveys;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    private String userName;
    private String email;
    private String password;

    @OneToMany(mappedBy="user", fetch=FetchType.EAGER)
    Set<UserSurvey> userSurveys = new LinkedHashSet<>();

    public User() {

    }

    public User(String username, String password, String email){
        this.userName = username;
        this.password = password;
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserSurvey> getUserSurveys() {
        return userSurveys;
    }

    public void setUserSurveys(Set<UserSurvey> userSurveys) {
        this.userSurveys = userSurveys;
    }
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

}
