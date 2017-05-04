
/**
 * Methods For Enemy Units
 * 
 * @author Cody Richter
 * @version 1.0
 */
public abstract class Enemy extends Unit
{
    /**
     * Makes new enemy unit
     */
    public Enemy()
    {
        super();
    }

    /**
     * Makes new enemy unit with given attributes
     */
    public Enemy(int healthLevel, int damageLevel, int range, int speed)
    {
        super(healthLevel, damageLevel, range, speed);
    }

}