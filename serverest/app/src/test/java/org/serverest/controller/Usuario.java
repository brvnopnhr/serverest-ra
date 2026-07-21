package org.serverest.controller;

import org.serverest.model.UsuarioDTO;
import io.restassured.http.ContentType;
import org.serverest.util.Endpoint;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;

public class Usuario {
    public static void listar(Integer statuscode, String ambiente) {
        when()
                .get(ambiente.concat(Endpoint.usuarios))
        .then()
                .statusCode(statuscode);
    }

    public static UsuarioDTO cadastrar(UsuarioDTO usuarioDTO, Integer statusCode, String mensagem, String ambiente) {
        usuarioDTO.setId(given()
                .body("{\n" +
                        "  \"nome\": \"" + usuarioDTO.getNome() + "\",\n" +
                        "  \"email\": \"" + usuarioDTO.getEmail() + "\",\n" +
                        "  \"password\": \"" + usuarioDTO.getPassword() + "\",\n" +
                        "  \"administrador\": \"" + usuarioDTO.getAdministrador() + "\"\n" +
                        "}")
                .contentType(ContentType.JSON)
        .when()
                .post(ambiente.concat(Endpoint.usuarios))
        .then()
                .statusCode(statusCode)
                .body("message", is(mensagem))
                .extract().path("_id"));
        return usuarioDTO;
    }

    public static void buscarPorId(UsuarioDTO usuarioDTO, Integer statusCode, String ambiente) {
        given()
                .pathParam("_id", usuarioDTO.getId())
        .when()
                .get(ambiente.concat(Endpoint.usuariosId))
        .then()
                .statusCode(statusCode)
                .body("nome", is(usuarioDTO.getNome()))
                .body("email", is(usuarioDTO.getEmail()))
                .body("password", is(usuarioDTO.getPassword()))
                .body("administrador", is(usuarioDTO.getAdministrador()))
                .body("_id", is(usuarioDTO.getId()));
    }

    public static void excluir(UsuarioDTO usuarioDTO, Integer statusCode, String mensagem, String ambiente) {
        given()
                .pathParam("_id", usuarioDTO.getId())
        .when()
                .delete(ambiente.concat(Endpoint.usuariosId))
        .then()
                .statusCode(statusCode)
                .body("message", is(mensagem));
    }

    public static String editar(String id, UsuarioDTO usuarioDTO, Integer statusCode, String mensagem, String ambiente) {
        return given()
                .pathParam("_id", id)
                .body("{\n" +
                        "  \"nome\": \"" + usuarioDTO.getNome() + "\",\n" +
                        "  \"email\": \"" + usuarioDTO.getEmail() + "\",\n" +
                        "  \"password\": \"" + usuarioDTO.getPassword() + "\",\n" +
                        "  \"administrador\": \"" + usuarioDTO.getAdministrador() + "\"\n" +
                        "}")
                .contentType(ContentType.JSON)
        .when()
                .put(ambiente.concat(Endpoint.usuariosId))
        .then()
                .statusCode(statusCode)
                .body("message", is(mensagem))
                .extract().path("_id");
    }

    public static UsuarioDTO autenticar(UsuarioDTO usuarioDTO, Integer statusCode, String message, String ambiente) {
        usuarioDTO.setToken(given()
                .body("{\n" +
                        "  \"email\": \"" + usuarioDTO.getEmail() + "\",\n" +
                        "  \"password\": \"" + usuarioDTO.getPassword() + "\"\n" +
                        "}")
                .contentType(ContentType.JSON)
        .when()
                .post(ambiente.concat(Endpoint.login))
        .then()
                .statusCode(statusCode)
                .body("message", is(message))
                .extract().path("authorization"));
        return usuarioDTO;
    }

    public static void excluirComCarrinhoAssociado(UsuarioDTO usuarioDTO, Integer statusCode, String mensagem, String idCarrinho, String ambiente) {
        given()
                .pathParam("_id", usuarioDTO.getId())
                .when()
                .delete(ambiente.concat(Endpoint.usuariosId))
                .then()
                .statusCode(statusCode)
                .body("message", is(mensagem))
                .body("idCarrinho", is(idCarrinho));
    }
}