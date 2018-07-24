package com.survey.surveys;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Survey {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String description;

    @OneToMany(mappedBy = "survey", fetch = FetchType.EAGER)
    private Set<SurveyQuestion> surveyQuestions = new HashSet<SurveyQuestion>();

    @OneToMany(mappedBy = "survey", fetch = FetchType.EAGER)
    private Set<UserSurvey> userSurveys = new HashSet<UserSurvey>();

    public Survey(){

    }

    public Survey(String description){
        this.description = description;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Set<UserSurvey> getUserSurveys() {
        return userSurveys;
    }

    public void setUserSurveys(Set<UserSurvey> userSurveys) {
        this.userSurveys = userSurveys;
    }
}
