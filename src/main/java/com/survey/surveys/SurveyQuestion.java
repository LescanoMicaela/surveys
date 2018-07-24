package com.survey.surveys;

import javax.persistence.*;

@Entity
public class SurveyQuestion {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="survey_id")
    private Survey survey;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="question_id")
    private Question question;

    public SurveyQuestion(){

    }

    public SurveyQuestion(Survey survey, Question question){
        this.survey = survey;
        this.question = question;

    }


    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
