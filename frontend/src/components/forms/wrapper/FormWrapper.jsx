import React from 'react';

import NicForm from '../nic/Forms';
import GovtForm from '../govt/Forms';
import OtherForm from '../other/Form';
import './styles-form.css';

const FormWrapper = ({ organisation, applicationForm, onSubmitHandler }) => (
    <>
        <div className='mt-1'>
            <span>
                Fill application form for on-boarding as Publisher Admin on NIC
                API Exchange (NAPIX) Platform -
            </span>
        </div>

        {organisation === 'nic' ? (
            <NicForm
                formData={applicationForm}
                handleSubmit={onSubmitHandler}
            />
        ) : null}
        {organisation === 'govt' ? (
            <GovtForm
                formData={applicationForm}
                handleSubmit={onSubmitHandler}
            />
        ) : null}
        {organisation === 'others' ? (
            <OtherForm
                formData={applicationForm}
                handleSubmit={onSubmitHandler}
            />
        ) : null}
    </>
);

export default FormWrapper;
