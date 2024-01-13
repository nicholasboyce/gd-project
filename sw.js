self.addEventListener("install", (e) => {
    e.waitUntil(
      caches
        .open("data-store")
        .then((cache) =>
          cache.addAll([
            "/index.html",
            "/gd-project/styles.css",
            "/gd-project/script.js",
            "/gd-project/pages/",
            "/gd-project/pages/about.html",
            "/gd-project/pages/register.html",
            "/gd-project/pages/search.html",
            "/gd-project/pages/properties.json",
          ])
        )
    );
});

self.addEventListener("fetch", (e) => {
    console.log(e.request.url);
    e.respondWith(
        caches.match(e.request).then((response) => response || fetch(e.request))
    );
});
  
  