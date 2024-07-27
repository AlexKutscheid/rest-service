package org.example.restservice;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(Arquillian.class)
public class PersonServiceArquillianTest {

    @Deployment
    public static Archive<?> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(PersonObject.class, PersonService.class)
                .addAsResource("META-INF/persistence.xml");
    }

    @Test
    public void testCreatePerson() {
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("http://localhost:8080/rest-service/api/person");

        PersonObject newPerson = new PersonObject(1, "Alex", "Kutscheid", LocalDate.of(1996, 6, 11));

        Response response = target.request()
                .post(javax.ws.rs.client.Entity.json(newPerson));

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        PersonObject createdPerson = response.readEntity(PersonObject.class);
        assertEquals(newPerson.getId(), createdPerson.getId());
        assertEquals(newPerson.getLastName(), createdPerson.getLastName());
        assertEquals(newPerson.getFirstName(), createdPerson.getFirstName());
        assertEquals(newPerson.getDateOfBirth(), createdPerson.getDateOfBirth());
    }
}
