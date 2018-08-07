package com.survey.surveys;

import javax.persistence.*;
import java.util.*;

@Entity
public class Question {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String question;

    @OneToMany(mappedBy = "question",fetch = FetchType.EAGER)
    private Set<QuestionAnswer> questionAnswers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private List<SurveyQuestion> surveyQuestions = new ArrayList<>();

    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    private List<UserSurveyAnswer> userSurveyAnswers = new ArrayList<UserSurveyAnswer>();



    public Question(){

    }

    public Question( String question){
        this.question = question;
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

    public Set<QuestionAnswer> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(Set<QuestionAnswer> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public List<SurveyQuestion> getSurveyQuestions() {
        return surveyQuestions;
    }

    public void setUserSurveyAnswers(List<UserSurveyAnswer> userSurveyAnswers) {
        this.userSurveyAnswers = userSurveyAnswers;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
