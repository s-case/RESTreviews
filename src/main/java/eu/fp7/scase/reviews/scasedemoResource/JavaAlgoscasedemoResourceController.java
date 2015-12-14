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


package eu.fp7.scase.reviews.scasedemoResource;

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
import javax.ws.rs.QueryParam; 



/*
 * JavaAlgoscasedemoResourceController class is responsible to handle incoming HTTP requests for the scasedemoResource resource. More specifically
 * this resource controller handles client requests that are delegated to an external server, which is executed at
 * http://109.231.126.106:8080/creditCharge-0.0.1-SNAPSHOT/rest/result/query
*/
@Path("/AlgoscasedemoResource")
public class JavaAlgoscasedemoResourceController{

    @Context
    private UriInfo oApplicationUri;

	/* 
	 * This function is the WEB API of resource scasedemoResource and accepts http  requests,   
     * which it delegates to the underlying Handler GetscasedemoResourceHandler. 
     * It returns any response formatted as stated in the @Produces JAX-RS annotation below.
    */
	@Path("/")
	@GET
	@Produces("application/JSON")
    public JavaScasedemoResourceOutputModel getscasedemoResource(@HeaderParam("authorization") String authHeader, @QueryParam("amount") String amount, @QueryParam("exp_month") String exp_month, @QueryParam("domain") String domain, @QueryParam("description") String description, @QueryParam("exp_year") String exp_year, @QueryParam("to") String to, @QueryParam("card_number") String card_number, @QueryParam("from") String from, @QueryParam("text") String text, @QueryParam("apikey") String apikey, @QueryParam("currency") String currency, @QueryParam("subject") String subject){

		//create a new GetscasedemoResourceHandler
		GetscasedemoResourceHandler oGetscasedemoResourceHandler = new GetscasedemoResourceHandler(authHeader, amount,  exp_month,  domain,  description,  exp_year,  to,  card_number,  from,  text,  apikey,  currency,  subject, oApplicationUri);
		return oGetscasedemoResourceHandler.getscasedemoresource();
    }
}

