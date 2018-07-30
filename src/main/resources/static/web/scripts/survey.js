var survey;
var counter = 0;
var answers = [];
var surveyQuestionIDs= [];

$(document).ready(function () {
    $.ajax({
        url: makeUrl(),
        dataType: 'json',
        success: function (data) {
            survey = data;
            showQuestions();

        },
        error: function () {
            console.log("This is not your survey")
        }
    })
});

function getParameterByName(name) {
    var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
    return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

function makeUrl() {
    var userSurveyID = getParameterByName("us");
    return '/api/user_survey_view/' + userSurveyID;
}





$("#startSurvey").click(function(){
    $("#surveyPresentation").hide();
//	$("#surveyPresentation").slideToggle();
    $("#surveyLayout").show(900);

})

function showQuestions(){
    $("#question").text(survey["survey-info"].QnA[0].question);
    $("#next").click(function(){
        counter +=1;
        widthProgressBar();
        postUserSurveyQuestion();
        // answers.push[$("#answer").val()];
        // surveyQuestionIDs.push[survey["survey-info"].QnA[counter-1].id];
        if ( survey["survey-info"].QnA[counter] == undefined){

            $("#question").text("Thank you");
            $("#next").hide();
            $("#answer").hide();
            $("#submit").show();

        }else{
            console.log(counter);
            $("#surveyLayout").hide();
            $("#question").text(survey["survey-info"].QnA[counter].question);
            $("#surveyLayout").slideToggle(500);



        }

    })

}

function widthProgressBar(){
    var progress = (counter*100) / survey["survey-info"].QnA.length;
    $("#progressBar").css("width", progress +"%");
}

function getAnswers(){
    var answer = $("#answer").val();
}

function postUserSurveyQuestion() {
    var userSurveyid = survey["u-survey-id"];
    var surveyQuestionid = survey["survey-info"].QnA[counter-1].id;
    $.post({
        url: "/api/userSurveys/" + userSurveyid + "/userSurveyAnswer",
        data: JSON.stringify({answer: $("#answer").val(), id: surveyQuestionid}),
        dataType: "text",
        contentType: "application/json"
    }).done(function () {
        console.log("answer saved");
    }).fail(
        function (e) {
            $("#alert").text(e.responseText)
        });

}