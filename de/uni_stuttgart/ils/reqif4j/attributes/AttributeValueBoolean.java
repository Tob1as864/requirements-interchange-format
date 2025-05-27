package de.uni_stuttgart.ils.reqif4j.attributes;

public class AttributeValueBoolean extends AttributeValue {
	
	
	public AttributeValueBoolean(String value, AttributeDefinition type) {
		super(value, type);
		
		if(value != null && value.toLowerCase().equals("true")) {
			this.value = true;
		}else{
			this.value = false;
		}
	}
	
	@Override
	public Object getValue() {
		return (boolean)this.value;
	}
	
}
