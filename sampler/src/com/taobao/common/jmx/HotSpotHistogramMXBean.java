package com.taobao.common.jmx;

/**
 * @author shutong.dy
 * @Date 2011-11-24 10:25:19
 */
public interface HotSpotHistogramMXBean {
	/**
	 * @return
	 */
	public String getOwnPid();

	/**
	 * @return
	 */
	public String getHistogramText();
}
