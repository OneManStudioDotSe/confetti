package com.github.jinatonic.confetti;

import com.github.jinatonic.confetti.confetto.Confetto;

import java.util.Random;

public interface ConfettoGenerator {
    /**
     * Generate a random confetto to animate.
     *
     * @param random a {@link Random} that can be used to generate random confetto.
     * @return the randomly generated confetto.
     */
    Confetto generateConfetto(Random random);
}
