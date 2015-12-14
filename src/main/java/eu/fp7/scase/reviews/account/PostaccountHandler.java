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


package eu.fp7.scase.reviews.account;


import javax.ws.rs.core.UriInfo;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import com.sun.jersey.core.util.Base64;
import eu.fp7.scase.reviews.account.JavaaccountModel;

import eu.fp7.scase.reviews.utilities.HypermediaLink;
import eu.fp7.scase.reviews.utilities.HibernateController;


/* This class processes POST requests for account resources and creates the hypermedia to be returned to the client*/

public class PostaccountHandler{


    private HibernateController oHibernateController;
    private UriInfo oApplicationUri; //Standard datatype that holds information on the URI info of this request
    private JavaaccountModel oJavaaccountModel;
	private String authHeader;
	private JavaaccountModel oAuthenticationAccount;

    public PostaccountHandler(String authHeader, JavaaccountModel oJavaaccountModel, UriInfo oApplicationUri){
        this.oJavaaccountModel = oJavaaccountModel;
        this.oHibernateController = HibernateController.getHibernateControllerHandle();
        this.oApplicationUri = oApplicationUri;
		this.authHeader = authHeader;
		this.oAuthenticationAccount = new JavaaccountModel(); 
    }

    public JavaaccountModel postJavaaccountModel(){

    	//check if there is a non null authentication header
    	if(authHeader == null){
    		throw new WebApplicationException(Response.Status.FORBIDDEN);
    	}
		else if(authHeader.equalsIgnoreCase("guest")){ //if guest and authentication mode are allowed, check if the request originates from a guest user
        	return createHypermedia(oHibernateController.postaccount(oJavaaccountModel));
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

        return createHypermedia(oHibernateController.postaccount(oJavaaccountModel));
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
    public JavaaccountModel createHypermedia(JavaaccountModel oJavaaccountModel){

        /* Create hypermedia links towards this specific account resource. These must be GET and POST as it is prescribed in the meta-models.*/
        oJavaaccountModel.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oApplicationUri.getPath()), "Get all accounts", "GET", "Sibling"));
        oJavaaccountModel.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oApplicationUri.getPath()), "Create a new account", "POST", "Sibling"));

        /* Then calculate the relative path to any resources that are related of this one and add the according hypermedia links to the Linklist.*/
        String oRelativePath;
        oRelativePath = oApplicationUri.getPath();
        oJavaaccountModel.getlinklist().add(new HypermediaLink(String.format("%s%s/%d", oApplicationUri.getBaseUri(), oRelativePath, oJavaaccountModel.getaccountId()), oJavaaccountModel.getusername(), "GET", "Child", oJavaaccountModel.getaccountId()));
        oJavaaccountModel.getlinklist().add(new HypermediaLink(String.format("%s%s/%d", oApplicationUri.getBaseUri(), oRelativePath, oJavaaccountModel.getaccountId()), oJavaaccountModel.getusername(), "PUT", "Child", oJavaaccountModel.getaccountId()));
        oJavaaccountModel.getlinklist().add(new HypermediaLink(String.format("%s%s/%d", oApplicationUri.getBaseUri(), oRelativePath, oJavaaccountModel.getaccountId()), oJavaaccountModel.getusername(), "DELETE", "Child", oJavaaccountModel.getaccountId()));
        return oJavaaccountModel;
    }
}
