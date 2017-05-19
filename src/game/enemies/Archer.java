package game.enemies;

import game.Enemy;

/**
 * game.Enemy base.Unit: game.enemies.Peasant
 * 
 * @author Cody Richter 
 * @version 1.0
 */
public class Archer extends Enemy
{
    public Archer()
    {
        super(5, 1, 4, 1,150, null);
        this.setSprite("archer");
    }

}
