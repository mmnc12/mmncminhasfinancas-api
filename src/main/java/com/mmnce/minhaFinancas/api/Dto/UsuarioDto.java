package com.mmnce.minhaFinancas.api.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsuarioDto {

	private String nome;
	private String email;
	private String senha;
}
