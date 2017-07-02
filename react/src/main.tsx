import 'babel-polyfill';
import React from 'react';
import ReactDOM from 'react-dom';

import App from './components/App';
import GroupsApp from './Groups';

ReactDOM.render(<App />, document.getElementById('content'));
ReactDOM.render(<GroupsApp />, document.getElementById('groupContent'));
