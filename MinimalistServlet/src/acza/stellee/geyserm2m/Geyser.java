//02 Apr 2015

package acza.stellee.geyserm2m;

public class Geyser {

	
	private final String geyserID;
	private boolean element;
	private float temperature;
	private float lowSetpoint;
	private float highSetpoint;
	
	
	public Geyser(String id){
		this.geyserID = id;
		this.element = false;
		this.temperature = 50;
		this.lowSetpoint = 40;
		this.highSetpoint = 60;
	}
	
	public void simulate_step(){
		if(this.temperature <= this.lowSetpoint){
			this.element = true;
		}
		else if(this.temperature >= this.highSetpoint){
			this.element = false;
		}
		
		if(this.element == true){	//Element ON
			this.temperature += 0.5;
		}
		else{						//Element OFF
			this.temperature -= (this.temperature - 25)/50;
		}
	}
	
	
	/*
	 * Getters and Setters
	 */
	
	public String getID(){
		return geyserID;
	}
	
	public boolean getElementState(){
		return this.element;
	}
	
	public float getTemperature(){
		return this.temperature;
	}
	
	public float getLowSetpoint(){
		return this.lowSetpoint;
	}
	
	public void setLowSetpoint(float temperature){
		this.lowSetpoint = temperature;
	}
	
	public float getHighSetpoint(){
		return this.highSetpoint;
	}
	
	public void setHighSetpoint(float temperature){
		this.highSetpoint = temperature;
	}

	@Override
	public String toString() {
		return "Geyser [geyserID=" + geyserID + ", element=" + element
				+ ", temperature=" + temperature + ", lowSetpoint="
				+ lowSetpoint + ", highSetpoint=" + highSetpoint + "]";
	}
	
	public String toCSV(){
		return "" + element + ", " + temperature + ", " + lowSetpoint  + ", " + highSetpoint;
	}
	
	
}

