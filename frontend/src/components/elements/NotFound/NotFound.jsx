import React from 'react';
import { Link } from 'react-router-dom';

import WarningSign from './yellow-exclamation-mark-angle-warning-sign.jpg';
import './styles.css';

const NotFound = () => (
    <div className='container'>
        <div id='not-found'>
            <img src={WarningSign} className='img-fluid' alt='Warning' />
            <h3>Page not found!</h3>
            <Link to='/' className='btn btn-primary'>
                Home
            </Link>
        </div>
    </div>
);

export default NotFound;
