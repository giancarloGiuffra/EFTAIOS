package it.polimi.model.player;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

public class PlayerTest {
    
    private Player player = PlayerFactory.createPlayer();

    @Test
    public void testCopyConstructor() {
        Player copia = PlayerFactory.copyPlayer(player);
        assertThat(copia, is(player));
        assertNotSame(copia, player);
        assertThat(copia.hashCode(), is(player.hashCode()));
    }

}
