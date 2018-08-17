package cn.com.unary.initcopy.filecopy.fileresolver;

/**
 * 文件数据包解析器
 * @author shark
 *
 */
public interface Resolver {
	/**
	 * 解析文件数据包，如果解析器无法对应，则会抛 IlegalStateException
	 * @param data 文件数据包
	 */
	void process (byte[] data);
}
