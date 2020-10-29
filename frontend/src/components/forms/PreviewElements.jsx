import React, { useEffect, useState } from 'react';

import {
    fetchMasterDepartmentWithId,
    fetchMasterOfficeCategoryWithId,
    fetchMasterStateWithId,
} from '../../Apis';

export const EmployeeCode = ({ value }) => (
    <div className='row'>
        <div className='col'>Employee Code</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);

export const EmployeeName = ({ value }) => (
    <div className='row'>
        <div className='col'>Employee Name</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);

export const Designation = ({ value }) => (
    <div className='row'>
        <div className='col'>Designation</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);

export const Department = ({ value }) => (
    <div className='row'>
        <div className='col'>Department</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);

export const Address = ({ value }) => (
    <div className='row'>
        <div className='col'>Address</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);

export const EmailId = ({ value }) => (
    <div className='row'>
        <div className='col'>Email Id</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);

export const MobileNumber = ({ value }) => (
    <div className='row'>
        <div className='col'>Mobile Number</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);

export const LandlineNumber = ({ value }) => (
    <div className='row'>
        <div className='col'>Landline Number</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);

export const ProjectName = ({ value }) => (
    <div className='row'>
        <div className='col'>Project Name</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);

export const DomainName = ({ value }) => (
    <div className='row'>
        <div className='col'>Domain Name</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);

export const GroupName = ({ value }) => {
    return (
        <div className='row'>
            <div className='col'>Group Name</div>

            <div className='col-1 text-right'>:</div>

            <div className='col-6'>{value}</div>
        </div>
    );
};

export const GovernmentType = ({ value }) => (
    <div className='row'>
        <div className='col'>Type of Government</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>
            {value === 'central' ? 'Central Governmenmt' : null}
            {value === 'state' ? 'State Government' : null}
        </div>
    </div>
);

export const State = ({ stateId }) => {
    const [stateName, setStateName] = useState('');

    useEffect(() => {
        if (stateId && stateId.trim().length) {
            fetchMasterStateWithId(stateId)
                .then((response) => response.data)
                .then((data) => setStateName(data.name))
                .catch(() => setStateName('Cannot determine name of State!'));
        } else setStateName('');
    }, [stateId]);

    return (
        <div className='row'>
            <div className='col'>State</div>

            <div className='col-1 text-right'>:</div>

            <div className='col-6'>{stateName}</div>
        </div>
    );
};

export const OfficeDepartment = ({ value, governmentType }) => {
    const [officeName, setOfficeName] = useState('');

    useEffect(() => {
        if (value && value.trim().length) {
            fetchMasterDepartmentWithId(governmentType, value)
                .then((response) => response.data)
                .then((data) => setOfficeName(data.name))
                .catch(() =>
                    setOfficeName('Cannot determine name of department!')
                );
        } else setOfficeName('');
    }, [value, governmentType]);

    return (
        <div className='row'>
            <div className='col'>
                {governmentType === 'central'
                    ? 'Ministry/Department'
                    : 'Department'}
            </div>

            <div className='col-1 text-right'>:</div>

            <div className='col-6'>{officeName}</div>
        </div>
    );
};

export const OfficeCategory = ({ value }) => {
    const [categoryName, setCategoryName] = useState('');

    useEffect(() => {
        if (value && value.trim().length) {
            fetchMasterOfficeCategoryWithId(value)
                .then((response) => response.data)
                .then((data) => setCategoryName(data.name))
                .catch(() =>
                    setCategoryName('Cannot determine office category!')
                );
        } else setCategoryName('');
    }, [value]);

    return (
        <div className='row'>
            <div className='col'>Office Category</div>

            <div className='col-1 text-right'>:</div>

            <div className='col-6'>{categoryName}</div>
        </div>
    );
};

export const OfficeName = ({ value }) => (
    <div className='row'>
        <div className='col'>Office Name</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);

export const OfficeAddress = ({ value }) => (
    <div className='row'>
        <div className='col'>Office Address</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);

export const GovernmentOfficeGroupName = ({ value }) => (
    <div className='row'>
        <div className='col'>Group/Project Name</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);

export const CompanyName = ({ value }) => (
    <div className='row'>
        <div className='col'>Company Name</div>

        <div className='col-1 text-right'>:</div>

        <div className='col-6'>{value}</div>
    </div>
);
