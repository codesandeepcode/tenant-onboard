import React from 'react';
import { Formik, Form, FieldArray } from 'formik';
import * as Yup from 'yup';

import * as message from '../messages';
import * as FormElements from '../FormElements';

const initialFormValues = {
    office: {
        department: '',
        state: '',
        category: '',
        name: '',
        address: '',
    },
    projectHead: {
        code: '',
        name: '',
        designation: '',
        group: '',
        emailId: '',
        mobileNo: '',
        landlineNo: '',
    },
    technicalHead: [
        {
            code: '',
            name: '',
            designation: '',
            emailId: '',
            mobileNo: '',
            landlineNo: '',
        },
    ],
    govtType: '',
    domainName: '',
    projectName: '',
    confirmPort: false,
    acceptTerms: false,
};

const validationRules = Yup.object({
    govtType: Yup.string().required(message.govt_type_required),
    office: Yup.object({
        department: Yup.string().required(message.required),
        state: Yup.string().required(message.required),
        category: Yup.string().required(message.required),
        name: Yup.string()
            .required(message.required)
            .max(99, message.max_length)
            .matches(
                /^[a-zA-Z]+( [a-zA-Z]+)*$/,
                message.letter_with_whitespace
            ),
        address: Yup.string()
            .required(message.required)
            .max(99, message.max_length)
            .matches(/^[a-zA-Z0-9\s.,-]*$/, message.invalid_address),
    }),
    projectHead: Yup.object({
        code: Yup.string()
            .required(message.required)
            .max(20, message.max_length),
        name: Yup.string()
            .required(message.required)
            .max(99, message.max_length)
            .matches(/^(([A-Z][a-z]*([ ]+)?)+)$/, {
                message: message.invalid_person_name,
                excludeEmptyString: true,
            }),
        designation: Yup.string()
            .required(message.required)
            .max(30, message.max_length),
        group: Yup.string()
            .required(message.required)
            .max(30, message.max_length),
        emailId: Yup.string()
            .required(message.required)
            .max(99, message.max_email_length)
            .matches(
                /^[_a-zA-Z0-9-]+(\.[_a-zA-Z0-9-]+)*@[a-z]+\.((com)|(in))$/,
                {
                    message: message.invalid_email,
                    excludeEmptyString: true,
                }
            ),
        mobileNo: Yup.string()
            .required(message.required)
            .min(10, message.min_mobile_no_length)
            .max(10, message.max_mobile_no_length)
            .matches(/^[789][0-9]*$/, {
                message: message.invalid_mobile_no,
                excludeEmptyString: true,
            }),
        landlineNo: Yup.string()
            .max(15, message.max_landline_no_length)
            .matches(/^[0-9]*$/, {
                message: message.only_number_type,
                excludeEmptyString: true,
            }),
    }),
    technicalHead: Yup.array().of(
        Yup.object().shape({
            code: Yup.string()
                .required(message.required)
                .max(20, message.max_length),
            name: Yup.string()
                .required(message.required)
                .max(99, message.max_length)
                .matches(/^(([A-Z][a-z]*([ ]+)?)+)$/, {
                    message: message.invalid_person_name,
                    excludeEmptyString: true,
                }),
            designation: Yup.string()
                .required(message.required)
                .max(30, message.max_length),
            emailId: Yup.string()
                .required(message.required)
                .max(99, message.max_email_length)
                .matches(
                    /^[_a-zA-Z0-9-]+(\.[_a-zA-Z0-9-]+)*@[a-z]+\.((com)|(in))$/,
                    {
                        message: message.invalid_email,
                        excludeEmptyString: true,
                    }
                ),
            mobileNo: Yup.string()
                .required(message.required)
                .min(10, message.min_mobile_no_length)
                .max(10, message.max_mobile_no_length)
                .matches(/^[789][0-9]*$/, {
                    message: message.invalid_mobile_no,
                    excludeEmptyString: true,
                }),
            landlineNo: Yup.string()
                .max(15, message.max_landline_no_length)
                .matches(/^[0-9]*$/, {
                    message: message.only_number_type,
                    excludeEmptyString: true,
                }),
        })
    ),
    domainName: Yup.string()
        .required(message.required)
        .max(30, message.max_length)
        .matches(/^([a-z0-9.-]*)\.((nic\.in)|(gov\.in))$/, {
            message: message.invalid_domain_name,
            excludeEmptyString: true,
        }),
    projectName: Yup.string()
        .required(message.required)
        .max(30, message.max_length)
        .matches(/^[a-zA-Z]+( [a-zA-Z]+)*$/, {
            message: message.letter_with_whitespace,
            excludeEmptyString: true,
        }),
    confirmPort: Yup.boolean().oneOf([true], message.confirm_port).required(),
    acceptTerms: Yup.boolean()
        .oneOf([true], message.accept_term_and_conditions)
        .required(),
});

const getMaxLength = (fieldName) => {
    const maxRule = Yup.reach(validationRules, fieldName);
    return maxRule.tests.find((e) => e.OPTIONS.name === 'max').OPTIONS.params
        .max;
};

const isFieldRequired = (fieldName) => {
    const requiredRule = Yup.reach(validationRules, fieldName);
    return (
        requiredRule.tests.find((e) => e.OPTIONS.name === 'required') !==
        undefined
    );
};

class ApplicationForm extends React.Component {
    constructor() {
        super();

        this.state = {
            apiErrors: {
                domain: [],
            },
        };
    }

    render() {
        return (
            <Formik
                initialValues={this.props.formData || initialFormValues}
                onSubmit={this.props.handleSubmit}
                validationSchema={validationRules}
            >
                {(actions) => (
                    <Form autoComplete='off' id='application-form'>
                        <div className='form-content'>
                            <FormElements.SpecifyGovtType
                                name='govtType'
                                required={isFieldRequired('govtType')}
                            />

                            <h4>Office Details</h4>
                            <div className='form-body'>
                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.OfficeDepartment
                                            name='office.department'
                                            governmentType={
                                                actions.values.govtType
                                            }
                                            required={isFieldRequired(
                                                'office.department'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.State
                                            name='office.state'
                                            required={isFieldRequired(
                                                'office.state'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.OfficeCategory
                                            name='office.category'
                                            required={isFieldRequired(
                                                'office.category'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.OfficeName
                                            name='office.name'
                                            required={isFieldRequired(
                                                'office.name'
                                            )}
                                            maxLength={getMaxLength(
                                                'office.name'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.StateAddress
                                            name='office.address'
                                            required={isFieldRequired(
                                                'office.address'
                                            )}
                                            maxLength={getMaxLength(
                                                'office.address'
                                            )}
                                            label='Office Address'
                                            placeholder='Location/Address of Office'
                                        />
                                    </div>
                                </div>
                            </div>

                            <h4>Project Head Details</h4>
                            <div className='form-body'>
                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.EmployeeCode
                                            name='projectHead.code'
                                            required={isFieldRequired(
                                                'projectHead.code'
                                            )}
                                            maxLength={getMaxLength(
                                                'projectHead.code'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.EmployeeName
                                            name='projectHead.name'
                                            required={isFieldRequired(
                                                'projectHead.name'
                                            )}
                                            maxLength={getMaxLength(
                                                'projectHead.name'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.Designation
                                            name='projectHead.designation'
                                            required={isFieldRequired(
                                                'projectHead.designation'
                                            )}
                                            maxLength={getMaxLength(
                                                'projectHead.designation'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.GroupNameTextField
                                            name='projectHead.group'
                                            required={isFieldRequired(
                                                'projectHead.group'
                                            )}
                                            maxLength={getMaxLength(
                                                'projectHead.group'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.EmailId
                                            name='projectHead.emailId'
                                            required={isFieldRequired(
                                                'projectHead.emailId'
                                            )}
                                            maxLength={getMaxLength(
                                                'projectHead.emailId'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.MobileNumber
                                            name='projectHead.mobileNo'
                                            required={isFieldRequired(
                                                'projectHead.mobileNo'
                                            )}
                                            maxLength={getMaxLength(
                                                'projectHead.mobileNo'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.LandlineNumber
                                            name='projectHead.landlineNo'
                                            required={isFieldRequired(
                                                'projectHead.landlineNo'
                                            )}
                                            maxLength={getMaxLength(
                                                'projectHead.landlineNo'
                                            )}
                                        />
                                    </div>
                                </div>
                            </div>

                            <h4>Technical Administrators Details</h4>
                            <FieldArray
                                name='technicalHead'
                                component={FormElements.TechnicalHeadSection}
                            />

                            <div className='form-body-end'>
                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.DomainName
                                            name='domainName'
                                            setErrorStatus={(error) =>
                                                this.setState({
                                                    apiErrors: {
                                                        domain: [
                                                            ...this.state
                                                                .apiErrors
                                                                .domain,
                                                            error,
                                                        ],
                                                    },
                                                })
                                            }
                                            required={isFieldRequired(
                                                'domainName'
                                            )}
                                            maxLength={getMaxLength(
                                                'domainName'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.ProjectName
                                            name='projectName'
                                            required={isFieldRequired(
                                                'projectName'
                                            )}
                                            maxLength={getMaxLength(
                                                'projectName'
                                            )}
                                        />
                                    </div>
                                </div>
                            </div>

                            <div className='form-end-checkbox'>
                                <FormElements.ConfirmPort443Check
                                    name='confirmPort'
                                    required={true}
                                />

                                <FormElements.AcceptTermsAndConditions
                                    name='acceptTerms'
                                    required={true}
                                />
                            </div>

                            {!actions.isValid && actions.submitCount !== 0 ? (
                                <div className='main-error'>
                                    Your application form have errors! Please
                                    rectify them before submission.
                                </div>
                            ) : null}

                            <div className='form-button'>
                                <button
                                    type='reset'
                                    className='btn btn-secondary'
                                >
                                    Reset
                                </button>
                                <button
                                    type='submit'
                                    className='btn btn-primary'
                                >
                                    Submit
                                </button>
                            </div>
                        </div>
                    </Form>
                )}
            </Formik>
        );
    }
}

export default ApplicationForm;
