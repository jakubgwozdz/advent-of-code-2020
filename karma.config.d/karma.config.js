// workaround copied from https://github.com/DaanVandenBosch/kotlin-js-karma-resources-test/blob/fix/karma.config.d/karma.config.js
// this file is required for :jsBrowserTest only, not on prod

config.middleware = config.middleware || [];
config.middleware.push('resource-loader');

function ResourceLoaderMiddleware() {
  const fs = require('fs');

  return function (request, response, next) {
    try {
      const content = fs.readFileSync(PROJECT_PATH + '/build/processedResources/js/test' + request.originalUrl);
      response.writeHead(200);
      response.end(content);
    } catch (ignored) {
      try {
        const content = fs.readFileSync(PROJECT_PATH + '/build/processedResources/js/main' + request.originalUrl);
        response.writeHead(200);
        response.end(content);
      } catch (ignored) {
        next();
      }
    }
  }
}

config.plugins.push({
  'middleware:resource-loader': ['factory', ResourceLoaderMiddleware]
});
