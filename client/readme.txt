 
#https://www.typescriptlang.org/docs/handbook/compiler-options.html
#

tsc -w -rootDir includes -outDir target --module amd  --sourceMap -t ES5
parameters  in tsconfig.json
amd --> asynchronous require.js
commonjs --> built in node. Synchronous


#Start by Debug

RXJS: 5
    "@reactivex/rxjs": "^5.4.1",
    "@types/core-js": "0.9.35",
RXJS4: rx-dom komt met RxJS 4

npm install
RSJX compilation problem
https://github.com/scotch-io/angular2-starter-basic/issues/1

https://www.webcodegeeks.com/javascript/requirejs/typescript-example-using-requirejs
http://ianreah.com/2013/02/06/Using-RequireJS-to-Compile-RxJS-into-a-JavaScript-Library.html
https://github.com/ReactiveX/rxjs
