package com.survey.surveys;

import javax.persistence.*;
import java.util.*;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String question;
//    private String type;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private List<SurveyQuestion> surveyQuestions = new ArrayList<>();

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private List<UserSurveyAnswer> userSurveyAnswers = new ArrayList<UserSurveyAnswer>();



    public Question(){

    }

    public Question( String question){
        this.question = question;
//        this.type = answer;

    }

    public Long getId() {
        return id;
    }



    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }

//    public Set<SurveyQuestion> getSurveyQuestions() {
//        return surveyQuestions;
//    }

    public void setSurveyQuestions(List<SurveyQuestion> surveyQuestions) {
        this.surveyQuestions = surveyQuestions;
    }

    public List<UserSurveyAnswer> getUserSurveyAnswers() {
        return userSurveyAnswers;
    }

    public UserSurveyAnswer getUserSurveyAnswer(User user) {
        return userSurveyAnswers.stream().filter(usa ->usa.getUserSurvey().getUser().equals(user)).findFirst().orElse(null);
    }

    public void setUserSurveyAnswer(List<UserSurveyAnswer> userSurveyAnswer) {
        this.userSurveyAnswers = userSurveyAnswer;
    }


}
