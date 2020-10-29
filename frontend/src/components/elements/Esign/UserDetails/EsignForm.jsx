import React, { useState } from 'react';
import { Formik, Form, useField } from 'formik';
import * as Yup from 'yup';

import './styles.css';
import {
    urlToDownloadPdfApplication,
    urlToSubmitFormToEsignService,
} from '../../../../Apis';

const EsignForm = ({ applicationNo }) => {
    const [personName, setPersonName] = useState('');

    return (
        <div id='esign-form'>
            <div className='container my-5'>
                <div className='col-sm-10 col-md-7 col-lg-6 mx-auto'>
                    <Formik
                        initialValues={{ userName: '' }}
                        validationSchema={Yup.object({
                            userName: Yup.string()
                                .required('This field is required')
                                .matches(/^[a-zA-Z ]*$/, {
                                    message: 'Only letters are allowed',
                                    excludeEmptyString: true,
                                }),
                        })}
                        onSubmit={(values, { setSubmitting }) => {
                            setPersonName(values.userName);
                            setSubmitting(false);
                            document.getElementById('start-esign').submit();
                        }}
                    >
                        <Form autoComplete='off' className='wrapper shadow-lg'>
                            <h1>One more step ...</h1>

                            <div className='form-content'>
                                <div className='reference-no'>
                                    Application No.
                                    <strong>{applicationNo}</strong>
                                </div>

                                <UserInput name='userName' />
                            </div>

                            <div className='action-buttons'>
                                <a
                                    href={urlToDownloadPdfApplication(
                                        applicationNo
                                    )}
                                    className='btn btn-secondary'
                                >
                                    View Application
                                </a>
                                <button
                                    type='submit'
                                    className='btn btn-success'
                                >
                                    Continue
                                </button>
                            </div>
                        </Form>
                    </Formik>
                </div>
            </div>

            <form
                id='start-esign'
                action={urlToSubmitFormToEsignService()}
                method='POST'
            >
                <input
                    type='hidden'
                    name='applicationNo'
                    value={applicationNo}
                />
                <input type='hidden' name='signingPerson' value={personName} />
            </form>
        </div>
    );
};

const UserInput = ({ name }) => {
    const [field, meta] = useField(name);

    return (
        <div className='user-input'>
            <div className='form-group'>
                <input
                    {...field}
                    placeholder='Full name'
                    className={
                        'form-control' +
                        (meta.error && meta.touched ? ' is-invalid' : '')
                    }
                />
                {meta.touched && meta.error ? (
                    <div className='invalid-feedback'>{meta.error}</div>
                ) : null}
            </div>
            <small className='form-text text-muted'>
                (Please provide name of person e-signing this application)
            </small>
        </div>
    );
};

export default EsignForm;
