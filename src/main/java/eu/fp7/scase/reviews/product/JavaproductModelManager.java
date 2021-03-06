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
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import eu.fp7.scase.reviews.utilities.HypermediaLink;

/* This class models a product manager resource. It also has the needed JAXB annoations for automated representation parsing/marshalling.*/

@XmlRootElement
public class JavaproductModelManager{


    //The Linklist property holds any hypermedia links to be sent back to the client.
	private List<HypermediaLink> linklist = new ArrayList<HypermediaLink>();


    public List<HypermediaLink> getlinklist(){
        return this.linklist;
    }

    public void setlinklist(List<HypermediaLink> linklist){
        this.linklist = linklist;
    }
}
