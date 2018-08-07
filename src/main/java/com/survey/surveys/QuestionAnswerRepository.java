package com.survey.surveys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {
}
