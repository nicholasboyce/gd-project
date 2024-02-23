const toggleButton = document.getElementsByClassName('toggle-button')[0]
const menu = document.getElementsByClassName('menu')[0]
const modalForm = document.querySelector('.login-form')

toggleButton.addEventListener('click', () => {
    menu.classList.toggle('active')
})

// Register service worker to control making site work offline
if ("serviceWorker" in navigator) {
    navigator.serviceWorker
      .register(
        "/js/sw.js",
      )
      .then(() => console.log("Service Worker Registered"));
}

const loginButton = document.querySelector(".signin");
const loginBackground = document.querySelector(".login-background");
const modal = document.querySelector(".modal");

loginButton.addEventListener('click', () => {
  //Show background div, blur everything, and show login modal
  modal.classList.toggle('popup')
});

loginBackground.addEventListener('click', () => {
  modal.classList.toggle('popup')
});

modalForm.addEventListener('submit', async (e) => {
  e.preventDefault();

  const data = new URLSearchParams();
  const formData = new FormData(modalForm);
  for (const pair of formData) {
    data.append(pair[0].slice(6), pair[1])
  }
  tokenResponse = await fetch('/csrf')
  token = await tokenResponse.json()

  await fetch('/login', {
    method: "POST",
    headers: {
      'X-CSRF-TOKEN': token.token,
    },
    body: data
  }).then((response) => {
    console.log(response)
  })
})

  