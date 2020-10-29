import React from 'react';

import NicPreview from '../nic/Preview';
import GovtPreview from '../govt/Preview';
import OtherPreview from '../other/Preview';
import './styles-preview.css';

const PreviewWrapper = ({
    organisation,
    applicationForm,
    disableSubmitButton,
    proceedToSubmission,
}) => (
    <>
        <div id='preview-request'>
            <div className='heading-preview'>
                <h3>Preview Application Details</h3>
            </div>

            <div className='form-content'>
                {organisation === 'nic' ? (
                    <NicPreview formData={applicationForm} />
                ) : null}

                {organisation === 'govt' ? (
                    <GovtPreview formData={applicationForm} />
                ) : null}

                {organisation === 'others' ? (
                    <OtherPreview formData={applicationForm} />
                ) : null}
            </div>

            <div className='form-button'>
                <button
                    type='button'
                    className='btn btn-secondary'
                    onClick={() => proceedToSubmission(false)}
                >
                    Back
                </button>
                <button
                    type='button'
                    className='btn btn-primary'
                    disabled={disableSubmitButton}
                    onClick={() => proceedToSubmission(true)}
                >
                    {disableSubmitButton ? (
                        <>
                            <span
                                class='spinner-border spinner-border-sm'
                                role='status'
                                aria-hidden='true'
                            ></span>
                            Submitting...
                        </>
                    ) : (
                        'Submit'
                    )}
                </button>
            </div>
        </div>
    </>
);

export default PreviewWrapper;
