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


import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.DefaultValue;


/* This class defines the web API of the manager purchase resource. It handles POST and GET HTTP requests, as it is prescribed by the meta-models.*/
@Path("/account/{accountId}/purchase")
public class JavapurchaseControllerManager{

    @Context
    private UriInfo oApplicationUri;

	/* This function handles POST requests that are sent with any media type stated in the @Consumes JAX-RS annotation below 
     and returns any response in any media type stated in the @Produces JAX-RS annotation below.*/
	@Path("/")
	@POST
	@Produces("application/JSON")
	@Consumes("application/JSON")
    public JavapurchaseModel postpurchase(@HeaderParam("authorization") String authHeader, @PathParam("accountId")int accountId, JavapurchaseModel oJavapurchaseModel){
        PostpurchaseHandler oPostpurchaseHandler = new PostpurchaseHandler(authHeader, accountId, oJavapurchaseModel, oApplicationUri);
        return oPostpurchaseHandler.postJavapurchaseModel();
    }

    /* This function handles GET requests  
     and returns any response in any media type stated in the @Produces JAX-RS annotation below.*/
	@Path("/")
	@GET
	@Produces("application/JSON")
    public JavapurchaseModelManager getpurchaseList(@HeaderParam("authorization") String authHeader, @PathParam("accountId")int accountId){
        GetpurchaseListHandler oGetpurchaseListHandler = new GetpurchaseListHandler(authHeader, accountId, oApplicationUri);
        return oGetpurchaseListHandler.getJavapurchaseModelManager();
    }

}
