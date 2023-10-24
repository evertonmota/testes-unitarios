package br.ce.wcaquino.servicos;

import static org.junit.Assert.assertEquals;

import java.awt.CardLayout;

import org.junit.Before;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.exceptions.NaoPodeDividirPorZeroException;
import junit.framework.Assert;

public class CalculadoraTest {

	private Calculadora calc;
	
	@Before
	public void setup() {
		calc = new Calculadora();
	}
	
	@Test
	public void deveSomarDoisValores() {
		
		//cenário
		int a = 5;
		int b= 3;
		calc = new Calculadora();
		
		//ação
		int resultado = calc.somar(a, b);
		
		//verificação
		Assert.assertEquals(8, resultado);
	}
	
	@Test
	public void deveSubtrairDoisValores() {
		// cenário
		int a = 10;
		int b =5;
		calc = new Calculadora();
		
		//ação
		int resultado = calc.subtrair(a , b);
		
		//Verificação
		Assert.assertEquals(5, resultado);
	}
	
	@Test
	public void deveDividirDoisValores() throws NaoPodeDividirPorZeroException{
		//cenário
		int a = 6;
		int b = 3;
		calc = new Calculadora();
		//ação
		int resultado = calc.divide(a , b);
		
		//verificação
		Assert.assertEquals(2, resultado);
	}

	@Test(expected = NaoPodeDividirPorZeroException.class)
	public void deveLancarExcecaoAoDividirPorZero() throws NaoPodeDividirPorZeroException {
		//cenário
		int a = 10;
		int b = 0;
		calc = new Calculadora();
		//ação
		int resultado = calc.divide(a , b);
		
		//verificação
	}

}
