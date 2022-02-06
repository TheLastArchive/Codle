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
    while (noOfGuesses < 7) {
        let userGuess = getUserInput().toLowerCase();
        if (invalidInput(userGuess)) {
            console.log("Invalid input");
            continue;
        }
        noOfGuesses++;
        if (isAnswerCorrect(wordToGuess, userGuess, noOfGuesses)) {
            console.log((noOfGuesses) + "/6\nThe word was " + wordToGuess.toUpperCase());
            return;
        }
    }
    console.log("X/6\nThe word was " + wordToGuess.toUpperCase());
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
    console.log(answerString);
    logUserGuess(noOfGuesses, answerString);
    return correctLetters === 5;
}

function logUserGuess(noOfGuesses, userGuess) {
    for (let i = 0; i++; i <= 5) {
        loggedGuesses[noOfGuesses][i] = userGuess.charAt(i);
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
