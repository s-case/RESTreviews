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


package eu.fp7.scase.reviews.review;


import javax.ws.rs.core.UriInfo;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import com.sun.jersey.core.util.Base64;
import eu.fp7.scase.reviews.account.JavaaccountModel;

import java.util.Iterator;
import eu.fp7.scase.reviews.utilities.HypermediaLink;
import eu.fp7.scase.reviews.utilities.HibernateController;
import eu.fp7.scase.reviews.product.JavaproductModel;

/* This class processes GET requests for review resources and creates the hypermedia to be returned to the client*/
public class GetreviewListHandler{

    private HibernateController oHibernateController;
    private UriInfo oApplicationUri; //Standard datatype that holds information on the URI info of this request
    private JavaproductModel oJavaproductModel;
	private String authHeader;
	private JavaaccountModel oAuthenticationAccount;

    public GetreviewListHandler(String authHeader, int productId, UriInfo oApplicationUri){
        this.oHibernateController = HibernateController.getHibernateControllerHandle();
        this.oApplicationUri = oApplicationUri;
        oJavaproductModel = new JavaproductModel();
        oJavaproductModel.setproductId(productId);
		this.authHeader = authHeader;
		this.oAuthenticationAccount = new JavaaccountModel(); 
    }

    public JavareviewModelManager getJavareviewModelManager(){

    	//check if there is a non null authentication header
    	if(authHeader == null){
    		throw new WebApplicationException(Response.Status.FORBIDDEN);
    	}
		else if(authHeader.equalsIgnoreCase("guest")){ //if guest and authentication mode are allowed, check if the request originates from a guest user
        	oJavaproductModel = oHibernateController.getreviewList(oJavaproductModel);
        	return createHypermedia(oJavaproductModel);
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

        oJavaproductModel = oHibernateController.getreviewList(oJavaproductModel);
        return createHypermedia(oJavaproductModel);
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
    public JavareviewModelManager createHypermedia(JavaproductModel oJavaproductModel){
        JavareviewModelManager oJavareviewModelManager = new JavareviewModelManager();

        /* Create hypermedia links towards this specific review resource. These must be GET and POST as it is prescribed in the meta-models.*/
        oJavareviewModelManager.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oApplicationUri.getPath()), "Get all reviews of this product", "GET", "Sibling"));
        oJavareviewModelManager.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oApplicationUri.getPath()), "Create a new review", "POST", "Sibling"));

        /* Then calculate the relative path to any related resource of this one and add for each one a hypermedia link to the Linklist.*/
        String oRelativePath;
        oRelativePath = oApplicationUri.getPath();
        Iterator<JavareviewModel> setIterator = oJavaproductModel.getSetOfJavareviewModel().iterator();
        while(setIterator.hasNext()){
            JavareviewModel oNextJavareviewModel = new JavareviewModel();
            oNextJavareviewModel = setIterator.next();
            oJavareviewModelManager.getlinklist().add(new HypermediaLink(String.format("%s%s/%d", oApplicationUri.getBaseUri(), oRelativePath, oNextJavareviewModel.getreviewId()), String.format("%s", oNextJavareviewModel.gettitle()), "GET", "Child", oNextJavareviewModel.getreviewId()));
        }

        /* Finally calculate the relative path towards the resources of which this one is related and add one hypermedia link for each one of them in the Linklist.*/
		oRelativePath = oApplicationUri.getPath();
        int iLastSlashIndex = String.format("%s%s", oApplicationUri.getBaseUri(), oRelativePath).lastIndexOf("/");
        oJavareviewModelManager.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oRelativePath).substring(0, iLastSlashIndex), "Delete the parent JavaproductModel", "DELETE", "Parent"));
        oJavareviewModelManager.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oRelativePath).substring(0, iLastSlashIndex), "Get the parent JavaproductModel", "GET", "Parent"));
        oJavareviewModelManager.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oRelativePath).substring(0, iLastSlashIndex), "Update the JavaproductModel", "PUT", "Parent"));
        return oJavareviewModelManager;
    }
}
