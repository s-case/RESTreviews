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


package eu.fp7.scase.reviews.utilities;


import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import eu.fp7.scase.reviews.review.JavareviewModel;
import eu.fp7.scase.reviews.product.JavaproductModel;
import eu.fp7.scase.reviews.purchase.JavapurchaseModel;
import eu.fp7.scase.reviews.account.JavaaccountModel;

/* This class follows the singleton pattern in order to build once and provide a unique hibernate session instance*/

public class HibernateUtil{

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory(){
        try {
        /* Create the unique hibernate session. All resource models should be added here.*/
            return new AnnotationConfiguration().configure()
					.addAnnotatedClass(JavareviewModel.class)
					.addAnnotatedClass(JavaproductModel.class)
					.addAnnotatedClass(JavapurchaseModel.class)
					.addAnnotatedClass(JavaaccountModel.class)
                    .buildSessionFactory();
        }
        catch (Throwable ex){
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory(){
        return sessionFactory;
    }
}
