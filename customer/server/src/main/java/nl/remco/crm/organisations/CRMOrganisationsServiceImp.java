/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package nl.remco.crm.organisations;

import java.math.BigDecimal;

public class CRMOrganisationsServiceImp  {

	private OrganisationInfo bedrijfA;
	private OrganisationInfo bedrijfB;

	public CRMOrganisationsServiceImp()
	{
		bedrijfA= new OrganisationInfo();
		bedrijfA.setKvkNumber( "123456789");
		bedrijfA.setCRMNumber( new BigDecimal(8000L));
		bedrijfA.setOrganisationName( "Bedrijf A");

		bedrijfB= new OrganisationInfo();
		bedrijfB.setKvkNumber( "888888888");
		bedrijfB.setCRMNumber( new BigDecimal(8001L));
		bedrijfB.setOrganisationName( "Bedrijf B");
	}


	public SearchOrganisationRes searchOrganisations(
			SearchOrganisationReq searchSchools) {
		SearchOrganisationRes response= new SearchOrganisationRes();
		response.getOrganisations().add( bedrijfA);

		response.getOrganisations().add( bedrijfB);
		return response;
	}


	public GetOrganisationInfoResponse getOrganisationInfo(
			GetOrganisationInfoRequest request)
	{
		GetOrganisationInfoResponse response= new GetOrganisationInfoResponse();
		for (BigDecimal school: request.getKvKNumber()){
			if (school.intValue()==8000) {
				response.getSchool().add( bedrijfA);
			} else if  (school.intValue()==8001) {
				response.getSchool().add( bedrijfB);
			}
		}
		return response;
	}

	public GetOrganisationHierarchyRes getOrganisationHierarchy(
			GetOrganisationHierarchyReq getOrgTree)
	{
		GetOrganisationHierarchyRes response= new GetOrganisationHierarchyRes();
		OrganisationRecords schoollist= new OrganisationRecords();

		for (BigDecimal org: getOrgTree.getOrganisations().getCRMNumber()){
			if (org.intValue()==8000) {
				response.getOrganisations().getOrganisation().add( bedrijfA);
			} else if  (org.intValue()==8001) {
				response.getOrganisations().getOrganisation().add( bedrijfB);
			}
		}

		response.setOrganisations(schoollist);
		return response;
	}



}
