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

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.WebApplicationException;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import com.sun.jersey.core.util.Base64;
import eu.fp7.scase.reviews.account.JavaaccountModel;

import eu.fp7.scase.reviews.utilities.HypermediaLink;
import eu.fp7.scase.reviews.utilities.HibernateController;



/* 
 *This class processes client requests for scasedemoResource resource that are to be delegated to an existing 3rd party service. 
 *Uppon its output receival, this class repackages the output and creates the hypermedia links with the search results to be returned to the client
*/
public class GetscasedemoResourceHandler{

    private HibernateController oHibernateController;
    private UriInfo oApplicationUri; //Standard datatype that holds information on the URI info of this request
	private JavaScasedemoResourceOutputModel oOutputDataModel;
	private String amount;
	private String exp_month;
	private String domain;
	private String description;
	private String exp_year;
	private String to;
	private String card_number;
	private String from;
	private String text;
	private String apikey;
	private String currency;
	private String subject;
	private String authHeader;
	private JavaaccountModel oAuthenticationAccount;
	private ClientConfig oClientConfiguration;

    public GetscasedemoResourceHandler(String authHeader, String amount, String exp_month, String domain, String description, String exp_year, String to, String card_number, String from, String text, String apikey, String currency, String subject, UriInfo oApplicationUri){
        this.oHibernateController = HibernateController.getHibernateControllerHandle();
        this.oApplicationUri = oApplicationUri;
		this.amount = amount;
		this.exp_month = exp_month;
		this.domain = domain;
		this.description = description;
		this.exp_year = exp_year;
		this.to = to;
		this.card_number = card_number;
		this.from = from;
		this.text = text;
		this.apikey = apikey;
		this.currency = currency;
		this.subject = subject;
		//initialize authentication variables
		this.authHeader = authHeader;
		this.oAuthenticationAccount = new JavaaccountModel(); 

		//initialize JAX-RS Client configuration 
		this.oClientConfiguration = new DefaultClientConfig();
		this.oClientConfiguration.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
    }

    public JavaScasedemoResourceOutputModel getscasedemoresource(){

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

		//Return any results in the hypermedia links form.
        return interoperateWithExternalService();
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

    /* 
	 * This function handles the interoperation with the existing 3rd party service. It calls the according functions to create the input 
	 * to be sent to it, to receive its output and if necessary to persist the outcome in the database. 
	 * Finally, the result is returned to the client.
    */
    public JavaScasedemoResourceOutputModel interoperateWithExternalService(){
		
		//create a new JAX-RS client
        Client oJAXRSRESTClient = Client.create(this.oClientConfiguration);

		//construct the GET query
		WebResource oTargetResource = oJAXRSRESTClient.resource("http://109.231.126.106:8080/creditCharge-0.0.1-SNAPSHOT/rest/result/query").queryParam("amount", this.amount).queryParam("exp_month", this.exp_month).queryParam("domain", this.domain).queryParam("description", this.description).queryParam("exp_year", this.exp_year).queryParam("to", this.to).queryParam("card_number", this.card_number).queryParam("from", this.from).queryParam("text", this.text).queryParam("apikey", this.apikey).queryParam("currency", this.currency).queryParam("subject", this.subject);
        ClientResponse oResponse = oTargetResource.accept(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(ClientResponse.class);
        this.oOutputDataModel = oResponse.getEntity(JavaScasedemoResourceOutputModel.class);

        if (isSuccessfullResponseCode(oResponse) == false) {
            throw new WebApplicationException();
        }	

		return this.oOutputDataModel;
	}

	/*
	 * This function checks the response code of the server reply against the known success codes. If the server response HTTP
     * code is within the success code range, this functions returns true. Otherwise it returns false
	*/
	private boolean isSuccessfullResponseCode(ClientResponse oResponse){
		if(oResponse.getStatus() == 200){ // Status OK
			return true;
		}
		else if(oResponse.getStatus() == 201){ // Status CREATED
			return true;
		}
		else if(oResponse.getStatus() == 202){ // Status Accepted
			return true;
		}
		else if(oResponse.getStatus() == 203){ // Status NON-AUTHORITATIVE INFORMATION
			return true;
		}
		else if(oResponse.getStatus() == 204){ // Status NO CONTENT
			return true;
		}
		else if(oResponse.getStatus() == 205){ // Status RESET CONTENT
			return true;
		}
		else if(oResponse.getStatus() == 206){ // Status PARTIAL CONTENT
			return true;
		}

		return false;
	}
	
}
