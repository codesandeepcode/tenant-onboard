import React from 'react';
import { Modal, ModalBody } from 'react-bootstrap';

import './styles.css';
const TermsAndConditionsModal = ({
    show,
    closePopupModal, // func to close modal
    agreementStatus, // true / false / null
}) => {
    const status = (flag) => {
        agreementStatus(flag);
        closePopupModal();
    };

    return (
        <Modal
            show={show}
            centered
            size='lg'
            dialogClassName='terms-and-conditions-modal'
            backdrop='static'
            keyboard={false}
            className='terms-conditions-model'
        >
            <ModalBody>
                <h5 className='text-center mb-4 mt-2'>
                    Obligations and Responsibilities of National API Exchange
                    Platform (NAPIX) users from concerned Government
                    Ministry/Department/Organization
                </h5>

                <ol className='mb-4 mx-4'>
                    <li>
                        Concerned Government Ministry/Department/Organization
                        shall be solely responsible for all the information,
                        content and data stored on the Servers. Concerned
                        Government Ministry/Department/Organization further
                        acknowledges that it shall be solely responsible to
                        undertake and maintain complete authenticity of the
                        information/data sent and/or received. Concerned
                        Government Ministry/Department/Organization shall also
                        take necessary measures to ensure that information/data
                        transmitted is authentic and consistent.
                    </li>
                    <li>
                        Concerned Government Ministry/Department/Organization
                        shall keep the account information such as userid,
                        password provided for accessing NIC API Exchange
                        Platform (NAPIX) in safe custody to avoid any misuse by
                        unauthorised users.
                    </li>
                    <li>
                        I hereby authorize NAPIX team to deactivate the id in
                        case of misuse/abuse.
                    </li>
                    <li>
                        I confirm that I will not host any application/content
                        which belongs to Top Secret and Secret Categories of
                        classification.
                    </li>
                    <li>
                        I confirm that the API(s) to be hosted/deployed on NIC
                        API Exchange Platform (NAPIX) follow both policies -
                        'Policy on Open Application Programming Interfaces
                        (APIs) for Government of India' and 'National Data
                        Sharing and Accessibility Policy (NDSAP) - 2012'.
                    </li>
                    <li>
                        I confirm that I have already obtained consent for
                        hosting/publishing API(s) in NIC API Exchange Platform
                        (NAPIX).
                    </li>
                    <li>
                        It is at the discrete level of API Owners(Publishers) to
                        verify the subscriber’s/developer’s details submitted
                        before authorising them to subscribe/consume their API.
                    </li>
                    <li>
                        NAPIX team doesn’t holds any responsibility in
                        whatsoever manner how the subscriber(s)/developer(s)
                        will be using the data/service provided by API. The
                        usage is the consent between the owner of API and the
                        respective subscriber/developer.
                    </li>
                </ol>

                {agreementStatus !== null ? (
                    <>
                        <div className='mx-2'>
                            By clicking on 'I Agree' button, I declare that I
                            have read the terms and conditions given above and
                            agree to abide by them. I shall be the single point
                            of contact for this account. I will be responsible
                            for any misuse of the service/violation of the
                            clauses mentioned above. NIC reserves the right to
                            deactivate the service in case of any violation.
                        </div>

                        <div className='action-buttons text-center mt-4 mb-3'>
                            <button
                                type='button'
                                className='btn btn-secondary'
                                onClick={() => status(false)}
                            >
                                Cancel
                            </button>
                            <button
                                type='submit'
                                className='btn btn-primary'
                                onClick={() => status(true)}
                            >
                                I Agree
                            </button>
                        </div>
                    </>
                ) : (
                    <>
                        <div className='action-buttons text-center mt-4 mb-3'>
                            <button
                                type='button'
                                className='btn btn-secondary'
                                onClick={closePopupModal}
                            >
                                Close
                            </button>
                        </div>
                    </>
                )}
            </ModalBody>
        </Modal>
    );
};

export default TermsAndConditionsModal;
