import React from 'react';
import { Link } from 'react-router-dom';

import SuccessIcon from './successful.png';
import './styles.css';

const Success = ({
    location: {
        state: { referenceNo },
    },
}) => (
    <div id='application-submit-success' className='container'>
        <div className='heading text-center'>
            <h2>Final step - Digitally signing application</h2>
        </div>
        <div>
            <img src={SuccessIcon} className='img-fluid' alt='Success' />

            <div className='text-center mb-5 mt-3'>
                <div className='emphasis'>
                    Your generated Application reference no :
                    <b>{referenceNo}</b>
                </div>
            </div>
        </div>
        <div className='mx-3 mb-5'>
            <ul>
                <li>
                    Please note down the Application reference no generated
                    above for future communication during on-boarding process
                </li>
                <li>
                    It is mandatory to sign the application form to complete the
                    on-boarding process. Please do so by clicking on the 'Sign
                    Application' button below
                </li>
                <li>
                    If you opt to digitally sign your application later, you
                    must complete the esigning process in 7 days else the
                    application will be marked as invalid
                </li>
                <li>
                    In case esign process unexpectedly failed, please retry the
                    process by entering your Application No in Home page
                </li>
                <li>
                    Please note that the esign process are automatic and you are
                    required to provide your credentials to authorise the
                    process. At the end, the page will automatically redirect to
                    Home
                </li>
            </ul>
        </div>

        <div className='form-button'>
            <Link to='/' className='btn btn-success'>
                Home
            </Link>
            <Link
                to={{
                    pathname: '/esign/collect',
                    state: {
                        applicationNo: referenceNo,
                    },
                }}
                className='btn btn-info'
            >
                Sign Application
            </Link>
        </div>
    </div>
);

export default Success;
