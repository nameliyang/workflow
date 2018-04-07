
package com.simple.workflow.config;


import org.apache.commons.digester.Rule;
import org.xml.sax.Attributes;


class ConfigDefineRule extends Rule {


    // ----------------------------------------------------------- Constructors


    public ConfigDefineRule(String nameAttribute, String classAttribute) {
        super();
        this.nameAttribute = nameAttribute;
        this.classAttribute = classAttribute;
    }


    // ----------------------------------------------------- Instance Variables


    private String classAttribute = null;


    private String nameAttribute = null;


    // --------------------------------------------------------- Public Methods


    public void begin(String namespace, String name, Attributes attributes)
        throws Exception {

        // Extract the actual name and implementation class to use
        String nameValue = attributes.getValue(nameAttribute);
        String classValue = attributes.getValue(classAttribute);

        // Add rules for this new element
        digester.addObjectCreate("*/" + nameValue, classValue);
        digester.addSetProperties("*/" + nameValue);
        digester.addRule("*/" + nameValue,
                         new ConfigRegisterRule(nameAttribute));

    }


}
