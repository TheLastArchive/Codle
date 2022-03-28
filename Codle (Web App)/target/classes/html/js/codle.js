let wordToGuess = "";
let wordID = 0;

window.onload = function getWord() {
    $.ajax({
        type: "GET",
        url: "/word",
        success: function(res) {
            wordToGuess = res["word"].toUpperCase();
            console.log(wordToGuess);
            wordID = res["id"];
        }
    });
}

function sendResults() {
    $.ajax({
        type: "POST",
        url: "/result",
        contentType: "application/json",
        data: JSON.stringify({
            "wordID": wordID,
            "guesses": loggedGuesses,
            "win": win
        })
    });
}

let loggedGuesses = [];
let guessNum = 1;
let rowIndex = 1;
let currentWord = [];
let win = false;
let emojiResults = "";

window.onkeydown = function keyPress(e) {
    let elementID = "row_" + guessNum + "_index_" + rowIndex;
    let key = e.key.toUpperCase();
    console.log(key);

    if (key === "BACKSPACE") {
        handleBackspace();
    }

    else if (key === "ENTER" && rowIndex === 6) {
        if (isAnswerCorrect()) {
            win = true;
            displayOverlay();
            sendResults();
            //endgame
        }
        else if (guessNum < 6){
            rowIndex = 1;
            guessNum++;
            currentWord = [];
        }
        else { //Didn't win
            displayOverlay();
            sendResults();
            alert(`Assertion Error:\nExpected :${wordToGuess}`)
        }
    }
    //Key has to be alpha char
    else if ((/[A-Z]/).test(key) && key.length === 1) {
        $("#" + elementID).text(key).attr("value", key);
        currentWord.push(key);
        rowIndex++;
    }
}

function handleBackspace() {
    //Don't make the rowIndex = 0 or else bad
    if (rowIndex > 1) { rowIndex--; }
    let elementID = "row_" + guessNum + "_index_" + rowIndex;
    $("#" + elementID).text("").attr("value", "");
    currentWord.pop();
}

//Ideally this method could be condensed
function isAnswerCorrect() {
    const green = "rgb(83, 131, 78)";
    const greenBlock = String.fromCodePoint(0x1F7E9);
    const yellow = "rgb(181, 159, 59)";
    const yellowBlock = String.fromCodePoint(0x1F7E8);
    const grey = "rgb(58, 58, 60)";
    const greyBlock = String.fromCodePoint(0x2B1B);
    //Create a copy list so it can be manipulated in this method
    let answer = wordToGuess.split("");
    let correctLetters = 0;
    emojiArray = ["","","","",""];

    //Check for green tiles first
    for (let i = 0; i < 5; i++) {
        let elementID = "row_" + guessNum + "_index_" + (i+1);
        if (answer[i] === currentWord[i]) {
            $("#" + elementID).css("background-color", green);
            answer[i] = "_";
            correctLetters++;
            emojiArray[i] = greenBlock;
        }
    }
    //Then check for yellow and grey tiles
    for (let i = 0; i < 5; i++) {
        let elementID = "row_" + guessNum + "_index_" + (i+1);
        if ($("#" + elementID).css("background-color") === green) {
             continue;
        }
        else if (answer.includes(currentWord[i])) {
            $("#" + elementID).css("background-color", yellow);
            answer[answer.indexOf(currentWord[i])] = "_";
            emojiArray[i] += yellowBlock;
        } 
        else {
            $("#" + elementID).css("background-color", grey);
            emojiArray[i] += greyBlock;
        }
    }
    //Log the user's guess
    loggedGuesses.push(currentWord.join(""));
    emojiResults += emojiArray.join("") + "\n";
    return correctLetters === 5;
}


function displayOverlay() {
    $("#endgame_overlay").show();
}

//Copy the emoji string to the user's clipboard
$("#share_button").on("click", function() {
    let score = win ? loggedGuesses.length : "x"
    navigator.clipboard.writeText(`Codle ${wordID} ${score}/6\n\n${emojiResults}`);
})