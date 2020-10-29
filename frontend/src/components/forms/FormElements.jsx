import React, { useState, useEffect } from 'react';
import { ErrorMessage, useField, useFormikContext } from 'formik';

import TermsAndConditionsModal from './T&CModal';
import {
    InputTextField,
    TextAreaField,
    SelectField,
    SpecialCheckboxField,
} from './Common';
import {
    fetchNicGroupList,
    verifyDomainDoesNotExist,
    fetchMasterDepartmentList,
    fetchMasterStateList,
    fetchMasterOfficeCategoryList,
} from '../../Apis';
import { useDebounce } from '../utils';

export const EmployeeCode = ({ name, blurHandler, ...props }) => {
    const [field, meta] = useField({ name, type: 'text' });

    return (
        <InputTextField
            label='Employee Code'
            placeholder='Enter valid employee code'
            field={field}
            meta={meta}
            onBlur={(e) => {
                field.onBlur(e);
                if (blurHandler) blurHandler(e);
            }}
            {...props}
        />
    );
};

export const NicEmployeeCode = ({ name, processValue, ...props }) => {
    const [field, meta] = useField({ name, type: 'text' });
    const debouncedValue = useDebounce(field.value, 2000);

    useEffect(() => {
        processValue(debouncedValue);
    }, [debouncedValue]);

    return (
        <InputTextField
            label='Employee Code'
            placeholder='Enter valid employee code'
            field={field}
            meta={meta}
            {...props}
        />
    );
};

// Error Object - {value, message}
export const GroupName = ({ name, employeeCode, ...props }) => {
    const [field, meta, helper] = useField(name);
    const [list, setList] = useState([]);
    const { setSubmitting } = useFormikContext();
    const debouncedValue = useDebounce(employeeCode, 2000); // debounce of 2 seconds

    const clearField = () => {
        helper.setValue('');
        setList([]);
    };

    useEffect(() => {
        if (debouncedValue) {
            setSubmitting(true);

            fetchNicGroupList(debouncedValue)
                .then((response) => response.data)
                .then((data) => setList(data))
                .then(() => setSubmitting(false))
                .catch((err) => {
                    let message = 'Unknown exception occured!';
                    if (err.response && err.response.status === 400) {
                        message = err.response.data.errors[0];
                    }

                    setSubmitting(false);
                    clearField();
                    helper.setError(message);
                });
        } else clearField();
    }, [debouncedValue]);

    const selectObject = {
        defaultLabel: 'Select your Group',
        options: {
            id: 'id',
            value: 'name',
            list,
        },
    };

    return (
        <SelectField
            label='Group Name'
            field={field}
            meta={meta}
            data={selectObject}
            {...props}
        />
    );
};

export const EmployeeName = ({ name, ...props }) => {
    const [field, meta] = useField({ name, type: 'text' });

    return (
        <InputTextField
            label='Employee Name'
            placeholder='Employee Name'
            field={field}
            meta={meta}
            {...props}
        />
    );
};

export const Designation = ({ name, ...props }) => {
    const [field, meta] = useField({ name, type: 'text' });

    return (
        <InputTextField
            label='Designation'
            placeholder='Designation'
            field={field}
            meta={meta}
            {...props}
        />
    );
};

export const Department = ({ name, ...props }) => {
    const [field, meta] = useField({ name, type: 'text' });

    return (
        <InputTextField
            label='Department/Organisation'
            placeholder='Department/Organisation'
            field={field}
            meta={meta}
            {...props}
        />
    );
};

export const StateAddress = ({ name, ...props }) => {
    const [field, meta] = useField(name);

    return (
        <TextAreaField
            label='Address (State)'
            placeholder='Address'
            field={field}
            meta={meta}
            {...props}
        />
    );
};

export const EmailId = ({ name, ...props }) => {
    const [field, meta] = useField({ name, type: 'text' });

    return (
        <InputTextField
            label='Email Id'
            placeholder='Email Id'
            field={field}
            meta={meta}
            {...props}
        />
    );
};

export const MobileNumber = ({ name, ...props }) => {
    const [field, meta] = useField({ name, type: 'text' });

    return (
        <InputTextField
            label='Mobile Number'
            placeholder='Mobile Number'
            field={field}
            meta={meta}
            {...props}
        />
    );
};

export const LandlineNumber = ({ name, ...props }) => {
    const [field, meta] = useField({ name, type: 'text' });

    return (
        <InputTextField
            label='Landline Number'
            placeholder='Landline Number (Official)'
            field={field}
            meta={meta}
            {...props}
        />
    );
};

export const ProjectName = ({ name, ...props }) => {
    const [field, meta] = useField({ name, type: 'text' });

    return (
        <InputTextField
            label='Project Name'
            placeholder='Project Name'
            field={field}
            meta={meta}
            {...props}
        />
    );
};

// Error Object - {value, message}
export const DomainName = ({ name, setErrorStatus, ...props }) => {
    const [field, meta, helper] = useField({ name, type: 'text' });
    const debouncedValue = useDebounce(field.value, 2000);
    const { setSubmitting } = useFormikContext();

    useEffect(() => {
        if (!debouncedValue) return;
        if (meta.error) return;

        verifyDomainDoesNotExist(debouncedValue).catch((err) => {
            let message = 'Unknown exception occured!';
            if (err.response && err.response.status === 400) {
                message = err.response.data.errors[0];
                setErrorStatus({ value: debouncedValue, message });
            }

            helper.setError(message);
        });
    }, [debouncedValue]);

    return (
        <InputTextField
            label='Domain Name'
            placeholder='Domain Name'
            field={field}
            meta={meta}
            {...props}
        />
    );
};

export const ConfirmPort443Check = ({ name, ...props }) => {
    const [field, meta] = useField({ name, type: 'checkbox' });

    return (
        <SpecialCheckboxField
            label='I confirm that I have deployed the API(s) on port 443'
            field={field}
            meta={meta}
            {...props}
        />
    );
};

export const AcceptTermsAndConditions = ({ name, ...props }) => {
    const [field, meta, helpers] = useField(name);

    const [showModal, setShowModal] = useState(false); // controls modal popup
    const [simpleView, setSimpleView] = useState(false); // show simple view of modal

    const handleChange = (event) => {
        if (simpleView) return;

        const { checked } = event.target;
        if (checked) setShowModal(true);
        else helpers.setValue(false);
    };

    const setAgreementStatus = (status) => helpers.setValue(status);

    const closePopupModal = () => {
        setSimpleView(false);
        setShowModal(false);
    };

    return (
        <>
            <SpecialCheckboxField
                label={
                    <>
                        I agree to the{' '}
                        <span
                            className='terms-and-conditions-modal-span'
                            onClick={() => {
                                setSimpleView(true);
                                setShowModal(true);
                            }}
                        >
                            Terms and Conditions
                        </span>{' '}
                        of NAPIX Platform
                    </>
                }
                field={field}
                meta={meta}
                onChange={handleChange}
                {...props}
            />

            <TermsAndConditionsModal
                show={showModal}
                closePopupModal={closePopupModal}
                agreementStatus={!simpleView ? setAgreementStatus : null}
            />
        </>
    );
};

export const CopyHodDetails = ({ name, changeHandler }) => {
    const [field, meta] = useField({ name, type: 'checkbox' });

    return (
        <div className='form-group'>
            <div className='form-check'>
                <input
                    {...field}
                    id={field.name}
                    type='checkbox'
                    className={
                        'form-check-input' +
                        (meta.error && meta.touched ? ' is-invalid' : '')
                    }
                    onChange={(e) => {
                        field.onChange(e);
                        changeHandler(e);
                    }}
                />

                <label className='form-check-label' htmlFor={field.name}>
                    Technical Admin - I details are same as Head of Department
                    (HoD) details
                </label>

                {meta.touched && meta.error ? (
                    <div className='invalid-feedback is-invalid'>
                        {meta.error}
                    </div>
                ) : null}
            </div>
        </div>
    );
};

export const SpecifyGovtType = ({ name, required }) => {
    const [field_central] = useField({ name, type: 'radio', value: 'central' });
    const [field_state] = useField({ name, type: 'radio', value: 'state' });

    return (
        <div className='mt-2'>
            Specify type of government -
            {required ? <span className='imp'>*</span> : null}
            <div className='choose-govt-type mt-1'>
                <div className='custom-control custom-radio custom-control-inline'>
                    <input
                        {...field_central}
                        id='central-radio-button'
                        type='radio'
                        className='custom-control-input'
                    />

                    <label
                        className='custom-control-label'
                        htmlFor='central-radio-button'
                    >
                        Central Government
                    </label>
                </div>

                <div className='custom-control custom-radio custom-control-inline'>
                    <input
                        {...field_state}
                        id='state-radio-button'
                        type='radio'
                        className='custom-control-input'
                    />

                    <label
                        className='custom-control-label'
                        htmlFor='state-radio-button'
                    >
                        State Government
                    </label>
                </div>
            </div>
            <div className='error mt-2 ml-4'>
                <ErrorMessage name={name} />
            </div>
        </div>
    );
};

export const OfficeDepartment = ({ name, governmentType, ...props }) => {
    const [central, setCentral] = useState([]);
    const [state, setState] = useState([]);
    const [list, setCurrentList] = useState([]);

    const [field, meta, helper] = useField(name);

    useEffect(() => {
        const makeCall = async (type) => {
            try {
                const response = await fetchMasterDepartmentList(type);
                return response.data;
            } catch (err) {
                console.error('Error in fetching resources ', err);
                return [];
            }
        };

        makeCall('central').then((data) => setCentral(data));
        makeCall('state').then((data) => setState(data));
    }, []);

    useEffect(() => {
        switch (governmentType) {
            case 'central':
                setCurrentList(central);
                break;
            case 'state':
                setCurrentList(state);
                break;
            default:
                setCurrentList([]);
        }

        helper.setValue('');
    }, [governmentType]);

    const selectObject = {
        defaultLabel:
            governmentType === 'central'
                ? 'Select Ministry/Department'
                : 'Select Department',
        options: {
            id: 'id',
            value: 'name',
            list,
        },
    };

    return (
        <SelectField
            label={
                governmentType === 'central'
                    ? 'Ministry/Department'
                    : 'Department'
            }
            field={field}
            meta={meta}
            data={selectObject}
            {...props}
        />
    );
};

export const State = ({ name, ...props }) => {
    const [field, meta] = useField(name);
    const [list, setList] = useState([]);

    useEffect(() => {
        fetchMasterStateList()
            .then((response) => response.data)
            .then((data) => setList(data))
            .catch((err) => console.error('Error in fetching resources ', err));
    }, []);

    const selectObject = {
        defaultLabel: 'Select State',
        options: {
            id: 'code',
            value: 'name',
            list,
        },
    };

    return (
        <SelectField
            label='State'
            field={field}
            meta={meta}
            data={selectObject}
            {...props}
        />
    );
};

export const OfficeCategory = ({ name, ...props }) => {
    const [field, meta] = useField(name);
    const [list, setList] = useState([]);

    useEffect(() => {
        fetchMasterOfficeCategoryList()
            .then((response) => response.data)
            .then((data) => setList(data))
            .catch((err) => console.error('Error in fetching resources ', err));
    }, []);

    const selectObject = {
        defaultLabel: 'Select Office Category',
        options: {
            id: 'id',
            value: 'name',
            list,
        },
    };

    return (
        <SelectField
            label='Office Category'
            field={field}
            meta={meta}
            data={selectObject}
            {...props}
        />
    );
};

export const OfficeName = ({ name, ...props }) => {
    const [field, meta] = useField(name);

    return (
        <InputTextField
            label='Office Name'
            placeholder='Office Name'
            field={field}
            meta={meta}
            {...props}
        />
    );
};

export const GroupNameTextField = ({ name, ...props }) => {
    const [field, meta] = useField(name);

    return (
        <InputTextField
            label='Group/Project Name'
            placeholder='Group/Project Name'
            field={field}
            meta={meta}
            {...props}
        />
    );
};

export const TechnicalHeadSection = ({
    form: {
        values: { technicalHead },
    },
    push,
    remove,
}) => {
    const maxTechnicalHeads = 3;

    const addTechnicalHead = () => {
        push({
            code: '',
            name: '',
            designation: '',
            emailId: '',
            mobileNo: '',
            landlineNo: '',
        });
    };

    const removeTechnicalHead = (index) => remove(index);

    return (
        <>
            {technicalHead.map((head, index) => (
                // TODO: need to define unique value for key
                <div key={index} className='technical-head'>
                    {index !== 0 ? (
                        <svg
                            viewBox='0 0 16 16'
                            className='bi bi-x-square-fill'
                            xmlns='http://www.w3.org/2000/svg'
                            focusable='false'
                            onClick={() => removeTechnicalHead(index)}
                        >
                            <path
                                fillRule='evenodd'
                                d='M2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2zm9.854 4.854a.5.5 0 0 0-.708-.708L8 7.293 4.854 4.146a.5.5 0 1 0-.708.708L7.293 8l-3.147 3.146a.5.5 0 0 0 .708.708L8 8.707l3.146 3.147a.5.5 0 0 0 .708-.708L8.707 8l3.147-3.146z'
                            />
                        </svg>
                    ) : null}

                    <div className='technical-head-content'>
                        <h5>Technical Head - {index + 1}</h5>

                        <div className='form-body'>
                            <div className='form-row'>
                                <div className='col-6'>
                                    <EmployeeCode
                                        name={`technicalHead.${index}.code`}
                                        required={true}
                                        maxLength={20}
                                    />
                                </div>

                                <div className='col-6'>
                                    <EmployeeName
                                        name={`technicalHead.${index}.name`}
                                        required={true}
                                        maxLength={99}
                                    />
                                </div>
                            </div>

                            <div className='form-row'>
                                <div className='col-6'>
                                    <Designation
                                        name={`technicalHead.${index}.designation`}
                                        required={true}
                                        maxLength={30}
                                    />
                                </div>

                                <div className='col-6'>
                                    <EmailId
                                        name={`technicalHead.${index}.emailId`}
                                        required={true}
                                        maxLength={99}
                                    />
                                </div>
                            </div>

                            <div className='form-row'>
                                <div className='col-6'>
                                    <MobileNumber
                                        name={`technicalHead.${index}.mobileNo`}
                                        required={true}
                                        maxLength={10}
                                    />
                                </div>

                                <div className='col-6'>
                                    <LandlineNumber
                                        name={`technicalHead.${index}.landlineNo`}
                                        required={false}
                                        maxLength={15}
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            ))}

            {technicalHead.length < maxTechnicalHeads ? (
                <div className='add-technical-head' onClick={addTechnicalHead}>
                    <svg
                        viewBox='0 0 16 16'
                        className='bi bi-person-plus-fill'
                        xmlns='http://www.w3.org/2000/svg'
                        focusable='false'
                    >
                        <path
                            fillRule='evenodd'
                            d='M1 14s-1 0-1-1 1-4 6-4 6 3 6 4-1 1-1 1H1zm5-6a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm7.5-3a.5.5 0 0 1 .5.5v2a.5.5 0 0 1-.5.5h-2a.5.5 0 0 1 0-1H13V5.5a.5.5 0 0 1 .5-.5z'
                        />
                        <path
                            fillRule='evenodd'
                            d='M13 7.5a.5.5 0 0 1 .5-.5h2a.5.5 0 0 1 0 1H14v1.5a.5.5 0 0 1-1 0v-2z'
                        />
                    </svg>

                    <h5>Click to add another Technical Head</h5>
                </div>
            ) : null}
        </>
    );
};
