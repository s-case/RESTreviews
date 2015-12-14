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

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.DefaultValue;


/* This class defines the web API of the individual product resource. It may handle PUT, GET and/or DELETE requests 
   depending on the specific CIM of the service.*/

@Path("/product/{productId}")
public class JavaproductController{

    @Context
    private UriInfo oApplicationUri;

	/* This function handles http GET requests  
    and returns any response formatted as stated in the @Produces JAX-RS annotation below.*/
	@Path("/")
	@GET
	@Produces("application/JSON")
    public JavaproductModel getproduct(@DefaultValue("guest") @HeaderParam("authorization") String authHeader, @PathParam("productId") int productId){
        GetproductHandler oGetproductHandler = new GetproductHandler(authHeader, productId, oApplicationUri);
        return oGetproductHandler.getJavaproductModel();
    }

	/* This function handles http PUT requests that are sent with any media type stated in the @Consumes JAX-RS annotation below 
    and returns any response formatted as stated in the @Produces JAX-RS annotation below.*/
	@Path("/")
	@PUT
	@Produces("application/JSON")
	@Consumes("application/JSON")
    public JavaproductModel putproduct(@HeaderParam("authorization") String authHeader, @PathParam("productId") int productId,  JavaproductModel oJavaproductModel){
        PutproductHandler oPutproductHandler = new PutproductHandler(authHeader, productId, oJavaproductModel, oApplicationUri);
        return oPutproductHandler.putJavaproductModel();
    }

    /* This function handles http DELETE requests  
    and returns any response formatted as stated in the @Produces JAX-RS annotation below.*/
	@Path("/")
	@DELETE
	@Produces("application/JSON")
    public JavaproductModel deleteproduct(@HeaderParam("authorization") String authHeader, @PathParam("productId") int productId){
        DeleteproductHandler oDeleteproductHandler = new DeleteproductHandler(authHeader, productId, oApplicationUri);
        return oDeleteproductHandler.deleteJavaproductModel();
    }
}

