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
        List<Object> usersa = currentUser(authentication).getUserSurveys().stream().filter(us -> us.getUserSurveyAnswers().size() > 0).collect(Collectors.toList());
            userDTO.put("id", user.getId());
            userDTO.put("email", user.getEmail());
            userDTO.put("name", user.getUserName());
        if ( usersa.size() > 0){
            userDTO.put("anseredSurvey", true );
            userDTO.put("UserSurveyID",currentUser(authentication).getUserSurveys().get(1).getId());
        }else{
            userDTO.put("anseredSurvey", false );
        }

        return userDTO;
    }



    public Map<String,Object> makeUserSurveyDTO(Authentication auth){
        Map<String, Object> surveyDTO = new LinkedHashMap<>();
        Survey survey = surveyRepo.findOne(Long.valueOf(1));
        Set<SurveyQuestion> surveyQuestions = new LinkedHashSet<>();
        surveyQuestions = survey.getSurveyQuestions();
//        surveyDTO.put("id", userSurvey.getId());
        surveyDTO.put("description", survey.getDescription());
        surveyDTO.put("QnA", surveyQuestions.stream().map(sq -> makeQuestionandAnsweDTO( sq, auth)).collect(toList()));
        surveyDTO.put("length", surveyQuestions.size());
        return surveyDTO;
    }

   

    public Map<String,Object> makeQuestionandAnsweDTO( SurveyQuestion surveyQuestion, Authentication auth){
        Map<String, Object> surveyDTO = new LinkedHashMap<>();
        surveyDTO.put("id",surveyQuestion.getId());
        surveyDTO.put("question", surveyQuestion.getQuestion().getQuestion());
        if (surveyQuestion.getQuestion().getUserSurveyAnswer(currentUser(auth)) != null){
            surveyDTO.put("answer", surveyQuestion.getQuestion().getUserSurveyAnswer(currentUser(auth)).getAnswer());
        } else{
            surveyDTO.put("answer", null);
        }
        return surveyDTO;
    }

//    public Map<String,Object> makeUserSurveyDTO(UserSurvey userSurvey){
//        Map<String, Object> UsersurveyDTO = new LinkedHashMap<>();
//        UsersurveyDTO.put("u-survey-id", userSurvey.getId());
//        UsersurveyDTO.put("user-id", makeUserDTO(userSurvey.getUser()));
//        UsersurveyDTO.put("survey-info", makeSurveyDTO(userSurvey.getSurvey()));
//        return UsersurveyDTO;
//    }



    @RequestMapping("/user_survey_view/{usID}")
    public ResponseEntity<Map<String, Object>> userSurveyView(@PathVariable Long usID, Authentication authentication) {
        UserSurvey userSurvey = userSurveyRepo.findOne(usID);
        if (authentication == null){
            return new ResponseEntity<>(makeMap("error", "You must log in to answer this survey"), HttpStatus.UNAUTHORIZED);
        }
        if ( userSurvey.getUser().getId() != currentUser(authentication).getId() ){
            return new ResponseEntity<>(makeMap("error",  "This survey is not for you"), HttpStatus.UNAUTHORIZED);

        } else {
                Map<String, Object> UsersurveyDTO = new LinkedHashMap<>();
                UsersurveyDTO.put("u-survey-id", userSurvey.getId());
                UsersurveyDTO.put("user", makeUserDTO(authentication));
//                UsersurveyDTO.put("survey-info", makeSurveyDTO(userSurvey.getSurvey()));
                UsersurveyDTO.put("survey-info", makeUserSurveyDTO(authentication));
            return new ResponseEntity<>(UsersurveyDTO, HttpStatus.OK);
            }
        }


    private User currentUser (Authentication authentication) {
        return userRepo.findByEmail(authentication.getName());
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String userName, @RequestParam String password,@RequestParam String email) {
        if (email.isEmpty() || password.isEmpty() ) {
            return new ResponseEntity<>(makeMap("error", "Empty fields must be filled"), HttpStatus.FORBIDDEN);
        }
        User user = userRepo.findByEmail(email);
        User username = userRepo.findByUserName(userName);
        if ( user != null ) {
            return new ResponseEntity<>(makeMap("error", "User already exists"), HttpStatus.CONFLICT);
        }

        if (username !=null) {
            return new ResponseEntity<>(makeMap("error", "Username already in use"), HttpStatus.CONFLICT);
        }
        User newUser = userRepo.save(new User(userName, password,email));

        //Sends emails when signup is successful.
        try{
            notificationService.sendNotifiction(newUser);
        }catch(MailException e){
            //catch error
            return new ResponseEntity<>(makeMap("error", "couldnt send email"), HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(makeMap("Username", newUser.getUserName()) , HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping(path = "/user_survey", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUserSurvey(Authentication authentication){
        Survey survey = surveyRepo.findOne(Long.valueOf(1));
        if (authentication == null) {
            return new ResponseEntity<>(makeMap("error", "You need to be logged answer"), HttpStatus.UNAUTHORIZED);
        }else if (currentUser(authentication).getUserSurveys().size() > 0) {
            return new ResponseEntity<>(makeMap("UserSurvey-id", currentUser(authentication).getUserSurveys().get(0).getId() ), HttpStatus.OK);
        }else{
            List<Object> usersa = currentUser(authentication).getUserSurveys().stream().filter(us -> us.getUserSurveyAnswers().size() > 0).collect(Collectors.toList());
            if( usersa.size() > 0){
                return new ResponseEntity<>(makeMap("error", "You already answered the survey"), HttpStatus.UNAUTHORIZED);
            }else{
                UserSurvey newUserSurvey = userSurveyRepo.save(new UserSurvey(currentUser(authentication),survey));
                return new ResponseEntity<>(makeMap("UserSurvey-id", newUserSurvey.getId()),  HttpStatus.CREATED);
            }

        }
    }

    @RequestMapping(path = "userSurveys/{nn}/userSurveyAnswer", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postUserSurveyAnswer(@PathVariable Long nn, Authentication authentication,
                                                                    @RequestBody Map<String,List<String>> myMap) {
        UserSurvey userSurveynn = userSurveyRepo.findOne(nn);
//
        if ( authentication == null){
            return new ResponseEntity<>(makeMap("error", "You need to be logged in"), HttpStatus.UNAUTHORIZED);
        } else if( userSurveynn == null){
            return new ResponseEntity<>(makeMap("error", "No such survey for this user"), HttpStatus.UNAUTHORIZED);
        } else if ( userSurveynn.getUser() != currentUser(authentication)){
            return new ResponseEntity<>(makeMap("error", "Not your survey"), HttpStatus.UNAUTHORIZED);
//        } else if ( myMap.get("answer").getAnswer() == ""){
//            return new ResponseEntity<>(makeMap("error","Answer can't be blank"), HttpStatus.FORBIDDEN);
        } else {
            List<String> answers = myMap.get("answer");
            List<String> surveyQuestionIDs = myMap.get("id");
            for ( int i= 0; i < answers.size(); i++){
                Long surveyQuestionID = Long.valueOf(surveyQuestionIDs.get(i));
                UserSurveyAnswer answer = new UserSurveyAnswer();
                SurveyQuestion surveyQuestion = surveyQuestionRepo.findOne(surveyQuestionID);

                    answer.setAnswer(answers.get(i));
                    answer.setQuestion(surveyQuestion.getQuestion());
                    answer.setUserSurvey(userSurveynn);
                    userSurveyAnswerRepo.save(answer);
                }
        }
        return new ResponseEntity<>(makeMap("SUCCESS", "Answered saved "),(HttpStatus.CREATED));
        }
    }





