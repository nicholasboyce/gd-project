self.addEventListener("install", (e) => {
    e.waitUntil(
      caches
        .open("data-store")
        .then(async (cache) => {
          let ok
          const base = 'client'
          const paths =  [
            `/${base}/`,
            `/${base}/index.html`,
            `/${base}/styles.css`,
            `/${base}/script.js`,
            `/${base}/images`,
            `/${base}/images/Coat_of_arms_of_Grenada.png`,
            `/${base}/images/Coat_of_arms_of_Grenada.png`,
            `/${base}/images/Coat_of_arms_of_the_University_of_the_West_Indies.png`,
            `/${base}/images/grenada-coast.jpg`,
            `/${base}/images/logo-wb-header-en.svg`,
            `/${base}/about/index.html`,
            `/${base}/register/index.html`,
            `/${base}/search/index.html`,
            `/${base}/search/properties.json`,
            `/${base}/styles/`,
            `/${base}/styles/about.css`,
            `/${base}/styles/search.css`,
            `/${base}/styles/register.css`,
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
  
  