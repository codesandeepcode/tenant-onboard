import React, { useState, useEffect } from 'react';
import { useField, useFormikContext } from 'formik';

import './styles.css';
import reloadImage from './reload.png';
import ErrorPopupBox from '../../ErrorPopupBox';
import { fetchCaptchaImage, verifyIfCaptchaIsValid } from '../../../../Apis';

const CaptchaBox = ({ name, setErrorStatus }) => {
    const [reloading, setReloading] = useState(false);
    const [captchaImage, setCaptchaImage] = useState('');
    const [showErrorPopup, setErrorPopup] = useState(false);
    const { setSubmitting, setFieldError, setFieldValue } = useFormikContext();

    const loading = (status) => {
        setReloading(status);
        setSubmitting(status);
    };

    const fetchImage = () => {
        loading(true);

        fetchCaptchaImage()
            .then((response) => response.data)
            .then((data) => {
                setCaptchaImage(Buffer.from(data, 'binary').toString('base64'));
                setFieldValue(name, '', false);
                loading(false);
            })
            .catch(() => {
                setErrorPopup(true);
                loading(false);
            });
    };

    const verifyUserInput = (captchaValue) => {
        if (!captchaValue) return;

        setSubmitting(true);

        verifyIfCaptchaIsValid(captchaValue)
            .then(() => {
                setErrorStatus('');
                setFieldError(name, '');
                setSubmitting(false);
            })
            .catch((err) => {
                let message = 'Unknown exception occured!';
                if (err.response && err.response.status === 400) {
                    message = err.response.data.errors[0];
                }

                setErrorStatus(message);
                setFieldError(name, message);
                setSubmitting(false);
            });
    };

    useEffect(() => {
        fetchImage();
    }, []); // run once

    return (
        <div id='captcha-box'>
            <ErrorPopupBox
                show={showErrorPopup}
                message='Cannot generate captcha image! Please try again'
                handleClose={() => setErrorPopup(false)}
            />

            <span>Captcha</span>

            <div className='row mx-2'>
                <div className='col-7'>
                    <UserInput
                        name={name}
                        onBlurHandler={(e) => verifyUserInput(e.target.value)}
                    />
                </div>

                <div className='col-3'>
                    <CaptchaImage base64Image={captchaImage} />
                </div>

                <div className='col-2'>
                    <Reload
                        onClickHandler={() => fetchImage()}
                        inProcess={reloading}
                    />
                </div>
            </div>
        </div>
    );
};

const UserInput = ({ name, onBlurHandler }) => {
    const [field, meta] = useField(name);

    return (
        <div className='form-group'>
            <input
                {...field}
                className={
                    'form-control' +
                    (meta.error && meta.touched ? ' is-invalid' : '')
                }
                placeholder='Enter captcha as shown in image'
                onBlur={(e) => {
                    field.onBlur(e);
                    onBlurHandler(e);
                }}
            />

            {meta.touched && meta.error ? (
                <div className='invalid-feedback'>{meta.error}</div>
            ) : null}
        </div>
    );
};

const CaptchaImage = ({ base64Image }) => {
    return (
        <img
            src={`data:image/png;base64,${base64Image}`}
            alt='Captcha Image'
            className='img-fluid'
        />
    );
};

const Reload = ({ onClickHandler, inProcess }) => {
    const [rotateClass, setRotateClass] = useState('');

    useEffect(() => setRotateClass(inProcess ? 'rotate-image' : ''), [
        inProcess,
    ]);

    return (
        <img
            src={reloadImage}
            alt='Reload'
            className={`img-fluid reload-image ${rotateClass}`}
            onClick={onClickHandler}
        />
    );
};

export default CaptchaBox;
