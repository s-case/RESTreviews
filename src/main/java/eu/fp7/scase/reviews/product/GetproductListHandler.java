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


import javax.ws.rs.core.UriInfo;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import com.sun.jersey.core.util.Base64;
import eu.fp7.scase.reviews.account.JavaaccountModel;

import java.util.Iterator;
import eu.fp7.scase.reviews.utilities.HypermediaLink;
import eu.fp7.scase.reviews.utilities.HibernateController;
import java.util.Set;
import java.util.HashSet;

/* This class processes GET requests for product resources and creates the hypermedia to be returned to the client*/

public class GetproductListHandler{

    private HibernateController oHibernateController;
    private UriInfo oApplicationUri; //Standard datatype that holds information on the URI info of this request
    private Set<JavaproductModel> SetOfproductModel = new HashSet<JavaproductModel>(); // Since this resource is not related of any other, this HashSet will hold the list to be sent back to client.
	private String authHeader;
	private JavaaccountModel oAuthenticationAccount;

    public GetproductListHandler(String authHeader, UriInfo oApplicationUri){
        this.oHibernateController = HibernateController.getHibernateControllerHandle();
        this.oApplicationUri = oApplicationUri;
		this.authHeader = authHeader;
		this.oAuthenticationAccount = new JavaaccountModel(); 
    }

    public JavaproductModelManager getJavaproductModelManager(){

    	//check if there is a non null authentication header
    	if(authHeader == null){
    		throw new WebApplicationException(Response.Status.FORBIDDEN);
    	}
		else if(authHeader.equalsIgnoreCase("guest")){ //if guest and authentication mode are allowed, check if the request originates from a guest user
			this.SetOfproductModel = oHibernateController.getproductList(this.SetOfproductModel);
        	return createHypermedia();
		}
		else{
	    	//decode the auth header
    		decodeAuthorizationHeader();

        	//authenticate the user against the database
        	oAuthenticationAccount = oHibernateController.authenticateUser(oAuthenticationAccount);

			//check if the authentication failed
			if(oAuthenticationAccount == null){
        		throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        	}
		}

        this.SetOfproductModel = oHibernateController.getproductList(this.SetOfproductModel);
        return createHypermedia();
    }

	/* This function performs the decoding of the authentication header */
    public void decodeAuthorizationHeader()
    {
    	//check if this request has basic authentication
    	if( !authHeader.contains("Basic "))
    	{
    		throw new WebApplicationException(Response.Status.BAD_REQUEST);
    	}
    	
        authHeader = authHeader.substring("Basic ".length());
        String[] decodedHeader;
        decodedHeader = Base64.base64Decode(authHeader).split(":");
        
        if( decodedHeader == null)
        {
        	throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
        
        oAuthenticationAccount.setusername(decodedHeader[0]);
        oAuthenticationAccount.setpassword(decodedHeader[1]);
    }

    /* This function produces hypermedia links to be sent to the client so as it will be able to forward the application state in a valid way.*/
    public JavaproductModelManager createHypermedia(){
        JavaproductModelManager oJavaproductModelManager = new JavaproductModelManager();

        /* Create hypermedia links towards this specific product resource. These must be GET and POST as it is prescribed in the meta-models.*/
        oJavaproductModelManager.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oApplicationUri.getPath()), "Get all products", "GET", "Sibling"));
        oJavaproductModelManager.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oApplicationUri.getPath()), "Create a new product", "POST", "Sibling"));

        /* Then calculate the relative path to any related resource of this one and add for each one a hypermedia link to the Linklist.*/
        String oRelativePath;
        oRelativePath = oApplicationUri.getPath();
        Iterator<JavaproductModel> setIterator = this.SetOfproductModel.iterator();
        while(setIterator.hasNext()){
            JavaproductModel oNextJavaproductModel = new JavaproductModel();
            oNextJavaproductModel = setIterator.next();
            oJavaproductModelManager.getlinklist().add(new HypermediaLink(String.format("%s%s/%d", oApplicationUri.getBaseUri(), oRelativePath, oNextJavaproductModel.getproductId()), String.format("%s", oNextJavaproductModel.gettitle()), "GET", "Child", oNextJavaproductModel.getproductId()));
        }
        return oJavaproductModelManager;
    }
}
