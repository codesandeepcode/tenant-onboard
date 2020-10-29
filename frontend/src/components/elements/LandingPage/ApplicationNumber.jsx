import React from 'react';
import { useField, useFormikContext } from 'formik';

import { verifyIfApplicationNumberIsValid } from '../../../Apis';

const ApplicationNumber = ({ name, setErrorStatus }) => {
    const { setSubmitting, setFieldError } = useFormikContext();

    const verifyInput = (applicationNo) => {
        if (!applicationNo) return;

        setSubmitting(true);

        verifyIfApplicationNumberIsValid(applicationNo)
            .then(() => {
                setFieldError(name, '');
                setSubmitting(false);
            })
            .catch((err) => {
                let message = 'Unknown exception occured!';
                if (err.response && err.response.status === 400) {
                    message = err.response.data.errors[0];
                    setErrorStatus({ applicationNo, message });
                }

                setFieldError(name, message);
                setSubmitting(false);
            });
    };

    const [field, meta] = useField(name);

    return (
        <div className='form-group'>
            <input
                {...field}
                placeholder='Enter your Application Reference no'
                className={
                    'form-control' +
                    (meta.error && meta.touched ? ' is-invalid' : '')
                }
                onBlur={(e) => {
                    field.onBlur(e);

                    if (!e.target.value) return;
                    if (meta.error) return;

                    verifyInput(e.target.value);
                }}
                maxLength={11}
            />

            {meta.touched && meta.error ? (
                <div className='invalid-feedback'>{meta.error}</div>
            ) : null}
        </div>
    );
};

export default ApplicationNumber;
