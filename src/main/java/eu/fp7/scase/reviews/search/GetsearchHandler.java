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


package eu.fp7.scase.reviews.search;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.ws.rs.core.UriInfo;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import com.sun.jersey.core.util.Base64;
import eu.fp7.scase.reviews.account.JavaaccountModel;

import eu.fp7.scase.reviews.utilities.HypermediaLink;
import eu.fp7.scase.reviews.utilities.HibernateController;
import eu.fp7.scase.reviews.utilities.PersistenceUtil;
import eu.fp7.scase.reviews.product.JavaproductModel;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

/* This class processes search requests for search resource and creates the hypermedia links with the search results to be returned to the client*/
public class GetsearchHandler{

    private HibernateController oHibernateController;
    private UriInfo oApplicationUri; //Standard datatype that holds information on the URI info of this request
    private JavaAlgosearchModel oJavaAlgosearchModel;
	private String searchProductCategory;
	private String searchProductTitle;
	private String searchProductDescription;
	private String searchKeyword;
	private String authHeader;
	private JavaaccountModel oAuthenticationAccount;

    public GetsearchHandler(String authHeader, String searchKeyword, String searchProductCategory, String searchProductTitle, String searchProductDescription, UriInfo oApplicationUri){
        oJavaAlgosearchModel = new JavaAlgosearchModel();
        this.oHibernateController = HibernateController.getHibernateControllerHandle();
        this.oApplicationUri = oApplicationUri;
		this.authHeader = authHeader;
		this.oAuthenticationAccount = new JavaaccountModel(); 
		this.searchProductCategory = searchProductCategory;
		this.searchProductTitle = searchProductTitle;
		this.searchProductDescription = searchProductDescription;
		this.searchKeyword = searchKeyword;
    }

    public JavaAlgosearchModel getsearch(){

    	//check if there is a non null authentication header
    	if(authHeader == null){
    		throw new WebApplicationException(Response.Status.FORBIDDEN);
    	}
		else if(authHeader.equalsIgnoreCase("guest")){ //if guest and authentication mode are allowed, check if the request originates from a guest user
			return searchDatabase();
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
        return searchDatabase();
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

    /* This function produces hypermedia links to be sent to the client that include all the search results, so as it will be able to forward the application state in a valid way.*/
    public JavaAlgosearchModel searchDatabase(){
		
		// if any searchable property of resource Product is included in clients search request
    	if((this.searchProductCategory != null && this.searchProductCategory.equalsIgnoreCase("true")) || (this.searchProductTitle != null && this.searchProductTitle.equalsIgnoreCase("true")) || (this.searchProductDescription != null && this.searchProductDescription.equalsIgnoreCase("true")))
    	{
			//then add hypermedia links to any search results from this resource
    		addProductHypermediaLinks();
    	}

		return this.oJavaAlgosearchModel;
	}

	/* This functions produces hypermedia links to be sent to the client that include search results of resource Product search requests
	 */
    public void addProductHypermediaLinks(){
   
    	FullTextEntityManager oFullTextEntityManager = PersistenceUtil.getFullTextEntityManager();
    	PersistenceUtil.beginEntityManagerTransaction();

		ArrayList<String> strQueryParams = new ArrayList<String>();		

		if((this.searchProductCategory != null && this.searchProductCategory.equalsIgnoreCase("true"))){
			strQueryParams.add("category");
		}

		if((this.searchProductTitle != null && this.searchProductTitle.equalsIgnoreCase("true"))){
			strQueryParams.add("title");
		}

		if((this.searchProductDescription != null && this.searchProductDescription.equalsIgnoreCase("true"))){
			strQueryParams.add("description");
		}

    	QueryBuilder oQueryBuilder = oFullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(JavaproductModel.class).get();
    	org.apache.lucene.search.Query oLuceneQuery = oQueryBuilder.keyword().onFields(strQueryParams.toArray(new String[strQueryParams.size()])).matching(this.searchKeyword).createQuery();
    	// wrap Lucene query in a javax.persistence.Query
    	javax.persistence.Query oJpaQuery = oFullTextEntityManager.createFullTextQuery(oLuceneQuery, JavaproductModel.class);

    	// execute search
    	List<JavaproductModel> JavaproductModelList =(List<JavaproductModel>) oJpaQuery.getResultList();

    	Iterator<JavaproductModel> iteratorOfJavaproductModelList = JavaproductModelList.iterator();
    	while(iteratorOfJavaproductModelList.hasNext())
    	{
    		JavaproductModel oJavaproductModel = iteratorOfJavaproductModelList.next();
			oJavaAlgosearchModel.getlinklist().add(new HypermediaLink(String.format("%s%s/%d", oApplicationUri.getBaseUri(), "product", oJavaproductModel.getproductId()), "Search result", "GET", "product"));
    	}
    	
    	PersistenceUtil.endEntityManagerTransaction();    	
    }	
	
}
