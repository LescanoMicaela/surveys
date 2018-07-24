package com.survey.surveys;

import javax.persistence.*;

@Entity
public class UserSurveyAnswer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    Long id;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="userSurvey_id")
    private UserSurvey userSurvey;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="question_id")
    private Question question;

    String answer;

    public UserSurveyAnswer(){

    }

    public UserSurveyAnswer(String answer){

        this.answer = answer;

    }

    public Long getId() {
        return id;
    }



    public UserSurvey getUserSurvey() {
        return userSurvey;
    }

    public void setUserSurvey(UserSurvey userSurvey) {
        this.userSurvey = userSurvey;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
