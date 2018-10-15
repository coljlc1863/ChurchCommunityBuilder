package churchCommunityBuilderTest;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

public class GUIHelper
{
	/**
	 * This method is supposed to help add components to a gridBagLayout.
	 * Sometimes I wonder if anything helps with a GridBagLayout.
	 *
	 * @param  component The piece being added to the target panel.
	 * @param  row The row number -- upper left hand corner of the component.
	 * @param  column The column number -- upper left hand corner of the component.
	 * @param  width The width in columns of the component.
	 * @param  height The height in rows of the component.
	 * @param  layout The layout manager of the target panel.
	 * @param  panel The target panel that the component is being added to
	 * @param  constraints The gridBagConstraints to use
	 */
	public static void addComponent(Component component, int row, int column, int width, int height,
	GridBagLayout layout, JPanel panel, GridBagConstraints constraints)
	{
		constraints.gridx = column;
		constraints.gridy = row;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		layout.setConstraints(component, constraints);
		panel.add(component);
	}
	
	public static void addComponent(Component component, int row, int column, int width, int height,
			GridBagLayout layout, JPanel panel, GridBagConstraints constraints, boolean end)
			{
				constraints.gridx = column;
				constraints.gridy = row;
				constraints.gridwidth = width;
				constraints.gridheight = height;
				
				if(end)
                    constraints.gridwidth = GridBagConstraints.REMAINDER;
                
                layout.setConstraints(component, constraints);
                panel.add(component);
			}
}