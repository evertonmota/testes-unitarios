package br.ce.wcaquino.servicos;

import org.junit.Assert;
import org.junit.Test;

import br.ce.wcaquino.entidades.Usuario;

public class AssertTest {

	@Test
	public void teste() {
		
		Assert.assertTrue(true);
		Assert.assertFalse(false);
		
		//short long int e booelan
		Assert.assertEquals(1, 1);
		
		//comportamento diferente para Float e Double por conta das casas decimais.
		Assert.assertEquals(0.51, 0.51, 0.01); // margem de erro por exemplo.
		Assert.assertEquals(0.51234, 0.512, 0.001);
		//Assert.assertEquals(0.51234, 0.512, 0.0001);
		Assert.assertEquals(Math.PI, 3.14, 0.01);
		
		int i =5;
		Integer i2 = 5;
		//Assert.assertEquals(i,i2); //erro de Parse tipo primitivo x Object
		Assert.assertEquals(Integer.valueOf(i),  i2);
		Assert.assertEquals(i, i2.intValue());
		
		Assert.assertEquals("Erro de comunicação",1,1);

		Assert.assertEquals("Bola","Bola");
		Assert.assertNotEquals("Bola","teste");
		
		Usuario user1 = new Usuario("Usuario 1");
		Usuario user2 = new Usuario("Usuario 1");
		Usuario u3 = user2;
		Usuario u4 = null;
		
		Assert.assertEquals(user1, user2);
		Assert.assertEquals(user1, user1);
		
		Assert.assertEquals(user2, u3);
		Assert.assertSame(user2, u3);

		Assert.assertTrue(u4 == null);
	}
}
