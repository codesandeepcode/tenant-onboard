import React from 'react';

import './footer.css';
import NicLogo from './nic-logo.png';

export default function Footer() {
    return (
        <footer className='footer footer-main'>
            <div className='footer-items'>
                <div className='footer-item-1'>
                    <ul>
                        <li>
                            <button type='button'>About Us</button>
                        </li>
                        <li>
                            <button type='button'>Terms of Use</button>
                        </li>
                        <li>
                            <button type='button'> FAQs</button>
                        </li>
                    </ul>
                </div>
                <div className='footer-item-2'>
                    <ul>
                        <li>
                            <button type='button'>Privacy Policy</button>
                        </li>
                        <li>
                            <button type='button'>Copyright Policy</button>
                        </li>
                        <li>
                            <button type='button'>Hyperlink Policy</button>
                        </li>
                    </ul>
                </div>
                <div className='footer-item-3'>
                    <ul>
                        <li>
                            <button type='button'>
                                Act, Rules &amp; Policies
                            </button>
                        </li>
                        <li>
                            <button type='button'>API Publishing Policy</button>
                        </li>
                        <li>
                            <button type='button'>Contact Us</button>
                        </li>
                    </ul>
                </div>
            </div>
            <div className='footer-logo'>
                <a
                    href='https://www.nic.in/'
                    target='_blank'
                    rel='noopener noreferrer'
                >
                    <img
                        src={NicLogo}
                        className='logo-resize-nic'
                        alt='National Informatics Centre'
                    />
                </a>
                <div className='copyright'>
                    This platform is implemented by National Informatics Centre.
                    <br />
                    Government of India
                </div>
            </div>
        </footer>
    );
}
