package com.survey.surveys;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
public class UserSurvey {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch= FetchType.EAGER)
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="survey_id")
    private Survey survey;

    @OneToMany(mappedBy = "userSurvey", fetch = FetchType.EAGER)
    private Set<UserSurveyAnswer> userSurveyAnswer = new LinkedHashSet<UserSurveyAnswer>();

    UserSurvey(){

    }

    UserSurvey(User user, Survey survey){
        this.user = user;
        this.survey = survey;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }
}
