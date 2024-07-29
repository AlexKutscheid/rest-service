package org.example.restservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PersonServiceTest {

    private PersonService personService;

    @BeforeEach
    void setUp() {
        personService = new PersonService();
    }

    @Test
    void testCreatePersonSuccess() {
        PersonObject person = new PersonObject(1, "Kutscheid", "Alex", LocalDate.of(1996, 6, 11));
        PersonObject createdPerson = personService.createPerson(person);

        assertNotNull(createdPerson);
        assertEquals(person.getId(), createdPerson.getId());
        assertEquals(person.getLastName(), createdPerson.getLastName());
        assertEquals(person.getFirstName(), createdPerson.getFirstName());
        assertEquals(person.getDateOfBirth(), createdPerson.getDateOfBirth());
    }

    @Test
    void testCreatePersonLastNameTooShort() {
        PersonObject person = new PersonObject(1, "Al", "Alex", LocalDate.of(1996, 6, 11));

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> personService.createPerson(person),
                "Expected throw"
        );

        assertTrue(thrown.getMessage().contains("Last name must be between 3 and 20 characters"));
    }

    @Test
    void testCreatePersonLastNameTooLong() {
        String longLastName = "A".repeat(21); // creates a string with 21 'A's
        PersonObject person = new PersonObject(1, longLastName, "Alex", LocalDate.of(1996, 6, 11));

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> personService.createPerson(person),
                "Expected throw"
        );

        assertTrue(thrown.getMessage().contains("Last name must be between 3 and 20 characters"));
    }

    @Test
    void testCreatePersonFirstNameTooLong() {
        String longFirstName = "A".repeat(21); // creates a string with 21 'A's
        PersonObject person = new PersonObject(1, "Kutscheid", longFirstName, LocalDate.of(1996, 6, 11));

        PersonObject createdPerson = personService.createPerson(person);

        assertNotNull(createdPerson);
        assertEquals(person.getFirstName(), createdPerson.getFirstName());
    }

    @Test
    void testCreatePersonDateOfBirthInFuture() {
        PersonObject person = new PersonObject(1, "Kutscheid", "Alex", LocalDate.now().plusDays(1));

        IllegalArgumentException thrown = assertThrows(
                IllegalArgumentException.class,
                () -> personService.createPerson(person),
                "Expected throw"
        );

        assertTrue(thrown.getMessage().contains("Date of birth cannot be in the future"));
    }
}