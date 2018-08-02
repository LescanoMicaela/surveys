setTimeout(function(){ $("body").show() }, 100);

const welcomeMessage = $("#userName");
var datauser;
const buttonTologAndSignin = $(".loggedout");

getData();

function getData() {
    $.ajax({
        url: "/api/user_info",
        dataType: 'json',
        success: function (data) {
            datauser = data;
            changeDisplay();
        }
    })}


function changeDisplay(){
    if ( currentUser()){
        displayLoggedIn();
    }else{
        displayLoggedOut();
    }
}

function currentUser(){
    if (datauser.currentUser == null) {
        return false;
    }else{
        return true;
    }
}


function displayLoggedIn(){
    $("#loggedOutView").hide();
    showElement("loggedInview");
    showElement("logout");
    welcomeMessage.text(", "+ datauser.currentUser.name);
    buttonTologAndSignin.hide();
    showButtonToAnsweredSurvey();
}

function displayLoggedOut() {
    showElement("loggedOutView");
    hideElement("loggedInview");
    hideElement("logout");
    buttonTologAndSignin.show();
}


$("#create-UserSurvey").click(function createUserSurvey(){
    $.post("/api/user_survey")
        .done(function(e){  window.location.href = "userSurvey.html?us=" +""+e["UserSurvey-id"]})
        .fail( function(e){console.log(e.responseJSON.error)})
});


$("#viewSurvey").click(function viewUserSurvey(){
    window.location.href = "answeredSurvey.html?us=" +""+datauser.currentUser.UserSurveyID;
});


function showButtonToAnsweredSurvey(){
    if(datauser.currentUser.anseredSurvey === true ){
        showElement("answered");
        hideElement("notAnswered");
    }else{
        hideElement("answered");
        showElement("notAnswered");

    }
}


function showElement(elementid){
    $("#"+elementid).show()
}


function hideElement(elementid){
    $("#"+elementid).hide()
}