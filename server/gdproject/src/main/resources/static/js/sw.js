self.addEventListener("install", (e) => {
    e.waitUntil(
      caches
        .open("data-store")
        .then(async (cache) => {
          let ok
          const base = '..'
          const paths =  [
            `/${base}/`,
            `/${base}/index.html`,
            `/${base}/css/styles.css`,
            `/${base}/js/script.js`,
            `/${base}/images`,
            `/${base}/images/Coat_of_arms_of_Grenada.png`,
            `/${base}/images/Coat_of_arms_of_Grenada.png`,
            `/${base}/images/Coat_of_arms_of_the_University_of_the_West_Indies.png`,
            `/${base}/images/grenada-coast.jpg`,
            `/${base}/images/logo-wb-header-en.svg`,
            `/${base}/about.html`,
            `/${base}/search.html`,
            `/${base}/properties.json`,
            `/${base}/css/`,
            `/${base}/css/about.css`,
            `/${base}/css/search.css`,
            `/${base}/css/register.css`,
          ]
          try {
            ok = await cache.addAll(paths)
          } catch (error) {
            console.error('sw: cache.addAll')
            for (let i of paths) {
              try {
                ok = await cache.add(i);
              } catch (err) {
                console.warn('sw: cache.add',i);
              }
            }
          }

          return ok;
        }
        )
    );
});

self.addEventListener("fetch", (e) => {
    console.log(e.request.url);
    e.respondWith(
        caches.match(e.request).then((response) => response || fetch(e.request))
    );
});
  
  