package churchCommunityBuilderTest;

import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONObject;

public class Movie implements Comparable
{
	private String title, overview, releaseDate, detailedOverview, sortTitle;
	private int id;
	/**
	 * Create a basic movie object from a JSON object
	 * @param json
	 */
	public Movie(JSONObject json)
	{
		try
		{
			this.title = json.getString("title");
			this.overview = json.getString("overview");
			this.releaseDate = json.getString("release_date");
			this.id = json.getInt("id");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Return a basic string description for painting purposes
	 */
	public String toString()
	{
		String out = this.title + "          " + this.releaseDate + "          " + this.overview;
		int min = Math.min(out.length(), 250);
		return out.substring(0, min);
	}
	
	
	public String getTitle() {
		return title;
	}
	
	
	public String getOverview() {
		return overview;
	}
	
	
	public String getReleaseDate() {
		return releaseDate;
	}
	
	
	public String getDetailedOverview() {
		return detailedOverview;
	}
	
	
	public int getId() {
		return id;
	}
	
	
	/**
	 * Get the detailed information about this movie.  This information comes from a second
	 * query.
	 * @param additionalInfo
	 */
	public void setDetailedOverview(JSONObject additionalInfo) 
	{
		try
		{
			this.detailedOverview = "Adult film: " + additionalInfo.getString("adult") + "\n";
			//The belongs_to_collection can either be a string 'null', or a JSONObject.  Determine
			//which we are dealing with and then move forward.
			if(!additionalInfo.getString("belongs_to_collection").equals("null"))
			{
				JSONObject collection = additionalInfo.getJSONObject("belongs_to_collection");
				this.detailedOverview += "Series: " + collection.getString("name") + "\n";
			}
			this.detailedOverview += "Budget: " + additionalInfo.getInt("budget") + "\n";
			if(additionalInfo.getJSONArray("genres") != null)
			{
				String out = "Genre(s): ";
	        	JSONArray genres = additionalInfo.getJSONArray("genres");
		        for (int i = 0; i < genres.length(); ++i) 
		        {
		        	JSONObject genre = genres.getJSONObject(i);
		        	out += genre.getString("name");
		        	if(i < genres.length() - 1)
		        		out += ", ";
		        }
		        out += "\n";
		        this.detailedOverview += out;
			}
			this.detailedOverview += "Home page: " + additionalInfo.getString("homepage") + "\n\n";
			//Add extra space to highlight the overview
			this.detailedOverview += "Overview: " + additionalInfo.getString("overview") + "\n\n";
			this.detailedOverview += "Popularity: " + additionalInfo.getDouble("popularity") + "\n";
			if(additionalInfo.getJSONArray("production_companies") != null)
			{
				String out = "Production Company(ies): ";
	        	JSONArray prodCompanies = additionalInfo.getJSONArray("production_companies");
		        for (int i = 0; i < prodCompanies.length(); ++i) 
		        {
		        	JSONObject prodComp = prodCompanies.getJSONObject(i);
		        	out += prodComp.getString("name");
		        	if(i < prodCompanies.length() - 1)
		        		out += ", ";
		        }
		        out += "\n";
		        this.detailedOverview += out;
			}
			if(additionalInfo.getJSONArray("production_countries") != null)
			{
				String out = "Production Country(s): ";
	        	JSONArray prodCountries = additionalInfo.getJSONArray("production_countries");
		        for (int i = 0; i < prodCountries.length(); ++i) 
		        {
		        	JSONObject prodCountry = prodCountries.getJSONObject(i);
		        	out += prodCountry.getString("name");;
		        	if(i < prodCountries.length() - 1)
		        		out += ", ";
		        }
		        out += "\n";
		        this.detailedOverview += out;
			}
			this.detailedOverview += "Release Date: " + additionalInfo.getString("release_date") + "\n";
			this.detailedOverview += "Revenue: " + additionalInfo.getInt("revenue") + "\n";
			this.detailedOverview += "Runtime: " + additionalInfo.getInt("runtime") + "\n";
			//Add extra space to highlight the tagline
			this.detailedOverview += "Status: " + additionalInfo.getString("status") + "\n\n";
			this.detailedOverview += "Tag Line: " + additionalInfo.getString("tagline") + "\n\n";
			this.detailedOverview += "Video: " + additionalInfo.getString("video") + "\n";
			this.detailedOverview += "Vote Average: " + additionalInfo.getString("vote_average") + "\n";
			this.detailedOverview += "Vote Count: " + additionalInfo.getString("vote_count") + "\n";
		}
		catch(Exception exception)
		{
			exception.printStackTrace();
		}
	}
	
	/**
	 * In order to properly sort the movies 'A" and 'The' have to be moved to the end of the title.
	 * Create a temp title to use for sorting. 
	 * @param movie
	 */
	private void setSortTitle(Movie movie)
	{
		movie.sortTitle = movie.title;
		if(movie.title.startsWith("A ") || movie.title.startsWith("The"))
		{
			StringTokenizer tokenizer = new StringTokenizer(movie.title);
			String firstToken = tokenizer.nextToken();
			String tempTitle = (movie.title.substring(firstToken.length(), movie.title.length())).trim();
			movie.sortTitle = tempTitle + " " + firstToken;
		}
	}


	/**
	 * The compareTo method is used for sorting.
	 */
	@Override
	public int compareTo(Object o) 
	{
		if(!(o instanceof Movie))
			return 1;
		setSortTitle(this);
		setSortTitle((Movie)o);
		return this.sortTitle.compareTo(((Movie)o).sortTitle);
	}
}
