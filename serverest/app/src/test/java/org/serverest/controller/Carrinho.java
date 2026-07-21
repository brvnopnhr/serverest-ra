package org.serverest.controller;

import org.serverest.model.UsuarioDTO;
import org.serverest.model.ProdutoDTO;
import io.restassured.http.ContentType;
import org.serverest.util.Endpoint;
import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;

public class Carrinho {
    public static String cadastrar(ProdutoDTO produtoDTO, Integer quantidade, UsuarioDTO usuarioDTO, Integer statusCode, String mensagem, String ambiente) {
        return given()
                .header("authorization", usuarioDTO.getToken())
                .body("{\n" +
                        "  \"produtos\": [\n" +
                        "    {\n" +
                        "      \"idProduto\": \"" + produtoDTO.getId() + "\",\n" +
                        "      \"quantidade\": " + quantidade + "\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}")
                .contentType(ContentType.JSON)
                .when()
                .post(ambiente.concat(Endpoint.carrinhos))
                .then()
                .statusCode(statusCode)
                .body("message", is(mensagem))
                .extract().path("_id");
    }

    public static void cancelarCompra(UsuarioDTO usuarioDTO, Integer statusCode, String mensagem, String ambiente) {
        given()
                .header("authorization", usuarioDTO.getToken())
        .when()
                .delete(ambiente.concat(Endpoint.cancelarCompra))
        .then()
                .statusCode(statusCode)
                .body("message", is(mensagem));
    }

    public static void concluirCompra(UsuarioDTO usuarioDTO, Integer statusCode, String mensagem, String ambiente) {
        given()
                .header("authorization", usuarioDTO.getToken())
                .when()
                .delete(ambiente.concat(Endpoint.concluirCompra))
                .then()
                .statusCode(statusCode)
                .body("message", is(mensagem));
    }
}