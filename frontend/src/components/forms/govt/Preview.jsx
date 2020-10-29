import React from 'react';

import * as PreviewField from '../PreviewElements';

const PreviewApplication = ({ formData }) => {
    const { office, projectHead, technicalHead } = formData;

    return (
        <>
            <PreviewField.GovernmentType value={formData.govtType} />

            <h4>Office Details</h4>

            <div className='form-body'>
                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.OfficeDepartment
                            value={office.department}
                            governmentType={formData.govtType}
                        />
                    </div>
                    <div className='col-6'>
                        <PreviewField.State stateId={office.state} />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.OfficeCategory value={office.category} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.OfficeName value={office.name} />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.OfficeAddress value={office.address} />
                    </div>
                </div>
            </div>

            <h4>Project Head Details</h4>

            <div className='form-body'>
                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.EmployeeCode value={projectHead.code} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.EmployeeName value={projectHead.name} />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.Designation
                            value={projectHead.designation}
                        />
                    </div>
                    <div className='col-6'>
                        <PreviewField.GovernmentOfficeGroupName
                            value={projectHead.group}
                        />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.EmailId value={projectHead.emailId} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.MobileNumber
                            value={projectHead.mobileNo}
                        />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.LandlineNumber
                            value={projectHead.landlineNo}
                        />
                    </div>
                </div>
            </div>

            {technicalHead.length ? (
                <div>
                    <h4>Technical Administrators Details</h4>

                    {technicalHead.map((item, index) => (
                        <div key={index}>
                            <h5>TechnicalHead - {index + 1}</h5>

                            <div className='form-body'>
                                <div className='row'>
                                    <div className='col-6'>
                                        <PreviewField.EmployeeCode
                                            value={item.code}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <PreviewField.EmployeeName
                                            value={item.name}
                                        />
                                    </div>
                                </div>

                                <div className='row'>
                                    <div className='col-6'>
                                        <PreviewField.Designation
                                            value={item.designation}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <PreviewField.EmailId
                                            value={item.emailId}
                                        />
                                    </div>
                                </div>

                                <div className='row'>
                                    <div className='col-6'>
                                        <PreviewField.MobileNumber
                                            value={item.mobileNo}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <PreviewField.LandlineNumber
                                            value={item.landlineNo}
                                        />
                                    </div>
                                </div>
                            </div>
                        </div>
                    ))}
                </div>
            ) : null}

            <div className='form-body-end'>
                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.DomainName value={formData.domainName} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.ProjectName
                            value={formData.projectName}
                        />
                    </div>
                </div>
            </div>
        </>
    );
};

export default PreviewApplication;
