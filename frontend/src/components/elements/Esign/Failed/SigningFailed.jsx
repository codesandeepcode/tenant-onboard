import React from 'react';
import Lottie from 'react-lottie';
import { Link } from 'react-router-dom';

import animationData from './4903-failed-compressed.json';
import './styles.css';

const defaultOptions = {
    background: 'transparent',
    autoplay: true,
    loop: false,
    animationData,
};

const SigningFailed = ({ applicationNo, errorMessage }) => (
    <div id='esign-failed'>
        <div className='banner'>
            <Lottie
                options={defaultOptions}
                style={{ width: '22rem', height: '22rem' }}
            />

            {applicationNo ? (
                <label>
                    For Application reference no :
                    <strong className='gap'>{applicationNo}</strong>
                </label>
            ) : null}

            <h1 className='display-4'>E-Signing process failed!</h1>

            <h4 className='font-weight-normal mt-2 mb-3'>
                Reason -
                <strong>{errorMessage || 'Unknown Exception occured!'}</strong>
            </h4>

            <span className='additional-message'>
                Please retry the e-sign process after a while by entering
                Application number in Home page
            </span>

            <div className='action-buttons'>
                <Link to='/' className='btn btn-primary'>
                    Home
                </Link>
            </div>
        </div>
    </div>
);

export default SigningFailed;
