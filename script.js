const toggleButton = document.getElementsByClassName('toggle-button')[0]
const menu = document.getElementsByClassName('menu')[0]

toggleButton.addEventListener('click', () => {
    menu.classList.toggle('active')
})

// Register service worker to control making site work offline
if ("serviceWorker" in navigator) {
    navigator.serviceWorker
      .register(
        "/gd-project/sw.js",
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


  