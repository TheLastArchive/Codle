const randomWords = require('random-words');
const ps = require('prompt-sync');

let loggedGuesses = [
    ["", "", "", "", ""],
    ["", "", "", "", ""],
    ["", "", "", "", ""],
    ["", "", "", "", ""],
    ["", "", "", "", ""],
    ["", "", "", "", ""]]

gameLogic();

function gameLogic() {
    let noOfGuesses = 0;
    let wordToGuess = getWord();
    while (noOfGuesses < 6) {
        let userGuess = getUserInput().toLowerCase();
        if (invalidInput(userGuess)) {
            console.log("Invalid input");
            continue;
        }
        noOfGuesses++;
        if (isAnswerCorrect(wordToGuess, userGuess, noOfGuesses)) {
            console.log((noOfGuesses) + "/6\nThe word was " + wordToGuess.toUpperCase());
            displayGrid();
            return;
        }
    }
    console.log("X/6\nThe word was " + wordToGuess.toUpperCase());
    displayGrid();
}

function isAnswerCorrect(wordToGuess, userGuess, noOfGuesses) {
    let answerString = "";
    let correctLetters = 0;
    for (let i = 0; i < 5; i++) {
        if (wordToGuess.charAt(i) === userGuess.charAt(i)) {
            answerString += "+";
            correctLetters++;
        }
        else if (wordToGuess.includes(userGuess.charAt(i))) {
            answerString += "0";
        }
        else {
            answerString += "-";
        }

    }
    logUserGuess(noOfGuesses, answerString);
    console.log(answerString);
    return correctLetters === 5;
}

function logUserGuess(noOfGuesses, answerString) {
    for (let i = 0; i <= 5; i++) {
        loggedGuesses[noOfGuesses - 1][i] = answerString.charAt(i);
    }
}

function getWord() {
    const words = randomWords({exactly: 500}).filter(
        word => word.length === 5);
    return words[Math.floor(Math.random() * words.length)];
}

function invalidInput(userGuess) {
    if (userGuess.length != 5) {
        return true;
    }
    if (/^[a-z]+$/.test(userGuess)) {
        return false;
    }
    return true;
}

function getUserInput() {
    const prompt = ps();
    return prompt();
}

function displayGrid() {
    for (var row of loggedGuesses) {
        if (row[0] === "") 
            return;
        let rowString = "";
        for (var char of row) {
            rowString += char + " ";
        }
        console.log(rowString);
    }
}
