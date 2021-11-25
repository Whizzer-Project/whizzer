package ro.allevo.fintpui.model;

public class FinTPLists {
	private String name;
	private String apiServiceName;
	private Class<?> className;
	
	public FinTPLists(String name, String apiServiceName, Class<?> className) {
		super();
		this.name = name;
		this.apiServiceName = apiServiceName;
		this.className = className;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getApiServiceName() {
		return apiServiceName;
	}
	public void setApiServiceName(String apiServiceName) {
		this.apiServiceName = apiServiceName;
	}
	public Class<?> getClassName() {
		return className;
	}
	public void setClassName(Class<?> className) {
		this.className = className;
	}
	
}
