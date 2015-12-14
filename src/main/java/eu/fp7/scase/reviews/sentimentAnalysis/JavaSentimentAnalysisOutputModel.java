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


package eu.fp7.scase.reviews.sentimentAnalysis;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashSet;
import java.util.Set;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import eu.fp7.scase.reviews.utilities.HypermediaLink;

/* 
 * JavaSentimentAnalysisOutputModel class is responsible to model the output data model of the sentimentAnalysis resource. This models the data
 * that will be received as output from the third party service.
*/

@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public class JavaSentimentAnalysisOutputModel{

    /* There follows a list with the properties that model the sentimentAnalysis resource, as prescribed in the External service layer CIM*/
	private String sentiment;

	// The Linklist property holds all the hypermedia links to be sent back to the client
	@Transient
	private List<HypermediaLink> linklist = new ArrayList<HypermediaLink>();

	/* There follows a list of setter and getter functions.*/
	public void setSentiment(String sentiment){
    	this.sentiment = sentiment;
    }

    public void setlinklist(List<HypermediaLink> linklist){
       	this.linklist = linklist;
   	}

	public String getSentiment(){
        return this.sentiment;
    }
	public List<HypermediaLink> getlinklist(){
        return this.linklist;
    }
}
