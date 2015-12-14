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


package eu.fp7.scase.reviews.product;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import javax.persistence.Table;
import javax.persistence.Transient;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.annotations.ForeignKey;

import eu.fp7.scase.reviews.utilities.HypermediaLink;
import eu.fp7.scase.reviews.review.JavareviewModel;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.FieldBridge;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import eu.fp7.scase.reviews.utilities.SetStringFieldBridge;

/* This class models the data of a product resource. It is enhanced with JAXB annotations for automated representation
parsing/marshalling as well as with Hibernate annotations for ORM transformations.*/
@XmlRootElement
@Entity
@Table(name="product")
@Indexed
public class JavaproductModel{


    /* There follows a list with the properties that model the product resource, as prescribed in the service CIM*/
		@ElementCollection(fetch = FetchType.EAGER)
		@CollectionTable(name="productcategory", joinColumns=@JoinColumn(name="productId"))
		@Column(name = "category")
		@ForeignKey(name = "fk_product_category")
		@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
		@FieldBridge(impl=SetStringFieldBridge.class)
		private Set<String> category = new HashSet<String>();

		@Column(name = "price")
		private double price;

		@Column(name = "title")
		@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
		private String title;

		@Column(name = "imageUrl")
		private String imageUrl;

		@Column(name = "description")
		@Field(index=Index.YES, analyze=Analyze.YES, store=Store.NO)
		private String description;

		@Id
		@GeneratedValue
		@Column(name = "productId")
		private int productId;

		// The Linklist property holds all the hypermedia links to be sent back to the client
		@Transient
		private List<HypermediaLink> linklist = new ArrayList<HypermediaLink>();

		// This property models the One to Many relationship between two resources as it is defined by the Hibernate syntax below.
		@OneToMany(fetch = FetchType.EAGER, mappedBy="product",orphanRemoval=true)
		@OnDelete(action=OnDeleteAction.CASCADE)
		private Set<JavareviewModel> SetOfJavareviewModel = new HashSet<JavareviewModel>();

    /* There follows a list of setter and getter functions.*/
	    public void setcategory(Set<String> category){
        	this.category = category;
    	}

	    public void setprice(double price){
        	this.price = price;
    	}

	    public void settitle(String title){
        	this.title = title;
    	}

	    public void setimageUrl(String imageUrl){
        	this.imageUrl = imageUrl;
    	}

	    public void setdescription(String description){
        	this.description = description;
    	}

	    public void setproductId(int productId){
        	this.productId = productId;
    	}

	    public void setlinklist(List<HypermediaLink> linklist){
        	this.linklist = linklist;
    	}

		@XmlTransient
	    public void setSetOfJavareviewModel(Set<JavareviewModel> SetOfJavareviewModel){
        	this.SetOfJavareviewModel = SetOfJavareviewModel;
    	}

	    public Set<String> getcategory(){
        	return this.category;
    	}

	    public double getprice(){
        	return this.price;
    	}

	    public String gettitle(){
        	return this.title;
    	}

	    public String getimageUrl(){
        	return this.imageUrl;
    	}

	    public String getdescription(){
        	return this.description;
    	}

	    public int getproductId(){
        	return this.productId;
    	}

	    public List<HypermediaLink> getlinklist(){
        	return this.linklist;
    	}

	    public Set<JavareviewModel> getSetOfJavareviewModel(){
        	return this.SetOfJavareviewModel;
    	}


    /* This function deletes explicitly any collections of this resource that are stored in the database 
    and iteratively does so for any subsequent related resources.
    NOTE: this function is needed to handle erroneous handling of cascade delete of some hibernate versions.*/
    public void deleteAllCollections(Session hibernateSession){

        Query productcategoryQuery = hibernateSession.createSQLQuery(String.format("DELETE FROM %s where %sId = %d","productcategory".toLowerCase(),"product",this.getproductId()));
        productcategoryQuery.executeUpdate();

    }
}
