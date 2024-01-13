self.addEventListener("install", (e) => {
    e.waitUntil(
      caches
        .open("data-store")
        .then(async (cache) => {
          let ok
          const base = 'gd-project'
          const paths =  [
            `/${base}/`,
            `/${base}/index.html`,
            `/${base}/styles.css`,
            `/${base}/script.js`,
            `/${base}/pages/`,
            `/${base}/pages/about.html`,
            `/${base}/pages/register.html`,
            `/${base}/pages/search.html`,
            `/${base}/pages/properties.json`,
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
  
  