package org.serverest.test;

import org.junit.Before;
import org.junit.Test;
import org.apache.http.HttpStatus;
import org.serverest.controller.Carrinho;
import org.serverest.controller.Produto;
import org.serverest.controller.Usuario;
import org.serverest.factory.ProdutoFactory;
import org.serverest.factory.UsuarioFactory;
import org.serverest.model.ProdutoDTO;
import org.serverest.model.UsuarioDTO;
import org.serverest.util.Ambiente;
import org.serverest.util.Mensagem;

public class TestExercicioCasa {
    private String ambiente;
    private String userPassword = "teste";
    private String adminUser = "true";
    private Integer productPrice = 10;
    private Integer productInitialAmount = 100;
    private Integer productCartAmount = 8;

    @Before
    public void definirAmbiente() {
        ambiente = Ambiente.localhost;
    }

    @Test
    public void naoDevePermitirExcluirUsuarioComCarrinhoAssociado() {
        //Criando usuario
        UsuarioDTO admin = UsuarioFactory.criar(userPassword, adminUser);
        Usuario.cadastrar(admin, HttpStatus.SC_CREATED, Mensagem.cadastroSucesso, ambiente);

        //Autenticando usuario
        Usuario.autenticar(admin, HttpStatus.SC_OK, Mensagem.loginSucesso, ambiente);

        //Criando produto
        ProdutoDTO produto = ProdutoFactory.criar(productPrice, productInitialAmount);
        Produto.cadastrar(produto, admin, HttpStatus.SC_CREATED, Mensagem.cadastroSucesso, ambiente);

        //Criando carrinho e capturando o id gerado
        String idCarrinho = Carrinho.cadastrar(produto, productCartAmount, admin, HttpStatus.SC_CREATED, Mensagem.cadastroSucesso, ambiente);

        //Checando estoque reduzido
        Produto.checarEstoque(produto, productInitialAmount - productCartAmount, HttpStatus.SC_OK, ambiente);

        //Tentando excluir usuario com carrinho associado
        Usuario.excluirComCarrinhoAssociado(admin, HttpStatus.SC_BAD_REQUEST, Mensagem.usuarioComCarrinhoNaoExcluido, idCarrinho, ambiente);

        //Concluindo a compra (carrinho é removido, estoque NAO e reabastecido)
        Carrinho.concluirCompra(admin, HttpStatus.SC_OK, Mensagem.exclusaoSucesso, ambiente);

        //Confirmando que o estoque permanece reduzido apos conclusao da compra
        Produto.checarEstoque(produto, productInitialAmount - productCartAmount, HttpStatus.SC_OK, ambiente);

        //Excluindo produto
        Produto.excluir(produto, admin, HttpStatus.SC_OK, Mensagem.exclusaoSucesso, ambiente);

        //Excluindo usuario, agora sem carrinho associado
        Usuario.excluir(admin, HttpStatus.SC_OK, Mensagem.exclusaoSucesso, ambiente);
    }
}