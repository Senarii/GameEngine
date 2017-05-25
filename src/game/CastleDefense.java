package game;

import game.enemies.Archer;
import game.enemies.Barbarian;
import game.enemies.Cavalry;
import game.enemies.Peasant;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Frank Williams on 5/8/2017.
 */
public class CastleDefense {

    //X-Values Of Board JPanel That Rows Are At And That Units Are Spawned At
    public static final int ROW1X = 100;
    public static final int ROW2X = 300;
    public static final int ROW3X = 500;

    //Current Wave Player Is On
    private static int wave = 0;
    private static int enemyAmount = 0;
    public static boolean gameOver = false;
    public static boolean nextWave = false;
    public static boolean waitingForWave = false;
    public static boolean addMoney = true;

    //Lists Holding All Units Spawned In On Board
    public static List<Enemy> enemies = new CopyOnWriteArrayList<Enemy>();
    public static List<Friendly> friendlies = new CopyOnWriteArrayList<Friendly>();

    //Money Player Has In Game - Set This Value To Be The Amount of Starting Money Player Has
    private static int balance = 100;

    //Amount of Lives Player Has: Player Will Lose a Life If a Unit Gets To The Other Side Of The Board
    private static int lives = 3;

    private static Timer t = new Timer();

    private static ArrayList<Class> availableEnemies = new ArrayList<>();

    public static void main(int control) {
        if (control == 0 && nextWave) {
            nextWave = false;
            waitingForWave = true;
            if (Main.VERBOSE) System.out.println("WAITING FOR WAVE");
            nextWave();
            return;
        } else if (control > 0) {
            waitingForWave = true;
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    int icontrol = control;
                    System.out.println("Enemy Spawn Started");
                    int enemyType = (int) (Math.random() * availableEnemies.size());
                    try {
                        if (Main.VERBOSE)
                            System.out.println("SPAWNING ENEMY OF TYPE " + availableEnemies.get(enemyType).getName());
                        Object e = availableEnemies.get(enemyType).newInstance();
                        int row = (int) (Math.random() * 3) + 1;
                        if (e instanceof Enemy) {
                            ((Enemy) e).spawn(row);
                            System.out.println(e.toString() + " Spawned in row " + row);
                        }
                        icontrol--;
                        waitingForWave = false;
                    } catch (InstantiationException ie) {
                        System.out.println("InstantiationException when spawning enemy: " + ie.getMessage());
                    } catch (IllegalAccessException iae) {
                        System.out.println("IllegalAccessException when spawning enemy: " + iae.getMessage());
                    }
                    main(icontrol);
                    System.out.println();
                }
            }, 5000);

        }
    }

    public static void nextWave(){
        if (waitingForWave) return;
        wave++;
        addMoney(50 * wave);
        if(Main.VERBOSE) System.out.println("WAVE SETUP");
        if (wave >= 0 && !availableEnemies.contains(Peasant.class)){
            if(Main.VERBOSE) System.out.println("PEASANT AVAILABLE");
            availableEnemies.add(Peasant.class);
        }
        if (wave >= 3 && !availableEnemies.contains(Archer.class)) {
            if(Main.VERBOSE) System.out.println("ARCHER AVAILABLE");
            availableEnemies.add(Archer.class);
        }
        if (wave >= 5 && !availableEnemies.contains(Barbarian.class)) {
            if(Main.VERBOSE) System.out.println("BARBARIAN AVAILABLE");
            availableEnemies.add(Barbarian.class);
        }
        if (wave >= 7 && !availableEnemies.contains(Cavalry.class)){
            if(Main.VERBOSE) System.out.println("CAVALRY AVAILABLE");
            availableEnemies.add(Cavalry.class);
        }

        enemyAmount = 3 * wave;
//        for(Friendly f : friendlies){
//            f.kill();
//            addMoney(50);
//        }
        System.out.println("Wave " + wave + " Started \n");
        main(enemyAmount);
        Main.menu.repaint();

    }

    public static void testEnemySpawn(){

    }

    public static void addEnemy(Enemy e){
        enemies.add(e);
    }

    public static void addFriendly(Friendly f){friendlies.add(f);}

    public static void doAction(Friendly u)
    {
        //Basic Conditions That Need To Be Met: Unit Has Health Remaining And Isn't At Edge Of Board
        if (u.getX() < Main.b.getWidth()-50)
        {
            nextWave = false;
            //Will Exit Method If Unit Is Currently Completing an Action
            if (u.isInAction()) return;

            u.currentlyAttacking = false;
            for (int i = (int)u.getX()+50; i < (50+(int)u.getX())+(u.getAttackRange())*20; i++)
            {

                for(Enemy e : enemies) {
                    if (!e.isDead() && e.getX() == i && e.getY() == u.getY() && !e.isProjectile()) {
                        u.attack(e);
                        if(Main.VERBOSE) System.out.println(u + " ATTACKING " + e);
                        u.currentlyAttacking = true;
                    } else {

                    }


                }
            }


            if (!u.currentlyAttacking) {
                u.move();
               // if(Main.VERBOSE) System.out.println(u + " moving");
            }

        }
        else if (u.getX() >= Main.b.getWidth()-51)
        {
            double multiplier = u.getCurrentHealth()/u.getMaxHealth();
            addMoney((int)(u.getUnitCost(u)*multiplier));
            if(Main.VERBOSE) System.out.println(u + "HAS REACHED END OF BOARD. KILLING...");
            u.kill();
        }
        nextWave = true;
        for (Friendly f : friendlies){
            if (!f.isDead()){
                nextWave = false;
            }
        }
        for (Enemy e : enemies){
            if (!e.isDead()){
                nextWave = false;
            }
        }
    }

    public static void doAction(Enemy u)
    {
        //Basic Conditions That Need To Be Met
        if (u.getX() > 0)
        {
            nextWave = false;
            //Will Exit Method If Unit Is Currently Completing an Action
            if (u.isInAction()) return;

            u.currentlyAttacking = false;
            for (int i = (int)u.getX() - ((int)u.getAttackRange())*20; i < (int)u.getX(); i++)
            {

                for(Friendly f : friendlies) {
                    if (!f.isDead() && f.getX() + 50 == i && u.getY() == f.getY()  && !f.isProjectile()) {
                        u.attack(f);
                        u.currentlyAttacking = true;
                        if(Main.VERBOSE) System.out.println(u + " ATTACKING " + f);
                    } else {

                    }
                }
            }

            if (!u.currentlyAttacking) {
                u.move();
                //if(Main.VERBOSE) System.out.println(u + " moving");
            }
        }
        else if (u.getX() <= 0 && !u.isDead())
        {
            if(Main.VERBOSE) System.out.println(u + "HAS REACHED END OF BOARD. GAME OVER.");
            endGame();
            u.kill();

        }
    }

    public static void endGame()
    {
        lives--;
        if (lives <= 0)
        gameOver = true;
        Main.menu.repaint();
    }

    //
    //
    // Economy
    //
    //

    /**
     * Takes Money From Player's Balance
     */
    public static void subtractMoney(int amount)
    {
        if (amount < 0) {}
        if (amount > balance) balance = 0;
        else balance -= amount;
        Main.b.repaint();
    }

    /**
     * Adds Money To Player's Balance
     */
    public static void addMoney(int amount)
    {
        if (amount < 0) {}
        else balance += amount;
        Main.b.repaint();
    }

    public static int getBalance()
    {
        return balance;
    }

    public static int getWave() {return wave;}

    public static int getLives() {return lives;}
}
