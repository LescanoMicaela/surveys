package com.survey.surveys;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String question;
//    private String type;

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private Set<SurveyQuestion> surveyQuestions = new HashSet<SurveyQuestion>();

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private Set<UserSurveyAnswer> userSurveyAnswer = new HashSet<UserSurveyAnswer>();



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

    public Set<SurveyQuestion> getSurveyQuestions() {
        return surveyQuestions;
    }

    public void setSurveyQuestions(Set<SurveyQuestion> surveyQuestions) {
        this.surveyQuestions = surveyQuestions;
    }

    public Set<UserSurveyAnswer> getUserSurveyAnswer() {
        return userSurveyAnswer;
    }

    public void setUserSurveyAnswer(Set<UserSurveyAnswer> userSurveyAnswer) {
        this.userSurveyAnswer = userSurveyAnswer;
    }
}
