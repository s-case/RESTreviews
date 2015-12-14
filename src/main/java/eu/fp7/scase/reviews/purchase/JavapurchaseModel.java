/*
 * ARISTOSTLE UNIVERSITY OF THESSALONIKI
 * Copyright (C) 2015
 * Aristotle University of Thessaloniki
 * Department of Electrical & Computer Engineering
 * Division of Electronics & Computer Engineering
 * Intelligent Systems & Software Engineering Lab
 *
 * Project             : reviews
 * WorkFile            : 
 * Compiler            : 
 * File Description    : 
 * Document Description: 
* Related Documents	   : 
* Note				   : 
* Programmer		   : RESTful MDE Engine created by Christoforos Zolotas
* Contact			   : christopherzolotas@issel.ee.auth.gr
*/


package eu.fp7.scase.reviews.purchase;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.ForeignKey;

import eu.fp7.scase.reviews.utilities.HypermediaLink;
import eu.fp7.scase.reviews.account.JavaaccountModel;


/* This class models the data of a purchase resource. It is enhanced with JAXB annotations for automated representation
parsing/marshalling as well as with Hibernate annotations for ORM transformations.*/
@XmlRootElement
@Entity
@Table(name="purchase")
public class JavapurchaseModel{


    /* There follows a list with the properties that model the purchase resource, as prescribed in the service CIM*/
		@Column(name = "quantity")
		private int quantity;

		@Column(name = "sku")
		private String sku;

		@Column(name = "date")
		private String date;

		@Id
		@GeneratedValue
		@Column(name = "purchaseId")
		private int purchaseId;

		// The Linklist property holds all the hypermedia links to be sent back to the client
		@Transient
		private List<HypermediaLink> linklist = new ArrayList<HypermediaLink>();

		// This property models the Many to One relationship between two resources as it is defined by the Hibernate syntax below.
		@ManyToOne(fetch = FetchType.EAGER)
		@JoinColumn(name="accountId")
		@ForeignKey(name = "fk_account_purchase")
		private JavaaccountModel account;

    /* There follows a list of setter and getter functions.*/
	    public void setquantity(int quantity){
        	this.quantity = quantity;
    	}

	    public void setsku(String sku){
        	this.sku = sku;
    	}

	    public void setdate(String date){
        	this.date = date;
    	}

	    public void setpurchaseId(int purchaseId){
        	this.purchaseId = purchaseId;
    	}

	    public void setlinklist(List<HypermediaLink> linklist){
        	this.linklist = linklist;
    	}

		@XmlTransient
	    public void setaccount(JavaaccountModel account){
        	this.account = account;
    	}

	    public int getquantity(){
        	return this.quantity;
    	}

	    public String getsku(){
        	return this.sku;
    	}

	    public String getdate(){
        	return this.date;
    	}

	    public int getpurchaseId(){
        	return this.purchaseId;
    	}

	    public List<HypermediaLink> getlinklist(){
        	return this.linklist;
    	}

	    public JavaaccountModel getaccount(){
        	return this.account;
    	}


}
