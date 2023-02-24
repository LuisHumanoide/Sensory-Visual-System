/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generator;

/**
 *
 * @author HumanoideFilms
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @see http://stackoverflow.com/a/12389479/909085
 */

public class ComponentLinkerTest extends JComponent
{
    private Map<JComponent, JComponent> linked;

    public ComponentLinkerTest ()
    {
        super ();
        linked = new HashMap<JComponent, JComponent> ();
    }

    public void link ( JComponent c1, JComponent c2 )
    {
        linked.put ( c1, c2 );
        repaint ();
    }

    protected void paintComponent ( Graphics g )
    {
        Graphics2D g2d = ( Graphics2D ) g;
        g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON );

        g2d.setPaint ( Color.BLACK );
        for ( JComponent c1 : linked.keySet () )
        {
            Point p1 = getRectCenter ( getBoundsInWindow ( c1 ) );
            Point p2 = getRectCenter ( getBoundsInWindow ( linked.get ( c1 ) ) );
            g2d.drawLine ( p1.x, p1.y, p2.x, p2.y );
        }
    }

    private Point getRectCenter ( Rectangle rect )
    {
        return new Point ( rect.x + rect.width / 2, rect.y + rect.height / 2 );
    }

    private Rectangle getBoundsInWindow ( Component component )
    {
        return getRelativeBounds ( component, getRootPaneAncestor ( component ) );
    }

    private Rectangle getRelativeBounds ( Component component, Component relativeTo )
    {
        return new Rectangle ( getRelativeLocation ( component, relativeTo ),
                component.getSize () );
    }

    private Point getRelativeLocation ( Component component, Component relativeTo )
    {
        Point los = component.getLocationOnScreen ();
        Point rt = relativeTo.getLocationOnScreen ();
        return new Point ( los.x - rt.x, los.y - rt.y );
    }

    private JRootPane getRootPaneAncestor ( Component c )
    {
        for ( Container p = c.getParent (); p != null; p = p.getParent () )
        {
            if ( p instanceof JRootPane )
            {
                return ( JRootPane ) p;
            }
        }
        return null;
    }

    public boolean contains ( int x, int y )
    {
        return false;
    }

    private static ComponentLinkerTest linker;

    public static void main ( String[] args )
    {
        setupLookAndFeel ();

        JFrame frame = new JFrame ();

        linker = new ComponentLinkerTest ();
        frame.setGlassPane ( linker );
        linker.setVisible ( true );

        JPanel content = new JPanel ();
        content.setLayout ( new GridLayout ( 10, 5, 5, 5 ) );
        content.setBorder ( BorderFactory.createEmptyBorder ( 5, 5, 5, 5 ) );
        frame.add ( content );

        for ( int i = 0; i < 50; i++ )
        {
            final JButton button = new JButton ( "Button" + i );
            button.addActionListener ( new ActionListener ()
            {
                public void actionPerformed ( ActionEvent e )
                {
                    link ( button );
                }
            } );
            content.add ( button );
        }

        frame.setDefaultCloseOperation ( JFrame.EXIT_ON_CLOSE );
        frame.pack ();
        frame.setLocationRelativeTo ( null );
        frame.setVisible ( true );
    }

    private static JButton last = null;

    private static void link ( JButton button )
    {
        if ( last == null )
        {
            last = button;
        }
        else
        {
            linker.link ( last, button );
            last = null;
        }
    }

    private static void setupLookAndFeel ()
    {
        try
        {
            UIManager.setLookAndFeel ( UIManager.getSystemLookAndFeelClassName () );
        }
        catch ( ClassNotFoundException e )
        {
            e.printStackTrace ();
        }
        catch ( InstantiationException e )
        {
            e.printStackTrace ();
        }
        catch ( IllegalAccessException e )
        {
            e.printStackTrace ();
        }
        catch ( UnsupportedLookAndFeelException e )
        {
            e.printStackTrace ();
        }
    }
}