import React from 'react';

import * as PreviewField from '../PreviewElements';

const PreviewApplication = ({ formData }) => {
    const { hog, hod, techAdmin1, techAdmin2 } = formData;

    return (
        <>
            <h4>Head of Group (HoG) details</h4>

            <div className='form-body'>
                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.EmployeeCode value={hog.code} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.GroupName value={formData.groupName} />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.EmployeeName value={hog.name} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.Designation value={hog.designation} />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.Address value={hog.address} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.Department value={hog.department} />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.EmailId value={hog.emailId} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.MobileNumber value={hog.mobileNo} />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.LandlineNumber value={hog.landlineNo} />
                    </div>
                </div>
            </div>

            <h4>Technical Administrators details</h4>
            <h5>Head of Department (HoD) details</h5>

            <div className='form-body'>
                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.EmployeeCode value={hod.code} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.EmployeeName value={hod.name} />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.Designation value={hod.designation} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.Department value={hod.department} />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.Address value={hod.address} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.EmailId value={hod.emailId} />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.MobileNumber value={hod.mobileNo} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.LandlineNumber value={hod.landlineNo} />
                    </div>
                </div>
            </div>

            <h5>Technical Admin - I (NIC Officer only)</h5>

            <div className='form-body'>
                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.EmployeeCode value={techAdmin1.code} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.EmployeeName value={techAdmin1.name} />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.Designation
                            value={techAdmin1.designation}
                        />
                    </div>
                    <div className='col-6'>
                        <PreviewField.Department
                            value={techAdmin1.department}
                        />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.Address value={techAdmin1.address} />
                    </div>
                    <div className='col-6'>
                        <PreviewField.EmailId value={techAdmin1.emailId} />
                    </div>
                </div>

                <div className='row'>
                    <div className='col-6'>
                        <PreviewField.MobileNumber
                            value={techAdmin1.mobileNo}
                        />
                    </div>
                    <div className='col-6'>
                        <PreviewField.LandlineNumber
                            value={techAdmin1.landlineNo}
                        />
                    </div>
                </div>
            </div>

            {techAdmin2.code.trim().length ? (
                <>
                    <h5>Technical Admin - II (NIC Officer only)</h5>
                    <div className='form-body'>
                        <div className='row'>
                            <div className='col-6'>
                                <PreviewField.EmployeeCode
                                    value={techAdmin2.code}
                                />
                            </div>
                            <div className='col-6'>
                                <PreviewField.EmployeeName
                                    value={techAdmin2.name}
                                />
                            </div>
                        </div>

                        <div className='row'>
                            <div className='col-6'>
                                <PreviewField.Designation
                                    value={techAdmin2.designation}
                                />{' '}
                            </div>
                            <div className='col-6'>
                                <PreviewField.Department
                                    value={techAdmin2.department}
                                />
                            </div>
                        </div>

                        <div className='row'>
                            <div className='col-6'>
                                <PreviewField.Address
                                    value={techAdmin2.address}
                                />
                            </div>
                            <div className='col-6'>
                                <PreviewField.EmailId
                                    value={techAdmin2.emailId}
                                />
                            </div>
                        </div>

                        <div className='row'>
                            <div className='col-6'>
                                <PreviewField.MobileNumber
                                    value={techAdmin2.mobileNo}
                                />
                            </div>
                            <div className='col-6'>
                                <PreviewField.LandlineNumber
                                    value={techAdmin2.landlineNo}
                                />
                            </div>
                        </div>
                    </div>
                </>
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
