package org.example.restservice;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;

@Path("/persons")
public class PersonResource {
    private PersonService personService = new PersonService();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createPerson(PersonObject person) {

        // Validate the person object
        String validationError = validatePerson(person);
        if (validationError != null) {
            return Response.status(Response.Status.BAD_REQUEST).entity(validationError).build();
        }

        PersonObject createdPerson = personService.createPerson(person);
        return Response.status(Response.Status.CREATED).entity(createdPerson).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePerson(PersonObject person) {
        PersonObject updatedPerson = personService.update(person);
        return Response.ok(updatedPerson).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePerson(PersonObject person) {
        personService.delete(person);
        return Response.noContent().build();
    }

    private String validatePerson(PersonObject person) {
        if (person.getLastName() == null || person.getLastName().length() < 3 || person.getLastName().length() > 20) {
            return "Last name must be between 3 and 20 characters.";
        }
        if (person.getFirstName() != null && person.getFirstName().length() > 20) {
            return "First name cannot be more than 20 characters.";
        }
        if (person.getDateOfBirth() == null || person.getDateOfBirth().isAfter(LocalDate.now())) {
            return "Date of birth cannot be in the future.";
        }
        return null; // No validation errors
    }
}
