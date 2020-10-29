import React, { useState } from 'react';
import { Formik, Form, ErrorMessage, useField } from 'formik';
import * as Yup from 'yup';

import './styles.css';
import frontImage from './images/onboarding_image_1.png';
import CaptchaBox from './Captcha/CaptchaBox';
import ApplicationNumber from './ApplicationNumber';

const validationRules = Yup.object().shape({
    radioOption: Yup.string().required(
        'Please choose one of the above options'
    ),
    organisation: Yup.string().when('radioOption', {
        is: (value) => value === 'register-publisher',
        then: Yup.string().required('Please specify the organisation type'),
    }),
    applicationNo: Yup.string().when('radioOption', {
        is: (value) => value === 'esign-completion',
        then: Yup.string()
            .required('Please enter valid Application No')
            .matches(/^NAPIX\/[\d]{5}$/, {
                message: 'Invalid Application No given',
                excludeEmptyString: true,
            }),
    }),
    captcha: Yup.string().required('This field is required'),
});

const fieldValidation = ({ error, values }) => {
    const errorObject = {};

    const r1 = error.api.applicationNo.find(
        (e) => e.applicationNo === values.applicationNo
    );
    if (r1) errorObject.applicationNo = r1.message;

    if (error.api.captcha) errorObject.captcha = error.api.captcha;

    return errorObject;
};

const clearFields = (setFieldValue) => {
    setFieldValue('applicationNo', '', false);
    setFieldValue('organisation', '', false);
};

const LandingPage = ({ history }) => {
    const [error, updateError] = useState({
        api: {
            applicationNo: [],
            captcha: '',
        },
    });

    const formSubmit = (values) => {
        const { organisation, radioOption, applicationNo } = values;

        switch (radioOption) {
            case 'register-publisher':
                history.push('/application-form', { organisation });
                break;
            case 'esign-completion':
                history.push('/esign/collect', { applicationNo });
                break;
            default:
                throw new Error(
                    `No case found for given radio option - '${radioOption}'`
                );
        }
    };

    return (
        <div id='landing-page' className='container-fluid'>
            <div className='card card0 m-5'>
                <div className='card-body'>
                    <h2 className='card-title text-center display-4 mx-4 mt-4'>
                        Publisher On-Boarding Registration with NAPIX
                    </h2>
                    <div className='row d-flex mt-4'>
                        <div className='col-lg-6'>
                            <div className='row px-3 justify-content-center mt-4 mb-5 border-line'>
                                <img
                                    src={frontImage}
                                    className='img-fluid on-boarding-image'
                                    alt='Bharat Api Logo'
                                />
                            </div>
                        </div>

                        <div className='col-lg-6'>
                            <Formik
                                initialValues={{
                                    radioOption: '',
                                    organisation: '',
                                    applicationNo: '',
                                    captcha: '',
                                }}
                                onSubmit={formSubmit}
                                validationSchema={validationRules}
                                validate={(values) =>
                                    fieldValidation({ values, error })
                                }
                            >
                                {({ values, isSubmitting, setFieldValue }) => (
                                    <Form autoComplete='off'>
                                        <div className='card2'>
                                            <div className='register-radio-choice'>
                                                <RadioOption
                                                    name='radioOption'
                                                    value='esign-completion'
                                                    onChangeHandler={() =>
                                                        clearFields(
                                                            setFieldValue
                                                        )
                                                    }
                                                >
                                                    Complete e-signing process
                                                </RadioOption>
                                            </div>

                                            {values.radioOption ===
                                            'esign-completion' ? (
                                                <ApplicationNumber
                                                    name='applicationNo'
                                                    setErrorStatus={(
                                                        errorObject
                                                    ) => {
                                                        updateError({
                                                            api: {
                                                                applicationNo: [
                                                                    ...error.api
                                                                        .applicationNo,
                                                                    errorObject,
                                                                ],
                                                            },
                                                        });
                                                    }}
                                                />
                                            ) : null}

                                            <span>--- OR ---</span>

                                            <div className='register-radio-choice'>
                                                <RadioOption
                                                    name='radioOption'
                                                    value='register-publisher'
                                                    onChangeHandler={() =>
                                                        clearFields(
                                                            setFieldValue
                                                        )
                                                    }
                                                >
                                                    Register for Publisher
                                                    Account
                                                </RadioOption>
                                            </div>

                                            {values.radioOption ===
                                            'register-publisher' ? (
                                                <>
                                                    <div className='choose-organisation'>
                                                        <span>
                                                            Choose organisation
                                                        </span>

                                                        <div className='mt-2'>
                                                            <div className='form-check'>
                                                                <RadioOption
                                                                    name='organisation'
                                                                    value='nic'
                                                                >
                                                                    NIC
                                                                </RadioOption>
                                                            </div>

                                                            <div className='form-check'>
                                                                <RadioOption
                                                                    name='organisation'
                                                                    value='govt'
                                                                >
                                                                    Government
                                                                </RadioOption>
                                                            </div>

                                                            <div className='form-check'>
                                                                <RadioOption
                                                                    name='organisation'
                                                                    value='others'
                                                                >
                                                                    Others
                                                                </RadioOption>
                                                            </div>
                                                        </div>
                                                    </div>

                                                    <div className='error'>
                                                        <ErrorMessage name='organisation' />
                                                    </div>
                                                </>
                                            ) : null}

                                            <div className='error'>
                                                <ErrorMessage name='radioOption' />
                                            </div>

                                            <CaptchaBox
                                                name='captcha'
                                                setErrorStatus={(message) => {
                                                    updateError({
                                                        api: {
                                                            ...error.api,
                                                            captcha: message,
                                                        },
                                                    });
                                                }}
                                            />

                                            <div className='row px-4 mt-2'>
                                                <button
                                                    type='submit'
                                                    className='btn btn-blue text-center'
                                                    disabled={isSubmitting}
                                                >
                                                    Register
                                                </button>
                                            </div>
                                        </div>
                                    </Form>
                                )}
                            </Formik>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

const RadioOption = ({ name, value, onChangeHandler, children }) => {
    const [field] = useField(name);

    return (
        <label className='form-check-label'>
            <input
                {...field}
                type='radio'
                value={value}
                checked={field.value === value}
                className='form-check-input'
                onChange={(e) => {
                    field.onChange(e);

                    if (onChangeHandler) onChangeHandler(e);
                }}
            />
            {children}
        </label>
    );
};

export default LandingPage;
