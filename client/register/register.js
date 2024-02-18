const form = document.querySelector('form');
const data = null;


form.addEventListener('submit', (e) => {
    e.preventDefault();

    data = new URLSearchParams(new FormData(form));

    fetch("http://localhost:8080/csrf", {
        method: "POST",
        body: data
    }).then(res => console.log(res))
});

