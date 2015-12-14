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

import eu.fp7.scase.reviews.utilities.HypermediaLink;
import eu.fp7.scase.reviews.utilities.HibernateController;
import eu.fp7.scase.reviews.product.JavaproductModel;

/* This class processes POST requests for review resources and creates the hypermedia to be returned to the client*/

public class PostreviewHandler{


    private HibernateController oHibernateController;
    private UriInfo oApplicationUri; //Standard datatype that holds information on the URI info of this request
    private JavareviewModel oJavareviewModel;
    private JavaproductModel oJavaproductModel;
	private String authHeader;
	private JavaaccountModel oAuthenticationAccount;

    public PostreviewHandler(String authHeader, int productId, JavareviewModel oJavareviewModel, UriInfo oApplicationUri){
        this.oJavareviewModel = oJavareviewModel;
        this.oHibernateController = HibernateController.getHibernateControllerHandle();
        this.oApplicationUri = oApplicationUri;
        oJavaproductModel = new JavaproductModel();
        oJavaproductModel.setproductId(productId);
        oJavareviewModel.setproduct(this.oJavaproductModel);
		this.authHeader = authHeader;
		this.oAuthenticationAccount = new JavaaccountModel(); 
    }

    public JavareviewModel postJavareviewModel(){

    	//check if there is a non null authentication header
    	if(authHeader == null){
    		throw new WebApplicationException(Response.Status.FORBIDDEN);
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

        return createHypermedia(oHibernateController.postreview(oJavareviewModel));
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
    public JavareviewModel createHypermedia(JavareviewModel oJavareviewModel){

        /* Create hypermedia links towards this specific review resource. These must be GET and POST as it is prescribed in the meta-models.*/
        oJavareviewModel.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oApplicationUri.getPath()), "Get all reviews of this product", "GET", "Sibling"));
        oJavareviewModel.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oApplicationUri.getPath()), "Create a new review", "POST", "Sibling"));

        /* Then calculate the relative path to any resources that are related of this one and add the according hypermedia links to the Linklist.*/
        String oRelativePath;
        oRelativePath = oApplicationUri.getPath();
        oJavareviewModel.getlinklist().add(new HypermediaLink(String.format("%s%s/%d", oApplicationUri.getBaseUri(), oRelativePath, oJavareviewModel.getreviewId()), oJavareviewModel.gettitle(), "GET", "Child", oJavareviewModel.getreviewId()));
        oJavareviewModel.getlinklist().add(new HypermediaLink(String.format("%s%s/%d", oApplicationUri.getBaseUri(), oRelativePath, oJavareviewModel.getreviewId()), oJavareviewModel.gettitle(), "PUT", "Child", oJavareviewModel.getreviewId()));
        oJavareviewModel.getlinklist().add(new HypermediaLink(String.format("%s%s/%d", oApplicationUri.getBaseUri(), oRelativePath, oJavareviewModel.getreviewId()), oJavareviewModel.gettitle(), "DELETE", "Child", oJavareviewModel.getreviewId()));

        /* Finally, calculate the relative path towards the resources of which this one is related.
        Then add hypermedia links for each one of them*/
		oRelativePath = oApplicationUri.getPath();
        int iLastSlashIndex = String.format("%s%s", oApplicationUri.getBaseUri(), oRelativePath).lastIndexOf("/");
        oJavareviewModel.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oRelativePath).substring(0, iLastSlashIndex), "Delete the parent JavaproductModel", "DELETE", "Parent"));
        oJavareviewModel.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oRelativePath).substring(0, iLastSlashIndex), "Get the parent JavaproductModel", "GET", "Parent"));
        oJavareviewModel.getlinklist().add(new HypermediaLink(String.format("%s%s", oApplicationUri.getBaseUri(), oRelativePath).substring(0, iLastSlashIndex), "Update the JavaproductModel", "PUT", "Parent"));
        return oJavareviewModel;
    }
}
