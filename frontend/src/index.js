import React from 'react';
import ReactDOM from 'react-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

import App from './App';
import Header from './components/base/Header/Header';
import Footer from './components/base/Footer/Footer';

ReactDOM.render(
    <React.StrictMode>
        <Header />
        <App />
        <Footer />
    </React.StrictMode>,
    document.getElementById('root')
);
