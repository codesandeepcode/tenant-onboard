package com.nic.service.onboarding.publisher;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import com.nic.service.esign.pdf.GovtApplicationModel;
import com.nic.service.esign.pdf.OthersApplicationModel;
import com.nic.service.masterdata.MasterDepartmentEntity;
import com.nic.service.masterdata.MasterGovernmentTypeEntity;
import com.nic.service.masterdata.MasterOfficeCategoryEntity;
import com.nic.service.masterdata.MasterStateEntity;
import com.nic.service.onboarding.publisher.NicApplicationFormModel.PersonalDetails;
import com.nic.service.utils.GovernmentType;
import com.nic.service.utils.RankTitle;

@Mapper(implementationName = "OnBoardingDataMapperImpl", imports = { LocalDateTime.class, RankTitle.class })
interface DataMapper {

	@Mappings({ 
		@Mapping(target = "id", ignore = true), 
		@Mapping(source = "rankTitle", target = "titleId"),
		@Mapping(source = "personalDetails.code", target = "employeeCode"),
		@Mapping(source = "personalDetails.name", target = "employeeName"),
		@Mapping(source = "personalDetails.mobileNo", target = "mobileNumber"),
		@Mapping(source = "personalDetails.landlineNo", target = "landlineNumber"),
		@Mapping(target = "active", constant = "1") 
	})
	NicPersonnelEntity map(PublisherApplicationEntity application, PersonalDetails personalDetails,
			RankTitle rankTitle);

	@Mappings({ 
		@Mapping(source = "formData.group", target = "groupName"),
		@Mapping(target = "active", constant = "1") 
	})
	NicOfficeEntity map(PublisherApplicationEntity application, NicApplicationFormModel formData);

	default Short map(RankTitle value) {
		return value.getId();
	}

	@Mappings({ 
		@Mapping(target = "applicationId", ignore = true),
		@Mapping(source = "referenceNo", target = "applicationReferenceNo"),
		@Mapping(source = "formData.domain", target = "domainName"),
		@Mapping(source = "formData.project", target = "projectName"),
		@Mapping(target = "creationDate", expression = "java(LocalDateTime.now())"),
		@Mapping(target = "applicationRemarks", ignore = true),
		@Mapping(target = "applicationStatusId", constant = "1"), 
		@Mapping(target = "active", constant = "1") 
	})
	PublisherApplicationEntity map(String referenceNo, NicApplicationFormModel formData);

	@Mappings({ 
		@Mapping(target = "applicationId", ignore = true),
		@Mapping(source = "referenceNo", target = "applicationReferenceNo"),
		@Mapping(target = "creationDate", expression = "java(LocalDateTime.now())"),
		@Mapping(target = "applicationRemarks", ignore = true),
		@Mapping(target = "applicationStatusId", constant = "1"), 
		@Mapping(target = "active", constant = "1") 
	})
	PublisherApplicationEntity map(String referenceNo, GovernmentApplicationFormModel formData);

	@Mappings({ 
		@Mapping(source = "formData.governmentType", target = "govtType"),
		@Mapping(source = "formData.office.department", target = "department"),
		@Mapping(source = "formData.office.state", target = "state"),
		@Mapping(source = "formData.office.category", target = "officeCategory"),
		@Mapping(source = "formData.office.name", target = "officeName"),
		@Mapping(source = "formData.office.address", target = "officeAddress"),
		@Mapping(source = "formData.projectHead.group", target = "groupName"),
		@Mapping(target = "active", constant = "1") 
	})
	GovtOfficeEntity map(PublisherApplicationEntity application, GovernmentApplicationFormModel formData);

	default MasterGovernmentTypeEntity mapEntity(GovernmentType value) {
		return new MasterGovernmentTypeEntity(value);
	}

	default MasterDepartmentEntity mapDeptEntity(String value) {
		if (StringUtils.isBlank(value))
			return null;

		return new MasterDepartmentEntity(Integer.parseInt(value));
	}

	default MasterStateEntity mapStateEntity(String value) {
		if (StringUtils.isBlank(value))
			return null;

		return new MasterStateEntity(value);
	}

	default MasterOfficeCategoryEntity mapOfficeEntity(String value) {
		if (StringUtils.isBlank(value))
			return null;

		return new MasterOfficeCategoryEntity(Integer.parseInt(value));
	}

	default Short map(GovernmentType value) {
		return value.getId();
	}
	
	@Mappings({
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "titleId", expression = "java(RankTitle.PROJECT_HEAD.getId())"),
		@Mapping(source = "formData.projectHead.code", target = "employeeCode"),
		@Mapping(source = "formData.projectHead.name", target = "employeeName"),
		@Mapping(source = "formData.projectHead.designation", target = "designation"),
		@Mapping(source = "formData.projectHead.emailId", target = "emailId"),
		@Mapping(source = "formData.projectHead.mobileNo", target = "mobileNumber"),
		@Mapping(source = "formData.projectHead.landlineNo", target = "landlineNumber"),
		@Mapping(target = "active", constant = "1")
	})
	GovtPersonnelEntity mapProjectHead(PublisherApplicationEntity application, GovernmentApplicationFormModel formData);

	@Mappings({
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "titleId", expression = "java(RankTitle.TECHNICAL_HEAD.getId())"),
		@Mapping(source = "techHead.code", target = "employeeCode"),
		@Mapping(source = "techHead.name", target = "employeeName"),
		@Mapping(source = "techHead.mobileNo", target = "mobileNumber"),
		@Mapping(source = "techHead.landlineNo", target = "landlineNumber"),
		@Mapping(target = "active", constant = "1")
	})
	GovtPersonnelEntity mapTechnicalHeadGovt(PublisherApplicationEntity application, TechnicalHeadDetailsModel techHead);

	@Mappings({
		@Mapping(target = "applicationId", ignore = true),
		@Mapping(source = "referenceNo", target = "applicationReferenceNo"),
		@Mapping(target = "creationDate", expression = "java(LocalDateTime.now())"),
		@Mapping(target = "applicationRemarks", ignore = true),
		@Mapping(target = "applicationStatusId", constant = "1"), 
		@Mapping(target = "active", constant = "1")
	})
	PublisherApplicationEntity map(String referenceNo, OthersApplicationFormModel formData);
	
	@Mappings({
		@Mapping(source = "formData.office.state", target = "state"),
		@Mapping(source = "formData.office.name", target = "companyName"),
		@Mapping(source = "formData.office.address", target = "companyAddress"),
		@Mapping(target = "active", constant = "1")
	})
	OthersOfficeEntity map(PublisherApplicationEntity application, OthersApplicationFormModel formData);
	
	@Mappings({
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "titleId", expression = "java(RankTitle.PROJECT_HEAD.getId())"),
		@Mapping(source = "projectHead.code", target = "employeeCode"),
		@Mapping(source = "projectHead.name", target = "employeeName"),
		@Mapping(source = "projectHead.mobileNo", target = "mobileNumber"),
		@Mapping(source = "projectHead.landlineNo", target = "landlineNumber"),
		@Mapping(target = "active",constant = "1"),
	})
	OthersPersonnelEntity map(PublisherApplicationEntity application,
			OthersApplicationFormModel.ProjectHead projectHead);
	
	@Mappings({
		@Mapping(target = "id", ignore = true),
		@Mapping(target = "titleId", expression = "java(RankTitle.TECHNICAL_HEAD.getId())"),
		@Mapping(source = "techHead.code", target = "employeeCode"),
		@Mapping(source = "techHead.name", target = "employeeName"),
		@Mapping(source = "techHead.mobileNo", target = "mobileNumber"),
		@Mapping(source = "techHead.landlineNo", target = "landlineNumber"),
		@Mapping(target = "active", constant = "1")
	})
	OthersPersonnelEntity mapTechnicalHeadOther(PublisherApplicationEntity application, TechnicalHeadDetailsModel techHead);
	
	@Mappings({
		@Mapping(source = "office.groupName", target = "group"),
		@Mapping(source = "hog", target = "hog"),
		@Mapping(source = "hod", target = "hod"),
		@Mapping(source = "techAdmin1", target = "techAdmin1"),
		@Mapping(source = "techAdmin2", target = "techAdmin2"),
		@Mapping(source = "application.domainName", target = "domain"),
		@Mapping(source = "application.projectName", target = "project")
	})
	NicApplicationFormModel map(PublisherApplicationEntity application, NicOfficeEntity office, NicPersonnelEntity hog,
			NicPersonnelEntity hod, NicPersonnelEntity techAdmin1, NicPersonnelEntity techAdmin2);
	
	@Mappings({ 
		@Mapping(source = "office.govtType", target = "governmentType"),
		@Mapping(source = "office", target = "office"),
		@Mapping(source = "projectHead.employeeCode", target = "projectHead.code"),
		@Mapping(source = "projectHead.employeeName", target = "projectHead.name"),
		@Mapping(source = "projectHead.designation", target = "projectHead.designation"),
		@Mapping(source = "projectHead.emailId", target = "projectHead.emailId"),
		@Mapping(source = "projectHead.mobileNumber", target = "projectHead.mobileNo"),
		@Mapping(source = "projectHead.landlineNumber", target = "projectHead.landlineNo"),
		@Mapping(source = "office.groupName", target = "projectHead.group"),
		@Mapping(source = "tenhnicalHeads", target = "technicalHeadList")
	})
	GovtApplicationModel map(PublisherApplicationEntity application, GovtOfficeEntity office,
			GovtPersonnelEntity projectHead, List<GovtPersonnelEntity> tenhnicalHeads);
	
	default GovernmentType map(MasterGovernmentTypeEntity entity) {
		return GovernmentType.getById(entity.getId());
	}
	
	@Mappings({
		@Mapping(source = "department.name", target = "department"),
		@Mapping(source = "state.stateNameEn", target = "state"),
		@Mapping(source = "officeCategory.name", target = "category"),
		@Mapping(source = "officeName", target = "name"),
		@Mapping(source = "officeAddress", target = "address"),
	})
	GovtApplicationModel.Office map(GovtOfficeEntity office);
	
	@Mappings({
		@Mapping(source = "employeeCode", target = "code"),
		@Mapping(source = "employeeName", target = "name"),
		@Mapping(source = "mobileNumber", target = "mobileNo"),
		@Mapping(source = "landlineNumber", target = "landlineNo"),
	})
	GovtApplicationModel.TechnicalHead map(GovtPersonnelEntity technicalHead);
	
	@Mappings({
		@Mapping(source = "employeeCode", target = "code"),
		@Mapping(source = "employeeName", target = "name"),
		@Mapping(source = "mobileNumber", target = "mobileNo"),
		@Mapping(source = "landlineNumber", target = "landlineNo")
	})
	PersonalDetails map(NicPersonnelEntity value);
	
	
	@Mappings({
		@Mapping(source = "technicalHeads", target = "technicalHead")
	})
	OthersApplicationModel map(PublisherApplicationEntity application, OthersOfficeEntity office,
			OthersPersonnelEntity projectHead, List<OthersPersonnelEntity> technicalHeads);

	@Mappings({
		@Mapping(source = "companyName", target = "name"),
		@Mapping(source = "state.stateNameEn", target = "state"),
		@Mapping(source = "companyAddress", target = "address"),
	})
	OthersApplicationModel.Office map(OthersOfficeEntity officeEntity);
	
	@Mappings({
		@Mapping(source = "employeeCode", target = "code"),
		@Mapping(source = "employeeName", target = "name"),
		@Mapping(source = "mobileNumber", target = "mobileNo"),
		@Mapping(source = "landlineNumber", target = "landlineNo")
	})
	OthersApplicationModel.ProjectHead mapProjectHead(OthersPersonnelEntity personnelEntity);
	
	@Mappings({
		@Mapping(source = "employeeCode", target = "code"),
		@Mapping(source = "employeeName", target = "name"),
		@Mapping(source = "mobileNumber", target = "mobileNo"),
		@Mapping(source = "landlineNumber", target = "landlineNo"),
	})
	OthersApplicationModel.TechnicalHead map(OthersPersonnelEntity personnelEntity);
	
	
	
}
