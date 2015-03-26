package phone.wobo.music.model;

import java.util.List;

public class MV {
	private int labelid;
	private String label;
	private int currentpage;
	private int total;
	private String picshape;
	private boolean nextpage;
	private List<MVPlayInfo> list;

	public boolean isNextpage() {
		return nextpage;
	}

	public void setNextpage(boolean nextpage) {
		this.nextpage = nextpage;
	}

	public int getLabelid() {
		return labelid;
	}

	public void setLabelid(int labelid) {
		this.labelid = labelid;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getCurrentpage() {
		return currentpage;
	}

	public void setCurrentpage(int currentpage) {
		this.currentpage = currentpage;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public String getPicshape() {
		return picshape;
	}

	public void setPicshape(String picshape) {
		this.picshape = picshape;
	}

	public List<MVPlayInfo> getList() {
		return list;
	}

	public void setList(List<MVPlayInfo> list) {
		this.list = list;
	}

}
