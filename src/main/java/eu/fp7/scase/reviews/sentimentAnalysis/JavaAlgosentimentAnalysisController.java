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
 * JavaAlgosentimentAnalysisController class is responsible to handle incoming HTTP requests for the sentimentAnalysis resource. More specifically
 * this resource controller handles client requests that are delegated to an external server, which is executed at
 * http://thalis.ee.auth.gr:3000/sentiment
*/
@Path("/AlgosentimentAnalysis")
public class JavaAlgosentimentAnalysisController{

    @Context
    private UriInfo oApplicationUri;

	/* 
	 * This function is the WEB API of resource sentimentAnalysis and accepts http  requests,   
     * which it delegates to the underlying Handler GetsentimentAnalysisHandler. 
     * It returns any response formatted as stated in the @Produces JAX-RS annotation below.
    */
	@Path("/")
	@GET
	@Produces("application/JSON")
    public JavaSentimentAnalysisOutputModel getsentimentAnalysis(@DefaultValue("guest") @HeaderParam("authorization") String authHeader, @QueryParam("text") String text){

		//create a new GetsentimentAnalysisHandler
		GetsentimentAnalysisHandler oGetsentimentAnalysisHandler = new GetsentimentAnalysisHandler(authHeader, text, oApplicationUri);
		return oGetsentimentAnalysisHandler.getsentimentanalysis();
    }
}

