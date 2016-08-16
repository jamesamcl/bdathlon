package SBOLtoBioPAX.converter;

import org.sbolstandard.core2.RestrictionType;

public class SequenceConstraint {
	private String display_id;
	private RestrictionType restriction_type;
	private String subject;
	private String object;
	
	public SequenceConstraint(String display_id, RestrictionType restriction_type, String subject, String object){
		setDisplayId(display_id);
		setRestrictionType(restriction_type);
		setSubject(subject);
		setObject(object);
	}

	public String getDisplayId() {
		return display_id;
	}

	public void setDisplayId(String display_id) {
		this.display_id = display_id;
	}

	public RestrictionType getRestrictionType() {
		return restriction_type;
	}

	public void setRestrictionType(RestrictionType restriction_type) {
		this.restriction_type = restriction_type;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}
}