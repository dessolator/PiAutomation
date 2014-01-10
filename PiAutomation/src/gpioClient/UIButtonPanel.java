package gpioClient;

import gpioCommon.RelayStatus;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class UIButtonPanel extends JPanel implements ActionListener{
	
	protected JButton flip;//flip button
	protected JButton status;//status button
	protected JButton quit;//quit button
	
	public UIButtonPanel() {
		super();
		/*
		 * initialize the flip button.
		 */
		flip = new JButton("Flip Switch");
        flip.setVerticalTextPosition(AbstractButton.CENTER);
        flip.setHorizontalTextPosition(AbstractButton.LEADING);
        flip.setActionCommand("flip");

        /*
		 * initialize the status button.
		 */
        status = new JButton("Switch Status");
        status.setVerticalTextPosition(AbstractButton.CENTER);
        status.setHorizontalTextPosition(AbstractButton.CENTER);
        status.setActionCommand("query");
        
        /*
		 * initialize the quit button.
		 */
        quit = new JButton("Quit Client");
        status.setVerticalTextPosition(AbstractButton.CENTER);
        status.setHorizontalTextPosition(AbstractButton.TRAILING);
        quit.setActionCommand("quit");


        /*
         * listen for actions on buttons.
         */
        flip.addActionListener(this);
        status.addActionListener(this);
        quit.addActionListener(this);
        
        /*
         * set tooltips for the buttons.
         */
        flip.setToolTipText("Flip the switch.");
        status.setToolTipText("Query switch status.");
        quit.setToolTipText("Quit the Client");

        /*
         * add Components to this container, using the default FlowLayout.
         */
        add(flip);
        add(status);
        add(quit);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try{
			if ("flip".equals(e.getActionCommand())) {
	            Client.flip(1);//if the flip button was pressed flip the switch.
	            return;
	        } 
			if("quit".equals(e.getActionCommand())){
				Client.quit();//if the quit button was pressed quit the client.
				return;
			}
			if("query".equals(e.getActionCommand())) {
	        	if(Client.query(1)==RelayStatus.ON){//if the query button was pressed query the switch.
					System.out.println("it's on");
				}
				else{
					System.out.println("it's off");
				}
	        	return;
	        }
		}
		catch(IOException exc){
			exc.printStackTrace();//flip and query throw IOExceptions.
		}
		
	}

}
