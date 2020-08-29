import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

/**
 * Some code based on: http://www.java-forums.org/new-java/7995-how-plot-graph-java-given-samples.html
 */
 
public class GraphingData extends JPanel 
{
    public double[] ydata;
    public double [] xdata;
    final int PAD = 20;
    
    public GraphingData(double[] xdataIn, double[] ydataIn)
    {
        xdata = xdataIn;
        ydata = ydataIn;
    }
 
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        int w = getWidth();
        int h = getHeight();
        g2.draw(new Line2D.Double(PAD, PAD, PAD, h-PAD));
        g2.draw(new Line2D.Double(PAD, h-PAD, w-PAD, h-PAD));
        double xInc = (double)(w - 2*PAD)/(ydata.length-1);
        double scale = (double)(h - 2*PAD)/getMax();
        g2.setPaint(Color.red);
        for(int i = 0; i < ydata.length; i++) 
        {
            double x = w - PAD - scale*xdata[i];
            double y = h - PAD - scale*ydata[i];
            g2.fill(new Ellipse2D.Double(x-2, y-2, 4, 4));
        }
    }
 
    private double getMax() 
    {
        double max = (double)(-Integer.MAX_VALUE);
        for(int i = 0; i < ydata.length; i++) 
        {
            if(ydata[i] > max)
            {
                max = (double)ydata[i];
            }
        }
        return max;
    }
 
    public static void main(double[] xdata, double[] ydata) 
    {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new GraphingData(xdata, ydata));
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}