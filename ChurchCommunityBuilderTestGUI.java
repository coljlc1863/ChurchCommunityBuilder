package churchCommunityBuilderTest;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.TitledBorder;

import org.json.JSONObject;


public class ChurchCommunityBuilderTestGUI extends JFrame 
{
	private String title = "Welcome to the Ultimate Movie Information Source";
	private JPanel mainPanel;
	private GridBagLayout layout;
	private GridBagConstraints constraints, noGrowthConstraints, largeGrowthconstraints;
	private JScrollPane detailedInformationPanel = new JScrollPane();
	private JPanel buttonPanel, moviePanel;
	private JButton nowPlaying, popular, topRated;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JTextArea detailInfo;
	private JList jListSource = new JList();
	
	public ChurchCommunityBuilderTestGUI()
	{
		this.setTitle("Church Community Builder - Movies App");
		//Create a frame that lists all the movies
        constraints = new GridBagConstraints();
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;
        
        noGrowthConstraints = new GridBagConstraints();
        noGrowthConstraints.weightx = 1.0;
        noGrowthConstraints.weighty = 0.0;
        noGrowthConstraints.fill = GridBagConstraints.BOTH;
        
        largeGrowthconstraints = new GridBagConstraints();
        largeGrowthconstraints.weightx = 5.0;
        largeGrowthconstraints.weighty = 5.0;
        largeGrowthconstraints.fill = GridBagConstraints.BOTH;
        
        layout = new GridBagLayout();
        
        //Overall panel
    	mainPanel = new JPanel();
    	mainPanel.setLayout(layout);
	    TitledBorder border = BorderFactory.createTitledBorder((BorderFactory.createLineBorder(Color.BLUE, 2)), title);
	    mainPanel.setBorder(border);
	    
	    //Movie Panel
	    createMovieListPanel();
	    GUIHelper.addComponent(moviePanel, 0, 0, 1, 1, layout, mainPanel, largeGrowthconstraints);
	    
	    //Detailed information panel
	    createDetailedPanel();
	    GUIHelper.addComponent(detailedInformationPanel, 1, 0, 1, 1, layout, mainPanel, largeGrowthconstraints);
	    
	    //ButtonsPanel
	    createButtonPanel();
	    GUIHelper.addComponent(buttonPanel, 2, 0, 1, 1, layout, mainPanel, noGrowthConstraints);
	    
        screenSize.height -= 150;
        screenSize.width -= 250;
        this.setSize(screenSize);
        this.add(this.mainPanel, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updateDisplay();
	}
	
	
	/**
	 * Force a repaint
	 */
    public void updateDisplay()
    {
    	this.revalidate();
        this.repaint();
        this.setVisible(true);
    }
	
    
	/**
	 * Make sure that the movie list panel is instantiated
	 */
	private void createMovieListPanel()
	{
	    moviePanel = new JPanel();
	}
	
	
	/**
	 * Create the detailed information panel
	 */
	private void createDetailedPanel()
	{
        detailInfo = new JTextArea(20, 5);
        detailInfo.setText("Click on a movie to get more detailed information about it.");
        detailInfo.setEditable(false);
        detailInfo.setLineWrap(true);
		detailedInformationPanel = new JScrollPane(detailInfo, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,  
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	}
	
	
	/** 
	 * Create the buttons panel.
	 */
	private void createButtonPanel()
	{
		buttonPanel = new JPanel();
		nowPlaying = new JButton("Now Playing");
        GUIHelper.addComponent(nowPlaying, 0, 0, 1, 1, layout, buttonPanel, constraints);
		popular = new JButton("Popular");
        GUIHelper.addComponent(popular, 0, 1, 1, 1, layout, buttonPanel, constraints);
		topRated = new JButton("Top Rated");
        GUIHelper.addComponent(topRated, 0, 2, 1, 1, layout, buttonPanel, constraints);
	}
	
	
	/**
	 * Update the list of movies.  This will mainly be called when the user has pressed
	 * a button and therefore selected a new lit of movies to peruse.
	 * @param list - The map of movies associated with the button just pressed.
	 * @param listOrgin - A keyword to update the title, and remind the user what he is looking at.
	 */
	public void updateList(Map<Integer, JSONObject> list, String listOrgin)
	{
		//Reset the title on the border
		this.title = listOrgin;
	    TitledBorder border = BorderFactory.createTitledBorder((BorderFactory.createLineBorder(Color.BLUE, 2)), listOrgin);
	    mainPanel.setBorder(border);
	    moviePanel.removeAll();
		try
		{
			//Build a list of movies from the hashmap
			Vector<Movie> listOfMovies = new Vector<Movie>();
			Set<Integer> ids = list.keySet();
			Iterator<Integer> iter = ids.iterator();
			while(iter.hasNext())
			{
				Movie movie = new Movie(list.get(iter.next()));
				listOfMovies.add(movie);
			}
			//Sort the list
			Collections.sort(listOfMovies);
			
			//Create the movie list
		    jListSource = new JList(listOfMovies);
		    
		    //Create the pane for the movies
		    JScrollPane movies = new JScrollPane(jListSource, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, 
		    		ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	        int height = (int)(screenSize.height * .40);
	        int width = (int)(screenSize.width * .97);
	        Dimension size = new Dimension(width, height);
	        movies.setPreferredSize(size);
	        
	        //Add the list panel to the gui - force a repaint
		    GUIHelper.addComponent(movies, 0, 0, 1, 1, layout, moviePanel, largeGrowthconstraints);
		    updateDisplay();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Get the detailed information and pass it to the gui
	 * @param movie
	 */
	public void setDetailedInformationPanel(Movie movie)
	{
		detailInfo.setText(movie.getDetailedOverview());
		updateDisplay();
	}


	public JButton getNowPlaying() {
		return nowPlaying;
	}


	public JButton getPopular() {
		return popular;
	}


	public JButton getTopRated() {
		return topRated;
	}


	public JList getjListSource() {
		return jListSource;
	}
}