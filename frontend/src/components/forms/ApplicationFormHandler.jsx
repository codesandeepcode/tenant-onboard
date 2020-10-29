import React, { useState } from 'react';
import { Switch, Route } from 'react-router-dom';

import './styles.css';
import FormWrapper from './wrapper/FormWrapper';
import PreviewWrapper from './wrapper/PreviewWrapper';
import {
    submitApplicationFormForGovernment,
    submitApplicationFormForNic,
    submitApplicationFormForOthers,
} from '../../Apis';
import ErrorPopupBox from '../elements/ErrorPopupBox';

const ApplicationFormHandler = ({
    match: { path },
    location: {
        state: { organisation },
    },
    history,
}) => {
    const [applicationForm, setApplicationForm] = useState(null);
    const [showErrorPopup, setErrorPopup] = useState(false);
    const [previewSubmit, setPreviewSubmitStatus] = useState(false);

    const applicationFormSubmitHandler = (values) => {
        setApplicationForm(values);
        history.push(`${path}/preview`, { organisation });
    };

    const previewFormSubmitHandler = (shouldApplicationSubmit) => {
        if (shouldApplicationSubmit) {
            setPreviewSubmitStatus(true);

            let apiObject;
            switch (organisation) {
                case 'nic':
                    apiObject = submitApplicationFormForNic(applicationForm);
                    break;
                case 'govt':
                    apiObject = submitApplicationFormForGovernment(
                        applicationForm
                    );
                    break;
                case 'others':
                    apiObject = submitApplicationFormForOthers(applicationForm);
                    break;
                default:
                    throw new Error(
                        `Cannot locate case for given value - '${organisation}'`
                    );
            }

            apiObject
                .then((response) => response.data)
                .then(({ referenceNo }) =>
                    history.push(`${path}/success`, { referenceNo })
                )
                .catch(() => {
                    setErrorPopup(true);
                    setPreviewSubmitStatus(false);
                });
        } else {
            history.push(`${path}`, { organisation });
        }
    };

    const getLabel = () => {
        switch (organisation) {
            case 'nic':
                return 'NIC';
            case 'govt':
                return 'Government';
            case 'others':
                return 'Others';
            default:
                throw new Error(
                    `Cannot locate case for given value - '${organisation}'`
                );
        }
    };

    return (
        <div className='container heading'>
            <div className='mt-4'>
                <h4>Government of India</h4>
                <h4 className='heading-department'>
                    Ministry of Electronics & Information Technology
                </h4>
                <h6>(Registration/Authorisation format)</h6>
            </div>
            <h2>On-Boarding Request Form ({getLabel()})</h2>

            <ErrorPopupBox
                show={showErrorPopup}
                message='Error in registering application!'
                handleClose={() => setErrorPopup(false)}
            />

            <Switch>
                <Route path={`${path}`} exact>
                    <FormWrapper
                        organisation={organisation}
                        applicationForm={applicationForm}
                        onSubmitHandler={applicationFormSubmitHandler}
                    />
                </Route>
                <Route path={`${path}/preview`} exact>
                    <PreviewWrapper
                        organisation={organisation}
                        applicationForm={applicationForm}
                        disableSubmitButton={previewSubmit}
                        proceedToSubmission={previewFormSubmitHandler}
                    />
                </Route>
            </Switch>
        </div>
    );
};

export default ApplicationFormHandler;
