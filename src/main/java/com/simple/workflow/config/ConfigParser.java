package com.simple.workflow.config;


import java.net.URL;

import com.simple.workflow.Workflow;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.RuleSet;


public class ConfigParser {

    // ----------------------------------------------------- Instance Variables


    private Digester digester = null;

    private RuleSet ruleSet = null;

    private boolean useContextClassLoader = true;

    // ------------------------------------------------------------- Properties


    public Digester getDigester() {

        if (digester == null) {
            digester = new Digester();
            RuleSet ruleSet = getRuleSet();
            digester.setNamespaceAware(ruleSet.getNamespaceURI() != null);
            digester.setUseContextClassLoader(getUseContextClassLoader());
            digester.setValidating(false);
            digester.addRuleSet(ruleSet);
        }
        return (digester);

    }


    public RuleSet getRuleSet() {

        if (ruleSet == null) {
            ruleSet = new ConfigRuleSet();
        }
        return (ruleSet);

    }

    public void setRuleSet(RuleSet ruleSet) {

        this.digester = null;
        this.ruleSet = ruleSet;

    }


    public boolean getUseContextClassLoader() {

        return (this.useContextClassLoader);

    }


    public void setUseContextClassLoader(boolean useContextClassLoader) {

        this.useContextClassLoader = useContextClassLoader;

    }

    // --------------------------------------------------------- Public Methods


    public void parse(Workflow workflow, URL url) throws Exception {

        // Prepare our Digester instance
        Digester digester = getDigester();
        digester.clear();
        digester.push(workflow);

        // Parse the configuration document
        digester.parse(url);

    }


    public void parse(URL url) throws Exception {

        // Prepare our Digester instance
        Digester digester = getDigester();
        digester.clear();

        // Parse the configuration document
        digester.parse(url);

    }


}
