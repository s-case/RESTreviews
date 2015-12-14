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

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.DefaultValue;


/* This class defines the web API of the individual purchase resource. It may handle PUT, GET and/or DELETE requests 
   depending on the specific CIM of the service.*/

@Path("/account/{accountId}/purchase/{purchaseId}")
public class JavapurchaseController{

    @Context
    private UriInfo oApplicationUri;

	/* This function handles http GET requests  
    and returns any response formatted as stated in the @Produces JAX-RS annotation below.*/
	@Path("/")
	@GET
	@Produces("application/JSON")
    public JavapurchaseModel getpurchase(@HeaderParam("authorization") String authHeader, @PathParam("purchaseId") int purchaseId){
        GetpurchaseHandler oGetpurchaseHandler = new GetpurchaseHandler(authHeader, purchaseId, oApplicationUri);
        return oGetpurchaseHandler.getJavapurchaseModel();
    }


    /* This function handles http DELETE requests  
    and returns any response formatted as stated in the @Produces JAX-RS annotation below.*/
	@Path("/")
	@DELETE
	@Produces("application/JSON")
    public JavapurchaseModel deletepurchase(@HeaderParam("authorization") String authHeader, @PathParam("purchaseId") int purchaseId){
        DeletepurchaseHandler oDeletepurchaseHandler = new DeletepurchaseHandler(authHeader, purchaseId, oApplicationUri);
        return oDeletepurchaseHandler.deleteJavapurchaseModel();
    }
}

