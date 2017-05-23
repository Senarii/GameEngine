package game.enemies;

import game.Enemy;

/**
 * game.Enemy base.Unit: game.enemies.Peasant
 * 
 * @author Cody Richter 
 * @version 1.0
 */
public class Peasant extends Enemy
{
    public Peasant()
    {
        super(3, 1, 1, 1,20, null);
        this.setSprite("peasant");
    }

}
