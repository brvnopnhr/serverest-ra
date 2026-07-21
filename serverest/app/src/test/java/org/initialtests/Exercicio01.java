package org.initialtests;

import org.junit.BeforeClass;
import org.junit.Test;
import com.github.javafaker.Faker;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;

public class Exercicio01 {
    @BeforeClass
    public static void preCondition() {
        baseURI = "http://localhost";
        port = 3000;
    }

    @Test
    public void exercicio01() {

        Faker faker = new Faker();
        String userName = faker.name().firstName();
        String userEmail = userName.concat("@eta.cesar.school");

        //A - Listar usuarios
        when()
                .get("/usuarios")
        .then()
                .statusCode(HttpStatus.SC_OK);

        //B - Cadastrar usuario
        String userId = given()
                .body("{\n" +
                        "  \"nome\": \"" + userName + "\",\n" +
                        "  \"email\": \"" + userEmail +"\",\n" +
                        "  \"password\": \"teste\",\n" +
                        "  \"administrador\": \"true\"\n" +
                        "}")
                .contentType(ContentType.JSON)
        .when()
                .post("/usuarios")
        .then()
                .statusCode(HttpStatus.SC_CREATED)
                .body("message", is("Cadastro realizado com sucesso"))
                .extract().path("_id");

        //B1 - Checar email duplicado
        given()
                .body("{\n" +
                        "  \"nome\": \"" + userName + "\",\n" +
                        "  \"email\": \"" + userEmail +"\",\n" +
                        "  \"password\": \"teste\",\n" +
                        "  \"administrador\": \"true\"\n" +
                        "}")
                .contentType(ContentType.JSON)
        .when()
                .post("/usuarios")
        .then()
                .statusCode(HttpStatus.SC_BAD_REQUEST)
                .body("message", is("Este email já está sendo usado"));

        //C - Listar detalhes do usuario
        given()
                .pathParam("_id", userId)
        .when()
                .get("/usuarios/{_id}")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("nome", is(userName))
                .body("email", is(userEmail))
                .body("password", is("teste"))
                .body("administrador", is("true"))
                .body("_id", is(userId));

        //D - Excluir usuario
        given()
                .pathParam("_id", userId)
        .when()
                .delete("/usuarios/{_id}")
        .then()
                .statusCode(HttpStatus.SC_OK)
                .body("message", is("Registro excluído com sucesso"));
    }
}