import React from 'react';
import { Formik, Form } from 'formik';
import * as Yup from 'yup';

import * as message from '../messages';
import * as FormElements from '../FormElements';
import { fetchNicEmployeeDetails } from '../../../Apis';

const initialFormValues = {
    hog: {
        code: '',
        name: '',
        designation: '',
        address: '',
        department: '',
        emailId: '',
        mobileNo: '',
        landlineNo: '',
    },
    hod: {
        code: '',
        name: '',
        designation: '',
        department: '',
        address: '',
        emailId: '',
        mobileNo: '',
        landlineNo: '',
    },
    techAdmin1: {
        code: '',
        name: '',
        designation: '',
        department: '',
        address: '',
        emailId: '',
        mobileNo: '',
        landlineNo: '',
    },
    techAdmin2: {
        code: '',
        name: '',
        designation: '',
        department: '',
        address: '',
        emailId: '',
        mobileNo: '',
        landlineNo: '',
    },
    groupName: '',
    domainName: '',
    projectName: '',
    confirmPort: false,
    acceptTerms: false,
    copyHod: false,
};

const validationRules = Yup.object({
    groupName: Yup.string().required(message.required),
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
    hog: Yup.object({
        code: Yup.string()
            .matches(/^[0-9]*$/, message.only_number_type)
            .max(6, message.max_length)
            .required(message.required),
    }),
    hod: Yup.object({
        code: Yup.string()
            .matches(/^[0-9]*$/, message.only_number_type)
            .max(6, message.max_length)
            .required(message.required),
    }),
    techAdmin1: Yup.object({
        code: Yup.string()
            .matches(/^[0-9]*$/, message.only_number_type)
            .max(6, message.max_length)
            .required(message.required),
    }),
    techAdmin2: Yup.object({
        code: Yup.string()
            .matches(/^[0-9]*$/, message.only_number_type)
            .max(6, message.max_length),
    }),
    confirmPort: Yup.boolean().oneOf([true], message.confirm_port),
    acceptTerms: Yup.boolean().oneOf(
        [true],
        message.accept_term_and_conditions
    ),
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

const clearFields = ({ values, category }) => {
    Object.keys(values[category])
        .filter((field) => field !== 'code')
        .map((field) => (values[category][field] = ''));
};

const validateFields = ({ state: { apiErrors }, values }) => {
    const errors = {};
    const { employeeCode, domain } = apiErrors;

    const hogTest = employeeCode.hog.find((e) => e.code === values.hog.code);
    if (hogTest) {
        errors.hog = {
            code: hogTest.message,
        };
    }

    const hodTest = employeeCode.hod.find((e) => e.code === values.hod.code);
    if (hodTest) {
        errors.hod = {
            code: hodTest.message,
        };
    }

    const techAdmin1Test = employeeCode.techAdmin1.find(
        (e) => e.code === values.techAdmin1.code
    );
    if (techAdmin1Test) {
        errors.techAdmin1 = {
            code: techAdmin1Test.message,
        };
    }

    const techAdmin2Test = employeeCode.techAdmin2.find(
        (e) => e.code === values.techAdmin2.code
    );
    if (techAdmin2Test) {
        errors.techAdmin2 = {
            code: techAdmin2Test.message,
        };
    }

    const domainNameTest = domain.find((e) => e.value === values.domainName);
    if (domainNameTest) {
        errors.domainName = domainNameTest.message;
    }

    return errors;
};

class ApplicationForm extends React.Component {
    constructor() {
        super();

        this.state = {
            apiErrors: {
                employeeCode: {
                    hog: [],
                    hod: [],
                    techAdmin1: [],
                    techAdmin2: [],
                },
                domain: [],
            },
            writableFields: [],
        };
    }

    isDisabled = (name) => !this.state.writableFields.includes(name);

    updateEmployeeDetails = ({
        name,
        value: empCode,
        category,
        actions: { values, errors, setFieldError },
    }) => {
        const removeFieldsFromDisabled = () => {
            this.setState({
                writableFields: this.state.writableFields.filter(
                    (field) => !field.includes(category)
                ),
            });
        };

        const arr = name.split('.');
        const result =
            errors.hasOwnProperty(arr[0]) &&
            errors[arr[0]].hasOwnProperty(arr[1]);

        if (result || !empCode.trim().length) {
            clearFields({ values, category });
            removeFieldsFromDisabled();

            return;
        }

        let type;
        switch (category) {
            case 'hog':
                type = 'hog';
                break;
            case 'hod':
                type = 'hod';
                break;
            case 'techAdmin1':
                type = 'other-one';
                break;
            case 'techAdmin2':
                type = 'other-two';
                break;
            default:
                throw new Error(
                    `No valid case found for given category - ${category}`
                );
        }

        fetchNicEmployeeDetails(empCode, type)
            .then((response) => response.data)
            .then((data) => {
                removeFieldsFromDisabled();

                values[category].name = data.employeeName || '';
                values[category].designation = data.employeeDesignation || '';
                values[category].department = 'NIC';
                values[category].address = data.officeAddress || '';
                values[category].emailId = data.emailId || '';
                values[category].mobileNo = data.mobileNo || '';
                values[category].landlineNo = data.landlineNo || '';

                const writableFields = Object.keys(values[category])
                    .filter((field) => values[category][field].trim() === '')
                    .map((field) => `${category}.${field}`);

                this.setState({
                    writableFields: [
                        ...this.state.writableFields,
                        ...writableFields,
                    ],
                });
            })
            .catch((err) => {
                let message = 'Unknown exception occured! Please retry';
                if (err.response && err.response.status === 400) {
                    message = err.response.data.errors[0];
                    this.state.apiErrors.employeeCode[category].push({
                        code: empCode,
                        message,
                    });
                }

                clearFields({ values, category });
                setFieldError(`${category}.code`, message);
            });
    };

    copyHodDetails = ({
        event: {
            target: { name, checked },
        },
        actions: { values, setFieldValue },
    }) => {
        if (checked) {
            if (!values.hod.code) {
                // Check whether hod code is inputted and set checkbox as false if not so
                setFieldValue(name, false);
                return;
            }

            Object.keys(values.hod).forEach((field) =>
                setFieldValue(`techAdmin1.${field}`, values.hod[field])
            );
        } else {
            Object.keys(values.techAdmin1).forEach((field) =>
                setFieldValue(`techAdmin1.${field}`, '')
            );
        }
    };

    render() {
        return (
            <Formik
                initialValues={this.props.formData || initialFormValues}
                onSubmit={this.props.handleSubmit}
                validationSchema={validationRules}
                validate={(values) =>
                    validateFields({ state: this.state, values })
                }
            >
                {(actions) => (
                    <Form id='application-form' autoComplete='off'>
                        <div className='form-content'>
                            <h4>Head of Group (HoG) details</h4>
                            <div className='form-body'>
                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.NicEmployeeCode
                                            name='hog.code'
                                            required={isFieldRequired(
                                                'hog.code'
                                            )}
                                            maxLength={getMaxLength('hog.code')}
                                            processValue={(value) =>
                                                this.updateEmployeeDetails({
                                                    name: 'hog.code',
                                                    value,
                                                    category: 'hog',
                                                    actions,
                                                })
                                            }
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.GroupName
                                            name='groupName'
                                            employeeCode={
                                                actions.values.hog.code
                                            }
                                            required={isFieldRequired(
                                                'groupName'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.EmployeeName
                                            name='hog.name'
                                            disabled={this.isDisabled(
                                                'hog.name'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.Designation
                                            name='hog.designation'
                                            disabled={this.isDisabled(
                                                'hog.designation'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.StateAddress
                                            name='hog.address'
                                            disabled={this.isDisabled(
                                                'hog.address'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.Department
                                            name='hog.department'
                                            disabled={this.isDisabled(
                                                'hog.department'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.EmailId
                                            name='hog.emailId'
                                            disabled={this.isDisabled(
                                                'hog.emailId'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.MobileNumber
                                            name='hog.mobileNo'
                                            disabled={this.isDisabled(
                                                'hog.mobileNo'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.LandlineNumber
                                            name='hog.landlineNo'
                                            disabled={this.isDisabled(
                                                'hog.landlineNo'
                                            )}
                                        />
                                    </div>
                                </div>
                            </div>

                            <h4>Technical Administrators details</h4>
                            <h5>Head of Department (HoD) details</h5>

                            <div className='form-body'>
                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.NicEmployeeCode
                                            name='hod.code'
                                            required={isFieldRequired(
                                                'hod.code'
                                            )}
                                            maxLength={getMaxLength('hod.code')}
                                            processValue={(value) =>
                                                this.updateEmployeeDetails({
                                                    name: 'hod.code',
                                                    value,
                                                    category: 'hod',
                                                    actions,
                                                })
                                            }
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.EmployeeName
                                            name='hod.name'
                                            disabled={this.isDisabled(
                                                'hod.name'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.Designation
                                            name='hod.designation'
                                            disabled={this.isDisabled(
                                                'hod.designation'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.Department
                                            name='hod.department'
                                            disabled={this.isDisabled(
                                                'hod.department'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.StateAddress
                                            name='hod.address'
                                            disabled={this.isDisabled(
                                                'hod.address'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.EmailId
                                            name='hod.emailId'
                                            disabled={this.isDisabled(
                                                'hod.emailId'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.MobileNumber
                                            name='hod.mobileNo'
                                            disabled={this.isDisabled(
                                                'hod.mobileNo'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.LandlineNumber
                                            name='hod.landlineNo'
                                            disabled={this.isDisabled(
                                                'hod.landlineNo'
                                            )}
                                        />
                                    </div>
                                </div>
                            </div>

                            <FormElements.CopyHodDetails
                                name='copyHod'
                                changeHandler={(event) =>
                                    this.copyHodDetails({ event, actions })
                                }
                            />

                            <h5>Technical Admin - I (NIC Officer only)</h5>
                            <div className='form-body'>
                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.NicEmployeeCode
                                            name='techAdmin1.code'
                                            required={isFieldRequired(
                                                'techAdmin1.code'
                                            )}
                                            maxLength={getMaxLength(
                                                'techAdmin1.code'
                                            )}
                                            processValue={(value) =>
                                                this.updateEmployeeDetails({
                                                    name: 'techAdmin1.code',
                                                    value,
                                                    category: 'techAdmin1',
                                                    actions,
                                                })
                                            }
                                            disabled={actions.values.copyHod}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.EmployeeName
                                            name='techAdmin1.name'
                                            disabled={this.isDisabled(
                                                'techAdmin1.name'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.Designation
                                            name='techAdmin1.designation'
                                            disabled={this.isDisabled(
                                                'techAdmin1.designation'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.Department
                                            name='techAdmin1.department'
                                            disabled={this.isDisabled(
                                                'techAdmin1.department'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.StateAddress
                                            name='techAdmin1.address'
                                            disabled={this.isDisabled(
                                                'techAdmin1.address'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.EmailId
                                            name='techAdmin1.emailId'
                                            disabled={this.isDisabled(
                                                'techAdmin1.emailId'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.MobileNumber
                                            name='techAdmin1.mobileNo'
                                            disabled={this.isDisabled(
                                                'techAdmin1.mobileNo'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.LandlineNumber
                                            name='techAdmin1.landlineNo'
                                            disabled={this.isDisabled(
                                                'techAdmin1.landlineNo'
                                            )}
                                        />
                                    </div>
                                </div>
                            </div>

                            <h5>Technical Admin - II (NIC Officer only)</h5>
                            <div className='form-body'>
                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.NicEmployeeCode
                                            name='techAdmin2.code'
                                            required={isFieldRequired(
                                                'techAdmin2.code'
                                            )}
                                            maxLength={getMaxLength(
                                                'techAdmin2.code'
                                            )}
                                            processValue={(value) =>
                                                this.updateEmployeeDetails({
                                                    name: 'techAdmin2.code',
                                                    value,
                                                    category: 'techAdmin2',
                                                    actions,
                                                })
                                            }
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.EmployeeName
                                            name='techAdmin2.name'
                                            disabled={this.isDisabled(
                                                'techAdmin2.name'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.Designation
                                            name='techAdmin2.designation'
                                            disabled={this.isDisabled(
                                                'techAdmin2.designation'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.Department
                                            name='techAdmin2.department'
                                            disabled={this.isDisabled(
                                                'techAdmin2.department'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.StateAddress
                                            name='techAdmin2.address'
                                            disabled={this.isDisabled(
                                                'techAdmin2.address'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.EmailId
                                            name='techAdmin2.emailId'
                                            disabled={this.isDisabled(
                                                'techAdmin2.emailId'
                                            )}
                                        />
                                    </div>
                                </div>

                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.MobileNumber
                                            name='techAdmin2.mobileNo'
                                            disabled={this.isDisabled(
                                                'techAdmin2.mobileNo'
                                            )}
                                        />
                                    </div>
                                    <div className='col-6'>
                                        <FormElements.LandlineNumber
                                            name='techAdmin2.landlineNo'
                                            disabled={this.isDisabled(
                                                'techAdmin2.landlineNo'
                                            )}
                                        />
                                    </div>
                                </div>
                            </div>

                            <div className='form-body-end'>
                                <div className='form-row'>
                                    <div className='col-6'>
                                        <FormElements.DomainName
                                            name='domainName'
                                            required={isFieldRequired(
                                                'domainName'
                                            )}
                                            maxLength={getMaxLength(
                                                'domainName'
                                            )}
                                            setErrorStatus={(error) =>
                                                this.setState({
                                                    apiErrors: {
                                                        ...this.state.apiErrors,
                                                        domain: [
                                                            ...this.state
                                                                .apiErrors
                                                                .domain,
                                                            error,
                                                        ],
                                                    },
                                                })
                                            }
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
