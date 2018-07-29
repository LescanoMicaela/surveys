package com.survey.surveys;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    @RequestMapping("/user_info")
    public Map<String,Object> makeSurveyDTO(Authentication auth) {
        Map<String,Object> dto = new LinkedHashMap<>();
        if (auth !=null) {
            dto.put("currentUser", currentUser(auth));
        }else{
            dto.put("currentUser", null);
        }
        return dto;

    }

    public Map<String,Object> makeUserDTO(User user) {
        Map<String, Object> userDTO = new LinkedHashMap<>();
            userDTO.put("id", user.getId());
            userDTO.put("email", user.getEmail());
            userDTO.put("name", user.getUserName());
        return userDTO;
    }

//    public Map<String,Object> makeSurveyDTO(Survey survey){
//        Map<String, Object> surveyDTO = new LinkedHashMap<>();
//            surveyDTO.put("survey-id", survey.getId());
//            surveyDTO.put("survey-description", survey.getDescription());
//            surveyDTO.put("survey-questions", survey.getSurveyQuestions().stream().map(sq -> makeSurveyQuestionDTO(sq)).collect(toList()));
//        return surveyDTO;
//    }

//    public Map<String,Object> makeSurveyQuestionDTO(SurveyQuestion surveyQuestion){
//        Map<String, Object> surveyDTO = new LinkedHashMap<>();
//        surveyDTO.put("id", surveyQuestion.getId());
//        surveyDTO.put("survey-question", surveyQuestion.getQuestion().getQuestion());
////        surveyDTO.put("survey-question-answer", surveyQuestion.getQuestion().getUserSurveyAnswer());
////        surveyDTO.put("survey-answer", surveyQuestion.getQuestion().getUserSurveyAnswer());
//        return surveyDTO;
//    }

    public Map<String,Object> makeUserSurveyDTO(UserSurvey userSurvey){
        Map<String, Object> surveyDTO = new LinkedHashMap<>();
        List<SurveyQuestion> surveyQuestios = userSurvey.getSurvey().getSurveyQuestions();
        surveyDTO.put("id", userSurvey.getId());
        surveyDTO.put("QnA", surveyQuestios.stream().map(sq -> makeQuestionandAnsweDTO(userSurvey, sq)).collect(toList()));
//        surveyDTO.put("survey-question-answer", surveyQuestion.getQuestion().getUserSurveyAnswer());
//        surveyDTO.put("survey-answer", surveyQuestion.getQuestion().getUserSurveyAnswer());
        return surveyDTO;
    }

   

    public Map<String,Object> makeQuestionandAnsweDTO(UserSurvey userSurvey, SurveyQuestion surveyQuestion){
        Map<String, Object> surveyDTO = new LinkedHashMap<>();
        surveyDTO.put("id",surveyQuestion.getId());
        surveyDTO.put("question", surveyQuestion.getQuestion().getQuestion());
        if (surveyQuestion.getQuestion().getUserSurveyAnswer(userSurvey.getUser()) != null){
            surveyDTO.put("answer", surveyQuestion.getQuestion().getUserSurveyAnswer(userSurvey.getUser()).getAnswer());
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
        Survey survey = userSurvey.getSurvey();
        if (authentication == null){
            return new ResponseEntity<>(makeMap("error", "You must log in to answer this survey"), HttpStatus.UNAUTHORIZED);
        }
        if ( userSurvey.getUser().getId() != currentUser(authentication).getId() ){
            return new ResponseEntity<>(makeMap("error",  "This survey is not for you"), HttpStatus.UNAUTHORIZED);

        } else {
                Map<String, Object> UsersurveyDTO = new LinkedHashMap<>();
                UsersurveyDTO.put("u-survey-id", userSurvey.getId());
                UsersurveyDTO.put("user", makeUserDTO(userSurvey.getUser()));
//                UsersurveyDTO.put("survey-info", makeSurveyDTO(userSurvey.getSurvey()));
                UsersurveyDTO.put("survey-info", makeUserSurveyDTO(userSurvey));
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

    @RequestMapping(path = "userSurveys/{nn}/userSurveyAnswer", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> postUserSurveyAnswer(@PathVariable Long nn, Authentication authentication,
                                                                    @RequestBody UserSurveyAnswer answer, @RequestBody Long idSurveyQuestion) {
        UserSurvey userSurveynn = userSurveyRepo.findOne(nn);
        List<SurveyQuestion> questions = userSurveynn.getSurvey().getSurveyQuestions();
//
        if ( authentication == null){
            return new ResponseEntity<>(makeMap("error", "You need to be logged in"), HttpStatus.UNAUTHORIZED);
        } else if( userSurveynn == null){
            return new ResponseEntity<>(makeMap("error", "No such survey for this user"), HttpStatus.UNAUTHORIZED);
        } else if ( userSurveynn.getUser() != currentUser(authentication)){
            return new ResponseEntity<>(makeMap("error", "Not your survey"), HttpStatus.UNAUTHORIZED);
        } else if ( answer.getAnswer() == ""){
            return new ResponseEntity<>(makeMap("error","Answer can't be blank"), HttpStatus.FORBIDDEN);
        } else {
           SurveyQuestion surveyQuestion = surveyQuestionRepo.findOne(idSurveyQuestion);
            answer.setQuestion(surveyQuestion.getQuestion());
            answer.setUserSurvey(userSurveynn);
            userSurveyAnswerRepo.save(answer);
            }
            return new ResponseEntity<>(makeMap("SUCCESS", "Answered saved "),(HttpStatus.CREATED));
        }
    }



