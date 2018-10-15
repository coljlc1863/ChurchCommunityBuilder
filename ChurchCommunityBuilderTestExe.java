package churchCommunityBuilderTest;



public class ChurchCommunityBuilderTestExe 
{
	/** 
	 * Launch the program
	 */
	public ChurchCommunityBuilderTestExe()
	{
		ChurchCommunityBuilderTestGUI gui = new ChurchCommunityBuilderTestGUI();
		new ChurchCommunityBuilderTestController(gui);
	}

	public static void main(String[] args)
	{
		new ChurchCommunityBuilderTestExe();
	}
}
