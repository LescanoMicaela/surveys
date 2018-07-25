package com.survey.surveys;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SurveyController {

    @Autowired UserRepository userRepo;

    @RequestMapping("/user_info")
    public Map<String,Object> makeSurveyDTO(Authentication auth) {
        Map<String,Object> dto = new LinkedHashMap<>();
        if (auth !=null) {
            dto.put("currentUser", makeUserDTO(getCurrentUser(auth)));
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

    public Map<String,Object> makeUserSurveyDTO(){

    }

    private User getCurrentUser (Authentication authentication) {
        return userRepo.findByEmail(authentication.getName());
    }

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }


    @RequestMapping(path = "/users", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> createUser(@RequestParam String userName, @RequestParam String password,@RequestParam String email) {
        if (email.isEmpty() || password.isEmpty() ) {
            return new ResponseEntity<>(makeMap("error", "empty field"), HttpStatus.FORBIDDEN);
        }
        User user = userRepo.findByEmail(email);
        User username = userRepo.findByUserName(userName);

        if ( (user != null ) || (username !=null)) {
            return new ResponseEntity<>(makeMap("error", "User already exists"), HttpStatus.CONFLICT);
        }
        User newUser = userRepo.save(new User(userName, password,email));
        return new ResponseEntity<>(makeMap("Username", newUser.getUserName()) , HttpStatus.CREATED);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }



//    @RequestMapping("/owners/{ownerId}")
//    public String findOwner(@PathVariable Long ownerId) {
//        Owner owner = ownerService.findOwner(ownerId);
//    ...
//    }

}
