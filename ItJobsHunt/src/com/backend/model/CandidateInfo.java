package com.backend.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="candidateinfo")
public class CandidateInfo {
	@Id
	@Column(name = "Cand_Id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Getter
	private int cand_Id;
	
	@Column(name = "Cand_FirstName")
	@Getter
	@Setter
	private String cand_FirstName;

	@Column(name = "Cand_LastName")
	@Getter
	@Setter
	private String cand_LastName;
}
