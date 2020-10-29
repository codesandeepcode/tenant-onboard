

-- Data for Name: master_application_status; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.master_application_status VALUES (1, 'in_process', 1);
INSERT INTO public.master_application_status VALUES (2, 'completed', 1);
INSERT INTO public.master_application_status VALUES (3, 'rejected', 1);


-- Data for Name: master_state; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.master_state VALUES ('01', 'JAMMU AND KASHMIR', 'जम्मू और कश्मीर', 1);
INSERT INTO public.master_state VALUES ('02', 'HIMACHAL PRADESH', 'हिमाचल प्रदेश', 1);
INSERT INTO public.master_state VALUES ('03', 'PUNJAB', 'पंजाब', 1);
INSERT INTO public.master_state VALUES ('04', 'CHANDIGARH', 'चंडीग', 1);
INSERT INTO public.master_state VALUES ('05', 'UTTARAKHAND', 'उत्तराखंड', 1);
INSERT INTO public.master_state VALUES ('06', 'HARYANA', 'हरियाणा', 1);
INSERT INTO public.master_state VALUES ('07', 'DELHI', 'दिल्ली', 1);
INSERT INTO public.master_state VALUES ('08', 'RAJASTHAN', 'राजस्थान', 1);
INSERT INTO public.master_state VALUES ('09', 'UTTAR PRADESH', 'उत्तर प्रदेश', 1);
INSERT INTO public.master_state VALUES ('10', 'BIHAR', 'बिहार', 1);
INSERT INTO public.master_state VALUES ('11', 'SIKKIM', 'सिक्किम', 1);
INSERT INTO public.master_state VALUES ('12', 'ARUNACHAL PRADESH', 'अरुणाचल प्रदेश', 1);
INSERT INTO public.master_state VALUES ('13', 'NAGALAND', 'नागालैंड', 1);
INSERT INTO public.master_state VALUES ('14', 'MANIPUR', 'मणिपुर', 1);
INSERT INTO public.master_state VALUES ('15', 'MIZORAM', 'मिज़ोरम', 1);
INSERT INTO public.master_state VALUES ('16', 'TRIPURA', 'त्रिपुरा', 1);
INSERT INTO public.master_state VALUES ('17', 'MEGHALAYA', 'मेघालय', 1);
INSERT INTO public.master_state VALUES ('18', 'ASSAM', 'आसाम', 1);
INSERT INTO public.master_state VALUES ('19', 'WEST BENGAL', 'पश्चिम बंगाल', 1);
INSERT INTO public.master_state VALUES ('20', 'JHARKHAND', 'झारखण्ड', 1);
INSERT INTO public.master_state VALUES ('21', 'ORISSA', 'ओड़िशा', 1);
INSERT INTO public.master_state VALUES ('22', 'CHHATISGARH', 'छत्तीसगढ़', 1);
INSERT INTO public.master_state VALUES ('23', 'MADHYA PRADESH', 'मध्य प्रदेश', 1);
INSERT INTO public.master_state VALUES ('24', 'GUJARAT', 'गुजरात', 1);
INSERT INTO public.master_state VALUES ('25', 'DAMAN & DIU', 'दमन और दीव', 1);
INSERT INTO public.master_state VALUES ('26', 'DADRA & NAGAR HAVELI', 'दादरा और नगर हवेली', 1);
INSERT INTO public.master_state VALUES ('27', 'MAHARASHTRA', 'महाराष्ट्र', 1);
INSERT INTO public.master_state VALUES ('28', 'ANDHRA PRADESH', 'आन्ध्र प्रदेश', 1);
INSERT INTO public.master_state VALUES ('29', 'KARNATAKA', 'कर्नाटक', 1);
INSERT INTO public.master_state VALUES ('30', 'GOA', 'गोवा', 1);
INSERT INTO public.master_state VALUES ('31', 'LAKSHADWEEP', 'लक्षद्वीप', 1);
INSERT INTO public.master_state VALUES ('32', 'KERALA', 'केरल', 1);
INSERT INTO public.master_state VALUES ('33', 'TAMIL NADU', 'तमिल नाडु', 1);
INSERT INTO public.master_state VALUES ('34', 'PUDUCHERRY', 'पुडुचेरी', 1);
INSERT INTO public.master_state VALUES ('35', 'ANDAMAN & NICOBAR ISLANDS', 'अंदमान और निकोबार द्वीप', 1);


-- Data for Name: master_department; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.master_department VALUES (1, 'Cabinet Secretariat', true, false, 1);
INSERT INTO public.master_department VALUES (3, 'Ministry of Agriculture', false, true, 1);
INSERT INTO public.master_department VALUES (4, 'Ministry of Coal', false, true, 1);
INSERT INTO public.master_department VALUES (2, 'Department of Atomic Energy', true, true, 1);


-- Data for Name: master_govt_type; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.master_govt_type VALUES (1, 'Central', 1);
INSERT INTO public.master_govt_type VALUES (2, 'State', 1);


-- Data for Name: master_office_category; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.master_office_category VALUES (1, 'Ministries/Departments/Attached Offices of Central Govt', 1);
INSERT INTO public.master_office_category VALUES (2, 'Subordinate office of Central Govt having no source of income', 1);
INSERT INTO public.master_office_category VALUES (3, 'Ministries/Departments and Offices of the State Govt', 1);
INSERT INTO public.master_office_category VALUES (4, 'Statutory bodies fully funded by Center/State Govt with no internal revenue resources', 1);
INSERT INTO public.master_office_category VALUES (5, 'Public sector undertakings', 1);
INSERT INTO public.master_office_category VALUES (6, 'Autonomous/Statutory bodies not fully funded by Central Govt/State Govt', 1);
INSERT INTO public.master_office_category VALUES (7, 'Autonomous/Statutory bodies under Central Govt/State Govt/Societies generating internal revenue apart from the grants they receive', 1);
INSERT INTO public.master_office_category VALUES (8, 'Others', 1);


-- Data for Name: master_rank_title; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.master_rank_title VALUES (1, 'Head of Group', 1);
INSERT INTO public.master_rank_title VALUES (2, 'Head of Division', 1);
INSERT INTO public.master_rank_title VALUES (3, 'Technical Admin', 1);
INSERT INTO public.master_rank_title VALUES (4, 'Project Head', 1);
INSERT INTO public.master_rank_title VALUES (5, 'Technical Head', 1);


-- Data for Name: publisher_application_details; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.publisher_application_details VALUES (141, 'NAPIX/20103', 'sqggroup.nic.in', 'Sqg Group NIC', '2020-09-29 13:39:07.430144', NULL, 1, 1);
INSERT INTO public.publisher_application_details VALUES (142, 'NAPIX/20104', 'napixs.nic.in', 'Test NIC', '2020-09-29 15:41:14.235753', NULL, 1, 1);
INSERT INTO public.publisher_application_details VALUES (143, 'NAPIX/20105', 'napixss.nic.in', 'Test product', '2020-09-29 19:00:20.293', NULL, 1, 1);
INSERT INTO public.publisher_application_details VALUES (144, 'NAPIX/20113', 'save-water.gov.in', 'Saving Water Project', '2020-10-20 16:40:29.427404', NULL, 1, 1);
INSERT INTO public.publisher_application_details VALUES (145, 'NAPIX/20114', 'pdf-food.nic.in', 'Food Managing Project', '2020-10-20 16:44:47.003041', NULL, 1, 1);
INSERT INTO public.publisher_application_details VALUES (146, 'NAPIX/20115', 'freeapi.nic.in', 'Free API Project', '2020-10-20 16:49:28.245726', NULL, 1, 1);


-- Data for Name: nic_office_details; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.nic_office_details VALUES (141, ' Software Checker Group', 1);
INSERT INTO public.nic_office_details VALUES (142, ' SQG', 1);
INSERT INTO public.nic_office_details VALUES (143, ' SQG', 1);


-- Data for Name: nic_personnel_details; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.nic_personnel_details (application_id, title_id, employee_code, employee_name, designation, department, address, email_id, mobile_number, landline_number, active) VALUES (141, 1, 1104, 'Pawan Kumar Joshi', 'Scientist-G', 'NIC', 'NIC HQS, NEW DELHI, 110003', 'pawan.joshi@nic.in', '9818662315', '11-24305268', 1);
INSERT INTO public.nic_personnel_details (application_id, title_id, employee_code, employee_name, designation, department, address, email_id, mobile_number, landline_number, active) VALUES (141, 2, 3629, 'G. Mayil Muthu Kumaran', 'Scientist-G', 'NIC', 'A4B3, II, A, NIC, NEW, 110003', 'muthu@nic.in', '9810119461', '11-24305748 ', 1);
INSERT INTO public.nic_personnel_details (application_id, title_id, employee_code, employee_name, designation, department, address, email_id, mobile_number, landline_number, active) VALUES (141, 3, 5516, 'Pooja Singh', 'Scientist-D', 'NIC', 'A1B4, III, III, NIC HQS, , 110003', 'singhpooja@nic.in', '9810273875', '11-24305294', 1);
INSERT INTO public.nic_personnel_details (application_id, title_id, employee_code, employee_name, designation, department, address, email_id, mobile_number, landline_number, active) VALUES (141, 3, 7102, 'Sandeep Yadav', 'Scientist-B', 'NIC', 'SQG LAB, NIC HQ, 110003', 'yadav.sandeep95@nic.in', '7891239272', '', 1);
INSERT INTO public.nic_personnel_details (application_id, title_id, employee_code, employee_name, designation, department, address, email_id, mobile_number, landline_number, active) VALUES (142, 1, 1104, 'Pawan Kumar Joshi', 'Scientist-G', 'NIC', 'NIC HQS, NEW DELHI, 110003', 'pawan.joshi@nic.in', '9818662315', '11-24305268', 1);
INSERT INTO public.nic_personnel_details (application_id, title_id, employee_code, employee_name, designation, department, address, email_id, mobile_number, landline_number, active) VALUES (142, 2, 3629, 'G. Mayil Muthu Kumaran', 'Scientist-G', 'NIC', 'A4B3, II, A, NIC, NEW, 110003', 'muthu@nic.in', '9810119461', '11-24305748 ', 1);
INSERT INTO public.nic_personnel_details (application_id, title_id, employee_code, employee_name, designation, department, address, email_id, mobile_number, landline_number, active) VALUES (142, 3, 5516, 'Pooja Singh', 'Scientist-D', 'NIC', 'A1B4, III, III, NIC HQS, , 110003', 'singhpooja@nic.in', '9810273875', '11-24305294', 1);
INSERT INTO public.nic_personnel_details (application_id, title_id, employee_code, employee_name, designation, department, address, email_id, mobile_number, landline_number, active) VALUES (143, 1, 1104, 'Pawan Kumar Joshi', 'Scientist-G', 'NIC', 'NIC HQS, NEW DELHI, 110003', 'pawan.joshi@nic.in', '9818662315', '11-24305268', 1);
INSERT INTO public.nic_personnel_details (application_id, title_id, employee_code, employee_name, designation, department, address, email_id, mobile_number, landline_number, active) VALUES (143, 2, 3629, 'G. Mayil Muthu Kumaran', 'Scientist-G', 'NIC', 'A4B3, II, A, NIC, NEW, 110003', 'muthu@nic.in', '9810119461', '11-24305748 ', 1);
INSERT INTO public.nic_personnel_details (application_id, title_id, employee_code, employee_name, designation, department, address, email_id, mobile_number, landline_number, active) VALUES (143, 3, 5516, 'Pooja Singh', 'Scientist-D', 'NIC', 'A1B4, III, III, NIC HQS, , 110003', 'singhpooja@nic.in', '9810273875', '11-24305294', 1);
INSERT INTO public.nic_personnel_details (application_id, title_id, employee_code, employee_name, designation, department, address, email_id, mobile_number, landline_number, active) VALUES (143, 3, 7102, 'Sandeep Yadav', 'Scientist-B', 'NIC', 'SQG LAB, NIC HQ, 110003', 'yadav.sandeep95@nic.in', '7891239272', '', 1);


-- Data for Name: esp_transaction_details; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.esp_transaction_details (application_id, esp_version, transaction_id, signing_person, ekyc_type, authentication_mode, sign_type, request_timestamp, response_status, response_code, response_error_code, response_error_message, response_x509_cert, signed_doc_hash, doc_sign_error, response_timestamp, sign_hash_algorithm) VALUES (141, '2.1', '999-BHARATAPI-20200929-134602-000001', 'Test User', 'A', '1', 'pkcs7', '2020-09-29 13:46:02.822636', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.esp_transaction_details (application_id, esp_version, transaction_id, signing_person, ekyc_type, authentication_mode, sign_type, request_timestamp, response_status, response_code, response_error_code, response_error_message, response_x509_cert, signed_doc_hash, doc_sign_error, response_timestamp, sign_hash_algorithm) VALUES (141, '2.1', '999-BHARATAPI-20200929-134940-000002', 'Testing', 'A', '1', 'pkcs7', '2020-09-29 13:49:40.068812', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO public.esp_transaction_details (application_id, esp_version, transaction_id, signing_person, ekyc_type, authentication_mode, sign_type, request_timestamp, response_status, response_code, response_error_code, response_error_message, response_x509_cert, signed_doc_hash, doc_sign_error, response_timestamp, sign_hash_algorithm) VALUES (142, '2.1', '999-BHARATAPI-20200929-191957-000003', 'Testing', 'A', '1', 'pkcs7', '2020-09-29 19:19:57.055676', '0', '58c965ea-78e0-4751-a04f-7fd000297933', 'ESP-710', 'User has cancelled the eSign Transaction', NULL, NULL, NULL, '2020-09-29 19:20:00.032', NULL);


-- Data for Name: govt_office_details; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.govt_office_details (application_id, govt_type_id, department_id, state_id, office_category_id, office_name, office_address, group_name, active) VALUES (144, 1, 1, '10', 3, 'Water Department', 'G-100, Mumbai, Maharashtra, India', 'Water Conservation Group', 1);
INSERT INTO public.govt_office_details (application_id, govt_type_id, department_id, state_id, office_category_id, office_name, office_address, group_name, active) VALUES (145, 2, 3, '07', 4, 'Food and Public Distribution Department', 'Ahemdabad, Gujarat, India - 201110', 'PDS Group', 1);


-- Data for Name: govt_personnel_details; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.govt_personnel_details (application_id, title_id, employee_code, employee_name, designation, email_id, mobile_number, landline_number, active) VALUES (144, 4, 'G-234', 'Vikas Sharma', 'Head Officer', 'vikassharma@gov.in', '9874498754', '87547778787', 1);
INSERT INTO public.govt_personnel_details (application_id, title_id, employee_code, employee_name, designation, email_id, mobile_number, landline_number, active) VALUES (144, 5, 'TV-123-11', 'Anirudh Karma', 'Junior Officer', 'karma@google.com', '7845785454', '', 1);
INSERT INTO public.govt_personnel_details (application_id, title_id, employee_code, employee_name, designation, email_id, mobile_number, landline_number, active) VALUES (145, 4, '122-TY', 'Bhatnagar ', 'Manager', 'bhatnagar@gov.in', '8754547878', '', 1);
INSERT INTO public.govt_personnel_details (application_id, title_id, employee_code, employee_name, designation, email_id, mobile_number, landline_number, active) VALUES (145, 5, '273666', 'Anant Khan', 'Technical Support', 'anantk@hotmail.com', '8787845454', '201027454545477', 1);


-- Data for Name: others_office_details; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.others_office_details VALUES (146, '17', 'Free API Company', 'G-312321, Shillong District, Meghalaya, India', 1);


-- Data for Name: others_personnel_details; Type: TABLE DATA; Schema: public; Owner: postgres

INSERT INTO public.others_personnel_details (application_id, title_id, employee_code, employee_name, designation, email_id, mobile_number, landline_number, active) VALUES (146, 4, '68736', 'Amrinder Singh', 'Project Head', 'sign@google.com', '9875454545', '', 1);
INSERT INTO public.others_personnel_details (application_id, title_id, employee_code, employee_name, designation, email_id, mobile_number, landline_number, active) VALUES (146, 5, 'YU-76-TYT', 'Nikhil Bains', 'Technical Head', 'nikhil@hotmail.com', '9875454545', '', 1);
INSERT INTO public.others_personnel_details (application_id, title_id, employee_code, employee_name, designation, email_id, mobile_number, landline_number, active) VALUES (146, 5, 'UY090YU-676', 'Akash Malik', 'Technical Support', 'akash@yahoo.com', '7547978787', '8889856656', 1);

