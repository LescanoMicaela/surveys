package com.survey.surveys;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class SurveyController {

    @Autowired
    UserRepository userRepo;
    @Autowired
    UserSurveyRepository userSurveyRepo;
    @Autowired
    UserSurveyAnswerRepository userSurveyAnswerRepo;
    @Autowired
    SurveyQuestionRepository surveyQuestionRepo;
    @Autowired
    NotificationService notificationService;
    @Autowired
    SurveyRepository surveyRepo;

    @RequestMapping("/user_info")
    public Map<String,Object> makeSurveyDTO(Authentication auth) {
        Map<String,Object> dto = new LinkedHashMap<>();
        if (auth !=null) {
            dto.put("currentUser",  makeUserDTO(auth));
        }else{
            dto.put("currentUser", null);
        }
        return dto;
    }


    public Map<String,Object> makeUserDTO(Authentication authentication) {
        Map<String, Object> userDTO = new LinkedHashMap<>();
        User user = currentUser(authentication);
        List<UserSurvey> userSurveys = user.getUserSurveys();
        List<Object> answersInSurvey = userSurveys.stream()
                                                    .filter(us -> us.getUserSurveyAnswers().size() > 0)
                                                    .collect(Collectors.toList());
        userDTO.put("id", user.getId());
        userDTO.put("email", user.getEmail());
        userDTO.put("name", user.getUserName());
        if ( answersInSurvey.size() > 0){
            userDTO.put("anseredSurvey", true );
            userDTO.put("UserSurveyID", userSurveys.get(0).getId());
        }else{
            userDTO.put("anseredSurvey", false );
        }
        return userDTO;
    }



    public Map<String,Object> makeUserSurveyDTO(Authentication auth){
        Map<String, Object> surveyDTO = new LinkedHashMap<>();
        //We just have one survey with id = 1;
        Survey survey = surveyRepo.findOne(Long.valueOf(1));
        Set<SurveyQuestion> surveyQuestions = new LinkedHashSet<>();
        surveyQuestions = survey.getSurveyQuestions();

        surveyDTO.put("description", survey.getDescription());
        surveyDTO.put("QnA", surveyQuestions.stream().map(sq -> makeQuestionandAnsweDTO( sq, auth)).collect(toList()));
        surveyDTO.put("length", surveyQuestions.size());
        return surveyDTO;
    }

    public Map<String,Object> makeUserSurveyAnswered(Authentication auth){
        Map<String, Object> surveyDTO = new LinkedHashMap<>();
        //We just have one survey with id = 1;
        Survey survey = surveyRepo.findOne(Long.valueOf(1));
        Set<SurveyQuestion> surveyQuestions = new LinkedHashSet<>();
        surveyQuestions = survey.getSurveyQuestions();

        surveyDTO.put("description", survey.getDescription());
        surveyDTO.put("QnA", surveyQuestions.stream().map(sq -> makeQuestionAnswered( sq, auth)).collect(toList()));
        surveyDTO.put("length", surveyQuestions.size());
        return surveyDTO;
    }
   

    public Map<String,Object> makeQuestionAnswered( SurveyQuestion surveyQuestion, Authentication auth){
        Map<String, Object> questionAnswerDTO = new LinkedHashMap<>();
        Question question= surveyQuestion.getQuestion();
        User user= currentUser(auth);
        UserSurveyAnswer userSurveyAnswer = question.getUserSurveyAnswer(user);

        questionAnswerDTO.put("id",surveyQuestion.getId());
        questionAnswerDTO.put("question", question.getQuestion());
        if (userSurveyAnswer != null){
            questionAnswerDTO.put("answer", userSurveyAnswer.getAnswer());
        } else{
            questionAnswerDTO.put("answer", null);
        }
        return questionAnswerDTO;
    }


    public Map<String,Object> makeQuestionandAnsweDTO( SurveyQuestion surveyQuestion, Authentication auth){
        Map<String, Object> questionAnswerDTO = new LinkedHashMap<>();
        Question question= surveyQuestion.getQuestion();
        User user= currentUser(auth);
        List<String> answerQuestions = question.getQuestionAnswers().stream()
                                                .map(aq -> aq.getAnswer()).collect(toList());
        UserSurveyAnswer userSurveyAnswer = question.getUserSurveyAnswer(user);

        questionAnswerDTO.put("id",surveyQuestion.getId());
        questionAnswerDTO.put("question", question.getQuestion());
        questionAnswerDTO.put("answer", answerQuestions.stream().map(aq -> makeAnswersDTO(aq)).collect(toList()));

        return questionAnswerDTO;
    }

    public Map<String,Object> makeAnswersDTO( String answer){
        Map<String, Object> answersDTO = new LinkedHashMap<>();
        answersDTO.put("option", answer);
        return answersDTO;
    }


    @RequestMapping("/user_survey_view/{usID}")
    public ResponseEntity<Map<String, Object>> userSurveyView(@PathVariable Long usID, Authentication authentication) {
        UserSurvey userSurvey = userSurveyRepo.findOne(usID);
        if (authentication == null) {
            return new ResponseEntity<>(makeMap("error", "You must log in to answer this survey"), HttpStatus.UNAUTHORIZED);
        } else {
            if (userSurvey.getUser().getId() != currentUser(authentication).getId()) {
                return new ResponseEntity<>(makeMap("error", "This survey is not for you"), HttpStatus.UNAUTHORIZED);

            } else {
                return new ResponseEntity<>( getUserSurveyDTO(userSurvey,authentication), HttpStatus.OK);
            }
        }
    }

    @RequestMapping("/user_Answeredsurvey_view/{usID}")
    public ResponseEntity<Map<String, Object>> userSurveyView2(@PathVariable Long usID, Authentication authentication) {
        UserSurvey userSurvey = userSurveyRepo.findOne(usID);
        if (authentication == null) {
            return new ResponseEntity<>(makeMap("error", "You must log in to answer this survey"), HttpStatus.UNAUTHORIZED);
        } else {
            if (userSurvey.getUser().getId() != currentUser(authentication).getId()) {
                return new ResponseEntity<>(makeMap("error", "This survey is not for you"), HttpStatus.UNAUTHORIZED);

            } else {
                return new ResponseEntity<>( getUserSurveyAnswered(userSurvey,authentication), HttpStatus.OK);
            }
        }
    }

    public Map<String,Object> getUserSurveyDTO (UserSurvey userSurvey, Authentication authentication){
        Map<String, Object> usersurveyDTO = new LinkedHashMap<>();
        usersurveyDTO.put("u-survey-id", userSurvey.getId());
        usersurveyDTO.put("user", makeUserDTO(authentication));
        usersurveyDTO.put("survey-info", makeUserSurveyDTO(authentication));
        return usersurveyDTO;
    }

    public Map<String,Object> getUserSurveyAnswered (UserSurvey userSurvey, Authentication authentication){
        Map<String, Object> usersurveyDTO = new LinkedHashMap<>();
        usersurveyDTO.put("u-survey-id", userSurvey.getId());
        usersurveyDTO.put("user", makeUserDTO(authentication));
        usersurveyDTO.put("survey-info", makeUserSurveyAnswered(authentication));
        return usersurveyDTO;
    }


    private User currentUser (Authentication authentication) {
        return userRepo.findByEmail(authentication.getName());
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String userName,
                                                          @RequestParam String password,@RequestParam String email) {
        User user = userRepo.findByEmail(email);
        User username = userRepo.findByUserName(userName);

        if (email.isEmpty() || password.isEmpty()) {
            return new ResponseEntity<>(makeMap("error", "Empty fields must be filled"), HttpStatus.FORBIDDEN);
        } else if (user != null) {
            return new ResponseEntity<>(makeMap("error", "User already exists"), HttpStatus.CONFLICT);
        } else if (username != null) {
            return new ResponseEntity<>(makeMap("error", "Username already in use"), HttpStatus.CONFLICT);
        } else {
            User newUser = userRepo.save(new User(userName, password, email));
            return new ResponseEntity<>(makeMap("Username", newUser.getUserName()), HttpStatus.CREATED);
        }
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping(path = "/user_survey", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUserSurvey(Authentication authentication) {
        Survey survey = surveyRepo.findOne(Long.valueOf(1));
        if (authentication == null) {
            return new ResponseEntity<>(makeMap("error", "You need to be logged answer"), HttpStatus.UNAUTHORIZED);
        } else {
            User user = currentUser(authentication);
            List<UserSurvey> userSurveys = user.getUserSurveys();
            if (user.getUserSurveys().size() > 0) {
                return new ResponseEntity<>(makeMap("UserSurvey-id", userSurveys.get(0).getId()), HttpStatus.OK);
            } else {
                List<Object> userSurveyAnswers = userSurveys.stream()
                                                    .filter(us -> us.getUserSurveyAnswers().size() > 0)
                                                    .collect(Collectors.toList());
                if ( userSurveyAnswers.size() > 0) {
                    return new ResponseEntity<>(makeMap("error", "You already answered the survey"), HttpStatus.UNAUTHORIZED);
                } else {
                    UserSurvey newUserSurvey = userSurveyRepo.save(new UserSurvey(user, survey));
                    return new ResponseEntity<>(makeMap("UserSurvey-id", newUserSurvey.getId()), HttpStatus.CREATED);
                }
            }
        }
    }


    @RequestMapping(path = "userSurveys/{nn}/userSurveyAnswer", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postUserSurveyAnswer(@PathVariable Long nn, Authentication authentication,
                                                                    @RequestBody Map<String,List<String>> myMap) {

        UserSurvey userSurveynn = userSurveyRepo.findOne(nn);
        if ( authentication == null){
            return new ResponseEntity<>(makeMap("error", "You need to be logged in"), HttpStatus.UNAUTHORIZED);
        } else if( userSurveynn == null){
            return new ResponseEntity<>(makeMap("error", "No such survey for this user"), HttpStatus.UNAUTHORIZED);
        } else if ( userSurveynn.getUser() != currentUser(authentication)){
            return new ResponseEntity<>(makeMap("error", "Not your survey"), HttpStatus.UNAUTHORIZED);
        } else {
            List<String> answers = myMap.get("answer");
            List<String> surveyQuestionIDs = myMap.get("id");

            for ( int i= 0; i < answers.size(); i++){
                Long surveyQuestionID = Long.valueOf(surveyQuestionIDs.get(i));
                SurveyQuestion surveyQuestion = surveyQuestionRepo.findOne(surveyQuestionID);
                UserSurveyAnswer answer = new UserSurveyAnswer();

                    answer.setAnswer(answers.get(i));
                    answer.setQuestion(surveyQuestion.getQuestion());
                    answer.setUserSurvey(userSurveynn);
                    userSurveyAnswerRepo.save(answer);
            }

            sendEmail(surveyQuestionIDs, authentication, answers);
        }
        return new ResponseEntity<>(makeMap("SUCCESS", "Answers saved "),(HttpStatus.CREATED));
        }

    public String makeListToString(List list1, List list2, String string1, String string2) {
        List newList = new ArrayList();
        newList.add(string1);
        newList.add(string2);
        newList.add("");

        for ( int j= 0; j < list1.size(); j++) {
            newList.add(list1.get(j));
            newList.add(list2.get(j));
        }

        String finalString =  String.join( "\r\n ", newList) ;
        return finalString;
    }

    public String getQuestion(String id){
        return surveyQuestionRepo.findOne(Long.valueOf(id)).getQuestion().getQuestion();
    }



    public void  sendEmail(List<String> surveyQuestionIDs, Authentication authentication, List<String> answers){

            //We create the email here, you can change the first two lines of the email.
            String intromessage = "Thank you for answering out survey,";
            String string2 = "Here are your answers:";

            List<String> questions = surveyQuestionIDs.stream()
                    .map(id -> getQuestion(id) )
                    .collect(toList());
            User user = currentUser(authentication);
            String message = makeListToString(questions,answers,intromessage,string2);

            //We send the emaul here, you can change the emailBcc, and emailfrom
            String bbc = "chip.mccormick@gmail.com";
            notificationService.sendNotifiction(user,"micaela@ubiqum.com",
                    "java.mail.testing.survey@gmail.com","Your answers", message);

    }


}
