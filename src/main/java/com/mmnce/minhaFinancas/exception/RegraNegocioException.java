package com.mmnce.minhaFinancas.exception;

public class RegraNegocioException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	//public RegraNegocioException(String email) {
		
	//}
	
	public RegraNegocioException(String mensagem) {
		super(mensagem);
	}
}
