package org.pikater.core.ontology;

import jade.content.onto.BeanOntology;
import jade.content.onto.Ontology;

import org.pikater.core.ontology.subtrees.account.User;


public class AccountOntology extends BeanOntology {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7595856728415263726L;

	private AccountOntology() {
        super("AccountOntology");

        String userPackage = User.class.getPackage().getName();
        
        try {
            add(userPackage);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static AccountOntology theInstance = new AccountOntology();

    public static Ontology getInstance() {
        return theInstance;
    }
}
