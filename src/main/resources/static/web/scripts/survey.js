setTimeout(function(){ $("body").show() }, 200);

var survey;
var questionsAndAnswers;
var counter = 0;
var answers = [];
var surveyQuestionIDs= [];


$(document).ajaxStart(function(){
    $("#loader").css("display", "flex");
});

$(document).ajaxComplete(function(){
    $("#loader").css("display", "none");
});

$(document).ready(function () {
    $.ajax({
        url: makeUrl(),
        dataType: 'json',
        success: function (data) {
            survey = data;
            questionsAndAnswers = survey["survey-info"].QnA;
            sortData(questionsAndAnswers);
            showQuestions();
            nameSurvey();
        },
        complete: function(){
            toggleQuestion();
        },
        error: function () {
            window.location.href = "index.html";
        }
    })
});

function postUserSurveyQuestion() {
    var userSurveyid = survey["u-survey-id"];
    $.post({
        url: "/api/userSurveys/" + userSurveyid + "/userSurveyAnswer",
        data: JSON.stringify({answer: answers , id: surveyQuestionIDs}),
        dataType: "text",
        contentType: "application/json"
    }).done(function () {
        console.log("answer saved");
        window.location.href = "results.html?us=" +""+survey["u-survey-id"]

    }).fail(
        function (e) {
            makeAlert(e.responseText)
        });

}


function sortData(data) {
    if (data != null) {
        data.sort(function (a, b) {
            var a1 = a.id, b1 = b.id;
            if (a1 == b1) return 0;
            return a1 > b1 ? 1 : -1;
        });
    }
}

function hideElement(elementid){
    $("#"+elementid).hide()
}

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

function makeUrl() {
    var userSurveyID = getParameterByName("us");
    return '/api/user_survey_view/' + userSurveyID;
}


function toggleQuestion(){
	$("#surveyPresentation").slideToggle(300);
}

function showQuestions(){
    $("#question").text(questionsAndAnswers[counter].question);
    createRadioButtons();
    $("#next").click(function changeQuestionDisplay(){
        makeAlert("");
        if(document.querySelector('input[name="answer"]:checked') != null ){
            counter +=1;
            widthProgressBar();
            createArraysQnA();
            emptyAnswerInput();

            if ( questionsAndAnswers[counter] == undefined){
                endSurveyDisplay();
            }else{
                showNextDisplay(questionsAndAnswers[counter].question);
            }
        }else{
            makeAlert("You must select an answer");
        }
    })
}

function showNextDisplay(message){
    hideElement("surveyLayout");
    $("#question").text(message);
    $("#surveyLayout").slideToggle(400);
}

function makeAlert(text){
    $("#alert").text(text);
}



$('#answer').keypress(function(e){
    if(e.which == 13){
        $('#next').click();
    }
});

$("#answer").keyup(function(){
    makeAlert("");
});

function widthProgressBar(){
    var progress = (counter*100) / questionsAndAnswers.length;
    $("#progressBar").css("width", progress +"%");
}


///just for input answers option
function getAnswers(){
    return $("#answer").val();
}

//for multiple option answer
function getAnswerOption(){
   return document.querySelector('input[name="answer"]:checked').value;
}


function nameSurvey(){
    $("#surveyTitle").text(survey["survey-info"].description);
}

function endSurveyDisplay(){
    showNextDisplay("Thank you");
    hideElement("next");
    hideElement("selectAnswer");
    postUserSurveyQuestion();
}

function createArraysQnA(){
    answers.push(getAnswerOption());
    surveyQuestionIDs.push(""+ questionsAndAnswers[counter-1].id);
}

function emptyAnswerInput(){
    $("#answer").val('')
}

function createRadioButtons(){
    var select = document.getElementById("selectAnswer");
    for(var q= 0; q < questionsAndAnswers[counter].answer.length; q++){
        var label = document.createElement("label");

        var radio = document.createElement("input");
        radio.type = "radio";
        radio.name = "answer";
        radio.value = questionsAndAnswers[counter].answer[q].option;
        label.appendChild(radio);

        label.appendChild(document.createTextNode(questionsAndAnswers[counter].answer[q].option));

        select.appendChild(label);
    }

}