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
  