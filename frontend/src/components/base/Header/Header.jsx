import React from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';

import emblemImage from './national-emblem.png';
import digitalIndiaLogo from './digital-india-logo.png';
import './header.css';

export default function Header() {
    return (
        <div
            className='container-fluid clearfix'
            style={{ background: '#002e4c' }}
        >
            <div
                className='float-left'
                style={{ margin: '10px 0px' }}
                id='header-flex'
            >
                <img
                    src={emblemImage}
                    className='header-emblem'
                    alt='National Emblem of India'
                />

                <div className='header-content'>
                    <a href='https://napix.gov.in/' className='header-napix'>
                        National API Exchange Platform
                    </a>
                    <sup className='header-beta'>BETA</sup>
                    <div className='header-meity'>
                        Ministry of Electronics &amp; Information Technology
                    </div>
                    <div className='header-nic'>
                        National Informatics Centre, Government of India
                    </div>
                </div>
            </div>
            <div className='float-right'>
                <a
                    href='https://digitalindia.gov.in/'
                    target='_blank'
                    rel='noopener noreferrer'
                >
                    <img
                        src={digitalIndiaLogo}
                        className='header-digital'
                        alt='Digital India Logo'
                    />
                </a>
            </div>
        </div>
    );
}
