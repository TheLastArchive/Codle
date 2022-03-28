
window.onload = function() {
    console.log("Script loaded");
    fetch('http://localhost:5000/word')
    .then(res => res.json())
    .then(data => console.log(data))
    .catch(error => console.error(error))
}

