package com.konstpan.lpcoreg.dto;

import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@JsonInclude(Include.NON_NULL)
public class ServiceResult {

	private String resultCode;
	private String resultDescription;
	private UUID eventLogID;

	public ServiceResult() {
		// Intentionally left blank.
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultDescription() {
		return resultDescription;
	}

	public void setResultDescription(String descr) {
		this.resultDescription = descr;
	}

	public UUID getEventLogID() {
		return eventLogID;
	}

	public void setEventLogID(UUID eventLogID) {
		this.eventLogID = eventLogID;
	}

}
