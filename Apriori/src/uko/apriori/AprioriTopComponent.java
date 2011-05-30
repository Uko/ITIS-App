/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package uko.apriori;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import org.openide.util.NbBundle;
import org.openide.windows.TopComponent;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//uko.apriori//Apriori//EN",
autostore = false)
@TopComponent.Description(preferredID = "AprioriTopComponent",
//iconBase="SET/PATH/TO/ICON/HERE", 
persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "editor", openAtStartup = true)
@ActionID(category = "Window", id = "uko.apriori.AprioriTopComponent")
@ActionReference(path = "Menu/Window" /*, position = 333 */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_AprioriAction",
preferredID = "AprioriTopComponent")
public final class AprioriTopComponent extends TopComponent
{
	protected ArrayList<ArrayList<HashSet<String>>> L;
	protected LinkedHashMap<Integer, HashSet<String>> T;
	protected double SUPPmin;
	protected LinkedHashMap<HashSet<String>, Double> SUPPs;
	public AprioriTopComponent() throws IOException
	{
		initComponents();
		setName(NbBundle.getMessage(AprioriTopComponent.class, "CTL_AprioriTopComponent"));
		setToolTipText(NbBundle.getMessage(AprioriTopComponent.class, "HINT_AprioriTopComponent"));
		putClientProperty(TopComponent.PROP_CLOSING_DISABLED, Boolean.TRUE);
		putClientProperty(TopComponent.PROP_UNDOCKING_DISABLED, Boolean.TRUE);
		File dir1 = new File ("resources/file.txt");
		System.out.println ("\n\n\n\n\n\n\n\n\n\nCurrent dir : " + dir1.getCanonicalPath());
		SUPPmin = Double.valueOf(SUPPminTextField.getText());
		transactionsTable1.attachToFile("transactions.txt");
		dataInitSet();

	}
protected void dataInitSet()
	{
		SUPPs = new LinkedHashMap<HashSet<String>, Double>();

		
		L = new ArrayList<ArrayList<HashSet<String>>>();
		T = new LinkedHashMap<Integer, HashSet<String>>();

		L.add(new ArrayList<HashSet<String>>());	//k == 0
		HashSet<String> tmpL = new HashSet<String>();
		for (int i = 0, n = transactionsTable1.getRowCount(); i < n; i++)
		{
			Integer k = Integer.valueOf((String) transactionsTable1.getValueAt(i, 0));
			String v = (String) transactionsTable1.getValueAt(i, 1);

			//add first elements sets elements
			tmpL.add(v);

			//gather transactions
			if (!T.containsKey(k))
			{
				T.put(k, new HashSet<String>());
			}
			T.get(k).add(v);
		}
		Object[] tmpStrings = tmpL.toArray();
		for (int i = 0, n = tmpL.size(); i < n; i++)
		{//fill one element sets - L0
			L.get(0).add(new HashSet<String>());
			L.get(0).get(i).add(String.valueOf(tmpStrings[i]));
		}
		jTextField1.setText(L.get(0).get(0).toString());
		for (HashSet s : T.values())
		{
			jTextField1.setText(jTextField1.getText() + "," + s.toString());
		}
	}

	/**
	 * Calculates support of this elements set.
	 * @param l
	 * @return 
	 */
	public double SUPP(HashSet<String> l)
	{
		int amount = 0;
		for (HashSet<String> t : T.values())
		{
			if (t.containsAll(l))
			{
				amount++;
			}
		}
		return amount * 1.0 / T.size();
	}

	/**
	 * Filters specified "level" of L sets. (L with index k)
	 * @param k 
	 */
	public void filterL(int k)
	{
		jTextField1.setText("");
		double sp;
		for (int i = 0, n = L.get(k).size(); i < n; i++)
		{
			if ((sp = SUPP(L.get(k).get(i))) >= SUPPmin)
			{
				SUPPs.put(L.get(k).get(i), sp);
				jTextField1.setText(jTextField1.getText() + L.get(k).get(i).toString() + ": " + sp + ", ");
			}
			else
			{
				L.get(k).remove(i);
				i--;
				n--;
			}
		}
	}

	public void filterL()
	{
		filterL(L.size() - 1);
	}

	public void buildNextL(int k)
	{
		L.add(new ArrayList<HashSet<String>>());
		for (int i = 0, n = L.get(k).size(); i < n; i++)
		//for (Iterator<HashSet<String>> it = L.get(k).iterator(); it.hasNext();)
		{
			//HashSet<String> currSet = it.next();
			for (int j = i + 1; j < n; j++)
			//for (Iterator<HashSet<String>> jt = it; jt.hasNext();)
			{
				//HashSet<String> currjSet = jt.next();
//				L.get(k+1).add(new HashSet<String>(currSet));
//				L.get(k+1).get(L.get(k+1).size()-1).addAll(currjSet);
				L.get(k + 1).add(new HashSet<String>(L.get(k).get(i)));
				L.get(k + 1).get(L.get(k + 1).size() - 1).addAll(L.get(k).get(j));
				if (L.get(k + 1).get(L.get(k + 1).size() - 1).size() > k + 2)
				{
					L.get(k + 1).remove(L.get(k + 1).size() - 1);
				}
			}
		}
		removeDuplicateWithOrder(L.get(k + 1));
		jTextField1.setText(jTextField1.getText() + "; " + L.get(k + 1).toString());
	}

	public void buildNextL()
	{
		buildNextL(L.size() - 1);
	}

	public static void removeDuplicateWithOrder(ArrayList arlList)
	{
		HashSet set = new HashSet();
		ArrayList newList = new ArrayList();
		for (Iterator iter = arlList.iterator(); iter.hasNext();)
		{
			Object element = iter.next();
			if (set.add(element))
			{
				newList.add(element);
			}
		}
		arlList.clear();
		arlList.addAll(newList);
	}
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ClickCanvas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        transactionsTable1 = new uko.apriori.TransactionsTable();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        SUPPminTextField = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        Controls = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        transactionsTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(transactionsTable1);

        jTextField1.setText("jTextField1");

        jLabel1.setText("Minimal support:");

        SUPPminTextField.setText("0.5");
        SUPPminTextField.addCaretListener(new javax.swing.event.CaretListener() {
            public void caretUpdate(javax.swing.event.CaretEvent evt) {
                SUPPminTextFieldCaretUpdate(evt);
            }
        });

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        javax.swing.GroupLayout ClickCanvasLayout = new javax.swing.GroupLayout(ClickCanvas);
        ClickCanvas.setLayout(ClickCanvasLayout);
        ClickCanvasLayout.setHorizontalGroup(
            ClickCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ClickCanvasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ClickCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTextField1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ClickCanvasLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SUPPminTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 370, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, ClickCanvasLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)))
                .addContainerGap())
        );
        ClickCanvasLayout.setVerticalGroup(
            ClickCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ClickCanvasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ClickCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(SUPPminTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(ClickCanvasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jButton1.setText("Step");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Calculate");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout ControlsLayout = new javax.swing.GroupLayout(Controls);
        Controls.setLayout(ControlsLayout);
        ControlsLayout.setHorizontalGroup(
            ControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, ControlsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(ControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE))
                .addContainerGap())
        );
        ControlsLayout.setVerticalGroup(
            ControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ControlsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(282, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ClickCanvas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(Controls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(Controls, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ClickCanvas, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void SUPPminTextFieldCaretUpdate(javax.swing.event.CaretEvent evt)//GEN-FIRST:event_SUPPminTextFieldCaretUpdate
	{//GEN-HEADEREND:event_SUPPminTextFieldCaretUpdate
		double tmpSuppMin = SUPPmin;
		try
		{
			SUPPmin = Double.valueOf(SUPPminTextField.getText());
			if (SUPPminTextField.getBackground().equals(Color.red))
			{
				SUPPminTextField.setBackground(Color.white);
			}
			dataInitSet();
		}
		catch (NumberFormatException e)
		{
			SUPPmin = tmpSuppMin;
			SUPPminTextField.setBackground(Color.red);
		}
	}//GEN-LAST:event_SUPPminTextFieldCaretUpdate

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
	{//GEN-HEADEREND:event_jButton1ActionPerformed
		filterL();
		buildNextL();
	}//GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
	{//GEN-HEADEREND:event_jButton2ActionPerformed
		jTextArea1.append(jLabel1.getText() + SUPPminTextField.getText() + System.getProperty("line.separator"));
		while (!L.get(L.size() - 1).isEmpty())
		{
			filterL();
			buildNextL();
		}
		for (HashSet s : SUPPs.keySet())
		{
			jTextArea1.append(s + ": " + SUPPs.get(s) + System.getProperty("line.separator"));
		}
	}//GEN-LAST:event_jButton2ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ClickCanvas;
    private javax.swing.JPanel Controls;
    private javax.swing.JTextField SUPPminTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private uko.apriori.TransactionsTable transactionsTable1;
    // End of variables declaration//GEN-END:variables
	@Override
	public void componentOpened()
	{
		// TODO add custom code on component opening
	}
	@Override
	public void componentClosed()
	{
		// TODO add custom code on component closing
	}
	void writeProperties(java.util.Properties p)
	{
		// better to version settings since initial version as advocated at
		// http://wiki.apidesign.org/wiki/PropertyFiles
		p.setProperty("version", "1.0");
		// TODO store your settings
	}
	void readProperties(java.util.Properties p)
	{
		String version = p.getProperty("version");
		// TODO read your settings according to their version
	}
}
