package com.survey.surveys;

import javax.persistence.*;
import java.util.*;

@Entity
public class Survey {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String description;

    @OneToMany(mappedBy = "survey", fetch = FetchType.EAGER)
    private Set<SurveyQuestion> surveyQuestions = new LinkedHashSet<>();

    @OneToMany(mappedBy = "survey", fetch = FetchType.EAGER)
    private List<UserSurvey> userSurveys = new ArrayList<UserSurvey>();

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


    public List<UserSurvey> getUserSurveys() {
        return userSurveys;
    }

    public void setUserSurveys(List<UserSurvey> userSurveys) {
        this.userSurveys = userSurveys;
    }

    public Set<SurveyQuestion> getSurveyQuestions() {
        return surveyQuestions;
    }

    public void setSurveyQuestions(LinkedHashSet<SurveyQuestion> surveyQuestions) {
        this.surveyQuestions = surveyQuestions;
    }

    public void addQuestion(SurveyQuestion surveyQuestion) {
        surveyQuestion.setSurvey(this);
        surveyQuestions.add(surveyQuestion);

    }
}
