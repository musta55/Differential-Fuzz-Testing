package org.apache.deltaspike.example.message;

import org.apache.deltaspike.jsf.api.message.JsfMessage;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Date;

/**
 *
 */
@Named
@RequestScoped
public class ControllerView {

    private String name;

    @Inject
    private JsfMessage<ApplicationMessages> msg;

    @Inject
    private JsfMessage<CustomizedMessages> custom;

    public void doGreeting() {
        msg.addInfo().helloWorld(name);
    }

    public String getName() {
        return name;
    }

    public void setName(String someName) {
        name = someName;
    }

    public String getNow() {
        // getTimestampMessage return a Message where you could do some customizations before calling toString().
        return custom.get().getTimestampMessage(new Date()).toString();
    }

    public String getCustomMessage() {
        return custom.get().fromFacesMessageBundle();
    }
}