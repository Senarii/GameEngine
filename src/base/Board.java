package base;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Game base.Board That Is Displayed In Background
 * 
 * @author Cody Richter & Frank Williams
 * @version 1.0
 */
public class Board extends JPanel
{
    private List<Unit> unitList = new ArrayList<Unit>();

    Image background;
    public Board()
    {
        background = Toolkit.getDefaultToolkit().createImage("src/background.jpeg");
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        /* Implementation Not Shown */
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        this.setBackground(Color.decode("#42f448"));
        //g2d.drawImage(background, 0, 0, null);
        repaint();
    }

    /**
     * Adds Unit To Board
     * @param u
     */
    public void addUnit(Unit u)
    {
        unitList.add(u);
    }

    /**
     * Removes Unit From Board
     * @param u
     */
    public void removeUnit(Unit u)
    {
        unitList.remove(u);
    }


}
