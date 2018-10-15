package churchCommunityBuilderTest;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

//TODO:  The search results are cached, don't re-search for a specified time interval.
public class ChurchCommunityBuilderTestController implements ActionListener, ListSelectionListener
{
	//Query strings
	public static final String API_KEY = "api_key=a8b869d0ef703d3e979ea2797baae5aa";
	public static final String NOW_PLAYING = "https://api.themoviedb.org/3/movie/now_playing?" + API_KEY + "&language=en-US&page=1";
	public static final String POPULAR = "https://api.themoviedb.org/3/movie/popular?" + API_KEY + "&language=en-US&page=1";
	public static final String TOP_RATED = "https://api.themoviedb.org/3/movie/top_rated?" + API_KEY + "&language=en-US&page=1";
	
	//NOTE: movie id MUST go between parts 1 and 2 to make a complete url
	public static final String DETAILS01 =  "https://api.themoviedb.org/3/movie/";
	public static final String DETAILS02 =  "?" + API_KEY + "&language=en-US";
	
	private ChurchCommunityBuilderTestGUI gui;
	private Map<Integer, JSONObject> nowPlaying = new HashMap<Integer, JSONObject>();
	private Map<Integer, JSONObject> popular = new HashMap<Integer, JSONObject>();
	private Map<Integer, JSONObject> topRated = new HashMap<Integer, JSONObject>();

	/**
	 * Create a new app and populate it with the 'Now Playing' data
	 * @param gui
	 */
	public ChurchCommunityBuilderTestController(ChurchCommunityBuilderTestGUI gui)
	{
		this.gui = gui;
		gui.getNowPlaying().addActionListener(this);
		gui.getPopular().addActionListener(this);
		gui.getTopRated().addActionListener(this);
		gui.getjListSource().addListSelectionListener(this);
		
		getNowPlaying();
	}
	
	
	/**
	 * Execute the 'Now Playing' query and forward the information to the gui
	 */
	private void getNowPlaying()
	{
		try
		{
			Document document = Jsoup.connect(NOW_PLAYING).ignoreContentType(true).get();
			JSONArray movies = prepareQueryResults(document);
			processQueryResults(nowPlaying, movies, "Now Playing");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Execute the 'Popular' query and forward the information to the gui
	 */
	private void getPopular()
	{
		try
		{
			Document document = Jsoup.connect(POPULAR).ignoreContentType(true).get();
			JSONArray movies = prepareQueryResults(document);
			processQueryResults(popular, movies, "Popular");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Execute the 'Top Rated' query and forward the information to the gui
	 */
	private void getTopRated()
	{
		try
		{
			Document document = Jsoup.connect(TOP_RATED).ignoreContentType(true).get();
			JSONArray movies = prepareQueryResults(document);
			processQueryResults(topRated, movies, "Top Rated");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Send the query results to the gui.
	 * Add a List listener to the new list.
	 * @param movieMap
	 * @param movies
	 * @param title
	 */
	private void processQueryResults(Map<Integer, JSONObject> movieMap, JSONArray movies, String title)
	{
		try
		{
			movieMap.clear();
	        for (int i = 0; i < movies.length(); ++i) 
	        {
	        	JSONObject movie = movies.getJSONObject(i);
	            int id = movie.getInt("id");
	            movieMap.put(id, movie);  
	        }
	        gui.updateList(movieMap, title);
	        gui.getjListSource().addListSelectionListener(this);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * The query results come back as Json inside of html tag, remove the tags.  Replace 
	 * text with actual characters.
	 * @param document
	 * @return
	 */
	private JSONArray prepareQueryResults(Document document)
	{
		Elements elements = document.getAllElements();
    	try
    	{
		    String out = elements.get(0).toString();
		    StringTokenizer tokenizer = new StringTokenizer(out, "<");
		    String temp = tokenizer.nextToken();
		    for(int i = 0; i < 3; i++)
		    	temp = tokenizer.nextToken();
		    
		    temp = temp.substring(5, (temp.length())).trim();
	        temp = temp.replaceAll("&quot;", "\"");
	    
	        JSONObject results = new JSONObject(temp);
	        JSONArray movies = results.getJSONArray("results");
	        return movies;
    	}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	
	/**
	 * Catch the events originating from the gui buttons.
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(e.getSource() == gui.getTopRated())
			getTopRated();
		
		else if(e.getSource() == gui.getPopular())
			getPopular();
		
		else
			getNowPlaying();
	}


	/**
	 * Catch the events coming from the list. (these are requests for more information)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) 
	{
		try
		{
			//Get the id for the movie object
			Movie movie = (Movie)(((JList)e.getSource()).getSelectedValue());
			//Execute the details query
			String query = DETAILS01 + movie.getId() + DETAILS02;
			Document document = Jsoup.connect(query).ignoreContentType(true).get();
			//Strip off the leading html, and other non friendly encoding
			Elements elements = document.getAllElements();
		    String out = elements.get(0).toString();
		    StringTokenizer tokenizer = new StringTokenizer(out, "<");
		    String temp = tokenizer.nextToken();
		    for(int i = 0; i < 3; i++)
		    	temp = tokenizer.nextToken();
		    
		    temp = temp.substring(5, (temp.length())).trim();
	        temp = temp.replaceAll("&quot;", "\"");
	        //Get the JSON object from the request
	        JSONObject detailedInformation = new JSONObject(temp);
	        movie.setDetailedOverview(detailedInformation);
	        //Send the movie, which now has the expanded information, to the gui.
	        gui.setDetailedInformationPanel(movie);
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
}