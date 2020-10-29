import axios from 'axios';

const baseUrl =
    !process.env.NODE_ENV || process.env.NODE_ENV === 'development'
        ? ''
        : process.env.PUBLIC_URL;

const directUrl =
    !process.env.NODE_ENV || process.env.NODE_ENV === 'development'
        ? process.env.REACT_APP_BACKEND_SERVER_URL
        : window.location.origin + process.env.PUBLIC_URL;

export const fetchNicGroupList = (employeeCode) =>
    axios.get(`${baseUrl}/api/nic/${employeeCode}/groups`);

export const fetchNicEmployeeDetails = (employeeCode, type) =>
    axios.get(`${baseUrl}/api/nic/${employeeCode}/personaldetails`, {
        params: { type },
    });

export const fetchMasterStateList = () =>
    axios.get(`${baseUrl}/api/master/states`);

export const fetchMasterStateWithId = (stateId) =>
    axios.get(`${baseUrl}/api/master/states/${stateId}`);

export const fetchMasterOfficeCategoryList = () =>
    axios.get(`${baseUrl}/api/master/officecategories`);

export const fetchMasterOfficeCategoryWithId = (officeCategoryId) =>
    axios.get(`${baseUrl}/api/master/officecategories/${officeCategoryId}`);

export const fetchMasterDepartmentList = (governmentType) =>
    axios.get(`${baseUrl}/api/master/departments/${governmentType}`);

export const fetchMasterDepartmentWithId = (governmentType, departmentId) =>
    axios.get(
        `${baseUrl}/api/master/departments/${governmentType}/${departmentId}`
    );

export const fetchCaptchaImage = () =>
    axios.get(`${baseUrl}/api/captcha/generate`, {
        responseType: 'arraybuffer',
    });

export const submitApplicationFormForNic = (applicationForm) =>
    axios.post(`${baseUrl}/api/onboard/publisher/nic`, applicationForm);

export const submitApplicationFormForGovernment = (applicationForm) =>
    axios.post(`${baseUrl}/api/onboard/publisher/govt`, applicationForm);

export const submitApplicationFormForOthers = (applicationForm) =>
    axios.post(`${baseUrl}/api/onboard/publisher/other`, applicationForm);

export const verifyDomainDoesNotExist = (domainName) =>
    axios.get(`${baseUrl}/api/onboard/verify/does-not-exist`, {
        params: {
            'domain-name': domainName,
        },
    });

export const verifyIfApplicationNumberIsValid = (applicationNumber) =>
    axios.get(`${baseUrl}/api/esign/check-validity`, {
        params: { 'application-number': applicationNumber },
    });

export const verifyIfCaptchaIsValid = (captchaValue) =>
    axios.post(`${baseUrl}/api/captcha/validate`, {
        captchaValue,
    });

export const urlToDownloadPdfApplication = (
    applicationNumber,
    signed = false
) => {
    const url = new URL(`${directUrl}/services/download-application-form`);
    url.searchParams.append('application-number', applicationNumber);
    url.searchParams.append('signed', signed);

    return url.href;
};

export const urlToSubmitFormToEsignService = () =>
    `${directUrl}/services/esign/process/esign-form`;
