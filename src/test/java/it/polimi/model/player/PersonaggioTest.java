package it.polimi.model.player;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class PersonaggioTest {

    private static Personaggio p;
    
    @BeforeClass
    public static void initPersonaggio(){
        p = Personaggio.CAPITANO;
    }
    
    @Test
    public void testRazza() {
        Razza razza = p.razza();
        assertEquals(razza,Razza.HUMAN);
    }

    @Test
    public void testNome() {
        String nome = p.nome();
        assertEquals(nome,"Ennio Maria Dominoni");
    }

    @Test
    public void testIsHuman() {
        boolean q = p.isHuman();
        assertTrue(q);
    }

    @Test
    public void testIsAlien() {
        boolean q = p.isAlien();
        assertFalse(q);
    }
    
    @Test
    public void testToString(){
        assertEquals(p.toString(),"CAPITANO");
    }
    

}
