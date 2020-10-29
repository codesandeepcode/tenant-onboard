import React from 'react';
import { Link } from 'react-router-dom';
import Lottie from 'react-lottie';

import './styles.css';
import animationData from './1818-success-animation-modified.json';
import { urlToDownloadPdfApplication } from '../../../../Apis';

const defaultOptions = {
    background: 'transparent',
    autoplay: true,
    loop: false,
    animationData,
};

const SigningSuccess = ({ applicationNo }) => (
    <div id='esign-success'>
        <div className='banner'>
            <Lottie
                options={defaultOptions}
                style={{ width: '28rem', height: '28rem' }}
            />

            <label>
                For Application reference no :<strong>{applicationNo}</strong>
            </label>

            <h1 className='display-4'>E-Signing process successful</h1>

            <ul>
                <li>
                    Your registration details is forwarded to concerned
                    authorities for approval
                </li>
                <li>
                    You may download signed application for your records using '
                    <b>Download Signed Application</b>' button provided
                </li>
            </ul>

            <div className='action-buttons'>
                <Link to='/' className='btn btn-primary'>
                    Home
                </Link>

                <a
                    id='downloadPdf'
                    href={urlToDownloadPdfApplication(applicationNo, true)}
                    className='btn btn-success'
                    download
                >
                    Download Signed Application
                </a>
            </div>
        </div>
    </div>
);

export default SigningSuccess;
