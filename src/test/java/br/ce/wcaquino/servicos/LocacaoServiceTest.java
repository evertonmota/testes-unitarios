package br.ce.wcaquino.servicos;


import static br.ce.wcaquino.utils.DataUtils.isMesmaData;
import static matchers.MatchersProprios.caiNumaSegunda;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.rules.ExpectedException;

import br.ce.wcaquino.entidades.Filme;
import br.ce.wcaquino.entidades.Locacao;
import br.ce.wcaquino.entidades.Usuario;
import br.ce.wcaquino.exceptions.FilmeSemEstoqueException;
import br.ce.wcaquino.exceptions.LocadoraException;
import br.ce.wcaquino.utils.DataUtils;
import builders.UsuarioBuilder;
import junit.framework.Assert;
import matchers.MatchersProprios;

public class LocacaoServiceTest {

	@Rule
	public ErrorCollector error = new ErrorCollector();

	@Rule
	public ExpectedException exception = ExpectedException.none(); 
	
	private LocacaoService  service;
	
	@Before
	public void setup() {
		service = new LocacaoService();
	}
	
	@Test
	public void deveAlugarFilme() throws Exception {
		
		Assume.assumeFalse(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenario
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		//acao
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		
		error.checkThat(resultado.getValor(),is(5.0));
		error.checkThat(isMesmaData(resultado.getDataLocacao(), new Date()), is(true));
		error.checkThat(resultado.getDataRetorno(),MatchersProprios.isHoje());

		error.checkThat(isMesmaData(resultado.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		error.checkThat(resultado.getDataRetorno(),MatchersProprios.isHojeComDiferencaDias(1));
		
		//verificação
		/*error.checkThat(locacao.getValor(),is(5.0));
		error.checkThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		error.checkThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		 */
		//verificação
		/*Assert.assertEquals(5.0, locacao.getValor(), 0.01);
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataLocacao(), new Date()));
		Assert.assertTrue(DataUtils.isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)));
		*/
		
		/*assertThat(locacao.getValor(),is(5.0));
		assertThat(isMesmaData(locacao.getDataLocacao(), new Date()), is(true));
		assertThat(isMesmaData(locacao.getDataRetorno(), DataUtils.obterDataComDiferencaDias(1)), is(true));
		*/
	}
	
	
	@Test(expected = FilmeSemEstoqueException.class)
	public void naoDeveAlugarFilmeSemEstoque() throws Exception {
		//cenario
		//LocacaoService servico = new LocacaoService();
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 0, 5.0));

		//acao
		service.alugarFilme(usuario, filmes);
					
	}
	
	//Forma Robusta
	@Test
	public void naoDeveAlugarFilmeSemUsuario() throws FilmeSemEstoqueException {
		//Cenário
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));

		//Ação
			try {
				service.alugarFilme(null, filmes);
				Assert.fail();
			} catch (LocadoraException e) {
				assertThat(e.getMessage(),is("Usuário Vazio."));
			}
	}
	//Forma Nova
	@Test
	public void naoDeveAlugarFilmeSemFilme() throws FilmeSemEstoqueException, LocadoraException {
		//cenario
		//LocacaoService servico = new LocacaoService();
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		
		exception.expect(LocadoraException.class);
		exception.expectMessage("Filme Vazio." );
		
		//acao
		service.alugarFilme(usuario, null);
	}
	
	
	@Test
	public void devePagar75PctNoFilme3() throws FilmeSemEstoqueException, LocadoraException {
		//cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 2, 4.0),new Filme("Filme 2", 2, 4.0),new Filme("Filme 3", 2, 4.0));
		
		//ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificação 
		// valoresDesconto25% -> 4+4+3 = 11
		assertThat(resultado.getValor(),is(11.0));		
	}
	
	
	@Test
	public void devePagar50PctNoFilme4() throws FilmeSemEstoqueException, LocadoraException {
		//cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0),
				new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0),
				new Filme("Filme 4", 2, 4.0));
		//ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificação 
		// valoresDesconto25% -> 4+4+3 = 11 
		// valoresDesconto50% -> 4+4+3+2 = 13 
		assertThat(resultado.getValor(),is(13.0));		
	}
	
	
	@Test
	public void devePagar25PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		//cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0),
				new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0),
				new Filme("Filme 4", 2, 4.0),
				new Filme("Filme 5", 2, 4.0));
		//ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificação 
		// 3 filmes Desconto25% -> 4+4+3 = 11 
		// 4 filmes Desconto50% -> 4+4+3+2 = 13 
		// 5 filmes Desconto25% -> 4+4+3+2+1 = 14 
		
		assertThat(resultado.getValor(),is(14.0));		
	}
	
	@Test
	public void devePagar0PctNoFilme5() throws FilmeSemEstoqueException, LocadoraException {
		//cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(
				new Filme("Filme 1", 2, 4.0),
				new Filme("Filme 2", 2, 4.0),
				new Filme("Filme 3", 2, 4.0),
				new Filme("Filme 4", 2, 4.0),
				new Filme("Filme 5", 2, 4.0),
				new Filme("Filme 6", 2, 4.0));
		//ação
		Locacao resultado = service.alugarFilme(usuario, filmes);
		
		//verificação 
		// 3 filmes Desconto25% -> 4+4+3 = 11 
		// 4 filmes Desconto50% -> 4+4+3+2 = 13 
		// 5 filmes Desconto25% -> 4+4+3+2+1 = 14 
		// 6 filmes Desconto0% -> 4+4+3+2+1 = 14
		assertThat(resultado.getValor(),is(14.0));		
	}
	
	@Test
	public void deveDevolverNaSegundaAoAlugarNoSabado() throws FilmeSemEstoqueException, LocadoraException {

		Assume.assumeTrue(DataUtils.verificarDiaSemana(new Date(), Calendar.SATURDAY));
		
		//cenário
		Usuario usuario = UsuarioBuilder.umUsuario().agora();
		List<Filme> filmes = Arrays.asList(new Filme("Filme 1", 1, 5.0));
		
		//ação
		Locacao retorno = service.alugarFilme(usuario, filmes);
		
		//verificação
		boolean isSegunda = DataUtils.verificarDiaSemana(retorno.getDataRetorno(), Calendar.MONDAY);
		Assert.assertTrue(isSegunda);
		
		//assertThat(retorno.getDataRetorno(), new DiaSemanaMatcher(Calendar.MONDAY));
		//assertThat(retorno.getDataRetorno(), caiEm(Calendar.SUNDAY));
		assertThat(retorno.getDataRetorno(), caiNumaSegunda());

	}
	
	
	/*
	@Test()
	public void testLocacao_FilmeSemEstoque2() {
		//cenario
		LocacaoService servico = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 2, 5.0);
				
		//acao
		try {
			servico.alugarFilme(usuario, filme);
			Assert.fail("Deveria ter lançado uma Exceção...");
		} catch (Exception e) {
			assertThat(e.getMessage(),is("O filme não possui em Estoque!"));
		}
	}
	

	@Test
	public void testLocacao_FilmeSemEstoque3() throws Exception {
		//cenario
		LocacaoService servico = new LocacaoService();
		Usuario usuario = new Usuario("Usuario 1");
		Filme filme = new Filme("Filme 1", 0, 5.0);
				
		//acao
		exception.expect(Exception.class);
		exception.expectMessage(is("O filme não possui em Estoque!"));
		
		servico.alugarFilme(usuario, filme);
	}
	*/
	
}
