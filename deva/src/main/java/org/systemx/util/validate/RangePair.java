package org.systemx.util.validate;

public class RangePair<Min, Max> {

	private Min min;
	private Max max;

	public RangePair() {
	}

	public RangePair(Min min, Max max) {
		setMin(min);
		setMax(max);
	}

	public Min getMin() {
		return min;
	}

	public RangePair setMin(Min min) {
		this.min = min;
		return this;
	}

	public Max getMax() {
		return max;
	}

	public RangePair setMax(Max max) {
		this.max = max;
		return this;
	}

}
