package com.mmnce.minhaFinancas.service;


import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.mmnce.minhaFinancas.exception.ErroAutenticacao;
import com.mmnce.minhaFinancas.exception.RegraNegocioException;
import com.mmnce.minhaFinancas.model.entity.Usuario;
import com.mmnce.minhaFinancas.model.repository.UsuarioRepository;
import com.mmnce.minhaFinancas.service.impl.UsuarioServiceImpl;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

	public static String EMAIL = "exemplo@email.com";
	public static String NOME = "Exemplo Nome";
	public static String SENHA = "senha";

	@SpyBean
	UsuarioServiceImpl service;

	@MockBean
	UsuarioRepository repository;

	@Test()
	public void deveSalvarUsuario() {
		Assertions.assertDoesNotThrow(() -> {
			// Cenário
			Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
			Usuario usuario = Usuario.builder().id(1l).nome(NOME).email(EMAIL).senha(SENHA).build();
			
			Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
			// Ação
			Usuario usuarioSalvo = service.salvarUsuario(new Usuario());

			// Verificação
			org.assertj.core.api.Assertions.assertThat(usuarioSalvo).isNotNull();
			org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
			org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo(NOME);
			org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo(EMAIL);
			org.assertj.core.api.Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo(SENHA);
		});
	};
	
	@Test
	public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			// Cenário
			Usuario usuario = Usuario.builder().email(EMAIL).build();
			Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(EMAIL);
			
			// Ação
			service.salvarUsuario(usuario);
			
			// Verificação
			Mockito.verify(repository, Mockito.never()).save(usuario);
		});
	}

	@Test
	public void deveAutenticaUmUsuarioComSucesso() {
		// Cenário
		Usuario usuario = Usuario.builder().email(EMAIL).senha(SENHA).id(1l).build();
		Mockito.when(repository.findByEmail(EMAIL)).thenReturn(Optional.of(usuario));

		// Açao
		Usuario result = service.autenticar(EMAIL, SENHA);

		// Verificação
		org.assertj.core.api.Assertions.assertThat(result).isNotNull();
	}

	@Test
	public void deveLancarErroQuandoNaoEncontrarUsuarioCadastradoComEmailInformado() {

		// Cenário
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

		// Ação
		Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.autenticar(EMAIL, SENHA));

		// Verificação
		org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class)
				.hasMessage("Usuário não encontrado para o email informado.");

	}

	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		// Cenário
		Usuario usuario = Usuario.builder().email(EMAIL).senha(SENHA).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

		// Ação
		Throwable exception = org.assertj.core.api.Assertions.catchThrowable(() -> service.autenticar(EMAIL, "12345"));

		// Verificação
		org.assertj.core.api.Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class)
				.hasMessage("Senha inválida.");
	}

	@Test()
	public void deveValidarEmail() {
		Assertions.assertDoesNotThrow(() -> {
			// Cenário

			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);

			// Ação
			service.validarEmail(EMAIL);
		});
	}

	@Test()
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		Assertions.assertThrows(RegraNegocioException.class, () -> {
			// Cenário
			Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);

			// Ação
			service.validarEmail(EMAIL);
		});
	}

}
