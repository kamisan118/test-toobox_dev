package util.tests;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;






import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 
public class ProgressBarTimer extends Application {
 
final Float values = 1.0f;//bar的值
final Label  labels = new Label();
final ProgressBar pbs = new ProgressBar();//bar本身
final ProgressIndicator pins = new ProgressIndicator();//圓餅圖
final HBox hbs = new HBox();

    @Override
    public void start(Stage stage) throws InterruptedException 
    {
        Group root = new Group();
        Scene scene = new Scene(root, 300, 100);
        stage.setScene(scene);
        stage.setTitle("Progress Controls");
        
        Timer ti = new Timer(1000,new TimerListener());
        
        labels.setText("progress:" );
        pbs.setProgress(values);
        pins.setProgress(values);
        hbs.setSpacing(1);
        hbs.setAlignment(Pos.CENTER);
        hbs.getChildren().addAll(labels, pbs, pins);
        
        final VBox vb = new VBox();
        vb.setSpacing(1);
        vb.getChildren().addAll(hbs);
        scene.setRoot(vb);
        stage.show();
        ti.start();
    }
    public static void main(String[] args) 
    {
        launch();
    }
    class TimerListener implements ActionListener
    {
    	double time = 1;
		public void actionPerformed(ActionEvent arg0) 
		{
			if(time > 0.1)
			{
				time = time - 0.1;
				pbs.setProgress(time);//改變bar的值
				pins.setProgress(time);//改變圓餅圖的值
				//labels[0].setText("progress:" + time);
			}
			else
			{
				time = 1.0;
				pbs.setProgress(time);
				pins.setProgress(time);
			}
		}
    	
	}
}