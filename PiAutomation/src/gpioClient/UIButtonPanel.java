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

	protected JButton flip;
	protected JButton status;
	protected JButton quit;
	public UIButtonPanel() {
		super();
		flip = new JButton("Flip Switch");
        flip.setVerticalTextPosition(AbstractButton.CENTER);
        flip.setHorizontalTextPosition(AbstractButton.LEADING); //aka LEFT, for left-to-right locales
//        flip.setMnemonic(KeyEvent.VK_D);
        flip.setActionCommand("flip");

        status = new JButton("Query Switch Status");
        status.setVerticalTextPosition(AbstractButton.BOTTOM);
        status.setHorizontalTextPosition(AbstractButton.CENTER);
//        status.setMnemonic(KeyEvent.VK_M);
        status.setActionCommand("query");
        
        quit = new JButton("Quit Client");
        //Use the default text position of CENTER, TRAILING (RIGHT).
//        quit.setMnemonic(KeyEvent.VK_E);
        quit.setActionCommand("quit");


        //Listen for actions on buttons 1 and 3.
        flip.addActionListener(this);
        status.addActionListener(this);
        quit.addActionListener(this);

        flip.setToolTipText("Flip the switch.");
        status.setToolTipText("Query switch status.");
        quit.setToolTipText("Quit the Client");

        //Add Components to this container, using the default FlowLayout.
        add(flip);
        add(status);
        add(quit);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		try{
			if ("flip".equals(e.getActionCommand())) {
	            Client.flip(1);
	            return;
	        } 
			if("quit".equals(e.getActionCommand())){
				Client.quit();
				return;
			}
			if("query".equals(e.getActionCommand())) {
	        	if(Client.query(1)==RelayStatus.ON){
					System.out.println("it's on");
				}
				else{
					System.out.println("it's off");
				}
	        }
		}
		catch(IOException exc){
			exc.printStackTrace();
		}
		
	}

}
