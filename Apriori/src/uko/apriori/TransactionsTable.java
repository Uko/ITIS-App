/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uko.apriori;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

/**
 *
 * @author mcangel
 */
public class TransactionsTable extends JTable
{
	protected String fileName;

	public void attachToFile(String fileName)
	{
		this.fileName = fileName;
		StringBuilder contents = new StringBuilder();
		try
		{
			//use buffering, reading one line at a time
			//FileReader always assumes default encoding is OK!
			BufferedReader input = new BufferedReader(new FileReader(fileName));
			try
			{
				String line = null; //not declared within while loop
				/*
				 * readLine is a bit quirky :
				 * it returns the content of a line MINUS the newline.
				 * it returns null only for the END of the stream.
				 * it returns an empty String if two newlines appear in a row.
				 */
				while ((line = input.readLine()) != null)
				{
					contents.append(line);
					contents.append(System.getProperty("line.separator"));
				}
			}
			finally
			{
				input.close();
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		
		setModel(new NormalTableModel());
		((NormalTableModel)dataModel).addColumn("Transaction");
		((NormalTableModel)dataModel).addColumn("Item");
		String[] lines = contents.toString().split(System.getProperty("line.separator"));
		for (String line : lines)
		{
			((NormalTableModel) this.dataModel).addRow(line.split(" "));
		}

	}
	
	@Override
	protected TableModel createDefaultDataModel()
	{
        return new NormalTableModel();
    }

	/**
	 * Removes all columns from the table and all data from table model.
	 */
	public void removeAllColumnsAndData()
	{
		((NormalTableModel) this.dataModel).setNumRows(0);	//removes all rows
		for(int n = 0, i = this.columnModel.getColumnCount()-1; i >= n; i--)
		{
			removeColumnAndData(i);
		}
	}
	/**
	 * Removes the specified column from the table and the associated 
	 * call data from the table model.
	 */
	public void removeColumnAndData(int vColIndex)
	{
		NormalTableModel model = (NormalTableModel) this.getModel();
		TableColumn col = this.getColumnModel().getColumn(vColIndex);
		int columnModelIndex = col.getModelIndex();
		Vector data = model.getDataVector();
		Vector colIds = model.getColumnIdentifiers();
		// Remove the column from the table
		this.removeColumn(col);

		// Remove the column header from the table model
		colIds.removeElementAt(columnModelIndex);

		// Remove the column data
		for (int r = 0; r < data.size(); r++)
		{
			Vector row = (Vector) data.get(r);
			row.removeElementAt(columnModelIndex);
		}
		model.setDataVector(data, colIds);

		// Correct the model indices in the TableColumn objects
		// by decrementing those indices that follow the deleted column
		Enumeration columnsEn = this.getColumnModel().getColumns();
		for (; columnsEn.hasMoreElements();)
		{
			TableColumn c = (TableColumn) columnsEn.nextElement();
			if (c.getModelIndex() >= columnModelIndex)
			{
				c.setModelIndex(c.getModelIndex() - 1);
			}
		}
		model.fireTableStructureChanged();
	}
}
